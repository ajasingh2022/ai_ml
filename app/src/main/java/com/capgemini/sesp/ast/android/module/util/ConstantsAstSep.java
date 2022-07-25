package com.capgemini.sesp.ast.android.module.util;

public class ConstantsAstSep {

	public static final String FRAMEWORK_VERSION_META = "FRAMEWORK_VERSION_META";
	public static final String DEFAULT_LOG_DIR_NAME = "SESPLogs";

	//Menu Key Words
	public static final String MAIN_MENU_ACTIVITY = "main_menu_activity";
	public static final int LOCATION_TRACK_PENDING_REQUEST_ID = 123456;



	public static final class DialogCodes {
		public static final int LOGOUT = 1;
	}

	public static final class MaterialControlAction {
		public static final int TO_STORAGE = 0;
		public static final int TO_SCRAPPING = 1;
		public static final int TO_SERVICE = 2;
	}

	public static final class MaterialLogisticList {
		public static final int STOCK_BALANCE = 0;
		public static final int PALLET_HANDLING = 1;
		public static final int MATERIAL_CINTROL = 2;
		public static final int CAR = 3;
		public static final int GOODS_RECEPTION = 4;
		public static final int STOCK_TAKING = 5;
		public static final int REGISTER_NEW_BROKEN_DEVICE = 6;
		public static final int DEVICE_INFO = 7;
		public static final int MATERIAL_LOGISTICS_SETTINGS = 8;
	}

	public static final class MaterialLogisticListSetting {
		public static final int NO_IDENTIFIER = -1;
		public static final int IDENTIFIER_GIAI = 0;
		public static final int IDENTIFIER_SLNO = 2;
		public static final int IDENTIFIER_PROPNO = 3;
	}

	public static final class ActivityConstants {
		public static final String CALLING_ACTIVITY = "calling-activity";

		public static final String METER_INSTALLATION_DM = "METER_INSTALLATION_DM";
		public static final String SOLAR_PANEL_INSTALLATION = "SOLAR_NEW_INSTALLATION";
		public static final String TROUBLESHOOT_SOLAR = "TROUBLESHOOT_SOLAR";
		public static final String METER_INSTALLATION_CT = "METER_INSTALLATION_CT";
		public static final String METER_REMOVAL_CT = "METER_INSTALLATION_DM";
		public static final String METER_CHANGE_DM_ROLLOUT = "METER_CHANGE_DM_ROLLOUT";
		public static final String METER_CHANGE_DM = "METER_CHANGE_DM";
		public static final String METER_CHANGE_CT= "METER_CHANGE_CT";
		public static final String TROUBLESHOOT_MEASUREPOINT_CT= "TROUBLESHOOT_MEASUREPOINT_CT";
		public static final String TROUBLESHOOT_MEASUREPOINT_DM= "TROUBLESHOOT_MEASUREPOINT_DM";
		public static final String METER_CHANGE_CT_ROLLOUT = "METER_CHANGE_CT_ROLLOUT";
		public static final int ACTIVITY_SPLASH = 1001;
		public static final int ACTIVITY_LOGIN = 1002;
		public static final int ACTIVITY_MAINMENU = 1003;
		public static final int ACTIVITY_SETTINGS = 1004;
		public static final String SELECT_PALLET_ACTIVITY = "SELECT_PALLET_ACTIVITY";
		public static final String MATERIAL_CONTROL_INPUT_ACTIVITY = "MATERIAL_CONTROL_INPUT_ACTIVITY";
		public static final String CAR_INPUT_ACTIVITY = "CAR_INPUT_ACTIVITY";
		public static final String GOODS_RECEPTION_ACTIVITY = "GOODS_RECEPTION_ACTIVITY";
		public static final String REGISTER_NEW_BROKEN_DEVICE_ACTIVITY = "REGISTER_NEW_BROKEN_DEVICE_ACTIVITY";
		public static final String DEVICE_INFO_INPUT_ACTIVITY = "DEVICE_INFO_INPUT_ACTIVITY";
		public static final String MATERIAL_LOGISTICS_SETTINGS = "MATERIAL_LOGISTICS_SETTINGS";
		public static final String MAIN_MENU_ACTIVITY = "MAIN_MENU_ACTIVITY";
		public static final String PALLET_INFORMATION_ACTIVITY = "PALLET_INFORMATION_ACTIVITY";
		public static final String PALLET_CONTENT_ACTIVITY = "PALLET_CONTENT_ACTIVITY";
		public static final String MATERIAL_LOGISTICS_ACTIVITY = "MATERIAL_LOGISTICS_ACTIVITY";
		public static final String COMMON_LIST_ACTIVITY = "COMMON_LIST_ACTIVITY";
		public static final String MATERIAL_CONTROL_REGISTER_UNIT_ACTIVITY = "MATERIAL_CONTROL_REGISTER_UNIT_ACTIVITY";
		public static final String METER_COMMUNICATION_ACTIVITY = "METER_COMMUNICATION_ACTIVITY";
		public static final String ADD_COMMENT_ACTIVITY = "ADD_COMMENT_ACTIVITY";
		public static final String MATERIAL_CONTROL_SELECT_REASON_ACTIVTY = "MATERIAL_CONTROL_SELECT_REASON_ACTIVTY";
		public static final String MATERIAL_CONTROL_SELECT_PALLET_ACTIVITY = "MATERIAL_CONTROL_SELECT_PALLET_ACTIVITY";
		public static final String PRINT_MENU_ACTIVITY = "PRINT_MENU_ACTIVITY";
		public static final String DEVICE_INFO_SHOW_ACTIVITY="DEVICE_INFO_SHOW_ACTIVITY";
		public static final String ORDER_LIST_ACTIVITY ="ORDER_LIST_ACTIVITY";
		public static final String ORDER_SUMMARY_ACTIVITY ="ORDER_SUMMARY_ACTIVITY";
		//EV Smart Charging
		public static final String EV_SMART_CHARGING_METER_INSTALLATION_DM = "EV_SMART_CHARGING_METER_INSTALLATION_DM";
		public static final String EV_SMART_CHARGING_METER_INSTALLATION_CT = "EV_SMART_CHARGING_METER_INSTALLATION_CT";
		public static final String EV_TROUBLESHOOT_MEASURE_POINT_DM= "EV_TROUBLESHOOT_MEASUREPOINT_DM";
		public static final String EV_TROUBLESHOOT_MEASURE_POINT_CT= "EV_TROUBLESHOOT_MEASUREPOINT_CT";
	}

