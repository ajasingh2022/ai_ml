package com.capgemini.sesp.ast.android.module.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class ProgressDialogRunnable implements Runnable {
    private Context context = null;
    private ProgressDialog pd = null;
    
    private String toastMessage = null;
    private String newTitle = null;
    private String newMessage = null;
    private boolean close = false;
    
    
    public ProgressDialogRunnable(Context context, ProgressDialog pd, boolean close) {
        this.context = context;
        this.pd = pd;
        this.close  = close;
    }
    
    /**
     * Will close ProgressDialog and show a ToastMessage
     * 
     * @param context getApplicationContext()
     * @param pd ProgressDialog to interact with
     * @param toastMessage Message to show in a ToastMessage
     */
    public ProgressDialogRunnable(Context context, ProgressDialog pd, String toastMessage) {
        this(context, pd, true);
        this.toastMessage = toastMessage;
    }

    public ProgressDialogRunnable(Context context, ProgressDialog pd, String title, String message) {
        this(context, pd, false);
        this.newTitle = title;
        this.newMessage = message;
    }

    public void run() {
        if (toastMessage != null && toastMessage.length() > 0) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
        } else {
            if (newTitle != null)
                pd.setTitle(newTitle);
            if (newMessage != null)
                pd.setMessage(newMessage);
        }
        if (close)
            pd.dismiss();
    }
}