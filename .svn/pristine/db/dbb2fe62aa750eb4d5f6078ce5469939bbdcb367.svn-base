package com.capgemini.sesp.ast.android.module.communication;

import android.content.SharedPreferences;
import android.util.Log;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.PropertyConstants;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceType;
import com.capgemini.sesp.ast.android.module.util.LanguageHelper;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.SecurityUtil;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.module.util.to.AndroidWOAttachmentBean;
import com.capgemini.sesp.ast.android.module.versionmanagement.SespStdVersionManager;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skvader.rsp.ast_sep.webservice_client.port.AndroidWebserviceClientAstSep;
import com.skvader.rsp.ast_sep.webservice_client.port.ForgotPasswordWebserviceClient;
import com.skvader.rsp.cft.common.context.UserContext;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.AndroidCallTO;
import com.skvader.rsp.cft.webservice_framework.WebServicePort;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


/**
 * Communication helper class which handles web service calls for entire application
 *
 * @author capgemini
 * @version 1.0
 * @since 1.0
 */
public class CommunicationHelper {

    public static final ObjectMapper JSONMAPPER = new ObjectMapper(); // can reuse, share globally
    private static String webserviceUrl = null;

    static {
        JSONMAPPER.setSerializationInclusion(Include.NON_NULL);
    }

    private static final int INFORMATIONAL = 100;
    private static final int INFORMATIONAL_MAX = 101;

    public static final int SUCCESSFUL_OK = 200;
    public static final int SUCCESSFUL_OK_MAX = 206;
    private static final int REDIRECTION = 300;
    private static final int REDIRECTION_MAX = 307;
    private static final int CLIENT_ERROR = 400;
    private static final int CLIENT_ERROR_MAX = 416;
    private static final int SERVER_ERROR = 500;
    private static final int SERVER_ERROR_MAX = 505;

    private static final String UPDATOR_ACTION_VERSION_CHECK = "new_version_check";
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_FRAMEWORK_VERSION = "frameworkVersion";
    private static final String PARAM_BUSINESS_VERSION = "businessVersion";
    private static final String HEADER_RESULT = "result";
    private static final String DEFAULT_CONNECTION_TIMEOUT = "10";