	public static final class DialogKeyConstants {
		public static final String ADD_BY_ID_DIALOG = "ADD_BY_ID_DIALOG";
		public static final String ADD_SEQUENCE_DIALOG = "ADD_SEQUENCE_DIALOG";
		public static final String ADD_BULK_DIALOG = "ADD_BULK_DIALOG";
		public static final String REMOVE_BY_ID_DIALOG = "REMOVE_BY_ID_DIALOG";
		public static final String BROKEN_ADD_BY_ID_DIALOG = "BROKEN_ADD_BY_ID_DIALOG";
		public static final String BROKEN_REMOVE_BY_ID_DIALOG = "BROKEN_REMOVE_BY_ID_DIALOG";
		public static final String MATERIAL_CONTROL_SELECT_PALLET_DIALOG = "MATERIAL_CONTROL_SELECT_PALLET_DIALOG";
	}

	public enum SharedPreferenceType {
		//PERSISTENT, LAST_SESSION, CREDENTIALS, USER_SETTINGS
		PERSISTENT, CREDENTIALS, USER_SETTINGS
	}

	public static final class SharedPreferenceKeys {
		// PERSISTENT
		public static final String DOWNLOAD_FINISHED = "download_finished";
		public static final String LANGUAGE = "language";
		public static final String ENABLE_BT_SCANNING = "enable_bt_scanning";
		public static final String LOCALE = "locale";
		public static final String SERVER_ADDRESS = "serverAddress";
		public static final String SSL_CERT_NAME = "sslCertName";
		public static final String SSL_PORT = "sslPort";
		public static final String BT_MAC_ADDRESS = "btMacAddress";
		public static final String USER_LOCKED = "user_locked";
		public static final String USER_LOGGED_OUT = "logged_out";

		// LAST_SESSION

		// CREDENTIALS
		public static final String LOGIN_OFFLINE_MODE = "login_offline_mode";
		public static final String TYPES_AST_SEP_CHECK_SUM = "types_ast_sep_check_sum";
		public static final String DATABASE_INFO = "database_info";

		// USER_SETTINGS
//		public static final String MATERIAL_LOGISTICS_SHOW_SETTINGS = "material_logistics_show_settings";
		public static final String MATERIAL_LOGISTICS_ID_IDENTIFIER = "material_logistics_id_identifier";
		public static final String MATERIAL_LOGISTICS_ID_STOCK = "material_logistics_id_stock";
		public static final String MATERIAL_LOGISTICS_GIAI_LENGTH = "material_logistics_giai_length";
		public static final String MATERIAL_LOGISTICS_PROPERTY_NUMBER_LENGTH = "material_logistics_serial_number_length";
		public static final String MATERIAL_LOGISTICS_SERIAL_NUMBER_LENGTH = "material_logistics_property_number_length";
		public static final String MATERIAL_LOGISTICS_IDENTFIER_LENGTH_USED = "material_logistics_identifier_length_used";
		public static final String SETTING_MAX_MINUTES_BETWEEN_DATA_UPDATE = "setting_minutes_until_update";
		public static final String SETTING_LAST_DATA_UPDATE = "setting_last_data_update";
		public static final String TYPE_DATA_DOWNLOADED_TIME = "setting_last_type_data_update";
		public static final String WO_DOWNLOADED_TIME = "setting_last_wo_update";
		public static final String FORCED_OFFLINE_MODE = "FORCED_OFFLINE_MODE";

		//Auto Update Settings
		public static  final String AUTO_UPDATE = "autoupdate";
		public static  final String VERSION_CHECK_URL = "versionCheckURL";
		public static  final String DOWNLOAD_URL = "downloadURL";
		//public static final String SESP_MAP_TILES_SOURCE = "map_tiles_source_";

		//register times
		public static  final String START_TRAVEL_TIME = "startTravelTime";
		public static  final String START_ORDER_TIME = "startorderTime";
		public static final String CASE_ID = "CASE_ID";


		//user position
		public static  final String GPS_COORD_SYS_TYPE = "GPS_COORD_SYS_TYPE";
		public static  final String GPS_X_COORD = "GPS_X_COORD";
		public static  final String GPS_Y_COORD = "GPS_Y_COORD";
	}

