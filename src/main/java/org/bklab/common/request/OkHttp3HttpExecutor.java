/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-04-23 10:06:30
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.common.request.OkHttp3HttpExecutor
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.request;

import dataq.core.httpclient.HttpPostClient;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.util.Optional;

public class OkHttp3HttpExecutor extends SimpleHttpExecutor {
    @Override
    public String postForObject(String postData) throws Exception {
        HttpPostClient client = new HttpPostClient(getServerUrl());
        client.setPostData(postData);
        client.setMime(getMime());
        ResponseBody responseBody = Optional.ofNullable(client.doRequest()).map(Response::body).orElse(null);
        return responseBody == null ? null : responseBody.string();
    }


}
