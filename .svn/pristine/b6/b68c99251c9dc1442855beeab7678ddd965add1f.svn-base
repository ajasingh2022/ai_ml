package com.capgemini.sesp.ast.android.ui.activity.material_logistics.device_info;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.GuiDate;
import com.skvader.rsp.ast_sep.common.to.ast.table.StockTO;
import com.skvader.rsp.ast_sep.common.to.custom.UnitInformationTO;
import com.skvader.rsp.cft.common.util.DateTimeFormat;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class DeviceInfoShowActivity extends PreferenceActivity {

    public static final String IDENTIFIER = "identifier";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            addPreferencesFromResource(R.xml.device_info_view);

            UnitInformationTO unitInformationTO = (UnitInformationTO) getIntent().getExtras().get("UnitInformationTO");

            if (unitInformationTO != null) {
                String tmp;
                getPreferenceScreen().findPreference("propertyNumber").setSummary(unitInformationTO.getPropertyNumber());
                getPreferenceScreen().findPreference("giai").setSummary(unitInformationTO.getGiai());
                getPreferenceScreen().findPreference("serialNumber").setSummary(unitInformationTO.getSerialNumber());

                tmp = (unitInformationTO.getUnitModelTO() != null) ? unitInformationTO.getUnitModelTO().getName() : "";
                getPreferenceScreen().findPreference("model").setSummary(tmp);

                tmp = (unitInformationTO.getUnitStatusTTO() != null) ? unitInformationTO.getUnitStatusTTO().getName() : "";
                getPreferenceScreen().findPreference("status").setSummary(tmp);

                getPreferenceScreen().findPreference("phoneNumber").setSummary(unitInformationTO.getPhoneNumber());
                getPreferenceScreen().findPreference("ipAddress").setSummary(unitInformationTO.getIpAddress());

                tmp = (unitInformationTO.getUnitModelManufacturerTTO() != null) ? unitInformationTO.getUnitModelManufacturerTTO().getName() : "";
                getPreferenceScreen().findPreference("manufacturer").setSummary(tmp);

                tmp = (unitInformationTO.getProductionDate() != null && unitInformationTO.getProductionDate().getDateDay() != null) ? DateTimeFormat.CLIENT_DATE.fromDate(unitInformationTO.getProductionDate().getLocalDate()) : "";
                getPreferenceScreen().findPreference("productionDate").setSummary(tmp);

                tmp = (unitInformationTO.getConfigurationType() != null) ? unitInformationTO.getIncomingUnitCommTTO().getName() + "/" + unitInformationTO.getOutgoingUnitCommTTO().getName() : "";
                getPreferenceScreen().findPreference("communicationType").setSummary(tmp);

                tmp = (unitInformationTO.getUnitOwnerTTO() != null) ? unitInformationTO.getUnitOwnerTTO().getName() : "";
                getPreferenceScreen().findPreference("owner").setSummary(tmp);

                tmp = (unitInformationTO.getConfigurationType() != null) ? unitInformationTO.getConfigurationType().getName() : "";
                getPreferenceScreen().findPreference("configuration").setSummary(tmp);

                tmp = (unitInformationTO.getUnitStatusReasonTTO() != null && unitInformationTO.getUnitStatusReasonTTO().size() > 0) ?
                        unitInformationTO.getUnitStatusReasonTTO().get(0).getName() : "";
                getPreferenceScreen().findPreference("reasonForStatus").setSummary(tmp);

                Long idStock = unitInformationTO.getStockCode();
                if (idStock != null) {
                    tmp = ObjectCache.getIdObjectName(StockTO.class, idStock);
                }
                getPreferenceScreen().findPreference("latestStock").setSummary(tmp);

                tmp = (unitInformationTO.getStockEnd() != null) ? GuiDate.formatDate(unitInformationTO.getStockEnd()) : "";
                getPreferenceScreen().findPreference("sent").setSummary(tmp);

                tmp = (unitInformationTO.getStockStart() != null) ? GuiDate.formatDate(unitInformationTO.getStockStart()) : "";
                getPreferenceScreen().findPreference("arrived").setSummary(tmp);
            }
        } catch (Exception e) {
            writeLog("DeviceInfoShowActivity : onCreate", e);
        }
    }
}
