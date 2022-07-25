package com.capgemini.sesp.ast.android.ui.service;

import android.os.AsyncTask;

import com.capgemini.sesp.ast.android.module.communication.CommunicationHelper;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.to.AndroidWOAttachmentBean;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


public class AttachmentSyncService extends AsyncTask<Void, Void, Boolean> {

    private transient long caseId = -1;
    private transient String fieldVisitId = null;
    private final int MAX_ATTEMPTS = 3;
    private AsyncResponse<Boolean> delegate = null;


    public AttachmentSyncService(long idCase, String fieldVisitId, AsyncResponse<Boolean> delegate) {
        this.caseId = idCase;
        this.fieldVisitId = fieldVisitId;
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return performAttachmentSync();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        delegate.processFinish(aBoolean);
    }

    private boolean performAttachmentSync() {
        boolean allAttachmentsSynched = true;
        try {
            List<AndroidWOAttachmentBean> androidWOAttachmentBeanList = DatabaseHandler.createDatabaseHandler().getAttachment(caseId, 0L, 0L, null);
            for (AndroidWOAttachmentBean androidWOAttachmentBean : androidWOAttachmentBeanList) {
                int numberOfAttempts = 1; //Max number of attempts to upload an attachment
                while (numberOfAttempts <= MAX_ATTEMPTS) {
                    try {
                        CommunicationHelper communicationHelper = new CommunicationHelper();
                        int serverResponseCode = communicationHelper.uploadAttachmentCallWebservice(androidWOAttachmentBean, fieldVisitId);
                        if (serverResponseCode >= CommunicationHelper.SUCCESSFUL_OK && serverResponseCode <= CommunicationHelper.SUCCESSFUL_OK_MAX) {
                            //Remove attachment from WO_ATTACHMENTS table after verifying upload is successful
                            DatabaseHandler.createDatabaseHandler().removeAttchment(
                                    androidWOAttachmentBean.getCaseId(),
                                    androidWOAttachmentBean.getCaseTypeId(),
                                    androidWOAttachmentBean.getFileName(),
                                    androidWOAttachmentBean.getAttachmentTypeId(),
                                    androidWOAttachmentBean.getReasonTypeId());
                        }
                        break;
                    } catch (Exception e) {
                        writeLog("AttachmentSyncService : performAttachmentSync()", e);
                        numberOfAttempts++;
                    }
                }
                if (numberOfAttempts > MAX_ATTEMPTS) allAttachmentsSynched = false;
            }
        } catch (Exception e) {
            writeLog("AttachmentSyncService : performAttachmentSync()", e);
        }
        return allAttachmentsSynched;
    }
}
