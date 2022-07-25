package com.capgemini.sesp.ast.android.module.util.exception;

import android.content.Context;
import android.util.Log;

import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.GuiDate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;


/**
 * Created by rkumari2 on 05-09-2018.
 */

public class SespLogHandler implements UncaughtExceptionHandler {

    public static final String STACKTRACE_FILE_ENDING = ".html";
    private static final String FILENAME_FORMAT = "yyyy-MM-dd_HH.mm.ss";
    public static final String FOLDERPATH = AndroidUtilsAstSep.getAppImageFolder("SESPException");
    public static final String TAG = SespLogHandler.class.getSimpleName();
    private static File logFile = null;
    private static Context context;

    public SespLogHandler(Context ctx) {
        context = ctx;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
    }

    public static void writeLog(String c, Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = " Exception: " + sw.toString();
        StringBuilder message = new StringBuilder();
        message.append(" DATE :").append(new Date()).append(exceptionAsString);
        message.append("_________________________________________________________\n");
        Log.e(c, message.toString());
        createLog(c + message.toString());
    }

    /*  Creating a file in internal storage */
    public static void createLog(String logContent) {
        if (logFile != null) {
            if (logFile.exists()) {
                double fileSize = logFile.length() / (1024 * 1024); //file size  to be 1 mb
                if (fileSize >= 1) {
                    File backupLog = new File(FOLDERPATH + "/" + createFilename());
                    logFile.renameTo(backupLog);
                }

                File files = new File(FOLDERPATH);
                for (File listOfFiles : files.listFiles()) {
                    //the file will be deleted if it exists more than 7 days EXCEPT the last modified file
                    if (((new Date().getDate() - new Date(listOfFiles.lastModified()).getDate()) > 7) &&
                            !(listOfFiles.getName().matches("SESPException.Log"))) {
                        listOfFiles.delete();

                    }
                }
            }
        }
        writeToFile(logContent);
    }

    private static void writeToFile(String content) {
        try {
            if (logFile == null) {
                logFile = new File(FOLDERPATH + "/SESPException.Log");
            }
            FileWriter fw = new FileWriter(logFile.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            writeLog(TAG + " :writeToFile()", e);
        }
    }


    private static String createFilename() {
        final StringBuilder builder = new StringBuilder(GuiDate.formatDate(new Date(), FILENAME_FORMAT));
        final DatabaseHandler databaseHandler = DatabaseHandler.createDatabaseHandler();
        String username = databaseHandler.getLastLoginUserName();
        if (username == null || (username != null && username.trim().equals(""))) {
            username = "unknown_user";
        }
        builder.append('_').append(username).append(STACKTRACE_FILE_ENDING);
        return builder.toString();
    }

}
