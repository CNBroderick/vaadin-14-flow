/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-04-22 10:43:15
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.common.request.LocalExecutor
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.request;

import dataq.core.operation.AbstractOperation;

public abstract class LocalExecutor implements IRequestExecutor {
    //执行前进一步设置;如 JDBC 连接等
    public abstract void beforeExecute(AbstractOperation op);

    @Override
    public Response execute(Request request) {
        try {
            Class<?> creator = request.getCreator();

            String className = creator.getPackage().getName() + "." + request.getOperationName();

            AbstractOperation op = Class.forName(className).asSubclass(AbstractOperation.class).getDeclaredConstructor().newInstance();

            op.setOperationName(request.getOperationName());
            request.getParams().forEach(op::setParam);
            beforeExecute(op);

            return Response.fromSuccess(op.execute());

        } catch (Exception e) {
            return Response.fromException(e);
        }
    }
}
