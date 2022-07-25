package com.capgemini.sesp.ast.android.ui.activity.document_download;

import android.content.Context;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by svadera on 4/18/2018.
 * This Adapter is used to poopulate the document names into the list view
 */
public class DocumentAdapter extends BaseAdapter {
    Context context;
    List<DocumentList> linkedList;
    protected LayoutInflater documentRow;
    private String[] downloadedFiles;
    private boolean[] checkBoxState = null;
    private ArrayMap<DocumentList, Boolean> checkedForDocument = new ArrayMap<>();

    public DocumentAdapter(Context context, List<DocumentList> linkedList, String[] downloadedFiles) {
        this.context = context;
        this.linkedList = linkedList;
        this.downloadedFiles = downloadedFiles;
        this.documentRow = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return linkedList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder documentViewHolder;
        boolean check = false;
        try{
        if (convertView == null) {
            convertView = documentRow.inflate(R.layout.document_list_row, parent, false);
            documentViewHolder = new ViewHolder();
            documentViewHolder.documentName = convertView.findViewById(R.id.document_list);
            documentViewHolder.documentCheckbox = convertView.findViewById(R.id.document_checkbox);
            convertView.setTag(documentViewHolder);
        } else {
            documentViewHolder = (ViewHolder) convertView.getTag();
        }
        final DocumentList docs = linkedList.get(position);
        documentViewHolder.documentName.setText(docs.getDocumentAliasName());
        checkBoxState = new boolean[linkedList.size()];
        //TO find whether the docwnloaded documents names matches the original document list
        for (int i = 0; i < downloadedFiles.length; i++) {
            if (docs.getDocumentAliasName().equals(downloadedFiles[i])) {
                check = true;
            }
        }
        //If the dowloaded document name  matches then the checkbox should be made invisible
        if (check) {
            documentViewHolder.documentCheckbox.setVisibility(View.INVISIBLE);
        } else {
            if (checkBoxState != null)
                documentViewHolder.documentCheckbox.setChecked(checkBoxState[position]);
            documentViewHolder.documentCheckbox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    boolean checkStatus = false;
                    if (((CheckBox) v).isChecked()) {
                        checkStatus = true;

                    }
                    checkBoxState[position] = checkStatus;
                    isChecked(position, checkStatus);

                }

            });
            if (checkedForDocument.get(docs) != null) {
                documentViewHolder.documentCheckbox.setChecked(checkedForDocument.get(docs));
            }
            documentViewHolder.documentCheckbox.setTag(docs);
        }
        } catch (Exception e) {
            writeLog("DocumentAdapter  : getView() ", e);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView documentName;
        CheckBox documentCheckbox;
    }

    public void isChecked(int position, boolean flag) {
        checkedForDocument.put(this.linkedList.get(position), flag);
    }

    // This is to store the selected document name for download
    public LinkedList<String> getSelectedDocumentNames() {

        LinkedList<String> list = new LinkedList<>();
        try {
            for (Map.Entry<DocumentList, Boolean> pair : checkedForDocument.entrySet()) {
                if (pair.getValue()) {
                    list.add(pair.getKey().getDocumentAliasName());
                }
            }
        } catch (Exception e) {
            writeLog("DocumentAdapter  : getSelectedDocumentNames() ", e);
        }
        return list;
    }
}




