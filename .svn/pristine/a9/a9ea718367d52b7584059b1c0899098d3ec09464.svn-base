package com.capgemini.sesp.ast.android.module.communication;

import android.util.Log;

/*import com.skvader.rsp.ast_sep.common.to.ast.table.FieldDocumentInfoTTO;*/
import com.skvader.rsp.ast_sep.common.mobile.bean.FCMTokenTO;
import com.skvader.rsp.ast_sep.common.mobile.bean.HelpDocBean;
import com.skvader.rsp.ast_sep.common.to.ast.table.AndroidLogTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.FieldDocumentInfoTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.StocktakingTO;
import com.skvader.rsp.ast_sep.common.to.custom.AddToStockTO;
import com.skvader.rsp.ast_sep.common.to.custom.AddToStocktakingTO;
import com.skvader.rsp.ast_sep.common.to.custom.NearByWorkOrder;
import com.skvader.rsp.ast_sep.common.to.custom.StockBalanceTO;
import com.skvader.rsp.ast_sep.common.to.custom.UnitChangeStatusCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.UnitInformationTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.AndroidCachedDataAstSepTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.AndroidStaticDataAstSepTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.DatabaseConstantsAstSepAndroid;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.DatabaseConstantsCftAndroid;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.PalletLightTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitIdentifierTO;
import com.skvader.rsp.ast_sep.webservice_client.port.AndroidWebserviceClientAstSep;
import com.skvader.rsp.ast_sep.webservice_client.port.ForgotPasswordWebserviceClient;
import com.skvader.rsp.cft.common.to.cft.table.AttachmentTO;
import com.skvader.rsp.cft.common.to.cft.table.ErrorTO;
import com.skvader.rsp.cft.common.to.cft.table.UserPositionTO;
import com.skvader.rsp.cft.common.to.cft.table.UsersTO;
import com.skvader.rsp.cft.common.to.custom.bean.AndroidRequestParametersTO;
import com.skvader.rsp.cft.common.util.AndroidAuthenticationTO;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.LockWorkorderCustomTO;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.UserLoginCustomTO;
import java.util.Date;
import java.util.List;

public class AndroidWebserviceClientAstSepImpl extends AndroidWebserviceClientBase implements AndroidWebserviceClientAstSep ,ForgotPasswordWebserviceClient {

	@Override
	public UsersTO getUser(String username) throws Exception {
		return call("getUser", UsersTO.class, username);
	}

	@Override
	public DatabaseConstantsAstSepAndroid loadAstAndroidConstants() throws Exception {
		return call("loadAstAndroidConstants",DatabaseConstantsAstSepAndroid.class);
	}

	@Override
	public DatabaseConstantsCftAndroid loadCftAndroidConstants() throws Exception {
		return call("loadCftAndroidConstants",DatabaseConstantsCftAndroid.class);
	}

	@Override
	public AndroidStaticDataAstSepTO loadStaticTypeDataForAndroid(String languageCode) throws Exception {
		return call("loadStaticTypeDataForAndroid", AndroidStaticDataAstSepTO.class, languageCode);
	}

	@Override
	public AndroidCachedDataAstSepTO loadDynamicTypeDataForAndroid(String checkSum, String languageCode,String lastRequestTimestamp) throws Exception {
		return call("loadDynamicTypeDataForAndroid", AndroidCachedDataAstSepTO.class, checkSum, languageCode, lastRequestTimestamp);
	}

	@Override
	public List<WorkorderCustomWrapperTO> getAssignedWorkOrders(String username, List<AndroidRequestParametersTO> androidReqParamTOs) throws Exception {
		return callList("getAssignedWorkOrders", WorkorderCustomWrapperTO[].class, username, androidReqParamTOs.toArray(new AndroidRequestParametersTO[0]));
	}

	@Override
	public Long getTimeLeftUntilPasswordExpire(String username) throws Exception {
		return call("getTimeLeftUntilPasswordExpire", Long.class, username);
	}

	@Override
	public UnitInformationTO getDeviceInfo(String identifier, Long idUnitIdentifierT) throws Exception {
		return call("getDeviceInfo", UnitInformationTO.class, identifier, idUnitIdentifierT);
	}

	@Override
	public UnitInformationTO getDeviceInfoWithUnitType(String identifier, Long idUnitIdentifierT, Long idUniT, Long idUnitModelT) throws Exception {
		return call("getDeviceInfoWithUnitType", UnitInformationTO.class, identifier, idUnitIdentifierT, idUniT, idUnitModelT);
	}

