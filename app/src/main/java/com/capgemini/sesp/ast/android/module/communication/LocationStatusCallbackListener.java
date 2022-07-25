/**
 * 
 */
package com.capgemini.sesp.ast.android.module.communication;

import android.location.Location;

/**
 * @author nirmchak
 *
 */
public interface LocationStatusCallbackListener {

	void onLocationChanged(final Location location);
}
