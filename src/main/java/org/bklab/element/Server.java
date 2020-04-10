/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-04-10 14:39:35
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.element.Server
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.element;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import org.bklab.common.ssh2.Ssh2Factory;
import org.bklab.common.ssh2.SshIdentityType;
import org.bklab.entity.HasLabels;
import org.bklab.util.DigitalFormatter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.function.Consumer;

public class Server implements HasLabels<Server> {

    private int id;
    private String hostName;
    private String address;
    private int sshPort = 22;

    private Identity identity = new Identity();
    private String description;
    private JsonObject data = new JsonObject();
    private Set<String> labels = new LinkedHashSet<>();


    private long lastPing = -1;

    public static boolean checkKeyValid(String keychain, String passphrase) {
        try {
            new JSch().addIdentity(keychain, passphrase);
            return true;
        } catch (JSchException e) {
            return false;
        }
    }

    public long ping() throws IOException {
        long t0 = System.currentTimeMillis();
        if (InetAddress.getByName(address).isReachable(60 * 1000)) {
            lastPing = System.currentTimeMillis() - t0;
            return System.currentTimeMillis() - t0;
        }
        return -1;
    }

    public String testPing() {
        long t0 = System.currentTimeMillis();
        try {
            if (InetAddress.getByName(address).isReachable(9999)) {
                lastPing = System.currentTimeMillis() - t0;
                return new DigitalFormatter(lastPing).toFormatted() + " ms";
            }
        } catch (IOException e) {
            return "无法连接";
        }
        lastPing = 9999;
        return ">9999 ms";
    }

    public long ping(int maxWaitingMilliSecond) {
        long t0 = System.currentTimeMillis();
        try {
            if (InetAddress.getByName(address).isReachable(maxWaitingMilliSecond)) {
                return System.currentTimeMillis() - t0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return maxWaitingMilliSecond;
    }

    @SafeVarargs
    public final boolean testConnectSsh(int timeoutMilliseconds, Consumer<Exception>... exceptionConsumers) {
        try {
            new Ssh2Factory(address, sshPort, identity).addExceptionConsumers(Arrays.asList(exceptionConsumers))
                    .createSession(timeoutMilliseconds).disconnect();
            return true;
        } catch (JSchException e) {
            Arrays.stream(exceptionConsumers).forEach(exceptionConsumer -> exceptionConsumer.accept(e));
        }
        return false;
    }

    public boolean isActiveSshPort(int maxWaitingMilliSecond) {
        return isPortActive(sshPort, maxWaitingMilliSecond);
    }

    public boolean isPortActive(int port, int maxWaitingMilliSecond) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(address, port), maxWaitingMilliSecond);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String getUrl() {
        return identity.getUsername() + "@" + address + ":" + sshPort;
    }

    public int getId() {
        return id;
    }

    public Server setId(int id) {
        this.id = id;
        return this;
    }

    public String getHostName() {
        return hostName;
    }

    public Server setHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public String getEncodeUsername() {
        return identity.getEncodeUsername();
    }

    public Server setEncodeUsername(String userName) {
        identity.setUsername(userName);
        return this;
    }

    public String getPassword() {
        return identity.getPassword();
    }

    public Server setPassword(String password) {
        identity.updatePassword(password);
        return this;
    }

    public String getKeychain() {
        return identity.getKeychain();
    }

    public Server setKeychain(String keychain) {
        identity.updateKeychain(keychain);
        return this;
    }

    public String getPassphrase() {
        return identity.getPassphrase();
    }

    public Server setPassphrase(String passphrase) {
        identity.updatePassphrase(passphrase);
        return this;
    }

    public String getEncodePassword() {
        return identity.getEncodePassword();
    }

    public Server setEncodePassword(String password) {
        identity.setPassword(password);
        return this;
    }

    public String getEncodeKeychain() {
        return identity.getEncodeKeychain();
    }

    public Server setEncodeKeychain(String keychain) {
        identity.setKeychain(keychain);
        return this;
    }

    public String getEncodePassphrase() {
        return identity.getEncodePassphrase();
    }

    public Server setEncodePassphrase(String passphrase) {
        identity.setPassphrase(passphrase);
        return this;
    }

    public boolean isUsingPassword() {
        return identity.getIdentityType().isUsingPassword();
    }

    public boolean isUsingKeychain() {
        return identity.getIdentityType().isUsingKeychain();
    }

    public SshIdentityType getIdentityType() {
        return identity.getIdentityType();
    }

    public Server setIdentityType(SshIdentityType identityType) {
        identity.setIdentityType(identityType);
        return this;
    }

    public String getIdentityTypeName() {
        return identity.getIdentityType() == null ? null : identity.getIdentityType().name();
    }

    public String getAddress() {
        return address;
    }

    public Server setAddress(String address) {
        this.address = address;
        return this;
    }

    public int getSshPort() {
        return sshPort;
    }

    public Server setSshPort(int sshPort) {
        this.sshPort = sshPort;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Server setDescription(String description) {
        this.description = description;
        return this;
    }

    public long getLastPing() {
        return lastPing;
    }

    public Identity getIdentity() {
        return identity;
    }

    public Server setIdentity(Identity identity) {
        this.identity = identity;
        return this;
    }

    public String getUserName() {
        return identity.getUsername();
    }

    public Server setUserName(String userName) {
        identity.updateUsername(userName);
        return this;
    }

    public JsonObject getData() {
        return data;
    }

    public Server setData(String data) {
        try {
            this.data = new Gson().fromJson(Objects.toString(data, ""), JsonObject.class);
        } catch (Exception e) {
            this.data = new JsonObject();
        }
        return this;
    }

    public Server setData(JsonObject data) {
        this.data = data;
        return this;
    }

    public String getDataJson() {
        return (data == null ? new JsonObject() : data).toString();
    }

    @Override
    public Set<String> getLabels() {
        return labels;
    }

    @Override
    public Server setLabels(Set<String> labels) {
        this.labels = labels;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Server.class.getSimpleName() + "{", "\n}")
                .add("\n\tid: " + id)
                .add("\n\tname: '" + hostName + "'")
                .add("\n\taddress: '" + address + "'")
                .add("\n\tdesc: '" + description + "'")
                .add("\n\tlabel: '" + toLabelJson() + "'")
                .toString();
    }
}