	@Override
	public UnitInformationTO getDeviceInfoForDomainWithUnitType(String identifier, Long idUnitIdentifierT, Long idDomain, Long idUniT, Long idUnitModelT) throws Exception {
		return call("getDeviceInfoForDomainWithUnitType", UnitInformationTO.class, identifier, idUnitIdentifierT, idDomain, idUniT, idUnitModelT);
	}

	@Override
	public UnitInformationTO getDeviceInfoWithModel(String identifier, Long idUnitIdentifierT, String unitModel) throws Exception {
		return call("getDeviceInfoWithModel", UnitInformationTO.class, identifier, idUnitIdentifierT, unitModel);
	}

	@Override
	public UnitInformationTO getDeviceInfoForDomain(String identifier, Long idUnitIdentifierT, Long idDomain) throws Exception {
		return call("getDeviceInfoForDomain", UnitInformationTO.class, identifier, idUnitIdentifierT, idDomain);
	}

	@Override
	public PalletLightTO getPallet(String palletCode) throws Exception {
		return call("getPallet", PalletLightTO.class, palletCode);
	}


	@Override
	public void sendPallet(Long idPallet, Long sendingIdStock, Long destinationIdStock, String freigthNumber) throws Exception {
		call("sendPallet", Void.class, idPallet, sendingIdStock, destinationIdStock, freigthNumber);
	}

	@Override
	public void receivePallet(Long idPallet, Long receivingIdStock) throws Exception {
		call("receivePallet", Void.class, idPallet, receivingIdStock);
	}

	@Override
	public void addRemoveUnitsToPallet(Long idPallet, Long sendingIdStock, List<UnitIdentifierTO> unitsToAdd, List<UnitIdentifierTO> unitsToRemove) throws Exception {
		call("addRemoveUnitsToPallet", Void.class, idPallet, sendingIdStock, unitsToAdd.toArray(new UnitIdentifierTO[0]), unitsToRemove.toArray(new UnitIdentifierTO[0]));
	}

	@Override
	public List<UnitIdentifierTO> getUnitsOnPallet(Long idPallet) throws Exception {
		return callList("getUnitsOnPallet", UnitIdentifierTO[].class, idPallet);
	}

	protected <T> T call(String methodName, Class<T> returnType, Object... parameters) throws Exception {
		return call(methodName, returnType, AndroidWebserviceClientAstSep.class, parameters);
	}
	// changes done for reset password functionality
	protected <T> T callForForgotPassword(String methodName, Class<T> returnType, Object... parameters) throws Exception {
		return call(methodName, returnType, ForgotPasswordWebserviceClient.class, parameters);
	}

	protected <T> List<T> callList(String methodName, Class<T[]> returnType, Object... parameters) throws Exception {
		return callList(methodName, returnType, AndroidWebserviceClientAstSep.class, parameters);
	}

	@Override
	public void changeUnitStatus(List<UnitChangeStatusCustomTO> unitChangeStatusCustomTOs) throws Exception {
		call("changeUnitStatus", Void.class, (Object)unitChangeStatusCustomTOs.toArray(new UnitChangeStatusCustomTO[0]));
	}

	@Override
	public StockBalanceTO getStockBalance(Long stockId) throws Exception {
		return call("getStockBalance", StockBalanceTO.class, stockId);
	}

	@Override
	public StocktakingTO getActiveStockTaking(Long stockId) throws Exception {
		return call("getActiveStockTaking", StocktakingTO.class, stockId);
	}

	@Override
	public void addToStock(List<AddToStockTO> addToStockTOs, Date startTimestamp) throws Exception {
		call("addToStock", Void.class, addToStockTOs.toArray(new AddToStockTO[0]), startTimestamp);
	}

	@Override
	public void addToStocktaking(Long idStocktaking, List<AddToStocktakingTO> addToStocktakingTOs, Date startTimestamp) throws Exception {
		call("addToStocktaking", Void.class, idStocktaking, addToStocktakingTOs.toArray(new AddToStocktakingTO[0]), startTimestamp);
	}

	@Override
	public void changeUnitStatusAndSetReason(UnitChangeStatusCustomTO unitChangeStatusCustomTO) throws Exception {
		call("changeUnitStatusAndSetReason", Void.class, unitChangeStatusCustomTO);

	}

