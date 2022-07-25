package com.capgemini.sesp.ast.android.ui.activity.login;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.capgemini.sesp.ast.android.module.communication.CommunicationHelper;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.skvader.rsp.cft.common.to.cft.table.ErrorTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yhegde on 5/30/2018.
 */
public class SendErrorLogsToDB extends IntentService {

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public SendErrorLogsToDB() {
        super("SendErrorLogsToDB");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("ERRORLOG", "ITS NEW SERVICE");

        //fetch all file names in SESPLog directory

        ArrayList<String> filenames = new ArrayList<String>();
        String logFilesPath = AndroidUtilsAstSep.getAppImageFolder(ConstantsAstSep.DEFAULT_LOG_DIR_NAME);

        File directory = new File(logFilesPath);
        File[] logFiles = directory.listFiles();

        for (int i = 0; i < logFiles.length; i++) {

            String file_name = logFiles[i].getName();
            filenames.add(file_name);
        }


        //Read each file, convert the data read to object of class ErrorTo and at last delete the file.

        String errorLog = null;

        for (int i = 0; i < logFiles.length; i++) {

            try {
                File myFile = new File(logFiles[i].getAbsolutePath());
                FileInputStream fis = new FileInputStream(myFile);

                byte[] dataArray = new byte[fis.available()];
                while (fis.read(dataArray) != -1) {
                    errorLog = new String(dataArray);
                }
                ErrorTO errorTO = CommunicationHelper.JSONMAPPER.readValue(errorLog.toString(), ErrorTO.class);
                AndroidUtilsAstSep.getDelegate().logError(errorTO);
                Boolean delR = myFile.delete();
                Log.d("LOGERROR", delR.toString());
                fis.close();


            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


}