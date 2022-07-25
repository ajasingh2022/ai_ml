package com.capgemini.sesp.ast.android.module.util.task;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceType;
import com.capgemini.sesp.ast.android.module.util.LanguageHelper;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.execute.ExecuteTask;
import com.capgemini.sesp.ast.android.module.util.execute.ExecuteType;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.AndroidCachedDataAstSepTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.AndroidStaticDataAstSepTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.DatabaseConstantsAstSepAndroid;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class DownloadCftAstConstantsExecuteTask extends ExecuteTask {

    private final static String TAG = "DownloadCftAstConstantsExecuteTask";

    public DownloadCftAstConstantsExecuteTask() {
        super(ExecuteType.AST_TYPE_DATA_DOWNLOAD);
    }

    @Override
    public void run() throws Exception {
        try {
            SharedPreferences p = AndroidUtilsAstSep.getSharedPreferences(SharedPreferenceType.PERSISTENT);
            String checkSum = p.getString(SharedPreferenceKeys.TYPES_AST_SEP_CHECK_SUM, "");
            ObjectCache.getDefaultInstance().loadStaticTypeDataForAndroid();
            if (p.getBoolean("LOAD_STATIC_TYPE_DATA_AND_CONSTANTS", true)) {
                //Load CFT and AST constants
                final DatabaseConstantsAstSepAndroid databaseConstantsAstSepAndroidTO
                        = AndroidUtilsAstSep.getDelegate().loadAstAndroidConstants();
                ObjectCache.registerIdObject(databaseConstantsAstSepAndroidTO);
            }

            if (p.getBoolean("LOAD_STATIC_TYPE_DATA_AND_CONSTANTS", true)
                    || p.getBoolean("RELOAD_STATIC_TYPE_DATA", true)) {
                ObjectCache.clear();
                //Load Static Type Data
                final AndroidStaticDataAstSepTO androidStaticDataAstSepTO
                        = AndroidUtilsAstSep.getDelegate().loadStaticTypeDataForAndroid(LanguageHelper.getLanguageCode());
                ObjectCache.registerObjects(androidStaticDataAstSepTO);
            }

            //Load Dynamic Type Data
            ObjectCache.getDefaultInstance().loadDynamicTypeDataForAndroid();
            final AndroidCachedDataAstSepTO androidTypesAstSepTO
                    = AndroidUtilsAstSep.getDelegate().loadDynamicTypeDataForAndroid(checkSum, LanguageHelper.getLanguageCode(), SESPPreferenceUtil.getTypedataDownloadTime());
            SESPPreferenceUtil.setTypedataDownloadTime();
            ObjectCache.registerObjects(androidTypesAstSepTO.getAndroidDynamicTypesDataAstSepTO());
            Editor pe = p.edit();
            pe.putString(SharedPreferenceKeys.DATABASE_INFO, androidTypesAstSepTO.getDatabaseInfo());
            pe.putBoolean("LOAD_STATIC_TYPE_DATA_AND_CONSTANTS", false);
            pe.putBoolean("RELOAD_STATIC_TYPE_DATA", false);
            pe.apply();
        } catch (Exception e) {
            writeLog(TAG + " : run()", e);
        }
    }

}
