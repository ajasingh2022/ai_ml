package com.capgemini.sesp.ast.android.module.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by tpatra on 13-Sep-17.
 */
public class FileFunctions {

    private static final String TAG =FileFunctions.class.getSimpleName();
    /*
	 * Directories
	 */
    private final static String APPLICATION_DIRECTORY = getStorageDirectory() + "ast_sep_pda" + File.separator;
    private final static String ERROR_STORAGE_DIRECTORY = APPLICATION_DIRECTORY + "error" + File.separator;

    private final static String DATA_DIRECTORY = APPLICATION_DIRECTORY + "data" + File.separator;
    private final static String PROPERTY_DIRECTORY = DATA_DIRECTORY + "property" + File.separator;
    private final static String GPS_DIRECTORY = APPLICATION_DIRECTORY + "gps" + File.separator;
    private final static String SALT_DIRECTORY = APPLICATION_DIRECTORY + "code" + File.separator;

    /*
     * Files
     */
    private static final String CONSTANTS_FILENAME = "constants";
    private static final String TYPE_FILENAME = "type";
    private static final String FLOWCONFIG_FILENAME = "flowconfig";
    private static final String USER_PASSWORD_FILENAME = "password";
    private static final String USER_WORKORDERS_DIRECTORY = "workorders";
    private static final String USER_WORKORDERS_STRUCTURE_DIRECTORY = "workorders_struct";
    private static final String USER_WORKORDERS_STRUCTURE_QUARANTINE_DIRECTORY = "workorders_struct_quarantine";
    private static final String USER_FUNCTIONS_FILENAME = "functions";

    private static final String GPS_PROP_FILENAME = "gps.prop";
    private static final String STOCK_PROP_FILENAME = "stock.prop";
    private static final String LOCALE_PROP_FILENAME = "locale.prop";
    private static final String BT_PRINTER_PROP_FILENAME = "btprinter.prop";
    private static final String PRINTER_TYPE_PROP_FILENAME = "printer.prop";
    private static final String IDENTIFIER_PROP_FILENAME = "identifier.prop";
    private static final String APN_PROP_FILENAME = "apn.prop";
    private static final String EMULATOR_VERSION_FILENAME = "emulator_version.prop";

    private static final String GPS_COORDINATE_FILENAME = "gps.txt";
    private static final String USER_SALT_FILENAME = "salt";
    private static final String GENERIC_USER_FILE = "genericuser";
    private static final String PASS_SALT_FILENAME = "passsalt";
    private final static String STORAGE_DIRECTORY = "Test";
            /*ZeraUtils.isHandheldMode() ?
            ZeraUtils.getProperty("storagedir") :
            (System.getProperty("user.home") != null) ?
                    (System.getProperty("user.home") + File.separator + System.getProperty("sep_business") + File.separator) :
                    (System.getProperty("sep_business") + File.separator); */

    public static String getStorageDirectory() {
        return STORAGE_DIRECTORY;
    }

    protected static String loadConfig(String code, String fileName) {
        String value = null;
        String propFile = getPropertyDirPath(fileName);
        Properties props = new Properties();
        InputStream fileInput = null;
        try {
            fileInput = new FileInputStream(propFile);
            props.load(fileInput);
            value = props.getProperty(code);
      /*  } catch (FileNotFoundException e) {
            writeLog(TAG + " :loadConfig()" , e);*/
        } catch (Exception e) {
            writeLog(TAG + " :loadConfig()" , e);
        } finally {
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (Exception e) {
                    writeLog(TAG + " :loadConfig()" , e);
                }
            }
        }
        if (value == null)
            return "";

        return value;
    }

    private static String getPropertyDirPath(String filename) {
        return PROPERTY_DIRECTORY + ( (filename!=null && filename.length()>0) ? filename : "");
    }

    protected static void saveConfig( String code, String value, String fileName) {
        Hashtable h = new Hashtable();
        h.put(code, value);
        saveConfig(h, fileName);
    }

    /**
     * Save several properties in one propfile.
     * @param h Hashtable containing properties. <String,String>
     * @param fileName
     */
    protected static void saveConfig(Hashtable h, String fileName) {
        String propFile = getPropertyDirPath(fileName);
        File f = new File(propFile);
        if (f.exists())
            f.delete();
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(f));
            Enumeration e = h.keys();
            while (e.hasMoreElements()) {
                String key = (String ) e.nextElement();
                String value = (String) h.get(key);
                pw.println(key + " = " + value);
            }
        } catch (IOException e) {
        } finally {
            if (pw != null)
                pw.close();
        }

    }
}
