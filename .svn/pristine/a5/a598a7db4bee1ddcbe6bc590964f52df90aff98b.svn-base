package com.capgemini.sesp.ast.android.ui.activity.add_attachments;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class AddAttachmentsActivity extends AppCompatActivity implements OnClickListener {

    private ListView fileList;
    private AttachmentAdapter adapter;

    private ArrayList<String> filesOnDevice;
    private ArrayList<String> attachmentTypeList;
    private ArrayMap<Integer, Integer> selectedItemsInSpinner;
    private static String TAG = AddAttachmentsActivity.class.getSimpleName();
    private static final String DIRECTORY_PATH = "/sesp/attachment_temp/";

    private ImageButton forwardButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attachments);

        forwardButton = findViewById(R.id.forward);
        forwardButton.setOnClickListener(this);
        backButton = findViewById(R.id.back);
        backButton.setOnClickListener(this);

        filesOnDevice = getFilesInDirectory();

        fileList = findViewById(R.id.attachments_listview);
        if (filesOnDevice != null && filesOnDevice.size() > 0) {
            adapter = new AttachmentAdapter(this, R.layout.add_attachments_list_item, filesOnDevice);
            fileList.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_attachments, menu);
        return true;
    }


    private ArrayList<String> getFilesInDirectory() {
        try {
            File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DIRECTORY_PATH);
            File[] files = directory.listFiles();
            ArrayList<String> temp = new ArrayList<String>();

            if (files != null) {
                for (File f : files) {
                    temp.add(f.getName());
                }
                return temp;
            }
        } catch (Exception e) {
            writeLog(TAG + " : getFilesInDirectory()", e);
        }
        return null;
    }

    /**
     * TODO When uploading a file to the database. Grab the entire file and send it.
     * Will probably have to use Outputstream instead of "File".
     *
     * @param position
     * @return
     */
    private File retrieveFile(int position) {
        return null;
    }

    /**
     * TODO Implement the deletion of a file on the filesystem.
     *
     * @param fileName
     * @param position
     */
    private boolean deleteFile(String fileName, int position) {
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DIRECTORY_PATH,
                    fileName);
        } catch (Exception e) {
            writeLog(TAG + "  : deleteFile() ", e);
        }

        if (file.delete()) {
            filesOnDevice.remove(position);
            return true;
        } else {
            Toast.makeText(this, R.string.could_not_delete, Toast.LENGTH_SHORT).show();
            return false;

        }
    }

    /**
     * TODO Save all the attachments and their attachment type to the database.
     * Remove all the files when done..?
     */
    private void saveToDatabase() {

    }

    /**
     * TODO Implement navigation between activities.
     */

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.forward) {

        } else if (v.getId() == R.id.back) {

        }
    }

    public class AttachmentAdapter extends ArrayAdapter<String> {

        private Context context;
        private ArrayAdapter<String> spinnerAdapter;


        public AttachmentAdapter(Context context, int resource,
                                 List<String> objects) {
            super(context, resource, objects);
            this.context = context;

            attachmentTypeList = setupAttachmentList();
            spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
                    attachmentTypeList);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Just to preselect the items in the spinners.
            // This will be used later to save the selections in each spinner.
            selectedItemsInSpinner = new ArrayMap<Integer, Integer>();
            for (int i = 0; i < filesOnDevice.size(); i++) {
                selectedItemsInSpinner.put(i, 2);
            }
        }

        private ArrayList<String> setupAttachmentList() {
            ArrayList<String> tempList = null;
            try {
                String[] temp = context.getResources().getStringArray(R.array.attachment_type);
                tempList = new ArrayList<String>();

                for (String s : temp) {
                    tempList.add(s);
                }
            } catch (Exception e) {
                writeLog(TAG + "  : setupAttachmentList() ", e);
            }
            return tempList;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.add_attachments_list_item, parent, false);
        try{
            TextView nameTextView = rowView.findViewById(R.id.add_attachment_filename_textview);
            nameTextView.setText(filesOnDevice.get(position));

            final Spinner attachmentSpinner = rowView.findViewById(R.id.add_attachment_attachment_type_spinner);
            attachmentSpinner.setAdapter(spinnerAdapter);

            if (selectedItemsInSpinner.get(position) != null) {
                attachmentSpinner.setSelection(selectedItemsInSpinner.get(position));
            }


            attachmentSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View v,
                                           int pos, long id) {

                    selectedItemsInSpinner.put(position, pos);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Button deleteButton = rowView.findViewById(R.id.add_attachment_delete_button);
            deleteButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), filesOnDevice.get(position), Toast.LENGTH_SHORT).show();
                    if (deleteFile(filesOnDevice.get(position), position)) {
                        selectedItemsInSpinner.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        } catch (Exception e) {
            writeLog(TAG + " : getView()", e);
        }
            return rowView;
        }
    }
}
