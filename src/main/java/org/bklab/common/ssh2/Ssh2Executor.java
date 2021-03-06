/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-04-10 14:31:54
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.common.ssh2.Ssh2Executor
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.ssh2;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import org.bklab.common.action.Action;
import org.bklab.common.action.ActionExecutor;
import org.bklab.common.action.ExecutionResult;
import org.bklab.element.HasExceptionConsumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Ssh2Executor implements ActionExecutor, HasExceptionConsumer<Ssh2Executor> {
    private final List<Consumer<Exception>> exceptionConsumers = new ArrayList<>();
    private final Session session;

    public Ssh2Executor(Session session) {
        this.session = session;
    }

    public SshExecutionResult execute(String command) throws Exception {
        ChannelExec channelExec = (ChannelExec) session.openChannel(SshChanelType.EXEC.getValue());
        channelExec.setCommand(command);
        channelExec.setInputStream(null);
        channelExec.connect();

        String result = getChannelOutput(channelExec, channelExec.getInputStream());
        String error = getChannelOutput(channelExec, channelExec.getErrStream());

        channelExec.disconnect();
        return (error.isBlank() ? SshExecutionResult.ok(result)
                : SshExecutionResult.error(error, result)).setCommand(command);
    }

    @Override
    public ExecutionResult execute(Action action) {
        String cmd = action.createCommand();
        String result = "";
        String error = "";
        try {
            ChannelExec channelExec = (ChannelExec) session.openChannel(SshChanelType.EXEC.getValue());

            channelExec.setCommand(cmd);
            channelExec.setInputStream(null);
            channelExec.connect();

            result = getChannelOutput(channelExec, channelExec.getInputStream());
            error = getChannelOutput(channelExec, channelExec.getErrStream());

            channelExec.disconnect();

            return action.createResult().setCommand(cmd)
                    .setError(error.isBlank() ? null : error)
                    .setRawResult(result)
                    .parseRecordIfAvailable(action.getRecordParser())
                    .setSuccess(true)
                    .setThrowable(null)
                    ;
        } catch (Exception e) {
            return action.createResult().setCommand(cmd).setThrowable(e).setError(error).setRawResult(result);
        }
    }

    private String getChannelOutput(Channel channel, InputStream in) throws IOException {

        byte[] buffer = new byte[1024];
        StringBuilder strBuilder = new StringBuilder();

        while (true) {
            while (in.available() > 0) {
                int i = in.read(buffer, 0, 1024);
                if (i < 0) {
                    break;
                }
                strBuilder.append(new String(buffer, 0, i));
            }

            if (channel.isClosed()) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception ignore) {
            }
        }
        return strBuilder.toString();
    }

    public void sessionDisconnected() {
        session.disconnect();
    }

    @Override
    public List<Consumer<Exception>> getExceptionConsumers() {
        return exceptionConsumers;
    }
}
