package com.capgemini.sesp.ast.android.module.driver.impl.datamaxoneilprinter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import datamaxoneil.Monitor;
import datamaxoneil.connection.ConnectionBase;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by samdasgu on 12/26/2016.
 */
public class InsecureBluetoothConnection extends ConnectionBase {

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothServerSocket m_BtListener = null;
    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private BluetoothSocket m_BtClient = null;
    private DataInputStream m_StreamRead = null;
    private DataOutputStream m_StreamWrite = null;
    private String m_Address = "00:00:00:00:00:00";
    private static final String TAG = "InsecureBluetoothConnection";

    public String getRemoteEnd() {
        return this.m_BtClient == null ? "-none-" : this.m_Address;
    }

    public String getFriendlyName() {
        return this.device.getName();
    }


    protected InsecureBluetoothConnection(boolean isServer, String targetDevice) throws Exception {
        super(isServer);
        this.m_Address = targetDevice;
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        if (this.adapter == null) {
            throw new Exception("Bluetooth not supported on device.");
        } else if (!this.adapter.isEnabled()) {
            throw new Exception("Bluetooth on device not enabled.");
        } else {
            this.device = this.adapter.getRemoteDevice(this.m_Address);
        }
    }

    public static InsecureBluetoothConnection createClient(String targetDevice) throws Exception {
        return new InsecureBluetoothConnection(false, targetDevice);
    }

    public static InsecureBluetoothConnection createServer(int port) throws Exception {
        return new InsecureBluetoothConnection(true, "");
    }

    protected boolean getHasData() {
        boolean hasData = false;

        try {
            hasData = this.m_StreamRead.available() > 0;
        } catch (Exception var3) {
            writeLog(TAG + ":getHasData() ", var3);
        }

        return hasData;
    }

    protected boolean innerOpen() throws IOException {
        if (this.getIsServerMode()) {
            if (!this.m_Reconnecting) {
                this.m_BtClient = this.m_BtListener.accept();
            }

            this.m_IsOpen = true;
        } else {
            try {
                //this.m_BtClient = this.device.createInsecureRfcommSocketToServiceRecord(this.device.getUuids()[0].getUuid());
                this.m_BtClient = this.device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                this.m_BtClient.connect();
                this.m_StreamRead = new DataInputStream(this.m_BtClient.getInputStream());
                this.m_StreamWrite = new DataOutputStream(this.m_BtClient.getOutputStream());
            } catch (IOException var2) {
                writeLog(TAG + " : innerOpen() ", var2);
            }

            this.m_IsOpen = this.m_StreamRead != null && this.m_StreamWrite != null;
            this.m_IsActive = this.m_IsOpen;
        }

        return this.m_IsOpen;
    }

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
                            writeLog(TAG + ":close() ", var15);
                        }

                        try {
                            if (this.m_StreamWrite != null) {
                                this.m_StreamWrite.close();
                            }
                        } catch (Exception var14) {
                            writeLog(TAG + ":close() ", var14);
                        }

                        try {
                            if (this.m_BtClient != null) {
                                this.m_BtClient.close();
                            }
                        } catch (Exception var13) {
                            writeLog(TAG + ":close() ", var13);
                        }

                        try {
                            if (!this.m_Reconnecting && this.m_BtListener != null) {
                                this.m_BtListener.close();
                            }
                        } catch (Exception var12) {
                            writeLog(TAG + ":close() ", var12);
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
                    writeLog(TAG + ":close() ", var16);
                } finally {
                    Monitor.exit(this.m_LockGeneral);
                }
            } else {
                timeout -= 100;
                if (timeout > 0) {
                    try {
                        Thread.sleep(100L);
                    } catch (Exception var18) {
                        writeLog(TAG + ":close() ", var18);
                    }
                }
            }
        } while (timeout > 0);

    }

    protected int innerRead(byte[] buffer) throws IOException {
        return this.m_StreamRead.read(buffer);
    }

    protected void innerWrite(byte[] buffer) throws IOException {
        try{
        this.m_StreamWrite.write(buffer);
        this.m_StreamWrite.flush();
    } catch (Exception e) {
        writeLog(TAG + ":close() ", e);
    }
    }

    protected boolean innerListen() {
        boolean hasConnection = false;

        try {
            this.m_BtClient = this.m_BtListener.accept();
            this.m_StreamRead = new DataInputStream(this.m_BtClient.getInputStream());
            this.m_StreamWrite = new DataOutputStream(this.m_BtClient.getOutputStream());
            hasConnection = true;
        } catch (Exception e) {
            writeLog(TAG + ":close() ", e);
        }

        return hasConnection;
    }

    protected String configSummary() {
        String results = "";
        try {
            results = "TCP " + (this.getIsServerMode() ? "(Server)" : "(Client)");
        } catch (Exception e) {
            writeLog(TAG + ": configSummary() ", e);
        }
        return results;
    }

    protected String configCompact() {
        String results = "";
        try {
            results = this.m_Address;
        } catch (Exception e) {
            writeLog(TAG + ": configCompact() ", e);
        }
        return results;
    }

    protected String configDetail() {
        String results = "";
        try {
            if (this.getIsServerMode()) {
                results = results + "Bluetooth Server Mode\r\n";
            } else if (this.getIsClientMode()) {
                results = results + "Bluetooth Client Settings\r\nTarget Address: " + this.m_Address;
            }
        } catch (Exception e) {
            writeLog(TAG + ": configDetail() ", e);
        }

        return results;
    }
}
