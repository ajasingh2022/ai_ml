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

import android.content.Context;

import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class SteppersAdapter extends RecyclerView.Adapter<SteppersViewHolder> {
    private static final int VIEW_COLLAPSED = 0;
    private static final int VIEW_EXPANDED = 1;

    private static final String TAG = "SteppersAdapter";
    private SteppersView steppersView;
    private Context context;
    private SteppersView.Config config;
    private List<SteppersItem> items;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private Map<Integer, Integer> frameLayoutIds = new ArrayMap<>();

    private int beforeStep = -1;
    private int currentStep = 0;
    public int maxReachedStep = 0;

    public SteppersAdapter(SteppersView steppersView, SteppersView.Config config, List<SteppersItem> items) {
        this.steppersView = steppersView;
        this.context = steppersView.getContext();
        this.config = config;
        this.items = items;
        this.fragmentManager = config.getFragmentManager();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == currentStep ? VIEW_EXPANDED : VIEW_COLLAPSED);
    }

    @Override
    public SteppersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if (viewType == VIEW_COLLAPSED) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.steppers_item, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.steppers_item_expanded, parent, false);
        }

        SteppersViewHolder vh = new SteppersViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final SteppersViewHolder holder, int p) {


        final int position = holder.getAdapterPosition();
        final SteppersItem steppersItem = items.get(position);

        //
        holder.transperentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do nothing
            }
        });
        holder.transperentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        if (currentStep  >= maxReachedStep  )
        {
            holder.transperentLayout.setVisibility(View.GONE);
            holder.buttonCancel.setVisibility(View.GONE);
            holder.buttonContinue.setEnabled(true);
            holder.buttonContinue.setVisibility(View.VISIBLE);
        }

        else {
            holder.transperentLayout.setVisibility(View.VISIBLE);
            holder.buttonCancel.setVisibility(View.VISIBLE);
            holder.buttonContinue.setEnabled(false);
            holder.buttonContinue.setVisibility(View.GONE);
        }

        holder.setChecked(position < currentStep);
        if(holder.isChecked()) {
            holder.roundedView.setChecked(true);
        } else {
            holder.roundedView.setChecked(false);
            holder.roundedView.setText(position + 1 + "");
            if (steppersItem.isDone()) {
                holder.roundedView.setChecked(true,R.color.stepHeading);
            }
        }

        holder.textViewLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (steppersItem.isDone())
                {
                    changeToStep(holder.getAdapterPosition());
                    if (holder.linearLayoutContent.getVisibility() == View.VISIBLE)
                        holder.linearLayoutContent.setVisibility(View.GONE);
                    else
                        holder.linearLayoutContent.setVisibility(View.VISIBLE);
                }
            }
        });


        if((position == currentStep || holder.isChecked()))
            holder.roundedView.setCircleAccentColor();
        else
            holder.roundedView.setCircleGrayColor();

        holder.textViewLabel.setText(steppersItem.getLabel());
        holder.textViewSubLabel.setText(steppersItem.getSubLabel());
        holder.linearLayoutContent.setVisibility(position == currentStep || position == beforeStep ? View.VISIBLE : View.GONE);
        holder.buttonContinue.setEnabled(steppersItem.isPositiveButtonEnable());
        steppersItem.addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                if(observable != null) {
                    SteppersItem item = (SteppersItem) observable;
                    holder.buttonContinue.setEnabled(item.isPositiveButtonEnable());
                }
            }
        });

        if (position == getItemCount() - 1) holder.buttonContinue.setText(context.getResources().getString(R.string.step_finish));
        else holder.buttonContinue.setText(context.getResources().getString(R.string.step_continue));

        holder.buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                if(position == getItemCount() - 1)
                    config.getOnFinishAction().onFinish();
                else {
                    if(steppersItem.getOnClickContinue() != null) {
                        steppersItem.getOnClickContinue().onClick();
                    } else {
                        nextStep();
                    }
                }
                v.setEnabled(true);
            }
        });

        if (steppersItem.isSkippable() && position < getItemCount() - 1) {
            holder.buttonSkip.setVisibility(View.VISIBLE);
            holder.buttonSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (steppersItem.getOnSkipStepAction() != null) {
                        steppersItem.getOnSkipStepAction().onSkipStep();
                    }
                    nextStep();
                }
            });
        } else {
            holder.buttonSkip.setVisibility(View.GONE);
        }

        if(config.isCancelAvailable()) {
            if (config.getOnCancelAction() != null)
                holder.buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        config.getOnCancelAction().onCancel();
                    }
                });
        } else {
            holder.buttonCancel.setVisibility(View.GONE);
        }

        if(beforeStep == position) {
            AnimationUtils.hide(holder.linearLayoutContent);
        }

        Fragment fragment = items.get(position).getFragment();
        fragmentTransaction = config.getFragmentManager().beginTransaction();

        if (config.getFragmentManager().findFragmentByTag(items.get(position).getSteppersItemIdentifier()) != null)
        {
            fragment = config.getFragmentManager().findFragmentByTag(items.get(position).getSteppersItemIdentifier());
            if (position == currentStep) {
                fragmentTransaction.attach(fragment);
                fragmentTransaction.show(fragment);
                commitFragmentTransactions();
                addFragmentView(fragment,holder,position);
            }

        }

        else {
            if (position == currentStep) {
                fragment = items.get(position).getFragment();
                fragmentTransaction.add(steppersView.getId(), fragment, items.get(position).getSteppersItemIdentifier());
                fragmentTransaction.attach(fragment);
                fragmentTransaction.show(fragment);
                commitFragmentTransactions();
                addFragmentView(fragment,holder,position);

            }
        }

        if(currentStep == position && !steppersItem.isDisplayed()) {
            steppersItem.setDisplayed(true);
        }
    }

    public void commitFragmentTransactions() {
        if (fragmentTransaction != null) {
            fragmentTransaction.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
    }

    private void addFragmentView(Fragment fragment,SteppersViewHolder holder,int position) {
        View fragmentview = config.getFragmentManager().findFragmentByTag(items.get(position).getSteppersItemIdentifier()).getView();
        if (fragmentview.getParent() != null) {
            ((ViewGroup) fragmentview.getParent()).removeView(fragmentview);
        }
        AnimationUtils.show(holder.linearLayoutContent);
        holder.frameLayout.removeAllViews();
        holder.frameLayout.removeAllViewsInLayout();
        holder.frameLayout.addView(fragmentview);
        ViewGroup.LayoutParams layoutParams = holder.frameLayout.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        holder.frameLayout.setLayoutParams(layoutParams);
    }

    protected void nextStep() {
        changeToStep(currentStep + 1);
    }

    public void changeToStep(int position) {
        if(position != currentStep) {
            this.beforeStep = currentStep;
            this.currentStep = position;
            if (position > maxReachedStep)
                maxReachedStep = position;
            if(beforeStep < currentStep)
                notifyItemRangeChanged(beforeStep, currentStep);
            else
                notifyItemRangeChanged(currentStep, beforeStep);

            if(config.getOnChangeStepAction() != null) {
                SteppersItem steppersItem = items.get(this.currentStep);
                config.getOnChangeStepAction().onChangeStep(this.currentStep, steppersItem);
            }
        } else {
            if(BuildConfig.DEBUG) Log.i(TAG, "This step is currently active");
        }
    }

    protected void setItems(List<SteppersItem> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private int fID = 87352142;

    public int findUnusedId(View view) {
        while( view.findViewById(++fID) != null );
        return fID;
    }

    private static String frameLayoutName() {
        return "android:steppers:framelayout";
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:steppers:" + viewId + ":" + id;
    }
}
