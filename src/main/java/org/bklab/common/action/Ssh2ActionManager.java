/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-04-03 12:39:16
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.common.action.Ssh2ActionManager
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action;

import dataq.core.data.schema.Schema;
import org.bklab.common.action.parser.NetStatParser;
import org.bklab.common.data.SchemaFieldFactory;

import java.util.Arrays;
import java.util.List;

public class Ssh2ActionManager {

    public static List<Action> getActions() {
        return Arrays.asList(createNetStatAction());
    }


    public static Action createNetStatAction() {
        Schema schema = new Schema();
        schema.addField(SchemaFieldFactory.string("Proto").caption("协议").get());
        schema.addField(SchemaFieldFactory.intType("Recv-Q").caption("网络接收队列").get());
        schema.addField(SchemaFieldFactory.intType("Send-Q").caption("网络发送队列").get());
        schema.addField(SchemaFieldFactory.string("Local Address").caption("本地地址").get());
        schema.addField(SchemaFieldFactory.intType("Local Port").caption("本地端口").get());
        schema.addField(SchemaFieldFactory.string("Foreign Address").caption("远程地址").get());
        schema.addField(SchemaFieldFactory.intType("Foreign Port").caption("远程端口").get());
        schema.addField(SchemaFieldFactory.string("State").caption("状态").get());
        schema.addField(SchemaFieldFactory.intType("PID").caption("进程号").get());
        schema.addField(SchemaFieldFactory.string("Program name").caption("进程名").get());
        schema.addField(SchemaFieldFactory.string("Timer Status").caption("计时器状态").get());
        schema.addField(SchemaFieldFactory.doubleType("Timer Value").caption("时间值").get());
        schema.addField(SchemaFieldFactory.intType("retransmission").caption("重发次数").get());
        schema.addField(SchemaFieldFactory.intType("probe").caption("发送探测次数").get());

        schema.setSkiplines(2);
        schema.setDelimiter("\\s+");

        return new Action().setId(1).setName("打印网络连接信息")
                .setCommand("netstat -antoplu").setRecordParser(new NetStatParser(schema))
                .setDescription("打印TCP、UDP网络连接信息").setSchema(schema)
                .addSubAction(createPingAction())
                ;
    }

    private static Action createPingAction() {
        return new Action().setId(2).setParentId(1).setCommand("ping {{Foreign Address}} -c 4")
                .setName("查看网络延迟(Linux)").setDescription("Linux版查看网络延迟")
                .addParameter(new ActionParameter("Foreign Address").setCaption("目标地址")
                        .setValueValidator(s -> List.of("0.0.0.0", ":::").contains(s) ? "地址不能为本机环回" : null)
                );


    }
}
