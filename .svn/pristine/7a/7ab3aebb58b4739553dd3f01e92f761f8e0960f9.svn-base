package com.capgemini.sesp.ast.android.ui.activity.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.skvader.rsp.ast_sep.common.to.ast.table.UiGroupTTO;

import java.util.List;

/**
 * This class is a adapter for displaying list of items under single field in order info screen
 */
public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.ViewHolder> {
    private int resourceLayout;
    private Context mContext;
    Long id;
    List<String> items;
    LayoutInflater layoutInflater;

    public FieldAdapter(Context context,OrderInfoModel orderInfoModel) {
        this.id=orderInfoModel.id;
        this.items = orderInfoModel.mValues;
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(layoutInflater.inflate(R.layout.field_row_element, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DatabaseHandler databaseHandler=DatabaseHandler.createDatabaseHandler();
        UiGroupTTO groupTTO= ObjectCache.getIdObject(UiGroupTTO.class,id);
        String translatedValue;
        if(items.get(position)!=null) {
            if ((groupTTO.getReturnValueType()) != null && groupTTO.getReturnValueType().equals("code")) {
                translatedValue = databaseHandler.translateDesignation(items.get(position));
                if (translatedValue == null) {
                    translatedValue = items.get(position);
                }
            } else {
                translatedValue = items.get(position);
            }
            String s = "--" + translatedValue;
            holder.value.setText(s);
        }
        else {
            holder.value.setText("");
        }
    }

    @Override
    public int getItemCount() {
        if(items==null)
            return 0;

            else
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView value;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            value = itemView.findViewById(R.id.row_order);
        }
    }
}
