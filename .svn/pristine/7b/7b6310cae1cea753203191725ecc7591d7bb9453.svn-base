package com.capgemini.sesp.ast.android.ui.activity.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.ui.service.AndroidStatisticsSyncService;
import com.capgemini.sesp.ast.android.ui.service.AsyncResponse;
import com.capgemini.sesp.ast.android.ui.service.CheckServerConnectionService;
import com.skvader.rsp.cft.common.util.AndroidAuthenticationTO;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class LogoutActivity extends AppCompatActivity {
	
	private Handler handler;
	private int failedConnections = 0;
	private int noOfOrdersToBeSynced = 0;
	
	private final int LOADING = 1;
	private final int LOADING_STOPPED = 0;
	private static final int ATTEMPTS_MAX = 3;
	private static final String TAG = LogoutActivity.class.getSimpleName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        
        final EditText logoutStatus = findViewById(R.id.activityLogoutStatusRspEditText);
        /*logoutStatus.setType(RspEditText.Type.OUTPUT);*/
        logoutStatus.setText(R.string.logout_started);

		Button logoutButton = findViewById(R.id.activityLogoutTryAgainButton);
        /*logoutButton.setType(Type.NAVIGATION);*/
		SESPPreferenceUtil.savePreference(ConstantsAstSep.SharedPreferenceKeys.USER_LOGGED_OUT,true);

        if (SessionState.getInstance().isLoggedInOnline()) {
			handler = new Handler() {
		      	  @Override
		      	  public void handleMessage(Message msg) {
		      		if (msg.arg1 == LOADING) {
		      			ProgressBar progressSpinner = findViewById(R.id.activityLogoutProgressbar);
		        		progressSpinner.setVisibility(View.VISIBLE);

						Button logoutButton = findViewById(R.id.activityLogoutTryAgainButton);
		        		logoutButton.setEnabled(false);
		      		} else if (msg.arg1 == LOADING_STOPPED) {
		      			ProgressBar progressSpinner = findViewById(R.id.activityLogoutProgressbar);
		      			progressSpinner.setVisibility(View.GONE);
						Toast.makeText(LogoutActivity.this, R.string.force_logout_message, Toast.LENGTH_LONG).show();
						Button logoutButton = findViewById(R.id.activityLogoutTryAgainButton);
		        		logoutButton.setEnabled(true);
		      		}
		      		
		      		  failedConnections = msg.arg2;
		      		  TextView failedConnectionsTextView = findViewById(R.id.activityLogoutFailedConnectionsTextView);
					  TextView activityLogoutNumbOrdersToSyncTextView = findViewById(R.id.activityLogoutNumbOrdersToSyncTextView);

					  failedConnectionsTextView.setText(String.valueOf(failedConnections));
					  activityLogoutNumbOrdersToSyncTextView.setText(String.valueOf(noOfOrdersToBeSynced));
	    			
	    			  String statusMessage = (String) msg.obj;
	    			  if (statusMessage != null)
	    				 logoutStatus.setText(logoutStatus.getText().toString() + "\n" + statusMessage);
		      	  }
			};
	        new LogoutThread(handler, failedConnections).start();
        } else {
        	Toast.makeText(this, R.string.logout_offline_message, Toast.LENGTH_LONG).show();
        	logout();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void logoutButtonClicked(View view) {
		Toast.makeText(this, R.string.logout_offline_message, Toast.LENGTH_LONG).show();
		logout();
    }
    
    private void logout() {
    	try{
    	SessionState.getInstance().clearSession();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
		}catch(Exception e)
		{
			writeLog(TAG+"  : logout() " ,e);
		}
    }

	private class LogoutThread extends Thread {

		private int failedConnections;
    	private Handler tHandler;
		private boolean syncStatus = false;
    	
    	public LogoutThread(Handler handler, int failedConnections) {
    		tHandler = handler;
			this.failedConnections = failedConnections;
    	}

		@Override
		public void run() {
    		try{
			Log.d("LogoutActivity","LogoutThread running start");
			int attempts = 1;
			//do {
			Log.d("LogoutActivity","Attempt : "+attempts);
			syncOrders();

			final AndroidAuthenticationTO androidAuthenticationTO = new AndroidAuthenticationTO();
			//Set Android device IP address.
			androidAuthenticationTO.setIpAddress(AndroidUtilsAstSep.getLocalIpAddress());
			androidAuthenticationTO.setOperatingSystem("Android:" + Build.VERSION.RELEASE);
			// Set Device Id
			androidAuthenticationTO.setDeviceSerialNumber(Build.SERIAL != Build.UNKNOWN ? Build.SERIAL : Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
			new Thread(new Runnable(){

				@Override
				public void run() {
					try {
						AndroidUtilsAstSep.getDelegate().logout(androidAuthenticationTO);
					} catch (Exception e) {
						writeLog(TAG + ":run()   :Logout functionality could not be updated in User Events: " , e);
					}
				}
			}).start();


			Log.d(TAG, "Android Statistics sync Service");
			startService(new Intent(getBaseContext(),AndroidStatisticsSyncService.class));

			//Wait for CheckServerConnectionService to finish execution
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				writeLog(TAG +" + run()" ,e) ;
			}
			//if(syncStatus) break;
			//} while (attempts++ < ATTEMPTS_MAX);
			if (syncStatus) {
				logout();
			} else {
				Message msg = new Message();
				msg.arg1 = LOADING_STOPPED;
				msg.arg2 = failedConnections;
				tHandler.sendMessage(msg);
			}
			Log.d("LogoutActivity","LogoutThread running end");
			} catch (Exception e) {
				writeLog("LogoutActivity  : run() ", e);
			}
		}
		private void syncOrders() {
    		try{
			// Check connection
			new CheckServerConnectionService(new AsyncResponse<Boolean>() {
				@Override
				public void processFinish(Boolean output) {
					Log.d("LogoutActivity","syncOrders() START");
					if(output){
						//Synchronizing work orders with server
						List<Long> caseIdsToBeSynched = DatabaseHandler.createDatabaseHandler().getCaseIdsForSync();
						noOfOrdersToBeSynced = caseIdsToBeSynched.size();
						for(Long caseId : caseIdsToBeSynched){
							Log.d("LogoutActivity", "Syncing work order, case Id : "+caseId);
							AndroidUtilsAstSep.suggestSaveWorkorder(caseId, null);
						}
						if(DatabaseHandler.createDatabaseHandler().getCaseIdsForSync().size() == 0){
							syncStatus = true;
							Log.d("LogoutActivity","All work orders are synched with server");
						}
					}

					if (!syncStatus) {
						Message msg = new Message();
						msg.arg1 = LOADING;
						msg.arg2 = failedConnections;
						msg.obj = getString(R.string.logout_attempt_x_failed) + failedConnections;
						tHandler.sendMessage(msg);
					}
					Log.d("LogoutActivity","syncOrders() END");
				}
			}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} catch (Exception e) {
				writeLog("LogoutActivity  : syncOrders() ", e);
			}
		}
	}
}
