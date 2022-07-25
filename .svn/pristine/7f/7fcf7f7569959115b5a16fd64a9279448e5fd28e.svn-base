package com.capgemini.sesp.ast.android.ui.activity.order;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.NetworkStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.LanguageHelper;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.ui.activity.receiver.NetworkStatusMonitorReceiver;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

@SuppressLint("InflateParams")
public class StatusActivity extends AppCompatActivity implements NetworkStatusCallbackListener {

    private ImageView networkStateIcon;
    private final String TAG = StatusActivity.class.getSimpleName();

    private EditText mUnsyncOrdersValue;
    private EditText mUnsyncPhotoValue;
    private EditText mFailedConnectionsValue;
    private EditText mIncompleteOrdersValue;

    /*
     * Local broadcast receiver used to
     * receive network connectivity information asynchronously
     */
    private transient BroadcastReceiver networkStatusReceiver = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);
        setCustomActionBar();
    }

    private void setCustomActionBar() {
        try{
        // Hiding the logo as requested
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(false);

        /*
		 * Setting up custom action bar view
		 */
        final ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final LayoutInflater layoutManager = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vw = layoutManager.inflate(R.layout.custom_action_bar_layout, null);

        // -- Customizing the action bar ends -----

        final View localeFlag = vw.findViewById(R.id.save_btn);
        localeFlag.setVisibility(View.INVISIBLE);
        networkStateIcon = vw.findViewById(R.id.network_state_iv);
        actionBar.setCustomView(vw);
        actionBar.setDisplayShowCustomEnabled(true);

        TextView txtTitleBar = findViewById(R.id.title_text);
        txtTitleBar.setText(getString(R.string.app_name));
        assignViews();
        }catch(Exception e) {
            writeLog(TAG+"  : setCustomActionBar() " ,e);
        }
    }

    /**
     * Refresh the views with latest information
     */
    private void updateViews() {
        mUnsyncOrdersValue.setText(String.valueOf(fetchNumberOfUnsycWorkorders()));
        mUnsyncPhotoValue.setText(String.valueOf(fetchNumberOfUnsycPhotos()));
        mFailedConnectionsValue.setText(String.valueOf(fetchNumberOfFailedConnections()));
        mIncompleteOrdersValue.setText(String.valueOf(fetchNumberOfIncompleteWorkorders()));
        List<Long> caseIdsToBeSynched = DatabaseHandler.createDatabaseHandler().getCaseIdsForSync();

        for (Long caseId : caseIdsToBeSynched) {
            Log.d("LogoutActivity", "Syncing work order, case Id : " + caseId);
            AndroidUtilsAstSep.suggestSaveWorkorder(caseId, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
        LanguageHelper.reloadIfLanguageChanged(this);

        final IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");

        this.networkStatusReceiver = new NetworkStatusMonitorReceiver(this);
        // Register broadcast receiver here
        registerReceiver(this.networkStatusReceiver, filter);
        updateViews();
        }catch(Exception e) {
            writeLog(TAG+"  : onResume() " ,e);
        }
    }

    private void assignViews() {
        mUnsyncOrdersValue = findViewById(R.id.unsync_orders_value);
        mUnsyncPhotoValue = findViewById(R.id.unsync_photo_value);
        mFailedConnectionsValue = findViewById(R.id.failed_connections_value);
        mIncompleteOrdersValue = findViewById(R.id.incomplete_orders_value);
    }


    public void updateButtonClicked(View view) {
        Log.d(TAG, "Fetching latest unsynchronized data");
        updateViews();
    }

    /**
     * Fetch the number of unsynchronized work orders
     * @return
     */
    private int fetchNumberOfUnsycWorkorders() {
        int noOfUnsycWorkorders = 0;
        try{
        noOfUnsycWorkorders = DatabaseHandler.createDatabaseHandler().getCaseIdsForSync().size();
        Log.d(TAG, "Number of unsynchronised work orders : " + noOfUnsycWorkorders);
        }catch(Exception e)
        {
            writeLog(TAG+"  : fetchNumberOfUnsycWorkorders() " ,e);
        }

        return noOfUnsycWorkorders;
    }

    /**
     * Fetch the number of unsynchronized photos
     * @return
     */
    private int fetchNumberOfUnsycPhotos() {
        int noOfUnsycPhotos = 0;
        try{
        noOfUnsycPhotos = DatabaseHandler.createDatabaseHandler().getNumberOfPhotosToBeSynched();
        Log.d(TAG, "Number of unsynchronised photos : " + noOfUnsycPhotos);
          }catch(Exception e) {
        writeLog(TAG+"  : fetchNumberOfUnsycPhotos() " ,e);
    }
        return noOfUnsycPhotos;
    }

    /**
     * Fetch the number of times the application has lost connection in a single session
     * @return
     */
    private int fetchNumberOfFailedConnections() {
        int noOfFailedConnections = 0;
        try{
        noOfFailedConnections = SessionState.getInstance().getFailedConnectionsCounter();
        Log.d(TAG, "Number of failed connections : " + noOfFailedConnections);
           }catch(Exception e) {
                     writeLog(TAG+"  : fetchNumberOfFailedConnections() " ,e);
              }
        return noOfFailedConnections;
    }

    /**
     * Fetch the number of Incomplete work orders.
     * @return
     */
    private int fetchNumberOfIncompleteWorkorders() {
        int noOfIncompleteWorkorders = 0;
        try{
        noOfIncompleteWorkorders = DatabaseHandler.createDatabaseHandler().getIncompleteWorkOrdersCount();
        Log.d(TAG, "Number of incomplete workorders : " + noOfIncompleteWorkorders);
           }catch(Exception e) {
               writeLog(TAG+"  : fetchNumberOfIncompleteWorkorders() " ,e);
          }
        return noOfIncompleteWorkorders;
    }


    @Override
    protected void onStop() {
        super.onStop();
        // De-register the broadcast receiver here
        if (this.networkStatusReceiver != null) {
            unregisterReceiver(this.networkStatusReceiver);
        }
    }

    @Override
    public void networkStatusChanged(boolean isConnected) {
        try{
        SessionState.getInstance().setLoggedInInOnlineMode(isConnected);
        networkStateIcon.setVisibility(View.VISIBLE);
        if (isConnected) {
            networkStateIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_cellular_4_bar_white));
        } else {
            networkStateIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_cellular_null_black));
        }
        }catch(Exception e) {
            writeLog(TAG+"  : networkStatusChanged() " ,e);
        }
    }
}
