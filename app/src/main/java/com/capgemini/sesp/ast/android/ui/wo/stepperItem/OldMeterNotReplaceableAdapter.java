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

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Set;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class OldMeterNotReplaceableAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    private transient SoftReference<OldMeterChangeYesNoPageFragment> fragmentRef = null;
    private transient List<WoEventResultReasonTTO> values = null;
    private transient Set<WoEventResultReasonTTO> alreadySelected = null;
    private String TAG = OldMeterNotReplaceableAdapter.class.getSimpleName();
    public OldMeterNotReplaceableAdapter(final OldMeterChangeYesNoPageFragment fragment,
                                         final List<WoEventResultReasonTTO> values, final Set<WoEventResultReasonTTO> selectedvalues) {
        super();

        this.fragmentRef = new SoftReference<OldMeterChangeYesNoPageFragment>(fragment);
        this.values = values;
        this.alreadySelected = selectedvalues;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vw = null;
        try{
        boolean matchFound = false;
        if (this.fragmentRef != null && this.fragmentRef.get() != null) {
            final Context context = this.fragmentRef.get().getActivity();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vw = inflater.inflate(R.layout.meter_change_nt_psble_list_layout, null);
            if (vw != null) {
                final TextView textView = vw.findViewById(R.id.ntPsbleReasonTv);
                if (values != null && values.size() > position) {
                    final WoEventResultReasonTTO eventResultReasonTTO = values.get(position);
                    if (eventResultReasonTTO != null) {
                        textView.setText(eventResultReasonTTO.getName());
                        vw.setTag(eventResultReasonTTO);
                        textView.setTag(vw);
                        for (WoEventResultReasonTTO woEventResultReasonTTO : alreadySelected) {
                            if (woEventResultReasonTTO.getId().longValue() == eventResultReasonTTO.getId().longValue()) {
                                matchFound = true;
                            }
                        }
                    }
                }
                //vw.setOnClickListener(this);
                final CheckBox cb = vw.findViewById(R.id.mtrNtPsbleCb);
                if (cb != null) {
                    cb.setOnCheckedChangeListener(this);
                    cb.setTag(vw);
                    if (matchFound) {
                        cb.setChecked(true);
                        matchFound = false;
                    }
                }
            }
        }
        } catch (Exception e) {
            writeLog(TAG + "  : getView() ", e);
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
        if (this.values != null
                && this.values.size() > position) {
            obj = this.values.get(position);
        }
        return obj;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void onCheckedChanged(final CompoundButton buttonView,
                                 final boolean isChecked) {
        try{

        if (buttonView != null && buttonView.getTag() != null
                && this.fragmentRef != null && this.fragmentRef.get() != null) {
            final View parentView = (View) buttonView.getTag();
            final View reasonTv = parentView.findViewById(R.id.ntPsbleReasonTv);
            final WoEventResultReasonTTO eventResultReasonTTO = (WoEventResultReasonTTO) parentView.getTag();
            if (isChecked) {
                this.fragmentRef.get().addNotPossibleReason(eventResultReasonTTO);
            } else {
                this.fragmentRef.get().removeNotPossibleReason(eventResultReasonTTO);
                buttonView.setBackgroundColor(this.fragmentRef.get().getResources().getColor(android.R.color.transparent));
                if (reasonTv != null) {
                    reasonTv.setBackgroundColor(this.fragmentRef.get().getResources().getColor(android.R.color.transparent));
                }
            }
        }
        } catch (Exception e) {
            writeLog(TAG + "  : onCheckedChanged() ", e);
        }
    }
}