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
import com.capgemini.sesp.ast.android.module.cache.WorkorderCache;
import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 7/27/2018.
 */
@SuppressLint("ValidFragment")
public class KeyFragment extends Fragment {

    private int selectedSegment;

    public static final int TECHNICIAN = 1;
    public static final int TEAM = 2;
    public static final int TOTAL = 3;

    private ListView keyListView;

    private TextView keyHeader1;
    private TextView keyHeader2;

    private boolean isKeyNumberDesc;
    private boolean isKeyInfoDesc;

    private List<WorkorderLiteTO> materialList;

    private List<MateriaTypeCategory> listMaterialKey;

    private MaterialKeyAdapter materialKeyAdapter;

    public KeyFragment()
    {

    }
    public KeyFragment(int selectedSegment)
    {
        this.selectedSegment=selectedSegment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.activity_material_list_key_tab,container,false);
        try{
        materialList = WorkorderCache.getWorkordersLite(true);
        listMaterialKey=getMaterialKeyList(selectedSegment);
        keyHeader1= view.findViewById(R.id.keyHeader1);
        keyHeader2= view.findViewById(R.id.keyHeader2);
        keyHeader1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(listMaterialKey, new MateriaTypeCategory.SortMaterialList(isKeyNumberDesc, ConstantsAstSep.MaterialListKeys.KEY_NUMBER));
                isKeyNumberDesc = !isKeyNumberDesc;
                materialKeyAdapter.notifyDataSetChanged();
            }
        });
        keyHeader2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(listMaterialKey, new MateriaTypeCategory.SortMaterialList(isKeyInfoDesc, ConstantsAstSep.MaterialListKeys.KEY_INFO));
                isKeyInfoDesc = !isKeyInfoDesc;
                materialKeyAdapter.notifyDataSetChanged();
            }
        });
        keyListView= view.findViewById(R.id.keys_list);
        materialKeyAdapter = new MaterialKeyAdapter(getContext(), listMaterialKey);
        keyListView.setAdapter(materialKeyAdapter);
        } catch (Exception e) {
            writeLog( "KeyFragment  : onCreateView() ", e);
        }
        return view;
    }

    public List<MateriaTypeCategory> getMaterialKeyList(int selectedSegment) {

        Map<String, MateriaTypeCategory> categories = new LinkedHashMap<String, MateriaTypeCategory>();
        try{
        for (WorkorderLiteTO wo : materialList) {
            Log.d("", "===key type output=== " + wo.getKeyInfo() + ":::" + wo.getKeyNumber());
            if ((selectedSegment == TECHNICIAN && !wo.isAssigned()) || (selectedSegment == TEAM && wo.isAssigned()) || selectedSegment == TOTAL) {
                MateriaTypeCategory category = categories.get(wo.getKeyNumber() + wo.getKeyInfo().toUpperCase(Locale.getDefault()));

                if (category == null) {
                    category = new MateriaTypeCategory();
                    category.setDetail1(wo.getKeyNumber());
                    category.setDetail2(wo.getKeyInfo());
                    category.setWorkorders(new ArrayList<WorkorderLiteTO>());
                    categories.put(wo.getKeyNumber() + wo.getKeyInfo().toUpperCase(Locale.getDefault()), category);
                }

                category.getWorkorders().add(wo);
            }
        }

        listMaterialKey = new ArrayList<MateriaTypeCategory>(categories.values());
        isKeyNumberDesc = false;
        Collections.sort(listMaterialKey, new MateriaTypeCategory.SortMaterialList(isKeyNumberDesc, ConstantsAstSep.MaterialListKeys.KEY_NUMBER));

        isKeyNumberDesc = !isKeyNumberDesc;
        } catch (Exception e) {
            writeLog("KeyFragment  :forceOfflineCheckBoxClicked()", e);
        }
        return listMaterialKey;
    }
}