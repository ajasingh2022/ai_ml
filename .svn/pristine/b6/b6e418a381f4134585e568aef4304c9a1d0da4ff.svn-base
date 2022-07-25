package com.capgemini.sesp.ast.android.module.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;


import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.skvader.rsp.cft.common.to.cft.table.SystemParameterTO;

import java.util.List;


public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = new Object[0];
        if (data != null) {
            pdus = (Object[]) data.get("pdus");
        }

        if (pdus != null) {
            for (int i = 0; i < pdus.length; i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                List<SystemParameterTO> systemParameterTOs = ObjectCache.getAllTypes(SystemParameterTO.class);
                String twilioPhoneNO = null;
                for (SystemParameterTO systemParameterTO : systemParameterTOs) {
                    if (systemParameterTO.getId().equals(AndroidUtilsAstSep.CONSTANTS().SYSTEM_PARAMETER__TWILIO_PH_NO)) {
                        twilioPhoneNO = systemParameterTO.getValue();
                        break;
                    }
                }

                String sender = smsMessage.getDisplayOriginatingAddress();
              if (twilioPhoneNO != null) {
                    if (sender.equalsIgnoreCase(twilioPhoneNO) ){
                        String messageBody = smsMessage.getMessageBody();
                        mListener.messageReceived(messageBody);
                   }
                }

            }
        }
    }


    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
