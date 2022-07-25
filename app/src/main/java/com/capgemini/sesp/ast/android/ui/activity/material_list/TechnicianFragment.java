package com.capgemini.sesp.ast.android.ui.activity.material_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.capgemini.sesp.ast.android.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 7/27/2018.
 */

public class TechnicianFragment extends Fragment {
    TabLayout tabs;
    ViewPager pager;
    private int TECHNICIAN=1;
    public TechnicianFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.activity_material_list_inner_tab,container,false);
        try{
        tabs= view.findViewById(R.id.typeTabs);
        pager= view.findViewById(R.id.typeViewPager);
        TechnicianPagerAdapter adapter = new TechnicianPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        } catch (Exception e) {
            writeLog( " TechnicianFragment : onCreateView() ", e);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public class TechnicianPagerAdapter extends FragmentPagerAdapter
    {
        public TechnicianPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch(position)
            {
                case 0: fragment=new UnitFragment(TECHNICIAN); break;
                case 1: fragment=new KeyFragment(TECHNICIAN);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return getString(R.string.title_units).toUpperCase(Locale.getDefault());
                case 1:
                    return getString(R.string.title_key).toUpperCase(Locale.getDefault());

            }
            return null;
        }
    }

}
