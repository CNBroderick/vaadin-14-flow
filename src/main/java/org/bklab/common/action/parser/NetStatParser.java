/*
 * Class: org.bklab.common.action.parser.NetStatParser
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action.parser;

import dataq.core.data.schema.Record;
import dataq.core.data.schema.Schema;
import org.bklab.util.StringExtractor;

import java.util.List;

public class NetStatParser extends RecordResultParser {

    public NetStatParser(Schema schema) {
        super(schema);
    }
    /*
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
     */

    //tcp        0      0 0.0.0.0:6263            0.0.0.0:*               LISTEN      28560/haproxy        off (0.00/0/0)
    @Override
    public Record apply(String source) {
        Record record = new Record(schema);
        String[] c = source.split("\\s+");
        int index = 0;

        record.setString("Proto", c[index++]);
        record.setNumber("Recv-Q", StringExtractor.parseInt(c[index++]));
        record.setNumber("Send-Q", StringExtractor.parseInt(c[index++]));

        String la = c[index];
        int laIndex = la.lastIndexOf(':');
        record.setString("Local Address", laIndex > 0 ? la.substring(0, laIndex) : c[index]);
        record.setNumber("Local Port", laIndex > 0 ? StringExtractor.parseInt(la.substring(laIndex)) : 0);

        String fa = c[++index];
        int faIndex = fa.lastIndexOf(':');
        record.setString("Foreign Address", faIndex > 0 ? fa.substring(0, faIndex) : c[index]);
        record.setNumber("Foreign Port", faIndex > 0 ? StringExtractor.parseInt(fa.substring(faIndex)) : 0);

        record.setString("State", List.of("udp", "udp6").contains(c[0]) ? "" : c[++index]);

        String c6 = c[++index];
        int indexOfC6 = c6.indexOf('/');
        record.setNumber("PID", indexOfC6 > 0 ? StringExtractor.parseInt(c6.substring(0, indexOfC6)) : -1);
        StringBuilder pn = new StringBuilder(indexOfC6 > 0 ? c6.substring(indexOfC6 + 1) : c6);
        for (int i = ++index; i < c.length - 3; i++) {
            pn.append(" ").append(c[index]);
        }
        record.setString("Program name", pn.toString());

        record.setString("Timer Status", c[c.length - 2]);
        String[] c8 = c[c.length - 1].replaceAll("\\(", "").replaceAll("\\)", "").split("/");

        record.setNumber("Timer Value", StringExtractor.parseDouble(c8[0]));
        record.setNumber("retransmission", StringExtractor.parseInt(c8[1]));
        record.setNumber("probe", StringExtractor.parseInt(c8[2]));
        return record;
    }
}
