package com.lkh.android.base.log;

import com.lkh.android.base.config.FilePathConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 写日志到sdcard
 */
public class LogToFile implements ILog {
    private String mPath;
    private Writer mWriter;

    private static final SimpleDateFormat TIMESTAMP_FMT = new SimpleDateFormat(
            "[yyyy-MM-dd HH:mm:ss] ");
    private String basePath = "";
    private static String BASE_FILENAME = "ta.log";
    private File logDir;

    public void open() {
        logDir = new File(FilePathConfig.ROOT_PATH+FilePathConfig.LOG_DIR);
        if (!logDir.exists()) {
            logDir.mkdirs();
            // do not allow media scan
            try {
                new File(logDir, ".nomedia").createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        basePath = logDir.getAbsolutePath() + "/" + BASE_FILENAME;
        try {
            File file = new File(basePath + "-" + getCurrentTimeString());
            mPath = file.getAbsolutePath();
            mWriter = new BufferedWriter(new FileWriter(mPath), 2048);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getCurrentTimeString() {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(now);
    }

    public String getPath() {
        return mPath;
    }

    @Override
    public void d(String tag, String message) {
        println(DEBUG, tag, message);
    }

    @Override
    public void e(String tag, String message) {
        println(ERROR, tag, message);
    }

    @Override
    public void i(String tag, String message) {
        println(INFO, tag, message);
    }

    @Override
    public void v(String tag, String message) {
        println(VERBOSE, tag, message);
    }

    @Override
    public void w(String tag, String message) {
        println(WARN, tag, message);
    }


    public void println(int priority, String tag, String message) {
        String printMessage = "";
        switch (priority) {
            case VERBOSE:
                printMessage = "[V]|"
                        + tag
                        + "|"
                        + message;
                break;
            case DEBUG:
                printMessage = "[D]|"
                        + tag
                        + "|"
                        + message;
                break;
            case INFO:
                printMessage = "[I]|"
                        + tag
                        + "|"
                        + message;
                break;
            case WARN:
                printMessage = "[W]|"
                        + tag
                        + "|"
                        + message;
                break;
            case ERROR:
                printMessage = "[E]|"
                        + tag
                        + "|"
                        + message;
                break;
            default:

                break;
        }
        println(printMessage);

    }

    public void println(String message) {
        open();
        try {
            mWriter.write(TIMESTAMP_FMT.format(new Date()));
            mWriter.write(message);
            mWriter.write('\n');
            mWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void close() {
        try {
            if (mWriter != null) {
                mWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
