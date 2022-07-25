/**
 *
 */
package com.capgemini.sesp.ast.android.ui.fragments;

//import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.NumberPicker;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.others.DiskLRUCacheImage;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.ui.adapter.ImageAdapter;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.StandardWoAttachImageFragment;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Reusable fragment for photo capture during work order processing
 *
 * @author Capgemini
 * @version 1.0
 * @since 30th December, 2014
 */
//@SuppressLint("InflateParams")
public class StandardWorkorderChooseExistImageFragment extends StandardWoAttachImageFragment implements OnClickListener {

	/*
     *  The image directory is where
	 *  the images are stored and taken by SESP android app
	 */

    private transient String imageDir = null;

    private transient Dialog dialog = null;

    private transient long caseId;
    private transient long caseTypeId;
    private transient String fieldVisitId;

    /*
     * The generic image directory where images taken outside
     * SESP are stored. Generally it would be standard image directory
     */
    private transient String[] externImageDir = null;

    /*
     * The absolute file location for the
     * last taken photo by SESP app
     */
    private transient String lastPhoto = null;
    private String TAG = StandardWorkorderChooseExistImageFragment.class.getSimpleName();

    /**
     * Return SESP image directory
     *
     * @return {@link String}
     */
    public String getImageDir() {
        return imageDir;
    }

    /**
     * Return absolute file location
     * for last taken photo by SESP app
     *
     * @return {@link String}
     */
    public String getLastPhoto() {
        return lastPhoto;
    }

    public StandardWorkorderChooseExistImageFragment() {
        super();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            final Intent launcherIntent = getActivity().getIntent();
            if (launcherIntent != null) {
                lastPhoto = launcherIntent.getExtras().getString(ConstantsAstSep.OrderHandlerConstants.LAST_WO_IMAGE);
                imageDir = launcherIntent.getExtras().getString(ConstantsAstSep.OrderHandlerConstants.DEFAULT_IMAGE_DIR);
                externImageDir = launcherIntent.getExtras().getStringArray(ConstantsAstSep.OrderHandlerConstants.PUBLIC_IMAGE_DIRS);
                this.caseId = launcherIntent.getExtras().getLong(ConstantsAstSep.OrderHandlerConstants.WORKORDER_CASE_ID);
                this.caseTypeId = launcherIntent.getExtras().getLong(ConstantsAstSep.OrderHandlerConstants.WORKORDER_CASE_TYPE_ID);
                this.fieldVisitId = launcherIntent.getExtras().getString(ConstantsAstSep.OrderHandlerConstants.FIELD_VISIT_ID);
                if (caseId == 0) {
                    final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
                    caseId = wo.getIdCase();
                    caseTypeId = wo.getIdCaseType();
                    fieldVisitId = wo.getFieldVisitID();
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ": onCreate() ", e);
        }
    }

