package com.capgemini.sesp.ast.android.module.util;

import com.skvader.rsp.cft.common.util.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by subhamit on 8/14/2017.
 */
public class StringUtil {
    public static String format(double value, int nodigits) {
        if (Double.isNaN(value))
            return "NaN";
        DecimalFormat myFormatter = new DecimalFormat("###.## ");
        myFormatter.setMaximumFractionDigits(nodigits);

        try {
            return myFormatter.format(value).replace(',','.');
        } catch (Exception e) {
            writeLog("StringUtil  : format()",e);
        }
        return "#";

    }
    public static Double convertStringToDouble(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        return Double.valueOf(str.replace(',', '.'));
    }

    public static String replaceDotToComma(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        return String.valueOf(str.replace('.', ','));
    }

    /**
     * this method splits a string with delimeter
     * @param original
     * @param separator
     * @return array of strings
     */
    public static String[] split(String original, String separator) {
        List<String> nodes = new ArrayList<String>();

        // Parse nodes into vector
        int index = original.indexOf(separator);
        while (index >= 0) {
            nodes.add(original.substring(0, index));
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }
        // Get the last node
        nodes.add(original);

        // Create splitted string array
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            int i =0;
            for(String item: nodes){
                result[i++] = item;
            }
        }
        return result;
    }


    /**
     * this method converts a string to long value
     * @param str
     * @return
     */
    public static Long convertStringToLong(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        return Long.valueOf(str.replace(',', '.'));

    }

    /**
     * this method checks if two objects are same
     * @param o1
     * @param o2
     * @return
     */
    public static boolean equalsWithNull(Object o1, Object o2) {
        if (o1 == null && o2 == null) return true;
        if (o1 == null || o2 == null) return false;
        return o1.equals(o2);
    }

/**
 * Return blank string
 */
    public static String checkNullString(String str) {
        return ((str == null) ? "" : str);
    }


    /**
     * Return empty or not
     */
    public static boolean isNotEmpty(String str) {
        return (str != null && (str.trim().length()>0));
    }

    public static boolean isValidDecimal(String str) {
        if(Utils.isEmpty(str)) return true;

        String regex = "[0-9,.]+";
        if(!str.matches(regex)) return false;
        if(str.charAt(str.length()-1)=='.' || str.charAt(str.length()-1)==',') return false;
        char ch[] = str.toCharArray();
        int comma =0;
        int dot = 0;
        for(int i=0;i<ch.length;i++) {
            if(ch[i] == ',') {
                comma++;
            } else if(ch[i] == '.') {
                dot++;
            }
        }
        return !((comma>0 && dot>0) || comma>1 || dot>1);
    }

    public static boolean isExistsCommaDot(String str){
        return str.contains(".") || str.contains(",");

    }
    /**
     * Validate a GSRN number String may optionally contain 8018,
     * the control number is not calculated and verified.
     *
     * @param correct
     * @param toValidate
     * @return
     */
    public static boolean validateGSRN(String correct, String toValidate){
        if( (correct != null && correct.length() > 0) && (toValidate != null && toValidate.length() > 0) ){
            return addPrefixIfMissing(correct, 18, "8018").equals(addPrefixIfMissing(toValidate, 18, "8018"));
        } else{
            return false;
        }
    }
    /**
     * method to remove the prefix code 8018 of unit identifier
     */

    public static String manipulateScannedCode(String gsrn){
        if(gsrn.length() == 22 && gsrn.startsWith("8018")) {
            gsrn = gsrn.substring("8018".length());
        }
        return gsrn;
    }


    /**
     * Add value if length of string matches.
     *
     * @param text number or text
     * @param lengthToMatch length to match
     * @param prefix prefix to add
     *
     */
    private static String addPrefixIfMissing(String text, int lengthToMatch, String prefix){
        if(text.length() == lengthToMatch){
            return prefix + text;
        } else {
            return text;
        }
    }

    public static String insertSeparator(String original, int interval, char padChar) {
        StringBuilder sb = new StringBuilder(original);

        for(int i = 0; i < original.length() / interval; i++) {
            sb.insert(((i + 1) * interval) + i, padChar);
        }

        return sb.toString();
    }
    public static String replaceCharAt(String s, int pos, char c) {
        return s.substring(0, pos) + c + s.substring(pos + 1);
    }

}
