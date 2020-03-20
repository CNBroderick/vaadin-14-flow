/*
 * Class: org.bklab.common.action.parser.RecordResultParser
 * Modify date: 2020/3/20 上午10:58
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action.parser;

import dataq.core.data.schema.Record;
import dataq.core.data.schema.Schema;

public abstract class RecordResultParser implements ResultParser<Record> {
    protected Schema schema;

    public RecordResultParser(Schema schema) {
        this.schema = schema;
    }
}
