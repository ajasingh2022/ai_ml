package com.capgemini.sesp.ast.android.ui.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.to.DisplayItem;
import com.skvader.rsp.cft.common.to.custom.base.InfoInterfaceTO;
import com.skvader.rsp.cft.common.to.custom.base.NameInterfaceTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by aditam on 4/25/2017.
 */
public class SespCheckboxAdapter<T extends NameInterfaceTO & InfoInterfaceTO> extends BaseAdapter {

    private final Context context;
    private final boolean multiSelection;
    private List<DisplayItem<T>> valueList = null;
    private Set<Integer> selectedValues = null;

    public SespCheckboxAdapter(Context context, List<DisplayItem<T>> objects, boolean isMultiSelection) {
        super();
        this.context = context;
        this.valueList = objects;
        multiSelection = isMultiSelection;
        selectedValues = new HashSet<Integer>();
    }

    private class ViewHolder {
        CheckBox checkbox;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (this.valueList != null) {
            count = this.valueList.size();
        }
        return count;
    }

    @Override
    public DisplayItem<T> getItem(int position) {
        DisplayItem<T> obj = null;
        if (this.valueList != null && this.valueList.size() > position) {
            obj = this.valueList.get(position);
        }
        return obj;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.check_box_text_row_item_layout, parent, false);
        holder.checkbox = rowView.findViewById(R.id.cb);
        holder.checkbox.setText(getItem(position).getName());
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!multiSelection) {
                    clearPreviousChecked();
                }
                if (!holder.checkbox.isChecked()) {
                    selectedValues.remove(position);
                }
                if (holder.checkbox.isChecked()) {
                    selectedValues.add(position);
                }

            }
        });
        if (selectedValues.contains(position)) {
            holder.checkbox.setChecked(true);
        } else {
            holder.checkbox.setChecked(false);
        }
        return rowView;
    }

    private void clearPreviousChecked() {
        selectedValues.clear();
        notifyDataSetChanged();
    }

    /**
     * this will return whether the user has selected any reason
     *
     * @return
     */
    public boolean isItemChecked() {
        return selectedValues != null && !selectedValues.isEmpty();
    }

    /**
     * This method returns the position of selected items
     * if multiSelection is true, user can select multiple items
     * if not selectedValues contains only one item
     *
     * @return
     */
    public Set<Integer> getSelectedPositions() {
        return selectedValues;
    }

    /**
     * This method returns the ids of selected items
     * if multiSelection is true, user can select multiple items
     * if not selectedValues contains only one item
     *
     * @return
     */
    public List<Long> getSelectedItemIds() {
        List<Long> selectedItemIds = new ArrayList<Long>();
        try {
            for (Integer position : selectedValues) {
                selectedItemIds.add(getItemId(position));
            }
        } catch (Exception e) {
            writeLog("SespCheckboxAdapter    : showErrorDialog() ", e);
        }

        return selectedItemIds;
    }

}
