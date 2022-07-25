package com.capgemini.sesp.ast.android.ui.activity.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.CaseTCustomTO;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.CASE_TYPE_NULL;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

class OrderSummaryAdapter extends BaseAdapter {
    private final Context context;
    private final List<OrderSummaryCategory> categories;
    private final LayoutInflater mInflater;

    public OrderSummaryAdapter(Context context, List<OrderSummaryCategory> categories) {
        this.context = context;
        this.categories = categories;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View vw = convertView;
        try {
            ViewHolder holder;
            if (vw == null) {
                vw = mInflater.inflate(R.layout.activity_order_summary_row, null);
                holder = new ViewHolder();
                holder.txtName = vw.findViewById(R.id.name);
                holder.txtCount = vw.findViewById(R.id.count);
                holder.txtTimeReservationCount = vw.findViewById(R.id.time_reservation_count);

                vw.setTag(R.id.ordersummary, holder);

            } else {
                holder = (ViewHolder) vw.getTag(R.id.ordersummary);
            }
            if (position % 2 == 1) {
                vw.setBackgroundColor(vw.getResources().getColor(R.color.colorDetailView));
            } else {
                vw.setBackgroundColor(vw.getResources().getColor(R.color.colorWhite));
            }

            final OrderSummaryCategory category = categories.get(position);

            holder.txtName.setText(!CASE_TYPE_NULL.equals(category.getIdCaseT()) ? ObjectCache.getIdObjectName(CaseTCustomTO.class, category.getIdCaseT()) : context.getString(R.string.all_orders));
            holder.txtCount.setText("" + category.getCount());

            int trCount = category.getTimeReservationCount();
            if (trCount > 0) {
                int color = (category.getTimeReservationWarning() ? R.color.colorHighlight : R.color.colorBlack);
                holder.txtTimeReservationCount.setTextColor(context.getResources().getColor(color));
                holder.txtTimeReservationCount.setText("" + trCount);
            } else {
                holder.txtTimeReservationCount.setText("");
            }
            vw.setTag(category);
        } catch (Exception e) {
            writeLog("OrderSummaryAdapter : getView()", e);
        }
        return vw;
    }

    static class ViewHolder {
        TextView txtName;
        TextView txtCount;
        TextView txtTimeReservationCount;
    }

}
