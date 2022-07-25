package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.flow.WoAttachmentCompletionCallback;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.to.AndroidWOAttachmentBean;
import com.capgemini.sesp.ast.android.ui.adapter.AttachmentAdapter;
import com.capgemini.sesp.ast.android.ui.adapter.AttachmentTypeListAdapter;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.to.cft.table.AttachmentTTO;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/21/2018.
 */
@SuppressLint("ValidFragment")
public class StandardWoAttachFileFragment
        extends CustomFragment implements WoAttachmentCompletionCallback {


    List<String> fileNames = new ArrayList<String>();
    List<AndroidWOAttachmentBean> beanList = new ArrayList<AndroidWOAttachmentBean>();

    private transient ListView selectedFilesLv = null;

    private transient long caseId;
    private transient long caseTypeId;
    private transient String fieldVisitId;
    private static String TAG = StandardWoAttachFileFragment.class.getSimpleName();
    private transient Dialog popupDialog = null;
    private transient DatabaseHandler sqliteHandler = null;

    private transient BaseAdapter selectedFilesAdapter = null;

    private transient boolean pageFlowStarted = false;
    private transient File[] filesInDevice = null;
    private ListView existinggFiles;

    public StandardWoAttachFileFragment() {
        super();
    }

    public StandardWoAttachFileFragment(String st) {
        super(st);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);
        final Intent launcherIntent = getActivity().getIntent();
        if(!pageFlowStarted && launcherIntent != null) {
            final WorkorderCustomWrapperTO wo = ((AbstractWokOrderActivity) getActivity()).workorderCustomWrapperTOUntouched;
            if (wo != null) {
                this.caseId = wo.getIdCase();
                this.caseTypeId = wo.getIdCaseType();
                this.fieldVisitId = wo.getFieldVisitID();
            }
        }
        final Comparator<File> fileComparator = new FileComparator();
        if (getFileDir() != null && !"".equals(getFileDir().trim())) {
            final File loc = new File(getFileDir());
            if (loc.exists()) {
                this.filesInDevice = loc.listFiles();
            }
        }

        if (this.filesInDevice != null && this.filesInDevice.length > 0) {
            Arrays.sort(this.filesInDevice, fileComparator);
        }
        } catch (Exception e) {
            writeLog(TAG + " : onCreate()", e);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.std_wo_add_attachment_fragment, container,
                false);
        initializePageValues();
        initViews();
        setUpListeners();
        return fragmentView;
    }

    private void setUpListeners() {
        existinggFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (filesInDevice != null) {
                    final File file = filesInDevice[position];
                    if (file != null) {
                        if(!isFileAdded(file.getAbsolutePath())){
                            showAttachmentTypeChooserPopup(file.getAbsolutePath());
                        }else {
                            Toast.makeText(getActivity(),getActivity().getString(R.string.file_already_attached) , Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        selectedFilesLv.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


    }

    private void initViews() {
        existinggFiles = fragmentView.findViewById(R.id.existingFilesLv);
        if (existinggFiles != null) {
            if (this.caseId == 0) {
                final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
                this.caseId = wo.getIdCase();
                this.caseTypeId = wo.getIdCaseType();
            }
            final LayoutInflater inflater = (LayoutInflater) fragmentView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            existinggFiles.setAdapter(new AttachmentAdapter(filesInDevice,inflater));

        }

        selectedFilesLv = fragmentView.findViewById(R.id.selectedFilesLv);
        if (selectedFilesLv != null) {
            beanList = getAndroidWoAttachmentList(caseId, caseTypeId);
            selectedFilesAdapter = new SelectedFileAdapter(getActivity(), beanList);
            selectedFilesLv.setAdapter(selectedFilesAdapter);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
/*        final ListView listView = (ListView) getView().findViewById(R.id.existingFilesLv);
        if (listView != null) {
            if (this.caseId == 0) {
                final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
                this.caseId = wo.getIdCase();
                this.caseTypeId = wo.getIdCaseType();
            }
            final LayoutInflater inflater = (LayoutInflater) getView().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listView.setAdapter(new AttachmentAdapter(filesInDevice,inflater));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (filesInDevice != null) {
                        final File file = filesInDevice[position];
                        if (file != null) {
                            if(!isFileAdded(file.getAbsolutePath())){
                                showAttachmentTypeChooserPopup(file.getAbsolutePath());
                            }else {
                                Toast.makeText(getActivity(),getActivity().getString(R.string.file_already_attached) , Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }*/
    }

    public String getFileDir() {
        return AndroidUtilsAstSep.getAppImageFolder(ConstantsAstSep.OrderHandlerConstants.DEFAULT_ATTACHMENT_DIR_NAME);
    }

    public boolean isFileAdded(String fileName) {
        return fileNames.contains(fileName);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Initialize the view objects





    }

    public void onPause() {
        super.onPause();
    }

    public DatabaseHandler getSqliteHandler() {
        if (sqliteHandler == null) {
            sqliteHandler = DatabaseHandler.createDatabaseHandler();
        }
        return sqliteHandler;
    }

    /**
     * Utility method to build up the existing list of attachments selected
     * for this workorder
     */
    private List<AndroidWOAttachmentBean> getAndroidWoAttachmentList(final long caseId, final long caseTypeId) {

        final List<AndroidWOAttachmentBean> list = getSqliteHandler().getAttachment(caseId, caseTypeId, 0, null);
        return list;
    }

    @SuppressLint("LongLogTag")
    private void showAttachmentTypeChooserPopup(final String imageFileName) {
        try{
        final View popupView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.attachment_type_popup_layout, null);
        Log.d("StandardWoAttachFileFragment", "Inflating attachment_type_popup_layout");
        if (popupView != null
                && popupView.findViewById(R.id.cancelButton) != null
                && popupView.findViewById(R.id.attachmentTypeView) != null) {

            final List<AttachmentTTO> attachmentTTOs = ObjectCache.getAllTypes(AttachmentTTO.class);

            final ListView attachmentTypeListView = popupView.findViewById(R.id.attachmentTypeView);
            // =================================================================================

            final Button cancelButton = popupView.findViewById(R.id.cancelButton);

            cancelButton.setBackgroundColor(getResources().getColor(R.color.colorDisable));
            cancelButton.setEnabled(false);
            cancelButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    popupDialog.dismiss();
                }
            });

            final AlertDialog.Builder builder
                    = GuiController.getCustomAlertDialog(getActivity(), popupView, null, null);
            popupDialog = builder.create();
            popupDialog.setCancelable(false);

            attachmentTypeListView.setAdapter(
                    new AttachmentTypeListAdapter(this,
                            attachmentTTOs, imageFileName, fieldVisitId, caseId, caseTypeId, popupDialog));
            popupDialog.show();
        }
        } catch (Exception e) {
            writeLog(TAG + " : showAttachmentTypeChooserPopup()", e);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onAttachmentTypeChoosenCompleted() {
        Log.d("StandardWoAttachFileFragment-onAttachmentTypeChoosenCompleted", "onAttachmentTypeChoosenCompleted of StandardWoAttachFileFragment getting called");
        Log.d("StandardWoAttachFileFragment-onAttachmentTypeChoosenCompleted", "Updating selectedFilesAdapter");
        // Update the adapter for selected files

        if (selectedFilesAdapter != null) {
            Log.d("StandardWoAttachFileFragment-onAttachmentTypeChoosenCompleted", "Applying new selected attachment list");
            beanList = getAndroidWoAttachmentList(caseId, caseTypeId);
            if (beanList != null) {
                fileNames.clear();
                for (AndroidWOAttachmentBean bean : beanList) {
                    fileNames.add(bean.getFileName());
                }
                ((SelectedFileAdapter) selectedFilesAdapter).setAndroidWOAttachmentBean(beanList);
                Log.d("StandardWoAttachFileFragment-onAttachmentTypeChoosenCompleted", "Updating data set for selected file list");
                selectedFilesAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showPromptUserAction() {
        //Do nothing
    }

    @Override
    public boolean validateUserInput() {
        return true;
    }


    /**
     * ============== private adapter to populate list of selected files ==================
     */
    private class SelectedFileAdapter extends BaseAdapter implements View.OnClickListener {

		/*
         * Related type tables
		 *
		 * ATTACHMENT_T
		 * ATTACHMENT_MIME_T
		 * ATTACHMENT_REASON_T
		 *
		 * Related data tables
		 *
		 * FIELD_TRANS
		 * FIELD_TRANS_ATTACHMENT_PART
		 * CASE_ATTACHMENT
		 * ATTACHMENT
		 * ATTACHMENT_REASON
		 *
		 */

        private transient SoftReference<Context> contextRef = null;
        private transient List<AndroidWOAttachmentBean> androidWOAttachmentBean = null;

        public SelectedFileAdapter(final Context context, final List<AndroidWOAttachmentBean> androidWOAttachmentBean) {
            if (context != null) {
                this.contextRef = new SoftReference<Context>(context);
            }

            if (androidWOAttachmentBean != null && !androidWOAttachmentBean.isEmpty()) {
                this.androidWOAttachmentBean = androidWOAttachmentBean;
            }
        }

        @Override
        public int getCount() {
            int count = 0;
            try{
            if (androidWOAttachmentBean != null) {
                count = androidWOAttachmentBean.size();
            }
        } catch (Exception e) {
            writeLog(TAG + " : getCount()", e);
        }
            return count;
        }

        @Override
        public Object getItem(int position) {
            AndroidWOAttachmentBean item = null;
            try{
            if (androidWOAttachmentBean != null && androidWOAttachmentBean.size() > position) {
                item = androidWOAttachmentBean.get(position);
            }
            } catch (Exception e) {
                writeLog(TAG + " : getItem()", e);
            }
            return item;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        //To be called by the parent fragment to update the list based on user action
        public void setAndroidWOAttachmentBean(List<AndroidWOAttachmentBean> androidWOAttachmentBean) {
            this.androidWOAttachmentBean = androidWOAttachmentBean;
        }

        @SuppressLint("LongLogTag")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d("SelectedFileAdapter-getView", "SelectedFileAdapter-getView being called with position=" + position);
            View vw = convertView;
            try{
            ViewHolder holder;
            if (vw == null) {
                final LayoutInflater inflater = (LayoutInflater) contextRef.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vw = inflater.inflate(R.layout.wo_attachment_list_row_layout, null);
                holder = new ViewHolder();
                // Lookup the view elements
                holder.woImageIcon = vw.findViewById(R.id.woImageIcon);
                holder.fileNameTv = vw.findViewById(R.id.fileName);
                holder.attachmentTypeTv = vw.findViewById(R.id.attachmentTypeTv);
                holder.removeAttachmentBtn = vw.findViewById(R.id.removeAttachmentBtn);
                vw.setTag(holder);

            } else {
                holder = (ViewHolder) vw.getTag();
            }

            if (contextRef != null && contextRef.get() != null) {
                try {
                    final AndroidWOAttachmentBean item = (AndroidWOAttachmentBean) getItem(position);
                    if (item != null) {

                        // Populate the attachment Type
                        if (item.getAttachmentTTO() != null) {
                            holder.attachmentTypeTv.setText(item.getAttachmentTTO().getName());
                        }

                        holder.fileNameTv.setText(item.getFileName().substring(item.getFileName().lastIndexOf("/")+1,item.getFileName().length()));
                        // setup listener to remove/discard this attachment
                        holder.removeAttachmentBtn.setOnClickListener(this);
                        // Tag the object
                        holder.removeAttachmentBtn.setTag(item);
                    }
                } catch (final Exception ex) {
                    writeLog(TAG + "  :SelectedPhotoAdapter  :getView",ex);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " : getView()", e);
        }
            return vw;
        }

        @Override
        public void onClick(View v) {
            try{
            if (v != null && v.getId() == R.id.removeAttachmentBtn) {
                if (v.getTag() != null && v.getTag() instanceof AndroidWOAttachmentBean) {
                    final AndroidWOAttachmentBean item = (AndroidWOAttachmentBean) v.getTag();
                    if (androidWOAttachmentBean != null && this.contextRef != null && this.contextRef.get() != null) {
                        androidWOAttachmentBean.remove(item);
                        beanList.remove(item);
                        fileNames.remove(item.getFileName());
                        //Delete from db also
                        getSqliteHandler().removeAttchment(item.getCaseId(), item.getCaseTypeId(), item.getFileName(), item.getAttachmentTTO().getId(), null);
                        this.notifyDataSetChanged();
                    }
                }
            } else if (v != null && v.getId() == R.id.cancelButton && popupDialog != null) {
                Log.d("StandardWoAttachFileFragment-onClick", "cancel button is called");
                popupDialog.dismiss();
            }
            } catch (Exception e) {
                writeLog(TAG + " : onClick()", e);
            }
        }

    }
    //To recycle the list view
    private class ViewHolder {

        ImageView woImageIcon;
        TextView fileNameTv;
        TextView attachmentTypeTv;
        ImageButton removeAttachmentBtn;


    }

    /**
     * Custom comparator to compare the file list based on last modified
     * first only
     */
    private class FileComparator implements Comparator<File> {

        @Override
        public int compare(final File lhs, final File rhs) {
            int result = 0;
            try{
            if (lhs != null && rhs != null) {
                if (lhs.lastModified() > rhs.lastModified()) {
                    result = -1;
                } else if (lhs.lastModified() < rhs.lastModified()) {
                    result = 1;
                }
            }
            } catch (Exception e) {
                writeLog(TAG + " : compare()", e);
            }
            return result;
        }

    }

}