	public static final class OrderListTabs {
		public static final int INSTALLATION = 1;
		public static final int METER = 2;
		public static final int CUSTOMER = 3;
		public static final int ADDRESS = 4;
		public static final int TIME_RESERVATION = 5;
		public static final int SLA = 6;
		public static final int KEY = 7;
	}
	//DocumentDownlaod
	public static final class DocumentDownloadAction {
		public static final String INSTALLATION_DOC_TYPE="INST_DOC";
		public static final String SAFETY_DOC_TYPE="SFTY_DOC";
		public static final String PDF_EXTENSION=".pdf";

	}
	public static final class MaterialListTabs {
		public static final int UNITS = 1;
		public static final int KEY = 2;

	}
	public static final class ForgotPasswordCode{
		public static final String USERNAME_INVALID ="100";
		public static final String MOBILE_NUMBER_OR_EMAIL_INVALID = "101";
		public static final String OTP_GENERATION_FAILED="102";
		public static final String OTP_SEND_FAILED="103";
		public static final String LOCKED_USER ="104";
		public static final String ALREADY_LOCKED_USER="105";
		public static final String PASSWORD_RESET_FAILED="106";
		public static final String MISMATCH_PASSWORD_REQUIREMENTS="107";
		public static final String PASSWORD_RESET_NOT_SUPPORTED="108";
		public static final String SUCCESSFULL="110";
		public static final String PASSWORD_RESET_SUCCESS="120";
		public static final String OTP_VALIDATION_FAILED="130";
	}
	public static final class MaterialControlTabs {
		public static final int REGISTER = 1;
		public static final int INFO2 = 2;
		public static final int INFO3 = 3;
		public static final int STOCK = 4;

	}


	public static final class MainMenuItems {
		public static final int ORDER_SUMMARY = 0;
		public static final int NAVIGATION = 1;
		public static final int MATERIAL_LIST = 2;
		public static final int MATERIAL_LOGISTIC = 3;
		public static final int PRINTER = 4;
		public static final int SERIAL_COMMUNICATION_TEST = 5;
		public static final int TEST_METER_COMMUNICATION = 6;
	}

	public static class PrintMenuItems {
		public static final int SEARCH_ORDERS_TO_PRINT = 0;
		public static final int PRINT_SCANNED_ID = 1;
	}

	public static final class NavigationMenuItems {
		public static final int MAP_GPS = 0;
		//public static final int MAP = 1;
		public static final int SAVE_X_Y = 1;
		public static final int NEAR_BY_INSTALLATIONS = 2;
    }

	public enum OrderListKeys {
		INST_CODE, METER_SRNO, METER_MODEL, CUST_NAME, ADDRESS, TIME_RESERVATION, SLA_DEADLINE, KEY_NUMBER, KEY_INFO,WO_NUMBER

	}

	public enum StockBalanceKeys {
		NAME, STATUS, AMOUNT
	}

	public enum MaterialListKeys {
		UNIT_TYPE, UNIT_MODEL, UNIT_COUNT, KEY_NUMBER, KEY_INFO, KEY_COUNT
	}


	public static final long THRESHOLD_TIME_RESERAVTION_WARNING = 7200000; // 2 hours
	public static final Long CASE_TYPE_NULL = -12345L;

	public static final class DefaultSettings {
		public static final Long MAX_MINUTES_BETWEEN_UPDATE_OF_DATA = 15L;
	}

	public static final class MeterChangeConstants {
		public static final String ID_CASE = "workorder";
		public static final String ALLOW_START_FLOW = "Start_of_order";
		public static final String COMMENT_FROM_NAVBAR = "from_navbar";
		public static final int STATUS_NOT_READY = 0;
		public static final int STATUS_READY_NOT_OK = 1;
		public static final int STATUS_READY_OK = 2;
		public static final int STATUS_CANT_READ = 3;
		public static final boolean NEW_METER = true;
		public static final boolean OLD_METER = false;
		public static final boolean POWER_ON = true;
		public static final boolean POWER_OFF = false;
		public static final String INTENT_FROM = "intent_from";
	}

	public static final class ConcentratorInstallationConstants {
		public static final String INSTALLATION_TYPE_NET_STATION = "NET_STATION";

	}

	/**
	 * Added for generic workorder execution flow
	 * related constants
	 *
	 * @author nirmchak
	 *
	 */

	public static final class OrderHandlerConstants {
		public static final String WORKORDER_DATA = "WORKORDER_DATA";
		public static final String MODIFIED_WORKORDER_DATA = "MODIFIED_WORKORDER_DATA";
		public static final String LEAVING_FLOW_PAGE_CLASS_NAME = "LEAVING_FLOW_PAGE";
		public static final String LEAVING_FLOW_PAGE_NUMBER = "LEAVING_FLOW_PAGE_NUMBER";
		public static final String DEFAULT_BROADCAST_ACTION = "SESP_WORKFLOW_PROGRESS_SAVER";
		public static final String WORKORDER_CASE_ID = "WORKORDER_CASE_ID";
		public static final String WORKORDER_CASE_TYPE_ID = "WORKORDER_CASE_TYPE_ID";
		public static final String FIELD_VISIT_ID = "FIELD_VISIT_ID";
		public static final String FLOW_NAME = "FLOW_NAME";
		public static final String FLOW_PAGE_TITLE = "FLOW_PAGE_TITLE";
		public static final String FLOW_PROGRESS = "FLOW_PROGRESS";
		public static final String LAST_UPDATE_TS = "LAST_UPDATE_TS";
		public static final String LAST_FLOW_PAGE_NUM = "LAST_FLOW_PAGE_NUM";
		public static final String SESPDISKCACHEDIRNAME = "SESPDiskCache";
		public static final String ORDERDETAILSACTIVITYCLASS = "ORDERDETAILSACTIVITYCLASS";
		public static final float DISKCACHEUPPERLIMITFACTOR = 0.3f;
		//public static final String INTENT_SHOW_ORDER_HANDLER_BACK_WARNING = "INTENT_SHOW_ORDER_HANDLER_BACK_WARNING";

