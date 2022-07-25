package com.capgemini.sesp.ast.android.module.util;

import android.util.ArrayMap;
import android.util.Log;

import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.skvader.rsp.ast_sep.common.to.ast.table.ProductTCTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitIdentifierTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitModelIdentifierTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitModelManufacturerTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventTechPlanTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstMepCollectionTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitMeterRegTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoEventCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitMeterCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitMeterRegisterCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.ProductTCustomTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCfgTCustomTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.FlowPageConstants;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by samdasgu on 5/23/2016.
 */
public class UnitInstallationUtils {

    private static final String TAG = "UnitInstallationUtils";


    /**
     * Required for TS DM to find out modified unit
     *
     * @param woUnits
     * @param idWoUnitT
     * @param idWoUnitStatusT
     * @return
     */
    public static WoUnitCustomTO getModifiedUnit(List<WoUnitCustomTO> woUnits, Long idWoUnitT, Long idWoUnitStatusT) {

        WoUnitCustomTO result = null;
        try {
            for (WoUnitCustomTO woUnit : woUnits) {
                if (woUnit.getIdWoUnitT() != null && woUnit.getIdWoUnitStatusTD() != null) {
                    if (woUnit.getIdWoUnitT().equals(idWoUnitT)
                            && woUnit.getIdWoUnitStatusTD().equals(idWoUnitStatusT)) {
                        result = woUnit;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":onPostExecute() ", e);
            result = null;
        }
        return result;
    }

    /**
     * Get list of registers for a meter
     *
     * @param wo           WorkorderCustomWrapperTO
     * @param idUnitStatus status of the unit (Existing/Dismantled etc..)
     * @return
     */
    public static List<WoUnitMeterRegisterCustomTO> getUnitMeterRegisters(final WorkorderCustomWrapperTO wo, final long idUnitStatus) {
        List<WoUnitMeterRegisterCustomTO> unitMeterRegisters = null;
        try {
            if (wo != null
                    && wo.getWorkorderCustomTO() != null
                    && wo.getWorkorderCustomTO().getWoUnits() != null) {
                for (WoUnitCustomTO woUnitCustomTo : wo.getWorkorderCustomTO().getWoUnits()) {
                    if (woUnitCustomTo.getWoUnitMeter() != null
                            && woUnitCustomTo.getWoUnitMeter().getMeterRegisters() != null) {
                        Long unitStatustype = TypeDataUtil.getOrgOrDiv(woUnitCustomTo.getIdWoUnitStatusTO(), woUnitCustomTo.getIdWoUnitStatusTD(),
                                woUnitCustomTo.getIdWoUnitStatusTV());
                        if ((unitStatustype != null) && (unitStatustype.longValue() == idUnitStatus)) {
                            unitMeterRegisters = woUnitCustomTo.getWoUnitMeter().getMeterRegisters();
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":getUnitMeterRegisters() ", e);
        }
        return unitMeterRegisters;
    }

    /**
     * Get meter registers from product
     *
     * @param wo
     * @return List<WoUnitMeterRegisterCustomTO>
     */
    public static List<WoUnitMeterRegisterCustomTO> getUnitMeterRegistersFromProduct(final WorkorderCustomWrapperTO wo) {
        List<WoUnitMeterRegisterCustomTO> unitMeterRegisters = null;
        try {
            Long idProductT = null;
            WoUnitMeterRegisterCustomTO woUnitMeterRegisterCustomTO = null;
            WoUnitMeterRegTO woUnitMeterRegMTO = null;
            if (wo != null
                    && wo.getWorkorderCustomTO() != null
                    && wo.getWorkorderCustomTO().getWoInst() != null
                    && wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint() != null) {
                List<WoInstMepCollectionTO> instMepColTos = wo.getWorkorderCustomTO().getWoInst().getInstMeasurepoint().getCollection();
                if ((instMepColTos != null) && (instMepColTos.size() > 0)) {
                    WoInstMepCollectionTO woInstMepCollectionTO = instMepColTos.get(0);
                    idProductT = woInstMepCollectionTO.getIdProductT();
                }
                ProductTCustomTO productTCustomTO = ObjectCache.getIdObject(ProductTCustomTO.class, idProductT);
                if ((productTCustomTO != null && Utils.isNotEmpty(productTCustomTO.getProductTCTOs()))) {
                    for (ProductTCTO productTCTO : productTCustomTO.getProductTCTOs()) {
                        if (productTCTO.getIdProductT().equals(idProductT)) {
                            woUnitMeterRegisterCustomTO = new WoUnitMeterRegisterCustomTO();
                            woUnitMeterRegMTO = new WoUnitMeterRegTO();
                            woUnitMeterRegMTO.setIdCase(wo.getIdCase());
                            woUnitMeterRegMTO.setIdRegisterT(productTCTO.getIdRegisterT());
                            woUnitMeterRegMTO.setIdTariffT(productTCTO.getIdTariffT());
                            woUnitMeterRegisterCustomTO.setWoUnitMeterReg(woUnitMeterRegMTO);
                            if (unitMeterRegisters == null) {
                                unitMeterRegisters = new ArrayList<WoUnitMeterRegisterCustomTO>();
                            }
                            unitMeterRegisters.add(woUnitMeterRegisterCustomTO);
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":getUnitMeterRegisters() ", e);
        }
        return unitMeterRegisters;
    }

    /**
     * Get the unique identifier value of a unit(GIAI, Serial Number or Property
     * Number)
     *
     * @param woUnitCustomTO
     * @return
     */
    public static String getUnitIdentifierValue(final WoUnitCustomTO woUnitCustomTO) {
        String result = null;
        try {
            if (woUnitCustomTO.getIdUnitIdentifierT().equals(CONSTANTS().UNIT_IDENTIFIER_T__GIAI)) {
                result = woUnitCustomTO.getGiai();
            } else if (woUnitCustomTO.getIdUnitIdentifierT().equals(CONSTANTS().UNIT_IDENTIFIER_T__SERIALNO)) {
                result = woUnitCustomTO.getSerialNumber();
            } else if (woUnitCustomTO.getIdUnitIdentifierT().equals(CONSTANTS().UNIT_IDENTIFIER_T__PROPERTYNO)) {
                result = TypeDataUtil.getOrgOrDiv(woUnitCustomTO.getPropertyNumberO(), woUnitCustomTO.getPropertyNumberD(), woUnitCustomTO.getPropertyNumberV());
            }
        } catch (Exception e) {
            writeLog(TAG + " :getUnitIdentifierValue()", e);
            result = "";
        }
        return result;
    }

    /**
     * Returns a unit with a uniquenumber = identifier.
     *
     * @param units      to search in
     * @param identifier to match
     * @return
     */
    public static WoUnitCustomTO getUnit(final List<WoUnitCustomTO> units, final String identifier) {

        try {
            for (WoUnitCustomTO unit : units) {
                String number = getUnitIdentifierValue(unit);
                if (identifier != null && identifier.equals(number)) {
                    return unit;
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":getUnit() ", e);
        }
        return null;
    }

    /**
     * Returns the unit list from the work order with a unit status(EXISTING, MOUNTED, DISMANTLED)
     *
     * @param wo              Workorder to search.
     * @param idWoUnitStatusT Matches original value
     * @return array of units
     */
    public static List<WoUnitCustomTO> getUnits(final WorkorderCustomWrapperTO wo, final Long idWoUnitStatusT) {
        List<WoUnitCustomTO> units = new ArrayList<WoUnitCustomTO>();
        try {
            if (wo.getWorkorderCustomTO().getWoUnits() != null
                    && !wo.getWorkorderCustomTO().getWoUnits().isEmpty()) {
                for (WoUnitCustomTO unit : wo.getWorkorderCustomTO().getWoUnits()) {
                    if (idWoUnitStatusT == null || idWoUnitStatusT.equals(TypeDataUtil.getOrgOrDiv(unit.getIdWoUnitStatusTO(),
                            unit.getIdWoUnitStatusTD(),
                            unit.getIdWoUnitStatusTV()))) {
                        units.add(unit);
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":getUnits() ", e);
        }
        return units;
    }

    /**
     * Returns the unit list from the work order for a unit type and unit status
     *
     * @param wo              Workorder to search.
     * @param idWoUnitT       Unit Type, (Meter etc)
     * @param idWoUnitStatusT Matches original value(EXISTING, MOUNTED, DISMANTLED)
     * @return array of units
     */
    public static List<WoUnitCustomTO> getUnits(final WorkorderCustomWrapperTO wo, final Long idWoUnitT, final Long idWoUnitStatusT) {
        List<WoUnitCustomTO> units = new ArrayList<WoUnitCustomTO>();
        try {
            if (wo.getWorkorderCustomTO().getWoUnits() != null
                    && !wo.getWorkorderCustomTO().getWoUnits().isEmpty()) {
                for (WoUnitCustomTO unit : wo.getWorkorderCustomTO().getWoUnits()) {
                    if (idWoUnitT.equals(unit.getIdWoUnitT())
                            && (idWoUnitStatusT == null || idWoUnitStatusT.equals(unit.getIdWoUnitStatusTO()))) {
                        units.add(unit);
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":getUnits() ", e);
        }
        return units;
    }

    /**
     * Returns the unit from the work order for a unit type
     *
     * @param wo              Workorder to search.
     * @param idWoUnitT       Unit Type, (Meter etc)
     * @param idWoUnitStatusT Matches original value
     * @return
     */
    public static WoUnitCustomTO getUnit(final WorkorderCustomWrapperTO wo, final Long idWoUnitT,
                                         final Long idWoUnitStatusT) {
        WoUnitCustomTO res = null;
        try {
            if (wo.getWorkorderCustomTO().getWoUnits() != null
                    && !wo.getWorkorderCustomTO().getWoUnits().isEmpty()) {
                for (WoUnitCustomTO unit : wo.getWorkorderCustomTO().getWoUnits()) {
                    if (unit.getIdWoUnitT().equals(idWoUnitT)) {
                        if (idWoUnitStatusT.equals(TypeDataUtil.getOrgOrDiv(unit.getIdWoUnitStatusTO(), unit.getIdWoUnitStatusTD(), unit.getIdWoUnitStatusTV()))) {
                            res = unit;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":getUnit() ", e);
        }
        return res;
    }

    /**
     * Get list of Unit Models for a Hardware configuration
     *
     * @param unitModelCfgId
     * @return List<UnitModelCustomTO>
     */
    public static List<UnitModelCustomTO> getUnitModelCfgTCustomTO(Long unitModelCfgId) {

        List<UnitModelCustomTO> unitModels = new ArrayList<UnitModelCustomTO>();
        try {
            List<Long> list = ObjectCache.getType(UnitModelCfgTCustomTO.class, unitModelCfgId).getIdUnitModels();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != null) {
                    unitModels.add(ObjectCache.getIdObject(UnitModelCustomTO.class, list.get(i)));
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":getUnitModelCfgTCustomTO() ", e);
        }
        return unitModels;
    }

    /**
     * Get Unit Identifier Name of a unit(GIAI, PROPERTYNO, SERIALNO)
     *
     * @param woUnitCustomTO
     * @return
     */
    public static String getUnitIdentifierName(final WoUnitCustomTO woUnitCustomTO) {
        return ObjectCache.getType(UnitIdentifierTTO.class, woUnitCustomTO.getIdUnitIdentifierT()).getCode();
    }

    /**
     * Get ICC Number for Sim Card
     *
     * @param wo
     * @param idUnitType
     * @return
     */
    public static String getICCNumberForSimCard(final WorkorderCustomWrapperTO wo, final long idUnitType) {
        String icc = null;
        try {
            if (wo != null
                    && wo.getWorkorderCustomTO() != null
                    && wo.getWorkorderCustomTO().getWoUnits() != null
                    && !wo.getWorkorderCustomTO().getWoUnits().isEmpty()) {
                for (final WoUnitCustomTO unitCustomTO : wo.getWorkorderCustomTO().getWoUnits()) {
                    if (unitCustomTO != null
                            && unitCustomTO.getIdUnitIdentifierT() != null
                            && unitCustomTO.getIdWoUnitT().longValue() == idUnitType) {
                        if (unitCustomTO.getSimcReferenceIdV() != null
                                && unitCustomTO.getSimcReferenceIdV().longValue() == CONSTANTS().SYSTEM_BOOLEAN_T__TRUE) {
                            // Take the delta value
                            icc = unitCustomTO.getSimcReferenceIdD();
                        } else {
                            // Go with original sim card icc
                            icc = unitCustomTO.getSimcReferenceIdO();
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":getICCNumberForSimCard() ", e);
        }
        return icc;
    }


    /**
     * Gets all UnitModels of specified idUnitT
     *
     * @param idUnitT
     * @return
     */
    public static List<UnitModelCustomTO> getUnitModelsOfType(Long idUnitT) {


        List<UnitModelCustomTO> filteredUnitModelCustomTOs = new ArrayList<UnitModelCustomTO>();
        try {
            List<UnitModelCustomTO> allUnitModelCustomTOs = TypeDataUtil.filterUserSettableAndSystemEnabled(ObjectCache.getAllIdObjects(UnitModelCustomTO.class));

            for (UnitModelCustomTO unitModelCustomTO : allUnitModelCustomTOs) {
                if (unitModelCustomTO.getIdUnitT().equals(idUnitT)) {
                    ////Unit Model Handling for non-ANTENNA
                    if (unitModelCustomTO.getUnitModelIdentifierTOs() != null
                            && !unitModelCustomTO.getUnitModelIdentifierTOs().isEmpty()) {
                        if (!filteredUnitModelCustomTOs.contains(unitModelCustomTO)) {
                            filteredUnitModelCustomTOs.add(unitModelCustomTO);
                        }
                    }
                    //Unit Model Handling for ANTENNA
                    else if (CONSTANTS().UNIT_T__ANTENNA.equals(idUnitT)) {
                        if (!filteredUnitModelCustomTOs.contains(unitModelCustomTO)) {
                            filteredUnitModelCustomTOs.add(unitModelCustomTO);
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":getUnitModelsOfType() ", e);
        }
        return filteredUnitModelCustomTOs;
    }

    /**
     * Get Identifier Value of a unit based on identifier type
     *
     * @param woUnitTO
     * @param idUnitIdentifierT
     * @return String
     */
    public static String getUnitIdentifierValue(WoUnitTO woUnitTO, Long idUnitIdentifierT) {
        if (idUnitIdentifierT.equals(AndroidUtilsAstSep.CONSTANTS().UNIT_IDENTIFIER_T__GIAI)) {
            return woUnitTO.getGiai();
        } else if (idUnitIdentifierT.equals(AndroidUtilsAstSep.CONSTANTS().UNIT_IDENTIFIER_T__PROPERTYNO)) {
            return TypeDataUtil.getOrgOrDiv(woUnitTO.getPropertyNumberO(), woUnitTO.getPropertyNumberD(), woUnitTO.getPropertyNumberV());
        } else if (idUnitIdentifierT.equals(AndroidUtilsAstSep.CONSTANTS().UNIT_IDENTIFIER_T__SERIALNO)) {
            return woUnitTO.getSerialNumber();
        } else {
            return null;
        }
    }

    /**
     * Filters UnitModels for Meters based on category
     *
     * @param unitModelCustomTOs
     * @param idMeaCategory
     * @return
     */
    public static List<UnitModelCustomTO> filterUnitModelsOnCategory(List<UnitModelCustomTO> unitModelCustomTOs, Long idMeaCategory) {
        if (idMeaCategory != null) {
            List<UnitModelCustomTO> filteredUnitModelCustomTOs = new ArrayList<UnitModelCustomTO>();

            for (UnitModelCustomTO unitModelCustomTO : unitModelCustomTOs) {
                if (unitModelCustomTO.getUnitModelMeterTO() != null && idMeaCategory.equals(unitModelCustomTO.getUnitModelMeterTO().getIdMeaCategoryT())) {
                    filteredUnitModelCustomTOs.add(unitModelCustomTO);
                }
            }
            return filteredUnitModelCustomTOs;
        } else {
            return unitModelCustomTOs;
        }
    }

    /**
     * Utility to retrieve
     * Antenna unit model name
     * unit model name for unit type = METER,CONCENTRATOR
     * <p/>
     * <p>
     * The returning map will contain values
     * </p>
     *
     * @param wo {@link WorkorderCustomWrapperTO}
     * @return Map<String,String>
     */

    public static Map<String, String> getExistingModelNames(final WorkorderCustomWrapperTO wo, final long unitType) {
        final Map<String, String> map = new ArrayMap<String, String>();
        try {
            if (wo != null
                    && wo.getWorkorderCustomTO() != null) {
                final List<WoEventCustomTO> eventList = wo.getWorkorderCustomTO().getWoEvents();
                if (eventList != null && !eventList.isEmpty()) {
                    for (final WoEventCustomTO eventTo : eventList) {
                        if (eventTo != null && eventTo.getTechnicalPlanning() != null) {
                            final WoEventTechPlanTO woEventTechPlanTO = eventTo.getTechnicalPlanning();
                            final Long antennaIdModel = woEventTechPlanTO.getAntIdUnitModel();
                            if (antennaIdModel != null && antennaIdModel > 0) {
                                final UnitModelCustomTO antennaUnitModel = ObjectCache.getIdObject(UnitModelCustomTO.class, antennaIdModel);
                                map.put(FlowPageConstants.ANTENNA_MODEL_NAME, antennaUnitModel.getName());
                            }
                        }
                    }
                }
                if (unitType != -1) {
                    final List<WoUnitCustomTO> unitLs = wo.getWorkorderCustomTO().getWoUnits();
                    if (unitLs != null && !unitLs.isEmpty()) {
                        for (final WoUnitCustomTO unit : unitLs) {
                            if (unit.getIdWoUnitT() != null
                                    && unit.getIdWoUnitT().longValue() == unitType) {
                                map.put(FlowPageConstants.UNIT_MODEL_NAME, unit.getUnitModel());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":getExistingModelNames() ", e);
        }
        return map;
    }

    /**
     * Filter unit model by domain
     *
     * @param data
     * @param idDomainList
     * @return List<UnitModelCustomTO>
     */
    public static List<UnitModelCustomTO> filterUnitModelByDomain(List<UnitModelCustomTO> data, List<Long> idDomainList) {
        List<UnitModelCustomTO> v = new ArrayList<UnitModelCustomTO>();
        try {
            Long[] idDomains = new Long[idDomainList.size()];
            idDomainList.toArray(idDomains);
            if (data == null) return null;

            for (UnitModelCustomTO unitModelCustomTO : data) {
                if (idDomains != null && idDomains.length > 0) {
                    Long idDomain = unitModelCustomTO.getIdDomain();
                    if (TypeDataUtil.isMatchingDomain(idDomain, idDomains)) {
                        v.add(unitModelCustomTO);
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":getExistingModelNames() ", e);
        }
        return TypeDataUtil.filterUserSettableAndSystemEnabled(v);
    }

    /**
     * Dismantle existing unit in the work order.
     *
     * @param wo
     * @param idUnitT
     */
    public static void dismantleExistingUnit(WorkorderCustomWrapperTO wo, Long idUnitT) {
        try {
            List<WoUnitCustomTO> unitCustomTos = wo.getWorkorderCustomTO().getWoUnits();
            if (unitCustomTos == null) {
                //Create new wo unit list
                wo.getWorkorderCustomTO().setWoUnits(new ArrayList<WoUnitCustomTO>());
            }
            //Dismantling any existing Meter Unit
            WoUnitCustomTO existingUnitCustomTO = UnitInstallationUtils.getUnit(wo, idUnitT, CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
            if (existingUnitCustomTO == null && CONSTANTS().WO_UNIT_T__METER.equals(idUnitT)) {
                existingUnitCustomTO = UnitInstallationUtils.getUnit(wo, CONSTANTS().WO_UNIT_T__METER_EXTERNAL, CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
            }

            if (existingUnitCustomTO != null) {
                Log.d(TAG, "DISMANTLING THE EXISTING UNIT " + existingUnitCustomTO.getGiai());
                existingUnitCustomTO.setIdWoUnitStatusTD(CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED);
                existingUnitCustomTO.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                existingUnitCustomTO.setUnitDismantledTimestamp(new Date());
            }
        } catch (Exception e) {
            writeLog(TAG + ":dismantleExistingUnit() ", e);
        }
    }

    /**
     * Install a new unit
     *
     * @param wo
     * @param idWoUnitT
     * @param selectedUnitModel
     * @param unitIdentifierValue
     */
    public static void installUnit(WorkorderCustomWrapperTO wo, Long idWoUnitT, Long selectedUnitModel, String unitIdentifierValue, boolean masterUnitSelected) {
        // Attaching new Meter Unit
        try {
            WoUnitCustomTO newUnitCustomTO = getUnit(wo, idWoUnitT, CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
            if (newUnitCustomTO == null) {
                // Create a New Unit
                // Add the new Unit in the workorder unit list
                newUnitCustomTO = new WoUnitCustomTO();
                if (idWoUnitT.equals(CONSTANTS().WO_UNIT_T__METER)) {
                    newUnitCustomTO.setWoUnitMeter(new WoUnitMeterCustomTO());
                }
                wo.getWorkorderCustomTO().getWoUnits().add(newUnitCustomTO);
            }
            UnitModelCustomTO unitModelCustomTO = null;
            if (selectedUnitModel != null) {
                unitModelCustomTO = ObjectCache.getIdObject(UnitModelCustomTO.class, selectedUnitModel);
                newUnitCustomTO.setUnitModel(unitModelCustomTO.getCode());
            }
            if (unitModelCustomTO != null) {
                List<UnitModelIdentifierTO> unitModelIdentifierTOs = unitModelCustomTO.getUnitModelIdentifierTOs();
                if ((unitModelIdentifierTOs != null) && (unitModelIdentifierTOs.size() > 0)) {
                    UnitModelIdentifierTO unitModelIdentifierTO = unitModelIdentifierTOs.get(0);
                    newUnitCustomTO.setIdUnitIdentifierT(unitModelIdentifierTO.getIdUnitIdentifierT());
                }
            } else {
                newUnitCustomTO.setIdUnitIdentifierT(CONSTANTS().UNIT_IDENTIFIER_T__GIAI);
            }

            if (newUnitCustomTO.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__GIAI.longValue()) {
                newUnitCustomTO.setGiai(unitIdentifierValue);
            } else if (newUnitCustomTO.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__PROPERTYNO.longValue()) {
                newUnitCustomTO.setPropertyNumberV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                newUnitCustomTO.setPropertyNumberD(unitIdentifierValue);
            } else if (newUnitCustomTO.getIdUnitIdentifierT().longValue() == CONSTANTS().UNIT_IDENTIFIER_T__SERIALNO.longValue()) {
                newUnitCustomTO.setSerialNumber(unitIdentifierValue);
            }
            newUnitCustomTO.setIdWoUnitStatusTD(CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
            newUnitCustomTO.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
            newUnitCustomTO.setIdCase(wo.getIdCase());
            newUnitCustomTO.setIdWoUnitT(idWoUnitT);
            newUnitCustomTO.setUnitMountedTimestamp(new Date());

            if (!masterUnitSelected) {
                // Set the master unit reference number (varchar field in db)
                newUnitCustomTO.setMasterReferenceIdD(null);
                newUnitCustomTO.setMasterReferenceIdV(CONSTANTS().SYSTEM_BOOLEAN_T__FALSE);
            }
        } catch (Exception e) {
            writeLog(TAG + ":installUnit() ", e);
        }
    }

    /**
     * Fetch the Existing Meter number
     *
     * @param wo
     * @return
     */
    public static String getExistingMeterNumber(WorkorderCustomWrapperTO wo) {
        String meterNumber = null;
        try {
            if (wo.getWorkorderCustomTO() != null
                    && wo.getWorkorderCustomTO().getWoUnits() != null) {
                for (final WoUnitCustomTO unitCustomTo : wo.getWorkorderCustomTO().getWoUnits()) {
                    if (unitCustomTo != null
                            && unitCustomTo.getWoUnitMeter() != null) {
                        meterNumber = getUnitIdentifierValue(unitCustomTo);
                        if (meterNumber != null) {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":getExistingMeterNumber() ", e);
        }
        return meterNumber;
    }

    /**
     * Fetch existing meter identifier
     *
     * @param wo
     * @return
     */
    public static String getExistingMeterIdentifier(WorkorderCustomWrapperTO wo) {
        String unitIdentifier = null;
        try {
            if (wo != null
                    && wo.getWorkorderCustomTO() != null
                    && wo.getWorkorderCustomTO().getWoUnits() != null) {
                WoUnitCustomTO unitMeterCustomTo = null;
                for (WoUnitCustomTO unitCustomTo : wo.getWorkorderCustomTO().getWoUnits()) {
                    if (unitCustomTo != null && unitCustomTo.getWoUnitMeter() != null) {
                        unitMeterCustomTo = unitCustomTo;
                        break;
                    }
                }
                if (unitMeterCustomTo != null) {
                    unitIdentifier = getUnitIdentifierName(unitMeterCustomTo);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":getExistingMeterIdentifier() ", e);
        }
        return unitIdentifier;
    }

    /**
     * Get Current WO Unit
     *
     * @param workorderTO
     * @param idWoUnitT
     * @return
     */
    public static WoUnitCustomTO getCurrentWoUnit(WorkorderCustomWrapperTO workorderTO, Long idWoUnitT) {
        if (idWoUnitT.equals(CONSTANTS().WO_UNIT_T__CM)) {
            WoUnitCustomTO woUnitCustomTO = getCurrentWoUnit(workorderTO, CONSTANTS().WO_UNIT_T__TERMINAL);
            if (woUnitCustomTO == null) {
                woUnitCustomTO = getCurrentWoUnit(workorderTO, CONSTANTS().WO_UNIT_T__MODEM);
            }
            return woUnitCustomTO;
        }

        WoUnitCustomTO woUnitCustomTO =
                getUnit(
                        workorderTO,
                        idWoUnitT,
                        CONSTANTS().WO_UNIT_STATUS_T__EXISTING
                );
        if (woUnitCustomTO == null) {
            woUnitCustomTO =
                    getUnit(
                            workorderTO,
                            idWoUnitT,
                            CONSTANTS().WO_UNIT_STATUS_T__MOUNTED
                    )
            ;
        }
        return woUnitCustomTO;
    }


    /**
     * Filter the unit manufacturer list
     *
     * @param unitModelManufacturerTTOs
     * @param idDomainList
     * @return
     */
    public static List<UnitModelManufacturerTTO> filterManufacturer(List<UnitModelManufacturerTTO> unitModelManufacturerTTOs, List<Long> idDomainList, Long idUnitT) {
        Long[] idDomains = new Long[idDomainList.size()];
        idDomainList.toArray(idDomains);
        List<UnitModelManufacturerTTO> filteredManufacturerList = new ArrayList<UnitModelManufacturerTTO>();
        try {
            List<UnitModelCustomTO> unitModels = ObjectCache.getAllIdObjects(UnitModelCustomTO.class);
            // Special treatment for 'Unknown'

            // For each manufacturer
            for (UnitModelManufacturerTTO unitModelManufacturerTTO : unitModelManufacturerTTOs) {
                if (unitModelManufacturerTTO.getCode().equals("UNKNOWN")) {
                    continue;
                }
                // loop on all unit models
                for (UnitModelCustomTO unitModelCustomTO : unitModels) {
                    // to find its unit model
                    if (unitModelManufacturerTTO.getId().equals(unitModelCustomTO.getIdUnitModelManufacturerT())) {
                        // and check if it is equal to the filter. If so do not
                        // delete it.
                        if (unitModelCustomTO.getIdUnitT().equals(idUnitT)) {
                            if (Utils.isNotEmpty(idDomains)) {
                                Long idDomain = unitModelCustomTO.getIdDomain();
                                if (!TypeDataUtil.isMatchingDomain(idDomain, idDomains)) {
                                    continue;
                                }
                            }
                            filteredManufacturerList.add(unitModelManufacturerTTO);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  :filterManufacturer() ", e);
        }
        return filteredManufacturerList;
    }

    /**
     * This method returns Mounted Custom Unit TO for voltage trafo
     *
     * @param workorderTO
     * @param idPhaseT
     * @return
     */
    public static WoUnitCustomTO getMountedVoltageTransformerWoUnit(WorkorderCustomTO workorderTO, Long idPhaseT) {

        try {
            for (WoUnitCustomTO woUnitCustomTO : workorderTO.getWoUnits()) {
                if (woUnitCustomTO.getIdWoUnitT().equals(CONSTANTS().WO_UNIT_T__VOLTAGE_TRAFO)) {
                    Long idWoUnitStatusT = TypeDataUtil.getOrgOrDiv(woUnitCustomTO.getIdWoUnitStatusTO(), woUnitCustomTO.getIdWoUnitStatusTD(), woUnitCustomTO.getIdWoUnitStatusTV());
                    if (idWoUnitStatusT.equals(CONSTANTS().WO_UNIT_STATUS_T__MOUNTED)) {
                        if (woUnitCustomTO.getWoUnitVt().getIdPhaseT().equals(idPhaseT)) {
                            return woUnitCustomTO;
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  :getMountedVoltageTransformerWoUnit() ", e);
        }
        return null;
    }

    /**
     * This method returns existing Custom Unit TO for voltage trafo
     *
     * @param workorderTO
     * @param idPhaseT
     * @return
     */
    public static WoUnitCustomTO getExistingVoltageTransformerWoUnit(WorkorderCustomTO workorderTO, Long idPhaseT) {
        try {
            for (WoUnitCustomTO woUnitCustomTO : workorderTO.getWoUnits()) {
                if (woUnitCustomTO.getIdWoUnitT().equals(CONSTANTS().WO_UNIT_T__VOLTAGE_TRAFO)) {
                    Long idWoUnitStatusT = woUnitCustomTO.getIdWoUnitStatusTO();
                    if (idWoUnitStatusT.equals(CONSTANTS().WO_UNIT_STATUS_T__EXISTING)) {
                        if (woUnitCustomTO.getWoUnitVt().getIdPhaseT().equals(idPhaseT)) {
                            return woUnitCustomTO;
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  :getExistingVoltageTransformerWoUnit() ", e);
        }
        return null;
    }

    /**
     * This method returns Mounted Custom Unit TO for current trafo
     *
     * @param workorderTO
     * @param idPhaseT
     * @return
     */

    public static WoUnitCustomTO getMountedCurrentTransformerWoUnit(WorkorderCustomTO workorderTO, Long idPhaseT) {

        try {
            for (WoUnitCustomTO woUnitCustomTO : workorderTO.getWoUnits()) {
                if (woUnitCustomTO.getIdWoUnitT().equals(CONSTANTS().WO_UNIT_T__CURRENT_TRAFO)) {
                    Long idWoUnitStatusT = TypeDataUtil.getOrgOrDiv(woUnitCustomTO.getIdWoUnitStatusTO(), woUnitCustomTO.getIdWoUnitStatusTD(), woUnitCustomTO.getIdWoUnitStatusTV());
                    if (idWoUnitStatusT.equals(CONSTANTS().WO_UNIT_STATUS_T__MOUNTED)) {
                        if (woUnitCustomTO.getWoUnitCt().getIdPhaseT().equals(idPhaseT)) {
                            return woUnitCustomTO;
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  :getMountedCurrentTransformerWoUnit() ", e);
        }
        return null;
    }

    /**
     * This method returns Existing Custom Unit TO for current trafo
     *
     * @param workorderTO
     * @param idPhaseT
     * @return
     */

    public static WoUnitCustomTO getExistingCurrentTransformerWoUnit(WorkorderCustomTO workorderTO, Long idPhaseT) {
        try {
            for (WoUnitCustomTO woUnitCustomTO : workorderTO.getWoUnits()) {
                if (woUnitCustomTO.getIdWoUnitT().equals(CONSTANTS().WO_UNIT_T__CURRENT_TRAFO)) {
                    Long idWoUnitStatusT = woUnitCustomTO.getIdWoUnitStatusTO();
                    if (idWoUnitStatusT.equals(CONSTANTS().WO_UNIT_STATUS_T__EXISTING)) {
                        if (woUnitCustomTO.getWoUnitCt().getIdPhaseT().equals(idPhaseT)) {
                            return woUnitCustomTO;
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  :getExistingCurrentTransformerWoUnit() ", e);
        }
        return null;
    }

    /**
     * Updates an existing unit or creates a new one
     *
     * @param newUnitList
     * @param unitCustomTO
     * @param unitModelCode
     * @param identifier
     * @param idWoUnitStatusT
     * @param idWoUnitT
     * @param isMounted
     * @param isDismantled
     * @param idCase
     * @return
     * @throws Exception
     */
    public static WoUnitCustomTO updateOrCreateUnit(List<WoUnitCustomTO> newUnitList, WoUnitCustomTO unitCustomTO, String unitModelCode, String identifier, Long idWoUnitStatusT, Long idWoUnitT, boolean isMounted, boolean isDismantled, Long idCase) throws Exception {
        try {
            if (unitCustomTO == null) {
                unitCustomTO = new WoUnitCustomTO();
            }

            if (unitModelCode != null) {
                unitCustomTO.setUnitModel(unitModelCode);
            }

            if (identifier != null) {
                unitCustomTO.setGiai(identifier);
                unitCustomTO.setIdUnitIdentifierT(CONSTANTS().UNIT_IDENTIFIER_T__GIAI);
            }

            if (unitCustomTO.getIdUnitIdentifierT() == null) {
                unitCustomTO.setIdUnitIdentifierT(CONSTANTS().UNIT_IDENTIFIER_T__NA);
            }

            unitCustomTO.setIdCase(idCase);
            TypeDataUtil.mapValueIntoDOV(idWoUnitStatusT, unitCustomTO, WoUnitTO.ID_WO_UNIT_STATUS_T_D);
            unitCustomTO.setIdWoUnitT(idWoUnitT);
            if (isMounted) {
                unitCustomTO.setUnitMountedTimestamp(new Date());
            } else if (isDismantled) {
                unitCustomTO.setUnitDismantledTimestamp(new Date());
            }

            newUnitList.add(unitCustomTO);
        } catch (Exception e) {
            writeLog(TAG + "  :updateOrCreateUnit() ", e);
        }
        return unitCustomTO;
    }

    /**
     * Dismantle existing SIM CARD in the work order.
     *
     * @param wo
     * @param idUnitT
     */
    public static void dismantleExistingSimCard(WorkorderCustomWrapperTO wo, Long idUnitT, String unitIdenValue) {
        try {
            List<WoUnitCustomTO> unitCustomTos = wo.getWorkorderCustomTO().getWoUnits();
            if (unitCustomTos == null) {
                //Create new wo unit list
                wo.getWorkorderCustomTO().setWoUnits(new ArrayList<WoUnitCustomTO>());
            }
            //Dismantling any existing Meter Unit
            WoUnitCustomTO existingUnitCustomTO = UnitInstallationUtils.getUnit(wo, idUnitT, CONSTANTS().WO_UNIT_STATUS_T__EXISTING);
            boolean simcardExists = true;


            if (existingUnitCustomTO != null) {
                Long giai = Long.valueOf(existingUnitCustomTO.getGiai());
                if (unitCustomTos != null) {
                    for (WoUnitCustomTO woUnitCustomTO : unitCustomTos) {
                        if (!CONSTANTS().WO_UNIT_T__SIMCARD.equals(woUnitCustomTO.getIdWoUnitT())
                                && woUnitCustomTO.getSimcIdUnitIdentifierTD() != null && giai.equals(woUnitCustomTO.getSimcIdUnitIdentifierTD())
                                && woUnitCustomTO.getSimcIdUnitIdentifierTV().longValue() == 1L
                                ) {
                            simcardExists = false;
                        }
                    }
                }
                if (simcardExists) {
                    Log.d(TAG, "DISMANTLING THE EXISTING UNIT " + existingUnitCustomTO.getGiai());
                    existingUnitCustomTO.setIdWoUnitStatusTD(CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED);
                    existingUnitCustomTO.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                    existingUnitCustomTO.setUnitDismantledTimestamp(new Date());
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":dismantleExistingSimCard() ", e);
        }
    }


    /**
     * Returns the SIM CARD from the work order for a unit type
     *
     * @param wo              Workorder to search.
     * @param idWoUnitT       Unit Type, (Meter etc)
     * @param idWoUnitStatusT Matches original value
     * @return
     */
    public static WoUnitCustomTO getSimCard(final WorkorderCustomWrapperTO wo, final Long idWoUnitT,
                                            final Long idWoUnitStatusT, String unitIdenValue) {
        WoUnitCustomTO res = null;
        try {
            if (wo.getWorkorderCustomTO().getWoUnits() != null
                    && !wo.getWorkorderCustomTO().getWoUnits().isEmpty()) {
                for (WoUnitCustomTO unit : wo.getWorkorderCustomTO().getWoUnits()) {
                    if (unitIdenValue.equals(unit.getGiai()) && null == unit.getSimcIdUnitIdentifierTD()) {
                        return null;
                    }
                    if (unit.getIdWoUnitT().equals(idWoUnitT)) {
                        if (idWoUnitStatusT.equals(TypeDataUtil.getOrgOrDiv(unit.getIdWoUnitStatusTO(), unit.getIdWoUnitStatusTD(), unit.getIdWoUnitStatusTV()))) {
                            res = unit;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog(TAG + ":getSimCard() ", e);
        }
        return res;
    }


}