    /**
     * This method is used for calling autoupdator.
     * @param actionName  - Action to be performed by the auto updator
     * @return object
     * @throws Exception
     */
    public static Object callUpdatorApp (String actionName, String userName, String password ) throws Exception {

        String serverAddress = null;
        if (actionName.equalsIgnoreCase(UPDATOR_ACTION_VERSION_CHECK)) {
            serverAddress = SESPPreferenceUtil.getPreferenceString(SharedPreferenceKeys.VERSION_CHECK_URL);
        }
    	/*
    	 * SSL could be implemented for auto updator later
    	 */
        final HttpURLConnection connection = SecurityUtil.getURLConnection(serverAddress);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAM_ACTION, UPDATOR_ACTION_VERSION_CHECK));
        params.add(new BasicNameValuePair(PARAM_FRAMEWORK_VERSION,
                SespStdVersionManager.getCurrentFrameworkVersion()));
        params.add(new BasicNameValuePair(PARAM_BUSINESS_VERSION,
                SespStdVersionManager.getCurrentBusinessVersion()));

        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(getQuery(params));
        wr.flush();

        int responseCode = connection.getResponseCode();
        if (responseCode >= SUCCESSFUL_OK && responseCode <= SUCCESSFUL_OK_MAX) {
            if (actionName.equalsIgnoreCase(UPDATOR_ACTION_VERSION_CHECK)) {
               return  connection.getHeaderField(HEADER_RESULT);
            }
        }
        return null;
    }



    /**
     * This method is used for calling web services.
     * @param interfaceClass <b>(Class)</b> - The interface class
     * @param methodName <b>(String)</b> - method name
     * @param returnType
     * @param inputParameters
     * @return object
     * @throws Exception
     */
    public Object callWebservice(Class<? extends WebServicePort> interfaceClass,
                                        String methodName, Class<?> returnType, Object... inputParameters) throws Exception {
        final String serverAddress = getServerAddress();
    	/*
    	 * The decision to go for normal connection or
    	 * SSL would be based on the protocol used in the URL
    	 * i.e. http or https
    	 */
        final String sslCertName = getSslCertName();
        final String sslPort = getSslPort();
         HttpURLConnection connection = null;
         int maxConnectionTimeout = 0;
        try {
             connection = SecurityUtil.getURLConnection(serverAddress, sslCertName, sslPort);
          maxConnectionTimeout = Integer.parseInt(ApplicationAstSep.getPropertyValue(PropertyConstants.KEY_CONNECTION_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT)) * 1000;
        }catch(Exception e)
        {
            writeLog("CommunicationHelper : callWebservice()", e);
        }
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        String sessionUserName = null;
        String sessionPassword = null;
        // changes done for reset password functionality
        if (!AndroidUtilsAstSep.forgotPassword) {
            sessionUserName = SessionState.getInstance().getCurrentUserUsername();
            sessionPassword= SessionState.getInstance().getCurrentUserPassword();
        }
        connection.setRequestProperty(UserContext.KEY_USERNAME, sessionUserName);
        connection.setRequestProperty(UserContext.KEY_PASSWORD, sessionPassword);
        connection.setRequestProperty(UserContext.KEY_LANGUAGE_CODE, LanguageHelper.getLanguageCode());
        connection.setRequestProperty(UserContext.KEY_COUNTRY_CODE, LanguageHelper.getCountryCode());
        connection.setReadTimeout(maxConnectionTimeout);
        connection.setConnectTimeout(maxConnectionTimeout);
        connection.setRequestMethod("POST");
        if(AndroidWebserviceClientAstSep.class.isAssignableFrom(interfaceClass)) {
            connection.setRequestProperty("method", "handleAstAndroidRequest");
        }  // changes done for reset password functionality
        else if (ForgotPasswordWebserviceClient.class.isAssignableFrom(interfaceClass)){
            connection.setRequestProperty("method", "handleAstAndroidRequestForgotPassword");
        }
        else {
            connection.setRequestProperty("method", "handleBstAndroidRequest");
        }

        AndroidCallTO androidCall = new AndroidCallTO();
        androidCall.setMethod(methodName);
        List<String> parameters = new ArrayList<String>();
        androidCall.setParameters(parameters);
        List<String> classNames = new ArrayList<String>();
        androidCall.setClassNames(classNames);
        for (Object inputParameter : inputParameters) {
            parameters.add(JSONMAPPER.writeValueAsString(inputParameter));
            classNames.add(inputParameter == null ? null : inputParameter.getClass().getName());
        }
        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(JSONMAPPER.writeValueAsString(androidCall));
        wr.flush();
        int responseCode = 0;
        try {
            responseCode = connection.getResponseCode();
        }catch(SocketTimeoutException ste){
            writeLog("CommunicationHelper   : callWebservice() " , ste);
            throw new CommunicationException(408,ApplicationAstSep.context.getResources().getString(R.string.request_timeout_message));
        }
        String responseFromServer = null;
        if(responseCode >= CLIENT_ERROR){
            responseFromServer = convertStreamToString(connection.getErrorStream());
        } else {
            responseFromServer = convertStreamToString(connection.getInputStream());
        }
        if (responseCode >= SUCCESSFUL_OK && responseCode <= SUCCESSFUL_OK_MAX) {
            Object readValue = JSONMAPPER.readValue(responseFromServer, returnType);
            return readValue;
        } else if (responseCode >= INFORMATIONAL && responseCode <= INFORMATIONAL_MAX) {
            throw new CommunicationException(responseCode, responseFromServer);
        } else if (responseCode >= REDIRECTION && responseCode <= REDIRECTION_MAX) {
            throw new CommunicationException(responseCode, responseFromServer);
        } else if (responseCode >= CLIENT_ERROR && responseCode <= CLIENT_ERROR_MAX) {
            throw new CommunicationException(responseCode, responseFromServer);
        } else if (responseCode >= SERVER_ERROR && responseCode <= SERVER_ERROR_MAX) {
            throw new CommunicationException(responseCode, responseFromServer);
        } else {
            throw new CommunicationException(responseCode, "Unknown status code.");
        }
    }

    //TODO : Need to refactor after getting the upload attachment webservice
    public int uploadAttachmentCallWebservice(AndroidWOAttachmentBean attachmentBean, String fieldVisitId) throws Exception {

        FileInputStream fileInputStream = new FileInputStream(attachmentBean.getFileName());
        /** Modified Code as after refractoring **/
        HttpClient httpClient = new HttpClient();
        httpClient.connectForMultipart("saveAttachment");
        httpClient.addFormPart("caseId", attachmentBean.getCaseId().toString());
        httpClient.addFormPart("reasonTypeId", attachmentBean.getReasonTypeId().toString());
        httpClient.addFormPart("attachmentTypeId",attachmentBean.getAttachmentTypeId().toString());
        httpClient.addFormPart("fieldVisitId", fieldVisitId);
        httpClient.addFilePart("file", attachmentBean.getFileName(), IOUtils.toByteArray(fileInputStream));
        httpClient.finishMultipart();


        int responseCode = httpClient.getConnection().getResponseCode();
        String responseFromServer = null;

        if(responseCode >= CLIENT_ERROR){
            responseFromServer = convertStreamToString(httpClient.getConnection().getErrorStream());
        } else {
            responseFromServer = convertStreamToString(httpClient.getConnection().getInputStream());
        }
        httpClient.disconnect();
        if (responseCode >= SUCCESSFUL_OK && responseCode <= SUCCESSFUL_OK_MAX) {
            return responseCode;
        } else if (responseCode >= INFORMATIONAL && responseCode <= INFORMATIONAL_MAX) {
            throw new CommunicationException(responseCode, responseFromServer);
        } else if (responseCode >= REDIRECTION && responseCode <= REDIRECTION_MAX) {
            throw new CommunicationException(responseCode, responseFromServer);
        } else if (responseCode >= CLIENT_ERROR && responseCode <= CLIENT_ERROR_MAX) {
            throw new CommunicationException(responseCode, responseFromServer);
        } else if (responseCode >= SERVER_ERROR && responseCode <= SERVER_ERROR_MAX) {
            throw new CommunicationException(responseCode, responseFromServer);
        } else {
            throw new CommunicationException(responseCode, "Unknown status code.");
        }

    }

    /**
     *
     * Method to convert stream to string
     * @param instream <b>(InputStream)</b> - Input stream as input to this method
     * @return String
     * @throws IOException <b>(Exception)</b> - The IO Exception
     */
    private static String convertStreamToString(InputStream instream) throws IOException {
        String line;
        BufferedReader br = new BufferedReader(new java.io.InputStreamReader(instream, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null)
            sb.append(line + '\n');
        return sb.toString();
    }

    public static void setDefaultServerAddress(final String url) {
        Log.i("CommunicationHelper", "Setting default server address=" + url);
        webserviceUrl = url;

        // Lets put this to shared prefs also
        setServerAddress(url);
    }

    public static String getDefaultServerAddress() {
        return webserviceUrl;
    }

    public static String getServerAddress() {
        SharedPreferences p = AndroidUtilsAstSep.getSharedPreferences(SharedPreferenceType.PERSISTENT);
        return p.getString(SharedPreferenceKeys.SERVER_ADDRESS, getDefaultServerAddress());
    }

    public static void setServerAddress(final String serverAddress) {
        final SharedPreferences p = AndroidUtilsAstSep.getSharedPreferences(SharedPreferenceType.PERSISTENT);
        p.edit().putString(SharedPreferenceKeys.SERVER_ADDRESS, serverAddress).apply();
    }
    public static String getSslCertName() {
        SharedPreferences p = AndroidUtilsAstSep.getSharedPreferences(SharedPreferenceType.PERSISTENT);
        return p.getString(SharedPreferenceKeys.SSL_CERT_NAME, null);
    }

    public static void setSslCertName(final String sslCertName) {
        final SharedPreferences p = AndroidUtilsAstSep.getSharedPreferences(SharedPreferenceType.PERSISTENT);
        p.edit().putString(SharedPreferenceKeys.SSL_CERT_NAME, sslCertName).apply();
    }
    public static String getSslPort() {
        SharedPreferences p = AndroidUtilsAstSep.getSharedPreferences(SharedPreferenceType.PERSISTENT);
        return p.getString(SharedPreferenceKeys.SSL_PORT, null);
    }

    public static void setSslPort(final String sslPort) {
        final SharedPreferences p = AndroidUtilsAstSep.getSharedPreferences(SharedPreferenceType.PERSISTENT);
        p.edit().putString(SharedPreferenceKeys.SSL_PORT, sslPort).apply();
    }

    private static String getQuery(List<NameValuePair> params)
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(pair.getName());
            result.append("=");
            result.append(pair.getValue());
        }
        return result.toString();
    }

    private static class HttpClient {
        private HttpURLConnection connection;
        private OutputStream os;

        private String delimiter = "--";
        private String boundary =  "SwA"+Long.toString(System.currentTimeMillis())+"SwA";


        public void connectForMultipart(String method) throws Exception {
            connection = SecurityUtil.getURLConnection(getServerAddress(), getSslCertName(), getSslPort());
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false); // Don't use a Cached Copy
            connection.setRequestProperty("method", method);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setRequestProperty(UserContext.KEY_USERNAME, SessionState.getInstance().getCurrentUserUsername());
            connection.setRequestProperty(UserContext.KEY_PASSWORD, SessionState.getInstance().getCurrentUserPassword());
            connection.setRequestProperty(UserContext.KEY_LANGUAGE_CODE, LanguageHelper.getLanguageCode());
            connection.setRequestProperty(UserContext.KEY_COUNTRY_CODE, LanguageHelper.getCountryCode());
            os = connection.getOutputStream();
        }

        public void addFormPart(String paramName, String value) throws Exception {
            writeParamData(paramName, value);
        }

        public HttpURLConnection getConnection() {
            return this.connection;
        }

        public void addFilePart(String paramName, String fileName, byte[] data) throws Exception {
            os.write( (delimiter + boundary + "\r\n").getBytes());
            os.write( ("Content-Disposition: form-data; name=\"" + paramName +  "\"; filename=\"" + fileName + "\"\r\n"  ).getBytes());
            os.write( ("Content-Type: application/octet-stream\r\n"  ).getBytes());
            os.write( ("Content-Transfer-Encoding: binary\r\n"  ).getBytes());
            os.write("\r\n".getBytes());

            os.write(data);

            os.write("\r\n".getBytes());

        }

        public void finishMultipart() throws Exception {
            os.write((delimiter + boundary + delimiter + "\r\n").getBytes());
        }


        private void writeParamData(String paramName, String value) throws Exception {
            os.write( (delimiter + boundary + "\r\n").getBytes());
            os.write( "Content-Type: text/plain\r\n".getBytes());
            os.write( ("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n").getBytes());
            os.write(("\r\n" + value + "\r\n").getBytes());
        }

        public void disconnect() throws IOException {
            os.flush();
            os.close();
            connection.disconnect();
        }
    }


}
