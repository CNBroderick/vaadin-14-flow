/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-04-10 14:31:54
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.common.ssh2.Ssh2Factory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.ssh2;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.bklab.element.HasExceptionConsumer;
import org.bklab.element.Identity;
import org.bklab.element.Server;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

public class Ssh2Factory implements HasExceptionConsumer<Ssh2Factory> {
    private final List<Consumer<Exception>> exceptionConsumers = new ArrayList<>();
    private final String address;
    private final int port;
    private final Identity identity;
    private final Properties config = new Properties();

    {
        config.put("StrictHostKeyChecking", "no");
    }

    public Ssh2Factory(Server server) {
        this.address = server.getAddress();
        this.port = server.getSshPort();
        this.identity = server.getIdentity();
    }

    public Ssh2Factory(String address, Identity identity) {
        this.address = address;
        this.port = 22;
        this.identity = identity;
    }

    public Ssh2Factory(String address, int port, Identity identity) {
        this.address = address;
        this.port = port;
        this.identity = identity;
    }

    public static Ssh2Factory createTest124() {
        Identity identity = new Identity();
        identity.updatePassword("Ronghai123?");
        identity.updateUsername("root");
        identity.setIdentityType(SshIdentityType.密码);
        return new Ssh2Factory("47.104.71.124", 22, identity);
    }

    public static Ssh2Factory createTest18() {
        Identity identity = new Identity();
        identity.updatePassword("Ronghai123?");
        identity.updateUsername("root");
        identity.setIdentityType(SshIdentityType.密码);
        return new Ssh2Factory("47.104.79.18", 22, identity);
    }

    public Session createSession(int timeoutMilliseconds, Consumer<JSchException> exceptionConsumer) {
        try {
            return createSession(timeoutMilliseconds);
        } catch (JSchException e) {
            exceptionConsumer.accept(e);
        }
        return null;
    }

    public Session createSession(int timeoutMilliseconds) throws JSchException {
        Function<String, byte[]> stringFunction = s -> s == null ? null : s.getBytes(StandardCharsets.UTF_8);
        JSch jsch = new JSch();
        if (identity.getIdentityType().isUsingKeychain()) {
            jsch.addIdentity("name", stringFunction.apply(identity.getKeychain()),
                    null, stringFunction.apply(identity.getPassphrase()));
        }
        Session session = jsch.getSession(identity.getUsername(), address);
        if (identity.getIdentityType().isUsingPassword()) {
            session.setPassword(identity.getPassword());
        }

        session.setConfig(config);
        session.connect(timeoutMilliseconds);
        return session;
    }

    public Ssh2Executor createSshExecutor(Consumer<JSchException> exceptionConsumer) {
        return new Ssh2Executor(createSession(60000, exceptionConsumer));
    }

    public SftpTransfer createSftpTransfer(Consumer<JSchException> exceptionConsumer) {
        return new SftpTransfer(createSession(60000, exceptionConsumer));
    }

    public SshShell createSshShell(Consumer<JSchException> exceptionConsumer) {
        return new SshShell(createSession(60000, exceptionConsumer));
    }

    public Ssh2Factory addConfig(Object key, Object value) {
        config.put(key, value);
        return this;
    }

    @Override
    public List<Consumer<Exception>> getExceptionConsumers() {
        return exceptionConsumers;
    }
}
