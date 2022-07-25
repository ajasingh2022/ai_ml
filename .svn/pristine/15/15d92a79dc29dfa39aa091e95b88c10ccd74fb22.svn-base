package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.capgemini.sesp.ast.android.BuildConfig;
import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.communication.WorkflowImageCaptureCallbackListener;
import com.capgemini.sesp.ast.android.module.flow.WoImageCompletionCallback;
import com.capgemini.sesp.ast.android.module.others.AsyncDrawable;
import com.capgemini.sesp.ast.android.module.others.BitmapWorkerTask;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.module.util.to.AndroidWOAttachmentBean;
import com.capgemini.sesp.ast.android.ui.activity.flow.SESPFlowActivity;
import com.capgemini.sesp.ast.android.ui.adapter.ImageAdapter;
import com.capgemini.sesp.ast.android.ui.adapter.ImageReasonListAdapter;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.PdaCaseTHTAttRTCTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInfoCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.to.cft.table.AttachmentReasonTTO;
import com.skvader.rsp.cft.common.to.cft.table.AttachmentTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/21/2018.
 */
@SuppressLint({"ValidFragment", "InflateParams", "LongLogTag"})
public class StandardWoAttachImageFragment
        extends CustomFragment implements View.OnClickListener, View.OnTouchListener, WorkflowImageCaptureCallbackListener, WoImageCompletionCallback {

    private transient ListView selectedPicsLv = null;
    GridView gridView;
    private transient Button captureImageBtn = null;
    private transient Button useExistingImageBtn = null;

    private transient Drawable enablingDisabling = null;
    private transient Drawable defaultDrawable = null;

    private transient long caseId;
    private transient long caseTypeId;
    private transient String fieldVisitId;

    private transient Dialog popupDialog = null;
    private transient DatabaseHandler sqliteHandler = null;

    private transient BaseAdapter selectedPhotosAdapter = null;

    private transient boolean pageFlowStarted = false;
    protected FragmentActivity mActivity;
    private String TAG = StandardWoAttachImageFragment.class.getSimpleName();
    List<String> fileNames = new ArrayList<String>();
    List<AndroidWOAttachmentBean> beanList = new ArrayList<AndroidWOAttachmentBean>();
    private List<String> beanListOrg = new ArrayList<String>();
    Long idCaseTHandlerT = null;
    static final String DEFAULT_PHOTO_MAX_LIMIT = "2";
    private ImageAdapter imageAdapter;
    protected String errorString = null;
    protected String imageFileName = null;

    public StandardWoAttachImageFragment() {
        super();
    }

    public StandardWoAttachImageFragment(final long caseId, final long caseTypeId) {
        super();
        this.caseId = caseId;
        this.caseTypeId = caseTypeId;
    }

    public StandardWoAttachImageFragment(boolean pageFlowStarted) {
        super();
        this.pageFlowStarted = pageFlowStarted;
    }


    public StandardWoAttachImageFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.std_wo_image_capture_fragment, container, false);
        initializePageValues();
        initViews(savedInstanceState);
        setupListeners();
        populateView();
        return fragmentView;
    }


    public void initializePageValues() {
        //Do nothing
        try {
            WorkorderCustomWrapperTO customWrapperTO = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            beanListOrg.clear();
            idCaseTHandlerT = WorkorderUtils.findIdCaseTypeHandlerType(customWrapperTO.getIdCaseType());
            List<AttachmentTO> attachmentTOs = customWrapperTO.getWorkorderCustomTO().getAttachmentTOLs();
            if (attachmentTOs != null && !attachmentTOs.isEmpty()) {
                for (AttachmentTO attachmentTO : attachmentTOs) {
                    beanListOrg.add(attachmentTO.getObjectName());
                }
            }


            final Intent launcherIntent = getActivity().getIntent();
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if (wo != null) {
                this.caseId = wo.getIdCase();
                this.caseTypeId = wo.getIdCaseType();
                this.fieldVisitId = wo.getFieldVisitID();
            }
        } catch (Exception e) {
            writeLog(TAG + " : initializePageValues()", e);
        }
    }

    private void initViews(Bundle savedInstanceState) {
        gridView = fragmentView.findViewById(R.id.gridVw);
        selectedPicsLv = fragmentView.findViewById(R.id.selectedPicsLv);
        captureImageBtn = fragmentView.findViewById(R.id.captureImageBtn);
        useExistingImageBtn = fragmentView.findViewById(R.id.useExistingImageBtn);

        gridView.setColumnWidth(getImageGridWidth());
        imageAdapter = new ImageAdapter(this, getImageDir(), getExternImageDir(), fieldVisitId, caseId, caseTypeId);
        gridView.setAdapter(imageAdapter);
        if (imageAdapter.getCount() == 0)
            gridView.setVisibility(View.GONE);
        else
            gridView.setVisibility(View.VISIBLE);
        gridView.setFocusable(false);
        gridView.setMinimumHeight(gridView.getWidth());

        generatDialogueIfRequired(imageAdapter, savedInstanceState);

        if (selectedPicsLv != null) {
            if (beanList.size() > 0 && beanList.get(0).getCaseId().equals(caseId)) {
                fileNames.clear();
                for (AndroidWOAttachmentBean bean : beanList) {
                    fileNames.add(bean.getFileName());
                    getSqliteHandler().addAttachment(caseId, caseTypeId, fieldVisitId, bean.getFileName(), CONSTANTS().ATTACHMENT_T__PHOTO, AndroidUtilsAstSep.convertDelimitedStringToLongList(bean.getReasonTypeId(), ","));
                }
            } else {
                beanList = getAndroidWoAttachmentList(caseId, caseTypeId);
            }

            selectedPhotosAdapter = new SelectedPhotoAdapter(getActivity(), beanList);
            selectedPicsLv.setAdapter(selectedPhotosAdapter);
            if (selectedPhotosAdapter.getCount() == 0)
                selectedPicsLv.setVisibility(View.GONE);
            else
                selectedPicsLv.setVisibility(View.VISIBLE);
            selectedPicsLv.setFocusable(false);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void applyChangesToModifiableWO() {
        try {
            WorkorderCustomWrapperTO workorderCustomWrapperTO = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();

            List<AttachmentTO> attachmentTOList = workorderCustomWrapperTO.getWorkorderCustomTO().getAttachmentTOLs();
            if (attachmentTOList == null) {
                attachmentTOList = new ArrayList<AttachmentTO>();
            } else {
                attachmentTOList.clear();
            }
            for (AndroidWOAttachmentBean androidWOAttachmentBean : beanList) {
                AttachmentTO attachmentTO = new AttachmentTO();
                attachmentTO.setObjectName(androidWOAttachmentBean.getFileName());
                attachmentTO.setObjectSize(0L);
                attachmentTO.setIdAttachmentT(CONSTANTS().ATTACHMENT_T__PHOTO);
                attachmentTO.setIdAttachmentMimeT(CONSTANTS().ATTACHMENT_MIME_T__IMAGE_JPEG);
                attachmentTO.setFieldVisitId(workorderCustomWrapperTO.getFieldVisitID());
                attachmentTOList.add(attachmentTO);
            }
            workorderCustomWrapperTO.getWorkorderCustomTO().setAttachmentTOLs(attachmentTOList);
        } catch (Exception e) {
            writeLog(TAG + " : applyChangesToModifiableWO()", e);
        }
    }


    private boolean generatDialogueIfRequired(ImageAdapter imageAdapter, Bundle savedInstanceState) {
        List<File> lf = imageAdapter.getFilesWithwrongNameC();
        if (lf != null) {
            if (lf.size() != 0) {

                @SuppressLint("RestrictedApi")
                LayoutInflater inflater = getLayoutInflater(savedInstanceState);
                View dialogueTitle = inflater.inflate(R.layout.image_file_wrong_alert_dialogue_title, null);
                ((TextView) dialogueTitle.findViewById(R.id.title)).setText(
                        getResources().getString(R.string.warning) + ":" +
                                getResources().getString(R.string.invalid_filename));

                //Show message
                String s = String.format("%s :%d %s :%s",
                        getResources().getString(R.string.maximum_characters_allowed),
                        imageAdapter.maxCharacterAllowed,
                        getResources().getString(R.string.character_allowed_in_valid_file_name),
                        getResources().getString(R.string.letters_atozAtozNumber0to9and_));
                ((TextView) dialogueTitle.findViewById(R.id.msg)).setText(s);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setCustomTitle(dialogueTitle);
                //To show file names
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.single_choice_text_view);
                for (File f : lf) {
                    arrayAdapter.add(f.getName());
                }
                DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };
                alertDialog.setAdapter(arrayAdapter, null);
                alertDialog.setPositiveButton(getResources().getString(R.string.ok), okListener);
                alertDialog.setCancelable(false);
                alertDialog.create().show();
                return true;

            }

        }
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public String getImageDir() {
        return AndroidUtilsAstSep.getAppImageFolder(ConstantsAstSep.OrderHandlerConstants.DEFAULT_CASE_IMAGE_DIR_NAME, this.caseId);
    }

    public String[] getExternImageDir() {
        final List<String> publicImageDirs = new ArrayList<String>();
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                if (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) != null) {
                    publicImageDirs.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
                }
                if (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) != null) {
                    publicImageDirs.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":getExternImageDir()", e);
        }
        return publicImageDirs.toArray(new String[]{});
    }

    private int getImageGridWidth() {
        int width = 0;
        DisplayMetrics metrics = new DisplayMetrics();
        try {
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            width = metrics.widthPixels - Double.valueOf(getResources().getDimension(R.dimen.image_grid_frag_horizontal_margin) * 2).intValue();
        } catch (Exception e) {
            writeLog(TAG + " : getImageGridWidth()", e);
        }

        return width;
    }

    public int getSelectedPhotosCount() {
        return selectedPicsLv.getAdapter().getCount();
    }

    public boolean isFileAdded(String fileName) {
        return fileNames.contains(fileName);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    private void setupListeners() {
        if (captureImageBtn != null) {
            captureImageBtn.setOnClickListener(this);
            captureImageBtn.setOnTouchListener(this);
        }

        if (useExistingImageBtn != null) {
            useExistingImageBtn.setOnClickListener(this);
            useExistingImageBtn.setOnTouchListener(this);
        }

        if (gridView != null) {

            gridView.setOnTouchListener(new GridView.OnTouchListener() {
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
        selectedPicsLv.setOnTouchListener(new ListView.OnTouchListener() {
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

    public DatabaseHandler getSqliteHandler() {
        try{
        if (sqliteHandler == null) {
            sqliteHandler = DatabaseHandler.createDatabaseHandler();
        }
        } catch (Exception e) {
            writeLog(TAG + " : getSqliteHandler", e);
        }
        return sqliteHandler;
    }


    @Override
    public void onClick(View v) {
        try{
        if (v.getId() == R.id.captureImageBtn) {

            int totalItemsInLv = selectedPicsLv.getAdapter().getCount();

            //If already 2 items are present in list then restrict user to take the snapshot

            int photoMaxLimit = Integer.parseInt(ApplicationAstSep.getPropertyValue(ConstantsAstSep.PropertyConstants.KEY_PHOTO_MAX_LIMIT, DEFAULT_PHOTO_MAX_LIMIT));

            if (totalItemsInLv < photoMaxLimit) {
                Log.d("onClick-StandardWoAttachImageFragment", "Calling regForImageCaptureInfo");
                ((SESPFlowActivity) getActivity()).regForImageCaptureInfo(this);
                Log.d("onClick-StandardWoAttachImageFragment", "Taking photo");
                ((SESPFlowActivity) getActivity()).takeSnapshot();

                ImageAdapter imageAdaptertempo = new ImageAdapter(this, getImageDir(), getExternImageDir(), fieldVisitId, caseId, caseTypeId);
                imageAdapter = imageAdaptertempo;

                if (imageAdapter.getCount() == 0)
                    gridView.setVisibility(View.GONE);
                else
                    gridView.setVisibility(View.VISIBLE);

                imageAdapter.notifyDataSetChanged();

            } else
                Toast.makeText(getActivity(), getActivity().getString(R.string.photo_limit_reached_in_list), Toast.LENGTH_SHORT).show();

        } else if (v.getId() == R.id.useExistingImageBtn) {
            startActivity(AndroidUtilsAstSep.getWoImageGalleryIntent(getActivity()));
        } else if (v != null
                && v.getId() == R.id.cancelButton
                && popupDialog != null) {
            Log.d("StandardWoAttachImageFragment-onClick", "cancel button is called");
            popupDialog.dismiss();
        } else if (v.getId() == R.id.save_btn
                && popupDialog != null) {
            Log.d(TAG, "save button is clicked");
            popupDialog.dismiss();
            onImageReasonChoosenCompleted(popupDialog, imageFileName);
        }
        } catch (Exception e) {
            writeLog(TAG + " : onClick", e);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            v.performClick();
        }

        return true;
    }

    /**
     * Utility method to build up the existing list of attachments selected
     * for this workorder
     */
    private List<AndroidWOAttachmentBean> getAndroidWoAttachmentList(final long caseId, final long caseTypeId) {

        final List<AndroidWOAttachmentBean> list = getSqliteHandler().getAttachment(caseId, caseTypeId, CONSTANTS().ATTACHMENT_T__PHOTO, null);
        try {
            if (BuildConfig.DEBUG) {
                if (list != null) {
                    Log.d(TAG, "Returning attached wo list size=" + list.size());
                } else {
                    Log.d(TAG, "Returning attached wo list is NULL");
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ": getAndroidWoAttachmentList()", e);
        }
        return list;
    }

    @Override
    public void showPromptUserAction() {
        try{
        final View alertView
                = ((LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.prompt_user_response_layout, null);
        if (alertView != null) {
            final Button okButton = alertView.findViewById(R.id.okButtonYesNoPage);
            final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(getActivity(),
                    alertView, errorString, null);
            final AlertDialog dialog = builder.create();
            dialog.show();
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        } catch (Exception e) {
            writeLog(TAG + ": showPromptUserAction()", e);
        }
    }


    public boolean validateUserInput() {
        try {
            //final Intent intent = getIntraPageIntent();
            String photoMandatoryValue = AndroidUtilsAstSep.getStringSharedPref(ConstantsAstSep.FlowPageConstants.SESP_WO_PHOTO_PREFS,
                    ConstantsAstSep.FlowPageConstants.IS_PHOTO_MANDATORY,
                    Context.MODE_PRIVATE,
                    "NO");

            Log.d(TAG, "IS PHOTO MANDATORY :: " + photoMandatoryValue);
            boolean isPhotoMandatory = "YES".equals(photoMandatoryValue);
            if (isPhotoMandatory) {
                if (beanList.isEmpty()) {
                    errorString = getString(R.string.photo_is_mandatory);
                    return false;
                }
            }

            List<WoInfoCustomTO> riskInfos =
                    WorkorderUtils.getWoInfoCustomTOOfType(AbstractWokOrderActivity.workorderCustomWrapperTO,CONSTANTS().INFO_T__INFO_RISK_OBSERVATION);
            Boolean riskObservationDone = AbstractWokOrderActivity.isPhotoMandatory;
            Boolean riskObservationPhotoPresent = false;
            if (riskInfos.size() >0) {
                riskObservationDone = true;
            } else {
                riskObservationDone = false;
            }
            if (Utils.isNotEmpty(riskObservationDone) && riskObservationDone) {
                if (beanList != null && Utils.isNotEmpty(beanList)) {
                    for (AndroidWOAttachmentBean bean : beanList) {
                        if (bean.getReasonTypeId().contains(String.valueOf(CONSTANTS().ATTACHMENT_REASON_T__RISK_OBSERVATION))) {
                            riskObservationPhotoPresent = true;
                            break;
                        }
                    }
                }
                if (!riskObservationPhotoPresent) {
                    StringBuffer stringBuffer = new StringBuffer(getString(R.string.photo_following_categories_need_a_photo_connection) + "\n");
                    AttachmentReasonTTO attachmentReasonTTO = ObjectCache.getIdObject(AttachmentReasonTTO.class, "RISK_OBSERVATION");
                    if (attachmentReasonTTO != null) {
                        stringBuffer.append("\n" + attachmentReasonTTO.getName());
                    }
                    errorString = stringBuffer.toString();
                    return false;
                }
            }
            final List<AttachmentReasonTTO> attachmentReasonTTOs = ObjectCache.getAllTypes(AttachmentReasonTTO.class);
            final List<PdaCaseTHTAttRTCTO> pdaCaseTHTAttRTCTOs = filterMandatoryReasons(ObjectCache.getAllTypes(PdaCaseTHTAttRTCTO.class));
            List<AttachmentReasonTTO> filteredAttachmentReasonTTos = WorkorderUtils.getAttachmentReasonType(idCaseTHandlerT, pdaCaseTHTAttRTCTOs, attachmentReasonTTOs);

            List<AttachmentReasonTTO> selectedAttachments = new ArrayList<>();
            if (beanList != null && Utils.isNotEmpty(beanList)) {
                for (AndroidWOAttachmentBean bean : beanList) {
                    String reasonTypeIds = bean.getReasonTypeId();
                    if (reasonTypeIds != null) {
                        List<String> reasonIds = Arrays.asList(reasonTypeIds.split(","));
                        if (filteredAttachmentReasonTTos != null && Utils.isNotEmpty(filteredAttachmentReasonTTos)) {
                            for (AttachmentReasonTTO filteredAttachmentReasonTTO : filteredAttachmentReasonTTos) {
                                if (reasonIds.contains(String.valueOf(filteredAttachmentReasonTTO.getId()))) {
                                    selectedAttachments.add(filteredAttachmentReasonTTO);
                                }
                            }
                        }
                    }
                }
            }
            filteredAttachmentReasonTTos.removeAll(selectedAttachments);

            if (filteredAttachmentReasonTTos.size() > 0) {
                StringBuffer stringBuffer = new StringBuffer(getString(R.string.photo_following_categories_need_a_photo_connection) + "\n");
                for (AttachmentReasonTTO attachmentReasonTTO : filteredAttachmentReasonTTos) {
                    stringBuffer.append("\n" + attachmentReasonTTO.getName());
                }
                errorString = stringBuffer.toString();
                return false;
            }
            applyChangesToModifiableWO();
        } catch (Exception e) {
            writeLog(TAG + " : validateUserInput()", e);
        }
        return true;
    }

    private List<PdaCaseTHTAttRTCTO> filterMandatoryReasons(List<PdaCaseTHTAttRTCTO> pdaCaseTHTAttRTCTOs) {
        List<PdaCaseTHTAttRTCTO> filteredReasons = new ArrayList<PdaCaseTHTAttRTCTO>();
        try {
            for (PdaCaseTHTAttRTCTO pdaCaseTHTAttRTCTO : pdaCaseTHTAttRTCTOs) {
                if (pdaCaseTHTAttRTCTO.getMandatory() == 1L) {
                    filteredReasons.add(pdaCaseTHTAttRTCTO);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ": filterMandatoryReasons()", e);
        }
        return filteredReasons;
    }

    /**
     * ============== private adapter to populate list of selected images ==================
     */
    private class SelectedPhotoAdapter extends BaseAdapter implements View.OnClickListener {

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

        public SelectedPhotoAdapter(final Context context, final List<AndroidWOAttachmentBean> androidWOAttachmentBean) {
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
        public AndroidWOAttachmentBean getItem(int position) {
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
            return 0;
        }


        //To be called by the parent fragment to update the list based on user action
        public void setAndroidWOAttachmentBean(List<AndroidWOAttachmentBean> androidWOAttachmentBean) {
            this.androidWOAttachmentBean = androidWOAttachmentBean;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d("SelectedPhotoAdapter-getView", "SelectedPhotoAdapter-getView being called with position=" + position);
            View vw = convertView;
            ViewHolder holder;


            if (vw == null) {
                final LayoutInflater inflater = (LayoutInflater) contextRef.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vw = inflater.inflate(R.layout.wo_image_list_row_layout, null);

                holder = new ViewHolder();

                // Lookup the view elements
                holder.woImageIcon = vw.findViewById(R.id.woImageIcon);
                holder.imageReasonTv = vw.findViewById(R.id.imageReasonTv);
                holder.infoAttachmentBtn = vw.findViewById(R.id.infoAttachmentBtn);
                holder.removeAttachmentBtn=vw.findViewById(R.id.removeAttachmentBtn);

                vw.setTag(holder);

            } else {
                holder = (ViewHolder) vw.getTag();
            }

            if (contextRef != null && contextRef.get() != null) {
                try {
                    final AndroidWOAttachmentBean item = getItem(position);
                    if (item != null) {

                        // Populate the attachment reason
                        if (item.getAttachmentReasonTTO() != null && item.getAttachmentReasonTTO().size() > 0) {
                            //TODO: implementation needs to change
                            StringBuffer strBuffer = new StringBuffer("");
                            for (AttachmentReasonTTO attachmentReasonTTO : item.getAttachmentReasonTTO()) {
                                if (!strBuffer.toString().equals("")) {
                                    strBuffer.append(", ");
                                }
                                strBuffer.append(attachmentReasonTTO.getName());
                            }
                            holder.imageReasonTv.setText(strBuffer.toString());
                        }

                        if (holder.woImageIcon != null) {
                            // set the image icon
                            final BitmapWorkerTask worker = new BitmapWorkerTask(holder.woImageIcon, 100, 80);
                            final AsyncDrawable aDrawable =
                                    new AsyncDrawable(getActivity().getResources(),
                                            BitmapFactory.decodeResource(getActivity().getResources(),
                                                    R.drawable.empty_photo_row), worker);
                            holder.woImageIcon.setImageDrawable(aDrawable);
                            worker.execute(item.getFileName());


                        }
                        // setup listener to view the reasons for this attachment and tag the object
                        holder.infoAttachmentBtn.setOnClickListener(this);
                        holder.infoAttachmentBtn.setTag(item);

                        // setup listener to remove/discard this attachment
                        holder.removeAttachmentBtn.setOnClickListener(this);
                        // Tag the object
                        holder.removeAttachmentBtn.setTag(item);
                    }
                } catch (final Exception ex) {
                    writeLog(TAG + "-SelectedPhotoAdapter-fragmentView", ex);
                }
            }
            return vw;
        }

        @Override
        public void onClick(View v) {
            try{
            if (v != null && v.getId() == R.id.infoAttachmentBtn) {
                if (v.getTag() != null && v.getTag() instanceof AndroidWOAttachmentBean) {
                    final AndroidWOAttachmentBean item = (AndroidWOAttachmentBean) v.getTag();
                    showReasonChooserPopup(item.getFileName(), item);
                }

            } else if (v != null && v.getId() == R.id.removeAttachmentBtn) {
                if (v.getTag() != null && v.getTag() instanceof AndroidWOAttachmentBean) {
                    final AndroidWOAttachmentBean item = (AndroidWOAttachmentBean) v.getTag();
                    if (this.androidWOAttachmentBean != null && this.contextRef != null && this.contextRef.get() != null) {
                        this.androidWOAttachmentBean.remove(v.getTag());
                        beanList.remove(v.getTag());
                        fileNames.remove(((AndroidWOAttachmentBean) v.getTag()).getFileName());
                        //Delete from db also
                        getSqliteHandler().removeAttchment(item.getCaseId(), item.getCaseTypeId(), item.getFileName(), CONSTANTS().ATTACHMENT_T__PHOTO, item.getReasonTypeId());
                        this.notifyDataSetChanged();
                    }
                }
            } else if (v != null && v.getId() == R.id.cancelButton && popupDialog != null) {
                Log.d("StandardWoAttachImageFragment-onClick", "cancel button is called");
                popupDialog.dismiss();
            } else if (v.getId() == R.id.save_btn
                    && popupDialog != null) {
                Log.d(TAG, "save button is clicked");
                popupDialog.dismiss();
                onImageReasonChoosenCompleted(popupDialog, imageFileName);
            }
            } catch (Exception e) {
                writeLog(TAG + " : onClick()", e);
            }
        }

    }


    @Override
    public void onImageTaken(final Intent imageInfo) {
        Log.d("StandardWoAttachImageFragment-onImageTaken", "StandardWoAttachImageFragment-onImageTaken called");
        String imageFileName = null;
        try {
            if (imageInfo != null) {
                imageFileName = imageInfo.getStringExtra(ConstantsAstSep.OrderHandlerConstants.LAST_WO_IMAGE);
            }
            showReasonChooserPopup(imageFileName, null);
        } catch (Exception e) {
            writeLog(TAG + ": onImageTaken()", e);
        }

    }

    private void showReasonChooserPopup(final String imageFileName, final AndroidWOAttachmentBean bean) {
        Log.d("StandardWoAttachImageFragment-showReasonChooserPopup", "StandardWoAttachImageFragment-showReasonChooserPopup called");
        final View popupView = getActivity().getLayoutInflater().inflate(R.layout.reason_photo_popup_layout, null);
        Log.d("StandardWoAttachImageFragment", "Inflating reason_photo_popup_layout");
        try {
            if (popupView != null
                    && popupView.findViewById(R.id.cancelButton) != null
                    && popupView.findViewById(R.id.reasonListView) != null) {

                popupView.findViewById(R.id.cancelButton).setOnClickListener(this);
                popupView.findViewById(R.id.save_btn).setOnClickListener(this);
                final List<AttachmentReasonTTO> attachmentReasonTTOs = ObjectCache.getAllTypes(AttachmentReasonTTO.class);
                final List<PdaCaseTHTAttRTCTO> pdaCaseTHTAttRTCTOs = ObjectCache.getAllTypes(PdaCaseTHTAttRTCTO.class);
                final List<AttachmentReasonTTO> filteredAttachmentReasonTTos = WorkorderUtils.getAttachmentReasonType(idCaseTHandlerT, pdaCaseTHTAttRTCTOs, attachmentReasonTTOs);
                this.imageFileName = imageFileName;


                final ListView reasonListView = (ListView) popupView.findViewById(R.id.reasonListView);
                // =================================================================================
                Log.d("StandardWoAttachImageFragment", "Setting ImageReasonListAdapter in StandardWoAttachImageFragment");


                final AlertDialog.Builder builder
                        = GuiController.getCustomAlertDialog(getActivity(), popupView, null, null);
                popupDialog = builder.create();
                reasonListView.setAdapter(
                        new ImageReasonListAdapter(this, filteredAttachmentReasonTTos, popupView,
                                bean != null ? AndroidUtilsAstSep.convertDelimitedStringToLongList(bean.getReasonTypeId(), ",") : null));

                popupDialog.setCancelable(false);
                popupDialog.show();
            }
        } catch (Exception e) {
            writeLog(TAG + ": showReasonChooserPopup()", e);
        }
    }

    @Override
    public void onImageReasonChoosenCompleted(Dialog popupDialog, String imageFileName) {
        try {
            Log.d(TAG, "onImageReasonChoosenCompleted of StandardWoAttachImageFragment getting called");
            Log.d(TAG, "Updating selectedPhotosAdapter");
            //Save the Attachment along with the reasons in SQLiteDatabase : Start
            final ListView reasonListView = (ListView) popupDialog.findViewById(R.id.reasonListView);
            List<Long> selectedIdAttachmentReason = ((ImageReasonListAdapter) reasonListView.getAdapter()).getSelectedIdAttachmentReason();
            getSqliteHandler().addAttachment(caseId, caseTypeId, fieldVisitId, imageFileName, CONSTANTS().ATTACHMENT_T__PHOTO, selectedIdAttachmentReason);
            //Save the Attachment along with the reasons in SQLiteDatabase : End
            // Update the adapter for selected photos

            if (selectedPhotosAdapter != null) {
                Log.d("StandardWoAttachImageFragment-onImageReasonChoosenCompleted", "Applying new selected attachment list");
                beanList = getAndroidWoAttachmentList(caseId, caseTypeId);
                if (beanList != null) {
                    fileNames.clear();
                    for (AndroidWOAttachmentBean bean : beanList) {
                        fileNames.add(bean.getFileName());
                    }
                    ((SelectedPhotoAdapter) selectedPhotosAdapter).setAndroidWOAttachmentBean(beanList);
                    Log.d("StandardWoAttachImageFragment-onImageReasonChoosenCompleted", "Updating data set for selected image list");
                    if (selectedPhotosAdapter.getCount() == 0)
                        selectedPicsLv.setVisibility(View.GONE);
                    else
                        selectedPicsLv.setVisibility(View.VISIBLE);
                    selectedPhotosAdapter.notifyDataSetChanged();
                } else {
                    Log.d("StandardWoAttachImageFragment-onImageReasonChoosenCompleted",
                            "StandardWoAttachImageFragment-onImageReasonChoosenCompleted - Returning null list, adapter not set thus");
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ": onImageReasonChoosenCompleted()", e);
        }
    }

    //To recycle the list view
    private class ViewHolder {

        ImageView woImageIcon;
        TextView imageReasonTv;
        ImageButton infoAttachmentBtn;
        ImageButton removeAttachmentBtn;


    }
}