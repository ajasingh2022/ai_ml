package com.capgemini.sesp.ast.android.module.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.MaterialLogisticListSetting;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceType;

import java.net.URISyntaxException;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


/**
 * Created by pramalli on 2/14/2016.
 */
public class SESPPreferenceUtil {


    /**
     * Returns sharedpreferences for SESP
     *
     * @param type name of the shared preferences to be returned
     * @return sharedpreference pbject
     */
    private static final long NULL = -1;

    private static SharedPreferences getSharedPreferences(SharedPreferenceType type) {
        return ApplicationAstSep.context.getSharedPreferences(type.name(), Context.MODE_PRIVATE);
    }

    /**
     * This method checks if preferences for a particular screen has been saved earlier by the user.
     *
     * @param - screen integer for which preferences needs to be checked.
     * @return - true or false based on findings.
     */
    public static boolean isPreferencesSaved(int screenIdentifier) {
        try {
            if (screenIdentifier == ConstantsAstSep.MainMenuItems.MATERIAL_LOGISTIC) {
                boolean identifierSet = false;
                boolean stockLocationSet = false;
                Long idUnitIdentifierT = getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER);
                if (idUnitIdentifierT != NULL) {
                    if (!idUnitIdentifierT.equals(AndroidUtilsAstSep.CONSTANTS().UNIT_IDENTIFIER_T__NA)) {
                        identifierSet = true;
                    }
                }
                Long idStock = getSharedPreferences(SharedPreferenceType.USER_SETTINGS).getLong(
                        SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK, -1
                );
                if (idStock != NULL) {
                    stockLocationSet = true;
                }
                return identifierSet && stockLocationSet;
            }
        }catch(Exception e) {
            writeLog("SESPPreferenceUtil  :networkStatusChanged()", e);
        }
        return true;
    }

    public static void savePreference(String preferenceKey, Long preferenceValue) {
        Editor pe = getSharedPreferences(SharedPreferenceType.USER_SETTINGS).edit();
        pe.putLong(preferenceKey, preferenceValue);
        pe.commit();
    }

    public static void savePreference(String preferenceKey, Boolean preferenceValue) {
        Editor pe = getSharedPreferences(SharedPreferenceType.USER_SETTINGS).edit();
        pe.putBoolean(preferenceKey, preferenceValue);
        pe.commit();
    }

    public static void savePreference(String preferenceKey, String preferenceValue) {
        Editor pe = getSharedPreferences(SharedPreferenceType.USER_SETTINGS).edit();
        pe.putString(preferenceKey, preferenceValue);
        pe.commit();
    }

    public static void savePreference(String preferenceKey, Intent preferenceValue) {
        if (preferenceValue != null) {
            Editor pe = getSharedPreferences(SharedPreferenceType.USER_SETTINGS).edit();
            pe.putString(preferenceKey, preferenceValue.toUri(0));
            pe.commit();
        }
    }

    public static String getPreferenceString(String preferenceKey) {
        return getSharedPreferences(SharedPreferenceType.USER_SETTINGS).getString(
                preferenceKey, ""
        );
    }

    public static Long getPreference(String preferenceKey) {
        return getSharedPreferences(SharedPreferenceType.USER_SETTINGS).getLong(
                preferenceKey, NULL
        );
    }

    public static Boolean getPreferenceBoolean(String preferenceKey) {
        return getSharedPreferences(SharedPreferenceType.USER_SETTINGS).getBoolean(
                preferenceKey, false
        );
    }

    public static Intent getPreferenceIntent(String preferenceKey) throws URISyntaxException {
        return Intent.getIntent(getSharedPreferences(SharedPreferenceType.USER_SETTINGS).getString(
                preferenceKey, ""
        ));
    }

    public static boolean isMLUnitIdentifierLengthUsed() {
        return getSharedPreferences(SharedPreferenceType.USER_SETTINGS).getBoolean(
                SharedPreferenceKeys.MATERIAL_LOGISTICS_IDENTFIER_LENGTH_USED, false
        );
    }

    public static void setMLIdentifierLengthUsed(boolean choice) {
        Editor pe = getSharedPreferences(SharedPreferenceType.USER_SETTINGS).edit();
        pe.putBoolean(SharedPreferenceKeys.MATERIAL_LOGISTICS_IDENTFIER_LENGTH_USED, choice);
        pe.commit();
    }

    public static void setGIAILength(Long length) {
        Editor pe = getSharedPreferences(SharedPreferenceType.USER_SETTINGS).edit();
        pe.putLong(SharedPreferenceKeys.MATERIAL_LOGISTICS_GIAI_LENGTH, length);
        pe.commit();
    }

    public static Long getGIAILength() {
        return getSharedPreferences(SharedPreferenceType.USER_SETTINGS).getLong(
                SharedPreferenceKeys.MATERIAL_LOGISTICS_GIAI_LENGTH, NULL
        );
    }

    public static void setSLNoLength(Long length) {
        Editor pe = getSharedPreferences(SharedPreferenceType.USER_SETTINGS).edit();
        pe.putLong(SharedPreferenceKeys.MATERIAL_LOGISTICS_SERIAL_NUMBER_LENGTH, length);
        pe.commit();
    }

    public static Long getSLNoLength() {
        return getSharedPreferences(SharedPreferenceType.USER_SETTINGS).getLong(
                SharedPreferenceKeys.MATERIAL_LOGISTICS_SERIAL_NUMBER_LENGTH, NULL
        );
    }

    public static void setPropertyNoLength(Long length) {
        Editor pe = getSharedPreferences(SharedPreferenceType.USER_SETTINGS).edit();
        pe.putLong(SharedPreferenceKeys.MATERIAL_LOGISTICS_PROPERTY_NUMBER_LENGTH, length);
        pe.commit();
    }

    public static Long getPropertyNoLength() {
        return getSharedPreferences(SharedPreferenceType.USER_SETTINGS).getLong(
                SharedPreferenceKeys.MATERIAL_LOGISTICS_PROPERTY_NUMBER_LENGTH, NULL
        );
    }

    public static int getCurrentMLUnitIdentifier() {
        Long identifier = getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER);
        if (identifier != NULL) {
            if (identifier.equals(CONSTANTS().UNIT_IDENTIFIER_T__GIAI)) {
                return MaterialLogisticListSetting.IDENTIFIER_GIAI;
            } else if (identifier.equals(CONSTANTS().UNIT_IDENTIFIER_T__SERIALNO)) {
                return MaterialLogisticListSetting.IDENTIFIER_SLNO;
            } else if (identifier.equals(CONSTANTS().UNIT_IDENTIFIER_T__PROPERTYNO)) {
                return MaterialLogisticListSetting.IDENTIFIER_PROPNO;
            }
        }
        return MaterialLogisticListSetting.NO_IDENTIFIER;
    }

    public static Long getIdentifierLengthNullIfOff() {
        Long identifier = getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER);
        if (identifier != NULL) {
            if (identifier.equals(CONSTANTS().UNIT_IDENTIFIER_T__GIAI)) {
                return checkUnitIdentifierLength(getGIAILength());
            } else if (identifier.equals(CONSTANTS().UNIT_IDENTIFIER_T__SERIALNO)) {
                return checkUnitIdentifierLength(getSLNoLength());
            } else if (identifier.equals(CONSTANTS().UNIT_IDENTIFIER_T__PROPERTYNO)) {
                return checkUnitIdentifierLength(getPropertyNoLength());
            }
        }
        return null;
    }

    private static Long checkUnitIdentifierLength(Long length) {
        if ((length != null) && (!length.equals(0L)) && (isMLUnitIdentifierLengthUsed())) {
            return length;
        } else {
            return null;
        }
    }

    /**
     * adds TYPE_DATA_DOWNLOADED_TIME (Current time)to preferences
     */
    public static void setTypedataDownloadTime() {
        Editor pe = getSharedPreferences(SharedPreferenceType.USER_SETTINGS).edit();
        pe.putString(SharedPreferenceKeys.TYPE_DATA_DOWNLOADED_TIME, AndroidUtilsAstSep.getCurrentDate());
        pe.commit();
    }

    /**
     * returns TYPE_DATA_DOWNLOADED_TIME
     *
     * @return
     */
    public static String getTypedataDownloadTime() {
        return getPreferenceString(SharedPreferenceKeys.TYPE_DATA_DOWNLOADED_TIME);
    }

    /**
     * adds WO_DOWNLOADED_TIME (Current time)to preferences
     */
    public static void setWoDownloadTime() {
        Editor pe = getSharedPreferences(SharedPreferenceType.USER_SETTINGS).edit();
        pe.putString(SharedPreferenceKeys.WO_DOWNLOADED_TIME, AndroidUtilsAstSep.getCurrentDate());
        pe.commit();
    }

    /**
     * Deletes  WO_DOWNLOADED_TIME n TYPE_DATA_DOWNLOADED_TIME
     * from preferences
     */
    public static void removeDownloadTimes() {
        Editor pe = getSharedPreferences(SharedPreferenceType.USER_SETTINGS).edit();
        pe.remove(SharedPreferenceKeys.WO_DOWNLOADED_TIME);
        pe.remove(SharedPreferenceKeys.TYPE_DATA_DOWNLOADED_TIME);
        pe.commit();
    }

    /**
     * returns WO_DOWNLOADED_TIME
     *
     * @return
     */
    public static String getWoDownloadTime() {
        return getPreferenceString(SharedPreferenceKeys.WO_DOWNLOADED_TIME);
    }

    public static void setForcedOfflineStatus(boolean forcedOfflineStatus) {
        Editor pe = getSharedPreferences(SharedPreferenceType.USER_SETTINGS).edit();
        pe.putBoolean(SharedPreferenceKeys.FORCED_OFFLINE_MODE, forcedOfflineStatus);
        pe.commit();
    }

    public static boolean getForcedOfflineStatus() {
        return getPreferenceBoolean(SharedPreferenceKeys.FORCED_OFFLINE_MODE);
    }
    public static void removeUserLocation() {

        Editor pe = getSharedPreferences(SharedPreferenceType.USER_SETTINGS).edit();
        pe.remove(SharedPreferenceKeys.GPS_X_COORD);
        pe.remove(SharedPreferenceKeys.GPS_Y_COORD);
        pe.remove(SharedPreferenceKeys.GPS_COORD_SYS_TYPE);
        pe.commit();

    }
}
