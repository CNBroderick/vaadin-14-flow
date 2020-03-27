/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-27 10:46:00
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.common.data.ExcelRecordDataFunction
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.data;

import dataq.core.data.schema.Field;
import dataq.core.data.schema.Record;
import dataq.core.data.schema.Schema;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExcelRecordDataFunction implements IRecordDataFunction<ExcelRecordDataFunction> {

    private final List<Consumer<Exception>> exceptionConsumers = new ArrayList<>();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean isSupport(Path path) {
        File file = path.toFile();
        return !file.isDirectory()
                && Stream.of(".xls", ".xlsx").anyMatch(s -> file.getName().toLowerCase().endsWith(s))
                && file.length() > 0
                ;
    }

    @Override
    public List<Record> parseData(Path path) throws Exception {
        Workbook workbook = createWorkbook(path);
        Sheet sheet = workbook.getSheetAt(0);
        Row headerRow = sheet.getRow(findHeaderRow(sheet));
        Schema schema = new Schema();
        for (int i = headerRow.getFirstCellNum(); i <= headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell == null) continue;
            schema.addField(new Field(getCellValue(cell, false)));
        }

        List<Record> records = new ArrayList<>();
        Row next;
        System.out.println("from " + headerRow.getRowNum() + " to " + sheet.getLastRowNum());
        for (int i = headerRow.getRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
            next = sheet.getRow(i);
            records.add(createRecord(schema, next));
        }

        return records.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private int findHeaderRow(Sheet sheet) {
        for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
            Row a = sheet.getRow(i - 1);
            Row b = sheet.getRow(i);
            int j = a.getLastCellNum() - a.getFirstCellNum();
            int k = b.getLastCellNum() - b.getFirstCellNum();
            if (k > 1 && j == k) return i - 1;
        }
        return 0;
    }

    private Record createRecord(Schema schema, Row row) {
        if (row == null) return null;
        Record record = new Record(schema);
//        if (row.getLastCellNum() - row.getFirstCellNum() != schema.fields().length - 1) return null;
        for (int i = row.getFirstCellNum(); i <= row.getLastCellNum(); i++) {
            Field field = null;
            if (i < schema.size()) field = schema.getField(i);
            if (field == null) continue;
            String name = field.getName();
            record.setString(i, getCellValue(row.getCell(i),
                    Stream.of("时间", "日期").anyMatch(name::contains))
            );
        }
        return record;
    }

    private String getCellValue(Cell cell, boolean isDate) {
        if (cell == null) return null;
        String value;
        switch (cell.getCellType()) {
            case NUMERIC: {
                value = Double.valueOf(cell.getNumericCellValue()).toString();

                LocalDateTime timeCellValue = cell.getLocalDateTimeCellValue();
                if (timeCellValue != null && isDate) {
                    value = dateTimeFormatter.format(timeCellValue);
                }
                break;
            }
            case BOOLEAN:
                value = Boolean.toString(cell.getBooleanCellValue());
                break;
            case _NONE:
                value = null;
                break;
            case FORMULA:
                value = cell.getCellFormula();
                break;
            default: {

                value = cell.getStringCellValue();
            }
        }
        return Optional.ofNullable(value).map(String::strip).orElse(value);
    }

    private Workbook createWorkbook(Path path) throws Exception {
        Workbook workbook = null;
        File file = path.toFile();
        if (file.getName().toLowerCase().endsWith(".xls")) {
            workbook = new HSSFWorkbook(Files.newInputStream(path, StandardOpenOption.READ));
        } else if (file.getName().toLowerCase().endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(path.toFile());
        }
        return workbook;
    }

    @Override
    public List<Consumer<Exception>> getExceptionConsumers() {
        return exceptionConsumers;
    }
}
