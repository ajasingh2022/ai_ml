package com.capgemini.sesp.ast.android.module.util;

import android.content.Context;
import android.util.Log;

import com.sesp.zera.utils.UsbDeviceListener;
import com.sesp.zera.utils.UsbSerialUtil;

import java.io.UnsupportedEncodingException;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by aditam on 6/1/2017.
 */
public class E600Utils {

    private final UsbSerialUtil usbSerialUtil;
    private Context context;

    public UsbSerialUtil getUsbSerialUtil() {
        return usbSerialUtil;
    }


    /**
     * Public constructor
     *
     * @param context
     */
    public E600Utils(Context context,  UsbDeviceListener listener) {
        this.context = context;
        usbSerialUtil = UsbSerialUtil.getInstance(context, listener);
    }

    /**
     * Connect Serial device e.g. E600 Meter
     */
    public void connectE600Device(){
        usbSerialUtil.connectUsbSerialDeviceE600Meter();
    }

    /**
     * Write data to serial buffer
     * @param data
     */
    public void executeCommand(String data){
        Log.d("E600Utils", "command send to meter in string" + data);
        try {
            Log.d("E600Utils", "command send to meter in bytes"+ data.getBytes("ASCII").toString());

            usbSerialUtil.writeToSerialDevice(data.getBytes("ASCII"));
        } catch (final Exception ex){
            writeLog( "E600Utils  :FolderCreate()", ex);
        }
    }

    /**
     * Read data from Serial Buffer
     */
    public void readData() {
        if (usbSerialUtil != null) {
            usbSerialUtil.readData();
        }
    }

    /**
     * Set baud rate
     * @param baudRate
     */
    public void setBaudRate(final int baudRate) {
        if (usbSerialUtil != null) {
            usbSerialUtil.setBaudRate(baudRate);
        }
    }
}