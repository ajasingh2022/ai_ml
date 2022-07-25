package com.capgemini.sesp.ast.android.module.util;

import android.content.Context;
import android.os.Environment;

import com.capgemini.sesp.ast.android.R;

import java.io.File;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by svadera on 4/23/2018.
 */
public class DocumentDirectoryOperation {
    String myfolder;
    String subFolder;
    private Context context;

    public DocumentDirectoryOperation(Context context) {
        this.context = context;
    }
    static String TAG =DocumentDirectoryOperation.class.getSimpleName();

    public String getDocumentType(String type) {
        try{
        myfolder = Environment.getExternalStorageDirectory() + "/" + context.getResources().getString(R.string.wo_documents);
        if (type.equalsIgnoreCase(ConstantsAstSep.DocumentDownloadAction.INSTALLATION_DOC_TYPE)) {
            subFolder = myfolder + "/" + context.getResources().getString(R.string.installation_document);
            return subFolder;
        } else {
            subFolder = myfolder + "/" + context.getResources().getString(R.string.safety_document);
        }
        } catch (final Exception ex){
            writeLog(TAG + " :getDocumentType()", ex);
        }
        return subFolder;
    }

    public boolean FolderCreate(String documentType) {
        File f = new File(getDocumentType(documentType));
        try {
            if (!f.exists())
                return f.mkdirs();
        } catch (final Exception ex){
            writeLog(TAG + " :FolderCreate()", ex);
        }
        return true;
    }

    public boolean deleteDocumentDirectory(File dir) {
        try{
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDocumentDirectory(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        } catch (Exception ex) {
            writeLog(TAG + " :deleteDocumentDirectory()", ex);
        }
        // The directory is now empty so delete it
        return dir.delete();
    }

    public String[] listAvailableDocuments(String documentType) {
        File dir = new File(getDocumentType(documentType));
        String[] files = dir.list();
        try {
            if (files.length == 0) {
                return files;
            }
        } catch (Exception ex) {
            writeLog(TAG + " :FolderCreate()", ex);
        }
        return files;
    }
}
