package com.capgemini.sesp.ast.android.module.util.task;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.execute.ExecuteTask;
import com.capgemini.sesp.ast.android.module.util.execute.ExecuteType;

import org.json.JSONException;
import org.json.JSONObject;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.TAG;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class AndroidTypeDataDownloadedAstSepExecuteTask extends ExecuteTask {
    private boolean firstTime = true;

    public AndroidTypeDataDownloadedAstSepExecuteTask() {
        super(ExecuteType.AST_TYPE_DATA_DOWNLOADED);
    }

    @Override
    public void run() {
        Log.d("downloaded","post download");

        Log.d("start time",String.valueOf(System.currentTimeMillis()));
        TypeDataTranslationCacheJob typeDataTranslationCacheJob=new TypeDataTranslationCacheJob();
        typeDataTranslationCacheJob.execute();

        Log.d("end time",String.valueOf(System.currentTimeMillis()));

    }
}