		public static final String INTERNAL_COMMENT = "INTERNAL_COMMENT";
		public static final String UTILITY_COMMENT = "UTILITY_COMMENT";

		public static final int BLUETOOTH_DISCOVERY_TIME_SECOND = 300;
		public static final String STD_BLUETOOTH_SEARCH_INTENT_ACTION = "STD_BLUETOOTH_SEARCH_INTENT_ACTION";

		public static final String LAST_WO_IMAGE="LAST_WO_IMAGE";
		public static final String DEFAULT_IMAGE_DIR = "DEFAULT_IMAGE_DIR";
		public static final String DEFAULT_IMAGE_DIR_NAME = "SESPWoImage";
		public static final String DEFAULT_CASE_IMAGE_DIR_NAME = "SESPTemp";
		public static final String DEFAULT_ATTACHMENT_DIR_NAME = "SESPWoAttachment";
		public static final String DEFAULT_IMAGE_FORMAT = ".jpg";
		public static final String PUBLIC_IMAGE_DIRS = "PUBLIC_IMAGE_DIRS";
		public static final String IMAGE_ACTIVITY_INTENT_ACTION = "SESP_WO_IMAGE_ACTIVITY";

		// Following constant would be moved to DatabaseConstantsAstSepAndroid in AstSepWebServiceClient later 
		public static final String INFO_TO_TECHNICIAN = "INFO_TO_TECHNICIAN";
		public static final String INFO_TO_OPERATOR = "INFO_TO_OPERATOR";
		public static final String INFO_TO_UTILITY = "INFO_TO_UTILITY";
		public static final String USER_COMMENT = "USER_COMMENT";

		//public static final String DT_FORMAT = "yyyy-MM-dd HH:mm:ss";
		public static final String IMAGE_EXT_JPG = ".jpg";
		public static final String IMAGE_EXT_JPEG = ".jpeg";
		public static final String IMAGE_EXT_PNG = ".png";
		public static final String IMAGE_EXT_GIF = ".gif";

		public static final String IMAGEFILEINTENTNAME = "IMAGEFILENAME";
		public static final String SESPWOIMAGEPREFNAME = "SESPWOIMAGE";

		/*public static final Long MINTIMEMILLISFORLOCUPDATE = Long.valueOf(20*1000);
		public static final Float MINDISTANCEFLOATFORLOCUPDATE = 1f;*/
		public static final Long MINTIMEMILLISFORLOCUPDATE = 500l;
		public static final Float MINDISTANCEFLOATFORLOCUPDATE = 0.2f;

		// Constants to be used in conjunction with intents for starting service to transmit workorder data to WS
		public static final String CASE_ID = "com.capgemini.sesp.android.extra.CASE_ID";
		public static final String CASE_TYPE_ID = "com.capgemini.sesp.android.extra.CASE_TYPE_ID";
		// Following action would be used in the intent filter of the service class
		public static final String ACTION_SAVE_WORKORDER = "com.capgemini.sesp.android.action.ACTION_SAVE_WORKORDER";
		public static final int SAVE_WO_FLAG = 1;

		public static final int WO_SAVE_THREAD_POOL_SIZE = 3;
		public static final int WO_SAVE_SERVICE_BOUNDED_START_ID = -10;
		public static final int WO_SAVE_SERVICE_NORMAL_START_ID = -20;

		public static final String FINAL_PAGE_INDEX = "FINAL_PAGE_INDEX";
		public static final String METER_INSTALLATION_CT_MEASURED_FIELD = "Meter Installation CT Measured (Field)";
		public static final String METER_CHANGE_CT_MEASURED_FIELD = "Meter Change CT Measured (Field)";
		public static final String METER_INSTALLATION_DM_FIELD = "Meter Installation Directly Measured (Field)";
		public static final String METER_CHANGE_CT_ROLLOUT_FIELD = "Meter Change CT Measured Rollout (Field)";
		public static final String METER_CHANGE_DM_FIELD = "Meter Change Directly Measured (Field)";
		public static final String METER_CHANGE_DM_ROLLOUT_FIELD = "Meter Change Directly Measured Rollout (Field)";
		public static final String METER_REMOVAL_DM_FIELD = "Meter Removal Directly Measured (Field)";
		public static final String REPEATER_INSTALLATION_FIELD = "Repeater Installation";
		public static final String CONCENTRATOR_INSTALLATION_FIELD = "Concentrator Installation";
		public static final String TS_MEP_CT_FIELD = "Troubleshoot Measurepoint CT";
		public static final String TS_MEP_DM_FIELD = "Troubleshoot Measurepoint DM";
		public static final String METER_REMOVAL_CT_FIELD = "Meter Removal CT Measured (Field)";
		public static final String TS_MUP_REPEATER_FIELD = "Troubleshoot Multipoint Repeater (Field)";
		public static final String TS_MUP_CONCENTRATOR_FIELD = "Troubleshoot Multipoint Concentrator (Field)";
		public static final String VERIFICATION = "Verification of equipment";
		/*EV Meter*/
		//public static final String EV_CHARGING_STATION_DM_FIELD = "EV Charging Station DM Field";
		public static final String EV_CHARGING_STATION_CT_FIELD = "EV Charging Station CT Field";
	}

