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
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitStatusReasonTTO;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Set;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by svadera on 9/28/2018.
 */

public class ChangeMeterReasonAdapter extends BaseAdapter {
    private transient SoftReference<ChangeMeterReasonPageFragment> fragmentRef = null;
    private transient List<UnitStatusReasonTTO> values = null;
    private transient Set<UnitStatusReasonTTO> alreadySelected = null;
    private int selectedPosition = -1;
    static String TAG = ChangeMeterReasonAdapter.class.getSimpleName();

    public ChangeMeterReasonAdapter(final ChangeMeterReasonPageFragment fragment,
                                    final List<UnitStatusReasonTTO> values, final Set<UnitStatusReasonTTO> selectedvalues) {
        super();

        this.fragmentRef = new SoftReference<ChangeMeterReasonPageFragment>(fragment);
        this.values = values;
        this.alreadySelected = selectedvalues;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vw = null;
        try {
            boolean matchFound = false;
            if (this.fragmentRef != null && this.fragmentRef.get() != null) {
                final Context context = this.fragmentRef.get().getActivity();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vw = inflater.inflate(R.layout.meter_change_nt_psble_list_layout, null);
                if (vw != null) {
                    final TextView textView = vw.findViewById(R.id.ntPsbleReasonTv);
                    if (values != null && values.size() > position) {
                        final UnitStatusReasonTTO unitStatusReasonTTO = values.get(position);
                        if (unitStatusReasonTTO != null) {
                            textView.setText(unitStatusReasonTTO.getName());
                            vw.setTag(unitStatusReasonTTO);
                            textView.setTag(vw);

                            for (UnitStatusReasonTTO reasons : alreadySelected) {

                                if (reasons.getId() != null) {

                                    if (reasons.getId().longValue() == unitStatusReasonTTO.getId().longValue()) {
                                        matchFound = true;
                                    }

                                }
                            }
                        }
                    }

                    final CheckBox cb = vw.findViewById(R.id.mtrNtPsbleCb);
                    if (cb != null) {
                        cb.setTag(vw);
                        if (matchFound || position == selectedPosition) {
                            cb.setChecked(true);
                        } else {
                            cb.setChecked(false);
                        }

                        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                final View parentView = (View) buttonView.getTag();
                                final View reasonTv = parentView.findViewById(R.id.ntPsbleReasonTv);
                                final UnitStatusReasonTTO unitStatusReasonTTO = (UnitStatusReasonTTO) parentView.getTag();
                                if (isChecked) {
                                    selectedPosition = position;
                                    fragmentRef.get().addChangeMeterReason(unitStatusReasonTTO);
                                    buttonView.setBackgroundColor(fragmentRef.get().getResources().getColor(android.R.color.holo_blue_light));
                                    if (reasonTv != null) {
                                        reasonTv.setBackgroundColor(fragmentRef.get().getResources().getColor(android.R.color.holo_blue_light));
                                    }
                                } else {
                                    selectedPosition = -1;
                                    fragmentRef.get().removeChangeMeterReason(unitStatusReasonTTO);
                                    buttonView.setBackgroundColor(fragmentRef.get().getResources().getColor(android.R.color.transparent));
                                    if (reasonTv != null) {
                                        reasonTv.setBackgroundColor(fragmentRef.get().getResources().getColor(android.R.color.transparent));
                                    }
                                }
                                notifyDataSetChanged();

                            }
                        });
                    }


                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : showHideReasonLayout() ", e);
        }
        return vw;
    }


    @Override
    public int getCount() {
        int count = 0;
        if (this.values != null) {
            count = this.values.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        Object obj = null;
        if (this.values != null && this.values.size() > position) {
            obj = this.values.get(position);
        }
        return obj;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
