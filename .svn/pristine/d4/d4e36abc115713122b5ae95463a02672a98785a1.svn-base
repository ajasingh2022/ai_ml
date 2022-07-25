package com.capgemini.sesp.ast.android.ui.activity.material_list;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.MaterialListTabs;

import java.util.List;

class MaterialDeviceAdapter extends BaseAdapter {
	private final Context context;
	private final List<MateriaTypeCategory> categories;
	private final LayoutInflater mInflater;
	private int listType;

	public MaterialDeviceAdapter(final Context context, final List<MateriaTypeCategory> categories, int listType) {
		this.context = context;
		this.categories = categories;
		this.listType=listType;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return categories.size();
	}

	@Override
	public Object getItem(final int position) {
		return categories.get(position);
	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View vw = convertView;
		ViewHolder holder;
		if (vw == null) {
			holder = new ViewHolder();
			if (listType == MaterialListTabs.UNITS){
				vw = mInflater.inflate(R.layout.material_list_units_row, null);
				if(vw!=null) {
					holder.txtAmount = vw.findViewById(R.id.detail3);
				}
			} else{
				vw = mInflater.inflate(R.layout.material_list_keys_row, null);
			}
			if(vw!=null){
				holder.txtDetail1 = vw.findViewById(R.id.detail1);
				holder.txtDetail2 = vw.findViewById(R.id.detail2);
				vw.setTag(holder);
			}
		} else {
			holder = (ViewHolder) vw.getTag();
		}

		final MateriaTypeCategory c = categories.get(position);
		// ArrayList<WorkorderLiteTO> list = (ArrayList<WorkorderLiteTO>)
		// c.getWorkorders();
		// holder.txtAmount.setVisibility(View.VISIBLE);
		Log.d("", "===check null=== " + position + "::" + c.getDetail1());
		if (c != null) {
			if (c.getDetail1() != null)
				holder.txtDetail1.setText(c.getDetail1());
			if (c.getDetail2() != null)
				holder.txtDetail2.setText(c.getDetail2());
			if (listType == MaterialListTabs.UNITS)
				holder.txtAmount.setText("" + c.getCount());
			/*else
				holder.txtAmount.setText("" + c.getAmount());*/
		}
		return vw;
	}

	static class ViewHolder {
		TextView txtDetail1;
		TextView txtDetail2;
		TextView txtAmount;
	}

}