	/**
	 * Added constants for Flow Page related activities
	 */
	public static final class FlowPageConstants {
		public static transient final String VERIFYINSTALLINFO = "VERIFYINSTALLINFO";
		public static transient final String PRINT_LABEL_FLOW_PAGE = "PRINT_LABEL_FLOW_PAGE";
		public static transient final String NEGATIVE_FLOW_PAGE = "NEGATIVE_FLOW_PAGE";
		public static transient final String ENTERED_MEP_NUMBER = "ENTERED_MEP_NUMBER";
		public static transient final String ENTERED_MET_NUMBER = "ENTERED_MET_NUMBER";
		public static transient final String ADDITIONAL_WORK = "ADDITIONAL_WORK";
		public static transient final String LOAD_CONTROL_EXISTS="LOAD_CONTROL_EXISTS";
		public static transient final String LOAD_CONTROL_CONNECTED="LOAD_CONTROL_EXISTS";
		public static transient final String NEXT_PAGE = "NEXT_PAGE";
		public static transient final String CHOSEN_REASON = "CHOSEN_REASON";
		public static transient final String SAVED_NOTE = "SAVED_NOTE";
		public static transient final String CHANGE_METER_REASON = "CHANGE_METER_REASON";
		public static transient final String COMMUNICATION_FAILURE_REASON = "COMMUNICATION_FAILURE_REASON";
		public static transient final String CONNECT_PHOTO = "CONNECT_PHOTO";
		public static transient final String METER_INSTALLATION_CHECK = "METER_INSTALLATION_CHECK";
		public static transient final String TECH_ACCSIABLE_KEY = "TECH_ACCSIABLE";
		public static transient final String CUST_ACCSIABLE_KEY = "CUST_ACCSIABLE";
		public static transient final String METER_POWERED = "METER_POWERED";
		public static transient final String METER_DISCONNECT_TYPE = "METER_DISCONNECT_TYPE";
		public static transient final String SELECTED_UNIT_MODEL = "SELECTED_UNIT_MODEL";
		public static transient final String UNIT_IDENTIFIER = "ENTERED_UNIT_IDNTIFIER";
		public static transient final String MASTER_METER_SELECTED = "MASTER_METER_SELECTED";
		public static transient final String ENFORCE_NEW_SIM = "ENFORCE_NEW_SIM";
		public static transient final String ENFORCE_NEW_CONCENTRATOR = "ENFORCE_NEW_CONCENTRATOR";
		public static transient final String ENFORCE_NEW_REPEATER = "ENFORCE_NEW_REPEATER";
		public static transient final String METER_NUMBER = "METER_NUMBER";
		public static transient final String BUILT_IN_COMMUNICATION = "BUILT_IN_COMMUNICATION";
		public static transient final String METER = "METER";
		public static transient final String OLD_METER_POWERED = "OLD_METER_POWERED";
		public static transient final String OLD_METER_DISCONNECT_TYPE = "OLD_METER_DISCONNECT_TYPE";
		public static transient final String QUALITY_CONTROL = "QUALITY_CONTROL";
		public static transient final String CAN_READ = "CAN_READ";
		public static transient final String REASON_NOT_READ = "REASON_NOT_READ";
		public static transient final String REG_VALUES = "REG_VALUES";
		public static transient final String VALIDDATAPROVIDED = "VALIDDATAPROVIDED";
		public static transient final String GPS_BUTTON_SELECTED = "GPS_BUTTON_SELECTED";
		public static transient final String NEW_X_COORDINATE = "NEW_X_COORDINATE";
		public static transient final String NEW_Y_COORDINATE = "NEW_Y_COORDINATE";
		public static transient final String NEW_COORDINATE_SYS ="NEW_COORDINATE_SYS";
		public static transient final String NEXT_PAGE_NEW_EXTERNAL_ANTENNA = "NEXT_PAGE_NEW_EXTERNAL_ANTENNA";
		public static transient final String NEW_EXTERNAL_ANTENA = "NEW_EXTERNAL_ANTENA";
		public static transient final String YES_NEW_CM_CHECKBOX = "YES_NEW_CM_CHECKBOX";
		public static transient final String KEEP_EXISTING_CM_CHECKBOX = "KEEP_EXISTING_CM_CHECKBOX";
		public static transient final String NEW_UNIT_IDENTIFIER_VALUE = "NEW_UNIT_IDENTIFIER_VALUE";
		public static transient final String BUILT_IN_CM_MODULE_CHECKED = "BUILT_IN_CM_MODULE_CHECKED";
		public static transient final String SELECTED_ANTENNA_MODEL = "SELECTED_ANTENNA_MODEL";
		public static transient final String SIGNAL_STRENGTH = "SIGNAL_STRENGTH";
		public static transient final String CABLE_LENGTH = "CABLE_LENGTH";
		public static transient final String DIRECTION = "DIRECTION";
		public static transient final String SELECTED_ANTENNA_PLACEMENT = "SELECTED_ANTENNA_PLACEMENT";
		public static transient final String YES_NEW_SIM_CHECKBOX = "YES_NEW_SIM_CHECKBOX";
		public static transient final String KEEP_EXISTING_SIM_CHECKBOX = "KEEP_EXISTING_SIM_CHECKBOX";
		public static transient final String YES_NEW_REPEATER_CHECKBOX = "YES_NEW_REPEATER_CHECKBOX";
		public static transient final String KEEP_EXISTING_REPEATER_CHECKBOX = "KEEP_EXISTING_REPEATER_CHECKBOX";
		public static transient final String YES_NEW_CONCENTRATOR_CHECKBOX = "YES_NEW_CONCENTRATOR_CHECKBOX";
		public static transient final String KEEP_EXISTING_CONCENTRATOR_CHECKBOX = "KEEP_EXISTING_CONCENTRATOR_CHECKBOX";
		public static transient final String REMOVE_EXISTING_SIM_CHECKBOX = "REMOVE_EXISTING_SIM_CHECKBOX";
		public static transient final String OLD_METER_INDICATORS = "OLD_METER_INDICATORS";
		public static transient final String POSITIVE_PAGE_TROUBLESHOOT = "POSITIVE_PAGE_TROUBLESHOOT";
		public static transient final String NEGATIVE_PAGE_TROUBLESHOOT = "NEGATIVE_PAGE_TROUBLESHOOT";
		public static transient final String TROUBLESHOOT_POSSIBLE = "TROUBLESHOOT_POSSIBLE";
		public static transient final String TROUBLESHOOT_NOT_POSSIBLE_REASON = "TROUBLESHOOT_NOT_POSSIBLE_REASON";
		public static transient final String TROUBLESHOOT_NOT_POSSIBLE_NOTE = "TROUBLESHOOT_NOT_POSSIBLE_NOTE";
		public static transient final String METER_PLACEMENT_OK = "METER_PLACEMENT_OK";
		public static transient final String PLINT_NUMBER_OK = "PLINT_NUMBER_OK";
		public static transient final String FUSE_OK = "FUSE_OK";
		public static transient final String NUM_PHASE_OK = "NUM_PHASE_OK";
		public static transient final String KEY_INFO_OK = "KEY_INFO_OK";
		public static transient final String KEY_NUMBER_OK = "KEY_NUMBER_OK";
		public static transient final String FEED_LINE_OK = "FEED_LINE_OK";
		public static transient final String INSTALLATION_POSSIBLE = "INSTALLATION_POSSIBLE";
		public static transient final String ANTENNA_MODEL_NAME = "ANTENNA_MODEL_NAME";
		public static transient final String UNIT_MODEL_NAME = "UNIT_MODEL_NAME";
		public static transient final String ENFORCE_NEW_CM = "ENFORCE_NEW_CM";
		public static transient final String SIM_CARD_INSTALLED = "SIM_CARD_INSTALLED";
		public static transient final String PRIMARY_OK_CLICKED = "PRIMARY_OK_CLICKED";
		public static transient final String PRIMARY_REM_CLICKED = "PRIMARY_REM_CLICKED";
		public static transient final String SECONDARY_OK_CLICKED = "SECONDARY_OK_CLICKED";
		public static transient final String SECONDARY_REM_CLICKED = "SECONDARY_REM_CLICKED";
		public static transient final String PRIMARY_VALUE_INDEX = "PRIMARY_VALUE_INDEX";
		public static transient final String SECONDARY_VALUE_INDEX = "SECONDARY_VALUE_INDEX";
		public static transient final String RATIO_VALUE = "RATIO_VALUE";
		public static transient final String CONFIRM_DISCONNECTION_OK_BTN = "CONFIRM_DISCONNECTION_OK_BTN";
		public static transient final String METER_CHANGE_POSSIBLE = "METER_CHANGE_POSSIBLE";
		public static transient final String NOT_POSSIBLE_REASON = "NOT_POSSIBLE_REASON";
		public static transient final String NOT_POSSIBLE_NOTE = "NOT_POSSIBLE_NOTE";
		public static transient final String MANUAL_CHANGE_CT_PARAMS = "MANUAL_CHANGE_CT_PARAMS";
		public static transient final String IS_PHOTO_MANDATORY = "IS_PHOTO_MANDATORY";
		public static transient final String YES_NEW_METER_CHECKBOX = "YES_NEW_METER_CHECKBOX";
		public static transient final String KEEP_EXISTING_METER_CHECKBOX = "KEEP_EXISTING_METER_CHECKBOX";
		public static transient final String TECH_ACCSIABLE_OK = "TECH_ACCSIABLE_OK";
		public static transient final String TECH_ACCSIABLE_REMARK = "TECH_ACCSIABLE_REMARK";
		public static transient final String CUST_ACCSIABLE_OK = "CUST_ACCSIABLE_OK";
		public static transient final String CUST_ACCSIABLE_REMARK = "CUST_ACCSIABLE_REMARK";
		public static transient final String SIM_ICC_NUMBER = "SIM_ICC_NUMBER";
		public static transient final String NEXT_PAGE_NO_METER = "NEXT_PAGE_NO_METER";
		public static transient final String COMMENT_TO_UTILITY = "COMMENT_TO_UTILITY";
		public static transient final String COMMENT_TO_INTERNAL = "COMMENT_TO_INTERNAL";
		public static transient final String SESP_WO_PHOTO_PREFS = "SESP_WO_PHOTO_PREFS";
		public static transient final String FUSE_SIZE_VALUE = "FUSE_SIZE_VALUE";
		public static transient final String PLINT_NUMBER_VALUE = "PLINT_NUMBER_VALUE";
		public static transient final String METER_PLACEMENT_VALUE = "METER_PLACEMENT_VALUE";
		public static transient final String KEY_NUMBER_VALUE = "KEY_NUMBER_VALUE";
		public static transient final String KEY_INFO_VALUE = "KEY_INFO_VALUE";
		public static transient final String NUM_PHASE_VALUE = "NUM_PHASE_VALUE";
		public static transient final String ACCREDIT_CONTROL = "PERFORM_ACCREDIT_CONTROL_YES";

