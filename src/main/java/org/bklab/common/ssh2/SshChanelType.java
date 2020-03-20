/*
 * Class: org.bklab.common.ssh2.SshChanelType
 * Modify date: 2020/3/20 上午10:58
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.ssh2;

public enum SshChanelType {

    SESSION("session"),
    SHELL("shell"),
    EXEC("exec"),
    X11("x11"),
    AGENT_FORWARDING("auth-agent@openssh.com"),
    DIRECT_TCP_IP("direct-tcpip"),
    FORWARDED_TCP_IP("forwarded-tcpip"),
    SFTP("sftp"),
    SUBSYSTEM("subsystem"),
    ;

    private final String value;

    SshChanelType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}