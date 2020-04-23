/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-04-22 10:43:15
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.common.request.SimpleHttpExecutor
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.request;

import dataq.core.operation.OperationContext;
import dataq.core.operation.OperationResult;

public abstract class SimpleHttpExecutor implements IRequestExecutor {
	private String serverUrl;
	private String mime;

	public String getMime() {
		if (mime == null) return "text/plain;charset=UTF-8";
		return mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public abstract String postForObject(String postData) throws Exception;


	public Response execute(Request request) {
		try {
			//OperationContext 需要在在网络上传输
			OperationContext context = new OperationContext();
			//将 request 信息转换成 context;context
			request.getParams().forEach(context::setParam);
			context.setOperationName(request.getOperationName());
			String strContext = context.toZippedXML();

			String strResult = postForObject(strContext);

			return Response.fromOperationResult(OperationResult.fromZippedXML(strResult));
		} catch (Exception e) {
			return Response.fromException(e);
		}
	}


}
