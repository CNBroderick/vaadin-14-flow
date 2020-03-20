/*
 * Class: org.bklab.common.ssh2.SshIdentityType
 * Modify date: 2020/3/20 上午11:03
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.ssh2;

public enum SshIdentityType {
    密码,
    密钥,
    ;

    public boolean isUsingPassword() {
        return this == 密码;
    }

    public boolean isUsingKeychain() {
        return this == 密钥;
    }
}
