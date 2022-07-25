/**
 * @copyright Capgemini
 */
package com.capgemini.sesp.ast.android.ui.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.capgemini.sesp.ast.android.BuildConfig;
import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.flow.WoImageCompletionCallback;
import com.capgemini.sesp.ast.android.module.others.AsyncDrawable;
import com.capgemini.sesp.ast.android.module.others.BitmapWorkerTask;
import com.capgemini.sesp.ast.android.module.others.DiskLRUCacheImage;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.module.util.to.AndroidWOAttachmentBean;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.StandardWoAttachImageFragment;
import com.skvader.rsp.ast_sep.common.to.ast.table.PdaCaseTHTAttRTCTO;
import com.skvader.rsp.cft.common.to.cft.table.AttachmentReasonTTO;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.WeakHashMap;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * The adapter that loads and displays the images
 * and place them to image fragment. This class has all the
 * implementation to show images in various ways in the grid view
 *
 * @author Capgemini
 * @version 1.0
 * @since 20th Nov, 2014
 */
@SuppressLint("InflateParams")
public class ImageAdapter extends BaseAdapter
        implements OnClickListener, OnLongClickListener/*, OnCheckedChangeListener*/ {

    //private transient boolean bigIvShown = false;
    private transient boolean checkEnabled = false;

    /*
     *  The set would contain all the image file names selected
     *  by the user for a specific work order
     */
    private transient Set<File> multiSelectedFiles = null;

    //private final Context mContext;
    //private int mItemHeight = 0;
    private transient int mNumColumns = 0;
    static String TAG = ImageAdapter.class.getSimpleName();
    private transient long caseId;
    private transient long caseTypeId;
    private transient String fieldVisitId;

    // Pointing the actual image file used from the image view reference
    private transient WeakHashMap<ImageView, File> imageFileRefMap = null;
    private transient WeakHashMap<CheckBox, File> checkFileRefMap = null;

    //private transient WeakHashMap<CheckBox, ImageView> checkBoxMap = null;
    private transient SoftReference<Fragment> fragmentRef = null;

    private transient String photoLocation = null;

    private transient File[] files = null;


    private transient List<File> filesWithwrongNameC = new ArrayList<>();


    private transient DiskLRUCacheImage cache = null;

    private transient Dialog popupDialog = null;

    private transient final String IMAGEADAPTERTAG = "ImageAdapter";

    private int checkBoxCounter = 0;

    private transient DatabaseHandler sqliteHandler = null;

    boolean[] checkBoxState;
    private static final String DEFAULT_PHOTO_MAX_LIMIT = "2";
    public int maxCharacterAllowed;

    public List<File> getFilesWithwrongNameC() {
        return filesWithwrongNameC;
    }

    public void setFilesWithwrongNameC(List<File> filesWithwrongNameC) {
        this.filesWithwrongNameC = filesWithwrongNameC;
    }

    private class ImageFileNameFilter implements FilenameFilter {

        @Override
        public boolean accept(final File dir, final String name) {
            boolean acceptance = false;
            if (name.toLowerCase(Locale.getDefault()).endsWith(ConstantsAstSep.OrderHandlerConstants.IMAGE_EXT_JPG)
                    || name.toLowerCase(Locale.getDefault()).endsWith(ConstantsAstSep.OrderHandlerConstants.IMAGE_EXT_PNG)
                    || name.toLowerCase(Locale.getDefault()).endsWith(ConstantsAstSep.OrderHandlerConstants.IMAGE_EXT_JPEG)
                    || name.toLowerCase(Locale.getDefault()).endsWith(ConstantsAstSep.OrderHandlerConstants.IMAGE_EXT_GIF)) {
                acceptance = true;
            }
            return acceptance;
        }
    }

    public ImageAdapter(final Fragment fragment,
                        final String imageDir,
                        final String[] publicImageDirs,
                        final String fieldVisitId,
                        final long caseId, final long caseTypeId) {
        super();
        this.photoLocation = imageDir;
        this.caseId = caseId;
        this.caseTypeId = caseTypeId;
        this.fieldVisitId = fieldVisitId;

        List<AndroidWOAttachmentBean> attachedFiles = DatabaseHandler.createDatabaseHandler().getAttachment(caseId, caseTypeId, CONSTANTS().ATTACHMENT_T__PHOTO, null);
        if (BuildConfig.DEBUG && attachedFiles != null) {
            Log.d("onCreate-ImageAdapter", "list of pre attached files for caseid=" + caseId + ",casetypeid=" + caseTypeId);
            Log.d("onCreate-ImageAdapter", "Attached file count=" + attachedFiles.size());
        }


        fragmentRef = new SoftReference<Fragment>(fragment);
        try {
            cache = DiskLRUCacheImage.getDiskCache(fragmentRef.get().getActivity());
        } catch (final Exception e1) {
            writeLog("ImageAdapter: ImageAdapter() : ", e1);
        }
        final FilenameFilter imageFilter = new ImageFileNameFilter();
        final Comparator<File> imageFileComparator = new ImageFileComparator();
        if (photoLocation != null && !"".equals(photoLocation.trim())) {
            final File loc = new File(photoLocation);
            if (loc.exists()) {
                this.files = getImageFilesInAppFolder(loc,imageFilter);
            }
        }

        final List<File> otherImageList = new ArrayList<File>();

        if (publicImageDirs != null && publicImageDirs.length > 0) {
            for (final String externalImageDir : publicImageDirs) {
                final File[] otherImages = new File(externalImageDir).listFiles(imageFilter);
                if (otherImages != null && otherImages.length > 0) {
                    otherImageList.addAll(Arrays.asList(otherImages));
                }
            }
        }

        // Let use see the last taken photo first
        if (this.files != null && this.files.length > 0) {
            Arrays.sort(this.files, imageFileComparator);
            checkBoxState = new boolean[files.length];
        }


        // Should allow image views to be garbage collected
        imageFileRefMap = new WeakHashMap<ImageView, File>();
        checkFileRefMap = new WeakHashMap<CheckBox, File>();
        //checkBoxMap = new WeakHashMap<CheckBox,ImageView>();

    }

    private File[] getImageFilesInAppFolder(File loc, FilenameFilter imageFilter) {
        List<File> tmpFiles = new ArrayList<File>(Arrays.asList(loc.listFiles(imageFilter)));
        maxCharacterAllowed = 64 - cache.convertToRegex(loc.getAbsolutePath()).length();

        for (File imageFile:tmpFiles)
        {
            try {
                cache.getBitmap(imageFile.getAbsolutePath());
            }catch (IllegalArgumentException e)
            {
                Log.d("LOG", "Ignore the file");
                filesWithwrongNameC.add(imageFile);
                //tmpFiles.remove(imageFile);
            }
        }
        tmpFiles.removeAll(filesWithwrongNameC);
        File[] tmpfiles = new File[(tmpFiles.toArray().length)];
        int i = 0;
        for (File f: tmpFiles)
        {
            tmpfiles[i++] = f;
        }
        return tmpfiles;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (files != null) {
            count = files.length;
        }
        return count;
    }

    @Override
    public Object getItem(final int position) {
        /*return position < mNumColumns ?
                null : Images.imageThumbUrls[position - mNumColumns];*/
        return null;
    }

    @Override
    public long getItemId(final int position) {
        return position < mNumColumns ? 0 : position - mNumColumns;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup container) {

        View vw = convertView;
        ViewHolder holder;
        try {
            if (fragmentRef != null && fragmentRef.get() != null) {
                final Context mContext = fragmentRef.get().getActivity();

                if (vw == null) {
                    vw = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.sesp_wo_image_grid_other_layout, null);
                    holder = new ViewHolder();

                    holder.iv1 = vw.findViewById(R.id.secOtherIv1);
                    vw.setTag(holder);

                } else {
                    holder = (ViewHolder) vw.getTag();
                }

                if (holder.iv1 != null) {
                    // On click listener to show the image in big screen
                    holder.iv1.setOnClickListener(this);
                    holder.iv1.setOnLongClickListener(this);
                }

                if (this.fragmentRef != null && this.fragmentRef.get() != null) {
                    final Context ctx = this.fragmentRef.get().getActivity();

                    // Using api level minimum 15 hence found deprecated otherwise we can user iv1.setBackground(...
                    holder.iv1.setBackground(ctx.getResources().getDrawable(R.drawable.photo_selector));
                }

                if (files != null) {

                    final File imageFile = files[position];
                    final Bitmap bmp = cache.getBitmap(imageFile.getAbsolutePath());

                    if (imageFileRefMap != null && holder.iv1 != null) {
                        imageFileRefMap.put(holder.iv1, imageFile);
                    }

                    holder.iv1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Get the selected/deselected file name
                            if (imageFileRefMap != null && imageFileRefMap.get(view) != null) {
                                int photoMaxLimit = Integer.parseInt(ApplicationAstSep.getPropertyValue(ConstantsAstSep.PropertyConstants.KEY_PHOTO_MAX_LIMIT, DEFAULT_PHOTO_MAX_LIMIT));
                                if (((StandardWoAttachImageFragment) fragmentRef.get()).getSelectedPhotosCount() < photoMaxLimit) {
                                    File imageFile = null;
                                    imageFile = imageFileRefMap.get(view);
                                    if (imageFile != null && checkBoxState[position] == false) {
                                        if (!((StandardWoAttachImageFragment) fragmentRef.get()).isFileAdded(imageFile.getAbsolutePath())) {
                                            showReasonChooserPopup(imageFile.getAbsolutePath());
                                        }
                                    }
                                } else {
                                    Toast.makeText(fragmentRef.get().getActivity(), fragmentRef.get().getActivity().getString(R.string.limit_reached), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });


                    if (bmp != null) {
                        Log.d(IMAGEADAPTERTAG, "Cache hit");
                        holder.iv1.setImageBitmap(bmp);
                    } else {
                        holder.iv1.setImageResource(R.drawable.empty_photo);
                        final BitmapWorkerTask worker = new BitmapWorkerTask(holder.iv1, 70, 60);
                        final AsyncDrawable aDrawable =
                                new AsyncDrawable(mContext.getResources(), BitmapFactory.decodeResource(mContext.getResources(),
                                        R.drawable.empty_photo), worker);
                        holder.iv1.setImageDrawable(aDrawable);

                        worker.execute(imageFile.getAbsolutePath());
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " : getView() ", e);
        }
        return vw;
    }

    static class ViewHolder {

        ImageView iv1;
    }

    public void setNumColumns(final int numColumns) {
        mNumColumns = numColumns;
    }

    public int getNumColumns() {
        return mNumColumns;
    }

    @Override
    public void onClick(final View v) {
        Log.d(IMAGEADAPTERTAG, "Image Adapter on click");
    }


    /**
     * Custom comparator to list the last taken photos
     * first only
     */
    private class ImageFileComparator implements Comparator<File> {

        @Override
        public int compare(final File lhs, final File rhs) {
            int result = 0;
            try {
                if (lhs != null && rhs != null) {
                    if (lhs.lastModified() > rhs.lastModified()) {
                        result = -1;
                    } else if (lhs.lastModified() < rhs.lastModified()) {
                        result = 1;
                    }
                }
            } catch (Exception e) {
                writeLog(TAG + " : compare() ", e);
            }
            return result;
        }

    }

    /**
     * User has long clicked on an image view
     * <p/>
     * <p>
     * May be used to show a contextual menu
     * (not action menu) for more options
     * </p>
     */

    @Override
    public boolean onLongClick(final View v) {
        //enableDisableChecks(!checkEnabled);
        checkEnabled = !checkEnabled;
        return true;
    }

    public DatabaseHandler getSqliteHandler() {
        if (sqliteHandler == null) {
            sqliteHandler = DatabaseHandler.createDatabaseHandler();
        }
        return sqliteHandler;
    }

    private void showReasonChooserPopup(final String imageFileName) {
        try {
            if (fragmentRef != null && fragmentRef.get() != null) {

                final View popupView = ((LayoutInflater) fragmentRef.get().getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.reason_photo_popup_layout, null);
                Log.d("StandardWoAttachImageFragment", "Inflating reason_photo_popup_layout");
                if (popupView != null
                        && popupView.findViewById(R.id.cancelButton) != null
                        && popupView.findViewById(R.id.save_btn) != null
                        && popupView.findViewById(R.id.reasonListView) != null) {

                    final List<AttachmentReasonTTO> attachmentReasonTTOs = ObjectCache.getAllTypes(AttachmentReasonTTO.class);
                    final List<PdaCaseTHTAttRTCTO> pdaCaseTHTAttRTCTOs = ObjectCache.getAllTypes(PdaCaseTHTAttRTCTO.class);
                    Long idCaseTHandlerT = WorkorderUtils.findIdCaseTypeHandlerType(caseTypeId);
                    final List<AttachmentReasonTTO> filteredAttachmentReasonTTos = WorkorderUtils.getAttachmentReasonType(idCaseTHandlerT, pdaCaseTHTAttRTCTOs, attachmentReasonTTOs);

                    final ListView reasonListView = popupView.findViewById(R.id.reasonListView);
                    // =================================================================================
                    Log.d("StandardWoAttachImageFragment", "Setting ImageReasonListAdapter in StandardWoAttachImageFragment");

                    final Button cancelButton = popupView.findViewById(R.id.cancelButton);
                    final Button saveButton = popupView.findViewById(R.id.save_btn);
                    // cancelButton.setBackgroundColor(fragmentRef.get().getActivity().getResources().getColor(R.color.colorDisable));
                    // cancelButton.setEnabled(false);
                    cancelButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupDialog.dismiss();
                        }
                    });
                    saveButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupDialog.dismiss();
                            // Execute the callback
                            if (fragmentRef != null && fragmentRef.get() != null && fragmentRef.get() instanceof WoImageCompletionCallback) {
                                Log.d(IMAGEADAPTERTAG, "Executing callback after db persistence is completed");
                                ((WoImageCompletionCallback) fragmentRef.get()).onImageReasonChoosenCompleted(popupDialog, imageFileName);
                            }
                        }
                    });
                    final AlertDialog.Builder builder
                            = GuiController.getCustomAlertDialog(fragmentRef.get().getActivity(), popupView, null, null);
                    popupDialog = builder.create();
                    popupDialog.setCancelable(false);

                    reasonListView.setAdapter(
                            new ImageReasonListAdapter(fragmentRef.get(),
                                    filteredAttachmentReasonTTos, popupView, null));
                    popupDialog.show();
                }
            }

        } catch (Exception e) {
            writeLog(TAG + " : showReasonChooserPopup() ", e);
        }
    }
}
