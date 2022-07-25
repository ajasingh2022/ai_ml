package com.capgemini.sesp.ast.android.module.cache;


import com.capgemini.sesp.ast.android.module.util.execute.ExecuteManager;
import com.capgemini.sesp.ast.android.module.util.execute.ExecuteType;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class CacheController {

    public static void downloadCachedData() throws Exception {
        try {
            ExecuteManager.getInstance().execute(ExecuteType.AST_TYPE_DATA_DOWNLOAD);
            ExecuteManager.getInstance().execute(ExecuteType.AST_TYPE_DATA_DOWNLOADED);
            ExecuteManager.getInstance().execute(ExecuteType.BST_TYPE_DATA_DOWNLOAD);
            ExecuteManager.getInstance().execute(ExecuteType.BST_TYPE_DATA_DOWNLOADED);
        } catch (Exception e) {
            writeLog("CacheController   : downloadCachedData() ", e);
        }


    }

    public static void downloadWorkorders() throws Exception {
        try {
        ExecuteManager.getInstance().execute(ExecuteType.DOWNLOAD_WORKORDERS);
        } catch (Exception e) {
            writeLog("CacheController   : downloadWorkorders() ", e);
        }
    }

}
