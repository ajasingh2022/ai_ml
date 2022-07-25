package com.capgemini.sesp.ast.android.ui.adapter;

import android.content.Context;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

/**
 * Created by nalwarsa on 3/2/2018.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {


    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private ArrayMap<String, List<String>> _listDataChild;
    @Override
    public int getGroupCount() {
        return 0;
    }

    public ExpandableListAdapter(Context _context, List<String> _listDataHeader, ArrayMap<String, List<String>> _listDataChild) {
        this._context = _context;
        this._listDataHeader = _listDataHeader;
        this._listDataChild = _listDataChild;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
