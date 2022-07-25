package com.capgemini.sesp.ast.android.module.util;

import android.content.Context;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.sesp.zera.utils.UsbDeviceListener;
import com.sesp.zera.utils.UsbSerialUtil;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by aditam on 6/1/2017.
 */
public class ZeraUtils {

    public static final String ZERA_MT_3000 = "ZERA_MT_3000";
    public static final String ZERA_MT_310 = "ZERA_MT_310";
    private String deviceType;
    private Context mContext;
    private final UsbSerialUtil usbSerialUtil;

    /**
     * Public constructor

     *
     * @param context
     */
    public ZeraUtils(Context context, UsbDeviceListener listener) {
        this.mContext = context;
        usbSerialUtil = UsbSerialUtil.getInstance(context, listener);
    }

    public static class ZeraResult{
        private Hashtable verificationResultMap;
        private String result;

        public Hashtable getVerificationResultMap() {
            return verificationResultMap;
        }

        public void setVerificationResultMap(Hashtable verificationResultMap) {
            this.verificationResultMap = verificationResultMap;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

    public void connectZeraDevice(){
        switch (deviceType) {
            case ZERA_MT_3000:
                usbSerialUtil.readDataFromMassStorageDevice();
                break;
            case ZERA_MT_310:
                usbSerialUtil.connectUsbSerialDevice();
                break;
            default:
                Toast.makeText(mContext, mContext.getString(R.string.device_type_not_set), Toast.LENGTH_SHORT).show();
        }
    }

    public void executeCommandZeraMT310(String command){
        String data = command;
        data = data + '\r';
        try {
            usbSerialUtil.writeToSerialDevice(data.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            writeLog("ZeraUtils + :executeCommandZeraMT310()", e);

        }
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public UsbSerialUtil getUsbSerialUtil() {
        return usbSerialUtil;
    }

}
