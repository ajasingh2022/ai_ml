package com.capgemini.sesp.ast.android.ui.activity.material_logistics.register_new_broken_device;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.ui.layout.SespListAdapter;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitIdentifierTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitTTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

class BrokenNewDeviceAdapter extends SespListAdapter<BrokenNewDeviceItem> {

    public BrokenNewDeviceAdapter(Context context, List<BrokenNewDeviceItem> items, Integer layout) {
        super(context, items, layout);
    }

    @Override
    protected View buildView(BrokenNewDeviceItem item, View convertView, final int position) {
        try {
            TextView idTextView = convertView.findViewById(R.id.activityAddRemoveListRowId);

            String unitType = ObjectCache.getTypeName(UnitTTO.class, item.getIdUnitT());
            String unitModel = ObjectCache.getTypeName(UnitModelCustomTO.class, item.getIdUnitModelT());

            idTextView.setText(item.getIdentifier());

            TextView activityAddRemoveListUnitTypeUnitModel = convertView.findViewById(R.id.activityAddRemoveListUnitInfo);
            activityAddRemoveListUnitTypeUnitModel.setText(ObjectCache.getTypeName(UnitIdentifierTTO.class, item.getIdUnitIdentifierT()) + "   " + (Utils.isNotEmpty(unitType) ? unitType : "") + "   " + (Utils.isNotEmpty(unitModel) ? unitModel : ""));

            TextView reasonTextView = convertView.findViewById(R.id.activityAddRemoveListRowReason);
            reasonTextView.setText(item.getUnitStatusReasonTTO().getName());

            ImageView iconView = convertView.findViewById(R.id.removeIconBorkenNewDeviceRow);

            iconView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    RegisterNewBrokenDeviceActivity.removeItem(position);
                }
            });
        } catch (Exception e) {
            writeLog("BrokenNewDeviceAdapter :buildView()", e);
        }
        return convertView;
    }

}
