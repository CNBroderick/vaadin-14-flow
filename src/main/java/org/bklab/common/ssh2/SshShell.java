/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-04-10 14:31:54
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.common.ssh2.SshShell
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.ssh2;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.bklab.element.HasExceptionConsumer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SshShell implements HasExceptionConsumer<SshShell> {

    private final List<Consumer<Exception>> exceptionConsumers = new ArrayList<>();
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

    @Override
    public List<Consumer<Exception>> getExceptionConsumers() {
        return exceptionConsumers;
    }
}
