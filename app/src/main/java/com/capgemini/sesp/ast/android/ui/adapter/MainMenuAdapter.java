package com.capgemini.sesp.ast.android.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.ui.activity.dashboard.MainMenuItem;

import java.util.List;

@SuppressLint("InflateParams")
public class MainMenuAdapter extends BaseAdapter {
	private List<MainMenuItem> items;
	private LayoutInflater mInflater;

	public MainMenuAdapter(Context context, List<MainMenuItem> items) {
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
		if (vw == null) {
			vw = mInflater.inflate(R.layout.activity_main_menu_row, null);
		}
		final MainMenuItem item = items.get(position);
		
		final TextView nameTextView = vw.findViewById(R.id.label);
		nameTextView.setText(item.getName());
		
		if(item.getIconid() != null) {
			final ImageView iconView = vw.findViewById(R.id.icon);
			iconView.setImageResource(item.getIconid());
		}
		return vw;
	}
	
}
