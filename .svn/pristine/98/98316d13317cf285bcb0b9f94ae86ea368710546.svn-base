package com.capgemini.sesp.ast.android.ui.activity.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.ActivityConstants;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceType;
import com.capgemini.sesp.ast.android.module.util.GuiDate;
import com.capgemini.sesp.ast.android.module.util.LanguageHelper;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.ui.activity.common.LanguageSelectionActivity;
import com.capgemini.sesp.ast.android.ui.customview.SESPSwitchPreference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class MainMenuSettingsActivity extends PreferenceActivity {
	private static final String KEY_LIST_PREFERENCE = "list_lang";
	private static final String ENABLE_BT_SCANNING = "enable_bt_scanning";
	private static final String MAX_MINUTES_BETWEEN_DATA_UPDATE = "max_minutes_between_data_update";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{

		addPreferencesFromResource(R.xml.main_preferences);
		
		Preference mLanguagePreference = getPreferenceScreen().findPreference(KEY_LIST_PREFERENCE);
		mLanguagePreference.setSummary(LanguageHelper.getLanguageName());
		mLanguagePreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				preference.setIntent(new Intent(
						getApplicationContext(),
						LanguageSelectionActivity.class).setFlags(
						Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(
						ActivityConstants.CALLING_ACTIVITY,
						ActivityConstants.ACTIVITY_SETTINGS));

				return false;
			}
		});

		SESPSwitchPreference btScanEnablePreference = (SESPSwitchPreference) getPreferenceScreen().findPreference(ENABLE_BT_SCANNING);
		btScanEnablePreference.setWidgetLayoutResource(R.layout.custom_switch); // THIS IS THE KEY OF ALL THIS. HERE YOU SET A CUSTOM LAYOUT FOR THE WIDGET
		btScanEnablePreference.setChecked(SESPPreferenceUtil.getPreferenceBoolean(SharedPreferenceKeys.ENABLE_BT_SCANNING));
		btScanEnablePreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				SESPPreferenceUtil.savePreference(SharedPreferenceKeys.ENABLE_BT_SCANNING, (Boolean) newValue);
				return true;
			}
		});
		} catch (Exception e) {
			writeLog("MainMenuSettingActivity  : onCreate() ", e);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		LanguageHelper.reloadIfLanguageChanged(this);
	}
	
	
	public static Long getMaxMinutesBetweenDataUpdateSetting() {
		Long minutes = null;
		try{
		SharedPreferences settings = AndroidUtilsAstSep.getSharedPreferences(SharedPreferenceType.USER_SETTINGS);
	     minutes = settings.getLong(SharedPreferenceKeys.SETTING_MAX_MINUTES_BETWEEN_DATA_UPDATE, 
	    		ConstantsAstSep.DefaultSettings.MAX_MINUTES_BETWEEN_UPDATE_OF_DATA);
			} catch (Exception e) {
				writeLog("MainMenuAboutActivity  : updateTimeDataWasDownloaded() ", e);
			}
	    return minutes;
	}
	/**
	 * 
	 * @param minutes
	 * @return true if successful, false otherwise
	 */
	public static boolean setMaxMinutesBetweenDataUpdateSetting(Long minutes) {
		if (minutes != null && minutes > 0L){
			SharedPreferences settings = AndroidUtilsAstSep.getSharedPreferences(SharedPreferenceType.USER_SETTINGS);
			Editor pe = settings.edit();
		    pe.putLong(SharedPreferenceKeys.SETTING_MAX_MINUTES_BETWEEN_DATA_UPDATE, minutes);
		    pe.apply();
		    return true;
		} else {
			return false;
		}
	}
	
	public static boolean isTimeToUpdateData() {
		/*SharedPreferences settings = AndroidUtilsAstSep.getSharedPreferences(SharedPreferenceType.USER_SETTINGS);
	    Long minutes = settings.getLong(SharedPreferenceKeys.SETTING_MAX_MINUTES_BETWEEN_DATA_UPDATE, 
	    		ConstantsAstSep.DefaultSettings.MAX_MINUTES_BETWEEN_UPDATE_OF_DATA);
	    
	    String lastUpdateString = settings.getString(SharedPreferenceKeys.SETTING_LAST_DATA_UPDATE, null);
	    
	    if (lastUpdateString == null){
	    	return true;
	    } else {
		    final SimpleDateFormat sdf = new SimpleDateFormat(GuiDate.DATE_TIME_FORMAT, Locale.getDefault());
		    
		    Calendar cal = Calendar.getInstance();
		    try {
	            cal.setTime(sdf.parse(lastUpdateString));
		        cal.add(Calendar.MINUTE, minutes.intValue());
		        Date timeWhenUpdateNeeded = cal.getTime();
			    
			    Date now = new Date();
			    
			    return now.after(timeWhenUpdateNeeded);
		    } catch (ParseException e) {
		    	return true;
		    }
	    }*/
		
		return true;
	}

	public static void updateTimeDataWasDownloaded() {
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat(GuiDate.DATE_TIME_FORMAT, Locale.getDefault());
			final String updatedTime = sdf.format(new Date());

			final SharedPreferences settings = AndroidUtilsAstSep.getSharedPreferences(SharedPreferenceType.USER_SETTINGS);
			settings.edit()
					.putString(SharedPreferenceKeys.SETTING_LAST_DATA_UPDATE, updatedTime)
					.apply();

		} catch (Exception e) {
			writeLog("MainMenuAboutActivity  : updateTimeDataWasDownloaded() ", e);
		}
	}
}
