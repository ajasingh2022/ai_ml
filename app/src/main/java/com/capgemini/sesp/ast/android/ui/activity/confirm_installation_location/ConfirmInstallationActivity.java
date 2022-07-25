package com.capgemini.sesp.ast.android.ui.activity.confirm_installation_location;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;

public class ConfirmInstallationActivity extends AppCompatActivity {

    private String arraySpinner1[];
    private String arraySpinner2[];
    private ImageButton forwardButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_confirm_installation_location);

        arraySpinner1=new String[9];
        arraySpinner1[0]="Tower station";
        arraySpinner1[1]="Brick made ground station 10 kV";
        arraySpinner1[2]="Sub station";
        arraySpinner1[3]="Ground station";
        arraySpinner1[4]="Pole station";
        arraySpinner1[5]="1-Pole station";
        arraySpinner1[6]="2-Pole station";
        arraySpinner1[7]="Property net station";
        arraySpinner1[8]="Wooden ground station 10 kV";

        arraySpinner2=new String[4];
        arraySpinner2[0]="Metering point";
        arraySpinner2[1]="Pole transformer";
        arraySpinner2[2]="Transformer in building";
        arraySpinner2[3]="Ground station transformer";

        final Spinner s = findViewById(R.id.remarkSpinner);
        final Spinner ss = findViewById(R.id.placementSpinner);


        forwardButton = findViewById(R.id.forward);

        final ArrayAdapter adapterone = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, arraySpinner1);
        s.setAdapter(adapterone);

        final ArrayAdapter adaptertwo = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, arraySpinner2);
        ss.setAdapter(adaptertwo);

        final ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(backButtonClicked);
    }

    final OnClickListener backButtonClicked = new OnClickListener() {
        @Override
        public void onClick(final View v) {
            ConfirmInstallationActivity.this.finish();
        }
    };

}
