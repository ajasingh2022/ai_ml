/**
 * @copyright Capgemini
 */
package com.capgemini.sesp.ast.android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.capgemini.sesp.ast.android.R;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;

import java.lang.ref.SoftReference;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * This adapter will be used in multiple fragments
 * and based on the provided list of {@link UnitModelCustomTO} it will show them in list view
 *
 * @author Capgemini
 * @version 1.0
 * @since 12th March, 2015
 */
public class UnitModelAdapter extends BaseAdapter {

    private transient List<UnitModelCustomTO> unitModelCustomTOLs = null;
    private transient SoftReference<Fragment> fragmentRef = null;

    public UnitModelAdapter(final Fragment fragment,
                            final List<UnitModelCustomTO> unitModelCustomTOLs) {
        this.fragmentRef = new SoftReference<Fragment>(fragment);
        if (unitModelCustomTOLs != null
                && !unitModelCustomTOLs.isEmpty()) {
            this.unitModelCustomTOLs = unitModelCustomTOLs;
        }
    }

    @Override
    public int getCount() {
        int count = 0;
        try {
            if (this.unitModelCustomTOLs != null) {
                count = this.unitModelCustomTOLs.size();
            }
        } catch (Exception e) {
            writeLog("UnitModelAdapter  : getCount() ", e);
        }
        return count;
    }

    @Override
    public Object getItem(final int position) {
        UnitModelCustomTO item = null;
        try {
            if (this.unitModelCustomTOLs != null && position < this.unitModelCustomTOLs.size()) {
                item = this.unitModelCustomTOLs.get(position);
            }
        } catch (Exception e) {
            writeLog("UnitModelAdapter  : getItem() ", e);
        }
        return item;
    }

    @Override
    public long getItemId(final int position) {
        return unitModelCustomTOLs.get(position).getId();
    }

    public int getPosition(final UnitModelCustomTO unitModelCustomTO) {
        int position = 0;
        try {
            if (unitModelCustomTO != null && unitModelCustomTOLs != null
                    && !unitModelCustomTOLs.isEmpty()
                    && unitModelCustomTOLs.contains(unitModelCustomTO)) {
                position = unitModelCustomTOLs.indexOf(unitModelCustomTO);
            }
        } catch (Exception e) {
            writeLog("UnitModelAdapter  : getView() ", e);
        }
        return position;
    }

    public int getPositionOfItem(UnitModelCustomTO unitModelCustomTO) {
        try {
            for (UnitModelCustomTO unitModelCustomTO1 : unitModelCustomTOLs) {
                if (unitModelCustomTO.getId() == unitModelCustomTO1.getId())
                    return getPosition(unitModelCustomTO1);

            }
        } catch (Exception e) {
            writeLog("UnitModelAdapter  : getView() ", e);
        }
        return 0;
    }

    private class ViewHolder {
        public TextView unitModelNameTextView;
    }

    /**
     * Inflate the custom layout based on the adapter value
     * for current position
     *
     * @param position    ({@link Integer})
     * @param convertView ({@link View})
     * @param parent      {@link ViewGroup}
     */

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        try {
            ViewHolder holder = new ViewHolder();
            LayoutInflater inflater = null;
            UnitModelCustomTO unitModelCustomTO = (UnitModelCustomTO) getItem(position);
            if (this.fragmentRef != null && this.fragmentRef.get() != null) {
                inflater = (LayoutInflater) this.fragmentRef.get().getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            //If holder not exist then locate all view from UI file.
            if (convertView == null) {
                // inflate UI from XML file
                convertView = inflater.inflate(R.layout.unit_model_list_row, parent, false);
                // get all UI view
                holder.unitModelNameTextView = convertView.findViewById(R.id.unitModelNameTextView);
                // set tag for holder
                convertView.setTag(holder);
            } else {
                // if holder created, get tag from view
                holder = (ViewHolder) convertView.getTag();
            }
            if (unitModelCustomTO != null) {
                holder.unitModelNameTextView.setText(unitModelCustomTO.getName());
            }
        } catch (Exception e) {
            writeLog("UnitModelAdapter  : getView() ", e);
        }
        return convertView;
    }

}