package com.capgemini.sesp.ast.android.module.util;

import android.content.Context;

import com.capgemini.sesp.ast.android.module.communication.CommunicationHelper;
import com.capgemini.sesp.ast.android.module.util.exception.SespExceptionHandler;
import com.skvader.rsp.cft.common.to.cft.table.ErrorTO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class SendErrorLogsThread extends Thread {
	
	private String[] files; // List of all files SESP created
	private Context ctx;

	public SendErrorLogsThread(Context ctx, String[] files) {
		this.files = files;
		this.ctx = ctx;
	}

	@Override
    public void run() {
		try {
			List<String> stacktraceFileNames = new ArrayList<String>();
	        for (String file : files) {
	        	if (file.endsWith(SespExceptionHandler.STACKTRACE_FILE_ENDING)) {
	        		stacktraceFileNames.add(file);
	        	}
	        }
	        
	        for (String stacktraceFileName : stacktraceFileNames) {
	            String line = "";
	        	StringBuffer fileContent = new StringBuffer();			
	        	InputStream is = ctx.openFileInput(stacktraceFileName);
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        	if (is!=null) {							
	        		while ((line  = reader.readLine()) != null) {	
	        			fileContent.append(line  + "\n" );
	        		}
	        		is.close();
	        	}
				ErrorTO errorTO = CommunicationHelper.JSONMAPPER.readValue(fileContent.toString(), ErrorTO.class);
	            AndroidUtilsAstSep.getDelegate().logError(errorTO);
	            ctx.deleteFile(stacktraceFileName);
	        }
		} catch (FileNotFoundException e) {
			writeLog("SendErrorLogsThread :run() ", e);
		} catch (Exception e) {
			writeLog("SendErrorLogsThread :run() ", e);

		}
	}
}