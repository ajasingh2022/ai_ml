package com.capgemini.sesp.ast.android.module.util.exception;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.view.Gravity;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.GuiDate;
import com.capgemini.sesp.ast.android.ui.activity.login.LoginActivity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skvader.rsp.cft.common.to.cft.table.ErrorTO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;
import java.util.Locale;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

//import com.capgemini.sesp.android.admin.activity.SESPDeviceAdministration;

public class SespExceptionHandler implements UncaughtExceptionHandler {

    public static final String STACKTRACE_FILE_ENDING = ".html";
    private static final String FILENAME_FORMAT = "yyyy-MM-dd_HH.mm.ss";
    private static final ObjectMapper JSONMAPPER = new ObjectMapper();
    public static final String TAG = SespExceptionHandler.class.getSimpleName();
    private static Context context;

    public SespExceptionHandler(Context ctx) {
        context = ctx;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        logError(e);
    }

    private static StatFs getStatFs() {
        File path = Environment.getDataDirectory();
        return new StatFs(path.getPath());
    }

    private static long getAvailableInternalMemorySize(StatFs stat) {
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return availableBlocks * blockSize;
    }

    private static long getTotalInternalMemorySize(StatFs stat) {
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return totalBlocks * blockSize;
    }

    private static String addInformation() {
        StringBuilder message = new StringBuilder();
        message.append("Locale: ").append(Locale.getDefault()).append("</br>");
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi;
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            message.append("Version: ").append(pi.versionName).append("</br>");
            message.append("Package: ").append(pi.packageName).append("</br>");
        } catch (Exception e) {
            writeLog(TAG + " :addInformation() - CustomExceptionHandler", e);
            message.append("Could not get Version information for ").append(
                    context.getPackageName());
        }
        message.append("Phone Model: ").append(android.os.Build.MODEL)
                .append("</br>");
        message.append("Android Version: ")
                .append(android.os.Build.VERSION.RELEASE).append("</br>");
        message.append("Board: ").append(android.os.Build.BOARD).append("</br>");
        message.append("Brand: ").append(android.os.Build.BRAND).append("</br>");
        message.append("Device: ").append(android.os.Build.DEVICE).append("</br>");
        message.append("Host: ").append(android.os.Build.HOST).append("</br>");
        message.append("ID: ").append(android.os.Build.ID).append("</br>");
        message.append("Model: ").append(android.os.Build.MODEL).append("</br>");
        message.append("Product: ").append(android.os.Build.PRODUCT).append("</br>");
        message.append("Type: ").append(android.os.Build.TYPE).append("</br>");
        StatFs stat = getStatFs();
        message.append("Total Internal memory: ")
                .append(getTotalInternalMemorySize(stat)).append("</br>");
        message.append("Available Internal memory: ")
                .append(getAvailableInternalMemorySize(stat)).append("</br>");
        return message.toString();
    }

    public void logError(Throwable e) {
        try {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString().replace("\n", "</br>").replace("\t", "&#9;");

            String filename = createFilename();

            ErrorTO errorTO = new ErrorTO();
            errorTO.setStacktrace(exceptionAsString);
            errorTO.setFileName(filename);
            errorTO.setMessage(addInformation());

            String jsonObj = JSONMAPPER.writeValueAsString(errorTO);
            writeToFile(jsonObj, filename);
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();

                    String errorMsg = context.getResources().getString(R.string.crash_log_message); //ideally this string would be in your Resources
                    Toast toast = Toast.makeText(context, errorMsg, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Looper.loop();
                }
            }.start();

            Thread.sleep(3500);
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(intent);
            System.exit(0);
        } catch (Exception ex) {
            writeLog(TAG + " :logError()", ex);
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

    private static void writeToFile(String content, String filename) {
        try {
            File logFile = new File(AndroidUtilsAstSep.getAppImageFolder(ConstantsAstSep.DEFAULT_LOG_DIR_NAME) + "/" + filename);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            FileWriter fw = new FileWriter(logFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            writeLog(TAG + " :writeToFile()", e);
        }
    }
}
