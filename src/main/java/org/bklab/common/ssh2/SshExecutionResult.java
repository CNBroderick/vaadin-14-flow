/*
 * Class: org.bklab.common.ssh2.SshExecutionResult
 * Modify date: 2020/3/20 上午10:58
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.ssh2;

import java.util.Objects;
import java.util.StringJoiner;

public class SshExecutionResult {

    private final String message;
    private boolean hasError;
    private String command;
    private String errorMessage;

    public SshExecutionResult(String message) {
        this.message = message;
    }

    public SshExecutionResult(boolean hasError, String message, String errorMessage) {
        this.hasError = hasError;
        this.message = message;
        this.errorMessage = errorMessage;
    }

    public static SshExecutionResult ok(String message) {
        return new SshExecutionResult(message);
    }

    public static SshExecutionResult error(String message, String errorMessage) {
        return new SshExecutionResult(true, message, errorMessage);
    }

    public boolean ok() {
        return !hasError;
    }

    public String getMessage() {
        return Objects.toString(hasError && message == null || message.strip().isBlank() ? errorMessage : message, "");
    }

    public String getErrorMessage() {
        return Objects.toString(errorMessage, "");
    }

    public String getCommand() {
        return command;
    }

    public SshExecutionResult setCommand(String command) {
        this.command = command;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SshExecutionResult.class.getSimpleName() + "{", "\n}")
                .add("\n\thasError: " + hasError)
                .add("\n\tcommand: '" + command + "'")
                .add("\n\tmessage: '" + message + "'")
                .add("\n\terrorMessage: '" + errorMessage + "'")
                .toString();
    }
}
