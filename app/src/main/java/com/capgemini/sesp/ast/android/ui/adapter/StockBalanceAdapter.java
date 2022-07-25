package com.capgemini.sesp.ast.android.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.stock_balance.StockBalanceRow;
import com.capgemini.sesp.ast.android.ui.layout.SespListAdapter;

import java.util.List;

public class StockBalanceAdapter extends SespListAdapter<StockBalanceRow> {
	
	public StockBalanceAdapter(Context context, List<StockBalanceRow> items, Integer layout) {
		super(context, items, layout);
	}

	@Override
    protected View buildView(StockBalanceRow item, View convertView, int position) {
		TextView nameTextView = convertView.findViewById(R.id.stock_balance_row_name);
		nameTextView.setText(item.getName());
		
		TextView statusTextView = convertView.findViewById(R.id.stock_balance_row_status);
		statusTextView.setText(item.getStatus());
		
		TextView amountTextView = convertView.findViewById(R.id.stock_balance_row_count);
		amountTextView.setText(String.valueOf(item.getAmount()));
		
	    return convertView;
    }

	

}
