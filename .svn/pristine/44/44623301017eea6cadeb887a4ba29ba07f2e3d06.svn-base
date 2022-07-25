package com.capgemini.sesp.ast.android.ui.activity.material_logistics.manage_pallet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.GuiWorker;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.ui.activity.common.ActivityFactory;
import com.capgemini.sesp.ast.android.ui.activity.scanner.BarcodeScanner;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.PalletLightTO;

import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.ActivityConstants;

public class SelectPalletActivity extends AppCompatActivity {

    private static final int SCAN_REQUEST = 1;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pallet);

    //    getSupportActionBar().setTitle(R.string.title_pallet_handling);
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(false);

        /*
         * Setting up custom action bar view
         */
        final ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final LayoutInflater layoutManager = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vw = layoutManager.inflate(R.layout.custom_action_bar_layout, null);
        ImageButton help_btn = vw.findViewById(R.id.menu_help);
        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new HelpDialog(SelectPalletActivity.this,ConstantsAstSep.HelpDocumentConstant.PAGE_MANAGE_PALLET);
                dialog.show();
            }
        });

        // -- Customizing the action bar ends -----
        getSupportActionBar().setCustomView(vw);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        TextView txtTitleBar = (TextView) findViewById(R.id.title_text);
		txtTitleBar.setText(R.string.title_pallet_handling);

        PackageManager pm = getPackageManager();
        Button  scanButton =  findViewById(R.id.scanPalletCodeButton);
        Button okButton = findViewById(R.id.okButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                scanButtonClicked(v);
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                okButtonClicked(v);
            }
        });

        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                SESPPreferenceUtil.getPreferenceBoolean(ConstantsAstSep.SharedPreferenceKeys.ENABLE_BT_SCANNING)){
            scanButton.setVisibility(View.INVISIBLE);
        }
    }
  
    public void okButtonClicked(View view) {
        /* Validation before sending request to web service */
    	EditText palletCodeEditText = findViewById(R.id.palletCodeText);
        String palletCode = palletCodeEditText.getText().toString();
        if (TextUtils.isEmpty(palletCode)) {
            Toast.makeText(this, R.string.pallet_code_can_not_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        new GetPalletWorkerThread(this,getString(R.string.fetching_pallet),palletCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void scanButtonClicked(View view) {
        Intent barCodeScanner = new Intent(getApplicationContext(), BarcodeScanner.class);
        startActivityForResult(barCodeScanner, SCAN_REQUEST );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_REQUEST && resultCode == Activity.RESULT_OK && data != null){
            EditText palletCodeEditText = findViewById(R.id.palletCodeText);
            palletCodeEditText.setText(data.getStringExtra("barcode_result"));
        }
    }

    private class GetPalletWorkerThread extends GuiWorker<PalletLightTO> {

        private String palletCode;

        public GetPalletWorkerThread(Activity ownerActivity,String progressDialogMessage,String palletCode) {
            super(ownerActivity, null,progressDialogMessage);
            this.palletCode = palletCode;
        }

        @Override
        protected PalletLightTO runInBackground() throws Exception {
            PalletLightTO palletLightTO = AndroidUtilsAstSep.getDelegate().getPallet(palletCode);
            return palletLightTO;
        }

        @Override
        protected void onPostExecute(boolean successful, PalletLightTO result) {
            if(successful){
                Intent palletInformationIntent = new Intent(getApplicationContext(), ActivityFactory.getInstance().getActivityClass(ActivityConstants.PALLET_INFORMATION_ACTIVITY));
                palletInformationIntent.putExtra(PalletInformationActivity.INTENT_PALLET, result);
                startActivity(palletInformationIntent);
            }
        }
    }
    
    
}
