/**
 *
 */
package com.capgemini.sesp.ast.android.ui.fragments;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.BluetoothService;
import com.capgemini.sesp.ast.android.module.communication.BluetoothServiceCallbackListener;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.skvader.rsp.cft.common.util.Utils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

//import android.annotation.SuppressLint;

/**
 * This fragment is pre-loaded with standard UI and functionality
 * to search, display list of nearby bluetooth devices or initiate a connection.
 * <p>
 * <p>
 * The fragment can be used inside any specific work-order flow or in
 * general inside any other activity or can be extended by any project for customization
 * </p>
 *
 * @author Capgemini
 * @version 1.0
 * @since 23rd December, 2014
 */
//@SuppressLint("InflateParams")
public class StandardBluetoothHandlerFragment extends Fragment
        implements OnClickListener, OnTouchListener, BluetoothServiceCallbackListener {

    private transient Button btSearchBtn = null;
    private transient Button cancelBtn = null;
    private transient AtomicBoolean searching = new AtomicBoolean(false);
    static String TAG = StandardBluetoothHandlerFragment.class.getSimpleName();
    private transient BluetoothService parentActivity = null;
    private transient ListView btDeviceSearchLv = null;
    private transient TextView printerTxtView = null;

    private transient BluetoothArrayAdapter adapter = null;
    private final transient List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();

    private String btDeviceMacAddress = null;

    public StandardBluetoothHandlerFragment() {
        super();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bluetooth_search, null);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Initialize the components
        btSearchBtn = getView().findViewById(R.id.btSearchBtn);
        cancelBtn = getView().findViewById(R.id.btCancelBtn);
        // List view (initially hidden) to display nearby blue-tooth devices
        btDeviceSearchLv = getView().findViewById(R.id.btDeviceSearchLv);
        printerTxtView = getView().findViewById(R.id.printerTxtView);

        if (getActivity() instanceof BluetoothService) {
            parentActivity = (BluetoothService) getActivity();
        }

        if (btDeviceSearchLv != null) {
            this.adapter = new BluetoothArrayAdapter(this, deviceList);
            this.btDeviceSearchLv.setAdapter(adapter);
        }

        btDeviceMacAddress = SESPPreferenceUtil.getPreferenceString(ConstantsAstSep.SharedPreferenceKeys.BT_MAC_ADDRESS);
        if (btDeviceMacAddress != null && !"".equals(btDeviceMacAddress)) {
            printerTxtView.setText(btDeviceMacAddress);
        }

        setupListeners();
    }

    private void setupListeners() {
        if (btSearchBtn != null) {
            btSearchBtn.setOnClickListener(this);
        }

        if (cancelBtn != null) {
            cancelBtn.setOnClickListener(this);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Pressing state
                if (v != null && v.getId() == R.id.btSearchBtn) {
                    // Previous state was undecided or false
                    // Make it enabled
                    v.setBackgroundColor(getResources().getColor(R.color.colorDisable));
                    v.setEnabled(false);

                }

            } else if (v != null && event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick();
            }
        } catch (Exception e) {
            writeLog(TAG + " : onTouch() ", e);
        }
        return true;
    }

    private void searchStopBluetoothSearch(final boolean state) {
        try {
            if (parentActivity != null) {
                if (state) {
                    // Start searching
                    parentActivity.searchLocalBluetoothDevices(this);
                } else {
                    // Stop searching
                    parentActivity.stopLocalBluetoothDeviceSearch();
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " : searchStopBluetoothSearch() ", e);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.btSearchBtn) {
                Log.d("StandardBluetoothHandlerFragment", "Bt Search button is clicked");
                if (searching.get()) {
                    btSearchBtn.setText(getResources().getString(R.string.search_bt));
                    btSearchBtn.setTextColor(getResources().getColor(R.color.colorAccentDark));

                } else {
                    btSearchBtn.setText(getResources().getString(R.string.stop_bt_search));
                    btSearchBtn.setTextColor(getResources().getColor(R.color.colorDisable));
                    deviceList.clear();
                    adapter.setDeviceList(deviceList);
                    adapter.notifyDataSetChanged();
                }

                Log.d("StandardBluetoothHandlerFragment", "Will be searched =" + !searching.get());

                searchStopBluetoothSearch(!searching.get());
                searching.set(!searching.get());
            } else if (v.getId() == R.id.btCancelBtn) {
                // Terminate the search process
                searchStopBluetoothSearch(false);
                getActivity().finish();
            } else if (v.getId() == R.id.btSearchIndivRowLayout) {
                // User has selected a particular bt device
                String deviceMacAddress = ((ViewHolder) v.getTag()).btDeviceAddrTv.getText().toString();
                Log.d("StandardBluetoothHandlerFragment", "User clicked :: " + deviceMacAddress);
                printerTxtView.setText(printerTxtView.getText() + "-" + deviceMacAddress);
                SESPPreferenceUtil.savePreference(ConstantsAstSep.SharedPreferenceKeys.BT_MAC_ADDRESS, deviceMacAddress);
                getActivity().finish();
            }
        } catch (Exception e) {
            writeLog(TAG + " : onClick() ", e);
        }
    }

    /**
     * Asynchronous call back method that would be invoked
     * when a blue-tooth device is found
     *
     * @param {@link BluetoothDevice}
     */

    @Override
    public void onBluetoothDevicesFound(final BluetoothDevice bluetoothDevice) {
        try {
            if (bluetoothDevice != null) {
                Log.d("StandardBluetoothHandlerFragment-onBluetoothDevicesFound", "Bluetooth device found");
                Log.d("StandardBluetoothHandlerFragment-onBluetoothDevicesFound", "Device name=" + bluetoothDevice.getName());
                Log.d("StandardBluetoothHandlerFragment-onBluetoothDevicesFound", "Device address=" + bluetoothDevice.getAddress());

                if (!deviceList.contains(bluetoothDevice)) {
                    Log.d("onBluetoothDevicesFound", "Adding new bluetooth device in list");
                    deviceList.add(bluetoothDevice);

                    if (btDeviceSearchLv != null && adapter != null) {
                        adapter.setDeviceList(deviceList);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " : onBluetoothDevicesFound() ", e);
        }
    }


    /**
     * View holder for List view
     */
    private class ViewHolder {
        TextView btDeviceNameTv;
        TextView btDeviceAddrTv;
        View rowLayout;
    }

    /**
     * Custom array adapter for showing device name, address and
     * device icon icon based on supported bluetooth profiles
     */

    private class BluetoothArrayAdapter extends BaseAdapter implements OnTouchListener {

        private transient List<BluetoothDevice> deviceList = null;
        private transient SoftReference<StandardBluetoothHandlerFragment> fragmentRef = null;

        public BluetoothArrayAdapter(final StandardBluetoothHandlerFragment fragment, final List<BluetoothDevice> deviceList) {
            super();

            this.fragmentRef = new SoftReference<StandardBluetoothHandlerFragment>(fragment);
            this.deviceList = deviceList;
        }

        public void setDeviceList(final List<BluetoothDevice> deviceList) {
            this.deviceList = deviceList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            LayoutInflater inflater = null;
            try {
                final BluetoothDevice device = deviceList.get(position);

                if (fragmentRef != null && fragmentRef.get() != null) {
                    inflater = (LayoutInflater) fragmentRef.get().getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                }

                if (convertView == null) {
                    // inflate UI from XML file
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.bluetooth_search_layout_row, parent, false);
                    // get all UI view
                    holder.btDeviceNameTv = convertView.findViewById(R.id.btDeviceNameTv);
                    holder.btDeviceAddrTv = convertView.findViewById(R.id.btDeviceAddrTv);
                    holder.rowLayout = convertView.findViewById(R.id.btSearchIndivRowLayout);
                    // set tag for holder
                    convertView.setTag(holder);

                } else {
                    // if holder created, get tag from view
                    holder = (ViewHolder) convertView.getTag();
                }

                if (holder.rowLayout != null) {
                    // Touch will be handled by the adapter
                    holder.rowLayout.setOnTouchListener(this);
                    // But click will be handled by the caller fragment
                    holder.rowLayout.setOnClickListener(fragmentRef.get());
                }

                // Populate device name
                if (holder.btDeviceNameTv != null) {
                    {
                        if (!Utils.safeToString(device.getName()).equals(""))
                            holder.btDeviceNameTv.setText(device.getName());
                        else
                            holder.btDeviceNameTv.setText(getResources().getString(R.string.device_name) + " " + getResources().getString(R.string.unknown));
                    }

                }
                // Populate device address
                if (!Utils.safeToString(device.getAddress()).equals("")
                        && holder.btDeviceAddrTv != null) {
                    holder.btDeviceAddrTv.setText(device.getAddress());
                }
            } catch (Exception e) {
                writeLog(TAG + " : getView() ", e);
            }
            return convertView;
        }

        @Override
        public int getCount() {
            int size = 0;
            if (this.deviceList != null) {
                size = this.deviceList.size();
            }
            return size;
        }

        @Override
        public BluetoothDevice getItem(int position) {
            BluetoothDevice obj = null;
            try {

                if (this.deviceList != null && this.deviceList.size() > position) {
                    obj = this.deviceList.get(position);
                    Log.d("bluetoothfragment-adapter", "Object retrieved for position=" + position);
                }
            } catch (Exception e) {
                writeLog(TAG + " : getItem() ", e);
            }
            return obj;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            try {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (v.getId() == R.id.btSearchIndivRowLayout) {
                        v.setBackgroundColor(fragmentRef.get().getActivity().getResources().getColor(android.R.color.holo_blue_light));
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performClick();
                }
            } catch (Exception e) {
                writeLog(TAG + " : onTouch() ", e);
            }
            return true;


        }
    }

}
