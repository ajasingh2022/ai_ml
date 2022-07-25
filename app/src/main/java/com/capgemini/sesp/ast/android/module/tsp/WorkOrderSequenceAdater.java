package com.capgemini.sesp.ast.android.module.tsp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.capgemini.sesp.ast.android.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

class WorkOrderSequenceAdater extends RecyclerView.Adapter<WorkOrderSequenceAdater.ViewHolder> {
    public WorkOrderSequenceAdater(MapsActivity mapsActivity, ArrayList<WorkOrder> items) {
        this.items =  items;
        layoutInflater = LayoutInflater.from(mapsActivity);
        this.mapsActivity = mapsActivity;

    }

    MapsActivity mapsActivity;
    SimpleDateFormat dislayFormat = new SimpleDateFormat("HH:mm");
    private ArrayList<WorkOrder> items;

    private LayoutInflater layoutInflater;

    int EXPANDED = 0;
    int COLLAPSED = 1;

    int selectedPosition = -1;




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View listItem= layoutInflater.inflate(R.layout.sequence_list_adapter, parent, false);
        LinearLayout linearLayout = listItem.findViewById(R.id.expandedView);


        if (viewType == EXPANDED){
            linearLayout.setVisibility(View.VISIBLE);
        }

        if (viewType == COLLAPSED){
            linearLayout.setVisibility(View.GONE);
        }

        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position == selectedPosition){
            holder.exandableView.setVisibility(View.VISIBLE);
            holder.etc.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_up_black_24dp,0);
        }
        else {
            holder.etc.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop_down_black_24dp,0);
            holder.exandableView.setVisibility(View.GONE);
        }

        WorkOrder workOrder = items.get(position);
        String caseId;
        if (position == 0)
         caseId = "Current Location -->" +workOrder.workorderLiteTO.getId();
        else
            caseId = items.get(position-1).workorderLiteTO.getId() +"-->" +workOrder.workorderLiteTO.getId();

        holder.etc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.exandableView.getVisibility() == View.VISIBLE) {
                    holder.exandableView.setVisibility(View.GONE);
                    selectedPosition = -1;
                }
                else {
                    holder.exandableView.setVisibility(View.VISIBLE);
                    selectedPosition=position;
                }
                notifyDataSetChanged();
            }
        });

        holder.etc.setText(dislayFormat.format(workOrder.getEtc()));
        holder.timeReservation.setText(workOrder.getWorkorderLiteTO().getTimeReservationEnd()
                == null ?"N/A": String.valueOf(workOrder.workorderLiteTO.getTimeReservationStart())+"-"+String.valueOf(workOrder.workorderLiteTO.getTimeReservationEnd()));
        holder.sla.setText(workOrder.sla.contains("null") ? "N/A":workOrder.sla);
        holder.casiId.setText(caseId);
        holder.casiId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.casiId.setBackgroundColor(0);
                if (!workOrder.selected){
                    holder.casiId.setBackgroundColor(mapsActivity.getApplicationContext().getResources()
                            .getColor(R.color.aqua_start));
                }
                workOrder.selected = !workOrder.selected;
                mapsActivity.updateRoad(items.get(position),workOrder.selected);

            }
        });


        long tt = workOrder.travelTime;
        int ss = (int)tt % 60;
        int hh = (int)tt / 60;
        int mm = hh % 60;
        hh = hh / 60;
        String ttS = ( hh + ":" + mm + ":" + ss);

        holder.travelTime.setText(ttS);

    }

    @Override
    public int getItemViewType(int position) {
        if (selectedPosition == position){
            return  EXPANDED;
        }
        else
            return COLLAPSED;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView casiId;
        TextView etc;
        TextView timeReservation;
        TextView sla;
        LinearLayout exandableView;
        LinearLayout parentview;
        TextView travelTime;

        public ViewHolder(View itemView) {
            super(itemView);
            casiId = itemView.findViewById(R.id.wo_id_text);
            etc = itemView.findViewById(R.id.wo_etc);
            sla = itemView.findViewById(R.id.sla);
            timeReservation = itemView.findViewById(R.id.time_reservation);
            exandableView = itemView.findViewById(R.id.expandedView);
            parentview = itemView.findViewById(R.id.parentLayout);
            travelTime = itemView.findViewById(R.id.travel_time_tv);
        }
    }

}