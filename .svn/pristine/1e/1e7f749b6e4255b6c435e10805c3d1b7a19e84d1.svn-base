package com.capgemini.sesp.ast.android.module.util;

import android.util.Log;

import com.skvader.rsp.ast_sep.common.to.ast.table.PdaWoResultConfigTCTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventResultReasonTTO;
import com.skvader.rsp.cft.common.to.TransferObjectUtils;
import com.skvader.rsp.cft.common.to.custom.base.BaseTO;
import com.skvader.rsp.cft.common.to.custom.base.IdDomainTO;
import com.skvader.rsp.cft.common.to.custom.base.LayerInterfaceTO;
import com.skvader.rsp.cft.common.to.custom.base.NameInterfaceTO;
import com.skvader.rsp.cft.common.to.custom.base.SearchableTO;
import com.skvader.rsp.cft.common.to.custom.base.SettableTO;
import com.skvader.rsp.cft.common.to.custom.base.SortOrderTO;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class TypeDataUtil {

	/**
	 * Get Result Reason type
	 * @param idCaseType
	 * @param resultReasonType
	 * @param listOfPdaWoResultReasonCfgs
	 * @param woEventResultReasonTTOList
	 * @return
	 */
	public static List<WoEventResultReasonTTO> getResultReasonType(Long idCaseType, Long resultReasonType,
											 List<PdaWoResultConfigTCTO> listOfPdaWoResultReasonCfgs,  List<WoEventResultReasonTTO> woEventResultReasonTTOList){
		List<WoEventResultReasonTTO> woEventResultReasonTTOs = new ArrayList<WoEventResultReasonTTO>();
		Log.i("TypeDataUtil","inside getResultReasonType()");
		for(PdaWoResultConfigTCTO pdaWoResultConfigTCTO : listOfPdaWoResultReasonCfgs){
			if((pdaWoResultConfigTCTO.getIdCaseT().longValue() == idCaseType.longValue())
					&& (pdaWoResultConfigTCTO.getIdPdaPageT().longValue() == resultReasonType.longValue())){
				Log.i("TypeDataUtil : ","inside getResultReasonType() if condition ");
				for(WoEventResultReasonTTO woEventResultReasonTTO : woEventResultReasonTTOList){
					if(pdaWoResultConfigTCTO.getIdWoEventResultReasonT().longValue() == woEventResultReasonTTO.getId().longValue()){
						woEventResultReasonTTOs.add(woEventResultReasonTTO);
					}
				}
			}
		}

		Collections.sort(woEventResultReasonTTOs, new Comparator<WoEventResultReasonTTO>() {
			@Override
			public int compare(WoEventResultReasonTTO o1, WoEventResultReasonTTO o2) {
				return o1.getCode().compareTo(o2.getCode());
			}
		});

		return woEventResultReasonTTOs;
	}

	/**
	 * Method to check matching domain Ids
	 * @param idDomain
	 * @param idDomains
	 * @return
	 */
	public static boolean isMatchingDomain(Long idDomain, Long[] idDomains) {
		if (idDomain == null || idDomains == null || idDomains.length == 0) {
			return true;
		} else {
			for (int i = 0; i < idDomains.length; i++) {
				if (idDomain.equals(idDomains[i])) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get original or deviated value
	 * @param org
	 * @param div
	 * @param flag
	 * @return String
	 */
	public static String getOrgOrDiv(String org, String div, Long flag){
		if (flag != null && flag.equals(AndroidUtilsAstSep.CONSTANTS().SYSTEM_BOOLEAN_T__TRUE)) {
			return div==null?"":div;
		} else {
			return org==null?"":org;
		}
	}

	/**
	 * Get original or deviated value
	 * @param org
	 * @param div
	 * @param flag
	 * @return Long
	 */
	public static Long getOrgOrDiv(Long org, Long div, Long flag){
		if (flag != null && flag.equals(AndroidUtilsAstSep.CONSTANTS().SYSTEM_BOOLEAN_T__TRUE)) {
			return div;
		} else {
			return org;
		}
	}

	/**
	 * Get original or deviated value
	 * @param org
	 * @param div
	 * @param flag
	 * @return Double
	 */
	public static Double getOrgOrDiv(Double org, Double div, Long flag){
		if (flag != null && flag.equals(AndroidUtilsAstSep.CONSTANTS().SYSTEM_BOOLEAN_T__TRUE)) {
			return div;
		} else {
			return org;
		}
	}

	/**
	 * Get Valid Original Deviated Value
	 *
	 * @param bean
	 * @param parameter
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValidOrgDivValue(BaseTO bean, String parameter) {
		try {
			if (parameter.toLowerCase(Locale.getDefault()).endsWith("_d") || parameter.toLowerCase().endsWith("_v") || parameter.toLowerCase().endsWith("_o")) {
				parameter = parameter.substring(0, parameter.length() - 2);
			}
			String destFieldGetBase = "get" + TransferObjectUtils.convertDbToJavaClassName(parameter);
			Method getMethodV = bean.getClass().getMethod(destFieldGetBase + "V");

			if (AndroidUtilsAstSep.CONSTANTS().SYSTEM_BOOLEAN_T__TRUE.equals(getMethodV.invoke(bean))) {
				Method getMethodD = bean.getClass().getMethod(destFieldGetBase + "D");
				return (T) getMethodD.invoke(bean);
			} else {
				Method getMethodO = bean.getClass().getMethod(destFieldGetBase + "O");
				return (T) getMethodO.invoke(bean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Map Value into DOV
	 * @param srcValue
	 * @param dstBean
	 * @param dstField
	 * @param constants
	 * @throws Exception
	 */
	public static void mapValueIntoDOV(Object srcValue, BaseTO dstBean, String dstField) throws Exception {
		boolean dstOrgDivType = (dstField.toLowerCase().endsWith("_d") || dstField.toLowerCase().endsWith("_v") || dstField.toLowerCase().endsWith("_o"));
		if (dstOrgDivType) {
			dstField = dstField.substring(0, dstField.length() - 2);
		}

		String dstMethodBaseName = TransferObjectUtils.convertDbToJavaClassName(dstField);
		Object[] valueParams = new Object[]{srcValue};
		if (dstOrgDivType) {
			Method dstMethod = dstBean.getClass().getMethod("get" + dstMethodBaseName + "D");
			Class[] typeParams = new Class[]{dstMethod.getReturnType()};

			Method dstGetMethodV = dstBean.getClass().getMethod("get" + dstMethodBaseName + "V");
			Long idValid = (Long)dstGetMethodV.invoke(dstBean, new Object[]{});

			if (idValid == null) {
				idValid = AndroidUtilsAstSep.CONSTANTS().SYSTEM_BOOLEAN_T__TRUE;
				Method dstSetMethodV = dstBean.getClass().getMethod("set" + dstMethodBaseName + "V", Long.class);
				dstSetMethodV.invoke(dstBean, idValid);

				Method dstSetMethodO = dstBean.getClass().getMethod("set" + dstMethodBaseName + "D", typeParams);
				dstSetMethodO.invoke(dstBean, valueParams);
			} else 	if (AndroidUtilsAstSep.CONSTANTS().SYSTEM_BOOLEAN_T__TRUE.equals(idValid)) {
				Method dstSetMethodD = dstBean.getClass().getMethod("set" + dstMethodBaseName + "D", typeParams);
				dstSetMethodD.invoke(dstBean, valueParams);
			} else {
				Method dstGetMethodO = dstBean.getClass().getMethod("get" + dstMethodBaseName + "O");
				Object valueO = dstGetMethodO.invoke(dstBean);
				if (!equals(valueO, srcValue)) {
					//set V true and D
					idValid = AndroidUtilsAstSep.CONSTANTS().SYSTEM_BOOLEAN_T__TRUE;
					Method dstSetMethodV = dstBean.getClass().getMethod("set" + dstMethodBaseName + "V", Long.class);
					dstSetMethodV.invoke(dstBean, idValid);

					Method dstSetMethodD = dstBean.getClass().getMethod("set" + dstMethodBaseName + "D", typeParams);
					dstSetMethodD.invoke(dstBean, valueParams);
				}
			}
		} else {
			Method dstMethod = dstBean.getClass().getMethod("get" + dstMethodBaseName);
			Class[] typeParams = new Class[]{dstMethod.getReturnType()};

			Method dstSetMethod = dstBean.getClass().getMethod("set" + dstMethodBaseName, typeParams);
			dstSetMethod.invoke(dstBean, new Object[]{valueParams});
		}
	}

	private static boolean equals(Object o1, Object o2) {
		if(o1 == o2) {
			return true;
		}
		return o1 != null && o1.equals(o2);
	}

	/**
	 * Filters a list to have only the USER_SETTABLE = 1 and SYSTEM_ENABLED = 1 and USER_SEARCHABLE = 1
	 * @param originalList
	 * @return
	 */
	public static <T extends SettableTO & SearchableTO> List<T> filterUserSettableAndSystemEnabled(List<T> originalList) {
		List<T> filteredList = new ArrayList<T>();
		for(T obj : originalList) {
			if(obj.getUserSearchable().equals(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE) && obj.getUserSettable().equals(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE)) {
				try {
					Method getSystemEnabledMethod = obj.getClass().getMethod("getSystemEnabled");
					Long idValid = (Long)getSystemEnabledMethod.invoke(obj, new Object[]{});
					if(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE.equals(idValid)) {
						filteredList.add(obj);
					}
				} catch (Exception e) {
					writeLog(TypeDataUtil.class.getSimpleName() + ":filterUserSettableAndSystemEnabled " ,e);
				}
			}
		}
		return filteredList;
	}

	/**
	 * Filter type data by domain
	 * @param objectList
	 * @param idDomainList
	 * @return List
	 */
	public static <T extends IdDomainTO & SettableTO & SearchableTO & NameInterfaceTO & SortOrderTO> List<T> filterTypeDataListByDomain(List<T> objectList, List<Long> idDomainList) {
		Long[] idDomains = new Long[idDomainList.size()];
		idDomainList.toArray(idDomains);
		if (objectList == null) return null;
		List<T> v = new ArrayList<T>();
		for (T object : objectList) {
			if (idDomains.length>0) {
				Long idDomain = object.getIdDomain();
				if (TypeDataUtil.isMatchingDomain(idDomain,idDomains)) {
					v.add(object);
				}
			}
		}
		Collections.sort(v, new Comparator<T>() {
			@Override
			public int compare(T object1, T object2) {
				if (object1 == null || object2 == null)
					return -1;

				if (object1.getSortOrder() != null && object2.getSortOrder() != null) {
					return object1.getSortOrder().compareTo(object2.getSortOrder());
				}

				if (object1.getName() != null && object2.getName() != null) {
					return object1.getName().toUpperCase().compareTo(object2.getName().toUpperCase());
				}

				//keep existing order
				return -1;
			}
		});
		return filterUserSettableAndSystemEnabled(v);
	}

	public static <T extends LayerInterfaceTO> List<T> filterBySystemLayerType(List<T> data, Long idSystemLayerT) {
		if(idSystemLayerT == null) {
			return data;
		}
		List<T> tos = new ArrayList<T>();
		for(T typeData: data){
			if(typeData.getIdSystemLayerT().longValue()==idSystemLayerT.longValue()){
				tos.add(typeData);
			}
		}
		return tos;
	}


}
