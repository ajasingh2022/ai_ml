package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.NetworkStatusCallbackListener;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.UnitInstallationUtils;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar.adapter.UnitEntryAdapter;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar.adapter.UnitEntryItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;

public class ReplaceUnitFragment extends CustomFragment implements NetworkStatusCallbackListener {

    Long unitType;
    ArrayList<UnitEntryItem> unitEntryItems;
    UnitEntryAdapter unitEntryAdapter = null;
    RecyclerView entriesListView;
    LinearLayout titleLayout;
    AddItemDialog.CommunicateFragment communicateFragment;

    ArrayMap<String, WoUnitCustomTO> existingIdentifierUnitMap;
    ArrayMap<String, WoUnitCustomTO> existingIdentifierUnitMapUpdated;

    String UNIT_ENTRIES ="UNIT_ENTRIES";

    public ReplaceUnitFragment(String stepId) {
        super(stepId);
    }

    public ReplaceUnitFragment(String stepId, Long unitType){
        super(stepId);
        this.unitType = unitType;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_replace_unit, null);
        initializePageValues();
        initilizeUI();
        return fragmentView;
    }

    @Override
    public void applyChangesToModifiableWO() {

        WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();

        for (UnitEntryItem unitEntryItem: unitEntryItems){
            WoUnitCustomTO woUnitCustomTOOld =
                    existingIdentifierUnitMap.get(unitEntryItem.getExistingUnit());
            if (woUnitCustomTOOld!=null) {
                woUnitCustomTOOld.setIdWoUnitStatusTD(CONSTANTS().WO_UNIT_STATUS_T__DISMANTLED);
                woUnitCustomTOOld.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                woUnitCustomTOOld.setUnitDismantledTimestamp(new Date());
                WoUnitCustomTO woUnitCustomTO = new WoUnitCustomTO();
                woUnitCustomTO.setIdCase(wo.getIdCase());
                woUnitCustomTO.setGiai(unitEntryItem.getNewUnit());
                woUnitCustomTO.setIdWoUnitStatusTD(CONSTANTS().WO_UNIT_STATUS_T__MOUNTED);
                woUnitCustomTO.setIdWoUnitStatusTV(CONSTANTS().SYSTEM_BOOLEAN_T__TRUE);
                woUnitCustomTO.setIdWoUnitT(this.unitType);
                woUnitCustomTO.setIdUnitIdentifierT(CONSTANTS().UNIT_IDENTIFIER_T__GIAI);
                woUnitCustomTO.setUnitMountedTimestamp(new Date());
                woUnitCustomTO.setUnitModel(woUnitCustomTOOld.getUnitModel());
                wo.getWorkorderCustomTO().getWoUnits().add(woUnitCustomTO);
            }
        }

    }

    @Override
    public void initializePageValues() {
        unitEntryItems = new ArrayList<UnitEntryItem>();
        Gson gson = new Gson();

        existingIdentifierUnitMap = new ArrayMap<>();
        WorkorderCustomWrapperTO workorderCustomWrapperTO =
                AbstractWokOrderActivity.getWorkorderCustomWrapperTO();

        List<WoUnitCustomTO> woUnitCustomTOS =
                UnitInstallationUtils.getUnits(workorderCustomWrapperTO,unitType,
                CONSTANTS().WO_UNIT_STATUS_T__EXISTING);

        for (WoUnitCustomTO woUnitCustomTO:woUnitCustomTOS){
            existingIdentifierUnitMap.put(woUnitCustomTO.getGiai(),woUnitCustomTO);
        }
        existingIdentifierUnitMapUpdated = new ArrayMap<>(existingIdentifierUnitMap);

        if (stepfragmentValueArray.get(UNIT_ENTRIES)!= null){
        unitEntryItems =     gson.fromJson((String) stepfragmentValueArray.get(UNIT_ENTRIES),
                    new TypeToken<ArrayList<UnitEntryItem>>(){}.getType());
        }

        for (UnitEntryItem unitEntryItem:unitEntryItems) {
            existingIdentifierUnitMapUpdated.remove(unitEntryItem.getExistingUnit());
        }

    }

    private void initilizeUI() {
        Button buttonAdd = fragmentView.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUIToAddItem(null);
            }
        });

        communicateFragment = new AddItemDialog.CommunicateFragment() {
            @Override
            public void updateUI() {
                if (unitEntryAdapter.getItemCount() >0){
                    titleLayout.setVisibility(View.VISIBLE);

                }
                else {
                    titleLayout.setVisibility(View.GONE);
                }
            }
        };

        titleLayout = fragmentView.findViewById(R.id.rvTitleLayout);
        titleLayout.setVisibility(View.GONE);

        if (unitEntryItems.size()>0){
            titleLayout.setVisibility(View.VISIBLE);
        }

        entriesListView = fragmentView.findViewById(R.id.entries);
        unitEntryAdapter = new UnitEntryAdapter(this.getContext(),unitEntryItems);
        entriesListView.setAdapter(unitEntryAdapter);
        entriesListView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        unitEntryAdapter.unitCreation = new UnitEntryAdapter.UnitCreation() {
            @Override
            public WoUnitCustomTO createUnitFromUnitEntryItem(UnitEntryItem unitEntryItem) {
                WoUnitCustomTO woUnitCustomTO = new WoUnitCustomTO();
                woUnitCustomTO.setGiai(unitEntryItem.getNewUnit());
                woUnitCustomTO.setUnitModel(unitEntryItem.getModel());
                woUnitCustomTO.setIdWoUnitT(ReplaceUnitFragment.this.unitType);
                woUnitCustomTO.setIdUnitIdentifierT(CONSTANTS().UNIT_IDENTIFIER_T__GIAI);
                return woUnitCustomTO;
            }

            @Override
            public void updateUnit(UnitEntryItem unitEntryItem) {
                showUIToAddItem(unitEntryItem);
            }
        };
        this.networkStatusChanged(AndroidUtilsAstSep.isNetworkAvailable());
    }

    private void showUIToAddItem(UnitEntryItem unitEntryItem) {

        if (unitEntryItem != null){
            existingIdentifierUnitMapUpdated.put(unitEntryItem.getExistingUnit(),
                    existingIdentifierUnitMap.get(unitEntryItem.getExistingUnit()));
            unitEntryItems.remove(unitEntryItem);
        }
        existingIdentifierUnitMapUpdated.clear();
        existingIdentifierUnitMapUpdated = new ArrayMap<>(existingIdentifierUnitMap);

        for (UnitEntryItem unitEntryItem1: unitEntryItems){
            existingIdentifierUnitMapUpdated.remove(unitEntryItem1.getExistingUnit());
        }

        AddItemDialog additemDialog = new AddItemDialog(this.getActivity(),unitEntryAdapter,
                unitEntryItems,existingIdentifierUnitMapUpdated,unitEntryItem);
        additemDialog.communicateFragment = communicateFragment;
        additemDialog.show();
        Window window = additemDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public boolean validateUserInput() {

        if (AndroidUtilsAstSep.isNetworkAvailable()) {
            for (UnitEntryItem unitEntryItem : unitEntryItems) {

                if (!(unitEntryItem.getVerificationStatus().equals(UnitEntryItem.VerificationStatus.SUCCESS))) {
                    return false;
                }
            }
        }
            applyChangesToModifiableWO();

            Gson gson = new Gson();
            stepfragmentValueArray.put(UNIT_ENTRIES,gson.toJson(unitEntryItems,
                    new TypeToken<ArrayList<UnitEntryItem>>(){}.getType()));
            return true;
    }

    @Override
    public void networkStatusChanged(boolean isConnected) {

        if (isConnected){
        fragmentView.findViewById(R.id.networkStatus).setVisibility(View.GONE);
        }
        else {
            fragmentView.findViewById(R.id.networkStatus).setVisibility(View.VISIBLE);
        }

    }
}
