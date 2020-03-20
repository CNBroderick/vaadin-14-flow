/*
 * Class: org.bklab.entity.multi.SqlScriptExecutor
 * Modify date: 2020/3/20 上午11:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.entity.multi;

import dataq.core.Script;
import dataq.core.data.schema.Recordset;
import dataq.core.jdbc.DBAccess;
import dataq.core.jdbc.JdbcDatasource;
import dataq.core.jdbc.ResultSetHelper;
import dataq.util.ExceptionUtil;
import dataq.util.StringUtil;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlScriptExecutor {
    private boolean errorContinue = Boolean.TRUE;
    private String delimiter = ";";
    private JdbcDatasource dataSource;

    public String getDelimiter() {
        return delimiter;
    }

    public SqlScriptExecutor setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }


    public JdbcDatasource getDataSource() {
        return dataSource;
    }

    public SqlScriptExecutor setDataSource(JdbcDatasource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public boolean isErrorContinue() {
        return errorContinue;
    }

    public SqlScriptExecutor setErrorContinue(boolean errorContinue) {
        this.errorContinue = errorContinue;
        return this;
    }

    public String executeScript(File srcScript) {
        String strContent = Script.fromFile(srcScript).getContent();
        return executeScript(strContent);
    }

    public String executeScript(String strContent) {
        StringBuffer outbuf = new StringBuffer();
        DBAccess dbAccess = null;
        try {
            dbAccess = dataSource.createDBAccess();

        } catch (Exception e) {
            return "数据库连接失败!" + ExceptionUtil.statckStaceToString(e);
        }

        for (String sql : buildSqlStatements(strContent)) {
            try {
                Statement stmt = dbAccess.getConnection().createStatement();
                boolean ret = stmt.execute(sql);
                if (ret) {
                    ResultSet rs = stmt.getResultSet();
                    ResultSetHelper helper = new ResultSetHelper(rs);
                    Recordset rset = helper.asRecordset();
                    outbuf.append("\n执行成功,rows=" + rset.size() + ",[" + sql + "]");
                    outbuf.append("\n").append(rset.toString());
                } else {
                    int updateCount = stmt.getUpdateCount();
                    outbuf.append("\n执行成功,updateCount=" + updateCount + ",[" + sql + "]");
                }
            } catch (Exception e) {
                outbuf.append("\n执行失败[" + sql + "],错误信息:" + e.getLocalizedMessage());
                if (errorContinue) continue;
                break;
            }
        }
        DBAccess.release(dbAccess);
        return outbuf.toString();
    }

    public String executeScriptBak(String strContent) {
        StringBuffer outbuf = new StringBuffer();
        DBAccess dbAccess = null;
        try {
            dbAccess = dataSource.createDBAccess();

        } catch (Exception e) {
            return "数据库连接失败!" + ExceptionUtil.statckStaceToString(e);
        }

        for (String sql : buildSqlStatements(strContent)) {
            try {
                int ret = dbAccess.execute(sql);
                outbuf.append("\n执行成功,ret=" + ret + ",[" + sql + "]");
            } catch (Exception e) {
                outbuf.append("\n执行失败[" + sql + "],错误信息:" + e.getLocalizedMessage());
                if (errorContinue) continue;
                break;
            }
        }
        DBAccess.release(dbAccess);
        return outbuf.toString();
    }

    private List<String> buildSqlStatements(String strContent) {
        List<String> sqls = new ArrayList<>();

        for (String tmpSql : StringUtil.split(strContent, ";")) {
            sqls.add(tmpSql);
        }
        return sqls;
    }

}
