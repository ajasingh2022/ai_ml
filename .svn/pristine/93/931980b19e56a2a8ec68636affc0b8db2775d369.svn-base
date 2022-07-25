package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoEventResultReasonTTO;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by svadera on 9/28/2018.
 */

public class TroubleshootNoCommunicatinAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
    private final Context context;
    private transient List<WoEventResultReasonTTO> valueList = null;
    CommunicationFailureReasonPageFragment obj;

    public TroubleshootNoCommunicatinAdapter(final Context context,
                                             final CommunicationFailureReasonPageFragment values) {
        super();
        this.context = context;
        List<WoEventResultReasonTTO> sortReasons = values.woEventResultReasonTTOs;
        Collections.sort(sortReasons, new Comparator<WoEventResultReasonTTO>() {
            @Override
            public int compare(WoEventResultReasonTTO o1, WoEventResultReasonTTO o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        this.valueList = sortReasons;
        values.unselectedReasonIds = new HashSet<Long>();
        obj = values;
    }

    private class ViewHolder {
        TextView text;
        CheckBox checkbox;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            ViewHolder holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final WoEventResultReasonTTO listItem = valueList.get(position);
            //If holder not exist then locate all view from UI file.
            if (convertView == null) {
                // inflate UI from XML file
                convertView = inflater.inflate(R.layout.meter_change_nt_psble_list_layout, parent, false);
                // get all UI view
                holder.text = convertView.findViewById(R.id.ntPsbleReasonTv);
                holder.checkbox = convertView.findViewById(R.id.mtrNtPsbleCb);
                // set tag for holder
                convertView.setTag(holder);
            } else {
                // if holder created, get tag from view
                holder = (ViewHolder) convertView.getTag();
            }
            holder.text.setText(listItem.getName());
            holder.checkbox.setTag(position);
            holder.checkbox.setChecked(listItem.getId() == obj.selectedReason);
            holder.checkbox.setOnCheckedChangeListener(this);
        } catch (NumberFormatException e) {
            writeLog("TroubleshootNoCommunicatinAdapter  : getView()", e);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        int count = 0;
        try {
            if (this.valueList != null) {
                count = this.valueList.size();
            }
        } catch (NumberFormatException e) {
            writeLog("TroubleshootNoCommunicatinAdapter  : getCount()", e);
        }
        return count;
    }

    @Override
    public WoEventResultReasonTTO getItem(int position) {
        WoEventResultReasonTTO obj = null;
        try {

            if (this.valueList != null && this.valueList.size() > position) {
                obj = this.valueList.get(position);
            }
        } catch (NumberFormatException e) {
            writeLog("TroubleshootNoCommunicatinAdapter  : getItem()", e);
        }
        return obj;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        try{
        if (buttonView != null) {
            final int position = (Integer) buttonView.getTag();
            if (isChecked) {
                obj.selectedReason = getItem(position).getId();
                obj.unselectedReasonIds.remove(obj.selectedReason);
            } else {
                obj.unselectedReason = getItem(position).getId();
                obj.unselectedReasonIds.add(obj.unselectedReason);
                if (obj.unselectedReasonIds.size() == valueList.size()) obj.selectedReason = -1;
            }
            notifyDataSetChanged();
        }
        } catch (NumberFormatException e) {
            writeLog("TroubleshootNoCommunicatinAdapter  : getItemId()", e);
        }
    }

    /**
     * this will return whether the user has selected any reason
     *
     * @return
     */
    public boolean isReasonChecked() {
        return obj.selectedReason != -1;
    }

}
