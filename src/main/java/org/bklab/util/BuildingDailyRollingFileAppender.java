/*
 * Class: org.bklab.util.BuildingDailyRollingFileAppender
 * Modify date: 2020/3/20 上午10:25
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.util;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * log4j appender扩展<br>
 * （1）按天并且只保留最近n天的 <br>
 * （2）如果一天的文件过大，可以按配置的大小将一天的文件进行切分
 *
 * @author Broderick
 * @since 2019-12-12
 */
@SuppressWarnings("WeakerAccess")
public class BuildingDailyRollingFileAppender extends FileAppender {

    // The code assumes that the following constants are in a increasing
    // sequence.
    private static final int TOP_OF_TROUBLE = -1;
    private static final int TOP_OF_MINUTE = 0;
    private static final int TOP_OF_HOUR = 1;
    private static final int HALF_DAY = 2;
    private static final int TOP_OF_DAY = 3;
    private static final int TOP_OF_WEEK = 4;
    private static final int TOP_OF_MONTH = 5;

    /**
     * The gmtTimeZone is used only in computeCheckPeriod() method.
     */
    private static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");

    /**
     * The default maximum file size is 1MB.
     */
    private static final long maxFileSize = 0x100000;
    private final Date now = new Date();
    private final BuildingRollingCalendar rollingCalendar = new BuildingRollingCalendar();
    public int checkPeriod = TOP_OF_TROUBLE;
    /**
     * The date pattern. By default, the pattern is set to "'.'yyyy-MM-dd" meaning daily
     * rollover.
     */
    private String datePattern = "'.'yyyy-MM-dd";
    /**
     * There is one backup file by default.
     */
    private int maxBackupIndex = 7;
    /**
     * The log file will be renamed to the value of the scheduledFilename variable when
     * the next interval is entered. For example, if the rollover period is one hour, the
     * log file will be renamed to the value of "scheduledFilename" at the beginning of
     * the next hour. The precise time when a rollover occurs depends on logging activity.
     */
    private String scheduledFilename;
    /**
     * The next time we estimate a rollover should occur.
     */
    private long nextCheck = System.currentTimeMillis() - 1;
    private SimpleDateFormat sdf;

    /**
     * The default constructor does nothing.
     */
    public BuildingDailyRollingFileAppender() {
        System.setProperty("DATAQ_HOME", System.getenv("DATAQ_HOME"));
    }

    /**
     * Instantiate a <code>DailyRollingFileAppender</code> and open the file designated by
     * <code>filename</code>. The opened filename will become the output destination for
     * this appender.
     */
    public BuildingDailyRollingFileAppender(Layout layout, String filename, String datePattern) throws IOException {
        super(layout, filename, true);
        this.datePattern = datePattern;
        activateOptions();
    }

    /**
     * Returns the value of the <b>DatePattern</b> option.
     */
    public String getDatePattern() {
        return datePattern;
    }

    /**
     * The <b>DatePattern</b> takes a string in the same format as expected by
     * {@link SimpleDateFormat}. This options determines the rollover schedule.
     */
    public void setDatePattern(String pattern) {
        datePattern = pattern;
    }

    /**
     * 返回日志文件最大备份数
     */
    public int getMaxBackupIndex() {
        return maxBackupIndex;
    }

    /**
     * 设置日志文件最大备份数
     * <p>
     * The <b>MaxBackupIndex</b> option determines how many backup files are kept before
     * the oldest is erased. This option takes a positive integer value. If set to zero,
     * then there will be no backup files and the log file will be renamed to the value of
     * the scheduledFilename variable when the next interval is entered.
     */
    public void setMaxBackupIndex(int maxBackups) {
        this.maxBackupIndex = maxBackups;
    }

