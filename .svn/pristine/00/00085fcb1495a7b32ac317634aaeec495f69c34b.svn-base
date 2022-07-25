package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skvader.rsp.ast_sep.common.to.ast.table.WoInstMepPowStatusTTO;

import java.lang.ref.SoftReference;
import java.util.List;

public class MeterConnectionTypeAdapter extends BaseAdapter {

    private transient SoftReference<Context> contextRef = null;
    private transient List<WoInstMepPowStatusTTO> woInstMepPowStatusTTOs = null;

    public MeterConnectionTypeAdapter(final Context context,
                                      final List<WoInstMepPowStatusTTO> woInstMepPowStatusTTOs) {
        if (context != null) {
            contextRef = new SoftReference<Context>(context);
        }
        this.woInstMepPowStatusTTOs = woInstMepPowStatusTTOs;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (this.woInstMepPowStatusTTOs != null) {
            count = woInstMepPowStatusTTOs.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        if (this.woInstMepPowStatusTTOs != null
                && this.woInstMepPowStatusTTOs.size() > position) {
            item = this.woInstMepPowStatusTTOs.get(position);
        }
        return item;
    }
    public int selectPowerStatusType(final WoInstMepPowStatusTTO tto) {

        int position = 0;
        if (tto != null && woInstMepPowStatusTTOs != null
                && !woInstMepPowStatusTTOs.isEmpty()
                && woInstMepPowStatusTTOs.contains(tto)) {
            position = woInstMepPowStatusTTOs.indexOf(tto);
        }
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView vw = null;
        if (this.contextRef != null && this.contextRef.get() != null) {
            final Context context = this.contextRef.get();
            vw
                    = (TextView) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(android.R.layout.simple_spinner_item, null);

            if (vw != null) {
                final WoInstMepPowStatusTTO instMepPowStatusTto = (WoInstMepPowStatusTTO) getItem(position);
                if (instMepPowStatusTto != null) {
                    vw.setText(instMepPowStatusTto.getName());
                    // Tag the object
                    vw.setTag(instMepPowStatusTto);
                }
            }
        }
        return vw;
    }

}