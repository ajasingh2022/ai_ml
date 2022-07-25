package com.capgemini.sesp.ast.android.ui.layout;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.skvader.rsp.ast_sep.common.mobile.bean.HelpDocBean;
import java.util.List;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


/**
 * Created by rkumari2 on 22-07-2019.
 * This class is to display the Help Data in the Dialog.
 */

public class HelpDialog extends Dialog {

    Context mContext;
    protected transient Dialog dialog = null;
    Button closeBtn;
    HelpDocBean helpDocBean;
    ArrayMap tempHelpArrayMap = new ArrayMap();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_help);
        try {
            final TextView titleTV = findViewById(R.id.HelpTitle);
            final TextView helpTextTV = findViewById(R.id.HelpTextTv);

            closeBtn = (Button) findViewById(R.id.dialogHelpCloseButton);
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            helpDocBean = DatabaseHandler.createDatabaseHandler().getHelpIDDocument(tempHelpArrayMap.get("HELP_ID").toString());
            if (helpDocBean != null) {

                if (!helpDocBean.getPageID().equals("null"))
                    titleTV.setText(helpDocBean.getPageID());
                else
                    titleTV.setText(helpDocBean.getModuleID());

                helpTextTV.setText(helpDocBean.getHelpText());

            }

        } catch (Exception e) {
            writeLog(" HelpDialog  : helpDialog() ", e);
        }


    }

    public HelpDialog(Context context, int helpId) {
        super(context);
        tempHelpArrayMap.put("HELP_ID", helpId);
    }


}
