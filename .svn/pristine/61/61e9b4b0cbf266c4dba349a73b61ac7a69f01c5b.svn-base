/**
 * 
 */
package com.capgemini.sesp.ast.android.ui.activity.order;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.capgemini.sesp.ast.android.R;

import java.lang.ref.SoftReference;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * @author nirmchak
 *
 */
public class OrderListClickListener implements OnClickListener {
	
	private transient SoftReference<OrderListActivity> contextRef = null;
	
	public OrderListClickListener(final OrderListActivity activity){
		if(activity!=null){
			contextRef = new SoftReference<OrderListActivity>(activity);
		}		
	}

    @SuppressLint("LongLogTag")
	@Override
    public void onClick(final View view) {
		try{
    	if(contextRef!=null && contextRef.get()!=null){
    		final OrderListActivity activity = contextRef.get();

    		
    		final LinearLayout timeReservedOrdersLayout = activity.findViewById(R.id.order_plan_layout);
    		final LinearLayout filterLayout = activity.findViewById(R.id.filter_layout);
    		
			int id = view.getId();
             if (id == R.id.cancel) {
            	activity.slideDown(filterLayout, true);

            } else if (id == R.id.cancel_orders) {
            	activity.slideDown(timeReservedOrdersLayout, true);

            } else if (id == R.id.swap) {
            	activity.slideDown(filterLayout, false);
            	activity.slideUP(timeReservedOrdersLayout);

            } else if (id == R.id.swap_orders) {
            	activity.slideDown(timeReservedOrdersLayout, false);
            	activity.slideUP(filterLayout);
            }
    	}
		} catch (Exception e) {
			writeLog("OrderListClickListener  : onClick()" ,e);
		}
    }

}