package com.capgemini.sesp.ast.android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.OrderListTabs;
import com.capgemini.sesp.ast.android.module.util.GuiDate;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.ui.activity.order.OrderListActivity;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitIdentifierTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;

import java.lang.ref.SoftReference;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class WorkOrderCategoryAdapter extends BaseAdapter {

    private SoftReference<OrderListActivity> contextRef = null;
    private final int currentVisibleLayout;

    public WorkOrderCategoryAdapter(OrderListActivity context, int currentVisibleLayout) {
        if (context != null) {
            contextRef = new SoftReference<OrderListActivity>(context);
        }

        this.currentVisibleLayout = currentVisibleLayout;
    }

    @Override
    public int getCount() {
        int count = 0;
        try {
            if (contextRef != null
                    && contextRef.get() != null
                    && contextRef.get().getWorkorderList() != null) {
                count = contextRef.get().getWorkorderList().size();
            }
        } catch (Exception e) {
            writeLog("WorkOrderCategoryAdapter  : getCount() ", e);
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        try {
            if (contextRef != null
                    && contextRef.get() != null) {
                final List<WorkorderLiteTO> woList = contextRef.get().getWorkorderList();
                if (woList != null && position < woList.size()) {
                    item = woList.get(position);
                }
            }
        } catch (Exception e) {
            writeLog("WorkOrderCategoryAdapter  : getItem() ", e);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {
        View vw = convertView;
        try {
            if (contextRef != null && contextRef.get() != null) {
                final OrderListActivity context = contextRef.get();
                if (vw == null) {
                    vw = LayoutInflater.from(context).inflate(R.layout.order_list_category_list_row, parent, false);
                }

                final WorkorderLiteTO wo = context.getWorkorderList().get(position);

                switch (currentVisibleLayout) {
                    case OrderListTabs.METER:
                        // Get the layout for each item in the list view
                        vw = LayoutInflater.from(context).inflate(R.layout.order_list_meter_list_row, parent, false);

                        final WoUnitTO meterWoUnitTO = wo.getMeterWoUnitTO();
                        if (meterWoUnitTO != null) {
                            String identifierValue = "";
                            if (meterWoUnitTO.getIdUnitIdentifierT().equals(CONSTANTS().UNIT_IDENTIFIER_T__GIAI)) {
                                identifierValue = meterWoUnitTO.getGiai();
                            } else if (meterWoUnitTO.getIdUnitIdentifierT().equals(CONSTANTS().UNIT_IDENTIFIER_T__SERIALNO)) {
                                identifierValue = meterWoUnitTO.getSerialNumber();
                            } else if (meterWoUnitTO.getIdUnitIdentifierT().equals(CONSTANTS().UNIT_IDENTIFIER_T__PROPERTYNO)) {
                                identifierValue = TypeDataUtil.getValidOrgDivValue(meterWoUnitTO, WoUnitTO.PROPERTY_NUMBER_D);
                            }

                            final TextView meterNumTv = vw.findViewById(R.id.meterNumTv);
                            // Populate meter number
                            if (meterNumTv != null) {
                                meterNumTv.setText(ObjectCache.getTypeName(UnitIdentifierTTO.class, meterWoUnitTO.getIdUnitIdentifierT()) + "/" + identifierValue);
                            }

                            final TextView meterModelTv = vw.findViewById(R.id.meterModelTv);
                            if (meterNumTv != null) {
                                meterModelTv.setText(ObjectCache.getIdObjectName(UnitModelCustomTO.class, meterWoUnitTO.getUnitModel()));
                            }
                        }

                        break;

                    case OrderListTabs.CUSTOMER:
                        vw = LayoutInflater.from(context).inflate(R.layout.order_list_customer_list_row, parent, false);
                        final TextView nameTv = vw.findViewById(R.id.customerNameTv);
                        if (nameTv != null) {
                            nameTv.setText(wo.getCustomerName());
                        }

                        break;

                    case OrderListTabs.ADDRESS:
                        vw = LayoutInflater.from(context).inflate(R.layout.order_list_address_list_row, parent, false);
                        final TextView addressTv = vw.findViewById(R.id.addressStreetTv);
                        if (addressTv != null) {
                            addressTv.setText(wo.getAddressStreet());
                        }


                        break;

                    case OrderListTabs.TIME_RESERVATION:
                        vw = LayoutInflater.from(context).inflate(R.layout.order_list_time_res_list_row, parent, false);
                        final TextView timeresStartTv = vw.findViewById(R.id.timeresStartTv);
                        if (wo.getTimeReservationStart() != null) {
                            GuiDate timeresStart = GuiDate.toGuiDateUsersTimesZone(wo.getTimeReservationStart(), true);
                            timeresStartTv.setText(GuiDate.formatSameDateAndTime(wo.getTimeReservationStart(), wo.getTimeReservationEnd()));
                        }
                        break;

                    case OrderListTabs.SLA:
                        vw = LayoutInflater.from(context).inflate(R.layout.order_list_sla_list_row, parent, false);

                        final TextView deadlineTv = vw.findViewById(R.id.deadlineTv);
                        final TextView relativeTimeTv = vw.findViewById(R.id.relativeTimeTv);

                        if (wo.getSlaDeadline() != null
                                && deadlineTv != null && relativeTimeTv != null) {
                            GuiDate slaDeadline = GuiDate.toGuiDateUsersTimesZone(wo.getSlaDeadline(), true);
                            deadlineTv.setText(slaDeadline.toString());
                            relativeTimeTv.setText(slaDeadline.getRelativeTime());
                        }
                        break;

                    case OrderListTabs.KEY:
                        vw = LayoutInflater.from(context).inflate(R.layout.order_list_key_list_row, parent, false);
                        final TextView keyNumTv = vw.findViewById(R.id.keyNumTv);
                        final TextView keyInfoTv = vw.findViewById(R.id.keyInfoTv);
                        if (keyNumTv != null) {
                            keyNumTv.setText(wo.getKeyNumber());
                        }
                        if (keyInfoTv != null) {
                            keyInfoTv.setText(wo.getKeyInfo());
                        }

                        break;

                    default:
                        break;
                }
            }
        } catch (Exception e) {
            writeLog("WorkOrderCategoryAdapter  : getItem() ", e);
        }
        return vw;
    }

    private void setDetails(View convertView, String leftColumn, String rightColumn, String additionalInfo) {
        setDetail(convertView, R.id.detail1, leftColumn, null);
        setDetail(convertView, R.id.detail2, rightColumn, null);
        setDetail(convertView, R.id.detail3, additionalInfo, null);
    }

    private void setDetail(View convertView, int id, String text, Boolean useWarningColor) {
        try {
            if (contextRef != null && contextRef.get() != null) {
                final Context context = contextRef.get();
                TextView detailTextView = convertView.findViewById(id);
                if (text != null) {
                    detailTextView.setVisibility(View.VISIBLE);
                    detailTextView.setText(text);

                    if (useWarningColor != null) {
                        detailTextView.setTextColor(context.getResources().getColor(useWarningColor ? R.color.colorHighlight : R.color.colorBlack));
                    }
                } else if (detailTextView != null) {
                    //detailTextView.setVisibility(View.GONE);
                    detailTextView.setText("");
                }
            }
        } catch (Exception e) {
            writeLog("WorkOrderCategoryAdapter  : setDetail() ", e);
        }
    }

}