	@Override
	public void changeUnitStatusSetReasonAndPallet(UnitChangeStatusCustomTO unitChangeStatusCustomTO)
			throws Exception {
		call("changeUnitStatusSetReasonAndPallet", Void.class, unitChangeStatusCustomTO);

	}

	@Override
	public void logError(ErrorTO errorTO) throws Exception {
		call("logError", Void.class, errorTO);
	}

	@Override
	public UserLoginCustomTO login(AndroidAuthenticationTO androidAuthenticationTO)
			throws Exception {
		return call("login", UserLoginCustomTO.class, androidAuthenticationTO);
	}

	@Override
	public void changeUnitStatusAndChangeStock(List<AddToStockTO> addToStockTOs, Date startTimestamp) throws Exception {
		call("changeUnitStatusAndChangeStock", Void.class, addToStockTOs.toArray(new AddToStockTO[0]), startTimestamp);
	}

	@Override
	public LockWorkorderCustomTO lockWorkorder(final LockWorkorderCustomTO lockWorkorderCustomTO, Boolean lockflag) throws Exception{
		return call("lockWorkorder",LockWorkorderCustomTO.class,lockWorkorderCustomTO,lockflag);
	}

	@Override
	public void saveWorkorder(final WorkorderCustomWrapperTO wo) throws Exception {
		Log.d("AndroidWebserviceClien","AndroidWebserviceClientAstSepImpl-, workorder="+wo.getId());
		call("saveWorkorder",Void.class,wo);
	}

	@Override
	public void addAttachment(AttachmentTO attachmentTO, Long aLong, List<Long> aLong1) {
		//Do nothing
	}

	@Override
	public void changeUnitStatusAndChangeStockWithFilters(List<AddToStockTO> addToStockTOs, Date startTimestamp) throws Exception {
		call("changeUnitStatusAndChangeStockWithFilters", Void.class, addToStockTOs.toArray(new AddToStockTO[0]), startTimestamp);
	}

	@Override
	public List<FieldDocumentInfoTTO> getWorkOrderDocumentInfoList(String loggedInUserName, String documentTypeCode) throws Exception {
		return callList("getWorkOrderDocumentInfoList", FieldDocumentInfoTTO[].class, loggedInUserName, documentTypeCode);
	}
	@Override
	public void addAndroidStatistics(AndroidAuthenticationTO androidAuthenticationTO, Integer totalWoCount,Integer noOfUnsycWorkorders,Integer noOfUnsycPhotos) throws Exception {
		call("addAndroidStatistics", Void.class, androidAuthenticationTO, totalWoCount,noOfUnsycWorkorders,noOfUnsycPhotos);
	}
	@Override
	public void addUserPositions(List<UserPositionTO> userPositionTOs) throws Exception{
		call("addUserPositions", Void.class,(Object)userPositionTOs.toArray(new UserPositionTO[0]) );
	}

	@Override
	public void logout(AndroidAuthenticationTO androidAuthenticationTO)
			throws Exception {
		call("logout", Void.class, androidAuthenticationTO);
	}

	@Override
	public List<NearByWorkOrder> findNearbyWorkOrders(Double aDouble, Double aDouble1, Double aDouble2) throws Exception {
		return callList("findNearbyWorkOrders", NearByWorkOrder[].class,aDouble,aDouble1,aDouble2 );
	}

	@Override
	public void updateFCMToken(FCMTokenTO fcmTokenTO) throws Exception {
		call("updateFCMToken",Void.class,fcmTokenTO);
	}

	@Override
	public void saveAndroidLog(AndroidLogTO androidLogTO) throws Exception {
		call("saveAndroidLog",Void.class,androidLogTO);
	}


	public List<HelpDocBean> getAllHelpDocuments(String dateTime) throws Exception{
		return callList("getAllHelpDocuments",HelpDocBean[].class,dateTime);
	}

	@Override
	public String checkUser(String userName, String contact) throws Exception {
		return callForForgotPassword("checkUser", String.class, userName, contact);
	}

	@Override
	public String validateOTP(String userName, String otp) throws Exception {
		return callForForgotPassword("validateOTP", String.class, userName, otp);
	}

	@Override
	public boolean lockUser(String userName) throws Exception {
		return callForForgotPassword("lockUser", Boolean.class, userName);
	}

	@Override
	public String resetPassword(String userName, String newPassword) throws Exception {
		return callForForgotPassword("resetPassword", String.class, userName, newPassword);
	}

}
