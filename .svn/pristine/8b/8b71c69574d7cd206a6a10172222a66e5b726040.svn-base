package com.capgemini.sesp.ast.android.ui.activity.order;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.ui.adapter.ChildViewHolder;
import com.skvader.rsp.ast_sep.common.to.ast.table.AndrCaseTListTTO;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.CaseTCustomTO;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class OrderSummaryItemsViewHolder extends ChildViewHolder {

    private TextView mOrderSummaryItemsTextViewName;
    private TextView mOrderSummaryItemsTextViewInfo;
    private LinearLayout app_layer;
    private WorkorderLiteTO workOrder;


    public OrderSummaryItemsViewHolder(final View itemView) {
        super(itemView);
        try {
            app_layer = itemView.findViewById(R.id.orderitem_layout);
            mOrderSummaryItemsTextViewName = itemView.findViewById(R.id.item_name);
            mOrderSummaryItemsTextViewInfo = itemView.findViewById(R.id.item_info);

            app_layer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CaseTCustomTO caseT = ObjectCache.getIdObject(CaseTCustomTO.class, workOrder.getIdCaseT());


                    Intent caseIntent;
                    Class launcherWODetailsCls = null;
                    List<AndrCaseTListTTO> andrCaseTListTTOs = ObjectCache.getAllTypes(AndrCaseTListTTO.class);
                    for (final AndrCaseTListTTO tto : andrCaseTListTTOs) {
                        if (workOrder.getIdCaseT().equals(tto.getIdCaseT())) /*workOrder.getIdCaseT().equals(tto.getIdCaseT())*/ {
                            if (tto.getListType().equals("EV") || tto.getListType().equals("METER") || tto.getListType().equals("CONCENTRATOR") || (tto.getListType().equals("SOLAR"))) {
                                launcherWODetailsCls = OrderInfoActivity.class;
                            }
                            break;
                        }
                    }


                    if (launcherWODetailsCls != null) {
                        caseIntent = new Intent(v.getContext(), launcherWODetailsCls);
                        caseIntent.putExtra(ConstantsAstSep.MeterChangeConstants.ID_CASE, workOrder.getId());
                        caseIntent.putExtra(ConstantsAstSep.MeterChangeConstants.ALLOW_START_FLOW, true);
                        v.getContext().startActivity(caseIntent);
                    } else {
                        Toast.makeText(v.getContext(), (v.getResources().getString(R.string.error_not_implemented)), Toast.LENGTH_SHORT).show();
                    }


                }
            });
        } catch (Exception e) {
            writeLog("OrderSummaryItemsViewHolder  : OrderSummaryItemsViewHolder() ", e);
        }

    }


    public void bind(OrderSummaryItems orderItems) {
        try {
            mOrderSummaryItemsTextViewName.setText(orderItems.getmName());
            mOrderSummaryItemsTextViewInfo.setText(orderItems.getmInfo());
            workOrder = orderItems.getWorkOrder();
        } catch (Exception e) {
            writeLog("OrderSummaryItemsViewHolder  : bind() ", e);
        }

    }
}
