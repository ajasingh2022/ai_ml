package com.capgemini.sesp.ast.android.module.driver;

import com.capgemini.sesp.ast.android.module.driver.iface.LabelPrinter;
import com.capgemini.sesp.ast.android.module.driver.impl.datamaxoneilprinter.DatamaxOneilMF4T;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by samdasgu on 1/6/2017.
 */
public class DriverManager {
    private LabelPrinter labelPrinter = null;

    // Label Printer

    public synchronized LabelPrinter getLabelPrinter(){
        return labelPrinter;
    }

    public synchronized boolean initLabelPrinter(String driverConstant) {
        try{
        if(driverConstant.equals(DriverConstants.LABEL_PRINTER__DATAMAX_ONEIL_MF4T)){
            labelPrinter = DatamaxOneilMF4T.getInstance();
        } else{
            labelPrinter = null;
        }
        } catch (Exception e) {
            writeLog( "DriverManager :initLabelPrinter() ", e);
        }
        return labelPrinter != null;
    }

}
