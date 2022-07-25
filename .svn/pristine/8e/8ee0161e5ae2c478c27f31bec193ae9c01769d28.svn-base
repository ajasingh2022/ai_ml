package com.capgemini.sesp.ast.android.ui.activity.order;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.ui.activity.common.AbstractOrderDetailsActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.UiGroupTTO;
import com.skvader.rsp.ast_sep.common.to.custom.OrderInfoCustomDataTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This activity shows  Order Details/Info
 * for the particular details
 * <p>
 * Created by rkumari2 on 29-11-2019.
 */
public class OrderInfoActivity extends AbstractOrderDetailsActivity {

    RecyclerView rvList;
    OrderInfoExpandableAdapter orderListExpandableAdapter;
    WorkorderCustomWrapperTO workorderCustomWrapperTO; //= getWorkorder();
    Resources resources = ApplicationAstSep.context.getResources();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.implClass = this.getClass();

    }

    @Override
    protected void setUpView() {
        rvList = findViewById(R.id.list);
        workorderCustomWrapperTO = getWorkorder();
        orderListExpandableAdapter = new OrderInfoExpandableAdapter(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(orderListExpandableAdapter);
        orderListExpandableAdapter.setData(getList());
        orderListExpandableAdapter.notifyDataSetChanged();

    }

    public ArrayList<UiGroupTTO> getUiGroupTTOList(List<UiGroupTTO> uiGroupTTOList) {
        ArrayList<OrderInfoCustomDataTO> woOrderCustomData = (ArrayList)workorderCustomWrapperTO.getOrderInfoCustomDataTO();
        ArrayList<UiGroupTTO> uiGroupTTOS = new ArrayList<>();

            for (OrderInfoCustomDataTO orderInfoCustomDataTO : woOrderCustomData) {
                for (UiGroupTTO uiGroupTTO : uiGroupTTOList) {
                if (orderInfoCustomDataTO.getId().equals(uiGroupTTO.getId())) {
                    uiGroupTTOS.add(uiGroupTTO);

                    break;
                }
            }
        }
        return uiGroupTTOS;
    }

    public ArrayList getList() {
        List<UiGroupTTO> uiGroupTTO = ObjectCache.getAllTypes(UiGroupTTO.class);
        ArrayList<UiGroupTTO> pairs = getUiGroupTTOList(uiGroupTTO);
        Map<String,OrderInfoCustomDataTO> stringOrderInfoCustomDataTOMap=new HashMap<>();
        for(OrderInfoCustomDataTO orderInfoCustomDataTO:workorderCustomWrapperTO.getOrderInfoCustomDataTO()){
            stringOrderInfoCustomDataTOMap.put(orderInfoCustomDataTO.getId().toString(),orderInfoCustomDataTO);
        }

        ArrayList<OrderInfoModel> returnValue = new ArrayList<>();
        ArrayMap parentIdChildren = new ArrayMap<String,List>();

        for(UiGroupTTO groupTTO:pairs) {

            if(groupTTO.getParentMappingId() == null){
                OrderInfoModel orderInfoModelRoot = new OrderInfoModel(groupTTO.getCode(),null);
                orderInfoModelRoot.id = groupTTO.getId();
                returnValue.add(orderInfoModelRoot);
            }
            else {
                OrderInfoModel orderInfoModelChild = null;
                if(groupTTO.getParentMappingId()!=null && groupTTO.getChildPresent()!=0){
                    orderInfoModelChild= new OrderInfoModel(groupTTO.getCode(),stringOrderInfoCustomDataTOMap.get(groupTTO.getId().toString()).getUiGroupList());
                }
                else {
                    orderInfoModelChild= new OrderInfoModel(groupTTO.getCode(),stringOrderInfoCustomDataTOMap.get(groupTTO.getId().toString()).getUiGroupList());
                }
                orderInfoModelChild.id = groupTTO.getId();

                if(parentIdChildren.containsKey(groupTTO.getParentMappingId().toString())){
                    ((List)(parentIdChildren.get(groupTTO.getParentMappingId().toString()))).add(orderInfoModelChild);
                }
                else {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(orderInfoModelChild);
                    parentIdChildren.put(groupTTO.getParentMappingId().toString(),arrayList);
                }
            }

        }

        for (OrderInfoModel orderInfoModel : returnValue){

            orderInfoModel.orderModels = setChildren(orderInfoModel.id,parentIdChildren);
        }

        return returnValue;
    }

    private ArrayList<OrderInfoModel> setChildren(Long parentId, ArrayMap parentIdChildren) {

        ArrayList<OrderInfoModel> orderInfoModels = (ArrayList<OrderInfoModel>) parentIdChildren.get(parentId.toString());

        for (OrderInfoModel orderInfoModel:orderInfoModels){
            if (parentIdChildren.containsKey(orderInfoModel.id.toString())){
                orderInfoModel.orderModels = setChildren(orderInfoModel.id,parentIdChildren);
            }
            else {
                continue;
            }
        }
        return orderInfoModels;
    }


}
