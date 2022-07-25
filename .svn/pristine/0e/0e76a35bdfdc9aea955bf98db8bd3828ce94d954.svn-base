/**
 *
 */
package com.capgemini.sesp.ast.android.module.others;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;

import java.io.File;
import java.lang.ref.WeakReference;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

    /*
     * Storing image view reference for which this
     * task has started and allowing it to be GCed easily
     */
    private transient WeakReference<ImageView> imageViewRef = null;

    private transient int targetHeight;
    private transient int targetWidth;
    static String TAG = BitmapWorkerTask.class.getSimpleName();

    private transient String key = null;

    public BitmapWorkerTask(final ImageView imageView,
                            final int targetHeight, final int targetWidth) {
        // Store the image view in weak reference, allow it to be GCed when android decides
        this.imageViewRef = new WeakReference<ImageView>(imageView);
        this.targetHeight = targetHeight;
        this.targetWidth = targetWidth;
    }

    /**
     * Start decoding the image and
     * once completed store the thumbnail to disk cache
     */
    @Override
    protected Bitmap doInBackground(final String... params) {
        Bitmap bitmap = null;
        // Decode image
        try {
            if (params != null && params.length > 0) {

                //for(int i=0;i<params.length;i++){
                final File imageFile = new File(params[0]);
                //key = imageFile.getAbsolutePath();
                key = imageFile.getAbsolutePath();
                if (imageFile.exists() && imageFile.isFile()) {
                    bitmap = AndroidUtilsAstSep.decodeBitmap(imageFile.getAbsolutePath(), targetHeight, targetWidth);
                }
                //}
            }
        } catch (Exception e) {
            writeLog(TAG + " :doInBackground() ", e);
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        try {
            final DiskLRUCacheImage cacheImage = DiskLRUCacheImage.getDiskCache();
            if (cacheImage != null && key != null && bitmap != null) {
                cacheImage.put(key, bitmap);
            }

            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewRef != null && imageViewRef.get() != null && bitmap != null) {
                final ImageView imageView = imageViewRef.get();
                final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                    imageView.setScaleType(ScaleType.FIT_XY);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :onPostExecute() ", e);
        }
    }

    /**
     * Private factory method for instantiating an instance of {@link BitmapWorkerTask}
     *
     * @param imageView {@link ImageView}
     * @return BitmapWorkerTask
     */

    private static BitmapWorkerTask getBitmapWorkerTask(final ImageView imageView) {
        BitmapWorkerTask ref = null;
        try{
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                ref = asyncDrawable.getBitmapWorkerTask();
            }
        }
        } catch (Exception e) {
            writeLog(TAG + " :getBitmapWorkerTask() ", e);
        }
        return ref;
    }
}