		public static transient final String PERFORM_VER_CTRAFO = "PERFORM_VER_CTRAFO_YES";
		public static transient final String PERFORM_TRAFO_MANAGEMENT = "PERFORM_TRAFO_MANAGEMENT";

		public static transient final String STATION_OK = "STATION_OK";
		public static transient final String STATION_REMARK = "STATION_REMARK";
		public static transient final String STATION_NAME_OK = "STATION_NAME_OK";
		public static transient final String STATION_NAME_REMARK = "STATION_NAME_REMARK";
		public static transient final String SLOT_OK = "SLOT_OK";
		public static transient final String SLOT_REMARK = "SLOT_REMARK";
		public static transient final String USER_CHOICE = "USER_CHOICE";
		public static transient final String YES = "YES";
		public static transient final String OK = "OK";
		public static transient final String REMARK = "REMARK";
		public static transient final String NO = "NO";
		public static transient final String FLOW_PDA_WORK_ORDER = "FLOW_PDA_WORK_ORDER";
		public static transient final String USER_SELECTION_EXTERNAL_ANTENA = "USER_SELECTION_EXTERNAL_ANTENA";
		public static transient String CONCENTRATOR = "CONCENTRATOR";
		public static transient String SIM_CARD = "SIM_CARD";
		public static transient String MODEM = "MODEM";
		public static transient String REPEATER = "REPEATER";
		public static transient String CM = "CM";
		public static transient final String NEXT_PAGE_PRODUCT="NEXT_PAGE_PRODUCT";
		public static transient final String TEST_METER_COMMUNICATION="$TEST_METER_COMMUNICATION$";

