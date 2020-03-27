/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-27 09:46:38
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.VaadinProfessionalAccessRequestHandler
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow;

import com.google.gson.JsonObject;
import com.vaadin.flow.server.RequestHandler;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;


public class VaadinProfessionalAccessRequestHandler implements RequestHandler {

    private static final String ACCESS_URL = "/bklab.org/access/vaadin/pro/";
    private final String baseUrl;


    public VaadinProfessionalAccessRequestHandler(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public boolean handleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (!pathInfo.startsWith(ACCESS_URL)) {
            return false;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", "broderick");
        jsonObject.addProperty("proKey", "BroderickLabs");

        try {
            String results = null;
            Class<?> vaadinComIntegration = Class.forName("com.vaadin.pro.licensechecker.VaadinComIntegration");
            Field field = vaadinComIntegration.getDeclaredField("BASE_URL");
            field.setAccessible(true);
            field.set(null, baseUrl + ACCESS_URL);
            return write(jsonObject.toString(), response);
        } catch (ClassNotFoundException e) {
            return write("not found class com.vaadin.pro.licensechecker.VaadinComIntegration, no need to access", response);
        } catch (NoSuchFieldException | SecurityException e) {
            return write("Not support in current version: NoSuchField or Security", response);
        } catch (IllegalAccessException e) {
            return write("Not support in current version: Illegal Access", response);
        }
    }

    private boolean write(String result, VaadinResponse response) throws IOException {
        try {

            response.setContentType("text/plain; version=0.0.4; charset=utf-8");
            response.setStatus(HttpURLConnection.HTTP_OK);
            response.getWriter().write(result);
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            response.sendError(HttpURLConnection.HTTP_INTERNAL_ERROR, writer.toString());
        }
        return true;
    }
}
