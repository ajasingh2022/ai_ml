package com.capgemini.sesp.ast.android.module.util.task;

import com.capgemini.sesp.ast.android.module.util.execute.ExecuteManager;
import com.capgemini.sesp.ast.android.module.util.execute.ExecuteTask;
import com.capgemini.sesp.ast.android.module.util.execute.ExecuteType;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class AndroidInitAstSepExecuteTask extends ExecuteTask {

	public AndroidInitAstSepExecuteTask() {
	    super(ExecuteType.AST_APPLICATION_STARTED);
    }

	@Override
    public void run() {
		try{
		ExecuteManager.getInstance().registerTask(new DownloadCftAstConstantsExecuteTask());
		ExecuteManager.getInstance().registerTask(new AndroidTypeDataDownloadedAstSepExecuteTask());
		} catch (Exception e) {
			writeLog("AndroidInitAstSepExecuteTask : run()", e);
		}
    }

}
