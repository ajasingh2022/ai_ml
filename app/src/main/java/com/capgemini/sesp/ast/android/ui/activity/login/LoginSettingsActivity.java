package com.capgemini.sesp.ast.android.ui.activity.login;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.CommunicationHelper;
import com.capgemini.sesp.ast.android.module.util.LanguageHelper;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;
//import com.capgemini.sesp.android.communication.CommunicationHelper;

public class LoginSettingsActivity extends PreferenceActivity {
    private static final String KEY_SERVER_ADDRESS = "serverAddress";
    private static final String KEY_RESET = "reset";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            addPreferencesFromResource(R.xml.login_preferences);

            Preference serverAddressPreference = getPreferenceScreen().findPreference(KEY_SERVER_ADDRESS);
            serverAddressPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String serverAddress = (String) newValue;
                    CommunicationHelper.setServerAddress(serverAddress);
                    refreshSettings();
                    Toast.makeText(LoginSettingsActivity.this, R.string.saved, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            Preference resetPreference = getPreferenceScreen().findPreference(KEY_RESET);
            resetPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    CommunicationHelper.setServerAddress(CommunicationHelper.getDefaultServerAddress());
                    refreshSettings();
                    Toast.makeText(LoginSettingsActivity.this, R.string.default_values_applied, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            refreshSettings();
        } catch (Exception e) {
            writeLog("LoginSettingsActivity  : onCreate() ", e);
        }
    }

    private void refreshSettings() {
        try {
            EditTextPreference serverAddressPreference = (EditTextPreference) getPreferenceScreen().findPreference(KEY_SERVER_ADDRESS);
            serverAddressPreference.setText(CommunicationHelper.getServerAddress());
            serverAddressPreference.setSummary(CommunicationHelper.getServerAddress());
        } catch (Exception e) {
            writeLog("LoginSettingsActivity  : refreshSettings() ", e);
        }
    }

    @Override
    protected void onResume() {
        try{
        super.onResume();
        LanguageHelper.reloadIfLanguageChanged(this);
        } catch (Exception e) {
            writeLog("LoginSettingsActivity  : refreshSettings() ", e);
        }
    }

}
