/**
 * @copyright Capgemini
 */
package com.capgemini.sesp.ast.android.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.SoftReference;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Image adapter for displaying workorder images
 *
 * @author Capgemini
 * @since 12th March, 2015
 * @version 1.0
 *
 */
public class WoImageViewAdapter extends BaseAdapter implements ViewTreeObserver.OnPreDrawListener {

    private transient SoftReference<Context> ctxRef = null;
    private transient String imageDir = null;
    private transient File[] imageFiles = null;

    /**
     * Target height of the decoded standard image height
     */
    private transient final int TARGET_HEIGHT_PX = 85;

    /**
     * Target height of the decoded standard image width
     */
    private transient final int TARGET_WIDTH_PX = 85;

    private transient final int IMAGE_DOWNGRADE_FACTOR = 2;

    private transient ImageView indivImageView = null;

    public WoImageViewAdapter(final Context context, final String imageDir) {
        super();
        if (context != null) {
            this.ctxRef = new SoftReference<Context>(context);
        }
        if (!"".equals(imageDir)) {
            final File directory = new File(imageDir);
            if (directory.exists() && directory.isDirectory()) {
                this.imageDir = directory.getAbsolutePath();
            }
        }
    }

    @Override
    public int getCount() {
        int count = 0;
        try {
            if (this.imageDir != null) {
                final File f = new File(imageDir);
                if (f.exists() && f.isDirectory()) {
                    this.imageFiles = f.listFiles();
                    if (this.imageFiles != null) {
                        count = this.imageFiles.length;
                    }
                }
            }
        } catch (Exception e) {
            writeLog("WoImageViewAdapter  : getCount() ", e);
        }
        return count;
    }

    @Override
    public Object getItem(final int position) {
        return null;
    }

    @Override
    public long getItemId(final int position) {
        return 0;
    }

    /**
     * For each image available in the provided SESP image directory
     * inflate an image view which would show downgraded image
     *
     * @param position
     * @param convertView
     * @param parent
     *
     */
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View vw = convertView;
        try {
            if (this.ctxRef != null && this.ctxRef.get() != null) {
                final Context ctx = this.ctxRef.get();

                ImageView imageView = null;

                if (vw == null) {
                    imageView = new ImageView(ctx);
                    imageView.setLayoutParams(new GridView.LayoutParams(TARGET_HEIGHT_PX, TARGET_WIDTH_PX));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setPadding(8, 8, 8, 8);
                    vw = imageView;
                }

                if (imageView != null) {
                    imageView.setImageBitmap(getScaledImage(position));
                }
            }
        } catch (Exception e) {
            writeLog("WoImageViewAdapter  : getView() ", e);
        }
        return vw;
    }

    /**
     * The method decodes actual size of the image
     * and downgrades to dimensions as provided by
     * target height/target width
     *
     * @param position
     * @return
     */

    private Bitmap getScaledImage(final int position) {
        Bitmap bitmap = null;
        try {
            if (this.imageDir != null) {
                final File[] images = new File(this.imageDir).listFiles();
                if (images != null && images.length >= position) {
                    final File imageFile = images[position];
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;

                    BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

                    int imageActualHeight = options.outHeight;
                    int imageActualWidth = options.outWidth;

                    int actualHeight = imageActualHeight;
                    int actualWidth = imageActualWidth;

                    int sampleSize = 1;

                    while (imageActualHeight > TARGET_HEIGHT_PX || imageActualWidth > TARGET_WIDTH_PX) {
                        // Scale down with factor of 2 to the power
                        sampleSize *= IMAGE_DOWNGRADE_FACTOR;
                        imageActualHeight = actualHeight / sampleSize;
                        imageActualWidth = actualWidth / sampleSize;
                    }

                    options.outHeight = imageActualHeight;
                    options.outWidth = imageActualWidth;

                    // Now load into memory the scaled version image
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = sampleSize;
                    bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
                }
            }
        } catch (Exception e) {
            writeLog("WoImageViewAdapter  : getView() ", e);
        }
        return bitmap;
    }

    @Override
    public boolean onPreDraw() {
        if (this.indivImageView != null) {
            this.indivImageView.getViewTreeObserver().removeOnPreDrawListener(this);
        }
        return true;
    }

}
