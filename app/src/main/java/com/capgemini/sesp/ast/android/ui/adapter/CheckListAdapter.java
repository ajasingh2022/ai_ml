

package com.capgemini.sesp.ast.android.ui.adapter;



/**
 * Created by nagravi on 20-06-2019.
 *//*
*/


import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.ui.wo.bean.CheckListBean;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar.SolarCheckListFragment;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class CheckListAdapter  extends RecyclerView.Adapter<CheckListAdapter.CheckViewHolder>{

    private LayoutInflater inflater;
    public static List<CheckListBean> imageCheckListModelArrayList;
    public SolarCheckListFragment solarCheckListFragment;
    private CheckInterface mListener;

    public CheckListAdapter(SolarCheckListFragment solarCheckListFragment, List<CheckListBean> imageCheckListModelArrayList, CheckInterface listener) {
        inflater = LayoutInflater.from(solarCheckListFragment.getActivity());
        this.imageCheckListModelArrayList = imageCheckListModelArrayList;
        this.solarCheckListFragment = solarCheckListFragment;
        this.mListener = listener;
    }

    @Override
    public CheckListAdapter.CheckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        CheckViewHolder holder = new CheckViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CheckListAdapter.CheckViewHolder holder, int position) {
        try {
        holder.checkBox.setText(imageCheckListModelArrayList.get(position).getCheckTitle());
        holder.checkBox.setChecked(imageCheckListModelArrayList.get(position).getSelected());

            if (holder.checkBox.isChecked()) {
                holder.tvCheckComment.setVisibility(View.GONE);
            } else {

                holder.tvCheckComment.setVisibility(View.VISIBLE);
                if(Utils.isNotEmpty(imageCheckListModelArrayList.get(position).getCheckComment()))
                {
                    holder.tvCheckComment.setText(imageCheckListModelArrayList.get(position).getCheckComment());
                }
                else
                    holder.tvCheckComment.setText("");
            }

        //holder.tvCheckComment.setText(imageCheckListModelArrayList.get(position).getCheckComment());
        holder.checkBox.setTag(position);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // update your model (or other business logic) based on isChecked
                imageCheckListModelArrayList.get(position).setSelected(isChecked);
                if(holder.checkBox.isChecked()) {
                    holder.tvCheckComment.setVisibility(View.GONE);

                }
                else {
                    mListener.onCheckBoxClick(false);
                    holder.tvCheckComment.setVisibility(View.VISIBLE);
                }
            }

        });
        holder.tvCheckComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                imageCheckListModelArrayList.get(position).setCheckComment(holder.tvCheckComment.getText().toString());
                mListener.afterCommentTextChange(holder.tvCheckComment.getText().toString(), position);
            }
        });
        }catch (Exception e)
        {
            writeLog("CheckListAdapter : onBindViewHolder()", e);
        }

    }


    @Override
    public int getItemCount() {
        return imageCheckListModelArrayList.size();
    }

    public class CheckViewHolder extends RecyclerView.ViewHolder {
        protected CheckBox checkBox;
        EditText tvCheckComment;

        public CheckViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.solarCheckBox);
            tvCheckComment = (EditText) itemView.findViewById(R.id.checkComment);
        }
    }
    public interface CheckInterface {
        void onCheckBoxClick(boolean isAnyChecked);
        void afterCommentTextChange(String value , int position);

    }

}


