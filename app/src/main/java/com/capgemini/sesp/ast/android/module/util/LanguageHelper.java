package com.capgemini.sesp.ast.android.module.util;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.ArrayMap;


import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceType;


import java.util.Locale;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class LanguageHelper {
    private static Map<Class<? extends Activity>, String> lastUsedLanguageCodes = new ArrayMap<Class<? extends Activity>, String>();
    private static Map<String, Drawable> flagIcons = null;

    public static boolean isLanguageSet() {
        SharedPreferences p = AndroidUtilsAstSep.getSharedPreferences(SharedPreferenceType.PERSISTENT);
        return p.contains(SharedPreferenceKeys.LANGUAGE);
    }

    public static String getCountryCode() {
        return Locale.getDefault().getCountry();
    }

    public static String getLanguageCode() {
        SharedPreferences p = null;
        try {
            p = AndroidUtilsAstSep.getSharedPreferences(SharedPreferenceType.PERSISTENT);
        } catch (Exception e) {
            writeLog("LanguageHelper : getLanguageCode()", e);
        }
        return p.getString(SharedPreferenceKeys.LANGUAGE, "en");
    }

    public static String getLanguageName() {
        try {
            String currentLanguageCode = LanguageHelper.getLanguageCode();
            SESPPreferenceUtil.savePreference("LanguageSelectedForHelp",currentLanguageCode);
            String[] languages = ApplicationAstSep.context.getResources().getStringArray(R.array.items_localize);
            for (String language : languages) {
                String[] split = language.split("#");
                String languageName = split[0];
                String languageCode = split[1];

                if (currentLanguageCode.equals(languageCode)) {
                    return languageName;
                }
            }
        } catch (Exception e) {
            writeLog("LanguageHelper : getLanguageName()", e);
        }
        return null;
    }

    public static boolean setLanguage(String languageCode) {

        boolean languageSupported = isLanguageSupported(languageCode);
        try {
            if (languageSupported) {
                Locale locale = new Locale(languageCode);
                Configuration config = new Configuration();
                config.locale = locale;
                ApplicationAstSep.context.getResources().updateConfiguration(config, ApplicationAstSep.context.getResources().getDisplayMetrics());
            }
        } catch (Exception e) {
            writeLog("LanguageHelper : setLanguage()", e);
        }
        return languageSupported;
    }

    private static boolean isLanguageSupported(String langCode) {
        try {
            for (Locale locale : Locale.getAvailableLocales()) {
                if (locale.getLanguage().equals(langCode)) {
                    return true;
                }
            }
        } catch (Exception e) {
            writeLog("LanguageHelper : isLanguageSupported()", e);
        }
        return false;
    }

    public static void reloadIfLanguageChanged(Activity activity) {
        try {
            String lastUsedLanguageCode = lastUsedLanguageCodes.get(activity.getClass());
            String currentLanguageCode = getLanguageCode();
            if (lastUsedLanguageCode == null) {
                lastUsedLanguageCode = currentLanguageCode;
                //If SESP crashes
                if (lastUsedLanguageCodes.isEmpty()) {
                    lastUsedLanguageCodes.put(activity.getClass(), currentLanguageCode);
                    setLanguage(currentLanguageCode);
                    Intent intent = new Intent(activity, activity.getClass());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(intent);
                }

                lastUsedLanguageCodes.put(activity.getClass(), currentLanguageCode);
            }

            if (!lastUsedLanguageCode.equals(currentLanguageCode)) {
                lastUsedLanguageCodes.put(activity.getClass(), currentLanguageCode);

                Intent intent = new Intent(activity, activity.getClass());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        } catch (Exception e) {
            writeLog("LanguageHelper : reloadIfLanguageChanged()", e);
        }
    }

    public static Drawable getFlagIcon(String languageCode) {
        try {
            if (flagIcons == null) {
                flagIcons = new ArrayMap<String, Drawable>();
                Resources r = ApplicationAstSep.context.getResources();

                flagIcons.put("en", r.getDrawable(R.drawable.ic_flag_united_kingdom));
                flagIcons.put("ar", r.getDrawable(R.drawable.ic_flag_hungary));
                flagIcons.put("sv", r.getDrawable(R.drawable.ic_flag_sweden));
                flagIcons.put("fi", r.getDrawable(R.drawable.ic_flag_finland));
                flagIcons.put("fr", r.getDrawable(R.drawable.ic_flag_france));
                flagIcons.put("hu", r.getDrawable(R.drawable.ic_flag_hungary));
                flagIcons.put("de", r.getDrawable(R.drawable.ic_flag_germany));
                flagIcons.put("zh", r.getDrawable(R.drawable.ic_flag_china));
            }
        } catch (Exception e) {
            writeLog("LanguageHelper : getFlagIcon()", e);
        }
        return flagIcons.get(languageCode);
    }

}
