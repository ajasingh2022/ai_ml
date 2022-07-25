package com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.CustomFragment;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar.adapter.VerifyUnitAdater;

public class VerifyExistingSolarPanels extends CustomFragment {
    RecyclerView rv;
    TextView counterIndicator;
    int existingPos = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.flow_fragment_verify_existing_solar_panel, null);

        {
            initializePageValues();
            initialize();
            populateData();
            setupListeners();
        }
        return fragmentView;
    }

    private void setupListeners() {
    }

    private void populateData() {

        updatePagerCount(existingPos);


    }

    private void updatePagerCount(int position) {

        existingPos = position;
        String indicateText = "/5";
        counterIndicator.setText(String.valueOf(position+1).concat(indicateText));

        if (position != 0 && position != 4) {
            counterIndicator.setCompoundDrawablesWithIntrinsicBounds(getActivity().getDrawable(R.drawable.ic_left_arrow_24dp1), null, getActivity().getDrawable(R.drawable.ic_right_arrow_24dp1), null);
            return;
        }
        else if(position == 0) {
            counterIndicator.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getActivity().getDrawable(R.drawable.ic_right_arrow_24dp1), null);
        }
        else if (position == 4){
            counterIndicator.setCompoundDrawablesWithIntrinsicBounds(getActivity().getDrawable(R.drawable.ic_left_arrow_24dp1), null, null, null);
        }

    }

    private void initialize() {

        rv = fragmentView.findViewById(R.id.rvExistingUnits);
        rv.setAdapter(new VerifyUnitAdater(this.getContext()));
        rv.setLayoutManager(new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.HORIZONTAL,false));

        counterIndicator = fragmentView.findViewById(R.id.counter);

        SnapHelper snapHelper = new PagerSnapHelper() ;
        snapHelper.attachToRecyclerView(rv);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    View snap = snapHelper.findSnapView(rv.getLayoutManager());
                    if(!(rv.getLayoutManager().getPosition(snap) == existingPos)){
                        updatePagerCount(rv.getLayoutManager().getPosition(snap));
                    }

                }
            });
        }
        else {
            rv.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    View snap = snapHelper.findSnapView(rv.getLayoutManager());
                    if(!(rv.getLayoutManager().getPosition(snap) == existingPos)){
                        updatePagerCount(rv.getLayoutManager().getPosition(snap));
                    }
                }
            });
        }

    }

    public VerifyExistingSolarPanels(String stepIdentifier) {
        super(stepIdentifier);
    }
}
