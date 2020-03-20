/*
 * Class: org.bklab.common.ssh2.SshShell
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.ssh2;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.bklab.element.HasExceptionConsumers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class SshShell extends HasExceptionConsumers<SshShell> {
    private final Session session;
    private final ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{});
    private ChannelShell shell;
    private ByteArrayOutputStream out = new ByteArrayOutputStream();


    public SshShell(Session session) {
        this.session = session;
        init();
    }

    private void init() {
        try {
            this.shell = (ChannelShell) session.openChannel(SshChanelType.SHELL.getValue());
            shell.setOutputStream(null);
            shell.setInputStream(in);
            shell.setOutputStream(out);
            shell.connect(5000);
        } catch (JSchException e) {
            callExceptionConsumers(e);
        }
    }

    public SshExecutionResult sendCommand(String command) {
        try {
            in.read(command.getBytes(StandardCharsets.UTF_8));
            out = new ByteArrayOutputStream();
            shell.setOutputStream(out);
            return SshExecutionResult.ok(out.toString(StandardCharsets.UTF_8)).setCommand(command);
        } catch (Exception e) {
            return SshExecutionResult.error("no message", e.getLocalizedMessage()).setCommand(command);
        }

    }

}
