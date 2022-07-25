package com.capgemini.sesp.ast.android.ui.activity.material_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;

import java.util.List;

/**
 * Created by nalwarsa on 7/30/2018.
 */

public class MaterialKeyAdapter extends BaseAdapter {

    private final Context context;
    private final List<MateriaTypeCategory> categories;
    private final LayoutInflater mInflater;

    public MaterialKeyAdapter(final Context context, final List<MateriaTypeCategory> categories)
    {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View vw ;
        ViewHolder holder;
        if(convertView==null)
        {
            vw=new View(context);
        }
        else
        {
            vw= convertView;
        }
            vw = mInflater.inflate(R.layout.material_list_keys_row, null);
            holder = new ViewHolder();
            holder.txtDetail1 = vw.findViewById(R.id.detail1);
            holder.txtDetail2 = vw.findViewById(R.id.detail2);
            vw.setTag(holder);
            final MateriaTypeCategory c = categories.get(position);
            if (c != null) {
                if (c.getDetail1() != null)
                    holder.txtDetail1.setText(c.getDetail1());
                if (c.getDetail2() != null)
                    holder.txtDetail2.setText(c.getDetail2());
            }
            if (position % 2 == 1) {
                vw.setBackgroundColor(vw.getResources().getColor(R.color.colorDetailView));
            } else {
                vw.setBackgroundColor(vw.getResources().getColor(R.color.colorPrimaryLighter));
            }

        return vw;
    }
    static class ViewHolder {
        TextView txtDetail1;
        TextView txtDetail2;
    }
}
