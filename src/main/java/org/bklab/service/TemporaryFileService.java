/*
 * Class: org.bklab.service.TemporaryFileService
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.service;

import java.io.File;
import java.io.IOException;

public class TemporaryFileService {

    private final static String separator = File.separator;
    private static TemporaryFileService instance;
    private static String appName;
    private final File root;

    public TemporaryFileService() {
        File file = new File(System.getProperty("java.io.tmpdir") + separator + appName);
        if (!file.exists() && !file.mkdirs()) throw new RuntimeException("创建临时文件夹失败");
        this.root = file;
    }

    public static TemporaryFileService getInstance() {
        if (instance == null) {
            instance = new TemporaryFileService();
        }
        return instance;
    }

    public static void setAppName(String appName) {
        TemporaryFileService.appName = appName;
    }

    public File getTenantRootDirectory() {
        File file = new File(root.getAbsolutePath() + separator + "0-" + appName);
        if (!file.exists() && !file.mkdirs()) throw new RuntimeException("创建用户临时文件夹失败：" + file.getAbsolutePath());
        return file;
    }

    public File createTemplateFileForUpload(String parentName, String fileName) {
        return createFileForMulti(getTenantRootDirectory(), fileName, "上传", parentName);
    }

    public File createTemplateDirectory(String directoryName) {
        File parentRoot = new File(getTenantRootDirectory().getAbsolutePath() + separator + directoryName + separator + System.currentTimeMillis());
        if (!parentRoot.exists() && !parentRoot.mkdirs())
            throw new RuntimeException("创建用户二级临时文件夹失败：" + parentRoot.getAbsolutePath());
        return parentRoot;
    }

    public File createDirectoryInParent(File parent, String directoryName) {
        File dir = new File(parent.getAbsolutePath() + separator + directoryName + separator + System.currentTimeMillis());
        if (!dir.exists() && !dir.mkdirs())
            throw new RuntimeException("创建用户多级临时文件夹失败：" + dir.getAbsolutePath());
        return dir;
    }

    public File createTemplateFile(String parentName, String fileName) {
        File file = new File(createTemplateDirectory(parentName).getAbsolutePath() + separator + fileName);
        if (file.exists() && !file.delete()) throw new RuntimeException("临时文件被占用，无法删除。：" + file.getAbsolutePath());
        try {
            if (file.createNewFile()) {
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("创建临时文件出错：" + file.getAbsolutePath());
        }
        throw new RuntimeException("无法创建临时文件。：" + file.getAbsolutePath());
    }

    public File createFileForMulti(File parentDirectory, String fileName, String... directoryNames) {
        if (parentDirectory.isFile()) throw new RuntimeException("parentDirectory is a file");
        File f = new File(parentDirectory.getAbsolutePath() + separator + String.join(separator, directoryNames));
        if (!f.exists() && !f.mkdirs()) throw new RuntimeException("创建临时文件夹失败：" + f.getAbsolutePath());
        f = new File(f.getAbsolutePath() + separator + fileName);
        try {
            if (!f.createNewFile()) throw new RuntimeException("创建临时文件失败：" + f.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("创建临时文件[" + f.getAbsolutePath() + "]出错: " + e.getLocalizedMessage());
        }
        return f;
    }

    public File createDirectoryForBackup(String directoryName) {
        return createDirectoryInParent(createTemplateDirectory("备份"), directoryName);
    }

    public File createDirectoryForTemplate(String directoryName) {
        return createDirectoryInParent(createTemplateDirectory("模板"), directoryName);
    }

    public File createDirectoryForMulti(File parentDirectory, String... directoryNames) {
        if (parentDirectory.isFile()) throw new RuntimeException("parentDirectory is a file");
        File f = new File(parentDirectory.getAbsolutePath() + separator + String.join(separator, directoryNames));
        if (!f.mkdirs()) throw new RuntimeException("创建临时文件夹失败：" + f.getAbsolutePath());
        return f;
    }

    public File createDirectoryForUpload(String directoryName) {
        return createDirectoryInParent(createTemplateDirectory("上传"), directoryName);
    }

    public File createDirectoryForImage(String directoryName) {
        return createDirectoryInParent(createTemplateDirectory("图片"), directoryName);
    }

    public File createDirectoryForData(String directoryName) {
        return createDirectoryInParent(createTemplateDirectory("数据"), directoryName);
    }

    public File createFileForBackup(String fileName) {
        return createTemplateFile("备份", fileName);
    }

    public File createFileForUpload(String fileName) {
        return createTemplateFile("上传", fileName);
    }

    public File createFileForTemplate(String fileName) {
        return createTemplateFile("模板", fileName);
    }

    public File createFileForImage(String fileName) {
        return createTemplateFile("图片", fileName);
    }

    public File createFileForData(String fileName) {
        return createTemplateFile("数据", fileName);
    }

    public File createFileForSsh(String fileName) {
        return createTemplateFile("ssh", fileName);
    }
}
