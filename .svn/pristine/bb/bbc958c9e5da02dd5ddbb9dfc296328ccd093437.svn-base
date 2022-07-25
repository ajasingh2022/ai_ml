package com.capgemini.sesp.ast.android.module.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by samdasgu on 11/15/2016.
 */
public class PermissionUtil {

    private PermissionUtil() {
    }

    public static boolean checkPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (!checkPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissions(Object o, int permissionId, String... permissions) {
        if (o instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) o, permissions, permissionId);
        }
    }
}
