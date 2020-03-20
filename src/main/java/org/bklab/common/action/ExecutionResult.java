/*
 * Class: org.bklab.common.action.ExecutionResult
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action;

import dataq.core.data.schema.Record;
import dataq.core.data.schema.Schema;
import org.bklab.common.action.parser.ConvertError;
import org.bklab.common.action.parser.ResultParser;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class ExecutionResult {

    private int actionId;
    private int resultId;
    private String command;
    private boolean isSuccess;
    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime finishTime = LocalDateTime.now();
    private RawResultFormatter rawResultFormatter;
    private ExecutionResultType resultType = ExecutionResultType.string;
    private String rawResult;
    private Schema schema;
    private List<Record> records;
    private List<ConvertError> convertErrors = new ArrayList<>();
    private String error = null;
    private Throwable throwable = null;

    public ExecutionResult() {
    }

    public ExecutionResult parseRecordIfAvailable(ResultParser<Record> recordParser) {
        return schema != null ? parseRecord(recordParser) : this;
    }

    public ExecutionResult parseRecord(ResultParser<Record> recordParser) {
        if (schema == null) throw new RuntimeException("schema is null");
        if (recordParser == null) return this;
        records = rawResult.lines().skip(schema.getSkiplines()).map(s -> parseOneRecord(s, recordParser))
                .filter(Objects::nonNull).collect(Collectors.toList());
        return this;
    }

    public String createExecuteSpendTime() {
        Duration d = Duration.between(startTime, finishTime).abs();
        long seconds = d.getSeconds();
        String t;
        if (seconds > 3600) {
            t = String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
        } else if (seconds > 60) {
            t = String.format("%02d:%02d", (seconds % 3600) / 60, seconds % 60);
        } else {
            t = seconds + "." + d.getNano() + " 秒";
        }
        return t;
    }

    private Record parseOneRecord(String source, ResultParser<Record> recordParser) {
        Record record = null;
        try {
            record = recordParser.apply(source);
        } catch (Exception e) {
            convertErrors.add(new ConvertError(source, e.getLocalizedMessage(), e));
        }
        if (record == null && source != null && !source.isBlank())
            convertErrors.add(new ConvertError(source, "转换到空值", null));

        return record;
    }

    public int getActionId() {
        return actionId;
    }

    public ExecutionResult setActionId(int actionId) {
        this.actionId = actionId;
        return this;
    }

    public String getCommand() {
        return command;
    }

    public ExecutionResult setCommand(String command) {
        this.command = command;
        return this;
    }

    public Schema getSchema() {
        return schema;
    }

    public ExecutionResult setSchema(Schema schema) {
        this.schema = schema;
        return this;
    }

    public String getRawResult() {
        return rawResult;
    }

    public ExecutionResult setRawResult(String rawResult) {
        try {
            this.rawResult = rawResultFormatter != null ? rawResultFormatter.format(rawResult) : rawResult;
        } catch (Exception e) {
            e.printStackTrace();
            this.rawResult = rawResult;
        }
        return this;
    }

    public List<Record> getRecords() {
        return records;
    }

    public ExecutionResult setRecords(List<Record> records) {
        this.records = records;
        return this;
    }

    public String getError() {
        return Objects.toString(error, getThrowableString());
    }

    public ExecutionResult setError(String error) {
        this.error = error;
        return this;
    }

    public boolean hasError() {
        return error != null || throwable != null;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public ExecutionResult setThrowable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    public String getThrowableString() {
        if (throwable == null) return "无";
        StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public ExecutionResult setSuccess(boolean success) {
        isSuccess = success;
        return this;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public ExecutionResult setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
        return this;
    }

    public ExecutionResult setFinishTime() {
        this.finishTime = LocalDateTime.now();
        return this;
    }

    public List<ConvertError> getConvertErrors() {
        return convertErrors;
    }

    public ExecutionResult setConvertErrors(List<ConvertError> convertErrors) {
        this.convertErrors = convertErrors;
        return this;
    }

    public int getResultId() {
        return resultId;
    }

    public ExecutionResult setResultId(int resultId) {
        this.resultId = resultId;
        return this;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public ExecutionResult setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public RawResultFormatter getRawResultFormatter() {
        return rawResultFormatter;
    }

    public ExecutionResult setRawResultFormatter(RawResultFormatter rawResultFormatter) {
        this.rawResultFormatter = rawResultFormatter;
        return this;
    }

    public ExecutionResultType getResultType() {
        return resultType;
    }

    public ExecutionResult setResultType(ExecutionResultType resultType) {
        this.resultType = resultType;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ExecutionResult.class.getSimpleName() + "{", "\n}")
                .add("\n\tactionId: " + actionId)
                .add("\n\tresultId: " + resultId)
                .add("\n\tcommand: '" + command + "'")
                .add("\n\tisSuccess: " + isSuccess)
                .add("\n\tfinishTime: " + finishTime)
                .add("\n\tresultType: " + resultType)
                .add("\n\trawResult: '" + rawResult + "'")
                .add("\n\tschema: " + schema)
                .add("\n\trecords: " + records)
                .add("\n\terror: '" + error + "'")
                .add("\n\tthrowable: " + throwable)
                .toString();
    }

}
