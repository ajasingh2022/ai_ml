package com.capgemini.sesp.ast.android.ui.activity.material_logistics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.ActivityConstants;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceType;
import com.capgemini.sesp.ast.android.module.util.LanguageHelper;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.dashboard.DashboardActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.StockTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitIdentifierTTO;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class MaterialLogisticsSettingsActivity extends PreferenceActivity {
	private static final long NULL = -1;
	
	public  static final String KEY_IDENTIFIER = "identifier";
	private static final String KEY_STOCK = "stock";
	private static final String KEY_LENGTH_OF_IDENTIFIER = "identifierLength";
	private static final String KEY_USE_IDENTIFIER_LENGTH = "identifierLengthOnOff";

	public static final String HIDE_LOCATION = "hide_location";
	
	private boolean settingsOkBeforeEnterThisScreen = false;
	
	/** IMPORTANT: USE SETTER AND GETTER TO EDIT THESE VARIABLES! */
	private static Long idUnitIdentifierT  			= null;
	private static Long idStock            			= null; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
		addPreferencesFromResource(R.xml.material_logistics_preferences);
		
		settingsOkBeforeEnterThisScreen = isSettingsSet();
		
		{ // Location/stock
			Preference locationPreference = getPreferenceScreen().findPreference(KEY_STOCK);
			
    		if (getIntent().getBooleanExtra(HIDE_LOCATION, false)) {
    			locationPreference.setEnabled(false);
    		} else {
				locationPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						preference.setIntent(new Intent(getApplicationContext(),SiteSelectionActivity.class).setFlags(
								Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(
								ActivityConstants.CALLING_ACTIVITY,
								ActivityConstants.ACTIVITY_SETTINGS));
						  refreshSettings();
						return false;
					}
				});
    		}
		}
	
		{ // Identifier
            ListPreference identifierPreference = (ListPreference) getPreferenceScreen().findPreference(KEY_IDENTIFIER);
            List<UnitIdentifierTTO> unitIdentifierTTOs = ObjectCache.getAllTypes(UnitIdentifierTTO.class);
            String[] unitIdentifierTTONamesArray = new String[unitIdentifierTTOs.size() - 1];
            String[] unitIdentifierTTOValuesArray = new String[unitIdentifierTTOs.size() - 1];
            for (int i = 0; i < unitIdentifierTTOs.size(); i++) {
            	if (!unitIdentifierTTOs.get(i).getId().equals(CONSTANTS().UNIT_IDENTIFIER_T__NA)) {
	                unitIdentifierTTONamesArray[i] = unitIdentifierTTOs.get(i).getName();
	                unitIdentifierTTOValuesArray[i] = String.valueOf(unitIdentifierTTOs.get(i).getId());
            	}
            }
            identifierPreference.setEntries(unitIdentifierTTONamesArray);
            identifierPreference.setEntryValues(unitIdentifierTTOValuesArray);
            identifierPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Long idUnitIdentifierT = Long.parseLong((String)newValue);
                    setIdUnitIdentifierT(idUnitIdentifierT);
                    refreshSettings();
                    return true;
                }
            });
        }
		
		{ // Use fixed length of identifier
			CheckBoxPreference onOffpreference = (CheckBoxPreference) getPreferenceScreen().findPreference(KEY_USE_IDENTIFIER_LENGTH);
			boolean fixedIdentfierOn = isIdentifierLengthUsed();
			onOffpreference.setChecked(fixedIdentfierOn);
			onOffpreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                	setIdentifierLengthUsed((Boolean) newValue);
                    refreshSettings();
                    return true;
                }
            });
		}
		
		{ // Length of GIAI
			EditTextPreference preference = (EditTextPreference) getPreferenceScreen().findPreference(KEY_LENGTH_OF_IDENTIFIER);
			preference.getEditText().setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
            preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                	Long newLongValue;
                	try {
                		newLongValue = Long.parseLong((String) newValue);
                	} catch (Exception e) {
                		newLongValue = 0L;
                	}
                    setLengthOfGiai(newLongValue);
                    refreshSettings();
                    return true;
                }
            });
		}


		refreshSettings();
		if (!isSettingsSet()) {
		    Toast.makeText(MaterialLogisticsSettingsActivity.this, R.string.you_need_to_select_identifier_and_location, Toast.LENGTH_SHORT).show();
		}
		} catch (Exception e) {
			writeLog("MaterialLogisticsSettingsActivity  : onCreate() ", e);
		}
	}

	private static SharedPreferences getSettingsSP() {
		return AndroidUtilsAstSep.getSharedPreferences(SharedPreferenceType.USER_SETTINGS);
    }

	@Override
    protected void onResume() {
        super.onResume();
        try{
        LanguageHelper.reloadIfLanguageChanged(this);
        refreshSettings();
		} catch (Exception e) {
			writeLog("MaterialLogisticsSettingsActivity  : onResume() ", e);
		}
    }
	
	private void refreshSettings() {
		try{
	    ListPreference identifierPreference = (ListPreference) getPreferenceScreen().findPreference(KEY_IDENTIFIER);
	    UnitIdentifierTTO unitIdentifierTTO = ObjectCache.getType(UnitIdentifierTTO.class, getIdUnitIdentifierT());
	    String selectedIdentifier = null;
	    if (unitIdentifierTTO != null)
	        selectedIdentifier = unitIdentifierTTO.getName();
	    if (selectedIdentifier == null) 
	        selectedIdentifier = getString(R.string.na);		
	    identifierPreference.setSummary(selectedIdentifier);
	    
	    Preference locationPreference = getPreferenceScreen().findPreference(KEY_STOCK);
	    StockTO stockTOs = ObjectCache.getIdObject(StockTO.class, getIdStock());
        String selectedStock = null;
        if (stockTOs != null)
        	selectedStock = stockTOs.getName();
        if (selectedStock == null) 
        	selectedStock = getString(R.string.na);        
        locationPreference.setSummary(selectedStock); 
	          
        EditTextPreference giaiPreference = (EditTextPreference) getPreferenceScreen().findPreference(KEY_LENGTH_OF_IDENTIFIER);
        Long giaiLength = getLengthOfGiai();
        if (giaiLength == 0L) {
        	giaiPreference.setSummary(R.string.not_set);
        } else {
        	giaiPreference.setSummary(String.valueOf(giaiLength));
        }
		} catch (Exception e) {
			writeLog("MaterialLogisticsSettingsActivity  : refreshSettings() ", e);
		}

	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            boolean settingsOK = isSettingsSet();
            if (settingsOK && !settingsOkBeforeEnterThisScreen) { // Go to Material Logistics menu
                Toast.makeText(MaterialLogisticsSettingsActivity.this, R.string.settings_are_ok, Toast.LENGTH_SHORT).show();
                Intent goToIntent = new Intent(getApplicationContext(), MaterialLogisticsActivity.class);
                startActivity(goToIntent);
            } else if (settingsOK && settingsOkBeforeEnterThisScreen) { 
            	// Settings was ok before entered this screen and is still ok, that means users came from some screen within material logistics.
            	// Just go back.
            	return super.onKeyDown(keyCode, event);
            } else { // User need to select identifier and location to show Material Logistics and has not done it. Go back to Main Menu.
                Toast.makeText(MaterialLogisticsSettingsActivity.this, R.string.you_have_to_select_identifier_and_location_to_be_able_to_go_to_material_logistics, Toast.LENGTH_LONG).show();
                Intent goToIntent = new Intent(getApplicationContext(), ActivityFactory.getInstance().getActivityClass(ActivityConstants.MAIN_MENU_ACTIVITY));
                startActivity(goToIntent);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

	public static boolean isSettingsSet() {
	    boolean identifierSet = false;
	    Long idIdentifiert = getIdUnitIdentifierT();
	    if (idIdentifiert != null && idIdentifiert != NULL) {
	    	if (!idIdentifiert.equals(AndroidUtilsAstSep.CONSTANTS().UNIT_IDENTIFIER_T__NA)) {
	    		identifierSet = true;
	    	}
	    }
	    boolean locationSet = getIdStock() != NULL;
        
	    return identifierSet && locationSet;
    }

    public static Long getIdUnitIdentifierT() {
        return getSettingsSP().getLong(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER, NULL);
    }

    public static void setIdUnitIdentifierT(Long idUnitIdentifierT) {
		try{
        MaterialLogisticsSettingsActivity.idUnitIdentifierT = idUnitIdentifierT;
        
        /** Store setting */
        Editor pe = getSettingsSP().edit();
        pe.putLong(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER, idUnitIdentifierT);
        pe.commit();
		} catch (Exception e) {
			writeLog("MaterialLogisticsSettingsActivity  : setIdUnitIdentifierT() ", e);
		}
    }

    public static Long getIdStock() {
        /** Load setting */
		return idStock = getSettingsSP().getLong(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK, NULL);
	}
    
    public static void setIdStock(Long idStock) {
		try{
        MaterialLogisticsSettingsActivity.idStock = idStock;
        
        /** Store setting */
        Editor pe = getSettingsSP().edit();
        pe.putLong(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK, idStock);
        pe.commit();
		} catch (Exception e) {
			writeLog("MaterialLogisticsSettingsActivity  : setIdStock() ", e);
		}
    }
    
	private void setIdentifierLengthUsed(boolean on) {
		try{
		Editor pe = getSettingsSP().edit();
        pe.putBoolean(SharedPreferenceKeys.MATERIAL_LOGISTICS_IDENTFIER_LENGTH_USED, on);
        pe.commit();
		} catch (Exception e) {
			writeLog("MaterialLogisticsSettingsActivity  : setIdentifierLengthUsed() ", e);
		}
    }
    
    public static boolean isIdentifierLengthUsed() {
		return getSettingsSP().getBoolean(SharedPreferenceKeys.MATERIAL_LOGISTICS_IDENTFIER_LENGTH_USED, false); // Default is off
    }
    
    private void setLengthOfGiai(Long length) {
		try{
        Editor pe = getSettingsSP().edit();
        pe.putLong(SharedPreferenceKeys.MATERIAL_LOGISTICS_GIAI_LENGTH, length);
        pe.commit();
		} catch (Exception e) {
			writeLog("MaterialLogisticsSettingsActivity  : setLengthOfGiai() ", e);
		}
    }
    
    private static Long getLengthOfGiai() {
    	return getSettingsSP().getLong(SharedPreferenceKeys.MATERIAL_LOGISTICS_GIAI_LENGTH, 22L); // Standard length of GIAI
    }
    
    public static Long getLengthOfGiaiWithNullIfOff() {
		Long length = getLengthOfGiai();
		if (!length.equals(0L) && isIdentifierLengthUsed()){
			return length;
		} else {
			return null;
		}
    }
    
	private void setLengthOfSerialNumber(Long length) {
		Editor pe = getSettingsSP().edit();
        pe.putLong(SharedPreferenceKeys.MATERIAL_LOGISTICS_SERIAL_NUMBER_LENGTH, length);
        pe.commit();
    }
    
	private static Long getLengthOfSerialNumber() {
		return getSettingsSP().getLong(SharedPreferenceKeys.MATERIAL_LOGISTICS_SERIAL_NUMBER_LENGTH, 0L); // 0 means it is off
    }
	
	public static Long getLengthOfSerialNumberWithNullIfOff() {
		Long length = getLengthOfSerialNumber();
		if (!length.equals(0L) && isIdentifierLengthUsed()){
			return length;
		} else {
			return null;
		}
    }
	
	private static Long getLengthOfPropertyNumber() {
		return getSettingsSP().getLong(SharedPreferenceKeys.MATERIAL_LOGISTICS_PROPERTY_NUMBER_LENGTH, 0L); // 0 means it is off
    }
	
	public static Long getLengthOfPropertyNumberWithNullIfOff() {
		Long length = getLengthOfPropertyNumber();
		if (!length.equals(0L) && isIdentifierLengthUsed()){
			return length;
		} else {
			return null;
		}
    }
	
	private void setLengthOfPropertyNumber(Long length) {
		Editor pe = getSettingsSP().edit();
        pe.putLong(SharedPreferenceKeys.MATERIAL_LOGISTICS_PROPERTY_NUMBER_LENGTH, length);
        pe.commit();
    }
	
	public static Long getLengthOfCurrentIdentifierWithNullIfOff() {
		Long identifier = getIdUnitIdentifierT();
		
		if (identifier != null && identifier != NULL) {
			Long length = null;
			
			if (identifier.equals(CONSTANTS().UNIT_IDENTIFIER_T__GIAI)) {
				length = getLengthOfGiaiWithNullIfOff();
			} else if (identifier.equals(CONSTANTS().UNIT_IDENTIFIER_T__SERIALNO)) {
				length = getLengthOfSerialNumberWithNullIfOff();
			} else if (identifier.equals(CONSTANTS().UNIT_IDENTIFIER_T__PROPERTYNO)) {
				length = getLengthOfPropertyNumberWithNullIfOff();
			}
			return length;
		} else {
			return null;
		}
    }
}
