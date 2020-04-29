/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-04-29 19:07:04
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.common.data.ExcelRecordDataFunctionTest
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.data;

import junit.framework.TestCase;
import org.bklab.util.StringExtractor;

import java.math.BigDecimal;

public class ExcelRecordDataFunctionTest extends TestCase {


    public void testName() {
        System.out.println(get("1231%"));
        System.out.println(get("12.31%"));
        System.out.println(get("12%%"));
        System.out.println(get("12ad"));
        System.out.println(get("12"));
    }

    private String get(String stringCellValue) {
        if (stringCellValue.matches("\\d+\\.?\\d+%"))
            return new BigDecimal(StringExtractor.getRealNumber(stringCellValue)).multiply(new BigDecimal("0.01")).toString();
        return stringCellValue;
    }
}