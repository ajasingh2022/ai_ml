package com.capgemini.sesp.ast.android.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.text.InputType;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.SiteSelectionActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.StockTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitIdentifierTTO;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * This fragment displayes the tabbed UI for MaterialLogistics settings page.
 */
public class MLSettingsFragment extends PreferenceFragment {

    public static final String KEY_IDENTIFIER = "identifier";
    private static final String KEY_STOCK = "stock";
    private static final String KEY_LENGTH_OF_IDENTIFIER = "identifierLength";
    private static final String KEY_USE_IDENTIFIER_LENGTH = "identifierLengthOnOff";

    public static final String HIDE_LOCATION = "hide_location";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.material_logistics_preferences);

            Preference locationPreference = getPreferenceScreen().findPreference(KEY_STOCK);
            if (getActivity().getIntent().getBooleanExtra(HIDE_LOCATION, false)) {
                locationPreference.setEnabled(false);
            } else {
                locationPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        preference.setIntent(new Intent(
                                getActivity(),
                                SiteSelectionActivity.class).setFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(
                                ConstantsAstSep.ActivityConstants.CALLING_ACTIVITY,
                                ConstantsAstSep.ActivityConstants.ACTIVITY_SETTINGS));
                        refreshSettings();
                        return false;
                    }
                });
            }


            /**
             *  Populate Identifier List
             */
            ListPreference identifierPreferenceList = (ListPreference) getPreferenceScreen().findPreference(KEY_IDENTIFIER);
            List<UnitIdentifierTTO> unitIdentifierTTOs = ObjectCache.getAllTypes(UnitIdentifierTTO.class);
            String[] unitIdentifierTTONamesArray = new String[unitIdentifierTTOs.size() - 1];
            String[] unitIdentifierTTOValuesArray = new String[unitIdentifierTTOs.size() - 1];
            int count = 0;
            for (int i = 0; i < unitIdentifierTTOs.size(); i++) {
                if (!unitIdentifierTTOs.get(i).getId().equals(CONSTANTS().UNIT_IDENTIFIER_T__NA)) {
//                unitIdentifierTTONamesArray[i] = unitIdentifierTTOs.get(i).getName();
//                unitIdentifierTTOValuesArray[i] = String.valueOf(unitIdentifierTTOs.get(i).getId());
                    unitIdentifierTTONamesArray[count] = unitIdentifierTTOs.get(i).getName();
                    unitIdentifierTTOValuesArray[count] = String.valueOf(unitIdentifierTTOs.get(i).getId());
                    count++;
                }
            }
            identifierPreferenceList.setEntries(unitIdentifierTTONamesArray);
            identifierPreferenceList.setEntryValues(unitIdentifierTTOValuesArray);
            identifierPreferenceList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Long idUnitIdentifierT = Long.parseLong((String) newValue);
                    SESPPreferenceUtil.savePreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER, idUnitIdentifierT);
                    refreshSettings();
                    return true;
                }
            });

            CheckBoxPreference identifierLengthPreference = (CheckBoxPreference) getPreferenceScreen().findPreference(KEY_USE_IDENTIFIER_LENGTH);
            identifierLengthPreference.setChecked(SESPPreferenceUtil.isMLUnitIdentifierLengthUsed());
            identifierLengthPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    SESPPreferenceUtil.savePreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_IDENTFIER_LENGTH_USED, (Boolean) newValue);
                    return true;
                }
            });

            EditTextPreference identifierPreference = (EditTextPreference) getPreferenceScreen().findPreference(KEY_LENGTH_OF_IDENTIFIER);
            identifierPreference.getEditText().setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
            identifierPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Long newLongValue;
                    try {
                        newLongValue = Long.parseLong((String) newValue);
                    } catch (Exception e) {
                        newLongValue = 0L;
                    }
                    SESPPreferenceUtil.setGIAILength(newLongValue);
                    refreshSettings();
                    return true;
                }
            });

            refreshSettings();
        } catch (Exception e) {
            writeLog("MLSettingsFragment : onCreate() ", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshSettings();
    }

    private void refreshSettings() {
        try {
            Preference locationPreference = getPreferenceScreen().findPreference(KEY_STOCK);

            StockTO stockTOs = ObjectCache.getIdObject(StockTO.class, SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_STOCK));
            String selectedStock = null;
            if (stockTOs != null)
                selectedStock = stockTOs.getName();
            if (selectedStock == null)
                selectedStock = getString(R.string.na);
            locationPreference.setSummary(selectedStock);


            ListPreference identifierPreferenceList = (ListPreference) getPreferenceScreen().findPreference(KEY_IDENTIFIER);
            UnitIdentifierTTO unitIdentifierTTO = ObjectCache.getType(UnitIdentifierTTO.class,
                    SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER));
            String selectedIdentifier = null;
            if ((unitIdentifierTTO != null) && (unitIdentifierTTO.getName() != null)) {
                selectedIdentifier = unitIdentifierTTO.getName();
            } else {
                selectedIdentifier = getString(R.string.na);
            }
            identifierPreferenceList.setSummary(selectedIdentifier);

            EditTextPreference identifierPreference = (EditTextPreference) getPreferenceScreen().findPreference(KEY_LENGTH_OF_IDENTIFIER);
            Long giaiLength = SESPPreferenceUtil.getGIAILength();
            if (!(giaiLength > 0L)) {
                identifierPreference.setSummary(R.string.not_set);
            } else {
                identifierPreference.setSummary(String.valueOf(giaiLength));
            }
        } catch (Exception e) {
            writeLog("MLSettingsFragment : refreshSettings() ", e);
        }

    }
}
