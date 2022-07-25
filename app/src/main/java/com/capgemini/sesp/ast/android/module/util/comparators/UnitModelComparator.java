package com.capgemini.sesp.ast.android.module.util.comparators;

import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;

import java.util.Comparator;

/**
 * Created by samdasgu on 12/6/2016.
 */
public class UnitModelComparator implements Comparator<UnitModelCustomTO> {

    public static final int ASCENDING = 1;
    public static final int DESCENDING = 2;
    private int sortOrder;

    public UnitModelComparator(int sortOrder) {
        this.sortOrder = sortOrder;
    }


    @Override
    public int compare(UnitModelCustomTO lhs, UnitModelCustomTO rhs) {
        if(sortOrder == ASCENDING) return lhs.getName().compareTo(rhs.getName());
        else if(sortOrder == DESCENDING) return rhs.getName().compareTo(lhs.getName());
        return 0;
    }
}
