package com.capgemini.sesp.ast.android.ui.activity.material_logistics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

@SuppressLint("InflateParams")
class MaterialLogisticsAdapter extends BaseAdapter {
	private List<MaterialLogisticsItem> items;
	private LayoutInflater mInflater;

	public MaterialLogisticsAdapter(Context context, List<MaterialLogisticsItem> items) {
		this.items = items;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View vw = convertView;
		try{
		if (vw == null) {
			vw = mInflater.inflate(R.layout.activity_main_menu_row, null);
		}
		final MaterialLogisticsItem item = items.get(position);
		
		final TextView nameTextView = vw.findViewById(R.id.label);
		nameTextView.setText(item.getName());
		
		if(item.getIconid() != null) {
			ImageView iconView = vw.findViewById(R.id.icon);
			iconView.setImageResource(item.getIconid());
		}
		if(item.getSecondLine() != null) {
		    TextView secondLineTextView = vw.findViewById(R.id.second_line);
		    secondLineTextView.setText(item.getSecondLine());
        }
		vw.setTag(item.getName());
		} catch (Exception e) {
			writeLog("MaterialLogisticsAdapter  : onCreate() ", e);
		}
		return vw;
	}

	static class ViewHolder {
		TextView txtName;
	}
	
}
