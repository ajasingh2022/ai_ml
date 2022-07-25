package com.capgemini.sesp.ast.android.ui.activity.register_meter_indication;

import android.app.Activity;

public class DISABLED_OldMeterIndicationActivity extends Activity{// implements OnClickListener{
/*	
	private RspButton fwdButton;
	private RspButton backButton;
	private Spinner spinner;
	private int currentMeterIndication;



	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_old_meter_indication);
		createWidgets();
	}
	
	public void createWidgets(){
		backButton = (RspButton) findViewById(R.id.back);
		backButton.setOnClickListener(this);
		fwdButton = (RspButton) findViewById(R.id.forward);
		fwdButton.setOnClickListener(this);
		fwdButton.setEnabled(false);
		ImageButton infoButton = (ImageButton) findViewById(R.id.infoButton);
		infoButton.setOnClickListener(this);
		ImageButton commentButton = (ImageButton) findViewById(R.id.commentButton);
		commentButton.setOnClickListener(this);	
		setSpinner();

	}
	private void setSpinner(){
		//if old meter indication info don't exist, show spinner
		int oldIndications = getOldMeterIndications();
		if (oldIndications == 0){
			spinner = (Spinner) findViewById(R.id.indication_spinner);
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			    	currentMeterIndication = getNumberFromSpinner();
			    	fwdButton.setEnabled(true);			        
			    }

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
		}
		else{
			nextIntent();
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == fwdButton.getId()){
			nextIntent();
		}
		else if(v.getId() == backButton.getId()){
			finish();
		}
		else if (v.getId()==R.id.commentButton)
			NavBar.startMeterChangeCommentIntent(this);
		else if (v.getId()==R.id.infoButton)
			NavBar.startMeterChangeInfoIntent(this);
	
	}
	private void nextIntent(){
		Intent nextIntent = new Intent(this, ActivityRegisterExternalAntennaRegisterInformation.class);
		this.startActivity(nextIntent);
	}
	private int getOldMeterIndications(){
		//TODO: get old information
		return 0;		
	}
	private void setOldMeterIndications(){
		//TODO: set new number of indications
		//x = currentMeterIndication
	}


	private int getNumberFromSpinner(){
		int number=-1;
		String textFieldString = spinner.getSelectedItem().toString();
		 try{
			 number = Integer.parseInt(textFieldString);
		 }
		catch (NumberFormatException e) {}
		return number;
	}

*/
}
