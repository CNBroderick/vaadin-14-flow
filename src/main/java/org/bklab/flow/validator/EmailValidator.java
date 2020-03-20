/*
 * Class: org.bklab.flow.validator.EmailValidator
 * Modify date: 2020/3/20 上午10:14
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.validator;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

import java.util.stream.IntStream;

public class EmailValidator implements Validator<String> {

    @Override
    public ValidationResult apply(String email, ValueContext context) {

        if (email == null || email.isBlank() || email.strip().length() < 5) {
            return ValidationResult.error("请填写正确的邮件地址");
        }

        IntStream chars = email.chars();
        if (email.chars().filter(c -> c == '@').count() != 1) return ValidationResult.error("邮箱地址无效");

        int atIndex = email.indexOf('@');
        String local = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        if (local.length() < 1 || local.charAt(0) == '.' || local.indexOf("..") > 0)
            return ValidationResult.error("邮箱本地地址无效");

        if (domain.length() < 3) return ValidationResult.error("邮箱域名地址无效");
        int i = domain.indexOf('.');
        if (i < 1 || i >= domain.length() - 1 || domain.contains("..")) return ValidationResult.error("邮箱域名地址无效");


        return ValidationResult.ok();
    }
}
