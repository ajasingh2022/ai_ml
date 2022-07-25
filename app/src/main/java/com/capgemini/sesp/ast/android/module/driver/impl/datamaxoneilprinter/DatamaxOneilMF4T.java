package com.capgemini.sesp.ast.android.module.driver.impl.datamaxoneilprinter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.capgemini.sesp.ast.android.module.driver.iface.LabelPrinter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import datamaxoneil.Monitor;
import datamaxoneil.connection.ConnectionBase;
import datamaxoneil.printer.DocumentEZ;
import datamaxoneil.printer.ParametersEZ;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by samdasgu on 12/28/2016.
 */
public class DatamaxOneilMF4T extends ConnectionBase implements LabelPrinter {

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothServerSocket m_BtListener = null;
    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private BluetoothSocket m_BtClient = null;
    private DataInputStream m_StreamRead = null;
    private DataOutputStream m_StreamWrite = null;
    private String m_printerAddress = "00:00:00:00:00:00";
    private static DatamaxOneilMF4T m_Printer = new DatamaxOneilMF4T(false);
    private static final String TAG = "DatamaxOneilMF4T";

    protected DatamaxOneilMF4T(boolean isServer) {
        super(isServer);
    }

    public static DatamaxOneilMF4T getInstance() {
        return m_Printer;
    }

    @Override
    public String checkDeviceStatus(String m_printerAddress) {
        this.m_printerAddress = m_printerAddress;
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        if (this.adapter == null) {
            return "Bluetooth not supported on device.";
        } else if (!this.adapter.isEnabled()) {
            return "Bluetooth on device not enabled.";
        } else {
            this.device = this.adapter.getRemoteDevice(this.m_printerAddress);
            return "Bluetooth device obtained";
        }
    }

    @Override
    public boolean openConnection() throws Exception {
        return getIsOpen() || open();
    }

    @Override
    public void closeConnection() {
        close();
    }

    @Override
    public void printBarCode(String header, String barcode, String footer) throws Exception {
        DocumentEZ docEZ = new DocumentEZ("MF204");
        ParametersEZ paramEZ = new ParametersEZ();
        byte[] printData = new byte[]{0};

        paramEZ.setHorizontalMultiplier(1);
        paramEZ.setVerticalMultiplier(2);

        //barcode EAN 128
        docEZ.writeText(header, 2430, 1);
        docEZ.writeBarCode("EN128", barcode, 2460, 1, paramEZ);
        docEZ.writeText(footer, 2500, 1);
        printData = docEZ.getDocumentData();
        write(printData);
        Thread.sleep(2000);
    }

    @Override
    protected boolean getHasData() {
        boolean hasData = false;

        try {
            hasData = this.m_StreamRead.available() > 0;
        } catch (Exception var3) {
            writeLog(TAG + ": getHasData() ", var3);

        }
        return hasData;
    }

    @Override
    protected boolean innerOpen() {
        try {
            this.m_BtClient = this.device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            this.m_BtClient.connect();
            this.m_StreamRead = new DataInputStream(this.m_BtClient.getInputStream());
            this.m_StreamWrite = new DataOutputStream(this.m_BtClient.getOutputStream());

            this.m_IsOpen = this.m_StreamRead != null && this.m_StreamWrite != null;
            this.m_IsActive = this.m_IsOpen;
        } catch (Exception e) {
            writeLog(TAG + " : innerOpen()", e);
        }
        return this.m_IsOpen;
    }

    @Override
    protected void close(boolean isInternalCall) {
        int timeout = isInternalCall ? 0 : 2147483647;

        do {
            if (Monitor.tryEnter(this.m_LockGeneral, 0L)) {
                try {
                    timeout = 0;
                    if (this.getIsOpen()) {
                        this.closeBase(isInternalCall);

                        try {
                            if (this.m_StreamRead != null) {
                                this.m_StreamRead.close();
                            }
                        } catch (Exception var15) {
                            writeLog(TAG + ": close() ", var15);
                        }

                        try {
                            if (this.m_StreamWrite != null) {
                                this.m_StreamWrite.close();
                            }
                        } catch (Exception var14) {
                            writeLog(TAG + ": close() ", var14);
                        }

                        try {
                            if (this.m_BtClient != null) {
                                this.m_BtClient.close();
                            }
                        } catch (Exception var13) {
                            writeLog(TAG + ": close() ", var13);
                        }

                        try {
                            if (!this.m_Reconnecting && this.m_BtListener != null) {
                                this.m_BtListener.close();
                            }
                        } catch (Exception var12) {
                            writeLog(TAG + ": close() ", var12);
                        }

                        this.m_StreamRead = null;
                        this.m_StreamWrite = null;
                        this.m_BtClient = null;
                        if (!this.m_Reconnecting) {
                            this.m_BtListener = null;
                        }

                        this.m_IsOpen = false;
                        this.m_IsActive = false;
                    }
                } catch (Exception var16) {
                    writeLog(TAG + ": close() ", var16);
                } finally {
                    Monitor.exit(this.m_LockGeneral);
                }
            } else {
                timeout -= 100;
                if (timeout > 0) {
                    try {
                        Thread.sleep(100L);
                    } catch (Exception var18) {
                        writeLog(TAG + ": close() ", var18);
                    }
                }
            }
        } while (timeout > 0);
    }

    @Override
    protected int innerRead(byte[] buffer) throws IOException {
        return this.m_StreamRead.read(buffer);
    }

    @Override
    protected void innerWrite(byte[] bytes) throws IOException {
        try{
        this.m_StreamWrite.write(bytes);
        this.m_StreamWrite.flush();
    } catch (Exception var3) {
        writeLog(TAG + ": innerListen() ", var3);
    }
    }

    @Override
    protected boolean innerListen() {
        boolean hasConnection = false;

        try {
            this.m_BtClient = this.m_BtListener.accept();
            this.m_StreamRead = new DataInputStream(this.m_BtClient.getInputStream());
            this.m_StreamWrite = new DataOutputStream(this.m_BtClient.getOutputStream());
            hasConnection = true;
        } catch (Exception var3) {
            writeLog(TAG + ": innerListen() ", var3);
        }

        return hasConnection;
    }

    @Override
    protected String configSummary() {
        String results = "";
        try {
            results = "TCP " + (this.getIsServerMode() ? "(Server)" : "(Client)");
        } catch (Exception var3) {
            writeLog(TAG + ": configSummary() ", var3);
        }
        return results;
    }

    @Override
    protected String configCompact() {
        String results = "";
        try {
            results = this.m_printerAddress;
        } catch (Exception var3) {
            writeLog(TAG + ": configCompact() ", var3);
        }
        return results;
    }

    @Override
    protected String configDetail() {
        String results = "";
        try {
            if (this.getIsServerMode()) {
                results = results + "Bluetooth Server Mode\r\n";
            } else if (this.getIsClientMode()) {
                results = results + "Bluetooth Client Settings\r\nTarget Address: " + this.m_printerAddress;
            }
        } catch (Exception e) {
            writeLog(TAG + ": configDetail() ", e);
        }
        return results;
    }
}


