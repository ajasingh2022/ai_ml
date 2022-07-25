package com.capgemini.sesp.ast.android.module.util;

import android.os.AsyncTask;
import android.util.Log;

import com.skvader.rsp.ast_sep.common.mobile.bean.HelpDocBean;
import com.skvader.rsp.cft.common.util.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;
/**
 * Created by rkumari2 on 22-07-2019.
 * This class is to call the help webservice method to get all the Json Data
 */
public class HelpDocumentThread extends AsyncTask<String, Void, Void> {

    List<HelpDocBean> helpDocBean = null;
    SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DEFAULT_MILLISECOND_FORMAT.getPattern());
    String previousUserName = DatabaseHandler.createDatabaseHandler().getLastLoginUserName();

    @Override
    protected Void doInBackground(String... strings) {
        try {
            if (previousUserName == null) {
                helpDocBean = AndroidUtilsAstSep.getDelegate().getAllHelpDocuments(null);
            }
            else {
                String date = SESPPreferenceUtil.getPreferenceString("DATE_FOR_HELP");
                helpDocBean = AndroidUtilsAstSep.getDelegate().getAllHelpDocuments(date);
            }
            if (!helpDocBean.isEmpty()) {
                DatabaseHandler.createDatabaseHandler().insertOrUpdateHelpDocument(helpDocBean);
            }
        } catch (Exception e) {
           writeLog("HelpDocumentThread  :doInBackground() " ,e);
        }
        return null;
    }
}

