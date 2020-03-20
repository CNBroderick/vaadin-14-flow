/*
 * Class: org.bklab.flow.component.SshIdentityField
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.component;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.bklab.element.Identity;
import org.bklab.flow.dialog.ExceptionDialog;
import org.bklab.flow.factory.ComboBoxFactory;
import org.bklab.flow.factory.PasswordFieldFactory;
import org.bklab.flow.factory.TextAreaFactory;
import org.bklab.flow.factory.TextFieldFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SshIdentityField extends CustomField<Identity> {

    private final ComboBox<SshIdentityType> type;
    private final TextField userName;
    private final PasswordField password;
    private final TextArea keychain;
    private final MemoryBuffer buffer = new MemoryBuffer();
    private Identity identity = new Identity();

    public SshIdentityField(String label) {
        setLabel(label);
        type = new ComboBoxFactory<SshIdentityType>()
                .items(Arrays.asList(SshIdentityType.values())).get();
        userName = new TextFieldFactory().placeholder("用户名").widthFull().get();
        password = new PasswordFieldFactory().setPlaceholder("密码").widthFull().get();
        keychain = new TextAreaFactory().placeholder("将密钥粘贴到此处").widthFull().get();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes(".pem", ".txt");
        upload.setMaxFiles(1);
        upload.setMaxFileSize(10240);
 /*
        TypeError: Cannot read property 'held' of null

        ChineseUploadI18N i18N = new ChineseUploadI18N();
        i18N.getDropFiles().setOne("拖入密钥文件").setMany("拖入多个密钥");
        i18N.getAddFiles().setOne("上传密钥").setMany("上传多个密钥");
        upload.setI18n(i18N);
*/
        upload.addSucceededListener(e -> {
            try {
                keychain.setValue(
                        new BufferedReader(new InputStreamReader(
                                buffer.getInputStream(), StandardCharsets.UTF_8.name()
                        )).lines().collect(Collectors.joining("\n"))
                );
            } catch (Exception ex) {
                new ExceptionDialog(ex).setTitle("无效的密钥格式").open();
            }
        });

        Function<Component[], HorizontalLayout> horizontalLayoutFunction = components -> {
            HorizontalLayout layout = new HorizontalLayout(components);
            layout.setWidthFull();
            layout.setMargin(false);
            return layout;
        };

        HorizontalLayout h1 = horizontalLayoutFunction.apply(new Component[]{type, userName, password});
        HorizontalLayout h2 = horizontalLayoutFunction.apply(new Component[]{upload, keychain});
        VerticalLayout main = new VerticalLayout(h1, h2);
        main.setMargin(false);
        main.setPadding(false);

        keychain.setValueChangeMode(ValueChangeMode.ON_CHANGE);
        keychain.addValueChangeListener(e -> {
            try {
                new JSch().addIdentity(e.getValue(), password.getValue());
            } catch (JSchException ex) {
                keychain.setErrorMessage("无效的密钥格式或密钥口令不正确");
            }
        });

        type.addValueChangeListener(e -> {
            h2.setVisible(e.getValue() == SshIdentityType.密钥);
            password.setPlaceholder(e.getValue() == SshIdentityType.密钥 ? "密钥口令" : "密码");
        });
        type.setValue(SshIdentityType.密码);


        add(main);
    }

    @Override
    protected Identity generateModelValue() {
        identity.updateUsername(userName.getValue());
        switch (type.getValue()) {
            case 密码:
                identity.updatePassword(password.getValue());
                identity.updateKeychain(null);
                identity.updatePassphrase(null);
                break;
            case 密钥:
                identity.updateKeychain(keychain.getValue());
                identity.updatePassphrase(password.getValue());
                identity.updatePassword(null);
                break;
        }
        identity.setIdentityType(type.getValue().name());
        return identity;
    }

    @Override
    protected void setPresentationValue(Identity newPresentationValue) {
        type.setValue(SshIdentityType.valueOf(newPresentationValue.getIdentityType().name()));
        userName.setValue(Objects.toString(newPresentationValue.getUsername(), ""));
        this.identity = newPresentationValue;
    }

    public enum SshIdentityType {
        密码,
        密钥,
        ;
    }
}
