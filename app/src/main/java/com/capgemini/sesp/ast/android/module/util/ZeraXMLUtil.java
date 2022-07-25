package com.capgemini.sesp.ast.android.module.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Hashtable;
import java.util.StringTokenizer;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class ZeraXMLUtil {
    static String TAG = ZeraXMLUtil.class.getSimpleName();
    private static final String DEFAULT_DECIMAL_VALUE = "0.000000";

    private static final String HYPHEN = "--";

    private static final String XML_SECTION__RESULT = "Result";

    private static final String XML_PARAM__RESULT__DATE = "Date";
    private static final String XML_PARAM__RESULT__TIME = "Time";
    private static final String XML_PARAM__RESULT__FUNCTION = "Function";
    private static final String XML_PARAM__RESULT__DATATYPE = "Datatype";
    private static final String XML_PARAM__RESULT__U_RANGE = "U-Range";
    private static final String XML_PARAM__RESULT__I_RANGE = "I-Range";
    private static final String XML_PARAM__RESULT__CZ = "Cz";
    private static final String XML_PARAM__RESULT__M_MODE = "M-Mode";

    private static final String XML_PARAM__RESULT__P1 = "P1";
    private static final String XML_PARAM__RESULT__P2 = "P2";
    private static final String XML_PARAM__RESULT__P3 = "P3";

    private static final String XML_PARAM__RESULT__Q1 = "Q1";
    private static final String XML_PARAM__RESULT__Q2 = "Q2";
    private static final String XML_PARAM__RESULT__Q3 = "Q3";

    private static final String XML_PARAM__RESULT__S1 = "S1";
    private static final String XML_PARAM__RESULT__S2 = "S2";
    private static final String XML_PARAM__RESULT__S3 = "S3";

    private static final String XML_PARAM__RESULT__SP = "SP";
    private static final String XML_PARAM__RESULT__SQ = "SQ";
    private static final String XML_PARAM__RESULT__SS = "SS";

    private static final String XML_PARAM__RESULT__RF = "RF";

    private static final String XML_PARAM__RESULT__FREQ = "FREQ";
    private static final String XML_PARAM__RESULT__LAMBDA1 = "Lambda1";
    private static final String XML_PARAM__RESULT__LAMBDA2 = "Lambda2";
    private static final String XML_PARAM__RESULT__LAMBDA3 = "Lambda3";
    private static final String XML_PARAM__RESULT__SLAMBDA = "SLambda";

    private static final String XML_PARAM__RESULT__ERROR = "Error";

    private static final String XML_PARAM__RESULT__IL1 = "IL1";
    private static final String XML_PARAM__RESULT__IL2 = "IL2";
    private static final String XML_PARAM__RESULT__IL3 = "IL3";

    private static final String XML_PARAM__RESULT__UPN1 = "UPN1";
    private static final String XML_PARAM__RESULT__UPN2 = "UPN2";
    private static final String XML_PARAM__RESULT__UPN3 = "UPN3";

    private static final String XML_PARAM__RESULT__UPP12 = "UPP12";
    private static final String XML_PARAM__RESULT__UPP23 = "UPP23";
    private static final String XML_PARAM__RESULT__UPP31 = "UPP31";

    private static final String XML_PARAM__RESULT__AU1 = "AU1";
    private static final String XML_PARAM__RESULT__AU2 = "AU2";
    private static final String XML_PARAM__RESULT__AU3 = "AU3";

    private static final String XML_PARAM__RESULT__AI1 = "AI1";
    private static final String XML_PARAM__RESULT__AI2 = "AI2";
    private static final String XML_PARAM__RESULT__AI3 = "AI3";

    private static final String XML_DATA__RESULT__DATATYPE__ACTUAL_VALUE = "Actual-Value";
    private static final String XML_DATA__RESULT__DATATYPE__METER_ERROR = "Meter-Error";
    private static final String XML_DATA__RESULT__FUNCTION__VALUE_MEASUREMENT = "Value-Measurement";
    private static final String XML_DATA__RESULT__FUNCTION__VECTOR_MEASUREMENT = "Vector-Measurement";

    public class ZeraResult {

    }


    public static void main(String args[]) {
        try {
            String xml = loadResultXml("C:\\_projects\\eon1\\verification\\result\\");
            int noResults = getNumberOfResultsInXml(xml);
            System.out.println("Number of Results: " + noResults);

            for (int i = 1; i <= noResults; i++) {
                System.out.println("-------------" + i + "---------------");
                String result = getZeraResult(xml, i);
                System.out.println(result);

            }
        } catch (Exception e) {
            writeLog(TAG + " :main() ", e);
        }

    }

    public static String loadResultXml(String path) {
        String result = null;

        try {
            //File zeraFile = new File(path + "RESULT.XML");
            path = path + "RESULT.XML";
            /*BufferedReader bufferedReader = new BufferedReader(new FileReader(zeraFile));

			StringBuffer stringBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line + "\n");
			}
			bufferedReader.close();
		   */
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(ApplicationAstSep.context.getAssets().open(path)));

            StringBuilder stringBuffer = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }
            bufferedReader.close();

            result = stringBuffer.toString();
        } catch (Exception e) {
            writeLog(ZeraXMLUtil.class.getSimpleName() + ":loadResultXml() ", e);
        }

        return result;
    }

    /**
     * Converts from xml zera date+time format to old format, e.g:
     * <p>
     * From xmlDate: dd.mm.yyyy xmlTime: HH:MM:SS   ->   ddmmyyyyhhmmss
     *
     * @param xmlZeraDate
     * @return
     */

    private static String xmlToTokenZeraDate(String xmlZeraDate, String xmlZeraTime) {
        StringBuffer date = new StringBuffer();
        try {
            date.append(xmlZeraDate.substring(0, 2));
            date.append(xmlZeraDate.substring(3, 5));
            date.append(xmlZeraDate.substring(6, 8));
            date.append(xmlZeraTime.substring(0, 2));
            date.append(xmlZeraTime.substring(3, 5));
            date.append(xmlZeraTime.substring(6, 8));
        } catch (Exception e) {
            writeLog(TAG + " :xmlToTokenZeraDate() ", e);
        }

        return date.toString();
    }

    private static String getDoubleValueAsString(String value, String unit) {

        double multiplier = 1;
        double doubleValue = 0;
        DecimalFormat decimalFormat = new DecimalFormat(DEFAULT_DECIMAL_VALUE);
        try {
            if (unit != null) {
                if (unit.charAt(0) == 'G') {
                    multiplier = 1000d * 1000d * 1000d;
                } else if (unit.charAt(0) == 'M') {
                    multiplier = 1000d * 1000d;
                } else if (unit.charAt(0) == 'k') {
                    multiplier = 1000d;
                } else if (unit.charAt(0) == 'm') {
                    multiplier = 0.001d;
                }
            }

            doubleValue = Double.valueOf(value).doubleValue();

            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
            decimalFormatSymbols.setDecimalSeparator('.');
            decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        } catch (Exception e) {
            writeLog(TAG + " :getDoubleValueAsString() ", e);
        }
        return decimalFormat.format(doubleValue *= multiplier);
    }

    private static String xmlToTokenValue(String xmlValue) {
        StringTokenizer stringTokenizer = new StringTokenizer(xmlValue, ";");
        String value = null;
        String unit = null;
        try {
            if (xmlValue.charAt(0) == ';') { // stringTokenizer does not return empty strings :(
                return "";
            }
            value = stringTokenizer.nextToken();
            unit = stringTokenizer.hasMoreTokens() ? stringTokenizer.nextToken() : null;
        } catch (Exception e) {
            writeLog(TAG + " :xmlToTokenValue() ", e);
        }
        return getDoubleValueAsString(value, unit);
    }

    private static String xmlToTokenZeraIRange(String xmlIRange) {
        StringTokenizer stringTokenizer = new StringTokenizer(xmlIRange, ";");
        String value = null;
        String unit = null;
        String clampUsed = null;
        try {
            value = stringTokenizer.nextToken();
            unit = stringTokenizer.nextToken();
            clampUsed = ";" + (stringTokenizer.hasMoreTokens() ? stringTokenizer.nextToken() : "");
        } catch (Exception e) {
            writeLog(TAG + " :xmlToTokenZeraIRange() ", e);
        }
        return getDoubleValueAsString(value, unit) + clampUsed;
    }


    private static String xmlToTokenZeraMM(String xmlMm) {
        if (xmlMm.endsWith("R") || xmlMm.endsWith("r")) {
            return "1";
        } else {
            return "0";
        }
    }

    private static String xmlToTokenZeraCz(String xmlCz) {
        String result = null;
        try {
            StringTokenizer stringTokenizer = new StringTokenizer(xmlCz, ";");
            String constant = stringTokenizer.nextToken();
            String exp = stringTokenizer.nextToken();
            String unit = stringTokenizer.nextToken();


            Hashtable units = new Hashtable();
            units.put("I/kWh", "0");
            units.put("I/kVarh", "1");
            units.put("I/kVah", "2");
            units.put("Wh/I", "3");
            units.put("Varh/I", "4");
            units.put("Vah/I", "5");
            units.get(unit);

            Hashtable exponents = new Hashtable();  // Note: Are these correct? Had to add x in front of them...
            exponents.put("xExp-5", "0");
            exponents.put("xExp-4", "1");
            exponents.put("xExp-3", "2");
            exponents.put("xExp-2", "3");
            exponents.put("xExp-1 ", "4");
            exponents.put("1", "5");
            exponents.put("xExp+1", "6");
            exponents.put("xExp+2", "7");
            exponents.put("xExp+3", "8");
            exponents.put("xExp+4", "9");
            exponents.put("xExp+5", "10");

            result = getDoubleValueAsString(constant, null) + ";" + units.get(unit) + ";" + exponents.get(exp);
        } catch (Exception e) {
            writeLog(TAG + " :xmlToTokenZeraCz() ", e);
        }
        return result;
    }

    private static String zeraXmlToTokenFormat(String resultXml) throws Exception {
        StringBuffer result = new StringBuffer();
        try {
            // ---------- ID -----------
            String id = "1";
            addResultRow(result, "ID", id);

            // ---------- REM -----------
            addResultRow(result, "Rem", Base64.encode(resultXml));

            // ---------- DATE -----------
            String xmlZeraDate = getXMLParameterData(resultXml, XML_PARAM__RESULT__DATE);
            String xmlZeraTime = getXMLParameterData(resultXml, XML_PARAM__RESULT__TIME);
            String dat = xmlToTokenZeraDate(xmlZeraDate, xmlZeraTime);
            addResultRow(result, "Dat", dat);

            // ---------- RID -----------
            String rid = "-1"; // TODO: Validate that the mapping below is correct
            String xmlDatatype = getXMLParameterData(resultXml, XML_PARAM__RESULT__DATATYPE);
            if (xmlDatatype.equals(XML_DATA__RESULT__DATATYPE__ACTUAL_VALUE)) {

                String function = getXMLParameterData(resultXml, XML_PARAM__RESULT__FUNCTION);
                if (function.equals(XML_DATA__RESULT__FUNCTION__VALUE_MEASUREMENT)) {
                    rid = "1";
                } else if (function.equals(XML_DATA__RESULT__FUNCTION__VECTOR_MEASUREMENT)) {
                    rid = "8";
                }
            } else if (xmlDatatype.equals(XML_DATA__RESULT__DATATYPE__METER_ERROR)) {
                rid = "5";
            }
            addResultRow(result, "RID", rid);

            // ---------- UR -----------
            String xmlURange = getXMLParameterData(resultXml, XML_PARAM__RESULT__U_RANGE);
            String ur = xmlToTokenValue(xmlURange);
            addResultRow(result, "UR", ur);

            // ---------- IR -----------
            String xmlIRange = getXMLParameterData(resultXml, XML_PARAM__RESULT__I_RANGE);
            String ir = xmlToTokenZeraIRange(xmlIRange);
            addResultRow(result, "IR", ir);

            // ---------- MM -----------
            String xmlMm = getXMLParameterData(resultXml, XML_PARAM__RESULT__M_MODE); // Currently not used
            String mm = xmlToTokenZeraMM(xmlMm);
            addResultRow(result, "MM", mm);

            // ---------- URAT -----------
            String urat = "1.000000;0;1.000000;0"; // Probably not used, probably does not change and is hardcoded
            addResultRow(result, "URat", urat);

            // ---------- IRAT -----------
            String irat = "1.000000;1.000000"; // Probably not used, probably does not change and is hardcoded
            addResultRow(result, "IRat", irat);

            if (rid.equals("1") || rid.equals("5")) {
                // ---------- AV0 -----------
                String xmlUPN1 = getXMLParameterData(resultXml, XML_PARAM__RESULT__UPN1);
                addResultRow(result, "AV0", xmlToTokenValue(xmlUPN1));

                // ---------- AV1 -----------
                String xmlUPN2 = getXMLParameterData(resultXml, XML_PARAM__RESULT__UPN2);
                addResultRow(result, "AV1", xmlToTokenValue(xmlUPN2));

                // ---------- AV2 -----------
                String xmlUPN3 = getXMLParameterData(resultXml, XML_PARAM__RESULT__UPN3);
                addResultRow(result, "AV2", xmlToTokenValue(xmlUPN3));

                // ---------- AV3 -----------
                String xmlIL1 = getXMLParameterData(resultXml, XML_PARAM__RESULT__IL1);
                addResultRow(result, "AV3", xmlToTokenValue(xmlIL1));

                // ---------- AV4 -----------
                String xmlIL2 = getXMLParameterData(resultXml, XML_PARAM__RESULT__IL2);
                addResultRow(result, "AV4", xmlToTokenValue(xmlIL2));

                // ---------- AV5 -----------
                String xmlIL3 = getXMLParameterData(resultXml, XML_PARAM__RESULT__IL3);
                addResultRow(result, "AV5", xmlToTokenValue(xmlIL3));

                // ---------- AV6 -----------
                String xmlAU1 = getXMLParameterData(resultXml, XML_PARAM__RESULT__AU1);
                addResultRow(result, "AV6", xmlToTokenValue(xmlAU1));

                // ---------- AV7 -----------
                String xmlAU2 = getXMLParameterData(resultXml, XML_PARAM__RESULT__AU2);
                addResultRow(result, "AV7", xmlToTokenValue(xmlAU2));

                // ---------- AV8 -----------
                String xmlAU3 = getXMLParameterData(resultXml, XML_PARAM__RESULT__AU3);
                addResultRow(result, "AV8", xmlToTokenValue(xmlAU3));

                // ---------- AV9 -----------
                String xmlAI1 = getXMLParameterData(resultXml, XML_PARAM__RESULT__AI1);
                addResultRow(result, "AV9", xmlToTokenValue(xmlAI1));

                // ---------- AV10 -----------
                String xmlAI2 = getXMLParameterData(resultXml, XML_PARAM__RESULT__AI2);
                addResultRow(result, "AV10", xmlToTokenValue(xmlAI2));

                // ---------- AV11 -----------
                String xmlAI3 = getXMLParameterData(resultXml, XML_PARAM__RESULT__AI3);
                addResultRow(result, "AV11", xmlToTokenValue(xmlAI3));

                // ---------- AV12 -----------
                String xmlUPP12 = getXMLParameterData(resultXml, XML_PARAM__RESULT__UPP12);
                addResultRow(result, "AV12", xmlToTokenValue(xmlUPP12));

                // ---------- AV13 -----------
                String xmlUPP23 = getXMLParameterData(resultXml, XML_PARAM__RESULT__UPP23);
                addResultRow(result, "AV13", xmlToTokenValue(xmlUPP23));

                // ---------- AV14 -----------
                String xmlUPP31 = getXMLParameterData(resultXml, XML_PARAM__RESULT__UPP31);
                addResultRow(result, "AV14", xmlToTokenValue(xmlUPP31));

                // ---------- AV15 -----------
                String xmlP1 = getXMLParameterData(resultXml, XML_PARAM__RESULT__P1);
                addResultRow(result, "AV15", xmlToTokenValue(xmlP1));

                // ---------- AV16 -----------
                String xmlP2 = getXMLParameterData(resultXml, XML_PARAM__RESULT__P2);
                addResultRow(result, "AV16", xmlToTokenValue(xmlP2));

                // ---------- AV17 -----------
                String xmlP3 = getXMLParameterData(resultXml, XML_PARAM__RESULT__P3);
                addResultRow(result, "AV17", xmlToTokenValue(xmlP3));

                // ---------- AV18 -----------
                String xmlQ1 = getXMLParameterData(resultXml, XML_PARAM__RESULT__Q1);
                addResultRow(result, "AV18", xmlToTokenValue(xmlQ1));

                // ---------- AV19 -----------
                String xmlQ2 = getXMLParameterData(resultXml, XML_PARAM__RESULT__Q2);
                addResultRow(result, "AV19", xmlToTokenValue(xmlQ2));

                // ---------- AV20 -----------
                String xmlQ3 = getXMLParameterData(resultXml, XML_PARAM__RESULT__Q3);
                addResultRow(result, "AV20", xmlToTokenValue(xmlQ3));

                // ---------- AV21 -----------
                String xmlS1 = replaceSpecialCharacter(getXMLParameterData(resultXml, XML_PARAM__RESULT__S1), HYPHEN, DEFAULT_DECIMAL_VALUE);
                addResultRow(result, "AV21", xmlToTokenValue(xmlS1));

                // ---------- AV22 -----------
                String xmlS2 = replaceSpecialCharacter(getXMLParameterData(resultXml, XML_PARAM__RESULT__S2), HYPHEN, DEFAULT_DECIMAL_VALUE);
                addResultRow(result, "AV22", xmlToTokenValue(xmlS2));

                // ---------- AV23 -----------
                String xmlS3 = replaceSpecialCharacter(getXMLParameterData(resultXml, XML_PARAM__RESULT__S3), HYPHEN, DEFAULT_DECIMAL_VALUE);
                addResultRow(result, "AV23", xmlToTokenValue(xmlS3));

                // ---------- AV24 -----------
                String xmlSP = getXMLParameterData(resultXml, XML_PARAM__RESULT__SP);
                addResultRow(result, "AV24", xmlToTokenValue(xmlSP));

                // ---------- AV25 -----------
                String xmlSQ = getXMLParameterData(resultXml, XML_PARAM__RESULT__SQ);
                addResultRow(result, "AV25", xmlToTokenValue(xmlSQ));

                // ---------- AV26 -----------
                String xmlSS = getXMLParameterData(resultXml, XML_PARAM__RESULT__SS);
                addResultRow(result, "AV26", xmlToTokenValue(xmlSS));

                // ---------- AV27 -----------
                String xmlRF = getXMLParameterData(resultXml, XML_PARAM__RESULT__RF);
                addResultRow(result, "AV27", xmlToTokenValue(xmlRF));

                // ---------- AV28 -----------
                String xmlFreq = getXMLParameterData(resultXml, XML_PARAM__RESULT__FREQ);
                addResultRow(result, "AV28", xmlToTokenValue(xmlFreq));

                // ---------- AV29 -----------
                String xmlLambda1 = replaceSpecialCharacter(getXMLParameterData(resultXml, XML_PARAM__RESULT__LAMBDA1), HYPHEN, DEFAULT_DECIMAL_VALUE);
                addResultRow(result, "AV29", xmlToTokenValue(xmlLambda1));

                // ---------- AV30 -----------
                String xmlLambda2 = replaceSpecialCharacter(getXMLParameterData(resultXml, XML_PARAM__RESULT__LAMBDA2), HYPHEN, DEFAULT_DECIMAL_VALUE);
                addResultRow(result, "AV30", xmlToTokenValue(xmlLambda2));

                // ---------- AV31 -----------
                String xmlLambda3 = replaceSpecialCharacter(getXMLParameterData(resultXml, XML_PARAM__RESULT__LAMBDA3), HYPHEN, DEFAULT_DECIMAL_VALUE);
                addResultRow(result, "AV31", xmlToTokenValue(xmlLambda3));

                // ---------- AV32 -----------
                if (rid.equals("1")) {
                    String xmlSLambda = getXMLParameterData(resultXml, XML_PARAM__RESULT__SLAMBDA);
                    addResultRow(result, "AV32", xmlToTokenValue(xmlSLambda));
                }

                if (rid.equals("5")) {
                    String xmlCz = getXMLParameterData(resultXml, XML_PARAM__RESULT__CZ);
                    String cz = xmlToTokenZeraCz(xmlCz);
                    addResultRow(result, "Cz", cz);

                    String imp = "10;0;0"; // Not really used, but leaving this hardcoded
                    addResultRow(result, "Imp", imp);

                    // ---------- Err -----------
                    String xmlError = getXMLParameterData(resultXml, XML_PARAM__RESULT__ERROR);
                    addResultRow(result, "Err", xmlToTokenValue(xmlError));
                }
            } else if (rid.equals("8")) {
                // ---------- IN/SN/L/Q -----------
                // Not mapped, are they used for KAT 3-5?
                addResultRow(result, "IN", ""); // Not used?
                addResultRow(result, "SN", ""); // Not used?
                addResultRow(result, "L", ""); // Not used?
                addResultRow(result, "Q", ""); // Not used?

                // ---------- BV -----------
                // BV fields are not mapped as they are not required according to �ystein K�resen
                addResultRow(result, "BV0", ""); // Not used?
                addResultRow(result, "BV1", ""); // Not used?
                addResultRow(result, "BV2", ""); // Not used?
                addResultRow(result, "BV3", ""); // Not used?
                addResultRow(result, "BV4", ""); // Not used?
                addResultRow(result, "BV5", ""); // Not used?
                addResultRow(result, "BV6", ""); // Not used?
                addResultRow(result, "BV7", ""); // Not used?
                addResultRow(result, "BV8", ""); // Not used?
                addResultRow(result, "BV9", ""); // L1TotalLoad (MeasuredLoadL1)
                addResultRow(result, "BV10", ""); // L2TotalLoad (MeasuredLoadL2)
                addResultRow(result, "BV11", ""); // L3TotalLoad (MeasuredLoadL3)
                addResultRow(result, "BV12", ""); // Not used?
                addResultRow(result, "BV13", ""); // Not used?
                addResultRow(result, "BV14", ""); // Not used?
                addResultRow(result, "BV15", ""); // L1MeasuredLoad
                addResultRow(result, "BV16", ""); // L2MeasuredLoad
                addResultRow(result, "BV17", ""); // L3MeasuredLoad
            }

            result.append("AGRACK\n");
        } catch (Exception e) {
            writeLog(TAG + " :zeraXmlToTokenFormat() ", e);
        }
        return result.toString();

    }

    private static void addResultRow(StringBuffer stringBuffer, String parameter, String result) {
        stringBuffer.append(parameter);
        stringBuffer.append(";");
        stringBuffer.append(result);
        stringBuffer.append("\n");
    }

    public static int getNumberOfResultsInXml(String xml) throws Exception {
        int allResults = 0;
        int supportedResults = 0;
        try {
            String param = "<" + XML_SECTION__RESULT + ">";

            int pos = 0;
            while (true) {
                pos = xml.indexOf(param, pos);

                if (pos != -1) {
                    pos += param.length();
                    allResults++;

                    String resultXml = getXMLResultSectionContents(xml, allResults, false);
                    String function = getXMLParameterData(resultXml, "Function");
                    String datatype = getXMLParameterData(resultXml, "Datatype");
                    if (isSupportResult(datatype, function)) {
                        supportedResults++;
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :getNumberOfResultsInXml() ", e);
        }
        return supportedResults;
    }

    private static boolean isSupportResult(String datatype, String function) {
        return datatype.equals(XML_DATA__RESULT__DATATYPE__ACTUAL_VALUE) || datatype.equals(XML_DATA__RESULT__DATATYPE__METER_ERROR);
    }

    private static String getXMLResultSectionContents(String xml, int resultNumber, boolean skipUnsupported) throws Exception {
        boolean performSearch = true;
        String startParameter = "<" + XML_SECTION__RESULT + ">";
        String endParameter = "</" + XML_SECTION__RESULT + ">";

        int currentPos = 0;
        int currentResult = 0;
        int currentSupportedResult = 0;
        try {
            while (performSearch) {
                int startPos = xml.indexOf(startParameter, currentPos);

                if (startPos > -1) {
                    int endPos = xml.indexOf(endParameter, startPos);

                    if (endPos > startPos) {
                        currentResult++;
                        currentPos = endPos;

                        if (skipUnsupported) { // Skip unsupported when counting
                            String resultXml = xml.substring(startPos + startParameter.length(), endPos);
                            String function = getXMLParameterData(resultXml, "Function");
                            String datatype = getXMLParameterData(resultXml, "Datatype");

                            if (isSupportResult(datatype, function)) {
                                currentSupportedResult++;
                            }

                            if (currentSupportedResult == resultNumber) {
                                return resultXml;
                            }
                        } else {
                            if (currentResult == resultNumber) {
                                return xml.substring(startPos + startParameter.length(), endPos);
                            }
                        }
                    }

                } else {
                    performSearch = false;
                }

            }
        } catch (Exception e) {
            writeLog(TAG + " :getNumberOfResultsInXml() ", e);
        }
        return null;
    }


    private static String getXMLParameterData(String xml, String parameterName) {
        String startParameter = "<" + parameterName + ">";
        String endParameter = "</" + parameterName + ">";
        try {
            int startPos = xml.indexOf(startParameter);

            if (startPos > -1) {
                int endPos = xml.indexOf(endParameter, startPos);

                if (endPos > startPos) {
                    return xml.substring(startPos + startParameter.length(), endPos);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :getXMLParameterData() ", e);
        }
        return null; // not found
    }

    public static String getZeraResult(String xml, int resultNumber) throws Exception {
        String resultXml = getXMLResultSectionContents(xml, resultNumber, true);
        return zeraXmlToTokenFormat(resultXml);
    }

    public static class Base64 {
        private static final String tbl = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

        public static String encode(String dataAsString) throws UnsupportedEncodingException {
            byte data[] = dataAsString.getBytes("UTF-8");

            String encoded = "", split = "";
            try {
                int paddingCount = (3 - (data.length % 3)) % 3;
                byte[] padded = new byte[data.length + paddingCount];
                System.arraycopy(data, 0, padded, 0, data.length);
                data = padded;
                for (int i = 0; i < data.length; i += 3) {
                    int j = ((data[i] & 0xff) << 16) + ((data[i + 1] & 0xff) << 8) + (data[i + 2] & 0xff);
                    encoded = encoded + tbl.charAt((j >> 18) & 0x3f) + tbl.charAt((j >> 12) & 0x3f) + tbl.charAt((j >> 6) & 0x3f) + tbl.charAt(j & 0x3f);
                }
                encoded = encoded.substring(0, encoded.length() - paddingCount) + "==".substring(0, paddingCount);
            } catch (Exception e) {
                writeLog(TAG + " :Base64() ", e);
            }
            return encoded;
        }

    }

    /**
     * @param actual
     * @param checkCharacter
     * @param replaceCharacter
     * @return String
     */
    private static String replaceSpecialCharacter(String actual, String checkCharacter, String replaceCharacter) {
        try {
            if (actual != null && actual != "" && actual.startsWith(checkCharacter)) {
                actual = replaceCharacter;
            }
        } catch (Exception e) {
            writeLog(TAG + " :Base64() ", e);
        }
        return actual;
    }

}
