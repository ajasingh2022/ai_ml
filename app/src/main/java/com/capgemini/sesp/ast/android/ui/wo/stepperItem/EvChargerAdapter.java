package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.bean.EvChargerListBean;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class EvChargerAdapter extends RecyclerView.Adapter<EvChargerAdapter.ViewHolder> {
    LayoutInflater inf;
    Map<Integer, EvChargerListBean> list = new TreeMap<>();
    Context context;
    Integer chargerCount;
    Integer adapterResumeCheck;
    Map<Integer, EvChargerListBean> resumingData;

    public EvChargerAdapter(Context context, Map<Integer, EvChargerListBean> resumingData/*, Integer chargerCount*/) {
        this.context = context;
        this.resumingData = resumingData;
        this.chargerCount = resumingData.size();
        if (!this.resumingData.isEmpty())
            this.adapterResumeCheck = 0;
        else
            this.adapterResumeCheck = this.chargerCount;
        if (this.resumingData.get(1).getIndex() == null || this.resumingData.get(1).getPower() == null || this.resumingData.get(1).getCurrentType() == null)
            this.adapterResumeCheck = this.resumingData.size();
        inf = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = inf.inflate(R.layout.ev_charger_adapter_item, parent, false);
        return new ViewHolder(listItem);
    }

    public Map<Integer, EvChargerListBean> getChargerDetails() {
        return list;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.connectorSerials.setText(Integer.toString(position + 1));
        if (resumingData != null && adapterResumeCheck < chargerCount) {
            EvChargerListBean evChargerListBean = resumingData.get(position + 1);
            if (evChargerListBean != null) {
                holder.editPower.setText(evChargerListBean.getPower());
                if (evChargerListBean.getCurrentType().equalsIgnoreCase("AC"))
                    holder.currentSpinner.setSelection(((ArrayAdapter<String>)holder.currentSpinner.getAdapter()).getPosition("AC"));
                else if (evChargerListBean.getCurrentType().equalsIgnoreCase("DC"))
                    holder.currentSpinner.setSelection(((ArrayAdapter<String>) holder.currentSpinner.getAdapter()).getPosition("DC"));
            }
            adapterResumeCheck++;
            if (adapterResumeCheck == chargerCount)
                AbstractWokOrderActivity.evInformationRestore = false;
        }

        holder.editPower.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String power = holder.editPower.getText().toString();
                String fragments[] = power.split("\\.");
                try {
                    power = holder.editPower.getText().toString();
                    Double.parseDouble(power);
                    if (Double.parseDouble(power) < 0d) {
                        holder.editPower.setText("0");
                        holder.editPower.setSelection(holder.editPower.getText().toString().length());
                    }
                    if (fragments.length > 1) {
                        if (fragments[1].length() > 3) {
                            holder.editPower.setText(holder.editPower.getText().toString().substring(0, holder.editPower.getText().toString().length() - 1));
                            holder.editPower.setSelection(holder.editPower.getText().toString().length());
                        }
                    }
                } catch (Exception e) {
                    if (fragments.length == 0) {
                        holder.editPower.setText("0.");
                        holder.editPower.setSelection(holder.editPower.getText().toString().length());
                    } else if (fragments.length == 3) {
                        String correction = fragments[0] + "." + fragments[1] + fragments[2];
                        holder.editPower.setText(correction);
                        holder.editPower.setSelection(holder.editPower.getText().toString().length());
                    } else if (power.endsWith(".")) {
                        holder.editPower.setText(holder.editPower.getText().toString().substring(0, holder.editPower.getText().toString().length() - 1));
                        holder.editPower.setSelection(holder.editPower.getText().toString().length());
                    }
                }
                fillDataForConnectors(holder, position);
            }
        });
        holder.currentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fillDataForConnectors(holder, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void fillDataForConnectors(ViewHolder holder, int position) {
        final Editable power = holder.editPower.getText();
        final Object currentType = holder.currentSpinner.getSelectedItem();
        EvChargerListBean evChargerListBean = null;
        if (power == null && currentType != null) {
            evChargerListBean = new EvChargerListBean("", currentType.toString(), position + 1);
        } else if (power != null && currentType == null) {
            evChargerListBean = new EvChargerListBean(power.toString(), "", position + 1);
        } else if (power != null && currentType != null) {
            evChargerListBean = new EvChargerListBean(power.toString(), currentType.toString(), position + 1);
        } else if (power == null && currentType == null) {
            evChargerListBean = new EvChargerListBean("", "", position + 1);
        }
        list.put(position, evChargerListBean);
    }

    @Override
    public int getItemCount() {
        return chargerCount;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView connectorSerials;
        EditText editPower;
        Spinner currentSpinner;

        public ViewHolder(View itemView) {
            super(itemView);
            connectorSerials = itemView.findViewById(R.id.connectorSerials);
            editPower = itemView.findViewById(R.id.editPower);
            currentSpinner = itemView.findViewById(R.id.currentSpinner);
        }
    }
}
