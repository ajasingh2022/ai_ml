/**
 * 
 */
package com.capgemini.sesp.ast.android.module.others;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * The drawable to wrap the bitmap used for work-order
 * related image display
 * 
 * @author Capgemini
 * @version 1.0
 * @since 20th November, 2014
 *
 */
public class AsyncDrawable extends BitmapDrawable {

	// Reference to background thread that is decoding the image
	private WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference = null;

	// Constructor used to wrap the reference of related bitmap decoding thread
    public AsyncDrawable(final Resources res, final Bitmap bitmap,
            final BitmapWorkerTask bitmapWorkerTask) {
        super(res, bitmap);
        try{
        bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        } catch (Exception e) {
            writeLog( "AsyncDrawable :AsyncDrawable() ", e);
        }
    }

    public BitmapWorkerTask getBitmapWorkerTask() {
        return bitmapWorkerTaskReference.get();
    }

}