    private int getImageGridWidth() {
        int width = 0;
        try {
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            width = metrics.widthPixels - Double.valueOf(getResources().getDimension(R.dimen.image_grid_frag_horizontal_margin) * 2).intValue();
        } catch (Exception e) {
            writeLog(TAG + ": getImageGridWidth() ", e);
        }
        return width;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.wo_image_layout, container,
                false);
        final GridView gridView = rootView.findViewById(R.id.gridVw);
        try {
            if (gridView != null) {
                //gridView.setColumnWidth(getActivity().findViewById(R.id.container).getWidth());
                gridView.setColumnWidth(getImageGridWidth());
                final long caseId = getCaseId();
                final long caseTypeId = getCaseTypeId();
                gridView.setAdapter(new ImageAdapter(this, getImageDir(), getExternImageDir(), fieldVisitId, caseId, caseTypeId));
            }
        } catch (Exception e) {
            writeLog(TAG + " : onCreateView() ", e);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (menuInflater != null) {
            menuInflater.inflate(R.menu.image, menu);
        }
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.clear_cache) {
            //return true;
            if (clearDiskImageCache()) {
                final View alertView = getActivity().getLayoutInflater().inflate(R.layout.layout_cache_clear_info, null);

                if (alertView != null) {
                    final Button clearOkButton = alertView.findViewById(R.id.okButtonImageCacheClear);
                    clearOkButton.setOnClickListener(this);

                    final AlertDialog.Builder builder =
                            GuiController.getCustomAlertDialog(getActivity(), alertView, null, null);
                    dialog = builder.create();
                    dialog.show();
                }
            }
        } else if (id == R.id.modCacheSize) {
            // Show the modify cache view
            final View adjustImageCacheView = getActivity().getLayoutInflater().inflate(R.layout.layout_adjust_image_cache_size, null);
            if (adjustImageCacheView != null) {
                final Button okAdjustCache = adjustImageCacheView.findViewById(R.id.okButtonAdjustCache);
                if (okAdjustCache != null) {
                    okAdjustCache.setOnClickListener(this);
                }

                final Button cancelButton = adjustImageCacheView.findViewById(R.id.cancelButtonAdjustCache);
                if (cancelButton != null) {
                    cancelButton.setOnClickListener(this);
                }

                populateMaxCacheSize(adjustImageCacheView.findViewById(R.id.numPicker));

                final AlertDialog.Builder builder =
                        GuiController.getCustomAlertDialog(getActivity(), adjustImageCacheView, null, null);
                dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public long getCaseId() {
        return caseId;
    }

    public long getCaseTypeId() {
        return caseTypeId;
    }

    public String[] getExternImageDir() {
        return externImageDir;
    }

    private boolean clearDiskImageCache() {
        boolean status = false;
        try {
            final DiskLRUCacheImage cache = DiskLRUCacheImage.getDiskCache(getActivity());
            if (cache != null) {
                status = cache.clearDiskCache();
            }
        } catch (final Exception e1) {
            writeLog(TAG + " : clearDiskImageCache()", e1);
        }
        return status;
    }

    private void adjustDiskCacheMaxSize(final int sizeInMb) {
        try {
            final long freeMB = AndroidUtilsAstSep.getFreeDiskSpaceInMB();

            if (sizeInMb > 0 && sizeInMb < (freeMB * ConstantsAstSep.OrderHandlerConstants.DISKCACHEUPPERLIMITFACTOR)) {
                DiskLRUCacheImage.getDiskCache().adjustCacheMaxSize(sizeInMb);
            }
        } catch (Exception e) {
            writeLog(TAG + " : adjustDiskCacheMaxSize() ", e);
        }
    }

    private void populateMaxCacheSize(final NumberPicker picker) {
        try {
            if (picker != null) {

                final long freeSpace = AndroidUtilsAstSep.getFreeDiskSpaceInMB();
                if (freeSpace > 1) {
                    Double val =
                            Math.floor(Double.valueOf(freeSpace) * Double.valueOf(ConstantsAstSep.OrderHandlerConstants.DISKCACHEUPPERLIMITFACTOR));
                    picker.setMaxValue(val.intValue());
                }
                picker.setMinValue(1);
            }
        } catch (Exception e) {
            writeLog(TAG + " : populateMaxCacheSize() ", e);
        }
    }

    @Override
    public void onClick(View v) {
        if (v != null
                && (v.getId() == R.id.okButtonImageCacheClear || v.getId() == R.id.cancelButtonAdjustCache)
                && dialog != null) {
            dialog.dismiss();
        } else if (v != null
                && v.getId() == R.id.okButtonAdjustCache
                && dialog != null) {
            final NumberPicker picker = dialog.findViewById(R.id.numPicker);
            if (picker != null) {
                adjustDiskCacheMaxSize(picker.getValue());
            }
            dialog.dismiss();
        } else if (v != null && v.getId() == R.id.woimagereasonindivrow) {
        }
    }
}
