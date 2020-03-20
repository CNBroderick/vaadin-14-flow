/*
 * Class: org.bklab.flow.validator.ChineseMobileNumberValidator
 * Modify date: 2020/3/20 上午10:14
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.validator;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

public class ChineseMobileNumberValidator implements Validator<String> {
    @Override
    public ValidationResult apply(String value, ValueContext context) {
        if (value == null || value.strip().length() <= 11) return ValidationResult.error("位数过短");

        if (value.length() == 11) value = "0086" + value;
        if (value.startsWith("+")) value = "00" + value.substring(1);

        if (value.length() != 15) return ValidationResult.error("位数不正确");
        if (!value.startsWith("0086")) return ValidationResult.error("请输入国内手机号码");
        if ((int) value.charAt(4) != 1) return ValidationResult.error("手机号码格式不正确");


//        value.chars().allMatch(c -> c >= '0' && c <= '9');
        return ValidationResult.ok();
    }
}
