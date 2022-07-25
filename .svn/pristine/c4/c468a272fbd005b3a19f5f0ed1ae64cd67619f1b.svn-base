/**
 * @Copyright Capgemini
 */
package com.capgemini.sesp.ast.android.module.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.ArrayMap;
import android.util.Log;
import android.util.LongSparseArray;

import androidx.annotation.NonNull;

import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.communication.CommunicationHelper;
import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO;
import com.capgemini.sesp.ast.android.module.util.to.AndroidWOAttachmentBean;
import com.capgemini.sesp.ast.android.module.util.to.DataTO;
import com.capgemini.sesp.ast.android.ui.activity.document_download.WoDocumentLiteTO;
import com.capgemini.sesp.ast.android.ui.activity.order.OrderSummaryCategory;
import com.capgemini.sesp.ast.android.ui.wo.bean.WoAuditLogBean;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skvader.rsp.ast_sep.common.mobile.bean.HelpDocBean;
import com.skvader.rsp.ast_sep.common.to.ast.table.FieldDocumentInfoTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoAddressTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoContactTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventTechPlanTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoContactCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoEventCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInstCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.to.cft.table.AttachmentReasonTTO;
import com.skvader.rsp.cft.common.to.cft.table.AttachmentTTO;
import com.skvader.rsp.cft.common.to.cft.table.SystemParameterTO;
import com.skvader.rsp.cft.common.to.custom.base.BaseTO;
import com.skvader.rsp.cft.common.to.custom.base.IdTO;
import com.skvader.rsp.cft.common.to.custom.base.IuTimestampTO;
import com.skvader.rsp.cft.common.util.DateTimeFormat;
import com.skvader.rsp.cft.common.util.Mapper;
import com.skvader.rsp.cft.common.util.Utils;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.CaseTCustomTO;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.TypeDataUtil.getValidOrgDivValue;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/*import com.skvader.rsp.ast_sep.common.to.ast.table.FieldDocumentInfoTTO;*/

/**
 * This class has DAO methods which would be
 * changed soon, all the directly exposed dao methods would be abstracted
 * by ContentProvider framework to have better design
 *
 * @author Capgemini
 * @version 1.0
 * @since 12th March, 2015
 */

public final class DatabaseHandler extends SQLiteOpenHelper {

    private final String TAG = "DatabaseHandler";

    public static final ObjectMapper JSONMAPPER = new ObjectMapper();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dataManager.db";
    private static final String TABLE_DATA = "data";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ID_ENTITY = "id_entity";                // PK for TO class
    private static final String KEY_IU_TIMESTAMP = "iu_timestamp";            // iu_timestamp for TO (if exists)
    private static final String KEY_TO_CLASS = "to_class";                // Full class name of TO
    private static final String KEY_DATA = "data";                    // JSON representation of TO

    // No need make the table name variables static
    // Following are the sqlite tables used for work orders -- to be specified in content URI soon
    private transient final String WO_ATTACHMENTS = "WO_ATTACHMENTS";
    private transient final String WO_SYNCH_AUDIT = "WO_SYNCH_AUDIT";
    private transient final String LAST_SESSION = "LAST_SESSION";
    private transient final String WORKORDER_CACHE = "WORKORDER_CACHE";
    private transient final String WO_SYNC_DATA = "WO_SYNC_DATA";
    private transient final String WO_FLOW_EXEC = "WO_FLOW_EXEC";
    private transient final String WO_USER_PREFRENCE = "WO_USER_PREFRENCE";
    private transient final String WO_PDA_PAGE_T = "WO_PDA_PAGE_T";
    private final String WO_STATUS_IN_LOCAL = "WO_STATUS_IN_LOCAL";
    private final String WO_STEP_STATUS = "WO_STEP_STATUS";
    private final String HELP_DOCUMENT = "HELP_DOCUMENTS";
    private static final String G_DISTANCE_MATRIX = "G_DISTANCE_MATRIX";
    private static final String G_DIRECTION_RESULT="G_DIRECTION_RESULT";
    private static final String TRANSLATION_DATA="TRANSLATION_DATA";

    /*
     * columns
     */
    private transient final String ID_KEY = "_id";
    private transient final String ID_CASE = "ID_CASE";
    private transient final String ID_CASE_T = "ID_CASE_T";
    private transient final String WORKORDER_JSON = "WORKORDER_JSON";
    private transient final String ATTACHMENT_PATH = "ATTACHMENT_PATH";
    private transient final String ATTACHMENT_MIME_TYPE_ID = "ATTACHMENT_MIME_TYPE_ID";
    private transient final String ATTACHMENT_REASON_TYPE_ID = "ATTACHMENT_REASON_TYPE_ID";
    private transient final String PAGE_ID = "PAGE_ID";
    private transient final String PAGE_INDEX = "PAGE_INDEX";
    private transient final String SUCCESSFULLY_COMPLETED = "SUCCESSFULLY_COMPLETED";
    private transient final String SYNCH_ATTEMPT_NUM = "SYNCH_ATTEMPT_NUM";
    private transient final String SYNCH_START_DATETIME = "SYNCH_START_DATETIME";
    private transient final String SYNCH_END_DATETIME = "SYNCH_END_DATETIME";
    private transient final String ERROR_INFO = "ERROR_INFO";
    private transient final String INST_CODE = "INST_CODE";
    private transient final String KEY_INFO = "KEY_INFO";
    private transient final String KEY_NUMBER = "KEY_NUMBER";
    private transient final String ADDRESS_STREET = "ADDRESS_STREET";
    private transient final String ADDRESS_CITY = "ADDRESS_CITY";
    private transient final String CUSTOMER_NAME = "CUSTOMER_NAME";
    private transient final String SLA_DEADLINE = "SLA_DEADLINE";
    private transient final String TIME_RESERVATION_START = "TIME_RESERVATION_START";
    private transient final String TIME_RESERVATION_START_MILLIS = "TIME_RESERVATION_START_MILLIS";
    private transient final String TIME_RESERVATION_END = "TIME_RESERVATION_END";
    private transient final String STARTED = "STARTED";
    private transient final String ASSIGNED = "ASSIGNED";
    private transient final String ANTENNA_ID_UNIT_MODEL = "ANTENNA_ID_UNIT_MODEL";
    private transient final String ID_UNIT_MODEL_CFT_T = "ID_UNIT_MODEL_CFT_T";
    private transient final String WO_UNIT_TO_JSON = "WO_UNIT_TO_JSON";
    private transient final String WORKORDERLTO_JSON = "WORKORDERLTO_JSON";
    private transient final String USER_COMPLETED = "USER_COMPLETED";
    private transient final String TRANSMITTED = "TRANSMITTED";
    private transient final String TRANSMISSION_SUCCESS = "TRANSMISSION_SUCCESS";
    private transient final String FIELD_VISIT_ID = "FIELD_VISIT_ID";
    private transient final String ID_PDA_PAGE_T = "ID_PDA_PAGE_T";
    private transient final String ABORT_UTIL = "ABORT_UTIL";
    private transient final String WO_SAVE_STATE = "WO_SAVE_STATE";
    private transient final String STATE = "STATE";
    public transient static final String LAST_LOGIN_USERNAME = "last_login_username";
    public transient static final String LAST_LOGIN_PASSWORD = "last_login_password";
    public transient static final String USER_PREFRENCE_KEY = "USER_PREFERENCE_KEY";
    public transient static final String USER_PREFRENCE_VALUE = "USER_PREFRENCE_VALUE";
    public static final String PRESENT_STEP = "PRESENT_STEP";
    public static final String STEP_NO = "STEP_NO";
    public static final String STEP_VALUE_STRING = "STEP_VALUE_STRING";
    public static final String STEP_WO = "STEP_WORK_ORDER_CUSTOM_WRAPPER_TO";
    private transient final String WO_DOCUMENT = "WO_DOCUMENT";
    private transient final String CODE = "CODE";
    private transient final String DOCUMENT_URL = "DOCUMENT_URL";
    private transient final String DOCUMENT_ALIAS_NAME = "DOCUMENT_ALIAS_NAME";
    private transient final String DOCUMENT_VERSION = "DOCUMENT_VERSION";
    private transient final String ID_DOMAIN = "ID_DOMAIN";
    private transient final String CHANGE_SIGNATURE = "CHANGE_SIGNATURE";
    private transient final String CHANGE_TIMESTAMP = "CHANGE_TIMESTAMP";
    private transient final String ID_FIELD_DOCUMENT_T = "ID_FIELD_DOCUMENT_T";
    private transient final String INFO = "INFO";
    private transient final String TOKEN_CODE = "TOKEN_CODE";
    private transient final String PRIORITY = "PRIORITY";
    private transient final String LOCAL_DB_CREATE_TIMESTAMP = "LOCAL_DB_CREATE_TIMESTAMP";
    public static final String ROLE_ID = "ROLE_ID";
    public static final String MODULE_ID = "MODULE_ID";
    public static final String HELPTEXT = "HELPTEXT";
    public static final String UPDATED_TIMESTAMP = "UPDATED_TIMESTAMP";
    public static final String TRANSLATED_NAME="TRANSLATED_NAME";

    private static final DatabaseHandler databaseHandler = new DatabaseHandler(ApplicationAstSep.context);

    private transient final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.CLIENT_DATE_TIME.getPattern(),
            Locale.getDefault());


    public static DatabaseHandler createDatabaseHandler() {
        return databaseHandler;
    }

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        // This will create tables

        /*
         * Create table to persist the PDA Page Type value
         */
        final String CREATE_PDA_PAGE_T_TABLE = "CREATE TABLE " + WO_PDA_PAGE_T +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + ID_CASE + " NUMBER NOT NULL,"
                + ID_PDA_PAGE_T + " NUMBER NOT NULL"
                +
                ")";
        db.execSQL(CREATE_PDA_PAGE_T_TABLE);


        final String CREATE_DATA_TABLE = "CREATE TABLE " + TABLE_DATA +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ID_ENTITY + " NUMBER,"
                + KEY_IU_TIMESTAMP + " NUMBER,"
                + KEY_TO_CLASS + " TEXT,"
                + KEY_DATA + " TEXT"
                +
                ")";
        db.execSQL(CREATE_DATA_TABLE);

        StringBuilder builder = new StringBuilder();

        /*
         *  Create table to store work order flow
         *  last executed flow page number
         */
        builder.append("CREATE TABLE ").append(WO_USER_PREFRENCE);
        builder.append("( ");
        builder.append(ID_KEY).append(" INTEGER PRIMARY KEY AUTOINCREMENT , ");
        builder.append(ID_CASE).append(" NUMBER NOT NULL , ");
        builder.append(PAGE_ID).append(" TEXT  NOT NULL, ");
        builder.append(PAGE_INDEX).append(" NUMBER NOT NULL, ");
        builder.append(USER_PREFRENCE_KEY).append(" TEXT  NOT NULL , ");
        builder.append(USER_PREFRENCE_VALUE).append(" TEXT ");
        builder.append(" ) ");
        Log.i(TAG, "Creating table - " + WO_USER_PREFRENCE);
        Log.i(TAG, builder.toString());

        db.execSQL(builder.toString());



        /*
         *  Create table to store work order flow
         *  last executed flow page number
         */
        builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(WO_FLOW_EXEC);
        builder.append("( ");
        builder.append(ID_KEY).append(" INTEGER PRIMARY KEY AUTOINCREMENT , ");
        builder.append(ID_CASE).append(" NUMBER NOT NULL , ");
        builder.append(ID_CASE_T).append(" NUMBER NOT NULL , ");
        builder.append(PAGE_INDEX).append(" NUMBER NOT NULL , ");
        builder.append(PAGE_ID).append(" TEXT  NOT NULL, ");
        builder.append(WORKORDER_JSON).append(" TEXT  , ");
        builder.append(ABORT_UTIL).append(" TEXT , ");
        builder.append(ID_PDA_PAGE_T).append(" NUMBER DEFAULT 0 , ");
        builder.append(SUCCESSFULLY_COMPLETED).append(" NUMBER DEFAULT 0 ");
        builder.append(" ) ");
        Log.i(TAG, "Creating table - " + WO_FLOW_EXEC);
        Log.i(TAG, builder.toString());
        db.execSQL(builder.toString());





        /*
         * Create table for modified work order data to be saved
         *
         */
        builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(WO_SYNC_DATA);
        builder.append("( ");
        builder.append(ID_KEY).append(" INTEGER PRIMARY KEY AUTOINCREMENT , ");
        builder.append(ID_CASE).append(" NUMBER NOT NULL , ");
        builder.append(WORKORDER_JSON).append(" TEXT NOT NULL ");
        builder.append(" ) ");

        Log.i(TAG, "Creating table - " + WO_SYNC_DATA);
        Log.i(TAG, builder.toString());

        db.execSQL(builder.toString());

        Log.i(TAG, "Table created - " + WO_SYNC_DATA);

        /*
         * Create table for modified work order data to be saved
         * Required for Circular Flows
         */
        builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(WO_SAVE_STATE);
        builder.append("( ");
        builder.append(ID_KEY).append(" INTEGER PRIMARY KEY AUTOINCREMENT , ");
        builder.append(STATE).append(" TEXT NOT NULL , ");
        builder.append(WORKORDER_JSON).append(" TEXT NOT NULL ");
        builder.append(" ) ");

        Log.i(TAG, "Creating table - " + WO_SAVE_STATE);
        Log.i(TAG, builder.toString());

        db.execSQL(builder.toString());

        Log.i(TAG, "Table created - " + WO_SAVE_STATE);

        /*
         * Create table for storing the file/attachment references
         * for work orders (NOT THE FILE CONTENT)
         */
        builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(WO_ATTACHMENTS);
        builder.append("( ");
        builder.append(ID_KEY).append(" INTEGER PRIMARY KEY AUTOINCREMENT , ");
        builder.append(ID_CASE).append(" NUMBER NOT NULL  , ");
        builder.append(ATTACHMENT_PATH).append(" TEXT NOT NULL , ");
        builder.append(FIELD_VISIT_ID).append(" TEXT NOT NULL , ");
        builder.append(ATTACHMENT_MIME_TYPE_ID).append(" NUMBER NOT NULL , ");
        builder.append(ATTACHMENT_REASON_TYPE_ID).append(" TEXT NOT NULL , "); // Nullable may be
        builder.append(TRANSMITTED).append(" NUMBER DEFAULT 0 , "); // Nullable may be
        builder.append(TRANSMISSION_SUCCESS).append(" NUMBER DEFAULT 0  "); // Nullable may be
        builder.append(" ) ");

        Log.i(TAG, "Creating table - " + WO_ATTACHMENTS);
        Log.i(TAG, builder.toString());

        db.execSQL(builder.toString());

        Log.i(TAG, "Table created - " + WO_ATTACHMENTS);

        /*
         *  WO_SYNCH_RETRY_DETAILS - Audit table that keep tracks of
         *  all attempts (successful or failed) for a completed work-order
         *
         *  Note: This table does not hold reference to WO_LAST_EXEC_FLOW_COUNT
         *  table because if the WO is successfully transmitted to WS then after 3 days
         *  the workorder data would be purged from SQLite tables, but only the reference
         *  would exist in the audit table given case id and case type id
         */
        builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(WO_SYNCH_AUDIT);
        builder.append(" ( ");
        builder.append(ID_KEY).append(" INTEGER PRIMARY KEY AUTOINCREMENT , ");
        builder.append(ID_CASE).append(" NUMBER NOT NULL , ");
        builder.append(ID_CASE_T).append(" NUMBER NOT NULL , ");
        builder.append(SYNCH_ATTEMPT_NUM).append(" NUMBER DEFAULT 1 , ");
        builder.append(SYNCH_START_DATETIME).append(" TEXT NOT NULL , ");
        builder.append(SYNCH_END_DATETIME).append(" TEXT , ");
        builder.append(SUCCESSFULLY_COMPLETED).append(" NUMBER DEFAULT 0 , ");
        builder.append(ERROR_INFO).append(" TEXT ");
        builder.append(" ) ");

        Log.i(TAG, "Creating table - " + WO_SYNCH_AUDIT);
        Log.i(TAG, builder.toString());

        db.execSQL(builder.toString());

        Log.i(TAG, "Table created - " + WO_SYNCH_AUDIT);

        builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(LAST_SESSION);
        builder.append(" ( ");
        builder.append(ID_KEY).append(" INTEGER PRIMARY KEY AUTOINCREMENT , ");
        builder.append(LAST_LOGIN_USERNAME).append(" TEXT NOT NULL , ");
        builder.append(LAST_LOGIN_PASSWORD).append(" TEXT NOT NULL  ");
        builder.append(" ) ");

        Log.i(TAG, "Creating table - " + LAST_SESSION);
        Log.i(TAG, builder.toString());

        db.execSQL(builder.toString());

        Log.i(TAG, "Table created - " + LAST_SESSION);
