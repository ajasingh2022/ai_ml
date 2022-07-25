package com.capgemini.sesp.ast.android.ui.activity.common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.ActivityConstants;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.HelpDocumentThread;
import com.capgemini.sesp.ast.android.module.util.LanguageHelper;
import com.capgemini.sesp.ast.android.module.util.ReloadTypeDataThread;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.ui.activity.dashboard.MainMenuSettingsActivity;
import com.capgemini.sesp.ast.android.ui.activity.login.LoginActivity;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class LanguageSelectionActivity extends AppCompatActivity {
	private transient String selectedLanguageCode;
	private transient OnClickListener onClickListener;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
		setContentView(R.layout.language_list);
		
		selectedLanguageCode = LanguageHelper.getLanguageCode();

		final LanguageListAdapter languageListAdapter = new LanguageListAdapter(getResources().getStringArray(R.array.items_localize));
		ListView languageListView = findViewById(R.id.language_list);
		languageListView.setAdapter(languageListAdapter);


		final TextView headerText = findViewById(R.id.title_text);
		headerText.setText(R.string.set_language);
		
		onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedLanguageCode = (String) v.getTag();
				SharedPreferences p = AndroidUtilsAstSep.getSharedPreferences(ConstantsAstSep.SharedPreferenceType.PERSISTENT);
				SharedPreferences.Editor pe = p.edit();
				pe.putString(ConstantsAstSep.SharedPreferenceKeys.LANGUAGE, selectedLanguageCode);
				pe.putBoolean("RELOAD_STATIC_TYPE_DATA", true);
				pe.putBoolean("RELOAD_BST_STATIC_TYPE_DATA", true);
				pe.apply();
				if (!LanguageHelper.setLanguage(selectedLanguageCode)) {
					Toast.makeText(LanguageSelectionActivity.this, getString(R.string.error_language_not_supported), Toast.LENGTH_SHORT).show();
				}

				final int callingActivity = getIntent().getIntExtra(ActivityConstants.CALLING_ACTIVITY, -1);
				final Intent intent = new Intent();
				switch (callingActivity) {
					case ActivityConstants.ACTIVITY_SPLASH:
					case ActivityConstants.ACTIVITY_LOGIN:
						intent.setClass(LanguageSelectionActivity.this, LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();
						break;
					case ActivityConstants.ACTIVITY_SETTINGS:
						String previousLanguageSelected = SESPPreferenceUtil.getPreferenceString("LanguageSelectedForHelp");
						if(!selectedLanguageCode.equals(previousLanguageSelected)) {
							DatabaseHandler.createDatabaseHandler().deleteHelpDocumentTable();
							new HelpDocumentThread().execute();
						}
						ReloadTypeDataThread reloadTypeDataThread = new ReloadTypeDataThread(LanguageSelectionActivity.this,
								MainMenuSettingsActivity.class, null, Intent.FLAG_ACTIVITY_CLEAR_TOP);
						reloadTypeDataThread.execute();
						break;
				}
			}
		};
		} catch (Exception e) {
			writeLog("LanguageSelectionActivity  : onCreate() ", e);
		}
	}

	private class LanguageListAdapter extends BaseAdapter {
		private transient String[] data;

		public LanguageListAdapter(final String[] data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.length;
		}

		@Override
		public Object getItem(final int position) {
			return data[position];
		}

		@Override
		public long getItemId(final int position) {
			return position;
		}

		@Override
		public View getView(final int position, final View convertView, final ViewGroup parent) {
			View vw = convertView;
			if (vw == null) {
				vw = LayoutInflater.from(LanguageSelectionActivity.this).inflate(R.layout.language_row, parent, false);
			}
				
			final ImageView languageFlag = vw.findViewById(R.id.flag_img);
			final TextView language = vw.findViewById(R.id.language);
			final RadioButton radio = vw.findViewById(R.id.radio);
			
			final String item = data[position];
			final String[] codeLangArr = item.split("#");
			final String name = codeLangArr[0];
			final String code = codeLangArr[1];
			
			vw.setTag(code);
			languageFlag.setImageDrawable(LanguageHelper.getFlagIcon(code));
			language.setText(name);
			
			radio.setChecked(code.equals(selectedLanguageCode));
			
			vw.setOnClickListener(onClickListener);

			return vw;
		}
	}

}
