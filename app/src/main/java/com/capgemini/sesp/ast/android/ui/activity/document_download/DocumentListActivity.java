
package com.capgemini.sesp.ast.android.ui.activity.document_download;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.DocumentDirectoryOperation;
import com.capgemini.sesp.ast.android.module.util.DocumentDownloadTask;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.skvader.rsp.ast_sep.common.to.ast.table.FieldDocumentInfoTTO;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


/**
 * Created by svadera on 4/13/2018.
 * This activity class is used for initiating the activity or class to perform the directory operation,
 * to perform downloading documents and populating the list
 */

public class DocumentListActivity extends Activity {
    ListView listView;
    private LinkedHashSet<String> documentHashSet = new LinkedHashSet<>();
    private List<DocumentList> documentLinkedList = new LinkedList<>();
    private LinkedList<String> selectedDocumentNames = new LinkedList<>();
    private DocumentDirectoryOperation documentDirectory;
    private String lastLoginUserName;
    private String documentType;
    String[] downloadedFiles;
    DocumentAdapter documentAdapter;
    private ImageButton button;
    String baseURL = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_document);
        Bundle b = getIntent().getExtras();
        TextView name = findViewById(R.id.title_text);
        String docType = b.getString("selectedMenuItem");
        if (docType.equalsIgnoreCase(ConstantsAstSep.DocumentDownloadAction.INSTALLATION_DOC_TYPE)) {
            name.setText(R.string.title_installation_document);
            DocumentList.setDocType(docType);
        } else {
            name.setText(R.string.title_safety_document);
            DocumentList.setDocType(docType);

        }
        listView = findViewById(R.id.list_document);
        button = findViewById(R.id.option_download);
        List<FieldDocumentInfoTTO> documentTO = (List<FieldDocumentInfoTTO>) getIntent().getSerializableExtra("FieldDocumentInfoTTO");
        //To Store the TO into the local database
        DatabaseHandler.createDatabaseHandler().saveWoDocumentLiteTO(documentTO);
        //To get the TO from the local database
        List<WoDocumentLiteTO> testList = DatabaseHandler.createDatabaseHandler().getWoDocumentLiteTO();
        //folder creation and deletion according to the username
        String currentUserName = SessionState.getInstance().getCurrentUserUsername();
        lastLoginUserName = DocumentList.getLastLoginUser();
        documentType = DocumentList.getDocType();
        documentDirectory = new DocumentDirectoryOperation(this);
        if (lastLoginUserName == null || currentUserName.equals(lastLoginUserName)) {
            boolean check = documentDirectory.FolderCreate(documentType);
            if (check) {
                Log.i("DocumentListActivity", "folder created");
            } else {
                Log.i("DocumentListActivity", "folder not created");
            }
        } else if (!currentUserName.equals(lastLoginUserName)) {
            String myfolder = Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.wo_documents);
            documentDirectory.deleteDocumentDirectory(new File(myfolder));
            documentDirectory.FolderCreate(documentType);

        }

        for (WoDocumentLiteTO liteVal : testList) {
            documentHashSet.add(liteVal.getDocumentAliasName());
        }
        for (String str : documentHashSet) {
            DocumentList docs = new DocumentList();
            docs.setDocumentAliasName(str);
            documentLinkedList.add(docs);
        }
        //To perform downloading of documents
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SessionState.getInstance().isLoggedInOnline()) {

                    try {
                        baseURL = AndroidUtilsAstSep.getProperty("fieldorder_doc_download_url", getApplicationContext());
                    } catch (IOException iox) {
                        writeLog("DocumentListActivity :onCreate" ,iox);
                    }
                    selectedDocumentNames = documentAdapter.getSelectedDocumentNames();
                    List<String> selectedURL = DatabaseHandler.createDatabaseHandler().getSelectedURL(selectedDocumentNames);
                    if (selectedDocumentNames.size() != 0) {
                        final DocumentDownloadTask downloadTask = new DocumentDownloadTask(DocumentListActivity.this, selectedURL);
                        downloadTask.execute(baseURL);
                    } else {
                        Toast.makeText(DocumentListActivity.this, getResources().getString(R.string.checkbox_not_select_message), Toast.LENGTH_LONG).show();
                    }
                } else
                    Toast.makeText(DocumentListActivity.this, getResources().getString(R.string.offline_message), Toast.LENGTH_LONG).show();


            }
        });
        //TO make the pdf viewer to open the selected document
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DocumentList docs = documentLinkedList.get(position);
                String selectedDocumentName = docs.getDocumentAliasName();
                File pdfFile = new File(documentDirectory.getDocumentType(documentType) + "/" + selectedDocumentName + ConstantsAstSep.DocumentDownloadAction.PDF_EXTENSION);
                try {
                    if (pdfFile.exists()) {
                        Uri path = Uri.fromFile(pdfFile);
                        Intent objIntent = new Intent(Intent.ACTION_VIEW);
                        objIntent.setDataAndType(path, "application/pdf");
                        objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(objIntent);
                    } else {
                        Toast.makeText(DocumentListActivity.this, getResources().getString(R.string.file_not_found), Toast.LENGTH_SHORT).show();
                    }
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(DocumentListActivity.this, getResources().getString(R.string.no_viewer_application_found), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                   writeLog("DocumentListActivity :onCreate" ,e);
                }
            }
        });
        getDownloadedFileNames();
        DocumentList.setLastLoginUser(currentUserName);
        documentAdapter = new DocumentAdapter(DocumentListActivity.this, documentLinkedList, downloadedFiles);
        listView.setAdapter(documentAdapter);
    } catch (Exception e) {
        writeLog("DocumentListActivity  :onCreate()", e);
    }

    }

    //lists the available documents in the directory and splits the document name from .pdf extenstion
    public void getDownloadedFileNames() {
        try {
            downloadedFiles = documentDirectory.listAvailableDocuments(documentType);
            for (int i = 0; i < downloadedFiles.length; i++) {
                String[] actualName = downloadedFiles[i].split(ConstantsAstSep.DocumentDownloadAction.PDF_EXTENSION);
                downloadedFiles[i] = actualName[0];
            }
        } catch (Exception e) {
           writeLog("DocumentListActivity  :getDownloadedFileNames()", e);
        }

    }

}


