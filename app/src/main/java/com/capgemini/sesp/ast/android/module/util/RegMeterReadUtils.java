package com.capgemini.sesp.ast.android.module.util;

import android.util.ArrayMap;

import com.skvader.rsp.ast_sep.common.to.ast.table.UnitModelMeterTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.Map;

/**
 * Utility class for registering meter reading values
 * Created by samdasgu on 5/8/2017.
 */
public class RegMeterReadUtils {

    /**
     * Get Minimum number digits after decimal
     * @param unitModelMeterMTO
     * @return
     */
    public static long getMinimumPostDecCount(UnitModelMeterTO unitModelMeterMTO){
        if(unitModelMeterMTO != null && unitModelMeterMTO.getNumDecimals()!=null){
            return unitModelMeterMTO.getNumDecimals();
        } else {
            return 1;
        }
    }

    /**
     * Get number of digits after decimal
     * @param unitModelMeterMTO
     * @return
     */
    public static long getPostDecCount(UnitModelMeterTO unitModelMeterMTO){
        if(unitModelMeterMTO != null && unitModelMeterMTO.getNumDecimals()!=null){
            return unitModelMeterMTO.getNumDecimals();
        } else {
            return 10;
        }
    }

    /**
     * Get Minimum number of digits before decimal
     * @param unitModelMeterMTO
     * @return
     */
    public static long getMinimumPreDecCount(UnitModelMeterTO unitModelMeterMTO){
        if(unitModelMeterMTO != null && unitModelMeterMTO.getNumDigits()!=null){
            return unitModelMeterMTO.getNumDigits();
        } else {
            return 1;
        }
    }

    /**
     * Get number of digits before decimal
     * @param unitModelMeterMTO
     * @return
     */
    public static long getPreDecCount(UnitModelMeterTO unitModelMeterMTO){
        if(unitModelMeterMTO != null && unitModelMeterMTO.getNumDigits()!=null){
            return unitModelMeterMTO.getNumDigits();
        } else {
            return 20;
        }
    }


    /**
     * Get Register Reading as String
     * @param readingMap
     * @return
     */
    public static String getRegisterReadingAsString(Map<String, String> readingMap) {
        String readValue = null;
        if(Utils.isNotEmpty(readingMap)) {
            StringBuilder readBuilder = new StringBuilder();
            for(String key : readingMap.keySet()) {
                String dReadValue = readingMap.get(key);
                if(dReadValue != null) {
                    readBuilder.append(key);
                    readBuilder.append(":");
                    readBuilder.append(dReadValue.toString());
                    readBuilder.append(";");
                }
            }
            readValue = readBuilder.toString();
        }
        return readValue;
    }

    /**
     * Get Register Reading as a Map
     * @param readValues
     * @return
     */
    public static Map<String, String> getRegisterReadingAsMap(String readValues) {
        Map<String,String> readingMap =  new ArrayMap<String, String>();
        if (Utils.isNotEmpty(readValues)) {
            String[] rValues = readValues.split(";");
            for (String tempString : rValues) {
                String[] meterRegRead = tempString.split(":");
                readingMap.put(meterRegRead[0], meterRegRead[1]);
            }
        }
        return readingMap;
    }
}
