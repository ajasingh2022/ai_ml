package com.capgemini.sesp.ast.android.ui.activity.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.ui.adapter.ExpandableRecyclerAdapter;
import com.capgemini.sesp.ast.android.ui.adapter.ParentListItem;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by geomathe on 3/13/2018.
 */

public class OrderSummaryListAdapter extends ExpandableRecyclerAdapter<OrderSummaryViewHolder, OrderSummaryItemsViewHolder> {

    private LayoutInflater mInflator;

    public OrderSummaryListAdapter(Context context, List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflator = LayoutInflater.from(context);
    }

    @Override
    public OrderSummaryViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View orderCategoryView = mInflator.inflate(R.layout.inst_code_list_row, parentViewGroup, false);
        return new OrderSummaryViewHolder(orderCategoryView);
    }

    @Override
    public OrderSummaryItemsViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View ordersView = mInflator.inflate(R.layout.inst_code_list_items, childViewGroup, false);
        return new OrderSummaryItemsViewHolder(ordersView);
    }

    @Override
    public void onBindParentViewHolder(OrderSummaryViewHolder orderCategoryViewHolder, int position, ParentListItem parentListItem) {
        try {
            OrderSummary orderCategory = (OrderSummary) parentListItem;
            orderCategoryViewHolder.bind(orderCategory);
        } catch (Exception e) {
            writeLog("OrderSummaryListAdapter  : onBindChildViewHolder() ", e);
        }
    }

    @Override
    public void onBindChildViewHolder(OrderSummaryItemsViewHolder ordersViewHolder, int position, Object childListItem) {
        try {
            OrderSummaryItems orders = (OrderSummaryItems) childListItem;
            ordersViewHolder.bind(orders);
        } catch (Exception e) {
            writeLog("OrderSummaryListAdapter  : onBindChildViewHolder() ", e);
        }
    }


}

