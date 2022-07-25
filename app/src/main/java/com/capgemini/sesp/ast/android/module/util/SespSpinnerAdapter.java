package com.capgemini.sesp.ast.android.module.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.module.util.to.DisplayItem;
import com.skvader.rsp.cft.common.to.custom.base.InfoInterfaceTO;
import com.skvader.rsp.cft.common.to.custom.base.NameInterfaceTO;

import java.util.List;


public class SespSpinnerAdapter<T extends NameInterfaceTO & InfoInterfaceTO> extends ArrayAdapter<DisplayItem<T>> {

    private List<DisplayItem<T>> objects;

    public SespSpinnerAdapter(Context context, int resource, List<DisplayItem<T>> objects) {
        super(context, resource);
        this.objects = objects;
    }

    public SespSpinnerAdapter(Context context, int resource, int textViewResourceId, List<DisplayItem<T>> objects) {
        super(context, resource, textViewResourceId);
        this.objects = objects;
    }


    @Override
    public void add(DisplayItem<T> obj) {
        objects.add(obj);
    }

    @Override
    public void clear() {
        objects.clear();
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public DisplayItem<T> getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        T userObject = objects.get(position).getUserObject();
        return userObject.getId();
    }

    public int getItemPosition(Long itemId) {
        for(DisplayItem displayItem : objects) {
            if(displayItem.getId() == itemId) {
                return objects.indexOf(displayItem);
            }
        }
        return -1;
    }

    @Override
    public void remove(DisplayItem obj) {
        objects.remove(obj);
    }

    public List<DisplayItem<T>> getItems() {
        return objects;
    }

    public void setItems(List<DisplayItem<T>> items) {
        objects = items;
    }

    @Override
    public View getDropDownView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new TextView(getContext());
            convertView.setMinimumHeight((int) GuIUtil.convertDpToPixel(30f, getContext()));
            //convertView.setMargins(0, (int) GuIUtil.convertDpToPixel(10f, getContext()), 0, 0);
        }

        TextView item = (TextView) convertView;
        item.setText(objects.get(position).getName());
        final TextView finalItem = item;
        item.post(new Runnable() {
            @Override
            public void run() {
                finalItem.setSingleLine(false);
            }
        });
        return item;
    }
}
