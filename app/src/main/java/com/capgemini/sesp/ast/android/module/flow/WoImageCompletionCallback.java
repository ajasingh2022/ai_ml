/**
 * @copyright Capgemini
 */
package com.capgemini.sesp.ast.android.module.flow;

import android.app.Dialog;

/**
 * This interface would be implemented by flow pages
 * for which there is a business necessity to capture image and 
 * take action when the image is captured and image data is available for further processing.
 * 
 * <p>
 * After implementing this interface the flow engine framework would 
 * call this method to let the corresponding flow page know that image is 
 * captured and image data is available 
 * </p>
 * 
 * 
 * @author Capgemini
 * @version 1.0
 * @since 12th March, 2015
 *
 */
public interface WoImageCompletionCallback {

	void onImageReasonChoosenCompleted(Dialog popUp, String imageFileName);
}
