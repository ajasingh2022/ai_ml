package com.capgemini.sesp.ast.android.module.util.comparators;

import com.skvader.rsp.cft.common.to.custom.base.TypeTO;

import java.util.Comparator;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by devldas on 10/30/2017.
 */
public class NumberFormatTypeDataComparator implements Comparator {

    private String TAG = NumberFormatTypeDataComparator.class.getSimpleName();

    @Override
    public int compare(Object object1, Object object2) {
        if (object1 == null || object2 == null)
            return -1;

        if(object1 instanceof TypeTO && object2 instanceof TypeTO){
            TypeTO obj1 = (TypeTO) object1;
            TypeTO obj2 = (TypeTO) object2;
            if(obj1.getName() != null && obj2.getName() != null){
                try{
                return Double.valueOf(obj1.getName()).compareTo(Double.valueOf(obj2.getName()));
                }
                catch(Exception e){
                    writeLog(TAG + ": compare() : Number Comparator,The values of" +
                            " the type name  are not numbers ", e);
                    return -1;
                }
            }
        }

        //keep existing order
        return -1;
    }
}