    public void activateOptions() {
        super.activateOptions();

        LogLog.debug("Max backup file kept: " + maxBackupIndex + ".");

        if (datePattern != null && fileName != null) {
            now.setTime(System.currentTimeMillis());
            sdf = new SimpleDateFormat(datePattern);
            int type = computeCheckPeriod();
            printPeriodicity(type);
            rollingCalendar.setType(type);
            File file = new File(fileName);
            scheduledFilename = fileName + sdf.format(new Date(file.lastModified()));
        } else {
            LogLog.error("Either File or DatePattern options are not set for appender [" + name + "].");
        }
    }

    void printPeriodicity(int type) {
        switch (type) {
            case TOP_OF_MINUTE:
                LogLog.debug("Appender [[+name+]] to be rolled every minute.");
                break;
            case TOP_OF_HOUR:
                LogLog.debug("Appender [" + name + "] to be rolled on top of every hour.");
                break;
            case HALF_DAY:
                LogLog.debug("Appender [" + name + "] to be rolled at midday and midnight.");
                break;
            case TOP_OF_DAY:
                LogLog.debug("Appender [" + name + "] to be rolled at midnight.");
                break;
            case TOP_OF_WEEK:
                LogLog.debug("Appender [" + name + "] to be rolled at start of week.");
                break;
            case TOP_OF_MONTH:
                LogLog.debug("Appender [" + name + "] to be rolled at start of every month.");
                break;
            default:
                LogLog.warn("Unknown periodicity for appender [[+name+]].");
        }
    }

    int computeCheckPeriod() {
        BuildingRollingCalendar rollingPastCalendar = new BuildingRollingCalendar(gmtTimeZone, Locale.ENGLISH);
        // set sate to 1970-01-01 00:00:00 GMT
        Date epoch = new Date(0);

        if (datePattern == null) {
            return TOP_OF_TROUBLE;
        }

        for (int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
            simpleDateFormat.setTimeZone(gmtTimeZone); // do all date formatting in
            // GMT
            String r0 = simpleDateFormat.format(epoch);
            rollingPastCalendar.setType(i);
            Date next = new Date(rollingPastCalendar.getNextCheckMillis(epoch));
            String r1 = simpleDateFormat.format(next);

            // System.out.println("Type = "+i+", r0 = "+r0+", r1 = "+r1);
            if (!r0.equals(r1)) {
                return i;
            }
        }

        return TOP_OF_TROUBLE; // Deliberately head for trouble...
    }

    /**
     * Rollover the current file to a new file.
     */
    void rollOver() {
        /* Compute filename, but only if datePattern is specified */
        if (datePattern == null) {
            errorHandler.error("Missing DatePattern option in rollOver().");
            return;
        }

        String datedFilename = fileName + sdf.format(now);
        // It is too early to roll over because we are still within the
        // bounds of the current interval. Rollover will occur once the
        // next interval is reached.
        if (scheduledFilename.equals(datedFilename)) {
            return;
        }

        // close current file, and rename it to datedFilename
        this.closeFile();

        File target = new File(scheduledFilename);
        if (target.exists() && target.delete()) {

        }

        File file = new File(fileName);
        boolean result = file.renameTo(target);
        if (result) {
            LogLog.debug(fileName + " -> " + scheduledFilename);

            // If maxBackups <= 0, then there is no file renaming to be done.
            if (maxBackupIndex > 0) {
                // Delete the oldest file, to keep system happy.
                file = new File(fileName + dateBefore());

                // 删除很久以前的历史log文件
                deleteAncientFilesIfExists(file);

                if (file.exists() && file.delete()) {

                }
            }
        } else {
            LogLog.error("Failed to rename [[+fileName+]] to [[+scheduledFilename+]].");
        }

        try {
            // This will also close the file. This is OK since multiple close operations
            // are safe.
            this.setFile(fileName, false, this.bufferedIO, this.bufferSize);
        } catch (IOException e) {
            errorHandler.error("setFile(" + fileName + ", false) call failed.");
        }

        scheduledFilename = datedFilename;
    }

