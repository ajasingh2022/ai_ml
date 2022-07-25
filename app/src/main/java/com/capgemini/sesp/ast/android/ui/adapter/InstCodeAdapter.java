package com.capgemini.sesp.ast.android.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.ui.activity.order.OrderListActivity;

import java.lang.ref.SoftReference;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class InstCodeAdapter extends BaseAdapter {

    private transient SoftReference<OrderListActivity> contextRef=null;
        static String TAG =InstCodeAdapter.class.getSimpleName();
    public InstCodeAdapter(final OrderListActivity context) {
    	if(context!=null){
    		contextRef = new SoftReference<OrderListActivity>(context);
    	}
    }

    @Override
    public int getCount() {
    	int count = 0;
    	try{
    	if(contextRef!=null && contextRef.get()!=null){
    		final List<WorkorderLiteTO> woList = contextRef.get().getWorkorderList();
    		if(woList!=null){
    			count = woList.size();
    		}
    	}
        } catch (Exception e) {
            writeLog(TAG +"  : getCount() ", e);
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
    	Object object = null;
    	try{
    	if(contextRef!=null && contextRef.get()!=null){
    		final List<WorkorderLiteTO> woList = contextRef.get().getWorkorderList();
    		if(woList!=null && position<woList.size()){
    			object = woList.get(position);
    		}
    	}
        } catch (Exception e) {
            writeLog(TAG +"  : getItem() ", e);
        }
        return object;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
    	View vw = convertView;
    	try{
    	if(contextRef!=null && contextRef.get()!=null){
    		final OrderListActivity context = contextRef.get();
    		
    		if (vw == null) {
    			vw = LayoutInflater.from(context).inflate(
                        R.layout.inst_code_list_row, parent, false);
            }

    		final TextView instCode = vw
                    .findViewById(R.id.inst_code);
    		final ImageView timeReservation = vw
                    .findViewById(R.id.time_reservation);
    		final ImageView started = vw
                    .findViewById(R.id.started);
    		final ImageView assigned = vw
                    .findViewById(R.id.assign);
    		final ImageView key = vw.findViewById(R.id.key);

    		final WorkorderLiteTO wo = context.getWorkorderList().get(position);

            instCode.setText(wo.getInstCode());
            if (wo.isStarted()) {
                started.setVisibility(View.VISIBLE);
            } else {
                started.setVisibility(View.INVISIBLE);
            }

            if (wo.isAssigned()) {
                assigned.setImageResource(R.drawable.ic_wo_assigned_user);
            } else {
                assigned.setImageResource(R.drawable.ic_wo_assigned_team);
            }

            if (wo.getKeyInfo() != null || wo.getKeyNumber() != null) {
                if (wo.getKeyInfo() != null) {
                    key.setImageResource(R.drawable.ic_key);
                } else {
                    key.setImageResource(R.drawable.ic_key_info);
                }
                key.setVisibility(View.VISIBLE);
            } else {
                key.setVisibility(View.INVISIBLE);
            }

            if (wo.getTimeReservationStart() != null) {
                timeReservation.setVisibility(View.VISIBLE);
                
                long timeLeft = wo.getTimeReservationStart().getTime() - System.currentTimeMillis();
    			if(timeLeft < ConstantsAstSep.THRESHOLD_TIME_RESERAVTION_WARNING) {
                    timeReservation.setImageResource(R.drawable.ic_clock_red);
                } else {
                    timeReservation.setImageResource(R.drawable.ic_time_reservation);
                }
            } else {
                timeReservation.setVisibility(View.INVISIBLE);
            }
            vw.setTag(wo);
    	}
        } catch (Exception e) {
            writeLog(TAG +"  : getView() ", e);
        }
        return vw;
    }


}
