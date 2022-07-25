package com.capgemini.sesp.ast.android.ui.customview;

import android.content.Context;
import android.content.res.Resources;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by samdasgu on 10/18/2016.
 */
public class SESPSwitchPreference extends SwitchPreference {

    private final Listener mListener = new Listener();

    private class Listener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!callChangeListener(isChecked)) {
                // Listener didn't like it, change it back.
                // CompoundButton will make sure we don't recurse.
                buttonView.setChecked(!isChecked);
                return;
            }

            SESPSwitchPreference.this.setChecked(isChecked);
        }
    }

    public SESPSwitchPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SESPSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SESPSwitchPreference(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        try {
            View checkableView = view.findViewById(R.id.custom_switch_item);
            if (checkableView != null && checkableView instanceof Checkable) {
                ((Checkable) checkableView).setChecked(this.isChecked());

                //sendAccessibilityEvent(checkableView);

                if (checkableView instanceof Switch) {
                    final Switch switchView = (Switch) checkableView;
                    final Resources resources = ApplicationAstSep.context.getResources();
                    switchView.setTextOn(resources.getString(android.R.string.yes));
                    switchView.setTextOff(resources.getString(android.R.string.no));
                    switchView.setOnCheckedChangeListener(mListener);
                }
            }

            //syncSummaryView(view);

        } catch (Exception e) {
            writeLog(" SESPSwitchPreference : onBindView() ", e);
        }
    }
}
