/**
 *  @copyright Capgemini
 */
package com.capgemini.sesp.ast.android.ui.activity.flow;

import com.capgemini.sesp.ast.android.module.communication.LocationCaptureInterface;
import com.capgemini.sesp.ast.android.module.communication.NetworkStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.communication.WorkflowImageCaptureCallbackListener;

/**
 * This is a marker interface to be used 
 * as reference inside flow engine
 *
 * <p>
 * The standard project implements this interface in {@link com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity}
 * </p>
 *
 * <p>
 * The purpose of this interface is to avoid reference to the implementation
 * class at runtime and make it implementation independent
 * </p>
 *
 *
 * @author Capgemini
 * @since 15th December, 2014
 * @version 1.0
 *
 */
public interface SESPFlowActivity extends NetworkStatusCallbackListener,
		LocationCaptureInterface {

	/**
	 *  This method should wrap all the detailed code for
	 *  handling image capture and allow the BST layer freedom to concentrate on 
	 *  business logic only
	 *
	 *  <p>
	 * The application must request for the following permissions in order to use this method
	 * <b>
	 *     android.permission.CAMERA
	 *   	<br>
	 *     android.permission.WRITE_EXTERNAL_STORAGE
	 * </b>
	 * </p>
	 */

	void takeSnapshot();


	/**
	 * When an image is captured this method would be called.
	 *
	 * <p>
	 * The parent activity may delegate the call to business/flow specific 
	 * pages/fragments
	 * </p>
	 *
	 * <p>
	 * The application must request for the following permissions in order to use this method
	 * <b>
	 *     android.permission.CAMERA
	 *   	<br>
	 *     android.permission.WRITE_EXTERNAL_STORAGE
	 * </b>
	 * </p>
	 *
	 * @param callBack {@link WorkflowImageCaptureCallbackListener}
	 */
	void regForImageCaptureInfo(final WorkflowImageCaptureCallbackListener callBack);

	/**
	 * Method to un-register for image capture callback
	 *
	 * @param callBack {@link WorkflowImageCaptureCallbackListener}
	 */

	void unregForImageCaptureInfo(final WorkflowImageCaptureCallbackListener callBack);

	/**
	 * This method would implement all the heavy lifting stuff (like initiating a broadcast receiver)
	 * for monitoring network status change call back.
	 *
	 * <p>
	 * The application must request the following permissions in-order to use this method
	 * <b>android.permission.ACCESS_NETWORK_STATE </b>
	 * </p>
	 *
	 * @param callBack {@link NetworkStatusCallbackListener}
	 */
	void regForNetStatChangeInfo(final NetworkStatusCallbackListener callBack);

	/**
	 * This method un-registers the listeners which have registered
	 * previously using regForNetStatChangeInfo.
	 *
	 * <p>
	 * All flow pages/activities/fragment that registers using regForNetStatChangeInfo
	 * should un-register during onPause/onStop call back stages  
	 * </p>
	 *
	 * @param callBack {@link NetworkStatusCallbackListener}
	 */
	void unregForNetStatChangeInfo(final NetworkStatusCallbackListener callBack);
}