/*
        WO_STATUS_IN_LOCAL & WO_STEP_STATUS Tables are created for managing Stepper Resume functionality

        Create Table to Store status of work order locally*/

        builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(WO_STATUS_IN_LOCAL);
        builder.append("( ");
        builder.append(ID_CASE).append(" TEXT PRIMARY KEY , ");
        builder.append(PRESENT_STEP).append(" NUMBER ,");
        builder.append("STEP_IDENTIFIERS").append(" TEXT ");
        builder.append(" ) ");
        Log.i(TAG, "Creating table - " + WO_STATUS_IN_LOCAL);
        Log.i(TAG, builder.toString());
        db.execSQL(builder.toString());
        Log.i(TAG, "Table created - " + WO_STATUS_IN_LOCAL);

        /*Table for saving Status of each Step*/

        builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(WO_STEP_STATUS);
        builder.append("( ");
        builder.append(ID_CASE).append(" NUMBER NOT NULL , ");
        builder.append(STEP_NO).append(" TEXT NOT NULL , ");
        builder.append(STEP_VALUE_STRING).append(" TEXT  , ");
        builder.append(STEP_WO).append(" TEXT  , ");
        builder.append("PRIMARY KEY (" + ID_CASE + "," + STEP_NO + ")");
        builder.append(" ) ");
        Log.i(TAG, "Creating table - " + WO_STEP_STATUS);
        Log.i(TAG, builder.toString());
        db.execSQL(builder.toString());



        /*
         * Create WORKORDER_CACHE table: a dedicated sqlite table for performance
         * improvement related to work-order
         */


        builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(WORKORDER_CACHE);
        builder.append(" ( ");
        builder.append(ID_KEY).append(" INTEGER PRIMARY KEY AUTOINCREMENT , ");
        builder.append(ID_CASE).append(" NUMBER NOT NULL , ");
        builder.append(ID_CASE_T).append(" NUMBER NOT NULL , ");
        builder.append(PRIORITY).append(" TEXT , ");
        builder.append(INST_CODE).append(" TEXT , ");
        builder.append(KEY_INFO).append(" TEXT , ");
        builder.append(KEY_NUMBER).append(" TEXT , ");
        builder.append(ADDRESS_STREET).append(" TEXT , ");
        builder.append(ADDRESS_CITY).append(" TEXT , ");
        builder.append(CUSTOMER_NAME).append(" TEXT , ");
        builder.append(SLA_DEADLINE).append(" TEXT , "); // Date field in reality
        builder.append(TIME_RESERVATION_START).append(" TEXT , "); // Date field in reality
        builder.append(TIME_RESERVATION_START_MILLIS).append(" NUMBER , "); // Date field in reality
        builder.append(TIME_RESERVATION_END).append(" TEXT , "); // Date field in reality
        builder.append(STARTED).append(" NUMBER , "); // Boolean field in reality
        builder.append(ASSIGNED).append(" NUMBER , "); // Boolean field in reality
        builder.append(ANTENNA_ID_UNIT_MODEL).append(" NUMBER , "); // Long value
        builder.append(ID_UNIT_MODEL_CFT_T).append(" NUMBER , "); // Long value
        builder.append(WO_UNIT_TO_JSON).append(" TEXT , ");
        builder.append(WORKORDERLTO_JSON).append(" TEXT , ");
        builder.append(WORKORDER_JSON).append(" TEXT , ");
        builder.append(USER_COMPLETED).append(" NUMBER DEFAULT 0 ");
        builder.append(" ) ");
        Log.i(TAG, "Creating table - " + WORKORDER_CACHE);
        db.execSQL(builder.toString());
        buildIndexes(db);


        /*
         * Create HELP_DOCUMENT table: a dedicated sqlite table for Help Document
         *  help document related  information
         * DOMAINID: PArticular Domain
         * RoleID : Particular Role
         * ModuleID : for Particular Module
         * HelpTEXT :  MAin Content
         * UPDATEDTIMESTAMP : timestamp when it is updated
         * pageId : Inside module for different page id
         */

        builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(HELP_DOCUMENT);
        builder.append("( ");
        builder.append(KEY_ID).append(" INTEGER  , ");
        builder.append(ID_DOMAIN).append(" TEXT , ");
        builder.append(ROLE_ID).append(" TEXT , ");
        builder.append(MODULE_ID).append(" TEXT , ");
        builder.append(HELPTEXT).append(" TEXT , ");
        builder.append(UPDATED_TIMESTAMP).append(" TEXT , ");
        builder.append(PAGE_ID).append(" TEXT  , ");
        builder.append("PRIMARY KEY (" + KEY_ID + ")");
        builder.append(" ) ");
        Log.i(TAG, "Creating table - " + KEY_ID);
        Log.i(TAG, builder.toString());
        db.execSQL(builder.toString());





        /*
         * Create WO_DOCUMENT table: a dedicated sqlite table for storing
         * document related information in Android application
         */

        builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(WO_DOCUMENT);
        builder.append(" ( ");
        builder.append(ID_KEY).append(" INTEGER PRIMARY KEY AUTOINCREMENT , ");
        builder.append(ID_DOMAIN).append(" NUMBER NOT NULL , ");
        builder.append(ID_CASE_T).append(" NUMBER , ");
        builder.append(ID_FIELD_DOCUMENT_T).append(" NUMBER NOT NULL , ");
        builder.append(CODE).append(" TEXT NOT NULL , ");
        builder.append(DOCUMENT_URL).append(" TEXT NOT NULL , ");
        builder.append(DOCUMENT_ALIAS_NAME).append(" TEXT NOT NULL , ");
        builder.append(DOCUMENT_VERSION).append(" TEXT NOT NULL , ");
        builder.append(INFO).append(" TEXT , ");
        builder.append(CHANGE_SIGNATURE).append(" TEXT , ");  // Date field in reality
        builder.append(CHANGE_TIMESTAMP).append(" TEXT , ");  // Date field in reality
        builder.append(TOKEN_CODE).append(" TEXT NOT NULL , ");
        builder.append(LOCAL_DB_CREATE_TIMESTAMP).append(" TEXT ");  // Date field in reality
        builder.append(" ) ");

        Log.i(TAG, "Creating table - " + WO_DOCUMENT);
        Log.i(TAG, builder.toString());

        db.execSQL(builder.toString());


        //Table dedicated for storing distance matrix from locations to locations
        final String L_FROM = "L_FROM";
        final String L_TO = "L_TO";
        final String ELEMENT = "ELEMENT";

        builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(G_DISTANCE_MATRIX).append(" ( ").append(
                ID_KEY + " integer NOT NULL PRIMARY KEY AUTOINCREMENT,").append(
                L_FROM + " VARCHAR(100) NOT NULL,").append(
                L_TO + " VARCHAR(100) NOT NULL,").append(
                ELEMENT + " VARCHAR(300) NOT NULL,").append(
                KEY_IU_TIMESTAMP + " timestamp NOT NULL)");

        db.execSQL(builder.toString());

        /*builder = new StringBuilder();
        builder.append("CREATE TABLE ").append( G_DIRECTION_RESULT).append(" ( ").append(
                ID_KEY+ " integer NOT NULL PRIMARY KEY AUTOINCREMENT,").append(
                L_FROM +" VARCHAR(100) NOT NULL,").append(
                L_TO+ " VARCHAR(100) NOT NULL,").append(
                ELEMENT+" TEXT NOT NULL,").append(
                KEY_IU_TIMESTAMP+" timestamp NOT NULL)");

        db.execSQL(builder.toString());*/

        // table for translation
        builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(TRANSLATION_DATA);
        builder.append(" ( ");
        //builder.append(ID_KEY).append(" INTEGER PRIMARY KEY AUTOINCREMENT , ");
        builder.append(TOKEN_CODE).append(" TEXT PRIMARY KEY , ");
        builder.append(TRANSLATED_NAME).append(" TEXT ");  // Data translated from code
        builder.append(" ) ");

        Log.i(TAG, "Creating table - " + TRANSLATION_DATA);
        Log.i(TAG, builder.toString());

        db.execSQL(builder.toString());
    }


    /**
     * Method added to create indexes considering large volume
     * of work-orders in future
     *
     * @param db {@link SQLiteDatabase}
     * @since 4th February, 2015
     */

    private void buildIndexes(final SQLiteDatabase db) {
        try {
            if (db != null) {
                StringBuilder builder = new StringBuilder();

                // Build index on "data" table
                builder.append(" CREATE INDEX ").append(" IDX__").append(TABLE_DATA);
                builder.append(" ON ").append(TABLE_DATA);
                builder.append(" ( ").append(KEY_TO_CLASS).append(" ) ");
                Log.i(TAG, "buildIndexes, executing query=" + builder.toString());

                db.execSQL(builder.toString());

                Log.i(TAG,
                        "buildIndexes, successfully created index= IDX__" + TABLE_DATA);

                builder = new StringBuilder();
                builder.append(" CREATE INDEX ").append(" IDX__CASE_ID_").append(WO_USER_PREFRENCE);
                builder.append(" ON ").append(WO_USER_PREFRENCE);
                builder.append(" ( ").append(ID_CASE);
                builder.append(" ) ");
                Log.i(TAG, "buildIndexes, executing query=" + builder.toString());
                db.execSQL(builder.toString());
                Log.i(TAG,
                        "buildIndexes, successfully created index= IDX__CASE_ID_" + WO_USER_PREFRENCE);


                builder = new StringBuilder();
                builder.append(" CREATE INDEX ").append(" IDX__CASE_ID_CASE_T__").append(WO_FLOW_EXEC);
                builder.append(" ON ").append(WO_FLOW_EXEC);
                builder.append(" ( ").append(ID_CASE).append(" , ").append(ID_CASE_T);
                builder.append(" ) ");
                Log.i(TAG, "buildIndexes, executing query=" + builder.toString());
                db.execSQL(builder.toString());
                Log.i(TAG,
                        "buildIndexes, successfully created index= DX__CASE_ID_CASE_T__" + WO_FLOW_EXEC);

                builder = new StringBuilder();
                builder.append(" CREATE INDEX ").append(" IDX__PAGE_INDEX_").append(WO_FLOW_EXEC);
                builder.append(" ON ").append(WO_FLOW_EXEC);
                builder.append(" ( ").append(PAGE_INDEX);
                builder.append(" ) ");
                Log.i(TAG, "buildIndexes, executing query=" + builder.toString());
                db.execSQL(builder.toString());
                Log.i(TAG,
                        "buildIndexes, successfully created index= IDX__PAGE_INDEX_" + WO_FLOW_EXEC);

                // Build index on ID_CASE_T column in WORKORDER_CACHE table

                builder = new StringBuilder();
                builder.append(" CREATE INDEX ").append(" IDX__ID_CASE_T__").append(WORKORDER_CACHE);
                builder.append(" ON ").append(WORKORDER_CACHE);
                builder.append(" ( ").append(ID_CASE_T); //.append(" , ").append(ID_CASE_T);
                builder.append(" ) ");

                Log.i(TAG, "buildIndexes, executing query=" + builder.toString());

                db.execSQL(builder.toString());

                Log.i(TAG,
                        "buildIndexes, successfully created index= IDX__ID_CASE_T__WORKORDER_CACHE");

                // Build index on ID_CASE column in WORKORDER_CACHE table

                builder = new StringBuilder();
                builder.append(" CREATE INDEX ").append(" IDX__ID_CASE__").append(WORKORDER_CACHE);
                builder.append(" ON ").append(WORKORDER_CACHE);
                builder.append(" ( ").append(ID_CASE); //.append(" , ").append(ID_CASE_T);
                builder.append(" ) ");

                Log.i(TAG, "buildIndexes, executing query=" + builder.toString());

                db.execSQL(builder.toString());

                Log.i(TAG,
                        "buildIndexes, successfully created index= IDX__ID_CASE__WORKORDER_CACHE");
            }
        } catch (Exception e) {
            writeLog(TAG + ": buildIndexes()", e);
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {

            // Will handle database upgrades
            Log.i(TAG, "onUpgrade, dropping index IDX__CASE_ID_CASE_T__WO_FLOW_EXEC");
            db.execSQL("DROP INDEX IF EXISTS IDX__CASE_ID_CASE_T__WO_FLOW_EXEC");
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
            Log.i(TAG, "onUpgrade, dropping table " + WO_ATTACHMENTS);
            db.execSQL("DROP TABLE IF EXISTS " + WO_ATTACHMENTS);
            Log.i(TAG, "onUpgrade, dropping table " + WO_SYNCH_AUDIT);
            db.execSQL("DROP TABLE IF EXISTS " + WO_SYNCH_AUDIT);

            Log.i(TAG, "onUpgrade, dropping table " + LAST_SESSION);
            db.execSQL("DROP TABLE IF EXISTS " + LAST_SESSION);

            Log.i(TAG, "onUpgrade, dropping table " + WORKORDER_CACHE);
            db.execSQL("DROP TABLE IF EXISTS " + WORKORDER_CACHE);

            Log.i(TAG, "onUpgrade, dropping table " + WO_FLOW_EXEC);
            db.execSQL("DROP TABLE IF EXISTS " + WO_FLOW_EXEC);

            Log.i(TAG, "onUpgrade, dropping table " + WO_PDA_PAGE_T);
            db.execSQL("DROP TABLE IF EXISTS " + WO_PDA_PAGE_T);

            Log.i(TAG, "onUpgrade, dropping table " + WO_SAVE_STATE);
            db.execSQL("DROP TABLE IF EXISTS " + WO_SAVE_STATE);

            Log.i(TAG, "onUpgrade, dropping table " + WO_USER_PREFRENCE);
            db.execSQL("DROP TABLE IF EXISTS " + WO_USER_PREFRENCE);

            Log.i(TAG, "onUpgrade, dropping table " + HELP_DOCUMENT);
            db.execSQL("DROP TABLE IF EXISTS " + HELP_DOCUMENT);

            Log.i(TAG, "onUpgrade, dropping table " + TRANSLATION_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TRANSLATION_DATA);

            onCreate(db);
        } catch (Exception e) {
            writeLog(TAG + ": onUpgrade()", e);
        }
    }

    public void create(BaseTO obj) {
        DataTO dataTO = new DataTO();

        if (obj instanceof IdTO) {
            dataTO.setIdEntity(((IdTO) obj).getId());
        }
        if (obj instanceof IuTimestampTO) {
            dataTO.setIuTimestamp(((IuTimestampTO) obj).getIuTimestamp());
        }

        dataTO.setToClass(obj.getClass().getName());
        try {
            dataTO.setData(JSONMAPPER.writeValueAsString(obj));
        } catch (Exception e) {
            writeLog(TAG + ": create()", e);
        }
        create(dataTO);
    }

    private void create(DataTO dataTO) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_ID_ENTITY, dataTO.getIdEntity());
            values.put(KEY_IU_TIMESTAMP, (dataTO.getIuTimestamp() != null) ? dataTO.getIuTimestamp().getTime() : null);
            values.put(KEY_TO_CLASS, dataTO.getToClass());
            values.put(KEY_DATA, dataTO.getData());

            // Inserting Row
            db.insert(TABLE_DATA, null, values);
        } catch (Exception e) {
            writeLog(TAG + ": create()", e);
        }
    }

    // Getting single transfer object
    public <T extends BaseTO> T load(long idEntity, Class<T> clazz) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + KEY_DATA + " FROM " + TABLE_DATA + " WHERE " + KEY_ID_ENTITY + " = ? AND  " + KEY_TO_CLASS + " = ?",
                new String[]{String.valueOf(idEntity), clazz.getName()});


        try {
            if (cursor != null && cursor.moveToFirst()) {
                try {
                    return CommunicationHelper.JSONMAPPER.readValue(cursor.getString(0), clazz);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                return null;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    /**
     * @param clazz
     * @return Will return all TOs of type className
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public <T extends BaseTO> List<T> getAll(Class<T> clazz) {
        SQLiteDatabase db = this.getReadableDatabase();

        List<T> tos = new ArrayList<T>();
        try {
            Cursor cursor = db.rawQuery("SELECT " + KEY_DATA + " FROM " + TABLE_DATA + " WHERE " + KEY_TO_CLASS + " = ? ", new String[]{clazz.getName()});


            if (cursor != null && cursor.moveToFirst()) {
                do {
                    try {
                        tos.add(JSONMAPPER.readValue(cursor.getString(0), clazz));
                    } catch (Exception e) {
                        writeLog(TAG + ": getAll()", e);
                    }
                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            writeLog(TAG + ": getAll()", e);
        }
        return tos;
    }

    /**
     * Delete an entity
     *
     * @param idEntity
     * @param clazz
     */
    public <T extends BaseTO> void delete(long idEntity, Class<T> clazz) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_DATA,
                    KEY_ID_ENTITY + " = ? AND " +
                            KEY_TO_CLASS + " = ?",
                    new String[]{String.valueOf(idEntity), clazz.getName()});
        } catch (Exception e) {
            writeLog(TAG + ": delete()", e);
        }
    }

    /**
     * Delete all entities
     *
     * @param clazz Type to clear
     */
    public <T extends BaseTO> void delete(Class<T> clazz) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_DATA,
                    KEY_TO_CLASS + " = ?",
                    new String[]{clazz.getName()});
        } catch (Exception e) {
            writeLog(TAG + ": delete()", e);
        }
    }

    /**
     * Update an entity
     *
     * @return number of TOs updated, should be 1, otherwise you are in deep shit!
     */
    public <T extends IdTO> int update(T object) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ENTITY, object.getId());
        values.put(KEY_TO_CLASS, object.getClass().getName());

        if (object instanceof IuTimestampTO) {
            values.put(KEY_IU_TIMESTAMP, ((IuTimestampTO) object).getIuTimestamp().getTime());
        }
        try {
            values.put(KEY_DATA, CommunicationHelper.JSONMAPPER.writeValueAsString(object));
        } catch (Exception e) {
            writeLog(TAG + ": update()", e);

        }

        // updating row
        int rowsUpated = db.update(TABLE_DATA, values,
                KEY_ID_ENTITY + " = ? AND " +
                        KEY_TO_CLASS + " = ?",
                new String[]{object.getId().toString(), object.getClass().getName()});

        return rowsUpated;
    }

    /**
     * Will empty table
     */
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DATA, null, null);
    }

    /**
     * @return Number of rows in table
     */
    public int getRowCount() {
        String countQuery = "SELECT " + KEY_ID + " FROM " + TABLE_DATA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    /**
     * Return all workorderlto object (after unmarshalling from json string)
     *
     * @return List<WorkorderLiteTO>
     */


    public List<WorkorderLiteTO> getAllWorkorderLiteTos() {
        final List<WorkorderLiteTO> tos = new ArrayList<WorkorderLiteTO>();

        Cursor cursor = null;
        SQLiteDatabase readableDb = null;

        try {
            readableDb = getReadableDatabase();
            cursor = readableDb.query(WORKORDER_CACHE, new String[]{WORKORDERLTO_JSON}, USER_COMPLETED + "=0 ", null, null, null, null);

            while (cursor != null && cursor.moveToNext()) {
                try {
                    final String json = cursor.getString(cursor.getColumnIndex(WORKORDERLTO_JSON));
                    if (json != null) {
                        final WorkorderLiteTO lto = JSONMAPPER.readValue(json, WorkorderLiteTO.class);
                        if (lto != null) {
                            tos.add(lto);
                        }
                    }
                } catch (final Exception ex) {
                    writeLog(TAG + ": getAllWorkorderLiteTos", ex);
                }
            }

        } catch (final Exception ex) {
            writeLog(TAG + ": getAllWorkorderLiteTos", ex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tos;
    }

    /**
     * Return all WorkorderCustomWrapperTO object (after unmarshalling from json string)
     *
     * @return List<WorkorderCustomWrapperTO>
     */

    public <T extends WorkorderCustomWrapperTO> List<T> getAllWorkorderWrapperTos(Class<T> clazz) {
        final List<T> wrapperTos = new ArrayList<T>();

        Cursor cursor = null;
        SQLiteDatabase readableDb = null;

        try {
            readableDb = getReadableDatabase();
            cursor = readableDb.query(WORKORDER_CACHE, new String[]{WORKORDER_JSON}, USER_COMPLETED + "=0 ", null, null, null, null);

            while (cursor != null && cursor.moveToNext()) {
                try {
                    final String json = cursor.getString(cursor.getColumnIndex(WORKORDER_JSON));
                    if (json != null) {
                        final T lto = JSONMAPPER.readValue(json, clazz);
                        if (lto != null) {
                            wrapperTos.add(lto);
                        }
                    }
                } catch (final Exception ex) {
                    writeLog(TAG + ": getAllWorkorderWrapperTos", ex);
                }
            }

        } catch (final Exception ex) {
            writeLog(TAG + ": getAllWorkorderWrapperTos", ex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return wrapperTos;
    }

    public List<OrderSummaryCategory> getOrderSummaryCatInfo() {
        final LongSparseArray<OrderSummaryCategory> map = new LongSparseArray<OrderSummaryCategory>();
        final List<OrderSummaryCategory> ls = new ArrayList<OrderSummaryCategory>();

        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase();

            // Find number of wo, number of WO with time reservation start !=null  for distinct case type
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append(" SELECT ").append(ID_CASE_T).append(" , ").append(" COUNT(").append(ID_CASE_T).append(") as NUM ");
            queryBuilder.append(" FROM ").append(WORKORDER_CACHE);
            queryBuilder.append(" WHERE ").append(USER_COMPLETED).append("=0 ");
            queryBuilder.append(" GROUP BY ").append(ID_CASE_T);

            cursor = db.rawQuery(queryBuilder.toString(), null);
            while (cursor != null && cursor.moveToNext()) {
                long caseTypeId = cursor.getLong(cursor.getColumnIndex(ID_CASE_T));
                int numOfWo = cursor.getInt(cursor.getColumnIndex("NUM"));

                final OrderSummaryCategory cat = new OrderSummaryCategory();
                cat.setIdCaseT(caseTypeId);
                cat.setNumberOfWoCount(numOfWo);
                map.put(caseTypeId, cat);
            }

            if (cursor != null) {
                cursor.close();
            }

            /*
             *  select count(1) for distinct case type having time reservation start !=null
             *  and (timeReservationStart.getTime() - System.currentTimeMillis()) < ConstantsAstSep.THRESHOLD_TIME_RESERAVTION_WARNING);
             */
            queryBuilder = new StringBuilder();
            queryBuilder.append(" SELECT ").append(ID_CASE_T).append(" , ").append(" COUNT(").append(ID_CASE_T).append(") as NUM ");
            queryBuilder.append(" FROM ").append(WORKORDER_CACHE);
            queryBuilder.append(" WHERE ").append(TIME_RESERVATION_START).append(" IS NOT NULL ");
            queryBuilder.append(" AND ").append(USER_COMPLETED).append("=0 ");
            queryBuilder.append(" GROUP BY ").append(ID_CASE_T);

            cursor = db.rawQuery(queryBuilder.toString(), null);
            while (cursor != null && cursor.moveToNext()) {
                long caseTypeId = cursor.getLong(cursor.getColumnIndex(ID_CASE_T));
                int num = cursor.getInt(cursor.getColumnIndex("NUM"));

                //final OrderSummaryCategory cat = new OrderSummaryCategory();
                final OrderSummaryCategory cat = map.get(caseTypeId);
                if (cat != null) {
                    cat.setTimeReservationCount(num);
                }
            }

            if (cursor != null) {
                cursor.close();
            }

            // Get time reservation warning
            queryBuilder = new StringBuilder();
            queryBuilder.append(" SELECT ").append(ID_CASE_T); //.append(" , ").append(" COUNT(").append(TIME_RESERVATION_START_MILLIS).append(") as NUM ");
            queryBuilder.append(" FROM ").append(WORKORDER_CACHE);
            queryBuilder.append(" WHERE ").append(TIME_RESERVATION_START).append(" IS NOT NULL ");
            queryBuilder.append(" AND ").append(USER_COMPLETED).append("=0 ");
            queryBuilder.append(" AND ( ").append(TIME_RESERVATION_START_MILLIS).append(" - ").append(System.currentTimeMillis()).append(" ) ");
            queryBuilder.append(" < ").append(ConstantsAstSep.THRESHOLD_TIME_RESERAVTION_WARNING);

            cursor = db.rawQuery(queryBuilder.toString(), null);
            while (cursor != null && cursor.moveToNext()) {
                long caseTypeId = cursor.getLong(cursor.getColumnIndex(ID_CASE_T));
                final OrderSummaryCategory cat = map.get(caseTypeId);
                if (cat != null) {
                    cat.setTimeReservationWarning(true);
                }
            }
        } catch (final Exception ex) {
            writeLog(TAG + ": getOrderSummaryCatInfo()", ex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (map != null && map.size() > 0) {
            for (int i = 0; i < map.size(); i++) {
                ls.add(map.valueAt(i));
            }
        }

        return ls;
    }

    public void purgeWorkorderCache() {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.delete(WORKORDER_CACHE, null, null);
        } catch (final Exception ex) {
            writeLog(TAG + ": purgeWorkorderCache()", ex);
        }
    }

    /**
     * While downloading the workorder
     * this method would be called which would pre-process some workorder info
     * so that some heavy lifting stuffs are not required to perform again
     *
     * @param workorderCustomWrapperTos
     */
    public void storeWorkorderLiteTO(List<? extends WorkorderCustomWrapperTO> workorderCustomWrapperTos) {
        for (WorkorderCustomWrapperTO woCustomWrapperTo : workorderCustomWrapperTos) {
            if (woCustomWrapperTo.getOrderStatus().equals(WorkorderCustomWrapperTO.OrderStatus.DELETED)) {
                delete(woCustomWrapperTo);
            } else {
                saveWOLiteTO(woCustomWrapperTo);
            }
        }
    }


    private void delete(WorkorderCustomWrapperTO customWrapperTO) {
        SQLiteDatabase readOrWriteDb = null;
        try {
            readOrWriteDb = getWritableDatabase();
            readOrWriteDb.delete(WORKORDER_CACHE, ID_CASE + "=?",
                    new String[]{String.valueOf(customWrapperTO.getIdCase())});
        } catch (final Exception ex) {
            writeLog(TAG + ": delete()", ex);
        }
    }

    /**
     * Will be used by Workorder Cahe
     *
     * @param customWrapperTO
     */
    public <T extends WorkorderCustomWrapperTO> void saveWOLiteTO(T customWrapperTO) {
        SQLiteDatabase readOrWriteDb = null;
        try {
            final WorkorderLiteTO liteTo = generateWorkorderLiteTo(customWrapperTO);
            if (liteTo != null) {
                final ContentValues contentValues = new ContentValues();
                contentValues.put(ID_CASE, customWrapperTO.getIdCase());
                contentValues.put(ID_CASE_T, customWrapperTO.getIdCaseType());

                if (liteTo.getPriority() != null) {
                    contentValues.put(PRIORITY, liteTo.getPriority());
                }
                if (liteTo.getInstCode() != null) {
                    contentValues.put(INST_CODE, liteTo.getInstCode());
                }
                if (liteTo.getKeyInfo() != null) {
                    contentValues.put(KEY_INFO, liteTo.getKeyInfo());
                }
                if (liteTo.getKeyNumber() != null) {
                    contentValues.put(KEY_NUMBER, liteTo.getKeyNumber());
                }
                if (liteTo.getAddressStreet() != null) {
                    contentValues.put(ADDRESS_STREET, liteTo.getAddressStreet());
                }
                if (liteTo.getAddressCity() != null) {
                    contentValues.put(ADDRESS_CITY, liteTo.getAddressCity());
                }
                if (liteTo.getCustomerName() != null) {
                    contentValues.put(CUSTOMER_NAME, liteTo.getCustomerName());
                }
                if (liteTo.getSlaDeadline() != null) {
                    contentValues.put(SLA_DEADLINE, sdf.format(liteTo.getSlaDeadline()));
                }
                if (liteTo.getTimeReservationStart() != null) {
                    final Date stDate = liteTo.getTimeReservationStart();
                    contentValues.put(TIME_RESERVATION_START, sdf.format(stDate));
                    contentValues.put(TIME_RESERVATION_START_MILLIS, stDate.getTime());
                }
                if (liteTo.getTimeReservationEnd() != null) {
                    contentValues.put(TIME_RESERVATION_END, sdf.format(liteTo.getTimeReservationEnd()));
                }
                if (liteTo.isStarted()) {
                    contentValues.put(STARTED, 1);
                } else {
                    contentValues.put(STARTED, 0);
                }
                if (liteTo.isAssigned()) {
                    contentValues.put(ASSIGNED, 1);
                } else {
                    contentValues.put(ASSIGNED, 0);
                }

                contentValues.put(ANTENNA_ID_UNIT_MODEL, liteTo.getAntIdUnitModel());
                contentValues.put(ID_UNIT_MODEL_CFT_T, liteTo.getIdUnitModelCfgT());

                if (liteTo.getMeterWoUnitTO() != null) {
                    final String woUnitToJson = JSONMAPPER.writeValueAsString(liteTo.getMeterWoUnitTO());
                    contentValues.put(WO_UNIT_TO_JSON, woUnitToJson);
                }

                final String entireLtoJson = JSONMAPPER.writeValueAsString(liteTo);
                contentValues.put(WORKORDERLTO_JSON, entireLtoJson);
                final String entireWrapperJson = JSONMAPPER.writeValueAsString(customWrapperTO);
                contentValues.put(WORKORDER_JSON, entireWrapperJson);

                // Delete and insert (This section can be improved later)
                readOrWriteDb = getWritableDatabase();
                readOrWriteDb.delete(WORKORDER_CACHE, ID_CASE + "=? AND " + ID_CASE_T + "=? AND " + USER_COMPLETED + "=0 ",
                        new String[]{String.valueOf(customWrapperTO.getIdCase()), String.valueOf(customWrapperTO.getIdCaseType())});
                readOrWriteDb.insert(WORKORDER_CACHE, null, contentValues);
            }
        } catch (final Exception ex) {
            writeLog(TAG + ": saveWOLiteTO()", ex);
        }
    }


    public List<WorkorderLiteTO> getWorkOrdersByType(final Long idCaseT) {
        final List<WorkorderLiteTO> woList = new ArrayList<WorkorderLiteTO>();
        if (idCaseT > 0) {
            SQLiteDatabase readableDb = null;
            Cursor cursor = null;
            try {
                readableDb = getReadableDatabase();
                cursor = readableDb.query(WORKORDER_CACHE,
                        new String[]{WORKORDERLTO_JSON}, ID_CASE_T + "=? AND " + USER_COMPLETED + "=0 ", new String[]{String.valueOf(idCaseT)}, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    try {
                        final String json = cursor.getString(cursor.getColumnIndex(WORKORDERLTO_JSON));
                        if (json != null) {
                            final WorkorderLiteTO to = JSONMAPPER.readValue(json, WorkorderLiteTO.class);
                            if (to != null) {
                                woList.add(to);
                            }
                        }
                    } catch (final Exception ex) {
                        // Unmarshall exception
                        writeLog(TAG + ": getWorkOrdersByType()", ex);
                    }
                }

            } catch (final Exception ex) {
                writeLog(TAG + ": getWorkOrdersByType()", ex);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return woList;
    }

    /**
     * Get complete WorkorderCustomWrapperTo object
     * based on the provided case id
     *
     * @param idCase
     * @return WorkorderCustomWrapperTO
     */

    public <T extends IdTO, WorkorderCustomWrapperTO> T getSpecificWorkorder(final long idCase, Class<T> clazz) {
        T wo = null;
        if (idCase > 0) {
            SQLiteDatabase db = null;
            Cursor cursor = null;

            try {
                db = getReadableDatabase();
                cursor = db.query(WORKORDER_CACHE, new String[]{WORKORDER_JSON}, ID_CASE + "=? AND " + USER_COMPLETED + "=0 ",
                        new String[]{String.valueOf(idCase)}, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    final String json = cursor.getString(cursor.getColumnIndex(WORKORDER_JSON));
                    if (json != null) {
                        wo = JSONMAPPER.readValue(json, clazz);
                    }
                }
            } catch (final Exception ex) {
                writeLog(TAG + " : getSpecificWorkorder()", ex);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return wo;
    }


    public List<WorkorderLiteTO> getTimeReservedWorkOrdersByType(final Long idCaseT) {
        final List<WorkorderLiteTO> woList = new ArrayList<WorkorderLiteTO>();
        if (idCaseT > 0) {
            SQLiteDatabase readableDb = null;
            Cursor cursor = null;
            try {
                readableDb = getReadableDatabase();
                cursor = readableDb.query(WORKORDER_CACHE,
                        new String[]{WORKORDERLTO_JSON}, ID_CASE_T + "=? AND TIME_RESERVATION_START IS NOT NULL AND " + USER_COMPLETED + "=0 ",
                        new String[]{String.valueOf(idCaseT)}, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    try {
                        final String json = cursor.getString(cursor.getColumnIndex(WORKORDERLTO_JSON));
                        if (json != null) {
                            final WorkorderLiteTO to = JSONMAPPER.readValue(json, WorkorderLiteTO.class);
                            if (to != null) {
                                woList.add(to);
                            }
                        }
                    } catch (final Exception ex) {
                        // Unmarshal exception
                        writeLog(TAG + ": getTimeReservedWorkOrdersByType()", ex);
                    }
                }
            } catch (final Exception ex) {
                writeLog(TAG + ": getTimeReservedWorkOrdersByType()", ex);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return woList;
    }

    public List<WorkorderLiteTO> getPlannedWorkOrdersByType(final Long ppId, final Long idCaseT, final boolean isTimeReserved) {
        final List<WorkorderLiteTO> woList = new ArrayList<WorkorderLiteTO>();
        if (idCaseT > 0) {
            SQLiteDatabase readableDb = null;
            Cursor cursor = null;
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append(ID_CASE_T);
            queryBuilder.append("=? AND ");
            if (isTimeReserved) {
                queryBuilder.append("TIME_RESERVATION_START IS NOT NULL AND ");
            }
            queryBuilder.append(USER_COMPLETED + "=0 ");
            try {
                readableDb = getReadableDatabase();
                cursor = readableDb.query(WORKORDER_CACHE,
                        new String[]{WORKORDERLTO_JSON}, queryBuilder.toString(),
                        new String[]{String.valueOf(idCaseT)}, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    try {
                        final String json = cursor.getString(cursor.getColumnIndex(WORKORDERLTO_JSON));
                        if (json != null) {
                            final WorkorderLiteTO to = JSONMAPPER.readValue(json, WorkorderLiteTO.class);
                            if (to != null && (to.getIdPlanningPeriod().equals(ppId))) {
                                woList.add(to);
                            }
                        }
                    } catch (final Exception ex) {
                        // Unmarshal exception
                        writeLog(TAG + ": getPlannedWorkOrdersByType()", ex);
                    }
                }
            } catch (final Exception ex) {
                writeLog(TAG + ": getPlannedWorkOrdersByType()", ex);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return woList;
    }


    /**
     * Extract limited values from WorkorderCustomWrapperTo to get WorkorderLiteTo
     * object, which would be stored in dedicated sqlite table for faster processing
     *
     * @param srcWo
     * @return
     */


    public WorkorderLiteTO generateWorkorderLiteTo(final WorkorderCustomWrapperTO srcWo) {

        final WorkorderLiteTO woLite = new WorkorderLiteTO();
        final WoAddressTO woAddressTO = srcWo.getWorkorderCustomTO().getWoInst().getAddress();
        CaseTCustomTO caseTCustomTO = ObjectCache.getIdObject(CaseTCustomTO.class, srcWo.getIdCaseType());
        final String addressStreet = WorkorderUtils.getWoAddressStreetPostalCodeInfoText(woAddressTO, caseTCustomTO);
        Log.d("DatabaseHandler", addressStreet);
        final String addressCity = Utils.safeToString(
                " ",
                getValidOrgDivValue(woAddressTO, WoAddressTO.POSTCODE_O),
                getValidOrgDivValue(woAddressTO, WoAddressTO.CITY_O)
        );
        woLite.setAddressStreet(addressStreet);
        woLite.setAddressCity(addressCity);
        woLite.setAssigned(srcWo.getAssignedToTeam());
        woLite.setPriority(srcWo.getPriority());
        woLite.setPriorityShortName(srcWo.getPriorityShortName());
        woLite.setIdCaseT(srcWo.getIdCaseType());
        woLite.setId(srcWo.getIdCase());
        woLite.setIdPlanningPeriod(srcWo.getIdPlanningPeriod());
        woLite.setNetStationId(WorkorderUtils.getNetstation(srcWo));
        final WoInstCustomTO woInstCustomTO = srcWo.getWorkorderCustomTO().getWoInst();
        if (woInstCustomTO != null) {
            final WoInstTO woInstTO = woInstCustomTO.getWoInstTO();
            woLite.setInstCode(woInstTO.getInstCode());

            final List<WoContactCustomTO> woContactTOs = new ArrayList<WoContactCustomTO>();
            woContactTOs.addAll(Utils.safeToList(woInstCustomTO.getInstMeasurepoint().getContacts()));
            woContactTOs.addAll(Utils.safeToList(woInstCustomTO.getContacts()));

            // customer name
            for (final WoContactCustomTO woContactCustomTO : woContactTOs) {
                WoContactTO woContactTO = woContactCustomTO.getWoContactTO();
                if (woContactTO.getIdWoContactT().equals(CONSTANTS().WO_CONTACT_T__EXISTING_CUSTOMER)) {
                    String name = Utils.safeToString(
                            " ",
                            getValidOrgDivValue(woContactTO, WoContactTO.FIRST_NAME_D),
                            getValidOrgDivValue(woContactTO, WoContactTO.LAST_NAME_D)
                    );
                    woLite.setCustomerName(name);
                    break;
                }
            }

            // keys
            final String keyNumber = getValidOrgDivValue(woInstTO, WoInstTO.KEY_NUMBER_D);
            final String keyInfo = getValidOrgDivValue(woInstTO, WoInstTO.KEY_INFO_D);
            woLite.setKeyNumber(Utils.safeToString(keyNumber).trim());
            woLite.setKeyInfo(Utils.safeToString(keyInfo).trim());
        }


        // find meter unit
        for (WoUnitCustomTO woUnitCustomTO : Utils.safeToList(srcWo.getWorkorderCustomTO().getWoUnits())) {
            if (woUnitCustomTO.getIdWoUnitT().equals(CONSTANTS().WO_UNIT_T__METER)) {
                WoUnitTO woUnitTO = Mapper.map(woUnitCustomTO, WoUnitTO.class);
                woLite.setMeterWoUnitTO(woUnitTO);
                break;
            }
        }

        // started
        if (srcWo.getLockUserName() != null) {
            woLite.setStarted(true);
        } else {
            woLite.setStarted(false);
        }
        for (WoEventCustomTO event : Utils.safeToList(srcWo.getWorkorderCustomTO().getWoEvents())) {
            Long idWoEventT = event.getWoEventTO().getIdWoEventT();
            if (idWoEventT.equals(CONSTANTS().WO_EVENT_T__TECHNICAL_PLANNING)) {
                WoEventTechPlanTO techPlanTO = event.getTechnicalPlanning();
                woLite.setAntIdUnitModel(techPlanTO.getAntIdUnitModel());
                woLite.setIdUnitModelCfgT(techPlanTO.getIdUnitModelCfgT());
            }
        }

        woLite.setTimeReservationStart(srcWo.getTimeReservationStart());
        woLite.setTimeReservationEnd(srcWo.getTimeReservationEnd());
        woLite.setSlaDeadline(srcWo.getSlaDeadline());

        return woLite;
    }

    private WoAuditLogBean getMaxSynchAttemptLog(final long caseId,
                                                 final long caseTypeId) {
        WoAuditLogBean woAuditLogBean = null;

        SQLiteDatabase readOnlyDb = null;
        Cursor cursor = null;
        try {
            readOnlyDb = getReadableDatabase();

            final String[] selectCols = new String[]{
                    ID_KEY,
                    ID_CASE, ID_CASE_T,
                    SYNCH_ATTEMPT_NUM,
                    SYNCH_START_DATETIME,
                    SYNCH_END_DATETIME,
                    SUCCESSFULLY_COMPLETED,
                    ERROR_INFO};

            final StringBuilder selectionClause = new StringBuilder();
            selectionClause.append(ID_CASE).append("=?");
            selectionClause.append(" AND ");
            selectionClause.append(ID_CASE_T).append("=?");

            final String[] selArgs = new String[]{String.valueOf(caseId), String.valueOf(caseTypeId)};

            cursor = readOnlyDb.query(WO_SYNCH_AUDIT, selectCols, selectionClause.toString(), selArgs, null, null, ID_KEY + " DESC");
            if (cursor != null && cursor.moveToFirst()) {

                woAuditLogBean = new WoAuditLogBean();
                woAuditLogBean.setCaseId(caseId);
                woAuditLogBean.setCaseTypeId(caseTypeId);

                // Db Primary Key id
                final Integer idVal = cursor.getInt(cursor.getColumnIndex(ID_KEY));

                if (idVal != null) {
                    woAuditLogBean.setId(idVal);
                }

                // Synch attempt number for this record
                final Integer synchAttempNum = cursor.getInt(cursor.getColumnIndex(SYNCH_ATTEMPT_NUM));
                if (synchAttempNum != null) {
                    woAuditLogBean.setSynchAttemptNum(synchAttempNum);
                }

                // Synch start date time
                final String synchStartDtStr = cursor.getString(cursor.getColumnIndex(SYNCH_START_DATETIME));
                if (!Utils.safeToString(synchStartDtStr).equals("")) {
                    woAuditLogBean.setStartDate(sdf.parse(synchStartDtStr));
                }

                // Synch end date time
                final String synchEndDtStr = cursor.getString(cursor.getColumnIndex(SYNCH_END_DATETIME));
                if (!Utils.safeToString(synchEndDtStr).equals("")) {
                    woAuditLogBean.setEndDate(sdf.parse(synchEndDtStr));
                }

                // Sych completion status
                final String status = cursor.getString(cursor.getColumnIndex(SUCCESSFULLY_COMPLETED));
                if (!Utils.safeToString(status).equals("")) {
                    woAuditLogBean.setStatus(Boolean.parseBoolean(status));
                }

                final String errorInfo = cursor.getString(cursor.getColumnIndex(ERROR_INFO));
                if (!Utils.safeToString(errorInfo).equals("")) {
                    woAuditLogBean.setErrorInfo(errorInfo);
                }
            }


        } catch (final Exception ex) {
            writeLog(TAG + ": getMaxSynchAttemptLog", ex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }
        return woAuditLogBean;
    }

    /**
     * When the user has completed the workorder
     * that should be marked in the db, to prevent it getting displayed again
     * in the order summary page
     *
     * @param caseId     (Case Id of the work-order)
     * @param caseTypeId (Case type id of the work-order)
     */

    public void markWoInSynchState(final long caseId, final long caseTypeId) {
        if (caseId > 0 && caseTypeId > 0) {
            // Do it using raw query
            final StringBuilder builder = new StringBuilder();
            builder.append(" UPDATE ").append(WORKORDER_CACHE);
            builder.append(" SET ").append(USER_COMPLETED).append(" = 1 "); //.append(TOTAL_SYNCH_ATTEMPT).append("+1)");
            //builder.append(" , ");
            //builder.append(LAST_UPDATE_TS).append(" = CURRENT_TIMESTAMP ");
            builder.append(" WHERE ").append(ID_CASE).append(" = ").append(caseId);
            builder.append(" AND ").append(ID_CASE_T).append(" = ").append(caseTypeId);

            Log.d(TAG, "Executing Query=" + builder.toString());

            SQLiteDatabase writableDb = null;
            try {
                writableDb = getWritableDatabase();
                writableDb.execSQL(builder.toString());
            } catch (final Exception ex) {
                writeLog(TAG + ": markWoInSynchState()", ex);
            }
        }
    }


    /**
     * This method does the following
     * <p>
     * 1. Check for the given case id and case type id
     * if any workorder audit already present in  WO_SYNCH_AUDIT table
     *
     * @param caseId
     * @param caseTypeId
     * @param startDate
     * @param endDate
     * @param sycnhStatus
     * @return
     */


    public WoAuditLogBean addOrUpdateWoSychAuditLog(final Long auditPkId, final long caseId,
                                                    final long caseTypeId, final Date startDate, final Date endDate,
                                                    final Boolean sycnhStatus, final String errorInfo) {

        WoAuditLogBean woAuditLogBean = null;
        SQLiteDatabase writableDb = null;

        final ContentValues contentValues = new ContentValues();
        contentValues.put(ID_CASE, caseId);
        contentValues.put(ID_CASE_T, caseTypeId);

        if (startDate != null) {
            contentValues.put(SYNCH_START_DATETIME, sdf.format(startDate));
        }

        if (endDate != null) {
            contentValues.put(SYNCH_END_DATETIME, sdf.format(endDate));
        }

        if (sycnhStatus != null) {
            contentValues.put(SUCCESSFULLY_COMPLETED, sycnhStatus);
        }

        try {
            writableDb = getWritableDatabase();

            if (auditPkId != null && auditPkId > 0) {
                // Update the data
                writableDb.update(WO_SYNCH_AUDIT, contentValues,
                        ID_KEY + "=?", new String[]{String.valueOf(auditPkId)});

            } else {
                int attemptNum = 1;
                woAuditLogBean = getMaxSynchAttemptLog(caseId, caseTypeId);

                if (woAuditLogBean != null) {
                    attemptNum = woAuditLogBean.getSynchAttemptNum() + 1;
                    contentValues.put(SYNCH_ATTEMPT_NUM, attemptNum);
                }

                long pk = writableDb.insert(WO_SYNCH_AUDIT, null, contentValues);


                if (woAuditLogBean == null) {
                    woAuditLogBean = new WoAuditLogBean();
                }

                woAuditLogBean.setId(pk);
                woAuditLogBean.setCaseId(caseId);
                woAuditLogBean.setCaseTypeId(caseTypeId);
                woAuditLogBean.setStartDate(startDate);
                woAuditLogBean.setEndDate(endDate);
                woAuditLogBean.setSynchAttemptNum(attemptNum);

                if (sycnhStatus != null) {
                    woAuditLogBean.setStatus(sycnhStatus);
                }
            }
        } catch (final Exception ex) {
            writeLog(TAG + ": addOrUpdateWoSychAuditLog()", ex);
        }

        return woAuditLogBean;
    }

    // ----- Methods applicable for workorder related functionalities ----

    public List<String> getVisitedPagesListForCase(final long caseId) {
        List<String> visitedPageList = new LinkedList<String>();
        if (caseId > 0) {
            SQLiteDatabase db = null;
            Cursor cursor = null;

            try {
                db = getReadableDatabase();
                cursor = db.query(WO_FLOW_EXEC, new String[]{PAGE_ID, PAGE_INDEX},
                        ID_CASE + "=? ", new String[]{String.valueOf(caseId)}, null, null, PAGE_INDEX);
                while (cursor != null && cursor.moveToNext()) {
                    visitedPageList.add(cursor.getString(cursor.getColumnIndex(PAGE_ID)));
                }
            } catch (final Exception ex) {
                writeLog(TAG + ": getVisitedPagesListForCase()", ex);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return visitedPageList;
    }

    /**
     * Fetch the number of incomplete work orders
     *
     * @return
     */
    public int getIncompleteWorkOrdersCount() {
        Log.d(TAG, " Getting number of incomplete workorders");
        SQLiteDatabase readableDb = null;
        int numberOfIncompleteWorkorders = 0;

        try {
            readableDb = getReadableDatabase();

            final StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append(" SELECT ");
            queryBuilder.append(" COUNT(DISTINCT ID_CASE) AS INCOMPLETE_WO_COUNT ");
            queryBuilder.append(" FROM ").append(WO_FLOW_EXEC);

            Log.d(TAG, "SQLlite Query : " + queryBuilder.toString());
            final Cursor cursor = readableDb.rawQuery(queryBuilder.toString(), null);
            while (cursor != null && cursor.moveToNext()) {
                numberOfIncompleteWorkorders = cursor.getInt(cursor.getColumnIndex("INCOMPLETE_WO_COUNT"));
            }
            if (cursor != null) {
                cursor.close();
            }

        } catch (final Exception ex) {
            writeLog(TAG + ": getIncompleteWorkOrdersCount()", ex);
        }
        return numberOfIncompleteWorkorders;
    }

    public String getVisitedPagesForCase(final long caseId, final long pageIndex) {
        String visitedPage = null;
        if (caseId > 0) {
            SQLiteDatabase db = null;
            Cursor cursor = null;

            try {
                db = getReadableDatabase();
                cursor = db.query(WO_FLOW_EXEC, new String[]{PAGE_ID},
                        ID_CASE + "=? AND " + PAGE_INDEX + "=?", new String[]{String.valueOf(caseId), String.valueOf(pageIndex)}, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    visitedPage = cursor.getString(cursor.getColumnIndex(PAGE_ID));
                }
            } catch (final Exception ex) {
                writeLog(TAG + ": getVisitedPagesForCase()", ex);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return visitedPage;
    }


    /**
     * Get Modified wo object from Flow Execution cache
     *
     * @param caseId
     * @param pageIndex
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends WorkorderCustomWrapperTO> T getModifiedWOWrapperFromFlow(final long caseId, final long pageIndex, Class<T> clazz) {
        T wo = null;
        if (caseId > 0) {
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try {
                db = getReadableDatabase();
                cursor = db.query(WO_FLOW_EXEC, new String[]{WORKORDER_JSON},
                        ID_CASE + "=?  AND " + PAGE_INDEX + "= ?", new String[]{String.valueOf(caseId), String.valueOf(pageIndex)}, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    final String json = cursor.getString(cursor.getColumnIndex(WORKORDER_JSON));
                    if (json != null) {
                        wo = JSONMAPPER.readValue(json, clazz);
                    }
                }
            } catch (Exception ex) {
                writeLog(TAG + ": getModifiedWOWrapperFromFlow()", ex);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

        }
        return wo;
    }

    /**
     * Get Abort table for the page from Flow Execution cache
     *
     * @param caseId
     * @param pageIndex
     * @return
     */
    public AbortUtil getAbortUtilsFromFlow(final long caseId, final long pageIndex) {
        AbortUtil abortTable = null;
        Log.d(TAG, "FETCHING ABORT UTIL FOR ID_CASE:" + caseId + ", PAGE_INDEX:" + pageIndex);
        if (caseId > 0) {
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try {
                db = getReadableDatabase();
                cursor = db.query(WO_FLOW_EXEC, new String[]{ABORT_UTIL},
                        ID_CASE + "=?  AND " + PAGE_INDEX + "= ?", new String[]{String.valueOf(caseId), String.valueOf(pageIndex)}, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    final String json = cursor.getString(cursor.getColumnIndex(ABORT_UTIL));
                    if (json != null) {
                        abortTable = JSONMAPPER.readValue(json, AbortUtil.class);
                    }
                }
            } catch (Exception ex) {
                writeLog(TAG + ": getAbortUtilsFromFlow()", ex);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return abortTable;
    }

    /**
     * Fetch PDA Page Type for the WO
     *
     * @param caseId
     * @return
     */
    public Long getPdaPageTforCase(final long caseId, final long pageIndex) {
        Long idPdaPageT = null;
        if (caseId > 0) {
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try {
                db = getReadableDatabase();
                cursor = db.query(WO_FLOW_EXEC, new String[]{ID_PDA_PAGE_T},
                        ID_CASE + "=?  AND " + PAGE_INDEX + "= ?", new String[]{String.valueOf(caseId), String.valueOf(pageIndex)}, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    idPdaPageT = cursor.getLong(cursor.getColumnIndex(ID_PDA_PAGE_T));
                    Log.d(TAG, "cursor.getColumnIndex(ID_PDA_PAGE_T) :: " + cursor.getColumnIndex(ID_PDA_PAGE_T));
                }
            } catch (Exception ex) {
                writeLog(TAG + ": getPdaPageTforCase()", ex);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

        }
        Log.d(TAG, "FETCHED CASE ID :: " + caseId + ", PDA PAGE TYPE :: " + idPdaPageT);
        return idPdaPageT;
    }


    /**
     * CLEAR PAGE VISITS FOR CASE ID
     *
     * @param caseId
     */
    public void clearPageVisitsForCase(final long caseId) {
        if (caseId > 0) {
            SQLiteDatabase db = getReadableDatabase();
            String whereclause = ID_CASE + "=?";
            String[] whereArgs = new String[]{String.valueOf(caseId)};
            db.delete(WO_FLOW_EXEC, whereclause, whereArgs);
            db.delete(WO_USER_PREFRENCE, whereclause, whereArgs);
        }
    }

    /**
     * CLEAR PAGE VISITS FOR PAGE INDEX AND CASE ID
     *
     * @param caseId
     * @param pageIndex
     */
    public void clearWoFlowExecForPageIndex(final long caseId, final long pageIndex) {
        if (caseId > 0) {
            SQLiteDatabase db = getReadableDatabase();
            String whereclause = ID_CASE + "=? AND " + PAGE_INDEX + ">=?";
            String[] whereArgs = new String[]{String.valueOf(caseId), String.valueOf(pageIndex)};
            db.delete(WO_FLOW_EXEC, whereclause, whereArgs);
        }
        Log.d(TAG, "WO_FLOW_EXEC cleared for ID_CASE = " + caseId + " AND PAGE_INDEX >=" + pageIndex);
    }

    /**
     * CLEAR PAGE PREFERENCE VALUES FOR PAGE INDEX AND CASE ID
     *
     * @param caseId
     * @param pageIndex
     */
    public void clearWoUserPreference(final long caseId, final long pageIndex) {
        if (caseId > 0) {
            SQLiteDatabase db = getReadableDatabase();
            String whereclause = ID_CASE + "=? AND " + PAGE_INDEX + ">=?";
            String[] whereArgs = new String[]{String.valueOf(caseId), String.valueOf(pageIndex)};
            db.delete(WO_USER_PREFRENCE, whereclause, whereArgs);
        }
        Log.d(TAG, "WO_USER_PREFRENCE cleared for ID_CASE = " + caseId + " AND PAGE_INDEX >=" + pageIndex);
    }

    /**
     * FETCH PAGE PREFERENCE VALUES FOR PAGE INDEX AND CASE ID
     *
     * @param caseId
     * @param pageIndex
     * @return
     */
    public List<String> fetchPreferenceKeysForPage(final long caseId, final long pageIndex) {
        List<String> userPreferenceKeys = new ArrayList<String>();
        if (caseId > 0) {
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try {
                db = getReadableDatabase();
                cursor = db.query(WO_USER_PREFRENCE, new String[]{USER_PREFRENCE_KEY},
                        ID_CASE + "=?  AND " + PAGE_INDEX + " = ?", new String[]{String.valueOf(caseId), String.valueOf(pageIndex)}, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    userPreferenceKeys.add(cursor.getString(cursor.getColumnIndex(USER_PREFRENCE_KEY)));

                }
            } catch (Exception ex) {
                writeLog(TAG + " : fetchPreferenceKeysForPage()", ex);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

        }
        return userPreferenceKeys;
    }


    /**
     * Utility DAO method to associate an attachment file
     * (could be of any type image, text file , doc etc)
     * with an work order
     *
     * @param caseId         {@link Integer}
     * @param attachmentPath {@link String}
     */

    public long addAttachment(final long caseId, final long caseTypeId, final String fieldVisitId,
                              final String attachmentPath, final Long attachmentMimeTypeId, final List<Long> reasonTypeIds) {


        long id = -1;

        // Validate the provided attachment and case id
        if (caseId > 0 && !"".equals(attachmentPath)
                && new File(attachmentPath).exists()) {
            boolean save = true;
            // No need add the same attachment twice.. check if exists
            final List<AndroidWOAttachmentBean> existingAttachments = getAttachment(caseId, caseTypeId, attachmentMimeTypeId, null);
            if (existingAttachments != null && !existingAttachments.isEmpty()) {
                for (final AndroidWOAttachmentBean bean : existingAttachments) {
                    if (bean != null && bean.getFileName() != null
                            && bean.getFileName().equals(attachmentPath)) {
                        save = false;
                        break;
                    }
                }
            }

            if (save) {
                Log.d(TAG, "Saving attachment for case id=" + caseId + ",castypeid=" + caseTypeId);
                id = saveAttachment(caseId, caseTypeId, fieldVisitId, attachmentPath, attachmentMimeTypeId, reasonTypeIds);
            } else {
                Log.d(TAG, "Removing old attachment for case id=" + caseId + ",castypeid=" + caseTypeId);
                removeAttchment(caseId, caseTypeId, attachmentPath, attachmentMimeTypeId, null);
                Log.d(TAG, "Inserting new attachment for case id=" + caseId + ",castypeid=" + caseTypeId);
                id = saveAttachment(caseId, caseTypeId, fieldVisitId, attachmentPath, attachmentMimeTypeId, reasonTypeIds);
            }
        }
        return id;
    }

    /**
     * Returns the list of attachments associated
     * with this work order
     *
     * @param caseId {@link Integer}
     * @return List<String>
     */

    public List<AndroidWOAttachmentBean> getAttachment(final long caseId,
                                                       final long caseTypeId, final long attachmentMimeTypeId, final List<Long> reasonTypeIds) {

        final List<AndroidWOAttachmentBean> attachments = new ArrayList<AndroidWOAttachmentBean>();
        SQLiteDatabase readableDb = null;


        try {
            readableDb = getReadableDatabase();

            final StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append(" SELECT ").append(WO_ATTACHMENTS).append(".").append(ATTACHMENT_PATH);
            queryBuilder.append(" , ").append(FIELD_VISIT_ID);
            queryBuilder.append(" , ").append(ATTACHMENT_REASON_TYPE_ID);
            queryBuilder.append(" , ").append(ATTACHMENT_MIME_TYPE_ID);
            queryBuilder.append(" FROM ").append(WO_ATTACHMENTS);
            queryBuilder.append(" WHERE ").append(ID_CASE).append("=").append(caseId);
            if (Utils.isNotEmpty(reasonTypeIds)) {
                queryBuilder.append(" AND ").append(WO_ATTACHMENTS).append(".").append(ATTACHMENT_REASON_TYPE_ID).append(" = ").append(AndroidUtilsAstSep.convertLongListToDelimitedString(reasonTypeIds, ","));
            }

            if (attachmentMimeTypeId != 0) {
                queryBuilder.append(" AND ").append(WO_ATTACHMENTS).append(".").append(ATTACHMENT_MIME_TYPE_ID).append(" = ").append(attachmentMimeTypeId);
            }

            Log.d(TAG, "SQLlite Query : " + queryBuilder.toString());
            final Cursor cursor = readableDb.rawQuery(queryBuilder.toString(), null);
            while (cursor != null && cursor.moveToNext()) {
                Log.d(TAG, "Creating new AndroidWOAttachmentBean");
                AndroidWOAttachmentBean bean = new AndroidWOAttachmentBean();
                bean.setFileName(cursor.getString(cursor.getColumnIndex(ATTACHMENT_PATH)));
                bean.setCaseId(caseId);
                bean.setFieldVisitId(cursor.getString(cursor.getColumnIndex(FIELD_VISIT_ID)));
                bean.setCaseTypeId(caseTypeId);
                bean.setReasonTypeId(cursor.getString(cursor.getColumnIndex(ATTACHMENT_REASON_TYPE_ID)));
                bean.setAttachmentTypeId(cursor.getLong(cursor.getColumnIndex(ATTACHMENT_MIME_TYPE_ID)));

                // Populate AttachmentReasonTTO
                if (Utils.isNotEmpty(bean.getReasonTypeId())) {
                    // Try to get the actual object
                    List<Long> reasonIds = AndroidUtilsAstSep.convertDelimitedStringToLongList(bean.getReasonTypeId(), ",");
                    final List<AttachmentReasonTTO> reasonTTOs = new LinkedList<AttachmentReasonTTO>();
                    for (Long reasonId : reasonIds) {
                        final AttachmentReasonTTO reasonTTO = ObjectCache.getType(AttachmentReasonTTO.class, reasonId);
                        reasonTTOs.add(reasonTTO);
                    }
                    bean.setAttachmentReasonTTO(reasonTTOs);
                }

                // Populate AttachmentMimeTTO
                if (bean.getAttachmentTypeId() > 0) {
                    // Try to get the actual object
                    final AttachmentTTO mimeTto = ObjectCache.getType(AttachmentTTO.class, bean.getAttachmentTypeId());
                    if (mimeTto != null) {
                        bean.setAttachmentTTO(mimeTto);
                    }
                }
                attachments.add(bean);
            }

            if (cursor != null) {
                cursor.close();
            }

        } catch (final Exception e) {
            writeLog(TAG + " : getAttachment()", e);
        }

        return attachments;
    }


    /**
     * Add the given attachment path to sqlite database
     * for the corresponding work order
     * <p>
     * Returns boolean to indicate success/failure
     *
     * @param caseId         {@link Integer}
     * @param attachmentPath {@link String}
     * @return {@link Boolean}
     */
    private long saveAttachment(final long caseId, final long caseTypeId, final String fieldVisitId,
                                final String attachmentPath, final long attachmentMimeTypeId, final List<Long> reasonTypeIds) {


        long id = -1;
        // Trusting the arguments as being called internally post basic validation
        final ContentValues contentValues = new ContentValues();
        contentValues.put(ID_CASE, caseId);
        contentValues.put(ATTACHMENT_PATH, attachmentPath);
        if (Utils.isNotEmpty(reasonTypeIds)) {
            contentValues.put(ATTACHMENT_REASON_TYPE_ID, AndroidUtilsAstSep.convertLongListToDelimitedString(reasonTypeIds, ","));
        }
        if (attachmentMimeTypeId != 0) {
            contentValues.put(ATTACHMENT_MIME_TYPE_ID, attachmentMimeTypeId);
        }
        if (fieldVisitId != null && !"".equals(fieldVisitId)) {
            contentValues.put(FIELD_VISIT_ID, fieldVisitId);
        }

        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            id = db.insert(WO_ATTACHMENTS, null, contentValues);
        } catch (final Exception ex1) {
            writeLog(TAG + " : saveAttachment()", ex1);
        }

        return id;
    }

    /**
     * When the user de-selects an image/other attachment
     * the corresponding entry would be removed from sqlite table WO_ATTACHMENTS
     *
     * @param caseId         {@link Integer}
     * @param attachmentPath {@link String}
     * @return boolean
     */

    public boolean removeAttchment(final long caseId, final long caseTypeId,
                                   final String attachmentPath, final long attachmentMimeTypeId, final String reasonTypeIds) {


        boolean status = false;


        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            String whereClause = null;
            String[] whereArgs = null;
            if (!"".equals(attachmentPath) && Utils.isNotEmpty(reasonTypeIds) && attachmentMimeTypeId != 0) {
                // Delete a specific attachment reference only
                whereClause = ID_CASE + "=? AND " + ATTACHMENT_PATH + "=? AND " + ATTACHMENT_REASON_TYPE_ID + "=? AND " + ATTACHMENT_MIME_TYPE_ID + "=?";
                whereArgs = new String[]{String.valueOf(caseId),
                        attachmentPath/*.toLowerCase(Locale.getDefault())*/, String.valueOf(reasonTypeIds),
                        String.valueOf(attachmentMimeTypeId)};

            } else if (!"".equals(attachmentPath) && Utils.isEmpty(reasonTypeIds) && attachmentMimeTypeId != 0) {
                // Delete a specific attachment reference only
                whereClause = ID_CASE + "=? AND " + ATTACHMENT_PATH + "=? AND " + ATTACHMENT_MIME_TYPE_ID + "=?";
                whereArgs = new String[]{String.valueOf(caseId),
                        attachmentPath/*.toLowerCase(Locale.getDefault())*/, String.valueOf(attachmentMimeTypeId)};
            } else {
                // Delete all attachment reference corresponding to this workorder
                whereClause = ID_CASE + "=?";
                whereArgs = new String[]{String.valueOf(caseId)};
            }

            if (db.delete(WO_ATTACHMENTS, whereClause.toString(), whereArgs) > 0) {
                status = true;
            }
        } catch (final Exception ex1) {
            writeLog(TAG + " : removeAttchment()", ex1);
        }
        return status;
    }

    /**
     * Save workorder flow Execution State
     */
    public <T extends WorkorderCustomWrapperTO> void createWoFlowExecForPage(final long idCase, final long idCaseT,
                                                                             final long pageIndex, final String currentPageId, final T wo, final AbortUtil abortUtil) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            final StringBuilder builder = new StringBuilder();
            builder.append(" DELETE FROM ").append(WO_FLOW_EXEC);
            builder.append(" WHERE ").append(ID_CASE).append(" = ").append(idCase);
            builder.append(" AND ").append(PAGE_INDEX).append(" = ").append(pageIndex);
            db.execSQL(builder.toString());

            final ContentValues contentValues = new ContentValues();
            contentValues.put(ID_CASE, idCase);
            contentValues.put(ID_CASE_T, idCaseT);
            contentValues.put(PAGE_ID, currentPageId);
            contentValues.put(PAGE_INDEX, pageIndex);
            contentValues.put(WORKORDER_JSON, JSONMAPPER.writeValueAsString(wo));
            contentValues.put(ABORT_UTIL, JSONMAPPER.writeValueAsString(abortUtil));

            Log.d(TAG, "INSERTING WO_FLOW_EXEC FOR ID_CASE :" + idCase + ", ID_CASE_T :" + idCaseT + ", PAGE_ID :" + currentPageId + ", PAGE_INDEX:" + pageIndex);
            db.insert(WO_FLOW_EXEC, null, contentValues);

        } catch (final Exception ex) {
            writeLog(TAG + " : createWoFlowExecForPage()", ex);
        }
    }

    /**
     * SAVE USER INPUT SELECTION FOR A PAGE
     *
     * @param idCase
     * @param pageIndex
     * @param currentPageId
     * @param pageValues
     */
    public void saveUserChoiceForPage(final long idCase, final long pageIndex, final String currentPageId,
                                      Map<String, String> pageValues) {

        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            String whereClause = ID_CASE + "=?  AND " + PAGE_ID + "=? ";
            String[] whereArgs = new String[]{String.valueOf(idCase), currentPageId};
            db.delete(WO_USER_PREFRENCE, whereClause, whereArgs);
            Set<String> pageValueKey = pageValues.keySet();
            Iterator<String> pageValueKeyItr = pageValueKey.iterator();
            while (pageValueKeyItr.hasNext()) {
                String valueKey = pageValueKeyItr.next();
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID_CASE, idCase);
                contentValues.put(PAGE_ID, currentPageId);
                contentValues.put(PAGE_INDEX, pageIndex);
                contentValues.put(USER_PREFRENCE_KEY, valueKey);
                contentValues.put(USER_PREFRENCE_VALUE, pageValues.get(valueKey));
                Log.d(TAG, "Saving user preference:Case id-" + idCase + " PageId:" + currentPageId + " preferencekey:" + valueKey + " prefernceValue:" + pageValues.get(valueKey));
                db.insert(WO_USER_PREFRENCE, null, contentValues);
            }

        } catch (final Exception ex) {
            writeLog(TAG + " : saveUserChoiceForPage() ", ex);
        }
    }

    /**
     * FETCH USER INPUT SELECTION FOR A PAGE
     *
     * @param caseId
     * @param pageIndex
     * @param pageId
     * @return
     */
    public Map<String, String> getUserChoiceForPage(final long caseId, final long pageIndex, final String pageId) {
        Log.d(TAG, " Getting page preference for Case-" + caseId + " Page-" + pageId + " PageIndex - " + pageIndex);
        Map<String, String> choiceMap = new ArrayMap<String, String>();
        if (caseId > 0) {
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try {
                db = getReadableDatabase();
                cursor = db.query(WO_USER_PREFRENCE, new String[]{USER_PREFRENCE_KEY, USER_PREFRENCE_VALUE},
                        ID_CASE + "=?  AND " + PAGE_ID + "=? ", new String[]{String.valueOf(caseId), pageId}, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    choiceMap.put(cursor.getString(cursor.getColumnIndex(USER_PREFRENCE_KEY)), cursor.getString(cursor.getColumnIndex(USER_PREFRENCE_VALUE)));
                }
            } catch (final Exception ex) {
                writeLog(TAG + " : getUserChoiceForPage()", ex);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return choiceMap;
    }

    /**
     * Save workorder flow Execution State
     */
    public void updateWoFlowExecForPage(final long idCase,
                                        final long pageIndex, final String currentPageId, WorkorderCustomWrapperTO wo, final Long idPdaPageT) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            final ContentValues contentValues = new ContentValues();
            contentValues.put(WORKORDER_JSON, JSONMAPPER.writeValueAsString(wo));
            contentValues.put(SUCCESSFULLY_COMPLETED, 1);
            contentValues.put(ID_PDA_PAGE_T, idPdaPageT);
            db.update(WO_FLOW_EXEC, contentValues,
                    ID_CASE + " = ? AND " +
                            PAGE_INDEX + " = ? AND " + PAGE_ID + " = ? ",
                    new String[]{String.valueOf(idCase), String.valueOf(pageIndex), currentPageId});
        } catch (final Exception ex) {
            writeLog(TAG + ": updateWoFlowExecForPage()", ex);
        }
    }

    /**
     * Return the latest modified work-order copy
     * for the provided case id and case type id and flow name
     *
     * @param caseId
     * @return
     */

    public <T extends WorkorderCustomWrapperTO> T getFinalWOForSync(final long caseId, final Class<T> clazz) {
        T wo = null;
        Cursor cursor = null;
        SQLiteDatabase readableDb = getReadableDatabase();
        try {
            if (caseId > 0) {
                cursor = readableDb.query(WO_SYNC_DATA, new String[]{WORKORDER_JSON}, ID_CASE + "=?",
                        new String[]{String.valueOf(caseId)}, null, null, ID_KEY + " DESC");
                while (cursor != null && cursor.moveToNext()) {
                    final String woDatString = cursor.getString(cursor.getColumnIndex(WORKORDER_JSON));
                    if (!"".equals(Utils.safeToString(woDatString))) {
                        wo = JSONMAPPER.readValue(woDatString, clazz);
                        break;
                    }
                }

            }

        } catch (final Exception ex) {
            writeLog(TAG + ": getFinalWOForSync()", ex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return wo;
    }

    public void saveFinalWOForSync(WorkorderCustomWrapperTO wo) {
        SQLiteDatabase writableDb = getWritableDatabase();
        try {
            final StringBuilder builder = new StringBuilder();
            builder.append(" DELETE FROM ").append(WO_SYNC_DATA);
            builder.append(" WHERE ").append(ID_CASE).append(" = ").append(wo.getIdCase());
            writableDb.execSQL(builder.toString());

            final ContentValues contentValues = new ContentValues();
            contentValues.put(ID_CASE, wo.getIdCase());
            contentValues.put(WORKORDER_JSON, JSONMAPPER.writeValueAsString(wo));
            writableDb.insert(WO_SYNC_DATA, null, contentValues);

        } catch (final Exception ex) {
            writeLog(TAG + ": saveFinalWOForSync()", ex);
        } finally {
            if (writableDb != null) {
                writableDb.close();
            }
        }
    }

    /**
     * SAVE WORKORDER STATE
     *
     * @param wo
     * @param state
     */
    public void saveWorkOrderState(WorkorderCustomWrapperTO wo, String state) {
        SQLiteDatabase writableDb = getWritableDatabase();
        try {
            final StringBuilder builder = new StringBuilder();
            builder.append(" DELETE FROM ").append(WO_SAVE_STATE);
            builder.append(" WHERE ").append(STATE).append(" = '").append(state).append("'");
            writableDb.execSQL(builder.toString());

            final ContentValues contentValues = new ContentValues();
            contentValues.put(STATE, state);
            contentValues.put(WORKORDER_JSON, JSONMAPPER.writeValueAsString(wo));
            writableDb.insert(WO_SAVE_STATE, null, contentValues);

        } catch (final Exception ex) {
            writeLog(TAG + ": saveWorkOrderState()", ex);
        } finally {
            if (writableDb != null) {
                writableDb.close();
            }
        }
    }

    /**
     * Return the saved work-order copy
     * for the provided state
     *
     * @param state
     * @return
     */

    public <T extends WorkorderCustomWrapperTO> T getWorkOrderForState(final String state, final Class<T> clazz) {
        T wo = null;
        Cursor cursor = null;
        SQLiteDatabase readableDb = getReadableDatabase();
        try {
            cursor = readableDb.query(WO_SAVE_STATE, new String[]{WORKORDER_JSON}, STATE + "=?",
                    new String[]{state}, null, null, ID_KEY + " DESC");
            while (cursor != null && cursor.moveToNext()) {
                final String woDatString = cursor.getString(cursor.getColumnIndex(WORKORDER_JSON));
                if (!"".equals(Utils.safeToString(woDatString))) {
                    wo = JSONMAPPER.readValue(woDatString, clazz);
                    break;
                }
            }

        } catch (final Exception ex) {
            writeLog(TAG + ": getWorkOrderForState()", ex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return wo;
    }

    /**
     * Retrieves the last user name
     *
     * @return
     */

    public String getLastLoginUserName() {
        String userName = null;

        SQLiteDatabase readableDb = null;
        Cursor cursor = null;

        try {
            readableDb = getReadableDatabase();

            cursor = readableDb.query(LAST_SESSION, new String[]{LAST_LOGIN_USERNAME}, //, LAST_LOGIN_PASSWORD},
                    null, null, null, null, ID_KEY + " DESC");
            while (cursor != null && cursor.moveToNext()) {
                userName = cursor.getString(cursor.getColumnIndex(LAST_LOGIN_USERNAME));
                break;
            }

        } catch (final Exception ex) {
            writeLog(TAG + ": getLastLoginUserName()", ex);
        }
        return userName;
    }

    /**
     * Retrieves the last user name
     *
     * @return
     */

    public String getLastLoginPassword() {
        String password = null;

        SQLiteDatabase readableDb = null;
        Cursor cursor = null;

        try {
            readableDb = getReadableDatabase();

            cursor = readableDb.query(LAST_SESSION, new String[]{LAST_LOGIN_PASSWORD},
                    null, null, null, null, ID_KEY + " DESC");
            while (cursor != null && cursor.moveToNext()) {
                password = cursor.getString(cursor.getColumnIndex(LAST_LOGIN_PASSWORD));
                break;
            }

        } catch (final Exception ex) {
            writeLog(TAG + ": getLastLoginPassword()", ex);
        }
        return password;
    }

    /**
     * LAST_SESSION
     *
     * @param userName
     * @param hashPassword
     */
    public void registerUserCreds(final String userName, final String hashPassword) {
        if (userName != null && !userName.equals("")
                && hashPassword != null && !hashPassword.equals("")) {
            SQLiteDatabase writableDb = null;
            try {
                writableDb = getWritableDatabase();

                // Delete all existing data to preserve only 1 record at a time in this table
                writableDb.delete(LAST_SESSION, null, null);

                // Insert the user information
                final ContentValues contentValues = new ContentValues();
                contentValues.put(LAST_LOGIN_USERNAME, userName);
                contentValues.put(LAST_LOGIN_PASSWORD, hashPassword);
                writableDb.insert(LAST_SESSION, null, contentValues);

            } catch (final Exception ex) {
                writeLog(TAG + ": registerUserCreds()", ex);
            }
        }
    }

    /**
     * Gets the Case Ids of completed work-orders to be synched
     *
     * @return
     */
    public List<Long> getCaseIdsForSync() {
        Log.d(TAG, "Getting case ids for sync activity");
        List<Long> caseIds = new ArrayList<Long>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            cursor = db.query(WO_SYNC_DATA, new String[]{ID_CASE},
                    null, null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                caseIds.add(cursor.getLong(cursor.getColumnIndex(ID_CASE)));
            }
        } catch (final Exception ex) {
            writeLog(TAG + ": getCaseIdsForSync()", ex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return caseIds;
    }

    /**
     * Delete Work order from Sync table
     *
     * @param caseId
     */
    public void deleteWOfromSync(final long caseId) {
        SQLiteDatabase writableDb = getWritableDatabase();
        try {
            final StringBuilder builder = new StringBuilder();
            builder.append(" DELETE FROM ").append(WO_SYNC_DATA);
            builder.append(" WHERE ").append(ID_CASE).append(" = ").append(caseId);
            writableDb.execSQL(builder.toString());
            Log.e(TAG, "SQLlite Query : " + builder.toString());
        } catch (final Exception ex) {
            writeLog(TAG + ": deleteWOfromSync()", ex);
        }
    }

    /**
     * Fetch the number of unsynchronized photos
     *
     * @return
     */
    public int getNumberOfPhotosToBeSynched() {
        Log.d(TAG, " Getting number of unsynchronized photos");
        SQLiteDatabase readableDb = null;
        int numberOfUnsycPhotos = 0;

        try {
            readableDb = getReadableDatabase();

            final StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append(" SELECT ");
            queryBuilder.append(" COUNT(1) AS PHOTO_COUNT ");
            queryBuilder.append(" FROM ").append(WO_ATTACHMENTS);

            Log.d(TAG, "SQLlite Query : " + queryBuilder.toString());
            final Cursor cursor = readableDb.rawQuery(queryBuilder.toString(), null);
            while (cursor != null && cursor.moveToNext()) {
                numberOfUnsycPhotos = cursor.getInt(cursor.getColumnIndex("PHOTO_COUNT"));
            }
            if (cursor != null) {
                cursor.close();
            }

        } catch (final Exception ex) {
            writeLog(TAG + ": getNumberOfPhotosToBeSynched()", ex);
        }
        return numberOfUnsycPhotos;
    }


    public void deleteStatusforWorkOrder(String caseId) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            StringBuilder queryString = new StringBuilder();
            queryString.append(" DELETE FROM ");
            queryString.append(WO_STATUS_IN_LOCAL);
            queryString.append(" WHERE ");
            queryString.append(ID_CASE).append(" = ").append(caseId);
            db.execSQL(queryString.toString());

            queryString = new StringBuilder();
            queryString.append(" DELETE FROM ");
            queryString.append(WO_STEP_STATUS);
            queryString.append(" WHERE ");
            queryString.append(ID_CASE).append(" = ").append(caseId);
            db.execSQL(queryString.toString());
        } catch (final Exception ex) {
            writeLog(TAG + " :deleteStatusforWorkOrder()", ex);
        }

    }


    public void updateStatusforWorkOrder(Long caseId, int presentStep, String stepIdentifiers) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            StringBuilder queryString = new StringBuilder();
            queryString.append("INSERT OR REPLACE INTO ");
            queryString.append(WO_STATUS_IN_LOCAL);
            queryString.append("(ID_CASE,PRESENT_STEP,STEP_IDENTIFIERS) VALUES (");
            queryString.append("'" + caseId + "'," + "'" + presentStep + "'," + "''" + stepIdentifiers + "''" + ')');
            db.execSQL(queryString.toString());
        } catch (final Exception ex) {
            writeLog(TAG + " :updateStatusforWorkOrder()", ex);
        }


    }

    public ArrayMap<String, Object> checkStatusforWorkOrder(String caseId) {

        SQLiteDatabase readableDb = null;

        int presentStep = 0;
        String stepIdentifiers = new String();
        ArrayMap<String, Object> arrayMap = new ArrayMap<>();
        try {
            readableDb = getReadableDatabase();

            final StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append(" SELECT ");
            queryBuilder.append(" PRESENT_STEP ,STEP_IDENTIFIERS ");
            queryBuilder.append(" FROM ").append(WO_STATUS_IN_LOCAL);
            queryBuilder.append(" WHERE ").append(ID_CASE).append(" = '").append(caseId).append("'");

            Log.d(TAG, "SQLlite Query : " + queryBuilder.toString());
            final Cursor cursor = readableDb.rawQuery(queryBuilder.toString(), null);
            while (cursor != null && cursor.moveToNext()) {
                presentStep = cursor.getInt(cursor.getColumnIndex("PRESENT_STEP"));
                stepIdentifiers = cursor.getString(cursor.getColumnIndex("STEP_IDENTIFIERS"));
            }
            if (cursor != null) {
                cursor.close();
            }

        } catch (final Exception ex) {
            writeLog(TAG + " :checkStatusforWorkOrder()", ex);
        }
        arrayMap.put("PRESENT_STEP", presentStep);
        arrayMap.put("STEP_IDENTIFIERS", stepIdentifiers);
        return arrayMap;


    }

    public void saveStepStatus(@NonNull String idCase, @NonNull String stepIdentifier,
                               @NonNull String valuesInStep,
                               @NonNull WorkorderCustomWrapperTO workorderCustomWrapperTO) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            StringBuilder queryString = new StringBuilder();
            queryString.append("INSERT OR REPLACE INTO ");
            queryString.append(WO_STEP_STATUS);
            queryString.append(" VALUES (" + idCase + "," + "'" + stepIdentifier + "'" + "," +
                    "'" + valuesInStep + "'" + "," + "'" + CommunicationHelper.JSONMAPPER.writeValueAsString(workorderCustomWrapperTO) + "'" + ")");
            db.execSQL(queryString.toString());
        } catch (Exception e) {
            writeLog(TAG + "  : saveStepStatus() ", e);
        }


    }

    public String getStepWO(@NonNull String idCase, @NonNull String stepIdentifier) {
        String woString = new String();

        SQLiteDatabase readableDb = null;

        try {
            readableDb = getReadableDatabase();

            final StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT ");
            queryBuilder.append(STEP_WO);
            queryBuilder.append(" FROM ");
            queryBuilder.append(WO_STEP_STATUS);
            queryBuilder.append(" WHERE ");
            queryBuilder.append(STEP_NO).append(" = ").append(" '" + stepIdentifier + "' ");

            queryBuilder.append(" AND ");
            queryBuilder.append(ID_CASE);
            queryBuilder.append(" = ");
            queryBuilder.append(Integer.parseInt(idCase));

            Log.d(TAG, "SQLlite Query : " + queryBuilder.toString());
            final Cursor cursor = readableDb.rawQuery(queryBuilder.toString(), null);
            while (cursor != null && cursor.moveToNext()) {
                woString = cursor.getString(cursor.getColumnIndex(STEP_WO));
            }
            if (cursor != null) {
                cursor.close();
            }

        } catch (final Exception ex) {
            writeLog(TAG + " :getStepStatus()", ex);
        }

        return woString;

    }

    public String getStepStatus(String idCase, String stepIdentifier) {
        String valuesInStep = new String();

        SQLiteDatabase readableDb = null;

        try {
            readableDb = getReadableDatabase();

            final StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT ");
            queryBuilder.append(STEP_VALUE_STRING);
            queryBuilder.append(" FROM ");
            queryBuilder.append(WO_STEP_STATUS);
            queryBuilder.append(" WHERE ");
            queryBuilder.append(STEP_NO).append(" = ").append(" '" + stepIdentifier + "' ");

            queryBuilder.append(" AND ");
            queryBuilder.append(ID_CASE);
            queryBuilder.append(" = ");
            queryBuilder.append(Integer.parseInt(idCase));

            Log.d(TAG, "SQLlite Query : " + queryBuilder.toString());
            final Cursor cursor = readableDb.rawQuery(queryBuilder.toString(), null);
            while (cursor != null && cursor.moveToNext()) {
                valuesInStep = cursor.getString(cursor.getColumnIndex(STEP_VALUE_STRING));
            }
            if (cursor != null) {
                cursor.close();
            }

        } catch (final Exception ex) {
            writeLog(TAG + " :getStepStatus()", ex);
        }

        return valuesInStep;
    }

    /**
     * Save data to WO_DOCUMENT
     */
    public void saveWoDocumentLiteTO(List<FieldDocumentInfoTTO> fieldDocumentInfoTTOList) {
        SQLiteDatabase readOrWriteDb = null;
        try {
            final List<WoDocumentLiteTO> woDocumentLiteTOs = generateWoDocumentLiteTO(fieldDocumentInfoTTOList);
            deletePreviousWoDocumentData();
            for (WoDocumentLiteTO woDocumentLiteTO : woDocumentLiteTOs) {
                if (woDocumentLiteTO != null) {
                    final ContentValues contentValues = new ContentValues();
                    contentValues.put(CODE, woDocumentLiteTO.getCode());
                    contentValues.put(DOCUMENT_URL, woDocumentLiteTO.getDocumentUrl());
                    contentValues.put(DOCUMENT_ALIAS_NAME, woDocumentLiteTO.getDocumentAliasName());
                    contentValues.put(DOCUMENT_VERSION, woDocumentLiteTO.getDocumentVersion());
                    contentValues.put(ID_DOMAIN, woDocumentLiteTO.getIdDomain());
                    contentValues.put(CHANGE_SIGNATURE, woDocumentLiteTO.getChangeSignature());
                    contentValues.put(CHANGE_TIMESTAMP, woDocumentLiteTO.getChangeTimestamp());
                    contentValues.put(ID_FIELD_DOCUMENT_T, woDocumentLiteTO.getIdFieldDocumentT());
                    contentValues.put(INFO, woDocumentLiteTO.getInfo());
                    contentValues.put(TOKEN_CODE, woDocumentLiteTO.getTokenCode());
                    contentValues.put(LOCAL_DB_CREATE_TIMESTAMP, woDocumentLiteTO.getLocalDBCreateTimestamp());
                    contentValues.put(ID_CASE_T, woDocumentLiteTO.getIdCaseT());

                    readOrWriteDb = getWritableDatabase();
                    readOrWriteDb.insert(WO_DOCUMENT, null, contentValues);
                }
            }
        } catch (final Exception ex) {
            writeLog(TAG + " :saveWoDocumentLiteTO()", ex);
        }
    }

    /**
     * Fetch data from WO_DOCUMENT
     */
    public List<WoDocumentLiteTO> getWoDocumentLiteTO() {

        Log.d(TAG, " Getting WO Documents...");
        SQLiteDatabase readableDb = null;

        List<WoDocumentLiteTO> woDocumentLiteTOs = new ArrayList<WoDocumentLiteTO>();

        try {
            readableDb = getReadableDatabase();

            final StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append(" SELECT *");
            queryBuilder.append(" FROM ").append(WO_DOCUMENT);

            Log.d(TAG, "SQLlite Query : " + queryBuilder.toString());
            final Cursor cursor = readableDb.rawQuery(queryBuilder.toString(), null);
            while (cursor != null && cursor.moveToNext()) {
                WoDocumentLiteTO woDocumentLiteTO = new WoDocumentLiteTO();
                woDocumentLiteTO.setId(cursor.getLong(cursor.getColumnIndex(ID_KEY)));
                woDocumentLiteTO.setIdDomain(cursor.getLong(cursor.getColumnIndex(ID_DOMAIN)));
                woDocumentLiteTO.setCode(cursor.getString(cursor.getColumnIndex(CODE)));
                woDocumentLiteTO.setDocumentUrl(cursor.getString(cursor.getColumnIndex(DOCUMENT_URL)));
                woDocumentLiteTO.setDocumentAliasName(cursor.getString(cursor.getColumnIndex(DOCUMENT_ALIAS_NAME)));
                woDocumentLiteTO.setDocumentVersion(cursor.getString(cursor.getColumnIndex(DOCUMENT_VERSION)));
                woDocumentLiteTO.setIdCaseT(cursor.getLong(cursor.getColumnIndex(ID_CASE_T)));
                woDocumentLiteTO.setIdFieldDocumentT(cursor.getLong(cursor.getColumnIndex(ID_FIELD_DOCUMENT_T)));
                woDocumentLiteTO.setInfo(cursor.getString(cursor.getColumnIndex(INFO)));
                woDocumentLiteTO.setChangeSignature(cursor.getString(cursor.getColumnIndex(CHANGE_SIGNATURE)));
                woDocumentLiteTO.setChangeTimestamp(cursor.getString(cursor.getColumnIndex(CHANGE_TIMESTAMP)));
                woDocumentLiteTO.setLocalDBCreateTimestamp(cursor.getString(cursor.getColumnIndex(LOCAL_DB_CREATE_TIMESTAMP)));
                woDocumentLiteTO.setTokenCode(cursor.getString(cursor.getColumnIndex(TOKEN_CODE)));

                if (woDocumentLiteTO != null) {
                    woDocumentLiteTOs.add(woDocumentLiteTO);
                }
            }
            if (cursor != null) {
                cursor.close();
            }

        } catch (final Exception ex) {
            writeLog(TAG + " :getWoDocumentLiteTO()", ex);
        }
        return woDocumentLiteTOs;
    }

    /**
     * Generates List of woDocumentLiteTOs
     */
    public List<WoDocumentLiteTO> generateWoDocumentLiteTO(List<FieldDocumentInfoTTO> fieldDocumentInfoTTOList) {

        List<WoDocumentLiteTO> woDocumentLiteTOs = new ArrayList<WoDocumentLiteTO>();
        try {
            for (FieldDocumentInfoTTO fieldDocumentInfoTTO : fieldDocumentInfoTTOList) {
                WoDocumentLiteTO woDocumentLiteTO = new WoDocumentLiteTO();

                woDocumentLiteTO.setIdDomain(fieldDocumentInfoTTO.getIdDomain());
                woDocumentLiteTO.setIdCaseT(fieldDocumentInfoTTO.getIdCaseT());
                woDocumentLiteTO.setIdFieldDocumentT(fieldDocumentInfoTTO.getIdFieldDocumentT());
                woDocumentLiteTO.setCode(fieldDocumentInfoTTO.getCode());
                woDocumentLiteTO.setInfo(fieldDocumentInfoTTO.getInfo());
                woDocumentLiteTO.setDocumentUrl(fieldDocumentInfoTTO.getDocumentUrl());
                woDocumentLiteTO.setDocumentAliasName(fieldDocumentInfoTTO.getDocumentAliasName());
                woDocumentLiteTO.setDocumentVersion(fieldDocumentInfoTTO.getDocumentVersion());
                woDocumentLiteTO.setChangeSignature(fieldDocumentInfoTTO.getChangeSignature());
                woDocumentLiteTO.setChangeTimestamp(Utils.createSkvaderTimestamp(fieldDocumentInfoTTO.getChangeTimestamp()));
                woDocumentLiteTO.setLocalDBCreateTimestamp(Utils.createSkvaderTimestamp(Calendar.getInstance().getTime()));
                woDocumentLiteTO.setTokenCode(fieldDocumentInfoTTO.getTokenCode());


                woDocumentLiteTOs.add(woDocumentLiteTO);

            }
        } catch (final Exception ex) {
            writeLog(TAG + " :fieldDocumentInfoTTOList()", ex);
        }
        return woDocumentLiteTOs;
    }

    /**
     * Delete Work order documents from Sync table
     */
    public void deletePreviousWoDocumentData() {
        SQLiteDatabase writableDb = getWritableDatabase();
        try {
            final StringBuilder builder = new StringBuilder();
            builder.append(" DELETE FROM ").append(WO_DOCUMENT);
            writableDb.execSQL(builder.toString());
            Log.e(TAG, "SQLlite Query : " + builder.toString());
        } catch (final Exception ex) {
            writeLog(TAG + " :deletePreviousWoDocumentData()", ex);
        }
    }

    /**
     * return a List of Field WO Document URL
     */
    public List<String> getSelectedURL(LinkedList<String> selectedDocumentAliasNameList) {
        SQLiteDatabase readableDb = getReadableDatabase();
        List selectedURL = new ArrayList<String>();
        try {
            for (String selectedDocumentAliasName : selectedDocumentAliasNameList) {

                final StringBuilder queryBuilder = new StringBuilder();
                queryBuilder.append(" SELECT ").append(DOCUMENT_URL);
                queryBuilder.append(" FROM ").append(WO_DOCUMENT);
                queryBuilder.append(" WHERE ").append(DOCUMENT_ALIAS_NAME);
                queryBuilder.append(" = '");
                queryBuilder.append(selectedDocumentAliasName).append("'");

                Log.d(TAG, "SQLlite Query : " + queryBuilder.toString());
                final Cursor cursor = readableDb.rawQuery(queryBuilder.toString(), null);
                if (cursor.moveToNext()) {
                    selectedURL.add(cursor.getString(cursor.getColumnIndex(DOCUMENT_URL)));
                }

                cursor.close();
            }

        } catch (final Exception ex) {
            writeLog(TAG + " :selectedDocumentAliasNameList()", ex);
        }

        if (selectedURL.size() == 0) {
            Log.i("DatabaseHAndler", "list is empty**********************");
        } else {
            Log.i("DatabaseHAndler", "list is full**********************");
        }

        return selectedURL;
    }

    /**
     * insert or update the Help_Document Table accordingly
     */

    public void insertOrUpdateHelpDocument(List<HelpDocBean> helpDocBeans) {
        try {
            for (HelpDocBean helpDocBean : helpDocBeans) {
                if (helpDocBean != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DEFAULT_MILLISECOND_FORMAT.getPattern());
                    SESPPreferenceUtil.savePreference("DATE_FOR_HELP", sdf.format(new Date()).toString());
                    SQLiteDatabase db = getWritableDatabase();
                    StringBuilder queryString = new StringBuilder();
                    queryString.append("INSERT OR REPLACE INTO ");
                    queryString.append(HELP_DOCUMENT);
                    // queryString.append("(" + KEY_ID + "," + ID_DOMAIN + "," + ROLE_ID + "," + MODULE_ID + "," + HELPTEXT + "," +UPDATED_TIMESTAMP+ "," + PAGE_ID + ")");
                    queryString.append(" VALUES (");
                    queryString.append("'").append(helpDocBean.getId()).append("'").append(",");
                    queryString.append("'").append(helpDocBean.getDomainID()).append("'").append(",");
                    queryString.append("'").append(helpDocBean.getRoleID()).append("'").append(",");
                    queryString.append("'").append(helpDocBean.getModuleID()).append("'").append(",");
                    queryString.append("'").append(helpDocBean.getHelpText()).append("'").append(",");
                    queryString.append("'").append(helpDocBean.getUpdatedOnTime()).append("'").append(",");
                    queryString.append("'").append(helpDocBean.getPageID()).append("'").append(" )");
                    db.execSQL(queryString.toString());
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : insertOrUpdateHelpDocuemnt() ", e);
        }


    }

    /**
     * It delete all the data of Help_Document Table
     */
    public void deleteHelpDocumentTable() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            final StringBuilder builder = new StringBuilder();
            builder.append(" DELETE FROM ").append(HELP_DOCUMENT);
            db.execSQL(builder.toString());
            Log.e(TAG, "SQLlite Query : " + builder.toString());
        } catch (Exception e) {
            writeLog(TAG + "  : deleteHelpDocumentTable() ", e);
        }
    }

    /**
     * fetches all the data of Help_Document Table based on help id
     */

    public HelpDocBean getHelpIDDocument(String helpID) {
        HelpDocBean helpData = null;
        try {
            SQLiteDatabase readableDb = getReadableDatabase();
            final StringBuilder queryBuilder = new StringBuilder();

            helpData = new HelpDocBean();
            queryBuilder.append(" SELECT ").append(" * ");
            queryBuilder.append(" FROM ").append(HELP_DOCUMENT);
            queryBuilder.append(" WHERE ").append(KEY_ID).append(" = '").append(helpID).append("'");

            final Cursor cursor = readableDb.rawQuery(queryBuilder.toString(), null);
            while (cursor != null && cursor.moveToNext()) {

                helpData.setHelpText(cursor.getString(cursor.getColumnIndex(HELPTEXT)));
                helpData.setPageID(cursor.getString(cursor.getColumnIndex(PAGE_ID)));
                helpData.setModuleID(cursor.getString(cursor.getColumnIndex(MODULE_ID)));
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            writeLog(TAG + "  : getHelpIDDocument() ", e);
        }
        return helpData;
    }

    public static String getMapApiKey() {

        String key;
        List<SystemParameterTO> systemParameterTOs = ObjectCache.getAllTypes(SystemParameterTO.class);
        for (SystemParameterTO systemParameterTO : systemParameterTOs) {
            if (systemParameterTO.getId().equals(CONSTANTS().SYSTEM_PARAMETER__GOOGLE_API_KEY)) {
                key = (systemParameterTO.getValue());
                key = "AIzaSyCNK1E1F4V2UqSAzTD8IU_pEBfPbBl8A0g";
                return key;
            }
        }
        return null;
    }
    // for insert or update translation
    public void insertTranslation(String tokenCode,String name){
        StringBuilder queryBuilder = new StringBuilder();
        String translatedName=null;
        try{
            SQLiteDatabase db =this.getWritableDatabase();
            SQLiteDatabase readableDatabase  = getReadableDatabase();
            queryBuilder.append(" SELECT ").append(TRANSLATED_NAME);
            queryBuilder.append(" FROM ").append(TRANSLATION_DATA);
            queryBuilder.append(" WHERE ").append(TOKEN_CODE).append(" = ").append("'").append(tokenCode).append("'");
            Cursor cursor =readableDatabase.rawQuery(queryBuilder.toString(),null);
            while (cursor !=null && cursor.moveToNext()){
                translatedName=cursor.getString(cursor.getColumnIndex(TRANSLATED_NAME));
            }
            ContentValues values = new ContentValues();
            if(translatedName !=null) {
                values.put(TRANSLATED_NAME, name);
                // updating row
                db.update(TRANSLATION_DATA,values,TOKEN_CODE + "=?",new String[]{tokenCode});
            }
            else {
                values.put(TOKEN_CODE, tokenCode);
                values.put(TRANSLATED_NAME, name);
                // Inserting Row
                db.insert(TRANSLATION_DATA, null, values);
                //db.insertWithOnConflict(TRANSLATION_DATA,name,values,);
                //db.insertWithOnConflict(TRANSLATION_DATA,name,values,cursor.getString(cursor.getColumnIndex(TRANSLATED_NAME)));
            }
        } catch (Exception e) {
            writeLog(TAG  +": insertTranslation()", e);
        }

    }
    // to translate for particular token
    public  String translateDesignation(String code) {
        StringBuilder queryBuilder = new StringBuilder();
        String name=null;
        JSONObject jsonObject=null;
        try {
            SQLiteDatabase readableDatabase  = getReadableDatabase();
            queryBuilder.append(" SELECT ").append(TRANSLATED_NAME);
            queryBuilder.append(" FROM ").append(TRANSLATION_DATA);
            queryBuilder.append(" WHERE ").append(TOKEN_CODE).append(" like ").append("'%").append(code).append("'");
            Cursor cursor =readableDatabase.rawQuery(queryBuilder.toString(),null);
            while (cursor !=null && cursor.moveToNext()){
                name=cursor.getString(cursor.getColumnIndex(TRANSLATED_NAME));
            }
        } catch (final Exception ex) {
            writeLog(TAG  +": translateDesignation()", ex);
        }
        return name;
    }

}
