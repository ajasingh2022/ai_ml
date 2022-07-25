package com.capgemini.sesp.ast.android.ui.activity.material_logistics;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.capgemini.sesp.ast.android.ui.fragments.MLSettingsFragment;

public class SESPMaterialLogisticsSettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MLSettingsFragment()).commit();

    }
}