    /**
     * 删除很久以前没有删除的日志文件（如果存在的话）
     *
     * @param oldestFile 日志文件
     */
    private void deleteAncientFilesIfExists(final File oldestFile) {
        // 找出久远日志文件列表
        File[] longestFiles = oldestFile.getParentFile().listFiles(pathname ->
                pathname.getPath().replaceAll("\\\\", "/").startsWith(fileName.replaceAll("\\\\", "/"))
                        && pathname.getName().compareTo(oldestFile.getName()) < 0);

        // 删除久远日志文件列表
        if (longestFiles != null) {
            for (File longestFile : longestFiles) {
                // 如果文件比配置的最老日期还老的话，删掉
                if (longestFile.delete()) {

                }
            }
        }
    }

    private String dateBefore() {
        String dataAnte = "";

        if (datePattern != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

            dataAnte = simpleDateFormat
                    .format(new Date(rollingCalendar.getPastCheckMillis(new Date(), maxBackupIndex)));
        }

        return dataAnte;
    }

    /**
     * This method differentiates DailyRollingFileAppender from its super class.
     * <p>
     * Before actually logging, this method will check whether it is time to do a
     * rollover. If it is, it will schedule the next rollover time and then rollover.
     */
    protected void subAppend(LoggingEvent event) {
        // 根据文件大小roll over
        rollOverBySize();

        // 根据时间roll over
        rollOverByTime();

        super.subAppend(event);
    }

    /**
     * 根据文件大小roll over
     */
    private void rollOverBySize() {
        long currentTimeMillis = System.currentTimeMillis();
        now.setTime(currentTimeMillis);

        if (fileName != null && qw != null) {
            long size = ((CountingQuietWriter) qw).getCount();
            if (size >= maxFileSize) {
                // close current file, and rename it
                this.closeFile();
                File[] currentDirFiles = new File(fileName).getParentFile().listFiles();
                int i = currentDirFiles == null ? 0 : currentDirFiles.length;
                int pointPosition = fileName.lastIndexOf('.');
                String dateString = sdf.format(now);
                String rollingFileName;
                if (pointPosition > 0) {
                    String head = fileName.substring(0, pointPosition);
                    String tail = fileName.substring(pointPosition);
                    if (currentDirFiles != null) {
                        long count = Arrays.stream(currentDirFiles).filter(Objects::nonNull).filter(f -> f.getName().contains(dateString)).count() + 1;
                        rollingFileName = head + dateString + "." + count + tail;
                    } else {
                        rollingFileName = head + dateString + ".0" + tail;
                    }
                } else {
                    if (currentDirFiles != null) {
                        long count = Arrays.stream(currentDirFiles).filter(Objects::nonNull).filter(f -> f.getName().contains(dateString)).count() + 1;
                        rollingFileName = fileName + dateString + "." + count + ".log";
                    } else {
                        rollingFileName = fileName + dateString + ".0" + ".log";
                    }
                }

                if (new File(fileName).renameTo(new File(rollingFileName))) {

                }
                try {
                    // This will also close the file. This is OK since multiple close
                    // operations
                    // are safe.
                    this.setFile(fileName, false, this.bufferedIO, this.bufferSize);
                } catch (IOException e) {
                    errorHandler.error("setFile(" + fileName + ", false) call failed.");
                }
            }
        }
    }

    /**
     * 根据时间roll over
     */
    private void rollOverByTime() {
        long currentTime = System.currentTimeMillis();

        if (currentTime < nextCheck) {
            return;
        }

        now.setTime(currentTime);
        nextCheck = rollingCalendar.getNextCheckMillis(now);

        try {
            rollOver();
        } catch (Exception e) {
            LogLog.error("rollOver() failed.", e);
        }
    }

    @Override
    public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize)
            throws IOException {
        super.setFile(fileName, append, this.bufferedIO, this.bufferSize);
        if (append) {
            File f = new File(fileName);
            ((CountingQuietWriter) qw).setCount(f.length());
        }
    }

    @Override
    protected void setQWForFiles(Writer writer) {
        this.qw = new CountingQuietWriter(writer, errorHandler);
    }

}

