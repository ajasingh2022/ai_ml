package com.capgemini.sesp.ast.android.ui.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class SespListAdapter<T extends SespListItem> extends BaseAdapter {
	private List<T> items;
	private LayoutInflater mInflater;
	private Integer layout;

	public SespListAdapter(Context context, List<T> items, Integer layout) {
		this.items = items;
		mInflater = LayoutInflater.from(context);
		this.layout = layout;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	/**
	 * 
	 * @param position
	 * @return Will return position, if this method is not overridden.
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View vw = convertView;
		if (vw == null) {
			vw =  mInflater.inflate(layout, parent, false);
		}
		final T item = items.get(position);
				
		return buildView(item, vw, position);
	}

	protected abstract View buildView(T item, View convertView, final int position);
	
}
