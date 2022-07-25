/**
 * 
 */
package com.capgemini.sesp.ast.android.module.util.task;

import android.util.Log;

import java.io.File;

/**
 * @author nirmchak
 *
 */
public class WoAttachmentTask implements Runnable {
	
	private transient long caseId;
	private transient long caseTypeId;
	private transient File file = null;
	private transient boolean addAttachment = false;
	private transient long attachmentMimeTypeId = 0;
	private transient long reasonTypeId = 0;	
	
	public WoAttachmentTask(final long caseId, 
			final long caseTypeId, final File file, final boolean addAttachment, 
			final long attachmentMimeTypeId, final long reasonTypeId){
		this.caseId = caseId;
		this.caseTypeId = caseTypeId;
		this.file = file;
		this.addAttachment = addAttachment;
		this.attachmentMimeTypeId = attachmentMimeTypeId;
		this.reasonTypeId = reasonTypeId;
	}

	@Override
	public void run() {
		if(caseId>0 && caseTypeId>0 && file!=null){
			if(addAttachment){
				Log.d("WoAttachmentTask","Adding attachment "+file.getAbsolutePath()+" for caseid="+this.caseId+" , case typeid="+this.caseTypeId);
				/*DatabaseHandler.createDatabaseHandler()
					.addAttachment(caseId, caseTypeId, file.getAbsolutePath(),attachmentMimeTypeId,reasonTypeId);*/
			}else{
				Log.d("WoAttachmentTask","Adding attachment "+file.getAbsolutePath()+" for caseid="+this.caseId+" , case typeid="+this.caseTypeId);
				//DatabaseHandler.createDatabaseHandler().removeAttchment(caseId, caseTypeId, file.getAbsolutePath(),attachmentMimeTypeId,reasonTypeId);
			}
		}
	}

}
