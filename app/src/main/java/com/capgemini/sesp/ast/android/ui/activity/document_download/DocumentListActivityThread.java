package com.capgemini.sesp.ast.android.ui.activity.document_download;

import android.app.Activity;
import android.content.Intent;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.skvader.rsp.ast_sep.common.to.ast.table.FieldDocumentInfoTTO;

import java.io.Serializable;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;



public class DocumentListActivityThread extends GuiWorker<List<FieldDocumentInfoTTO>> {
    private Activity ownerActivity;
    String documentType;

    public DocumentListActivityThread(Activity ownerActivity, String documentType) {
        super(ownerActivity);
        this.ownerActivity = ownerActivity;
        this.documentType = documentType;

    }

    @Override
    protected List<FieldDocumentInfoTTO> runInBackground() throws Exception {
        List<FieldDocumentInfoTTO> val = null;

            setMessage(R.string.fetching_document_list);
            String username = SessionState.getInstance().getCurrentUserUsername();
            val = AndroidUtilsAstSep.getDelegate().getWorkOrderDocumentInfoList(username, documentType);

        return val;
    }

    @Override
    protected void onPostExecute(boolean successful, List<FieldDocumentInfoTTO> result) {
        try{
        if (successful) {
            final Intent intent = new Intent(ownerActivity, DocumentListActivity.class);
            intent.putExtra("selectedMenuItem", documentType);
            intent.putExtra("FieldDocumentInfoTTO", (Serializable) result);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ownerActivity.startActivity(intent);
        }
        } catch (Exception e) {
            writeLog("DocumentListActivityThread  :onPostExecute()", e);
        }
    }

}




