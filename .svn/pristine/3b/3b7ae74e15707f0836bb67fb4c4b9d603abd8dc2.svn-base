package com.capgemini.sesp.ast.android.ui.activity.register_additional_work;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;

import java.util.ArrayList;

public class RegisterAdditionalWorkActivity extends AppCompatActivity implements OnClickListener {
	
	private CheckBox externalAntennaCheckBox, clearance5MinutesCheckBox, clearance10MinutesCheckBox,
		fuseChangeCheckBox, cablesChangedCheckBox, newGroupMountedCheckBox;
	
	private ImageButton forwardButton, backButton;
	
	private ArrayList<String> registeredWork;
	private CheckBox[] checkBoxes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_additional_work);

		externalAntennaCheckBox = findViewById(R.id.external_antenna_installation_checkbox);
		clearance5MinutesCheckBox = findViewById(R.id.making_clearance_5_minutes_checkbox);
		clearance10MinutesCheckBox = findViewById(R.id.making_clearance_10_minutes_checkbox);
		fuseChangeCheckBox = findViewById(R.id.fuse_change_checkbox);
		cablesChangedCheckBox = findViewById(R.id.cables_changed_checkbox);
		newGroupMountedCheckBox = findViewById(R.id.new_group_mounted_checkbox);
		
		checkBoxes = new CheckBox[] {externalAntennaCheckBox, clearance5MinutesCheckBox, clearance10MinutesCheckBox, 
				fuseChangeCheckBox, cablesChangedCheckBox, newGroupMountedCheckBox};
		
		forwardButton = findViewById(R.id.forward);
		forwardButton.setOnClickListener(this);
		backButton = findViewById(R.id.back);
		backButton.setOnClickListener(this);
		
		registeredWork = new ArrayList<String>();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.register_additional_work, menu);
		return true;
	}
	
	/**
	 * TODO Save the additional work to the database.
	 */
	private void saveToDatabase() {
		
		registeredWork.clear();
		
		for(CheckBox check : checkBoxes) {
			if (check.isChecked()) {
				registeredWork.add(check.getText().toString());
			}
		}
		
		Toast.makeText(this, registeredWork.toString(), Toast.LENGTH_SHORT).show();
	}

	/**
	 * TODO Implement the navigation.
	 */
	@Override
	public void onClick(View v) {
		
		if (v.getId() == R.id.forward) {
			saveToDatabase();

			
		} else if (v.getId() == R.id.backButton) {
			
		}
	}
}
