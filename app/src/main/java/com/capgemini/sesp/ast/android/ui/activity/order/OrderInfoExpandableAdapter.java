package com.capgemini.sesp.ast.android.ui.activity.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.skvader.rsp.ast_sep.common.to.ast.table.UiGroupTTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import java.util.ArrayList;

/**
 * This class is a adapter for Order Detail page
 * for the particular details
 * <p>
 * Created by rkumari2 on 29-11-2019.
 */
public class OrderInfoExpandableAdapter extends RecyclerView.Adapter<OrderInfoExpandableAdapter.ViewHolder> {
    private Context context;
    protected WorkorderCustomWrapperTO workOrder;
    private ArrayList<OrderInfoModel> modelsAll = new ArrayList<>();

    public OrderInfoExpandableAdapter(Context context) {
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.order_details_item, parent
                , false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final OrderInfoModel orderInfoModel = modelsAll.get(position);

        Long id = orderInfoModel.id;
        DatabaseHandler databaseHandler = DatabaseHandler.createDatabaseHandler();
        UiGroupTTO groupTTO = ObjectCache.getIdObject(UiGroupTTO.class, id);
        String value;
        if (groupTTO.getTokenCode() != null) {
            value = databaseHandler.translateDesignation(groupTTO.getTokenCode());
        } else {
            value = groupTTO.getName();
        }

        holder.textView.setText(value);

        if (orderInfoModel.orderModels != null && orderInfoModel.orderModels.size() > 0) {
            holder.innerView.setVisibility(View.VISIBLE);
            holder.imgArrow.setVisibility(View.VISIBLE);
            switch (orderInfoModel.state) {

                case CLOSED:
                    holder.imgArrow.setImageResource(R.drawable.ic_play_circle_fill_24dp);
                    holder.innerView.setVisibility(View.GONE);
                    break;
                case OPENED:
                    holder.imgArrow.setImageResource(R.drawable.ic_arrow_down_circle_order_info_24dp);
                    holder.innerView.setVisibility(View.VISIBLE);
                    OrderInfoExpandableAdapter orderInfoExpandableAdapter = new OrderInfoExpandableAdapter(context);
                    holder.innerView.setLayoutManager(new LinearLayoutManager(context));
                    holder.innerView.setAdapter(orderInfoExpandableAdapter);
                    orderInfoExpandableAdapter.setData(orderInfoModel.orderModels);
                    orderInfoExpandableAdapter.notifyDataSetChanged();
                    holder.innerView.setVisibility(View.VISIBLE);
                    break;
            }
        }
        else {
            holder.imgArrow.setImageResource(R.drawable.selector);
            holder.imgArrow.setVisibility(View.VISIBLE);
            holder.innerView.setVisibility(View.GONE);
        }
        if (orderInfoModel.mValues != null && orderInfoModel.mValues.size() > 0) {
            holder.values.setVisibility(View.VISIBLE);
            holder.values.setLayoutManager(new LinearLayoutManager(context));
            FieldAdapter fieldAdapter = new FieldAdapter(context, orderInfoModel);
            holder.values.setAdapter(fieldAdapter);
            fieldAdapter.notifyDataSetChanged();
        } else {
            holder.values.setVisibility(View.GONE);
        }

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandCollapseChild(holder, position, orderInfoModel);

            }
        });
        holder.imgArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandCollapseChild(holder, position, orderInfoModel);
            }
        });

    }

    private void expandCollapseChild(ViewHolder holder, int position, OrderInfoModel rootorderInfoModel) {
        if (rootorderInfoModel.orderModels.isEmpty()) {
            return;
        }
        switch (rootorderInfoModel.state) {

            case CLOSED:
                rootorderInfoModel.state = OrderInfoModel.STATE.OPENED;
                break;

            case OPENED:
                rootorderInfoModel.state = OrderInfoModel.STATE.CLOSED;
                break;
        }
        this.notifyItemChanged(position);

    }

    @Override
    public int getItemCount() {
        return modelsAll.size();
    }

    public void setData(ArrayList<OrderInfoModel> list) {
        modelsAll = list;


    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        RecyclerView values;
        ImageView imgArrow;
        RecyclerView innerView;

        public ViewHolder(View itemView) {

            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tvTitle);
            imgArrow = (ImageView) itemView.findViewById(R.id.imgArrow);
            values = itemView.findViewById(R.id.values);
            innerView = itemView.findViewById(R.id.innerRV);
        }
    }

}
