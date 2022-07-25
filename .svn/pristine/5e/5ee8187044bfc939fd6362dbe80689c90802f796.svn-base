package com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.skvader.rsp.ast_sep.common.to.custom.UnitInformationTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoUnitCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class UnitEntryAdapter extends RecyclerView.Adapter<UnitEntryAdapter.ViewHolder> {
    LayoutInflater mInflater;
    Context mContext;
    List<UnitEntryItem> items;
    public UnitCreation unitCreation;

    public UnitEntryAdapter(Context context, List items) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.items = items;
    }

    public interface UnitCreation {
        WoUnitCustomTO createUnitFromUnitEntryItem(UnitEntryItem unitEntryItem);

        void updateUnit(UnitEntryItem unitEntryItem);
    }

    @NonNull
    @Override
    public UnitEntryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.unit_entry_adapter, parent, false);
        UnitEntryAdapter.ViewHolder holder = new UnitEntryAdapter.ViewHolder(view);
        return holder;
    }

    private void showPopupMenu(View view, UnitEntryItem unitEntryItem) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_adapter_unit_entry);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_edit_item) {
                    unitCreation.updateUnit(unitEntryItem);
                }

                if (item.getItemId() == R.id.menu_remove_item) {
                    UnitEntryAdapter.this.items.remove(unitEntryItem);
                    UnitEntryAdapter.this.notifyDataSetChanged();
                }
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onBindViewHolder(@NonNull UnitEntryAdapter.ViewHolder holder, int position) {
        UnitEntryItem unitEntryItem = items.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.itemView, unitEntryItem);
            }
        });

        holder.existingUnit.setText(unitEntryItem.ExistingUnit);
        holder.newUnit.setText(unitEntryItem.newUnit);
        holder.unitModel.setText(unitEntryItem.Model);


        holder.retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.verifyUnit == null) {
                    holder.verifyUnit = new VerifyUnit(holder,unitEntryItem);
                    holder.verifyUnit.inProgress = false;
                }
                if (!holder.verifyUnit.inProgress) {
                    holder.verifyUnit = new VerifyUnit(holder,unitEntryItem);
                    holder.verifyUnit.inProgress = true;
                    holder.verifyUnit.execute();
                }
            }
        });

        switch (unitEntryItem.getVerificationStatus()) {

            case FAILED:
                holder.retry.setText(Html.fromHtml("<p><u" +
                        ">" + mContext.getResources().getString(R.string.verification_failed) + "</u></p>"));
                holder.retry.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
                        mContext.getDrawable(R.drawable.ic_cancel_hollow_24dp), null,
                        null);
                break;

            case SUCCESS:
                holder.retry.setText(Html.fromHtml("<p" +
                        "><u>" + mContext.getResources().getString(R.string.ok) + "</u></p>"));
                holder.retry.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
                        mContext.getDrawable(R.drawable.ic_done_24dp),
                        null, null);
                break;
            case IN_PROGRESS:
                holder.retry.setText(mContext.getResources().getString(R.string.verifying));
                holder.retry.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
                        mContext.getDrawable(R.drawable.circular_progress_bar), null, null);
                break;

            default:
                holder.retry.setText(Html.fromHtml("<p><u>" + mContext.getResources().getString(R.string.status) + "</u></p>"));
                holder.retry.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
                        mContext.getDrawable(R.drawable.ic_perform_verification)
                        , null, null);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView existingUnit;
        TextView newUnit;
        TextView unitModel;
        TextView retry;
        VerifyUnit verifyUnit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            existingUnit = itemView.findViewById(R.id.existing);
            newUnit = itemView.findViewById(R.id.newUnit);
            unitModel = itemView.findViewById(R.id.model);
            retry = itemView.findViewById(R.id.onlineVerificationBtn);
        }
    }

    private class VerifyUnit extends AsyncTask<Void, Void, UnitInformationTO> {

        ViewHolder mHolder;
        boolean inProgress = false;
        UnitEntryItem unitEntryItem;

        public VerifyUnit(ViewHolder holder,UnitEntryItem unitEntryItem)
        {
            mHolder = holder;
            this.unitEntryItem = unitEntryItem;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inProgress = true;
            unitEntryItem.setVerificationStatus(UnitEntryItem.VerificationStatus.IN_PROGRESS);
            UnitEntryAdapter.this.notifyItemChanged(items.indexOf(unitEntryItem));
        }

        @Override
        protected void onPostExecute(UnitInformationTO unitInformationTO) {

            if (unitInformationTO != null && AndroidUtilsAstSep.CONSTANTS().UNIT_STATUS_T__MOUNTABLE.equals(unitInformationTO.getUnitStatusTTO().getId())) {
                unitEntryItem.setVerificationStatus(UnitEntryItem.VerificationStatus.SUCCESS);
            } else {
                unitEntryItem.setVerificationStatus(UnitEntryItem.VerificationStatus.FAILED);
            }
            inProgress = false;
            notifyItemChanged(items.indexOf(unitEntryItem));
            super.onPostExecute(unitInformationTO);
        }

        @Override
        protected UnitInformationTO doInBackground(Void... voids) {
            UnitInformationTO unitInformationTO = null;
            try {
                WoUnitCustomTO woUnitCustomTO =
                        unitCreation.createUnitFromUnitEntryItem(unitEntryItem);

                unitInformationTO = Utils.isNotEmpty(woUnitCustomTO.getUnitModel()) ?
                        AndroidUtilsAstSep.getDelegate().getDeviceInfoWithModel(woUnitCustomTO.getGiai(),
                                woUnitCustomTO.getIdUnitIdentifierT(), woUnitCustomTO.getUnitModel()) :
                        AndroidUtilsAstSep.getDelegate().getDeviceInfo(woUnitCustomTO.getGiai(),
                                woUnitCustomTO.getIdUnitIdentifierT());
            } catch (Exception e) {
                writeLog("OnlineVerificationPageFragment : doInBackground()", e);
            }
            return unitInformationTO;
        }

    }

}

