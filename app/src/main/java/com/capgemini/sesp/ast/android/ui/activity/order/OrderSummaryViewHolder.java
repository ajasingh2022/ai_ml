package com.capgemini.sesp.ast.android.ui.activity.order;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.ui.adapter.ParentViewHolder;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


public class OrderSummaryViewHolder extends ParentViewHolder {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;

    private final ImageView mArrowExpandImageView;
    private ImageView timeReservation;
    private ImageView started;
    private ImageView assigned;
    private ImageView key;
    private TextView mMovieTextView;
    private TextView priorityType;

    public OrderSummaryViewHolder(View itemView) {
        super(itemView);


        mMovieTextView = itemView.findViewById(R.id.inst_code);
        timeReservation = itemView.findViewById(R.id.time_reservation);
        started = itemView.findViewById(R.id.started);
        assigned = itemView.findViewById(R.id.assign);
        key = itemView.findViewById(R.id.key);
        mArrowExpandImageView = itemView.findViewById(R.id.iv_arrow_expand);
        priorityType =  itemView.findViewById(R.id.priority_type);
    }

    public void bind(OrderSummary orderCategory) {
        try {
            mMovieTextView.setText(orderCategory.getName().trim());
            if ("user".equals(orderCategory.getAssigned()))
                assigned.setImageResource(R.drawable.ic_wo_assigned_user);
            else
                assigned.setImageResource(R.drawable.ic_wo_assigned_team);
            if ("visible".equals(orderCategory.getStarted()))
                started.setVisibility(View.VISIBLE);
            else
                started.setVisibility(View.INVISIBLE);
            if ("visible".equals(orderCategory.getKey()))
                key.setVisibility(View.VISIBLE);
            else
                key.setVisibility(View.INVISIBLE);
            if ("visible ic_clock_red".equals(orderCategory.getTimeReservation())) {
                timeReservation.setImageResource(R.drawable.ic_clock_red);
                timeReservation.setVisibility(View.VISIBLE);
            } else if ("visible ic_time_reservation".equals(orderCategory.getTimeReservation())) {
                timeReservation.setImageResource(R.drawable.ic_time_reservation);
                timeReservation.setVisibility(View.VISIBLE);
            } else
                timeReservation.setVisibility(View.INVISIBLE);
            priorityType.setText(orderCategory.getPriority());

            if (orderCategory.getPriority().toString() == "H"){
                priorityType.setTextColor(priorityType.getResources().getColor(R.color.colorRed));
            }
           else if (orderCategory.getPriority().toString() == "M"){
                priorityType.setTextColor(priorityType.getResources().getColor(R.color.colorSaffron));
            }
            else  if (orderCategory.getPriority().toString() == "L"){
                priorityType.setTextColor(priorityType.getResources().getColor(R.color.colorAccentDark));
            }
        } catch (Exception e) {
            writeLog("OrderSummaryViewHolder  : bind() ", e);
        }
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);

        if (expanded) {
            mArrowExpandImageView.setRotation(ROTATED_POSITION);
        } else {
            mArrowExpandImageView.setRotation(INITIAL_POSITION);
        }

    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        try {
            RotateAnimation rotateAnimation;
            if (expanded) { // rotate clockwise
                rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else { // rotate counterclockwise
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
            mArrowExpandImageView.startAnimation(rotateAnimation);
        } catch (Exception e) {
            writeLog("OrderSummaryViewHolder  : onExpansionToggled() ", e);
        }
    }
}
