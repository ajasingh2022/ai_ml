package com.capgemini.sesp.ast.android.ui.activity.material_list;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.cache.WorkorderCache;
import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitTTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCfgTCustomTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 7/27/2018.
 */

@SuppressLint("ValidFragment")
public class UnitFragment extends Fragment {

    private int selectedSegment;

    public static final int TECHNICIAN = 1;
    public static final int TEAM = 2;
    public static final int TOTAL = 3;

    private ListView unitListView;

    private TextView unitHeader1;
    private TextView unitHeader2;
    private TextView unitHeader3;

    private boolean isUnitTypeDesc;
    private boolean isUnitModelDesc;
    private boolean isUnitCountDesc;

    private List<WorkorderLiteTO> materialList;
    static String TAG = UnitFragment.class.getSimpleName();
    private List<MateriaTypeCategory> listMaterialUnit;

    static MaterialUnitAdapter materialUnitAdapter;

    public UnitFragment() {

    }

    public UnitFragment(int selectedSegment) {
        this.selectedSegment = selectedSegment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(R.layout.activity_material_list_unit_tab, container, false);
            materialList = WorkorderCache.getWorkordersLite(true);
            listMaterialUnit = getMaterialDeviceList(selectedSegment);
            unitHeader1 = view.findViewById(R.id.unitHeader1);
            unitHeader2 = view.findViewById(R.id.unitHeader2);
            unitHeader3 = view.findViewById(R.id.unitHeader3);
            unitHeader1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Collections.sort(listMaterialUnit, new MateriaTypeCategory.SortMaterialList(isUnitTypeDesc, ConstantsAstSep.MaterialListKeys.UNIT_TYPE));
                    isUnitTypeDesc = !isUnitTypeDesc;
                    materialUnitAdapter.notifyDataSetChanged();
                }
            });
            unitHeader2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Collections.sort(listMaterialUnit, new MateriaTypeCategory.SortMaterialList(isUnitModelDesc, ConstantsAstSep.MaterialListKeys.UNIT_MODEL));
                    isUnitModelDesc = !isUnitModelDesc;
                    materialUnitAdapter.notifyDataSetChanged();
                }
            });
            unitHeader3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Collections.sort(listMaterialUnit, new MateriaTypeCategory.SortMaterialList(isUnitCountDesc, ConstantsAstSep.MaterialListKeys.UNIT_COUNT));
                    isUnitCountDesc = !isUnitCountDesc;
                    materialUnitAdapter.notifyDataSetChanged();
                }
            });
            unitListView = view.findViewById(R.id.unit_list);
            materialUnitAdapter = new MaterialUnitAdapter(getContext(), listMaterialUnit);
            unitListView.setAdapter(materialUnitAdapter);
        } catch (Exception e) {
            writeLog(TAG + "  : onCreateView() ", e);
        }
        return view;
    }

    public List<MateriaTypeCategory> getMaterialDeviceList(int selectedSegment) {
        try {
            final Map<String, MateriaTypeCategory> categories = new LinkedHashMap<String, MateriaTypeCategory>();
            // count unit models
            final Map<String, Integer> unitModelCounts = new LinkedHashMap<String, Integer>();
            for (final WorkorderLiteTO wo : materialList) {
                if ((selectedSegment == TECHNICIAN && !wo.isAssigned()) || (selectedSegment == TEAM && wo.isAssigned()) || selectedSegment == TOTAL) {

                    if (wo.getIdUnitModelCfgT() != null) {
                        final UnitModelCfgTCustomTO config = ObjectCache.getIdObject(UnitModelCfgTCustomTO.class, wo.getIdUnitModelCfgT());


                        if (wo.getAntIdUnitModel() != null) {
                            UnitModelCustomTO unitModelCustomTO = ObjectCache.getIdObject(UnitModelCustomTO.class, wo.getAntIdUnitModel());
                            addOne(unitModelCounts, unitModelCustomTO.getCode());
                        }

                        boolean allowCombinedUnits = false;
                        for (final Long idUnitModel : Utils.safeToList(config.getIdUnitModels())) {
                            UnitModelCustomTO unitModelCustomTO = ObjectCache.getType(UnitModelCustomTO.class, idUnitModel);
                            if (unitModelCustomTO != null && CONSTANTS().SYSTEM_BOOLEAN_T__TRUE.equals(unitModelCustomTO.getAllowCombinedUnits())) {
                                allowCombinedUnits = true;
                            }
                        }

                        if (allowCombinedUnits) {
                            addOne(unitModelCounts, config.getCode());
                        } else {
                            for (final Long idUnitModel : Utils.safeToList(config.getIdUnitModels())) {
                                UnitModelCustomTO unitModelCustomTO = ObjectCache.getType(UnitModelCustomTO.class, idUnitModel);
                                if (Utils.isNotEmpty(unitModelCustomTO)) {
                                    addOne(unitModelCounts, unitModelCustomTO.getCode());
                                }
                            }
                        }

                    }
                }
            }

            // create categories for the unit model counts
            for (Map.Entry<String, Integer> entry : unitModelCounts.entrySet()) {
                MateriaTypeCategory category = categories.get(entry.getKey().toString());

                if (category == null) {
                    UnitModelCustomTO unitModel = ObjectCache.getIdObject(UnitModelCustomTO.class, entry.getKey());
                    if (unitModel != null) {
                        category = new MateriaTypeCategory();
                        category.setDetail1(ObjectCache.getTypeName(UnitTTO.class, unitModel.getIdUnitT()));
                        category.setDetail2(unitModel.getName());
                        category.setCount(entry.getValue());

                        categories.put(entry.getKey().toString(), category);
                    } else {
                        Log.d("MaterialListActivity", " No unit model found for idUnitModel = " + entry.getKey());
                    }
                }
            }

            listMaterialUnit = new ArrayList<MateriaTypeCategory>(categories.values());

            isUnitModelDesc = false;
            Collections.sort(listMaterialUnit, new MateriaTypeCategory.SortMaterialList(isUnitModelDesc, ConstantsAstSep.MaterialListKeys.UNIT_MODEL));
            isUnitModelDesc = !isUnitModelDesc;
        } catch (Exception e) {
            writeLog(TAG + "  : getMaterialDeviceList() ", e);
        }
        return listMaterialUnit;
    }

    private static void addOne(Map<String, Integer> unitModelCounts, String idUnitModel) {
        try {
            Integer count = unitModelCounts.get(idUnitModel);
            if (count == null) {
                count = 0;
            }
            count++;
            unitModelCounts.put(idUnitModel, count);
        } catch (Exception e) {
            writeLog(TAG + "  : addOne() ", e);
        }
    }

}