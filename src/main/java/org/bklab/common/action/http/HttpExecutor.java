/*
 * Class: org.bklab.common.action.http.HttpExecutor
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bklab.common.action.Action;
import org.bklab.common.action.ActionExecutor;
import org.bklab.common.action.ActionParameter;
import org.bklab.common.action.ExecutionResult;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class HttpExecutor implements ActionExecutor {

    @Override
    public ExecutionResult execute(Action action) {
        ExecutionResult executionResult = new ExecutionResult().setActionId(action.getId())
                .setRawResultFormatter(action.getRawResultFormatter()).setResultType(action.getResultType());
        String command = action.getCommand();
        try {
            String method = getData(action.getData(), "RequestMethod", JsonElement::getAsString, "GET");
            if (!"POST".equals(method) && action.hasActionParameters())
                command += "?" + createParameterUrl(action.getActionParameters());

            URL url = new URL(command);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            initConnection(connection, action.getData());

            if ("POST".equals(method)) {
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.write(createParameterUrl(action.getActionParameters()).getBytes(StandardCharsets.UTF_8));
                out.flush();
                out.close();
            }

            connection.connect();

/*            Map<String, List<String>> headers = connection.getHeaderFields();
            for (String key : headers.keySet()) {
                System.out.println(key + "--->" + headers.get(key));
            }*/

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            String getLine;
            while ((getLine = in.readLine()) != null) {
                result.append(getLine);
            }
            in.close();

            return executionResult.setCommand(command).setSuccess(true).setRawResult(result.toString())
                    .setFinishTime()
                    .setSchema(action.getSchema())
                    .parseRecordIfAvailable(action.getRecordParser());
        } catch (Exception e) {
            return executionResult.setCommand(command).setError(e.getLocalizedMessage()).setFinishTime()
                    .setThrowable(e).setSuccess(false);

        }
    }

    private String createParameterUrl(Map<String, ActionParameter> actionParameters) {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, ActionParameter> entry : actionParameters.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue().getValue(), StandardCharsets.UTF_8));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

    private void initConnection(HttpURLConnection connection, JsonObject data) throws Exception {
        connection.setRequestMethod(getData(data, "RequestMethod", JsonElement::getAsString, "GET"));
        connection.setConnectTimeout(getData(data, "ConnectTimeout", JsonElement::getAsInt, 10000));
        connection.setReadTimeout(getData(data, "ReadTimeout", JsonElement::getAsInt, 10000));

        if (data.has("RequestProperty") && !data.get("RequestProperty").isJsonNull()) {
            JsonObject object = data.get("RequestProperty").getAsJsonObject();
            for (String s : object.keySet()) {
                connection.setRequestProperty(s, object.get(s).getAsString());
            }
        }

        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
    }

    private <E> E getData(JsonObject data, String name, Function<JsonElement, E> function, E defaultValue) {
        return data.has(name) && !data.isJsonNull() ? function.apply(data.get(name)) : defaultValue;
    }

    private void setData(JsonObject data, String name, Function<JsonElement, String> elementConsumer, Consumer<String> consumer) {
        if (data.has(name) && !data.isJsonNull()) consumer.accept(elementConsumer.apply(data.get(name)));
    }
}
