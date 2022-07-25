package com.capgemini.sesp.ast.android.ui.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 7/20/2018.
 */

public class AdvancedSearchActivity extends AppCompatActivity {

    private String installationCode;
    private String model;
    private String address;
    private String deadline;
    EditText instCodeEt;
    EditText modelEt;
    EditText addressEt;
    EditText deadlineEt;
    OrderListActivity orderObj;
    List<WorkorderLiteTO> workorders;
    List<String> woInfo;
    ArrayList<String> searchCriteria;
    List<WorkorderLiteTO> filteredWo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_advanced_search);
            getSupportActionBar().setTitle(getString(R.string.title_order_list));
            instCodeEt = findViewById(R.id.instEditText);
            modelEt = findViewById(R.id.modelEditText);
            addressEt = findViewById(R.id.addressEditText);
            deadlineEt = findViewById(R.id.deadlineEditText);
            TextView titletext = findViewById(R.id.title_text);
            titletext.setText(getString(R.string.adv_search));
        } catch (Exception e) {
            writeLog("AdvancedSearchActivity : onCreate()", e);
        }
    }

    public void advSearchButtonClicked(View view) {
        try {
            searchCriteria = new ArrayList<>();
            searchCriteria.add(instCodeEt.getText().toString());
            searchCriteria.add(modelEt.getText().toString());
            searchCriteria.add(addressEt.getText().toString());
            searchCriteria.add(deadlineEt.getText().toString());
            Intent resultIntent = getIntent();
            resultIntent.putStringArrayListExtra("SearchCriteriaList", searchCriteria);
            setResult(RESULT_OK, resultIntent);
            finish();
        } catch (Exception e) {
            writeLog("AdvancedSearchActivity : advSearchButtonClicked()", e);
        }
    }

    public void backButtonClicked(View view) {
        try {
            Intent resultIntent = getIntent();
            setResult(RESULT_CANCELED, resultIntent);
            finish();
        } catch (Exception e) {
            writeLog("AdvancedSearchActivity : backButtonClicked()", e);
        }
    }
}