		//SOLAR//
		public static transient final String SCAN_MISMATCH_UNIT_ID = "SCAN_MISMATCH_UNIT_ID";
		public static transient final String CHECKLIST_ITEM ="CHECKLIST_ITEM";
		public static transient final String EDITABLE_INFO ="EDITABLE_INFO";
		public static transient final String PANEL_INSTALLATION_CHANGE_FLAG ="PANEL_INSTALLATION_CHANGE_FLAG";

	}

	public static final class PageNavConstants {
		public static transient final String NEXT_PAGE = "NEXT_PAGE";
		public static transient final String NEXT_PAGE_YES = "NEXT_PAGE_YES";
		public static transient final String NEXT_PAGE_NO = "NEXT_PAGE_NO";
		public static transient final String NEXT_PAGE_NO_L2 = "NEXT_PAGE_NO_L2";
		public static transient final String NEXT_PAGE_NO_L3 = "NEXT_PAGE_NO_L3";
		public static transient final String NEXT_PAGE_WITHOUT_MASTER = "NEXT_PAGE_WITHOUT_MASTER";
		public static transient final String NEXT_PAGE_WITH_MASTER = "NEXT_PAGE_WITH_MASTER";
		public static transient final String NEXT_PAGE_SIM_CARD = "NEXT_PAGE_SIM_CARD";
		public static transient final String NEXT_PAGE_NEW_EXTERNAL_ANTENNA = "NEXT_PAGE_NEW_EXTERNAL_ANTENNA";
		public static transient final String NEXT_PAGE_UNIT_VERIFICATION = "NEXT_PAGE_UNIT_VERIFICATION";
		public static transient final String NEXT_PAGE_CM_MODULE = "NEXT_PAGE_CM_MODULE";
		public static transient final String NEXT_PAGE_ADD_PHOTO = "NEXT_PAGE_ADD_PHOTO";
		public static transient final String POSITIVE_PAGE = "POSITIVE_PAGE";
		public static transient final String NEGATIVE_PAGE = "NEGATIVE_PAGE";
		public static transient final String POSITIVE_PAGE_TROUBLESHOOT = "POSITIVE_PAGE_TROUBLESHOOT";
		public static transient final String NEGATIVE_PAGE_TROUBLESHOOT = "NEGATIVE_PAGE_TROUBLESHOOT";
		public static transient final String NEXT_PAGE_SIM_CARD_METER = "NEXT_PAGE_SIM_CARD_METER";
		public static transient final String NEXT_PAGE_SIM_CARD_CM = "NEXT_PAGE_SIM_CARD_CM";
		public static transient final String NEXT_PAGE_NO_METER = "NEXT_PAGE_NO_METER";
		public static transient final String NEXT_PAGE_METER_INDICATION_NEW_METER = "NEXT_PAGE_METER_INDICATION_NEW_METER";
		public static transient final String NEXT_PAGE_PRINT_LABEL = "NEXT_PAGE_PRINT_LABEL";
		public static transient final String NEXT_PAGE_CONNECT_PHOTO = "NEXT_PAGE_CONNECT_PHOTO";
		public static transient final String NEXT_PAGE_TIME_RESERVATION = "NEXT_PAGE_TIME_RESERVATION";
		public static transient final String NEXT_PAGE_REGISTER_COORDS = "NEXT_PAGE_REGISTER_COORDS";
		public static transient final String NEXT_PAGE_CHECK_POWER_STATUS = "NEXT_PAGE_CHECK_POWER_STATUS";
		public static transient final String NEXT_PAGE_REGISTER_TIME = "NEXT_PAGE_REGISTER_TIME";
	}