/**
 * MyRollingCalendar is a helper class to DailyMaxRollingFileAppender. Given a periodicity
 * type and the current time, it computes the past maxBackupIndex date.
 */
class BuildingRollingCalendar extends GregorianCalendar {

    private static final long serialVersionUID = 1L;

    private static final int TOP_OF_TROUBLE = -1;
    private static final int TOP_OF_MINUTE = 0;
    private static final int TOP_OF_HOUR = 1;
    private static final int HALF_DAY = 2;
    private static final int TOP_OF_DAY = 3;
    private static final int TOP_OF_WEEK = 4;
    private static final int TOP_OF_MONTH = 5;

    private int type = TOP_OF_TROUBLE;

    BuildingRollingCalendar() {
        super();
    }

    BuildingRollingCalendar(TimeZone tz, Locale locale) {
        super(tz, locale);
    }

    long getPastCheckMillis(Date now, int maxBackupIndex) {
        return getPastDate(now, maxBackupIndex).getTime();
    }

    private Date getPastDate(Date now, int maxBackupIndex) {
        this.setTime(now);

        switch (type) {
            case TOP_OF_MINUTE:
                this.set(Calendar.MINUTE, this.get(Calendar.MINUTE) - maxBackupIndex);
                break;

            case TOP_OF_HOUR:
                this.set(Calendar.HOUR_OF_DAY, this.get(Calendar.HOUR_OF_DAY) - maxBackupIndex);
                break;

            case HALF_DAY:
                int hour = get(Calendar.HOUR_OF_DAY);
                if (hour < 12) {
                    this.set(Calendar.HOUR_OF_DAY, 12);
                } else {
                    this.set(Calendar.HOUR_OF_DAY, 0);
                }
                this.set(Calendar.DAY_OF_MONTH, this.get(Calendar.DAY_OF_MONTH) - maxBackupIndex);
                break;

            case TOP_OF_DAY:
                this.set(Calendar.DATE, this.get(Calendar.DATE) - maxBackupIndex);
                break;

            case TOP_OF_WEEK:
                this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());
                this.set(Calendar.WEEK_OF_YEAR, this.get(Calendar.WEEK_OF_YEAR) - maxBackupIndex);
                break;

            case TOP_OF_MONTH:
                this.set(Calendar.MONTH, this.get(Calendar.MONTH) - maxBackupIndex);
                break;

            default:
                throw new IllegalStateException("Unknown periodicity type.");
        }

        return this.getTime();
    }

    long getNextCheckMillis(Date now) {
        return getNextCheckDate(now).getTime();
    }

    private Date getNextCheckDate(Date now) {
        this.setTime(now);

        switch (type) {
            case TOP_OF_MINUTE:
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.MINUTE, 1);
                break;
            case TOP_OF_HOUR:
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.HOUR_OF_DAY, 1);
                break;
            case HALF_DAY:
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                int hour = get(Calendar.HOUR_OF_DAY);
                if (hour < 12) {
                    this.set(Calendar.HOUR_OF_DAY, 12);
                } else {
                    this.set(Calendar.HOUR_OF_DAY, 0);
                    this.add(Calendar.DAY_OF_MONTH, 1);
                }
                break;
            case TOP_OF_DAY:
                this.set(Calendar.HOUR_OF_DAY, 0);
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.DATE, 1);
                break;
            case TOP_OF_WEEK:
                this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());
                this.set(Calendar.HOUR_OF_DAY, 0);
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case TOP_OF_MONTH:
                this.set(Calendar.DATE, 1);
                this.set(Calendar.HOUR_OF_DAY, 0);
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.MONTH, 1);
                break;
            default:
                throw new IllegalStateException("Unknown periodicity type.");
        }
        return getTime();
    }

    void setType(int type) {
        this.type = type;
    }
}