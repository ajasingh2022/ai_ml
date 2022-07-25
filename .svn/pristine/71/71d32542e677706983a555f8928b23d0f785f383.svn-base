package com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AdditionalDataUtils;
import com.capgemini.sesp.ast.android.ui.adapter.CheckListAdapter;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.bean.CheckListBean;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.CustomFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoAdditionalDataTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoAdditionalDataTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoAdditionalDataValueTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoAdditionalDataCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


@SuppressLint("ValidFragment")
public class SolarCheckListFragment extends CustomFragment implements CheckListAdapter.CheckInterface {
    private RecyclerView recyclerView;
    private List<CheckListBean> checkListModelArrayList;
    private CheckListAdapter checkListAdapter;
    private CheckBox chkboxSelectAll;
    private LinearLayoutManager layoutManager = null;
    boolean flag = false;
    private int contentView;
    Gson gson = new Gson();
    Type type = new TypeToken<ArrayList<CheckListBean>>() {
    }.getType();
    protected transient int nextPageIdentifier = -1;

    protected interface NextPageIdentifier {
        int EDITABLE_INFO = 1;
        int NOT_POSSIBLE = 2;
    }

    protected static final String CHECKLISTMODELARRAY = "CHECKLISTMODELARRAY";
    protected static final String CHECK_OR_NOT = " CHECK_OR_NOT";

    public void setContentView(int cV) {
        this.contentView = cV;
    }

    public SolarCheckListFragment(String stepId) {
        super(stepId);
    }

    public SolarCheckListFragment() {
        super();
    }

    public int getContentView() {
        return contentView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {

            fragmentView = inflater.inflate(R.layout.fragment_solar_check_list, container, false);
            initializeUI();
            populateData();
        } catch (Exception e) {
            writeLog("SolarCheckListFragment  : onCreateView() ", e);

        }
        return fragmentView;
    }

