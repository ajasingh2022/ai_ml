package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skvader.rsp.ast_sep.common.to.ast.table.CurrentTransformerPriTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.CurrentTransformerSecTTO;

import java.lang.ref.SoftReference;
import java.util.List;


/**
 * Created by svadera on 8/30/2018.
 */
public class CurrentAmpsAdapter<T> extends BaseAdapter {

    private static final String TAG ="TAG" ;
    private transient SoftReference<Context> contextRef = null;
    private transient List<T> ctTToList = null;
    boolean mIsPrimary = true;

    public CurrentAmpsAdapter(final Context ctx,
                              final List<T> ctTToList, boolean isPrimary) {
        if (ctx != null) {
            this.contextRef = new SoftReference<Context>(ctx);
        }
        this.ctTToList = ctTToList;
        this.mIsPrimary = isPrimary;
    }

    public int getPosition(final Object currentTransformerTTO) {
        int position = 0;
        if (currentTransformerTTO != null && ctTToList != null
                && !ctTToList.isEmpty()
                && ctTToList.contains(currentTransformerTTO)) {
            position = ctTToList.indexOf(currentTransformerTTO);
        }
        return position;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (ctTToList != null) {
            count = ctTToList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        if (ctTToList != null
                && ctTToList.size() > 0
                && ctTToList.size() > position) {
            item = ctTToList.get(position);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView vw = null;
        if (this.contextRef != null && this.contextRef.get() != null) {
            final Context context = this.contextRef.get();
            vw
                    = (TextView) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(android.R.layout.simple_spinner_item, parent, false);
            vw.setEllipsize(null);

            if (vw != null) {
                final Object ctTTo;
                String name;
                ctTTo = getItem(position);
                if (ctTTo != null) {
                    if (mIsPrimary) {
                        name = ((CurrentTransformerPriTTO) ctTTo).getName();
                    } else {
                        name = ((CurrentTransformerSecTTO) ctTTo).getName();
                    }
                    vw.setText(name);
                    vw.setTag(ctTTo);
                    Log.d(TAG, "Setting view tag for meter placement spinner=" + ctTTo);
                }
                else
                {
                    vw.setText("");
                }
            }
        }
        return vw;
    }
}

