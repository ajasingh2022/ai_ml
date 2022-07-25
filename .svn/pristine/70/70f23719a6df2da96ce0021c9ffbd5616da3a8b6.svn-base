package com.capgemini.sesp.ast.android.ui.activity.printer;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.driver.iface.LabelPrinter;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.module.util.ConnectBTPrinterThread;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.ui.activity.bluetooth.StandardBluetoothHandlerActivity;
import com.capgemini.sesp.ast.android.ui.activity.dashboard.DashboardActivity;
import com.capgemini.sesp.ast.android.ui.activity.dashboard.MainMenuItem;
import com.capgemini.sesp.ast.android.ui.adapter.MainMenuAdapter;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class PrintMenuActivity extends AppCompatActivity {

    private String btPrinterMacAddress;
    private TextView btPrinterAddressTv;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_menu);

        // Hiding the logo as requested
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
                final Dialog dialog = new HelpDialog(PrintMenuActivity.this,ConstantsAstSep.HelpDocumentConstant.PRINTER);
                dialog.show();
            }
        });
        // -- Customizing the action bar ends -----

        final View localeFlag = vw.findViewById(R.id.save_btn);
        if (localeFlag != null) {
            localeFlag.setVisibility(View.INVISIBLE);
        }

        getSupportActionBar().setCustomView(vw);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        TextView txtTitleBar = findViewById(R.id.title_text);
        txtTitleBar.setText(R.string.title_activity_print_menu);

        listView = findViewById(R.id.menuList);                                                  //Std UI modifications
        listView.setAdapter(new MainMenuAdapter(this, createItems()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                onSelectionOfListItem(listView, view, position, id);                                       //Calling the onSelectionOfListItem function indirectly

            }
        });

        btPrinterAddressTv = findViewById(R.id.btPrinterAddressTv);
        //setListAdapter(new MainMenuAdapter(this, createItems()));                                     //std ui modifications

        Button checkStatusB = findViewById(R.id.checkStatus);

    }

    @Override
    protected void onResume() {
        super.onResume();
        printMACAddress();

    }

    protected void printMACAddress() {
        try{
        if (SESPPreferenceUtil.getPreferenceString(ConstantsAstSep.SharedPreferenceKeys.BT_MAC_ADDRESS) != null) {
            setBtPrinterMacAddress(SESPPreferenceUtil.getPreferenceString(ConstantsAstSep.SharedPreferenceKeys.BT_MAC_ADDRESS));
            Log.d("PrintMenuActivity", "Bluetooth Mac address :: " + getBtPrinterMacAddress());
            btPrinterAddressTv.setText(getResources().getString(R.string.printer_address) + getBtPrinterMacAddress());
        } else {
            btPrinterAddressTv.setText(getResources().getString(R.string.printer_address));
        }
        } catch (Exception e) {
            writeLog("PrintMenuActivity : printMACAddress()", e);
        }

    }


    protected List<MainMenuItem> createItems() {
        List<MainMenuItem> items = null;
        try {
            int[] itemIconIds = new int[]{
                    R.drawable.ic_printer,
                    R.drawable.ic_printer
            };
            String[] itemNames = getResources().getStringArray(R.array.items_print_menu);

            items = new ArrayList<MainMenuItem>();

            for (int i = 0; i < itemNames.length; i++) {
                Integer iconid = null;
                if (i < itemIconIds.length) {
                    iconid = itemIconIds[i];
                }
                items.add(new MainMenuItem(itemNames[i], iconid));
            }
        } catch (Exception e) {
            writeLog("PrintMenuActivity : createItems()", e);
        }
        return items;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_print_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        try{
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent btPrinterIntent = new Intent(getApplicationContext(), StandardBluetoothHandlerActivity.class);
            startActivity(btPrinterIntent);
            return true;
        }
        } catch (Exception e) {
            writeLog("PrintMenuActivity : onOptionsItemSelected()", e);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onSelectionOfListItem(ListView l, View v, int position, long id) {
        try {
            if (AndroidUtilsAstSep.validateExistingBtPrinterSettings(this, getBtPrinterMacAddress())) {
                if (position == ConstantsAstSep.PrintMenuItems.SEARCH_ORDERS_TO_PRINT) {
                    long typeDataDownloadDiffInMinutes = Long.valueOf(ApplicationAstSep.getPropertyValue(ConstantsAstSep.PropertyConstants.KEY_TYPE_DATA_DOWNLOAD_TIME_DIFF, null));
                    if (SessionState.getInstance().isLoggedInOnline() && AndroidUtilsAstSep.isElapsedTime(SESPPreferenceUtil.getWoDownloadTime(), AndroidUtilsAstSep.getCurrentDate(), typeDataDownloadDiffInMinutes)) {
                        AndroidUtilsAstSep.downloadWorkordersIfNeeded(this, SearchOrdersToPrintActivity.class, 0);
                    } else {
                        Intent searchOrdersToPrint = new Intent(getApplicationContext(), SearchOrdersToPrintActivity.class);
                        startActivity(searchOrdersToPrint);
                    }
                } else if (position == ConstantsAstSep.PrintMenuItems.PRINT_SCANNED_ID) {
                    Intent printScannedId = new Intent(getApplicationContext(), PrintScannedIdActivity.class);
                    startActivity(printScannedId);
                }
            }
        } catch (Exception e) {
            writeLog("PrintMenuActivity : onSelectionOfListItem()", e);
        }
    }

    public void checkPrinterStatus(View v) {
        try {
            Log.d("PrintMenuActivity", "checkPrinterStatus is clicked");
            if (AndroidUtilsAstSep.validateExistingBtPrinterSettings(this, getBtPrinterMacAddress())) {
                AndroidUtilsAstSep.turnOnOffBluetooth(true);
                final LabelPrinter labelPrinter = ApplicationAstSep.getDriverManager().getLabelPrinter();

                new ConnectBTPrinterThread(this, labelPrinter, getBtPrinterMacAddress()).start();
            }
        } catch (Exception e) {
            writeLog("PrintMenuActivity : checkPrinterStatus()", e);
        }
    }

    public String getBtPrinterMacAddress() {
        return btPrinterMacAddress;
    }

    public void setBtPrinterMacAddress(String btPrinterMacAddress) {
        this.btPrinterMacAddress = btPrinterMacAddress;
    }

}
