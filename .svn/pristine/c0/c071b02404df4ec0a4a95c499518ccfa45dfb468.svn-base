package com.capgemini.sesp.ast.android.module.communication;

import android.location.LocationListener;

/**
 * Created by aditam on 3/2/2016.
 */
public interface LocationCaptureInterface extends LocationListener {

    /**
     *  Handle the LocationManager and course/fine location access
     *  in this method
     *
     *  <p>
     *  The implementing application must request for the following permissions
     *  in order to use this method
     *  <b>
     *      android.permission.INTERNET
     *      <br>
     *		android.permission.ACCESS_FINE_LOCATION
     *		<br>
     *		android.permission.ACCESS_COARSE_LOCATION
     *  </b></p>
     *
     *  @param callBack {@link LocationStatusCallbackListener}
     *  @param provider {@link String} Provider constant
     *
     */
    void regForLocationChangeInfo(final LocationStatusCallbackListener callBack,
                                  final String provider) throws Exception;

    /**
     * Reverse of regForLocationChangeInfo method
     *
     * Each implementing entity (activity/fragment) should unregister
     * at onStop/onPause
     *
     * @param callBack {@link LocationStatusCallbackListener}
     * @param provider {@link String} Provider constant
     */
    void unregForLocationChangeInfo(final LocationStatusCallbackListener callBack, final String provider);

}
