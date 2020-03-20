/*
 * Class: org.bklab.common.action.Action
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dataq.core.data.schema.Record;
import dataq.core.data.schema.Schema;
import org.bklab.common.action.convertor.ActionParameterJsonConverter;
import org.bklab.common.action.convertor.SchemaJsonConverter;
import org.bklab.common.action.parser.RecordResultParser;
import org.bklab.common.action.parser.ResultParser;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Action {

    private int id;
    private int parentId = 0;
    private String name;
    private ActionType type = ActionType.SHELL命令;
    private String command;
    private String description = "";

    private RawResultFormatter rawResultFormatter;
    private Schema schema;
    private ResultParser<Record> recordParser;
    private Map<String, ActionParameter> actionParameters = new HashMap<>();
    private List<Action> subActions = new ArrayList<>();
    private JsonObject data = new JsonObject();
    private ExecutionResultType resultType = ExecutionResultType.string;


    public Action() {
    }

    public ExecutionResult createResult() {
        return new ExecutionResult().setActionId(id).setSchema(schema);
    }

    public String createCommand() {
        String c = command;
        for (Map.Entry<String, ActionParameter> entry : actionParameters.entrySet()) {
            ActionParameter parameter = entry.getValue();
            String value = parameter == null ? "" : parameter.getValue();
            c = command.replaceAll("\\{\\{" + entry.getKey() + "}}", value);
            if (parameter == null) {
                throw new RuntimeException("请设置参数：" + entry.getKey());
            }
        }
        return c;
    }

    /**
     * 当参数值为空时，替换成空字符串
     *
     * @return 命令
     */
    public String createCommandQuiet() {
        String c = command;
        for (Map.Entry<String, ActionParameter> entry : actionParameters.entrySet()) {
            ActionParameter parameter = entry.getValue();
            String value = parameter == null ? "" : parameter.getValue();
            c = command.replaceAll("\\{\\{" + entry.getKey() + "}}", value);
        }
        return c;
    }

    public Action setParameter(ActionParameter parameter) {
        if (actionParameters.containsKey(parameter.getName())) actionParameters.put(parameter.getName(), parameter);
        else throw new IllegalArgumentException("无效的参数名：" + name);
        return this;
    }

    public Action setParameter(String name, String value) {
        if (actionParameters.containsKey(name)) actionParameters.get(name).setValue(value);
        else throw new IllegalArgumentException("参数名不存在：" + name);
        return this;
    }

    public Action addParameter(ActionParameter parameter) {
        actionParameters.put(parameter.getName(), parameter);
        return this;
    }

    public Action addSubAction(Action subAction) {
        this.subActions.add(subAction);
        return this;
    }

    public boolean hasSubAction() {
        return !subActions.isEmpty();
    }

    public boolean hasActionParameters() {
        return !actionParameters.isEmpty();
    }

    public String getSchemaJson() {
        return new SchemaJsonConverter().convert(schema);
    }

    public String getActionParametersJson() {
        ActionParameterJsonConverter converter = new ActionParameterJsonConverter();
        return actionParameters.values().stream().collect(JsonArray::new,
                (a, p) -> a.add(converter.toJsonElement(p)), JsonArray::addAll).toString();
    }

    public int getId() {
        return id;
    }

    public Action setId(int id) {
        if (id < 1) throw new RuntimeException("ssh2Action Id need beyond 0");
        this.id = id;
        return this;
    }

    public int getParentId() {
        return parentId;
    }

    public Action setParentId(int parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Action setName(String name) {
        this.name = name;
        return this;
    }

    public String getCommand() {
        return command;
    }

    public Action setCommand(String command) {
        this.command = command;
        return this;
    }

    public Schema getSchema() {
        return schema;
    }

    public Action setSchema(String json) {
        this.schema = new SchemaJsonConverter().convert(json);
        return this;
    }

    public Action setSchema(Schema schema) {
        this.schema = schema;
        return this;
    }

    public List<Action> getSubActions() {
        return subActions == null ? new ArrayList<>() : subActions;
    }

    public Action setSubAction(List<Action> subAction) {
        this.subActions = subAction;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Action setDescription(String description) {
        this.description = description;
        return this;
    }

    public Map<String, ActionParameter> getActionParameters() {
        return actionParameters;
    }

    public Action setActionParameters(String json) {
        ActionParameterJsonConverter converter = new ActionParameterJsonConverter();
        this.actionParameters = new HashMap<>();
        new Gson().fromJson(json, JsonArray.class).forEach(element -> addParameter(converter.fromJsonElement(element)));
        return this;
    }

    public Action setActionParameters(Map<String, ActionParameter> commandParameter) {
        this.actionParameters = commandParameter;
        return this;
    }

    public Action setActionParameters(List<ActionParameter> commandParameter) {
        this.actionParameters = commandParameter.stream().collect(Collectors.toMap(
                ActionParameter::getName, Function.identity()));
        return this;
    }

    public List<ActionParameter> getActionParameterList() {
        return new ArrayList<>(actionParameters.values());
    }

    public ActionParameter getActionParameter(String key) {
        return actionParameters.getOrDefault(key, new ActionParameter(key));
    }

    public ResultParser<Record> getRecordParser() {
        return recordParser;
    }

    public Action setRecordParser(String parser) {
        if (parser == null || parser.isBlank()) return this;
        try {
            recordParser = Class.forName(parser).asSubclass(RecordResultParser.class).getDeclaredConstructor(Schema.class).newInstance(schema);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Action setRecordParser(ResultParser<Record> recordParser) {
        this.recordParser = recordParser;
        return this;
    }

    public String getRecordParserClassName() {
        return recordParser == null ? null : recordParser.getClass().getName();
    }

    public String toBriefString() {
        return name + "[" + command + "]";
    }

    public String toBriefReplacedString() {
        return name + "[" + createCommandQuiet() + "]";
    }

    public ActionType getType() {
        return type;
    }

    public Action setType(ActionType type) {
        this.type = type;
        return this;
    }

    public Action setType(String type) {
        this.type = ActionType.valueOf(type);
        if (type == null) this.type = ActionType.SHELL命令;
        return this;
    }

    public JsonObject getData() {
        return data;
    }

    public Action setData(JsonObject data) {
        this.data = data;
        return this;
    }

    public Action setData(String data) {
        try {
            this.data = new Gson().fromJson(data, JsonObject.class);
        } catch (Exception e) {
            System.err.println("building.common.action.Action.setData(java.lang.String) 解析json失败：");
            e.printStackTrace();
        }
        return this;
    }

    public RawResultFormatter getRawResultFormatter() {
        return rawResultFormatter;
    }

    public Action setRawResultFormatter(String formatter) {
        if (formatter == null || formatter.isBlank()) return this;
        try {
            rawResultFormatter = Class.forName(formatter).asSubclass(RawResultFormatter.class).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Action setRawResultFormatter(RawResultFormatter rawResultFormatter) {
        this.rawResultFormatter = rawResultFormatter;
        return this;
    }

    public String getRawResultFormatterClassName() {
        return rawResultFormatter == null ? null : rawResultFormatter.getClass().getName();
    }

    public ExecutionResultType getResultType() {
        return resultType;
    }

    public Action setResultType(ExecutionResultType resultType) {
        this.resultType = resultType;
        return this;
    }

    public String getResultTypeName() {
        return resultType == null ? ExecutionResultType.string.name() : resultType.name();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Action.class.getSimpleName() + "{", "\n}")
                .add("\n\tid: " + id)
                .add("\n\tparentId: " + parentId)
                .add("\n\tname: '" + name + "'")
                .add("\n\ttype: " + type)
                .add("\n\tcommand: '" + command + "'")
                .add("\n\tdescription: '" + description + "'")
                .add("\n\trawResultFormatter: " + rawResultFormatter)
                .add("\n\tschema: " + schema)
                .add("\n\trecordParser: " + recordParser)
                .add("\n\tactionParameters: " + actionParameters)
                .add("\n\tsubActions: " + subActions)
                .add("\n\tdata: " + data)
                .add("\n\tresultType: " + resultType)
                .toString();
    }
}
