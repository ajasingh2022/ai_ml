/**
 *
 */
package com.capgemini.sesp.ast.android.module.versionmanagement;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.capgemini.sesp.ast.android.module.communication.CommunicationHelper;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * The version manager class for Standard Project, its job will be
 * as follows
 * <p>
 * <p><b>
 * <li>Check with existing web service/Push messenger to obtain the latest version available for this product</li>
 * <li>Compare version info and update user informally, if user chooses to update install the new version</li>
 * <li>Maintain internal data integrity and necessary backups before update</li>
 * <li>Inform user for probable data loss (if not backward compatible) and asks for confirmation</li>
 * <li>Relieves user for manually downloading/or the system proactively polling for version check</li>
 * <li>Allows the user to rollback to previous version if something goes wrong
 * <li>Shows list of available past versions and choose to rollback to any past version
 * </b></p>
 * <p>
 * <p>
 * The functionality of this class would be similar to auto-updater, however, it would provide
 * for manageable interface and would be running in seperate process with the identical linux user id under the hood
 * </p>
 * <p>
 * <p>
 * This would provide pluggable option for communicating with the server using chat protocols for receiving callback
 * from server
 * </p>
 *
 * @author nirmchak
 */
public class SespStdVersionManager {

    private static final String UPDATOR_ACTION_VERSION_CHECK = "new_version_check";
    private static final String UPDATOR_REQUIRED = "yes";
    private static final String UPDATOR_NOT_REQUIRED = "no";
    static String TAG = SespStdVersionManager.class.getSimpleName();

    /**
     * Returns the current version info of the application from android manifest info
     * The syntax is versionCode = major version number
     * version name = <minor version number>.<hotfix version>
     * <p>
     * Total output of the method = <major version number i.e. versioncode in manifest>.<minor version number>.<hotfix version number>
     *
     * @return
     */

    public static String getCurrentBusinessVersion() {
        String version = null;
        try {
            final Context context = ApplicationAstSep.context;
            Log.i("SespStdVersionManager", "Obtaining business version of package=" + context.getPackageName());
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (final Exception ex) {
            writeLog(TAG + " :getCurrentBusinessVersion() ", ex);
        }

        return version;
    }

    /**
     * Returns the current framework version read
     * from the manifest
     *
     * @return String
     */
    public static String getCurrentFrameworkVersion() {
        String version = null;
        try {
            final Context context = ApplicationAstSep.context;
            Log.i("SespStdVersionManager", "Obtaining framework version of package=" + context.getPackageName());
            final ApplicationInfo appInfo
                    = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            version = appInfo.metaData.getString(ConstantsAstSep.FRAMEWORK_VERSION_META);
        } catch (final Exception ex) {
            writeLog(TAG + ":getCurrentFrameworkVersion() ", ex);
        }

        return version;
    }

    /**
     * Returns the current framework version read
     * from the manifest
     *
     * @return String
     */
    public static boolean isUpdateAvailable() {
        try {
            String version = (String) CommunicationHelper.callUpdatorApp(UPDATOR_ACTION_VERSION_CHECK, getCurrentFrameworkVersion(), getCurrentBusinessVersion());
            if (version != null) {
                return true;
            }
        } catch (final Exception ex) {
            writeLog(TAG + ":isUpdateAvailable() ", ex);
        }
        return false;
    }

}
