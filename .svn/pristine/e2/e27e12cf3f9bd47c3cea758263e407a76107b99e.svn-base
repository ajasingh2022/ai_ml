/*
 * Copyright (C) 2015 Krystian Drożdżyński
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.drozdzynski.library.steppers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.drozdzynski.library.steppers.interfaces.OnCancelAction;
import me.drozdzynski.library.steppers.interfaces.OnChangeStepAction;
import me.drozdzynski.library.steppers.interfaces.OnFinishAction;
import me.drozdzynski.library.steppers.interfaces.OnVisitBackAction;

public class SteppersView extends LinearLayout {


    private NotifyActivityListener notifyActivityListener; // implemented in the Activity



    public interface NotifyActivityListener {
        public void onClickItem(int position); // not shown in impl above
        public void onLongClickItem(int position);
    }


    public SteppersAdapter getSteppersAdapter() {
        return steppersAdapter;
    }

    public void setSteppersAdapter(SteppersAdapter steppersAdapter) {
        this.steppersAdapter = steppersAdapter;
    }

    private SteppersAdapter steppersAdapter;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    RecyclerView recyclerView;



    Context context;
    Activity activity;

    private Config config;
    private List<SteppersItem> items;

    public SteppersView(Context context) {
        super(context);
        this.context=context;
    }

    public SteppersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SteppersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SteppersView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context=context;
    }

    public SteppersView setConfig(Config config) {
        this.config = config;
        return this;
    }

    public SteppersView setItems(List<SteppersItem> items) {
        this.items = items;
        return this;
    }

    public void setActiveItem(int position) {
        steppersAdapter.changeToStep(position);
    }

    public void nextStep() {
        steppersAdapter.nextStep();
    }

    public void build() {
        if(config != null) {
            setOrientation(LinearLayout.HORIZONTAL);

            activity = (Activity)context;
            notifyActivityListener = (NotifyActivityListener)activity;

            recyclerView = new RecyclerView(getContext());
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            recyclerView.setLayoutParams(layoutParams);
            addView(recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            steppersAdapter = new SteppersAdapter(this, config, items);
            recyclerView.setAdapter(steppersAdapter);

            recyclerView.addOnItemTouchListener(new StepperItemTouchListener(context,recyclerView, new StepperItemTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    notifyActivityListener.onClickItem(position);
                        Log.d(this.getClass().getSimpleName(),"Stepper Item Clicked");

                }

                @Override
                public void onLongClick(View view, int position) {
                    notifyActivityListener.onLongClickItem(position);
                    Log.d(this.getClass().getSimpleName(),"Stepper Item Long Clicked");

                }
            }));


        } else {
            throw new RuntimeException("SteppersView need config, read documentation to get more info");
        }
    }

    private boolean possitiveButtonEnable = true;

    public static class Config {


        private OnVisitBackAction onVisitBackAction;
        private OnFinishAction onFinishAction;
        private OnCancelAction onCancelAction;
        private OnChangeStepAction onChangeStepAction;
        private FragmentManager fragmentManager;
        private boolean cancelAvailable = true;

        public Config() {

        }

        public OnVisitBackAction getOnVisitBackAction() {
            return onVisitBackAction;
        }

        public Config setOnVisitBackAction(OnVisitBackAction onVisitBackAction) {
            this.onVisitBackAction = onVisitBackAction;
            return this;
        }

        public Config setOnFinishAction(OnFinishAction onFinishAction) {
            this.onFinishAction = onFinishAction;
            return this;
        }

        public OnFinishAction getOnFinishAction() {
            return onFinishAction;
        }

        public Config setOnCancelAction(OnCancelAction onCancelAction) {
            this.onCancelAction = onCancelAction;
            return this;
        }

        public OnCancelAction getOnCancelAction() {
            return onCancelAction;
        }

        public void setOnChangeStepAction(OnChangeStepAction onChangeStepAction) {
            this.onChangeStepAction = onChangeStepAction;
        }

        public OnChangeStepAction getOnChangeStepAction() {
            return onChangeStepAction;
        }

        public void setFragmentManager(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
        }

        public FragmentManager getFragmentManager() {
            return fragmentManager;
        }

        public void setCancelAvailable(boolean cancelAvailable) {
            this.cancelAvailable = cancelAvailable;
        }

        public boolean isCancelAvailable() {
            return cancelAvailable;
        }
    }

    static int fID = 190980;
    protected static int findUnusedId(View view) {
        while( view.getRootView().findViewById(++fID) != null );
        return fID;
    }

}