    /**
     * Populate UI components with data
     */
    private void populateData() {
        try {
            layoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
            if (Utils.isNotEmpty(stepfragmentValueArray.get(CHECKLISTMODELARRAY))) {

                checkListModelArrayList = gson.fromJson(stepfragmentValueArray.get(CHECKLISTMODELARRAY).toString(), type);
            } else {
                checkListModelArrayList = getModel(false);
            }

            checkListAdapter = new CheckListAdapter(this, checkListModelArrayList, SolarCheckListFragment.this);
            checkListAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(checkListAdapter);
            recyclerView.setLayoutManager(layoutManager);
            chkboxSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (flag && !isChecked) {

                    } else {
                        // update your model (or other business logic) based on isChecked
                        checkListModelArrayList = getModel(chkboxSelectAll.isChecked());
                        checkListAdapter = new CheckListAdapter(SolarCheckListFragment.this, checkListModelArrayList, SolarCheckListFragment.this);
                        recyclerView.setAdapter(checkListAdapter);
                    }
                    flag = false;
                }
            });
        } catch (Exception e) {
            writeLog("SolarCheckListFragment  : populateData() ", e);

        }


    }

    /**
     * Lookup and initialize all the view widgets
     */

    protected void initializeUI() {
        final View parentView = fragmentView;
        if (parentView != null) {
            recyclerView = fragmentView.findViewById(R.id.recycler);
            chkboxSelectAll = fragmentView.findViewById(R.id.selectall);
        }
    }


    public void onCheckBoxClick(boolean isAnyChecked) {
        try {
            stepfragmentValueArray.put(CHECK_OR_NOT, isAnyChecked);
            if (chkboxSelectAll.isChecked()) {
                flag = true;
            }
            if (!isAnyChecked) {
                chkboxSelectAll.setChecked(false);
            }
        } catch (Exception e) {
            writeLog("SolarCheckListFragment  : onCheckBoxClick() ", e);

        }
    }

    @Override
    public void afterCommentTextChange(String value, int position) {

        checkListModelArrayList.get(position).setCheckComment(value);


    }

    private ArrayList<CheckListBean> getModel(boolean isSelect) {
        ArrayList<CheckListBean> checkQuestionList = new ArrayList<CheckListBean>();
        try {
            List<WoAdditionalDataTTO> checkList = AdditionalDataUtils.filterControl("CHECK");
            for (WoAdditionalDataTTO woAdditionalDataTTO : checkList) {

                CheckListBean checkListModel = new CheckListBean();
                checkListModel.setCheckId(woAdditionalDataTTO.getId());
                checkListModel.setCheckTitle(woAdditionalDataTTO.getName());
                checkListModel.setSelected(isSelect);
                checkQuestionList.add(checkListModel);
            }
        } catch (Exception e) {
            writeLog("SolarCheckListFragment : getModel()", e);
        }
        return checkQuestionList;
    }


    @Override
    public boolean validateUserInput() {
        Gson gson = new Gson();
        String string = gson.toJson(checkListModelArrayList);

        stepfragmentValueArray.put(CHECKLISTMODELARRAY, string);
        boolean status = false; // For validation
        int nextPageStatus = 0; //To Define the Next Page either connect Photos page or Not accessible
        try {
            if (chkboxSelectAll.isChecked()) {
                status = true;
            } else {
                for (int i = 0; i < checkListModelArrayList.size(); i++) {
                    if (checkListModelArrayList.get(i).getSelected()) {
                        status = true;
                    } else {
                        if (Utils.isNotEmpty(checkListModelArrayList.get(i).getCheckComment())) {
                            status = true;
                        } else
                            return false;
                    }
                }
            }


            if (status) {
                applyChangesToModifiableWO();
            }
        } catch (Exception e) {
            writeLog("SolarCheckListFragment  : validateUserInput() ", e);
        }
        return status;
    }


    public void applyChangesToModifiableWO() {
        try {
            final WorkorderCustomWrapperTO workorderCustomWrapperTO = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            List<WoAdditionalDataCustomTO> woAdditionalDataCustomToList = new ArrayList<WoAdditionalDataCustomTO>();

            String checkedOrNot = null;
            List<WoAdditionalDataTO> woAdditionalDataTOList = new ArrayList<WoAdditionalDataTO>();
            if (Utils.isEmpty(checkListModelArrayList)) {
                checkListModelArrayList = gson.fromJson(stepfragmentValueArray.get(CHECKLISTMODELARRAY).toString(), type);
            }
            for (CheckListBean checkListBean : checkListModelArrayList) {

                List<WoAdditionalDataValueTO> woAdditionalDataValueTOS = new ArrayList<WoAdditionalDataValueTO>();
                WoAdditionalDataTO woAdditionalDataTO = new WoAdditionalDataTO();
                WoAdditionalDataValueTO woAdditionalDataValueTO = new WoAdditionalDataValueTO();
                WoAdditionalDataCustomTO woAdditionalDataCustomTO = new WoAdditionalDataCustomTO();
                woAdditionalDataValueTO.setIdWoAdditionalData(checkListBean.getCheckId());
                if (checkListBean.getSelected()) {
                    checkedOrNot = "YES";
                } else {
                    checkedOrNot = checkListBean.getCheckComment();
                }
                if (Utils.isNotEmpty(checkedOrNot)) {
                    woAdditionalDataValueTO.setAdditionalDataValueO(checkedOrNot);
                    //TypeDataUtil.mapValueIntoDOV(checkedOrNot, woAdditionalDataValueTO, WoAdditionalDataValueTO.ADDITIONAL_DATA_VALUE_O);
                }
                woAdditionalDataValueTO.setIdCase(workorderCustomWrapperTO.getIdCase());
                woAdditionalDataTO.setIdWoAdditionalDataT(checkListBean.getCheckId());
                woAdditionalDataTO.setIdCase(workorderCustomWrapperTO.getIdCase());
                woAdditionalDataValueTOS.add(woAdditionalDataValueTO);
                woAdditionalDataCustomTO.setAdditionalData(woAdditionalDataTO);
                woAdditionalDataCustomTO.setAdditionalDataValue(woAdditionalDataValueTOS);
                woAdditionalDataCustomToList.add(woAdditionalDataCustomTO);
            }
            workorderCustomWrapperTO.getWorkorderCustomTO().getAdditionalDataList().addAll(woAdditionalDataCustomToList);
            // workorderCustomWrapperTO.getWorkorderCustomTO().setAdditionalDataList(woAdditionalDataCustomToList);

        } catch (Exception e) {
            writeLog("SolarCheckListFragment  : applyChangesToModifiableWO() ", e);

        }
    }


}
