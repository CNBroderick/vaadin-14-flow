/*
 * Class: org.bklab.common.ssh2.SftpTransfer
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.ssh2;

import com.jcraft.jsch.*;
import org.bklab.element.HasExceptionConsumers;
import org.bklab.service.TemporaryFileService;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SftpTransfer extends HasExceptionConsumers<SftpTransfer> {

    private final Session session;
    public String currentPath;
    public char separator;
    private ChannelSftp channel;

    public SftpTransfer(Session session) {
        this.session = session;
        this.init();
    }

    public void init() {
        try {
            channel = (ChannelSftp) session.openChannel(SshChanelType.SFTP.getValue());
            channel.connect();
            currentPath = pwd();
            separator = currentPath.startsWith("/") ? '/' : '\\';
        } catch (JSchException e) {
            callExceptionConsumers(e);
        }
    }

    public String pwd() {
        try {
            this.currentPath = channel.pwd();
            return currentPath;
        } catch (SftpException e) {
            callExceptionConsumers(e);
        }
        return "";
    }

    public File get(ChannelSftp.LsEntry entry, SftpProgressMonitorImpl monitor) {
        try {
            File file = TemporaryFileService.getInstance().createFileForSsh(entry.getFilename());
            FileOutputStream stream = new FileOutputStream(file);
            channel.get(currentPath + separator + entry.getFilename(), stream, monitor, ChannelSftp.OVERWRITE, 0);
            return file;
        } catch (Exception e) {
            callExceptionConsumers(e);
        }
        return null;
    }

    public File get(String destination, SftpProgressMonitorImpl monitor) {
        String fileName = destination;
        int i = fileName.lastIndexOf("/");
        if (i >= 0) fileName = fileName.substring(i);
        i = fileName.lastIndexOf('\\');
        if (i >= 0) fileName = fileName.substring(i);

        return get(destination, fileName, monitor);
    }

    public File get(String destination, String fileName, SftpProgressMonitorImpl monitor) {
        try {
            File file = TemporaryFileService.getInstance().createFileForSsh(fileName);
            FileOutputStream stream = new FileOutputStream(file);
            channel.get(destination, stream, monitor, ChannelSftp.OVERWRITE, 0);
            return file;
        } catch (Exception e) {
            callExceptionConsumers(e);
        }
        return null;
    }

    public void put(File file, String destination, SftpProgressMonitorImpl monitor) {
        try {
            channel.put(new BufferedInputStream(new FileInputStream(file)), destination, monitor, ChannelSftp.OVERWRITE);
        } catch (Exception e) {
            callExceptionConsumers(e);
        }
    }

    public void put(InputStream inputStream, String destination, SftpProgressMonitorImpl monitor) {
        try {
            channel.put(inputStream, destination, monitor, ChannelSftp.OVERWRITE);
        } catch (Exception e) {
            callExceptionConsumers(e);
        }
    }

    public Collection<ChannelSftp.LsEntry> cd(String path) {
        try {
            channel.cd(path);
            this.currentPath = pwd();
        } catch (SftpException e) {
            callExceptionConsumers(e);
        }
        return ls();
    }

    public Collection<ChannelSftp.LsEntry> cd(ChannelSftp.LsEntry entry) {
        if (entry.getAttrs().isDir()) {
            cd(currentPath + separator + entry.getFilename());
        } else {
            callExceptionConsumers(new JSchException("无法cd到一个非目录文件"));
        }
        return ls();
    }

    public Collection<ChannelSftp.LsEntry> ls() {
        return ls(currentPath);
    }

    public Collection<ChannelSftp.LsEntry> ls(String remotePath) {
        try {
            ChanelSftpLsEntrySelector selector = new ChanelSftpLsEntrySelector();
            channel.ls(remotePath, selector);
            return selector.getEntries();
        } catch (SftpException e) {
            callExceptionConsumers(e);
        }
        return new LinkedHashSet<>();
    }

    public void mv(String oldPath, String newPath) {
        try {
            System.out.println("mv oldPath = " + oldPath + ", newPath = " + newPath);
            channel.rename(oldPath, newPath);
        } catch (SftpException e) {
            callExceptionConsumers(e);
        }
    }

    public void rm(String path) {
        try {
            channel.rm(path);
        } catch (SftpException e) {
            callExceptionConsumers(e);
        }
    }

    public void rmdir(String path) {
        try {
            Collection<ChannelSftp.LsEntry> fileAndFolderList = ls(path);
            for (ChannelSftp.LsEntry item : fileAndFolderList) {
                if (!item.getAttrs().isDir()) {
                    rm(path + separator + item.getFilename());
                } else if (!(List.of(".", "..").contains(item.getFilename()))) {
                    try {
                        channel.rmdir(path + separator + item.getFilename());
                    } catch (Exception e) {
                        rmdir(path + separator + item.getFilename());
                    }
                }
            }
            channel.rmdir(path);
        } catch (Exception e) {
            callExceptionConsumers(e);
        }
    }

    public void mkdir(String path) {
        try {
            channel.mkdir(path);
        } catch (SftpException e) {
            callExceptionConsumers(e);
        }
    }

    public void disconnect() {
        channel.disconnect();
    }

    public Session getSession() {
        return session;
    }

    private static class ChanelSftpLsEntrySelector implements ChannelSftp.LsEntrySelector {

        private final Set<ChannelSftp.LsEntry> entries = new LinkedHashSet<>();

        @Override
        public int select(ChannelSftp.LsEntry lsEntry) {
            entries.add(lsEntry);
            return 0;
        }

        public Collection<ChannelSftp.LsEntry> getEntries() {
            return entries.stream().sorted(
                    Comparator.comparing(e -> !e.getAttrs().isDir())
            ).collect(Collectors.toList());
        }
    }

    public static class SftpProgressMonitorImpl implements SftpProgressMonitor {

        private final Consumer<Double> progressListener;
        private final Consumer<File> finishListener;
        private long current = 0;
        private long total = 0;
        private String src;
        private String destination;
        private int op;

        public SftpProgressMonitorImpl(Consumer<Double> progressListener) {
            this.progressListener = progressListener;
            this.finishListener = f -> {
            };
        }

        public SftpProgressMonitorImpl(Consumer<Double> progressListener, Consumer<File> finishListener) {
            this.progressListener = progressListener;
            this.finishListener = finishListener;
        }

        @Override
        public void init(int i, String s, String s1, long l) {
            this.op = i;
            this.src = s;
            this.destination = s1;
            this.total = l;
        }

        @Override
        public boolean count(long current) {
            this.current = current;
            progressListener.accept(1.0d * current / total);

            return true;
        }

        @Override
        public void end() {
            finishListener.accept(new File(src));
        }

        public long getTotal() {
            return total;
        }

        public String getSrc() {
            return src;
        }

        public String getDestination() {
            return destination;
        }

        public int getOp() {
            return op;
        }

        public long getCurrent() {
            return current;
        }
    }
}
