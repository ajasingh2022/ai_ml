package com.capgemini.sesp.ast.android.ui.activity.dashboard;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.CommunicationHelper;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.module.util.GuiDate;
import com.capgemini.sesp.ast.android.module.util.LanguageHelper;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.module.versionmanagement.SespStdVersionManager;
import com.skvader.rsp.cft.common.business.SystemInfo;
import com.skvader.rsp.cft.common.business.SystemInfoController;

import java.net.URL;
import java.util.Properties;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class MainMenuAboutActivity extends PreferenceActivity {

	private String TAG = MainMenuAboutActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.main_about);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updatePreferenceSummaries();
		LanguageHelper.reloadIfLanguageChanged(this);
	}
	
	private void updatePreferenceSummaries() {
		final String businessVersion = SespStdVersionManager.getCurrentBusinessVersion();
		final String frameworkVersion = SespStdVersionManager.getCurrentFrameworkVersion();
		try {
		SystemInfo bi = SystemInfoController.getInstance().getBusinessInfo();
        SystemInfo fi = SystemInfoController.getInstance().getFrameworkInfo();
        String systemInfo = System.getProperties().getProperty("os.name") + " " + System.getProperties().getProperty("os.version") + " " + System.getProperties().getProperty("os.arch");
        String javaVersion = System.getProperties().getProperty("java.vm.vendor") + " " + System.getProperties().getProperty("java.vm.version") + " " + System.getProperties().getProperty("java.vm.name");
        
        Properties p = new Properties();
		try {
			URL buildInfoUrl = AndroidUtilsAstSep.getResource("com/skvader/rsp/ast_sep/android/buildinfo.properties");
			p.load(buildInfoUrl.openStream());
		} catch (Exception e) {
			writeLog(TAG +": updatePreferenceSummaries() ", e );
		}
		String buildTime = p.getProperty("build.time", GuiDate.toGuiDateUsersTimesZone(ApplicationAstSep.STARTUP_TIME, true).toString());
		String buildUser = p.getProperty("build.user", System.getProperties().getProperty("user.name")); // user.name will not work on Android, but this will only happen when running from eclipse
		
        // session
		setSummary("logged_in_as", SessionState.getInstance().getCurrentUserUsername());
		setSummary("locale", LanguageHelper.getLanguageCode());
		setSummary("server_address", CommunicationHelper.getServerAddress());
		setSummary("database_information", AndroidUtilsAstSep.getDatabaseInfo());
		
		// build
		setSummary("framework_name", fi.getName() + " (" + fi.getCode() + ")");
		//setSummary("framework_version", fi.getVersion());
		setSummary("framework_version", frameworkVersion);
		setSummary("business_name", bi.getName() + " (" + bi.getCode() + ")");
		//setSummary("business_version", bi.getVersion());
		setSummary("business_version", businessVersion);
		setSummary("build_time", buildTime);
		setSummary("build_user", buildUser);
		
		// system
		setSummary("system", systemInfo);
		setSummary("java", javaVersion);
		} catch (Exception e) {
			writeLog(TAG +": updatePreferenceSummaries() ", e );
		}
	}

	private void setSummary(String preferenceName, Object summary) {
		Preference p;
		try {
			p = getPreferenceScreen().findPreference(preferenceName);
			p.setSummary(summary + "");
		}catch(Exception e)
		{
			writeLog(TAG + " : setSummary() " , e);
		}
	}
	
}
