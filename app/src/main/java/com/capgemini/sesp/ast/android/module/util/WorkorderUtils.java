package com.capgemini.sesp.ast.android.module.util;


import android.util.ArrayMap;
import android.util.Log;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.UnitType;
import com.capgemini.sesp.ast.android.module.util.comparators.TypeDataComparator;
import com.skvader.rsp.ast_sep.common.to.ast.table.MeterCommunicationCheckResultTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.PdaCaseTHTAttRTCTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoAdditionalDataTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoAdditionalDataValueTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoAddressTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoCommChannelTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventFvTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventResultReasonTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventResultReasonTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventTechPlanTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoFuseTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstElTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstMepCollectionTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstMepTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoAdditionalDataCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoContactCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoCustTimeReservCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoEventCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoEventFieldVisitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInfoCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInstCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInstElCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInstMeasurepointCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoInstMepElCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCfgTCustomTO;
import com.skvader.rsp.cft.common.to.cft.table.AttachmentReasonTTO;
import com.skvader.rsp.cft.common.to.cft.table.AttachmentTO;
import com.skvader.rsp.cft.common.to.cft.table.CustCommChannelTO;
import com.skvader.rsp.cft.common.to.cft.table.DomainRelationTO;
import com.skvader.rsp.cft.common.to.cft.table.InfoTO;
import com.skvader.rsp.cft.common.to.cft.table.SystemParameterTO;
import com.skvader.rsp.cft.common.to.custom.casehandler.CaseCustWrapperCustomTO;
import com.skvader.rsp.cft.common.util.Utils;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.CaseTCustomTO;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.DomainCustomTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class WorkorderUtils {

    public static UnitType unitType = null;
    private final static String TAG = WorkorderUtils.class.getSimpleName();

    public static String getPostcode(WorkorderCustomWrapperTO wo, Long idWoContactT) {
        WoContactCustomTO contacts = getWoInstMepContact(wo, idWoContactT);
        if (contacts != null) {
            //list is sorted by SortOrderTO
            WoAddressTO address = contacts.getAddresses().get(0);
            if (address != null)
                return TypeDataUtil.getOrgOrDiv(address.getPostcodeO(), address.getPostcodeD(), address.getPostcodeV());
            else
                return null;
        } else
            return null;
    }

    public static String getCity(WorkorderCustomWrapperTO wo, Long idWoContactT) {
        WoContactCustomTO contacts = getWoInstMepContact(wo, idWoContactT);
        if (contacts != null) {
            //list is sorted by SortOrderTO
            WoAddressTO address = contacts.getAddresses().get(0);

            if (address != null)
                return TypeDataUtil.getOrgOrDiv(address.getCityO(), address.getCityD(), address.getCityV());
            else
                return null;
        } else
            return null;
    }

    public static String getPhoneNumber(WorkorderCustomWrapperTO wo, Long idWoContactT, Long idCustCommChannelT) {
        WoContactCustomTO contacts = getWoInstMepContact(wo, idWoContactT);
        if (contacts != null) {
            WoCommChannelTO mobile = getCommChannel(contacts, idCustCommChannelT);
            if (mobile != null)
                return TypeDataUtil.getOrgOrDiv(mobile.getValueO(), mobile.getValueD(), mobile.getValueV());
            else
                return null;
        } else
            return null;
    }
    public static String getContactDetails(final WorkorderCustomWrapperTO wo, final Long idCaseCustT, final Long idCustCommChannelT){
        StringBuffer info = new StringBuffer("");
        List<CaseCustWrapperCustomTO> caseCustWrapperCustomTOs = wo.getWorkorderCustomTO().getCaseCustWrapperCustomTOs();
        if(Utils.isNotEmpty(caseCustWrapperCustomTOs)){
            for(CaseCustWrapperCustomTO caseCustWrapperCustomTO : caseCustWrapperCustomTOs){
                if(caseCustWrapperCustomTO != null && caseCustWrapperCustomTO.getIdCaseCustT() != null
                        && caseCustWrapperCustomTO.getIdCaseCustT().longValue() == idCaseCustT && Utils.isNotEmpty(caseCustWrapperCustomTO.getCustCommChannelTOs())){
                    for(CustCommChannelTO custCommChannelTO : caseCustWrapperCustomTO.getCustCommChannelTOs()){
                        if(custCommChannelTO.getIdCustCommChannelT().longValue() == idCustCommChannelT.longValue()
                                && Utils.isNotEmpty(custCommChannelTO.getInfo())){
                            if(Utils.isNotEmpty(info) && info.length()>0){
                                info.append(", ");
                            }
                            info.append(custCommChannelTO.getInfo());
                        }
                    }
                }
            }
        }

        return info.toString();

    }
    public static String getSurname(WorkorderCustomWrapperTO wo, Long idWoContactT) {
        WoContactCustomTO contacts = getWoInstMepContact(wo, idWoContactT);
        if (contacts != null)
            return TypeDataUtil.getOrgOrDiv(contacts.getWoContactTO().getFirstNameO(), contacts.getWoContactTO().getFirstNameD(), contacts.getWoContactTO().getFirstNameV());
        else
            return null;
    }

    public static String getLastname(WorkorderCustomWrapperTO wo, Long idWoContactT) {
        WoContactCustomTO contacts = getWoInstMepContact(wo, idWoContactT);
        if (contacts != null)
            return TypeDataUtil.getOrgOrDiv(contacts.getWoContactTO().getLastNameO(), contacts.getWoContactTO().getLastNameD(), contacts.getWoContactTO().getLastNameV());
        else
            return null;
    }

    public static String getWoInstStreetInfo(WorkorderCustomWrapperTO wo) {
        String street = "";
        try {
            WoAddressTO address = wo.getWorkorderCustomTO().getWoInst().getAddress();
            street = getWoAddressStreetInfoText(address);
        } catch (Exception e) {
            writeLog(TAG + ":getWoInstStreetInfo() ", e);
        }
        return street;
    }

    public static String getWoAddressStreetInfoText(WoAddressTO address) {
        String streetInfo = "";
        try {

            String street = TypeDataUtil.getOrgOrDiv(address.getStreetO(), address.getStreetD(), address.getStreetV());
            if (GuIUtil.notNullnotEmpty(street)) {
                streetInfo = streetInfo.length() > 0 ? streetInfo + " " + street : street;
            }

            String streetNum = TypeDataUtil.getOrgOrDiv(address.getStreetNumberO(), address.getStreetNumberD(), address.getStreetNumberV());
            if (GuIUtil.notNullnotEmpty(streetNum)) {
                streetInfo = streetInfo.length() > 0 ? streetInfo + " " + streetNum : streetNum;
            }

            String letter = TypeDataUtil.getOrgOrDiv(address.getLetterO(), address.getLetterD(), address.getLetterV());
            if (GuIUtil.notNullnotEmpty(letter)) {
                streetInfo = streetInfo.length() > 0 ? streetInfo + " " + letter : letter;
            }

            String entrance = TypeDataUtil.getOrgOrDiv(address.getEntranceO(), address.getEntranceD(), address.getEntranceV());
            if (GuIUtil.notNullnotEmpty(entrance)) {
                streetInfo = streetInfo.length() > 0 ? streetInfo + " " + entrance : entrance;
            }

            String apNo = TypeDataUtil.getOrgOrDiv(address.getApartmentNumberO(), address.getApartmentNumberD(), address.getApartmentNumberV());
            if (GuIUtil.notNullnotEmpty(apNo)) {
                streetInfo = streetInfo.length() > 0 ? streetInfo + " " + apNo : apNo;
            }

            String stairs = TypeDataUtil.getOrgOrDiv(address.getStairsO(), address.getStairsD(), address.getStairsV());
            if (GuIUtil.notNullnotEmpty(stairs)) {
                streetInfo = streetInfo.length() > 0 ? streetInfo + " " + stairs : stairs;
            }

        } catch (Exception e) {
            writeLog(TAG + ":getWoInstStreetInfoTEXT() ", e);
        }
        return streetInfo;
    }

    public static String getWoAddressStreetPostalCodeInfoText(WoAddressTO address, CaseTCustomTO caseTCustomTO) {
        String streetInfo = "";
        try {

            String street = TypeDataUtil.getOrgOrDiv(address.getStreetO(), address.getStreetD(), address.getStreetV());
            if (GuIUtil.notNullnotEmpty(street)) {
                streetInfo = streetInfo.length() > 0 ? streetInfo + " " + street.trim() : street.trim();
            }

            String streetNum = TypeDataUtil.getOrgOrDiv(address.getStreetNumberO(), address.getStreetNumberD(), address.getStreetNumberV());
            if (GuIUtil.notNullnotEmpty(streetNum)) {
                streetInfo = streetInfo.length() > 0 ? streetInfo + " " + streetNum.trim() : streetNum.trim();
            }

            String letter = TypeDataUtil.getOrgOrDiv(address.getLetterO(), address.getLetterD(), address.getLetterV());
            if (GuIUtil.notNullnotEmpty(letter)) {
                streetInfo = streetInfo.length() > 0 ? streetInfo + " " + letter.trim() : letter.trim();
            }

            String entrance = TypeDataUtil.getOrgOrDiv(address.getEntranceO(), address.getEntranceD(), address.getEntranceV());
            if (GuIUtil.notNullnotEmpty(entrance)) {
                streetInfo = streetInfo.length() > 0 ? streetInfo + " " + entrance.trim() : entrance.trim();
            }

            String apNo = TypeDataUtil.getOrgOrDiv(address.getApartmentNumberO(), address.getApartmentNumberD(), address.getApartmentNumberV());
            if (GuIUtil.notNullnotEmpty(apNo)) {
                streetInfo = streetInfo.length() > 0 ? streetInfo + " " + apNo.trim() : apNo.trim();
            }

            String stairs = TypeDataUtil.getOrgOrDiv(address.getStairsO(), address.getStairsD(), address.getStairsV());
            if (GuIUtil.notNullnotEmpty(stairs)) {
                streetInfo = streetInfo.length() > 0 ? streetInfo + " " + stairs.trim() : stairs.trim();
            }
            if (!(caseTCustomTO.getCode().equalsIgnoreCase("CONC_INSTALLATION_FIELD") || caseTCustomTO.getCode().equalsIgnoreCase("REPEATER_INSTALLATION_FIELD")
                    || caseTCustomTO.getCode().equalsIgnoreCase("TS_MUP_CONC_FIELD") || caseTCustomTO.getCode().equalsIgnoreCase("TS_MUP_REPEATER_FIELD"))) {
                String postalCode = TypeDataUtil.getOrgOrDiv(address.getPostcodeO(), address.getPostcodeD(), address.getPostcodeV());

                if (GuIUtil.notNullnotEmpty(postalCode)) {
                    streetInfo = streetInfo.length() > 0 ? streetInfo + ", " + postalCode.trim() : postalCode.trim();
                }
            }


        } catch (Exception e) {
            writeLog(TAG + ":getWoAddressStreetPostalCodeInfoText() ", e);
        }
        return streetInfo;
    }

    public static String getWoAddressStreetInfo(WorkorderCustomWrapperTO wo, Long idWoContactT) {

        WoContactCustomTO contacts = getWoInstMepContact(wo, idWoContactT);

        //list is sorted by SortOrderTO
        if (contacts != null) {
            WoAddressTO address = contacts.getAddresses().get(0);

            if (address != null) {
                return getWoAddressStreetInfoText(address);
            } else
                return null;
        } else
            return null;
    }

    private static WoContactCustomTO getWoInstMepContact(final WorkorderCustomWrapperTO wo, final Long idWoContactT) {
        WoContactCustomTO res = null;
        // No null pointer error and abrupt crash
        if (wo != null
                && wo.getWorkorderCustomTO() != null
                && wo.getWorkorderCustomTO().getWoInst() != null
                && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint() != null) {

            final List<WoContactCustomTO> contacts = wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getContacts();
            if (contacts != null && !contacts.isEmpty()) {
                for (WoContactCustomTO contactCustomTo : contacts) {
                    // Will be later removed by JXPath expression
                    if (contactCustomTo != null
                            && contactCustomTo.getWoContactTO() != null
                            && contactCustomTo.getWoContactTO().getIdWoContactT().equals(idWoContactT)) {
                        res = contactCustomTo;
                        break;
                    }
                }
            }
        }
        return res;
    }

    private static WoAddressTO getAddress(WoContactCustomTO woInstMepContact, Long idWoAddressT) {

        WoAddressTO res = null;
        if (woInstMepContact != null) {
            List<WoAddressTO> address = woInstMepContact.getAddresses();
            for (int i = 0; i < address.size(); i++) {
                if (address.get(i).getIdWoAddressT().equals(idWoAddressT)) {
                    res = address.get(i);
                    break;
                }
            }
        }
        return res;
    }

    private static WoCommChannelTO getCommChannel(WoContactCustomTO woInstMepContact, Long idCustCommChannelT) {

        WoCommChannelTO res = null;
        List<WoCommChannelTO> comm = woInstMepContact.getCommunicationChannels();
        for (int i = 0; i < comm.size(); i++) {
            if (comm.get(i).getIdWoCommChannelT().equals(idCustCommChannelT)) {
                res = comm.get(i);
                break;
            }
        }
        return res;


    }

    /**
     * FETCH WoEventCustomTO based on ID_WO_EVENT_T
     *
     * @param wo
     * @param techPlanId
     * @return
     */
    public static WoEventCustomTO getWoEventCustom(WorkorderCustomTO wo, Long techPlanId) {
        WoEventCustomTO res = null;
        List<WoEventCustomTO> list = wo.getWoEvents();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getWoEventTO().getIdWoEventT().equals(techPlanId)) {
                res = list.get(i);
            }
        }

        return res;
    }

    public static String getNetstation(WorkorderCustomWrapperTO wo) {
        WoInstElTO instEl = wo.getWorkorderCustomTO().getWoInst().getInstElectrical().getWoInstElTO();
        return TypeDataUtil.getOrgOrDiv(instEl.getNetstationO(), instEl.getNetstationD(), instEl.getNetstationV());
    }

    public static String getCableBox(WorkorderCustomWrapperTO wo) {
        WoInstElTO instEl = wo.getWorkorderCustomTO().getWoInst().getInstElectrical().getWoInstElTO();
        return TypeDataUtil.getOrgOrDiv(instEl.getCableboxO(), instEl.getCableboxD(), instEl.getCableboxV());
    }

    public static String getInstCity(WorkorderCustomWrapperTO wo) {
        WoAddressTO address = wo.getWorkorderCustomTO().getWoInst().getAddress();
        return TypeDataUtil.getOrgOrDiv(address.getCityO(), address.getCityD(), address.getCityV());
    }

    public static String getInstPostCode(WorkorderCustomWrapperTO wo) {
        WoAddressTO address = wo.getWorkorderCustomTO().getWoInst().getAddress();
        return TypeDataUtil.getOrgOrDiv(address.getPostcodeO(), address.getPostcodeD(), address.getPostcodeV());
    }

    public static String getInstStreet(WorkorderCustomWrapperTO wo) {
        WoAddressTO address = wo.getWorkorderCustomTO().getWoInst().getAddress();
        return TypeDataUtil.getOrgOrDiv(address.getStreetO(), address.getStreetD(), address.getStreetV());
    }

    public static String getInstStreetNo(WorkorderCustomWrapperTO wo) {
        WoAddressTO address = wo.getWorkorderCustomTO().getWoInst().getAddress();
        return TypeDataUtil.getOrgOrDiv(address.getStreetNumberO(), address.getStreetNumberD(), address.getStreetNumberV());
    }

    public static String getYCoord(WorkorderCustomWrapperTO wo) {
        WoInstTO inst = wo.getWorkorderCustomTO().getWoInst().getWoInstTO();
        return TypeDataUtil.getOrgOrDiv(inst.getYCoordinateO(), inst.getYCoordinateD(), inst.getYCoordinateV());
    }

    public static String getXCoord(WorkorderCustomWrapperTO wo) {
        WoInstTO inst = wo.getWorkorderCustomTO().getWoInst().getWoInstTO();
        return TypeDataUtil.getOrgOrDiv(inst.getXCoordinateO(), inst.getXCoordinateD(), inst.getXCoordinateV());
    }

    public static String getKeyNumber(WorkorderCustomWrapperTO wo) {
        WoInstTO inst = wo.getWorkorderCustomTO().getWoInst().getWoInstTO();
        return TypeDataUtil.getOrgOrDiv(inst.getKeyNumberO(), inst.getKeyNumberD(), inst.getKeyNumberV());
    }

    public static String getKeyInfo(WorkorderCustomWrapperTO wo) {
        WoInstTO inst = wo.getWorkorderCustomTO().getWoInst().getWoInstTO();
        return TypeDataUtil.getOrgOrDiv(inst.getKeyInfoO(), inst.getKeyInfoD(), inst.getKeyInfoV());
    }

    public static String getExtraPlmntInfo(WorkorderCustomWrapperTO wo) {
        WoInstMepTO instMep = wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO();
        return TypeDataUtil.getOrgOrDiv(instMep.getAdditionalMeterPlmtInfoO(), instMep.getAdditionalMeterPlmtInfoD(), instMep.getAdditionalMeterPlmtInfoV());
    }

    public static String getMultipointCode(WorkorderCustomWrapperTO wo) {
        return wo.getWorkorderCustomTO().getWoInst().getInstMultipoint().getWoInstMupTO().getMultipointCode();
    }

    public static Long getIdMeaCategoryT(WorkorderCustomWrapperTO wo) {
        WoInstMepTO woInstMepTO = wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO();
        return TypeDataUtil.getOrgOrDiv(woInstMepTO.getIdMeaCategoryTO(), woInstMepTO.getIdMeaCategoryTD(), woInstMepTO.getIdMeaCategoryTV());
    }

    /**
     * Get Field visit informataion
     *
     * @param wo
     * @return WoEventFieldVisitCustomTO
     */
    public static WoEventFieldVisitCustomTO getWoEventFieldVisitCustomTO(WorkorderCustomWrapperTO wo) {
        WoEventCustomTO woEventCustomTO = getOrCreateWoEventCustomTO(wo,
                CONSTANTS().WO_EVENT_T__FIELD_VISIT, CONSTANTS().WO_EVENT_SOURCE_T__SESP_PDA);
        WoEventFieldVisitCustomTO woEventFieldVisitCustomMTO = woEventCustomTO.getFieldVisit();
        if (woEventFieldVisitCustomMTO == null) {
            woEventFieldVisitCustomMTO = new WoEventFieldVisitCustomTO();
            woEventCustomTO.setFieldVisit(woEventFieldVisitCustomMTO);
        }
        return woEventFieldVisitCustomMTO;
    }

    /**
     * Set Field Visit information
     *
     * @param wo
     * @param startTimeDate
     * @param endTimeDate
     * @param travelTimeInMinutes
     * @param travel_km
     */
    public static void setFieldVisit(WorkorderCustomWrapperTO wo, Date startTimeDate, Date endTimeDate, Long travelTimeInMinutes, Long travel_km) {

        WoEventFieldVisitCustomTO woEventFieldVisitCustomTO = getWoEventFieldVisitCustomTO(wo);

        WoEventFvTO woEventFvTO = new WoEventFvTO();
        woEventFieldVisitCustomTO.setWoEventFvTO(woEventFvTO);

        woEventFvTO.setIdCase(wo.getIdCase());
        woEventFvTO.setFieldVisitId(wo.getFieldVisitID());

        if (startTimeDate != null) {
            woEventFvTO.setFieldVisitStart(startTimeDate);
        }
        if (endTimeDate != null) {
            woEventFvTO.setFieldVisitEnd(endTimeDate);
        }

        if (travelTimeInMinutes != null) {
            woEventFvTO.setTravelTime(travelTimeInMinutes);
        }
        if (travel_km != null) {
            woEventFvTO.setTravelDistance(travel_km);
        }

        woEventFvTO.setPdaVersion("Android Version :: " + android.os.Build.VERSION.SDK_INT);
    }


    /**
     * Get Technology Plan name of the Work order
     *
     * @param wo
     * @return
     */
    public static String getTechnologyPlanName(final WorkorderCustomWrapperTO wo) {
        String techPlanName = null;
        for (final WoEventCustomTO woEvent : wo.getWorkorderCustomTO().getWoEvents()) {
            final WoEventTechPlanTO woEventTechPlan = woEvent.getTechnicalPlanning();
            if (woEventTechPlan != null) {
                techPlanName = ObjectCache.getIdObjectName(UnitModelCfgTCustomTO.class, woEventTechPlan.getIdUnitModelCfgT());
                break;
            }
        }
        return techPlanName;
    }

    /**
     * Create Information
     *
     * @param note
     * @param userId
     * @param idInfoSourceT
     * @param idInfoT
     * @return WoInfoCustomTO
     */
    public static WoInfoCustomTO createInfo(String note, String userId, Long idInfoSourceT, Long idInfoT) {
        WoInfoCustomTO woInfoCustomTO = new WoInfoCustomTO();
        InfoTO infoTO = new InfoTO();
        infoTO.setText(note);
        infoTO.setSignature(userId);
        infoTO.setTimestamp(new Date());
        infoTO.setIdInfoSourceT(idInfoSourceT);
        infoTO.setIdInfoT(idInfoT);
        woInfoCustomTO.setInfo(infoTO);
        return woInfoCustomTO;
    }


    /**
     * Adds a woInfoCustomMTO to the workorderMTO infoList, the array of woInfoCustomMTOs will be created if it does not exist
     *
     * @param workorderTO
     * @param woInfoCustomTO
     */
    public static void addWoInfoCustomMTO(WorkorderCustomWrapperTO workorderTO, WoInfoCustomTO woInfoCustomTO) {

        List<WoInfoCustomTO> woInfoCustomTOList = workorderTO.getWorkorderCustomTO().getInfoList();

        if (woInfoCustomTOList == null) {
            woInfoCustomTOList = new ArrayList<WoInfoCustomTO>();
        }
        woInfoCustomTOList.add(woInfoCustomTO);
        workorderTO.getWorkorderCustomTO().setInfoList(woInfoCustomTOList);
    }

    /**
     * Adds event result reasons to current unsaved (id is null) field visit, with the option to not add duplicates.
     *
     * @param workorderTO
     * @param woEventResultReasonList
     * @param onlyIfNotExists         set to true in order to prevent already existing result reasons from being be added again (normally what you want)
     */
    public static void addEventResultReasons(WorkorderCustomWrapperTO workorderTO, Set<WoEventResultReasonTTO> woEventResultReasonList, boolean onlyIfNotExists) {
        WoEventCustomTO woEventCustomTO = getOrCreateWoEventCustomTO(workorderTO, CONSTANTS().WO_EVENT_T__FIELD_VISIT, CONSTANTS().WO_EVENT_SOURCE_T__SESP_PDA);
        List<WoEventResultReasonTO> woEventResultReasonTOs = woEventCustomTO.getResultReasons();
        List<WoEventResultReasonTO> toAddList = new ArrayList<WoEventResultReasonTO>();
        if (woEventCustomTO.getResultReasons() == null) {
            woEventResultReasonTOs = new ArrayList<WoEventResultReasonTO>();
            woEventCustomTO.setResultReasons(woEventResultReasonTOs);
        }

        for (WoEventResultReasonTTO woEventResultReasonTTO : woEventResultReasonList) {
            boolean alreadyExist = false;
            for (WoEventResultReasonTO woEventResultReasonTO : woEventResultReasonTOs) {
                if (woEventResultReasonTO.getIdWoEventResultReasonT().equals(woEventResultReasonTTO.getId())) {
                    alreadyExist = true;
                    break;
                }
            }
            if (!(onlyIfNotExists && alreadyExist)) {
                WoEventResultReasonTO woEventResultReasonTO = new WoEventResultReasonTO();
                woEventResultReasonTO.setIdCase(workorderTO.getIdCase());
                woEventResultReasonTO.setIdWoEventResultReasonT(woEventResultReasonTTO.getId());
                toAddList.add(woEventResultReasonTO);
            }
        }
        woEventResultReasonTOs.addAll(toAddList);
    }

    /**
     * Gets all  attachments for a workorder.
     *
     * @return
     */
    public static List<AttachmentTO> getAttachmentsByType(WorkorderCustomWrapperTO wo, Long idAttachMentType) {
        List<AttachmentTO> attachmentTOList = new ArrayList<AttachmentTO>();
        if (wo != null
                && wo.getWorkorderCustomTO() != null
                && wo.getWorkorderCustomTO().getAttachmentTOLs() != null) {
            for (AttachmentTO tempAttachment : wo.getWorkorderCustomTO().getAttachmentTOLs()) {
                if (tempAttachment.getIdAttachmentT().longValue() == idAttachMentType.longValue()) {
                    attachmentTOList.add(tempAttachment);
                }
            }
        }
        return attachmentTOList;
    }

    /**
     * Gets all  attachments for a workorder.
     *
     * @return
     */
    public static List<AttachmentTO> getAllAttachments(WorkorderCustomWrapperTO wo) {
        List<AttachmentTO> attachmentTOList = new ArrayList<AttachmentTO>();
        if (wo != null
                && wo.getWorkorderCustomTO() != null
                && wo.getWorkorderCustomTO().getAttachmentTOLs() != null) {
            for (AttachmentTO tempAttachment : wo.getWorkorderCustomTO().getAttachmentTOLs()) {
                attachmentTOList.add(tempAttachment);
            }
        }
        return attachmentTOList;
    }

    /**
     * All infos of the type woInfoCustomTO.getIdInfoT() will be removed from the workorderTO,
     * the one woInfoCustomTO instance will take their place.
     *
     * @param workorderTO
     * @param woInfoCustomTO
     */
    public static void deleteAndAddWoInfoCustomMTO(WorkorderCustomWrapperTO workorderTO, WoInfoCustomTO woInfoCustomTO) {
        deleteWoInfoCustomMTO(workorderTO, woInfoCustomTO);
        workorderTO.getWorkorderCustomTO().getInfoList().add(woInfoCustomTO);
    }

    /**
     * Delete the instance of woInfoCustomTO from workorder object
     *
     * @param workorderTO
     * @param woInfoCustomTO
     */
    public static void deleteWoInfoCustomMTO(WorkorderCustomWrapperTO workorderTO, WoInfoCustomTO woInfoCustomTO) {

        Long deleteIdInfoT = woInfoCustomTO.getInfo().getIdInfoT();
        if (workorderTO.getWorkorderCustomTO().getInfoList() == null) {
            workorderTO.getWorkorderCustomTO().setInfoList(new ArrayList<WoInfoCustomTO>());
        }
        Iterator<WoInfoCustomTO> infoCustomToItr = workorderTO.getWorkorderCustomTO().getInfoList().iterator();
        while (infoCustomToItr.hasNext()) {
            WoInfoCustomTO tempInfoCustomTo = infoCustomToItr.next();
            if (deleteIdInfoT.longValue() == tempInfoCustomTo.getInfo().getIdInfoT().longValue()) {
                infoCustomToItr.remove();
                continue;
            }
        }
    }

    public static List<WoInfoCustomTO> getWoInfoCustomTOOfType(WorkorderCustomWrapperTO workorderCustomWrapperTO,Long infoType){

        List<WoInfoCustomTO> infoList=workorderCustomWrapperTO.getWorkorderCustomTO().getInfoList();
        ArrayList<WoInfoCustomTO> returnValues = new ArrayList<>();

        for(WoInfoCustomTO woInfoCustomTO:infoList){
            if (woInfoCustomTO.getInfo().getIdInfoT().longValue() == infoType.longValue()){
                returnValues.add(woInfoCustomTO);
            }
        }

        return returnValues;
    }

    /**
     * @param workorderCustomWrapperTO
     * @param woEventT
     * @param idWoEventSourceT
     * @return
     */

    public static WoEventCustomTO getOrCreateWoEventCustomTO(final WorkorderCustomWrapperTO workorderCustomWrapperTO,
                                                             final Long woEventT, final Long idWoEventSourceT) {
        WoEventCustomTO woEventCustomTO = null;

        List<WoEventCustomTO> woEventCustomTOArray = workorderCustomWrapperTO.getWorkorderCustomTO().getWoEvents();

        if (woEventCustomTOArray == null || (woEventCustomTOArray != null && woEventCustomTOArray.isEmpty())) {
            woEventCustomTO = new WoEventCustomTO();
            final WoEventTO woEventTO = new WoEventTO();
            woEventTO.setIdCase(workorderCustomWrapperTO.getIdCase());
            woEventTO.setEventTimestamp(new Date());
            woEventTO.setIdWoEventSourceT(idWoEventSourceT);
            woEventTO.setIdWoEventT(woEventT);
            woEventCustomTO.setWoEventTO(woEventTO);
            final WoEventFvTO eventFvTO = new WoEventFvTO();
            eventFvTO.setIdCase(workorderCustomWrapperTO.getIdCase());
            eventFvTO.setFieldVisitStart(new Date());
            final WoEventFieldVisitCustomTO fieldVisit = new WoEventFieldVisitCustomTO();
            fieldVisit.setWoEventFvTO(eventFvTO);
            woEventCustomTO.setFieldVisit(fieldVisit);
            woEventCustomTOArray = new ArrayList<WoEventCustomTO>();
            woEventCustomTOArray.add(woEventCustomTO);
            workorderCustomWrapperTO.getWorkorderCustomTO().setWoEvents(woEventCustomTOArray);
            Log.d(TAG, "getOrCreateWoEventCustomTO : new woEventCustomTO added");
        } else {
            Log.d(TAG, "getOrCreateWoEventCustomTO woEventCustomTOArray is NOT NULL");

            for (WoEventCustomTO eventCustomTo : woEventCustomTOArray) {
                if (eventCustomTo != null
                        && eventCustomTo.getWoEventTO() != null
                        && eventCustomTo.getWoEventTO().getIdWoEventSourceT().longValue() == idWoEventSourceT
                        && eventCustomTo.getWoEventTO().getIdWoEventT().longValue() == woEventT) {
                    // Found existing record
                    woEventCustomTO = eventCustomTo;

                    if (woEventCustomTO.getFieldVisit() == null) {
                        final WoEventFvTO eventFvTO = new WoEventFvTO();
                        eventFvTO.setIdCase(workorderCustomWrapperTO.getIdCase());
                        eventFvTO.setFieldVisitStart(new Date());

                        // Following version is for test only
                        eventFvTO.setPdaVersion("Android Version :: " + android.os.Build.VERSION.SDK_INT);
                        final WoEventFieldVisitCustomTO fieldVisit = new WoEventFieldVisitCustomTO();
                        fieldVisit.setWoEventFvTO(eventFvTO);
                        woEventCustomTO.setFieldVisit(fieldVisit);
                    }
                    Log.d(TAG,
                            "getOrCreateWoEventCustomTO existing WoEventCustomTO found for provided event type and event source type");
                    break;
                }
            }

            if (woEventCustomTO == null) {
                Log.d(TAG,
                        "getOrCreateWoEventCustomTO existing WoEventCustomTO NOT FOUND for provided event type and event source type");
                // Build a new event
                woEventCustomTO = new WoEventCustomTO();

                final WoEventTO woEventTO = new WoEventTO();
                woEventTO.setIdCase(workorderCustomWrapperTO.getIdCase());
                woEventTO.setEventTimestamp(new Date());
                woEventTO.setIdWoEventSourceT(idWoEventSourceT);
                woEventTO.setIdWoEventT(woEventT);
                woEventCustomTO.setWoEventTO(woEventTO);

                woEventCustomTOArray.add(woEventCustomTO);
                workorderCustomWrapperTO.getWorkorderCustomTO().setWoEvents(woEventCustomTOArray);
            }
        }
        return woEventCustomTO;
    }

    /**
     * This utility method would be called when the work-order
     * completes via negative/exceptional flow requiring the user
     * to choose a reason type and optionally specifying a note
     *
     * @param note          (String)
     * @param idInfoSourceT (Source type would be PDA here)
     * @param idInfoT       (Info type)
     * @return {@link WoInfoCustomTO}
     */
    public static WoInfoCustomTO createInfo(final String note,
                                            final Long idInfoSourceT, final Long idInfoT) {
        return createInfo(note, SessionState.getInstance().getCurrentUser().getUserName(), idInfoSourceT, idInfoT);
    }

    /**
     * Returns all values of the given data type found in a workorder.
     *
     * @param wo                  Workorder to search.
     * @param idWoAdditionalDataT data type to match
     * @return List of values
     */
    public static List<String> getAdditionalDataValues(WorkorderCustomWrapperTO wo, Long idWoAdditionalDataT) {
        List<String> values = new ArrayList<String>();
        List<WoAdditionalDataCustomTO> ad = wo.getWorkorderCustomTO().getAdditionalDataList();
        if (ad != null) {
            for (WoAdditionalDataCustomTO woAdditionalDataCustomTO : ad) {
                if (woAdditionalDataCustomTO.getAdditionalData().getIdWoAdditionalDataT().equals(idWoAdditionalDataT)) {
                    for (WoAdditionalDataValueTO woAdditionalDataValueTO : woAdditionalDataCustomTO.getAdditionalDataValue()) {
                        String value = TypeDataUtil.getOrgOrDiv(woAdditionalDataValueTO.getAdditionalDataValueO(),
                                woAdditionalDataValueTO.getAdditionalDataValueD(),
                                woAdditionalDataValueTO.getAdditionalDataValueV());
                        values.add(value);
                    }
                }
            }
        }
        return values;
    }


    public static WoAdditionalDataCustomTO createAdditionalData(Long idCase, String additionalDataValue, Long CONSTANT_ADDITIONAL_DATA_T) {
        WoAdditionalDataTO woAdditionalDataTO = new WoAdditionalDataTO();
        WoAdditionalDataValueTO woAdditionalDataValueTO = new WoAdditionalDataValueTO();

        woAdditionalDataTO.setIdCase(idCase);
        woAdditionalDataTO.setIdWoAdditionalDataT(CONSTANT_ADDITIONAL_DATA_T);
        woAdditionalDataValueTO.setIdCase(idCase);
        woAdditionalDataValueTO.setAdditionalDataValueD(additionalDataValue);
        woAdditionalDataValueTO.setAdditionalDataValueV(new Long(1));

        List<WoAdditionalDataValueTO> woAdditionalDataValueTOList = new ArrayList<WoAdditionalDataValueTO>();
        woAdditionalDataValueTOList.add(woAdditionalDataValueTO);

        WoAdditionalDataCustomTO woAdditionalDataCustomTO = new WoAdditionalDataCustomTO();
        woAdditionalDataCustomTO.setAdditionalData(woAdditionalDataTO);
        woAdditionalDataCustomTO.setAdditionalDataValue(woAdditionalDataValueTOList);

        return woAdditionalDataCustomTO;
    }

    /**
     * Get Domains for work order
     *
     * @param wo
     * @return
     */
    public static List<Long> getDomainsForWO(WorkorderCustomWrapperTO wo) {
        return getDomainsForDataDomain(wo.getIdDomain());
    }

    private static List<Long> getDomainsForDataDomain(Long dataDomain) {
        if (dataDomain == null) return null;
        List<Long> v = new ArrayList<Long>();
        DomainCustomTO domainCustomTO = ObjectCache.getIdObject(DomainCustomTO.class, dataDomain);
        if (domainCustomTO != null) {
            for (DomainRelationTO domainRelationTO : domainCustomTO.getDomainRelationTOs()) {
                if (dataDomain.equals(domainRelationTO.getIdDomain())) {
                    v.add(domainRelationTO.getVisibleIdDomain());
                }
            }
        }
        return v;
    }

    /**
     * ADD/REMOVE Result Reason Ids from Field Visit event object
     *
     * @param selectedReason
     * @param unselectedReasonIds
     * @param wo
     */
    public static void addRemoveResultReasons(long selectedReason, Set<Long> unselectedReasonIds, WorkorderCustomWrapperTO wo) {

        //TO BE REMOVED
        if (Utils.isNotEmpty(unselectedReasonIds)) {
            unselectedReasonIds.remove(selectedReason);
            WoEventCustomTO woEventCustomTO = WorkorderUtils.getOrCreateWoEventCustomTO(wo, CONSTANTS().WO_EVENT_T__FIELD_VISIT, CONSTANTS().WO_EVENT_SOURCE_T__SESP_PDA);
            WoEventResultReasonTO toBeRemoved = null;
            if (woEventCustomTO.getResultReasons() != null) {
                for (Long unselectedReasonId : unselectedReasonIds) {
                    for (WoEventResultReasonTO woEventResultReasonTO : woEventCustomTO.getResultReasons()) {
                        if (unselectedReasonId.equals(woEventResultReasonTO.getIdWoEventResultReasonT())) {
                            toBeRemoved = woEventResultReasonTO;
                        }
                    }
                    if (toBeRemoved != null) {
                        Log.d(TAG, "REMOVING REASON ID :: " + unselectedReasonId);
                        woEventCustomTO.getResultReasons().remove(toBeRemoved);
                    }
                }
            }
        }

        //TO BE ADDED
        if (selectedReason != 1) {
            WoEventResultReasonTTO woEventResultReasonTTOs = ObjectCache.getIdObject(WoEventResultReasonTTO.class, selectedReason);
            if (Utils.isNotEmpty(woEventResultReasonTTOs)) {
                Set<WoEventResultReasonTTO> woEventResultReasonTTOSet = new HashSet<WoEventResultReasonTTO>();
                woEventResultReasonTTOSet.add(ObjectCache.getType(WoEventResultReasonTTO.class, selectedReason));
                Log.d(TAG, "ADDING REASON ID :: " + selectedReason);
                WorkorderUtils.addEventResultReasons(wo, woEventResultReasonTTOSet, true);
            }
        }

    }

    /**
     * ADD/REMOVE Result Reason Ids from Field Visit event object
     *
     * @param selectedReasons
     * @param reasonIds
     * @param wo
     */
    public static void addRemoveResultReasons(List<Long> selectedReasons, Set<Long> reasonIds, WorkorderCustomWrapperTO wo) {

        //TO BE REMOVED
        if (Utils.isNotEmpty(reasonIds)) {
            if (Utils.isNotEmpty(selectedReasons)) {
                reasonIds.removeAll(selectedReasons);
            }
            WoEventCustomTO woEventCustomTO = WorkorderUtils.getOrCreateWoEventCustomTO(wo, CONSTANTS().WO_EVENT_T__FIELD_VISIT, CONSTANTS().WO_EVENT_SOURCE_T__SESP_PDA);
            WoEventResultReasonTO toBeRemoved = null;
            if (woEventCustomTO.getResultReasons() != null) {
                for (Long reasonId : reasonIds) {
                    for (WoEventResultReasonTO woEventResultReasonTO : woEventCustomTO.getResultReasons()) {
                        if (reasonId.equals(woEventResultReasonTO.getIdWoEventResultReasonT())) {
                            toBeRemoved = woEventResultReasonTO;
                        }
                    }
                    if (toBeRemoved != null) {
                        Log.d(TAG, "REMOVING REASON ID :: " + reasonId);
                        woEventCustomTO.getResultReasons().remove(toBeRemoved);
                    }
                }
            }
        }

        //TO BE ADDED
        if (Utils.isNotEmpty(selectedReasons)) {
            for (Long idSelectedReason : selectedReasons) {
                WoEventResultReasonTTO woEventResultReasonTTOs = ObjectCache.getIdObject(WoEventResultReasonTTO.class, idSelectedReason);
                if (Utils.isNotEmpty(woEventResultReasonTTOs)) {
                    Set<WoEventResultReasonTTO> woEventResultReasonTTOSet = new HashSet<WoEventResultReasonTTO>();
                    woEventResultReasonTTOSet.add(ObjectCache.getType(WoEventResultReasonTTO.class, idSelectedReason));
                    Log.d(TAG, "ADDING REASON ID :: " + idSelectedReason);
                    WorkorderUtils.addEventResultReasons(wo, woEventResultReasonTTOSet, true);
                }
            }
        }


    }


    /**
     * Check if the current field visit already has woEventResultReasonT
     *
     * @param workorderTO
     * @param woEventResultReasonT
     * @return
     */
    public static boolean hasEventResultReason(WorkorderCustomTO workorderTO, Long woEventResultReasonT) {
        WoEventCustomTO woEventCustomTO = getResultReasonsForFieldVisitEvent(workorderTO);
        if (woEventCustomTO != null && woEventCustomTO.getResultReasons() != null) {
            for (WoEventResultReasonTO resultReasonTO : woEventCustomTO.getResultReasons()) {
                if (resultReasonTO.getIdWoEventResultReasonT().equals(woEventResultReasonT)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Gets the current unsaved (id is null) field visit woEventCustomMTO
     *
     * @param workorderTO
     * @return Current unsaved (id is null) field visit woEventCustomMTO or null if it does not exist
     */
    public static WoEventCustomTO getResultReasonsForFieldVisitEvent(WorkorderCustomTO workorderTO) {
        List<WoEventCustomTO> woEventCustomMTOs = workorderTO.getWoEvents();

        for (WoEventCustomTO woEventCustomTO : woEventCustomMTOs) {
            if ((woEventCustomTO.getWoEventTO().getIdWoEventT().equals(CONSTANTS().WO_EVENT_T__FIELD_VISIT)
                    && woEventCustomTO.getWoEventTO().getIdWoEventSourceT().equals(CONSTANTS().WO_EVENT_SOURCE_T__SESP_PDA))
                    && (woEventCustomTO.getWoEventTO().getId() == null)) {
                return woEventCustomTO;
            }
        }

        return null;
    }

    /**
     * Fetch Customer Time Reservation Info
     *
     * @param workorder
     * @param idWoContactT
     * @return
     */
    public static WoCustTimeReservCustomTO getWoCustTimeReservCustomTO(WorkorderCustomWrapperTO workorder, Long idWoContactT) {
        WoCustTimeReservCustomTO custTimeReservCustomTO = null;
        try {
            List<WoCustTimeReservCustomTO> custTimeReservCustomTOs = workorder.getWorkorderCustomTO().getWoCustTimeReservCustomTOs();

            for (WoCustTimeReservCustomTO woCustTimeReservCustomTO : custTimeReservCustomTOs) {
                if (woCustTimeReservCustomTO.getInfoMTO().getIdInfoT().equals(idWoContactT)) {
                    custTimeReservCustomTO = woCustTimeReservCustomTO;
                    break;
                }
            }
        } catch (Exception e) {
            custTimeReservCustomTO = null;
        }
        return custTimeReservCustomTO;
    }

    /**
     * Check if collection product exists
     *
     * @param wo
     * @return
     */
    public static boolean isCollectionProductExist(WorkorderCustomWrapperTO wo) {
        WoInstMepCollectionTO woInstMepCollectionTO = null;
        if (wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getCollection() != null && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getCollection().size() > 0) {
            woInstMepCollectionTO = wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getCollection().get(0);
        }
        return (woInstMepCollectionTO != null && woInstMepCollectionTO.getIdProductT() != null);
    }

    /**
     * Check if Meter Register Exists
     *
     * @param wo
     * @return
     */
    public static boolean isMeterRegisterExists(WorkorderCustomWrapperTO wo) {
        List<WoUnitCustomTO> woUnitTOList = wo.getWorkorderCustomTO().getWoUnits();
        WoUnitCustomTO woUnitCustomTO = null;
        if (Utils.isNotEmpty(woUnitTOList)) {
            for (WoUnitCustomTO unitCustomTO : woUnitTOList) {
                Long status;
                try {
                    status = TypeDataUtil.getValidOrgDivValue(unitCustomTO, WoUnitTO.ID_WO_UNIT_STATUS_T_D);
                    if (unitCustomTO.getIdWoUnitT().equals(CONSTANTS().WO_UNIT_T__METER) &&
                            ((status.equals(CONSTANTS().WO_UNIT_STATUS_T__EXISTING)) || status.equals(CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED))) {
                        woUnitCustomTO = unitCustomTO;
                    }

                    if (woUnitCustomTO == null) {
                        if (unitCustomTO.getIdWoUnitT().equals(CONSTANTS().WO_UNIT_T__METER_EXTERNAL) &&
                                ((status.equals(CONSTANTS().WO_UNIT_STATUS_T__EXISTING)) || status.equals(CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED))) {
                            woUnitCustomTO = unitCustomTO;
                        }
                    }

                } catch (Exception e) {
                    writeLog(TAG + " : isMeterRegisterExists()", e);
                }
            }
        }

        return (woUnitCustomTO != null
                && woUnitCustomTO.getWoUnitMeter() != null
                && woUnitCustomTO.getWoUnitMeter().getMeterRegisters() != null);
    }

    public static boolean isMeterInstalled(WorkorderCustomWrapperTO workorder) {
        return (UnitInstallationUtils.getUnit(workorder, CONSTANTS().WO_UNIT_T__METER, CONSTANTS().WO_UNIT_STATUS_T__MOUNTED) != null);
    }


    /**
     * Get Result Reason type
     *
     * @param idCaseTHandlerT
     * @param pdaCaseTHTAttRTCTOList
     * @param attachmentReasonTTOList
     * @return
     */
    public static List<AttachmentReasonTTO> getAttachmentReasonType(Long idCaseTHandlerT,
                                                                    List<PdaCaseTHTAttRTCTO> pdaCaseTHTAttRTCTOList, List<AttachmentReasonTTO> attachmentReasonTTOList) {
        List<AttachmentReasonTTO> attachmentReasonTTOs = new ArrayList<AttachmentReasonTTO>();
        for (PdaCaseTHTAttRTCTO pdaCaseTHTAttRTCTO : pdaCaseTHTAttRTCTOList) {
            if ((pdaCaseTHTAttRTCTO.getIdCaseTHandlerT().longValue() == idCaseTHandlerT.longValue())) {
                for (AttachmentReasonTTO attachmentReasonTTO : attachmentReasonTTOList) {
                    if (pdaCaseTHTAttRTCTO.getIdAttachmentReasonT().longValue() == attachmentReasonTTO.getId().longValue()) {
                        attachmentReasonTTOs.add(attachmentReasonTTO);
                    }
                }
            }
        }
        Collections.sort(attachmentReasonTTOs, new TypeDataComparator());
        return attachmentReasonTTOs;
    }


    public static Long findIdCaseTypeHandlerType(Long idCaseType) {
        CaseTCustomTO caseTTO = ObjectCache.getType(CaseTCustomTO.class, idCaseType);
        return caseTTO.getIdCaseTHandlerT();
    }


    public static void saveWoInstMepElTO(
            WorkorderCustomWrapperTO workorderTO,
            Long idCurrentTransformerPrimary,
            Long idCurrentTransformerSecondary,
            String plintNumberD,
            String idPlintControl,
            Long idFuseSocketControl,
            Long meterConstant) throws Exception {
        WoInstCustomTO inst = workorderTO.getWorkorderCustomTO().getWoInst();
        if (inst == null) {
            inst = new WoInstCustomTO();
            workorderTO.getWorkorderCustomTO().setWoInst(inst);
        }

        WoInstMeasurepointCustomTO woInstMep = inst.getInstMeasurepoint();
        if (woInstMep == null) {
            woInstMep = new WoInstMeasurepointCustomTO();
            workorderTO.getWorkorderCustomTO().getWoInst().setInstMeasurepoint(woInstMep);
        }

        WoInstMepElCustomTO woInstMepElCustom = woInstMep.getWoInstMepElCustomTO();
        if (woInstMepElCustom == null) {
            woInstMepElCustom = new WoInstMepElCustomTO();
            workorderTO.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().setWoInstMepElCustomTO(woInstMepElCustom);
            woInstMepElCustom.setIdCase(workorderTO.getIdCase());
            woInstMepElCustom.setIdWoInstMep(workorderTO.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getWoInstMepTO().getId());
        }
        if (idCurrentTransformerPrimary != null) {
            TypeDataUtil.mapValueIntoDOV(idCurrentTransformerPrimary, woInstMepElCustom, WoInstMepElCustomTO.ID_CURRENT_TRANSFORMER_P_T_D);
        }
        if (idCurrentTransformerSecondary != null) {
            TypeDataUtil.mapValueIntoDOV(idCurrentTransformerSecondary, woInstMepElCustom, WoInstMepElCustomTO.ID_CURRENT_TRANSFORMER_S_T_D);
        }
        if (Utils.isNotEmpty(plintNumberD)) {
            TypeDataUtil.mapValueIntoDOV(plintNumberD, woInstMepElCustom, WoInstMepElCustomTO.PLINT_NUMBER_D);
        }
        if (Utils.isNotEmpty(idFuseSocketControl)) {
            woInstMepElCustom.setIdFuseSocketCtrlResultT(idFuseSocketControl);
        }
        if (Utils.isNotEmpty(idPlintControl)) {
            woInstMepElCustom.setIdPlintControlResultT(Long.valueOf(idPlintControl));
        }
        if (meterConstant != null) {
            TypeDataUtil.mapValueIntoDOV(meterConstant, woInstMepElCustom, WoInstMepElCustomTO.REGISTER_CONSTANT_D);
        }
    }


    public static void saveWoInstElTO(WorkorderCustomWrapperTO workorderTO,
                                      Long idPhasetTypeD,
                                      Long idInstElNsT,
                                      Long feedingLines) throws Exception {
        WoInstElCustomTO instElCustomTO = workorderTO.getWorkorderCustomTO().getWoInst().getInstElectrical();
        if (instElCustomTO == null) {
            instElCustomTO = new WoInstElCustomTO();
            workorderTO.getWorkorderCustomTO().getWoInst().setInstElectrical(instElCustomTO);
        }

        WoInstElTO woInstElTo = workorderTO.getWorkorderCustomTO().getWoInst().getInstElectrical().getWoInstElTO();
        if (woInstElTo == null) {
            woInstElTo = new WoInstElTO();
            woInstElTo.setIdCase(workorderTO.getIdCase());
            workorderTO.getWorkorderCustomTO().getWoInst().getInstElectrical().setWoInstElTO(woInstElTo);
        }
        if (idPhasetTypeD != null) {
            TypeDataUtil.mapValueIntoDOV(idPhasetTypeD, woInstElTo, WoInstElTO.ID_PHASE_T_D);
        }
        if (idInstElNsT != null) {
            TypeDataUtil.mapValueIntoDOV(idInstElNsT, woInstElTo, WoInstElTO.ID_INST_EL_NS_T_D);
        }

        if (Utils.isNotEmpty(feedingLines)) {
            TypeDataUtil.mapValueIntoDOV(feedingLines, woInstElTo, WoInstElTO.FEEDING_LINES_D);
        }
    }


    public static void saveWoInstTO(
            WorkorderCustomWrapperTO workorderTO,
            String xCoordinate,
            String yCoordinate,
            Long idSystemCoordSystemT,
            String keyInfo,
            String keyNumber,
            Long accessibleToTechnician) throws Exception {
        WoInstTO woInstTO = workorderTO.getWorkorderCustomTO().getWoInst().getWoInstTO();
        if (woInstTO == null) {
            woInstTO = new WoInstTO();
            workorderTO.getWorkorderCustomTO().getWoInst().setWoInstTO(woInstTO);
            woInstTO.setIdCase(workorderTO.getIdCase());
        }

        if (woInstTO != null) {

            if (Utils.isNotEmpty(xCoordinate)) {
                TypeDataUtil.mapValueIntoDOV(xCoordinate, woInstTO, WoInstTO.X_COORDINATE_D);
            }
            if (Utils.isNotEmpty(yCoordinate)) {
                TypeDataUtil.mapValueIntoDOV(yCoordinate, woInstTO, WoInstTO.Y_COORDINATE_D);
            }
            if (idSystemCoordSystemT != null) {
                TypeDataUtil.mapValueIntoDOV(idSystemCoordSystemT, woInstTO, WoInstTO.ID_SYSTEM_COORD_SYSTEM_T_D);
            }

            if (Utils.isNotEmpty(keyInfo)) {
                TypeDataUtil.mapValueIntoDOV(keyInfo, woInstTO, WoInstTO.KEY_INFO_D);
            }
            if (Utils.isNotEmpty(keyNumber)) {
                TypeDataUtil.mapValueIntoDOV(keyNumber, woInstTO, WoInstTO.KEY_NUMBER_D);
            }
            if (null != accessibleToTechnician) {
                woInstTO.setAccessibleToTechnicianD(accessibleToTechnician);
                woInstTO.setAccessibleToTechnicianV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
            } else {
                Long workorderAccessibleToTechnician = TypeDataUtil.getOrgOrDiv(woInstTO.getAccessibleToTechnicianO(), woInstTO.getAccessibleToTechnicianD(), woInstTO.getAccessibleToTechnicianV());
                if (null != workorderAccessibleToTechnician) {
                    TypeDataUtil.mapValueIntoDOV(workorderAccessibleToTechnician, woInstTO, WoInstTO.ACCESSIBLE_TO_TECHNICIAN_D);
                }
            }
        }
    }


    public static void saveInstElectricalMainFuse(
            WorkorderCustomWrapperTO workorderTO,
            String fuseSize,
            Long idFusePhysicalSizeTD,
            Long idFuseT) throws Exception {

        WoInstElCustomTO instElCustomTO = workorderTO.getWorkorderCustomTO().getWoInst().getInstElectrical();
        if (instElCustomTO == null) {
            instElCustomTO = new WoInstElCustomTO();
            workorderTO.getWorkorderCustomTO().getWoInst().setInstElectrical(instElCustomTO);
        }

        WoFuseTO woFuseTO = instElCustomTO.getMainFuse();
        if (woFuseTO == null) {
            woFuseTO = new WoFuseTO();
            workorderTO.getWorkorderCustomTO().getWoInst().getInstElectrical().setMainFuse(woFuseTO);
            woFuseTO.setIdCase(workorderTO.getIdCase());
        }
        if (Utils.isNotEmpty(fuseSize)) {
            TypeDataUtil.mapValueIntoDOV(fuseSize, woFuseTO, WoFuseTO.FUSE_SIZE_D);
        }
        if (idFusePhysicalSizeTD != null) {
            TypeDataUtil.mapValueIntoDOV(idFusePhysicalSizeTD, woFuseTO, WoFuseTO.ID_FUSE_PHYSICAL_SIZE_T_D);
        }
        if (idFuseT != null) {
            TypeDataUtil.mapValueIntoDOV(idFuseT, woFuseTO, WoFuseTO.ID_FUSE_T_D);
        }
    }


    public static void saveWoInstMepTO(
            WorkorderCustomWrapperTO workorderTO,
            Long idInstMepMeterPlmtTD,
            Long idWoInstMepPowStatusTD,
            Double signalStrength,
            Long externalControlExists,
            Long externalControlConnected,
            Long accessibleToEndCustomer,
            Long antennaDirection) throws Exception {
        WoInstCustomTO woInstCustomTO = workorderTO.getWorkorderCustomTO().getWoInst();
        if (woInstCustomTO == null) {
            woInstCustomTO = new WoInstCustomTO();
            workorderTO.getWorkorderCustomTO().setWoInst(woInstCustomTO);
        }

        WoInstMeasurepointCustomTO woInstMeasurepointCustomTO = woInstCustomTO.getInstMeasurepoint();
        if (woInstMeasurepointCustomTO == null) {
            woInstMeasurepointCustomTO = new WoInstMeasurepointCustomTO();
            workorderTO.getWorkorderCustomTO().getWoInst().setInstMeasurepoint(woInstMeasurepointCustomTO);
        }

        WoInstMepTO woInstMepTO = woInstMeasurepointCustomTO.getWoInstMepTO();
        if (woInstMepTO == null) {
            woInstMepTO = new WoInstMepTO();
            woInstMepTO.setIdCase(workorderTO.getIdCase());
            workorderTO.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().setWoInstMepTO(woInstMepTO);
        }

        if (idInstMepMeterPlmtTD != null) {
            TypeDataUtil.mapValueIntoDOV(idInstMepMeterPlmtTD, woInstMepTO, WoInstMepTO.ID_INST_MEP_METER_PLMT_T_D);
        }
        if (idWoInstMepPowStatusTD != null) {
            TypeDataUtil.mapValueIntoDOV(idWoInstMepPowStatusTD, woInstMepTO, WoInstMepTO.ID_WO_INST_MEP_POW_STATUS_T_D);
        }
        if (signalStrength != null) {
            TypeDataUtil.mapValueIntoDOV(signalStrength, woInstMepTO, WoInstMepTO.SIGNAL_STRENGTH_D);
        }
        if (externalControlExists != null) {
            TypeDataUtil.mapValueIntoDOV(externalControlExists, woInstMepTO, WoInstMepTO.EXTERNAL_CONTROL_EXISTS_D);

            if (externalControlConnected != null) {
                TypeDataUtil.mapValueIntoDOV(externalControlConnected, woInstMepTO, WoInstMepTO.EXTERNAL_CONTROL_CONNECTED_D);
            }
        }
        if (accessibleToEndCustomer != null) {
            woInstMepTO.setAccessibleToEndCustomer(accessibleToEndCustomer);
        }
        if (antennaDirection != null) {
            TypeDataUtil.mapValueIntoDOV(antennaDirection, woInstMepTO, WoInstMepTO.ANTENNA_DIRECTION_D);
        }

    }

    public static Map<String, String> loadRegisterMap(MeterCommunicationCheckResultTO registerEntries) {
        String registers = registerEntries.getResult();
        Map<String, String> registerMap = new ArrayMap<String, String>();
        try{
        String[] arr = StringUtil.split(registers, "|");
        for (String entry : arr) {
            String[] register = entry.split(":");
            if (register.length > 1) {
                registerMap.put(register[0], register[1]);
            }
        }
        } catch (Exception e) {
            writeLog(TAG + " :loadRegisterMap() ", e);
        }
        return registerMap;
    }

    public static Map<String, String> loadTariffMap(MeterCommunicationCheckResultTO tariffentries) {
        Map<String, String> tariffMap = new ArrayMap<String, String>();
        try {
            String tariffs = tariffentries.getResult();
            String[] arr = StringUtil.split(tariffs, "|");
            for (String entry : arr) {
                String[] tariff = entry.split(":");
                if (tariff.length > 1) {
                    tariffMap.put(tariff[0], tariff[1]);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :loadTariffMap() ", e);
        }
        return tariffMap;
    }

    /**
     * RETURN Meter Communication Check Enabled yes/no
     *
     * @return
     */
    public static String isMeterCommunicationCheckEnabled() {
        String meterCommCheckEnabled = null;
        try {
            List<SystemParameterTO> systemParameterTOs = ObjectCache.getAllTypes(SystemParameterTO.class);
            for (SystemParameterTO systemParameterTO : systemParameterTOs) {
                if (systemParameterTO.getId().equals(CONSTANTS().SYSTEM_PARAMETER__METER_COMMUNICATION_CHECK_ENABLED)) {
                    meterCommCheckEnabled = systemParameterTO.getValue();
                    break;
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " :isMeterCommunicationCheckEnabled() ", e);
        }
        return meterCommCheckEnabled;
    }
}
