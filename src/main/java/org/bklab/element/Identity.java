/*
 * Class: org.bklab.element.Identity
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.element;

import org.bklab.common.ssh2.SshIdentityType;
import org.bklab.util.SM4Util;

import java.util.Properties;

public class Identity {

    private static final String IV = "584ef4e66cf131c4de545ebe187922f3";
    private static final String USERNAME_PROPERTY = "username";
    private static final String PASSWORD_PROPERTY = "password";
    private static final String KEYCHAIN_PROPERTY = "keychain";
    private static final String PASSPHRASE_PROPERTY = "passphrase";
    private final Properties properties = new Properties();
    private SshIdentityType identityType = SshIdentityType.密码;

    public void updateUsername(String username) {
        update(USERNAME_PROPERTY, username, IV);
    }

    public void updatePassword(String password) {
        update(PASSWORD_PROPERTY, password, IV);
    }

    public void updateKeychain(String keychain) {
        update(KEYCHAIN_PROPERTY, keychain, IV);
    }

    public void updatePassphrase(String passphrase) {
        update(PASSPHRASE_PROPERTY, passphrase, IV);
    }

    public String getUsername() {
        return get(USERNAME_PROPERTY, IV);
    }

    public Identity setUsername(String username) {
        if (username != null) properties.put(USERNAME_PROPERTY, username);
        return this;
    }

    public String getPassword() {
        return get(PASSWORD_PROPERTY, IV);
    }

    public Identity setPassword(String password) {
        if (password != null) properties.put(PASSWORD_PROPERTY, password);
        return this;
    }

    public String getKeychain() {
        return get(KEYCHAIN_PROPERTY, IV);
    }

    public Identity setKeychain(String keychain) {
        if (keychain != null) properties.put(KEYCHAIN_PROPERTY, keychain);
        return this;
    }

    public String getPassphrase() {
        return get(PASSPHRASE_PROPERTY, IV);
    }

    public Identity setPassphrase(String passphrase) {
        if (passphrase != null) properties.put(PASSPHRASE_PROPERTY, passphrase);
        return this;
    }

    public String getEncodeUsername() {
        return properties.getProperty(USERNAME_PROPERTY);
    }

    public String getEncodePassword() {
        return properties.getProperty(PASSWORD_PROPERTY);
    }

    public String getEncodeKeychain() {
        return properties.getProperty(KEYCHAIN_PROPERTY);
    }

    public String getEncodePassphrase() {
        return properties.getProperty(PASSPHRASE_PROPERTY);
    }

    public SshIdentityType getIdentityType() {
        return identityType;
    }

    public Identity setIdentityType(SshIdentityType identityType) {
        this.identityType = identityType;
        return this;
    }

    public Identity setIdentityType(String identityType) {
        this.identityType = SshIdentityType.valueOf(identityType);
        return this;
    }

    public void update(String key, String value, String IV) {
        if (value == null) properties.remove(key);
        else properties.put(key, SM4Util.encode(value, IV));
    }

    public String get(String key, String IV) {
        return properties.getOrDefault(key, null) == null ? null : SM4Util.decode(properties.getProperty(key), IV);
    }

    @Override
    public String toString() {
        return Identity.class.getName();
    }

}