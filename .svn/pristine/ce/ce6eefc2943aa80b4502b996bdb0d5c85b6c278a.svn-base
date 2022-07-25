package com.capgemini.sesp.ast.android.ui.activity.receiver;

import android.app.Dialog;
import android.util.ArrayMap;

import java.util.Map;

/**
 * Created by samdasgu on 2/13/2017.
 */
public class DialogFactory {

    private transient static final DialogFactory INSTANCE = new DialogFactory();
    //Map to hold  DialogName-DialogClass entries

    private Map<String,Class<? extends Dialog>> dialogClasses = new ArrayMap<String, Class<? extends Dialog>>();

    /**
     * Return pre initialized instance
     * @return - DialogFactory
     */
    public static DialogFactory getInstance() {
        return INSTANCE;
    }

    public  void addDialog(String dialogKey,Class<? extends Dialog> dialogClass){
        dialogClasses.put(dialogKey,dialogClass);
    }
    public  Class<? extends Dialog> getDialogClass(String dialogKey){
        return dialogClasses.get(dialogKey);
    }

}
