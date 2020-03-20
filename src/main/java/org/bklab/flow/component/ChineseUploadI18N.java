/*
 * Class: org.bklab.flow.component.ChineseUploadI18N
 * Modify date: 2020/3/20 上午10:14
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.component;

import com.vaadin.flow.component.upload.UploadI18N;

public class ChineseUploadI18N extends UploadI18N {

    public ChineseUploadI18N() {

        this
                .setDropFiles(new DropFiles().setOne("拖入单个文件").setMany("拖入多个文件"))
                .setAddFiles(new AddFiles().setOne("上传单个文件").setMany("上传多个文件"))
                .setCancel("取消")
                .setError(new Error().setFileIsTooBig("文件过大").setIncorrectFileType("无效的文件类型").setTooManyFiles("太多文件"))
                .setUploading(new Uploading().setError(new Uploading.Error().setUnexpectedServerError("意外的服务器错误")
                        .setForbidden("被服务器禁止").setServerUnavailable("服务器不可用")))
                .setUnits(new Units())
        ;
    }

}
