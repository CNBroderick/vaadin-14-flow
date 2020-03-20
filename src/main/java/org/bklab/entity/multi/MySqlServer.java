/*
 * Class: org.bklab.entity.multi.MySqlServer
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.entity.multi;

import dataq.Dataq;
import dataq.core.Script;
import dataq.core.data.schema.Record;
import dataq.core.data.schema.Recordset;
import dataq.core.jdbc.DBAccess;
import dataq.core.jdbc.JdbcDatasource;
import dataq.core.xml.XmlObject;
import dataq.util.FileUtil;
import org.bklab.entity.HasLabels;
import org.bklab.util.SM4Util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

public class MySqlServer implements HasLabels<MySqlServer> {
    public final static String MYSQL_JDBC_Driver = "org.gjt.mm.mysql.Driver";
    public final static String MYSQL_URL_PARAMETERS = "useSSL=false&useUnicode=yes&characterEncoding=UTF-8";
    public final static String MYSQL_INFORMATION_SCHEMA = "INFORMATION_SCHEMA";
    public final static String MYSQL_SHOW_DATABASES = "show databases";
    public final static String MYSQL_CREATE_DATABASE = "create database ";
    private final static String IV = "04bc7072885c7b9fd4ba69a1457cc2c1";

    private int id;
    private String name;
    private String ip;
    private int port = 3306;
    private String password;
    private String user;
    private Set<String> labels = new LinkedHashSet<>();

    public static DBAccess createMasterDBAccess() {

        return createDBAccess("building-master-mysql");
    }

    public static DBAccess createRootDBAccess() {
        return createDBAccess("building-master-mysql-root");
    }

    public static DBAccess createDBAccess(String name) {
        try {
            for (XmlObject x : XmlObject.fromFile(Dataq.getDataqHome() + "/etc/jdbc.xml").children("jdbcdatasource")) {
                if (x.getString("name").equals(name)) {
                    JdbcDatasource ds = new JdbcDatasource();
                    String user = x.getString("user");
                    ds.setUser(Objects.toString(SM4Util.decode(user, IV), user));
                    String password = x.getString("password");
                    ds.setPassword(Objects.toString(SM4Util.decode(password, IV), password));
                    ds.setDriver(x.getString("driver"));
                    ds.setUrl(x.getString("url"));
                    return ds.createDBAccess();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("未找到数据源：" + name);
    }

    public String getIp() {
        return ip;
    }

    public MySqlServer setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public int getPort() {
        return port;
    }

    public MySqlServer setPort(int port) {
        this.port = port;
        return this;
    }

    public String getUser() {
        return user;
    }

    public MySqlServer setUser(String user) {
        this.user = Objects.toString(SM4Util.decode(user, IV), user);
        return this;
    }

    public String getEncodeUser() {
        return SM4Util.encode(user, IV);
    }

    public String getPassword() {
        return password;
    }

    public MySqlServer setPassword(String password) {
        this.password = Objects.toString(SM4Util.decode(password, IV), password);
        return this;
    }

    public String getEncodePassword() {
        return SM4Util.encode(password, IV);
    }

    public Script createShellScript(File sqlFile) {

        String strContent = Script.fromFile(sqlFile).getContent();
        //去掉特殊字符
        strContent = strContent.replace('`', ' ');

        Script script = new Script();

        script.setName(FileUtil.getFileNameOnly(sqlFile.getAbsolutePath()) + ".sh");

        String sb = "mysql -u" +
                getUser() +
                " -p" + getPassword() +
                " 2>&1 << EOF\n" +
                strContent +
                "\nEOF";
        script.setContent(sb);
        return script;

    }

    public SqlScriptExecutor createScriptExecutor(String dbName) {
        return new SqlScriptExecutor().setDataSource(createJdbcDataSource(dbName));
    }

    private String createUrl(String dbName) {
        return "jdbc:mysql://" + getIp() + ":" + getPort() + "/" + dbName + '?' + MYSQL_URL_PARAMETERS;
    }

    public Boolean test() throws Exception {
        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        Connection connection = DriverManager.getConnection("jdbc:mysql://" + getIp() + ":" + getPort() + '?' + MYSQL_URL_PARAMETERS, user, password);
        boolean result = connection.isValid(60);
        connection.close();
        return result;
    }

    public Boolean silenceTest() {
        try {
            return test();
        } catch (Exception e) {
            return false;
        }
    }

    public Connection connect() throws Exception {
        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        return DriverManager.getConnection("jdbc:mysql://" + getIp() + ":" + getPort() + '?' + MYSQL_URL_PARAMETERS, user, password);
    }

    public JdbcDatasource createJdbcDataSource(String dbName) {
        if (user == null) throw new RuntimeException("当前租户未设置数据库账户，请联系平台管理员。");
        if (password == null) throw new RuntimeException("当前租户未设置数据库密码，请联系平台管理员。");
        if (dbName == null) throw new RuntimeException("当前租户未设置所使用的数据库名称，请联系平台管理员。");
        JdbcDatasource ds = new JdbcDatasource(dbName);
        ds.setUser(user);
        ds.setPassword(password);
        ds.setDriver(MYSQL_JDBC_Driver);
        ds.setUrl(createUrl(dbName));
        return ds;
    }


    public List<String> getDatabaseNames() {
        JdbcDatasource ds = createJdbcDataSource(MYSQL_INFORMATION_SCHEMA);
        List<String> dbnames = new ArrayList<>();
        DBAccess db = null;
        try {
            db = ds.createDBAccess();
            Recordset rset = db.queryForRecordset(MYSQL_SHOW_DATABASES);
            for (Record rec : rset.getRecords()) {
                dbnames.add(rec.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBAccess.release(db);
        }
        return dbnames;
    }

    public void dropDatabase(String dbName) {
        JdbcDatasource ds = createJdbcDataSource(MYSQL_INFORMATION_SCHEMA);

        DBAccess db = null;
        try {
            db = ds.createDBAccess();
            db.execute("drop  database " + dbName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBAccess.release(db);
        }
    }

    public boolean createDatabase(String dbName) {
        JdbcDatasource ds = createJdbcDataSource(MYSQL_INFORMATION_SCHEMA);

        DBAccess db = null;
        try {
            db = ds.createDBAccess();
            String sql = "CREATE DATABASE " + dbName + "  DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci";
            db.execute(sql);
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        } finally {
            DBAccess.release(db);
        }

    }

    public String toUrl() {
        return user + "@" + ip + ":" + port;
    }

    public int getId() {
        return id;
    }

    public MySqlServer setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MySqlServer setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Set<String> getLabels() {
        return labels;
    }

    @Override
    public MySqlServer setLabels(Set<String> labels) {
        this.labels = labels;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MySqlServer.class.getSimpleName() + "{", "\n}")
                .add("\n\tname: '" + name + "'")
                .add("\n\tip: '" + ip + "'")
                .add("\n\tport: " + port)
                .add("\n\tuser: '" + user + "'")
                .add("\n\tlabel: '" + toLabelJson() + "'")
                .toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