	/**
	 * Added for declaring custom actions
	 *
	 */
	public static final class CustomActionConstants {
		public static final String LOG_ERROR = "com.skavader.rsp.android.LOGERROR";
	}

	/**
	 * Unit Type enum for checking UnitType
	 */
	public enum UnitType {
		METER,
		CONCENTRATOR,
		SIMCARD,
		REPEATER,
		CM
    }

	public static final class PropertyConstants{
		public static transient final String KEY_TYPE_DATA_DOWNLOAD_TIME_DIFF = "type_data_download_time_diff";
		public static transient final String KEY_WORKORDER_DOWNLOAD_TIME_DIFF = "wo_download_time_diff";
		public static transient final String KEY_SERVER_URL = "server_url";
		public static transient final String KEY_SSL_CERT_NAME = "ssl_certificate_name";
		public static transient final String KEY_SSL_PORT = "ssl_port";
		public static transient final String KEY_PROPERTIES_FILE = "rsp.properties";
		public static transient final String KEY_AUTO_UPDATE = "autoupdate";
		public static transient final String KEY_VERSION_CHECK_URL = "versionCheckURL";
		public static transient final String KEY_DOWNLOAD_URL = "downloadURL";
		public static transient final String KEY_TILE_SOURCE_FACTORY = "map_tiles_source_";
		public static transient final String KEY_CONNECTION_TIMEOUT = "connectionTimeOut";
		public static transient final String KEY_PHOTO_MAX_LIMIT = "photo_max_limit";
	}

	public static final class StockhandlingConstants {
		public static transient final String GIAI_BARCODE_PREFIX = "8004";
		public static transient final String INST_BARCODE_PREFIX = "8018";
		public static transient final String BARCODE_RESULT = "barcode_result";
		public static transient final String CONTROL = "CONTROL";
		public static transient final String METER_EXTERNAL = "METER_EXTERNAL";
		public static transient final String SUSPICIOUS_OLD_METER = "SUSPICIOUS_OLD_METER";
		public static transient final String SPECIAL_WASTE = "SPECIAL_WASTE";
		public static transient final String LOCAL_STOCK = "LOCAL_STOCK";
		public static transient final String SERVICE_VEHICLE = "SERVICE_VEHICLE";
		public static transient final String MOUNTABLE = "MOUNTABLE";
		public static transient final String WASTED = "WASTED";
		public static transient final String MATERIAL_CONTROL_PALLET_CODE = "MATERIAL_CONTROL_PALLET_CODE";
	}


	public static final class ZeraConstants {
		public static transient final String ZERA_MT_310_DATA = "ZERA_MT_310_DATA";
	}

	public static final class HelpDocumentConstant {
		public static transient final int MAIN_MENU = 1;
		public static transient final int ORDER_SUMMARY = 2;
		public static transient final int MATERIAL_lIST = 3;
		public static transient final int MATERIAL_lOGISTICS = 4;
		public static transient final int NAVIGATION = 5;
		public static transient final int PRINTER = 6;
		public static transient final int COMMUNICATION = 7;

		public static transient final int PAGE_STOCK_BALANCE = 8;
		public static transient final int PAGE_MANAGE_PALLET= 9;
		public static transient final int PAGE_MATERIAL_CONTROL = 10;
		public static transient final int PAGE_CAR = 12;
		public static transient final int PAGE_GOODS_RECEPTION = 11;
		public static transient final int PAGE_STOCK_TAKING = 13;
		public static transient final int PAGE_REGISTER_NEW_BROKEN_DEVICE = 14;
		public static transient final int PAGE_DEVICE_INFO = 15;
		public static transient final int PAGE_ORDER_LIST =16;
		public static transient final int PAGE_ORDER_INFO =17;

		public static transient final int PASSWORD_RESET =18;

	}
//Solar Constants
	public static final class SolarConstant{
	public static transient final String CUSTOMER_TYPE = "CUSTOMER_TYPE";
	public static transient final String INSTALLATION_SURFACE = "INSTALLATION_SURFACE";
	public static transient final String TYPE_OF_SOLAR_PANEL = "TYPE_OF_SOLAR_PANEL";
	}


}
