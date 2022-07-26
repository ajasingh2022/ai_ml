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


import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class SteppersViewHolder extends RecyclerView.ViewHolder {

    private boolean isChecked;

    protected View itemView;
    protected RoundedView roundedView;
    protected TextView textViewLabel;
    protected TextView textViewSubLabel;
    protected RelativeLayout linearLayoutContent;
    protected RelativeLayout transperentLayout;
    protected FrameLayout frameLayout;
    protected LinearLayout frameLayoutsContainer;
    protected Button buttonContinue;
    protected Button buttonSkip;
    protected Button buttonCancel;
    protected Fragment fragment;

    public SteppersViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.roundedView = (RoundedView) itemView.findViewById(R.id.roundedView);
        this.textViewLabel = (TextView) itemView.findViewById(R.id.textViewLabel);
        this.textViewSubLabel = (TextView) itemView.findViewById(R.id.textViewSubLabel);
        this.linearLayoutContent = (RelativeLayout) itemView.findViewById(R.id.linearLayoutContent);
        this.transperentLayout = (RelativeLayout) itemView.findViewById(R.id.transperentLayout);
        this.frameLayout = (FrameLayout) itemView.findViewById(R.id.frameLayout);
        this.buttonContinue = (Button) itemView.findViewById(R.id.buttonContinue);
        this.buttonSkip = (Button) itemView.findViewById(R.id.buttonSkip);
        this.buttonCancel = (Button) itemView.findViewById(R.id.buttonCancel);
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    /**
     * @return true if step is done, false if not
     */
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
