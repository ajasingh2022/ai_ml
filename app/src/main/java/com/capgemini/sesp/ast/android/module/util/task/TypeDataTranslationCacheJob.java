package com.capgemini.sesp.ast.android.module.util.task;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.TAG;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class TypeDataTranslationCacheJob extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] objects) {

        Log.d("start time typedata",String.valueOf(System.currentTimeMillis()));
        DatabaseHandler databaseHandler=DatabaseHandler.createDatabaseHandler();
        Cursor cursor = databaseHandler.getReadableDatabase().rawQuery("select * from data where data like '%\"tokenCode\":\"T_%' ",null);
        SQLiteDatabase db =databaseHandler.getWritableDatabase();
        JSONObject jsonObject = null;
        StringBuilder queryString = new StringBuilder();
        while(cursor !=null && cursor.moveToNext()){
            String value = cursor.getString(4);
            try {
                jsonObject = new JSONObject(value);
                String token=String.valueOf(jsonObject.get("tokenCode"));
                String name=String.valueOf(jsonObject.get("name"));
                String code=String.valueOf(jsonObject.get("code"));
                databaseHandler.insertTranslation(token,name);
            } catch (JSONException e) {
                writeLog(TAG + ": insert()", e);
            }
        }
        Log.d("end time typedata",String.valueOf(System.currentTimeMillis()));
        return null;
    }
}
