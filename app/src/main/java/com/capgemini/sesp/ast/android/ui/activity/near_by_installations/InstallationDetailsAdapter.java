package com.capgemini.sesp.ast.android.ui.activity.near_by_installations;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoAddressTO;
import com.skvader.rsp.ast_sep.common.to.custom.NearByWorkOrder;
import com.skvader.rsp.cft.common.util.Utils;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.CaseTCustomTO;

import java.util.List;


public class InstallationDetailsAdapter extends RecyclerView.Adapter<InstallationDetailsAdapter.ViewHolder> {
    private final Activity context;
    List<NearByWorkOrder> nearByWorkOrders;
    private int selectedPosition = -1;


    public InstallationDetailsAdapter(Activity context,
                                      List<NearByWorkOrder> nearByWorkOrders) {
        this.context = context;
        this.nearByWorkOrders = nearByWorkOrders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_row_near_by_installtion, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

         {
            NearByWorkOrder nearByWorkOrder = nearByWorkOrders.get(position);
            {
                if(nearByWorkOrder.getAssignedUser()==null){
                    holder.nameUser.setText(R.string.cannot_be_shown_restricted_domain);
                    holder.phoneNumber.setText(R.string.cannot_be_shown_restricted_domain);
                }else {
                    holder.nameUser.setText(nearByWorkOrder.getAssignedUser().getFirstName());
                    holder.phoneNumber.setText(nearByWorkOrder.getAssignedUser().getCellPhone());
                }
                holder.nameUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.hideShowView.getVisibility() == View.VISIBLE) {
                            holder.hideShowView.setVisibility(View.GONE);
                            selectedPosition = -1;
                        }
                        else {
                            holder.hideShowView.setVisibility(View.VISIBLE);
                            selectedPosition=position;
                        }
                        notifyDataSetChanged();
                    }
                });

                if (position == selectedPosition) {
                    holder.nameUser.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_up_black_24dp, 0);
                    holder.hideShowView.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.hideShowView.setVisibility(View.GONE);
                    holder.nameUser.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop_down_black_24dp,0);
                }
            }

            holder.isExpanded = false;

            holder.woId.setText(nearByWorkOrder.getIdCase().toString());

            holder.woType.setText(ObjectCache.getIdObjectName(CaseTCustomTO.class,
                    nearByWorkOrder.getIdCaseType()));


            StringBuilder address = new StringBuilder();

            WoAddressTO woAddressTO = nearByWorkOrder.getAddress();

            String careOf = TypeDataUtil.getOrgOrDiv(woAddressTO.getCareOfO(), woAddressTO.getCareOfD(), woAddressTO.getCareOfV());
            if (Utils.isNotEmpty(careOf)) address.append(careOf).append(" ");

            String apartNumber = TypeDataUtil.getOrgOrDiv(woAddressTO.getApartmentNumberO(), woAddressTO.getApartmentNumberD(), woAddressTO.getApartmentNumberV());
            if (Utils.isNotEmpty(apartNumber)) address.append(apartNumber).append(" ");

            String streetNumber = TypeDataUtil.getOrgOrDiv(woAddressTO.getStreetNumberO(), woAddressTO.getStreetNumberD(), woAddressTO.getStreetNumberV());
            if (Utils.isNotEmpty(streetNumber)) address.append(streetNumber).append(" ");

            String street = TypeDataUtil.getOrgOrDiv(woAddressTO.getStreetO(), woAddressTO.getStreetD(), woAddressTO.getStreetV());
            if (Utils.isNotEmpty(street)) address.append(street).append(" ");

            String city = TypeDataUtil.getOrgOrDiv(nearByWorkOrder.getAddress().getCityO(),
                    nearByWorkOrder.getAddress().getCityO(), nearByWorkOrder.getAddress().getCityV());
            if (Utils.isNotEmpty(city)) address.append(city).append(" ");

            String postCode = TypeDataUtil.getOrgOrDiv(woAddressTO.getPostcodeO(), woAddressTO.getPostcodeD(), woAddressTO.getPostcodeV());
            if (Utils.isNotEmpty(postCode)) address.append(postCode).append(" ");
            holder.address.setText(address.toString());

        }
    }

    @Override
    public int getItemCount() {
        return nearByWorkOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameUser;
        TextView phoneNumber;
        TextView woId;
        TextView woType;
        TextView address;
        LinearLayout hideShowView;
        ImageButton expandCollapseButton;
        Boolean isExpanded;


        public ViewHolder(View itemView) {
            super(itemView);
            phoneNumber = (TextView) itemView.findViewById(R.id.phoneNumber);

            nameUser = (TextView) itemView.findViewById(R.id.nameUser);


            woId = (TextView) itemView.findViewById(R.id.woId);
            woType = (TextView) itemView.findViewById(R.id.woType);
            address = (TextView) itemView.findViewById(R.id.address);
            hideShowView = (LinearLayout) itemView.findViewById(R.id.hideShowView);
        }
    }
}
