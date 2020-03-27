/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-27 10:47:02
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.common.data.CsvRecordDataFunction
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.data;

import dataq.core.data.schema.Record;
import dataq.core.data.schema.Schema;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.bklab.common.ssh2.SchemaFieldFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CsvRecordDataFunction implements IRecordDataFunction<CsvRecordDataFunction> {

    private final List<Consumer<Exception>> exceptionConsumers = new ArrayList<>();

    private StringReader createReader(Path path) throws Exception {

        List<String> strings;
        try {
            strings = Files.readAllLines(path);
        } catch (Exception e) {
            strings = Files.readAllLines(path, Charset.forName("GB2312"));
        }

        int position = 0;
        String a = strings.get(position);
        String b = strings.get(position + 1);

        while (a != null && b != null) {
            int k = a.split(",").length;
            int j = b.split(",").length;
            if (k > 1 && k == j) {
                break;
            }
            a = b;
            b = strings.get(++position);
        }
        return new StringReader(strings.stream().skip(position - 1).collect(Collectors.joining("\n")));
    }

    @Override
    public boolean isSupport(Path path) {
        File file = path.toFile();
        return !file.isDirectory() && file.getName().endsWith(".csv") && file.length() > 0;
    }

    @Override
    public List<Record> parseData(Path path) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(createReader(path));
        String[] header = bufferedReader.readLine().split(",");
        CSVFormat format = CSVFormat.DEFAULT.withHeader(header).withIgnoreEmptyLines();
        CSVParser parse = CSVParser.parse(bufferedReader, format);
        Schema schema = new Schema();
        for (String k : header) {
            schema.addField(new SchemaFieldFactory(k.strip()).get());
        }

        List<Record> records = parse.getRecords().stream().map(csvRecord -> {
            try {
                if (csvRecord.size() != header.length) return null;
                Record record = new Record(schema);
                for (String k : header) {
                    record.kv(k, csvRecord.get(k));
                }
                return record;
            } catch (Exception e) {
                return null;
            }

        }).filter(Objects::nonNull).collect(Collectors.toList());
        bufferedReader.close();
        return records;
    }

    @Override
    public List<Consumer<Exception>> getExceptionConsumers() {
        return exceptionConsumers;
    }
}
