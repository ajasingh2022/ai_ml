package com.capgemini.sesp.ast.android.module.util.comparators;

import com.skvader.rsp.cft.common.to.custom.base.NameInterfaceTO;
import com.skvader.rsp.cft.common.to.custom.base.SortOrderTO;

import java.util.Comparator;

/**
 * Created by kampatra on 7/28/2017.
 */
public class TypeDataComparator  implements Comparator {

    @Override
    public int compare(Object object1, Object object2) {
        if (object1 == null || object2 == null)
            return -1;

        //Sort on Sort Order
        if (object1 instanceof SortOrderTO && object2 instanceof SortOrderTO) {
            SortOrderTO obj1 = (SortOrderTO) object1;
            SortOrderTO obj2 = (SortOrderTO) object2;
            if (obj1.getSortOrder() != null && obj2.getSortOrder() != null) {
                return obj1.getSortOrder().compareTo(obj2.getSortOrder());
            }
        }

        //Sort on Name
        if (object1 instanceof NameInterfaceTO && object2 instanceof NameInterfaceTO) {
            NameInterfaceTO obj1 = (NameInterfaceTO) object1;
            NameInterfaceTO obj2 = (NameInterfaceTO) object2;
            if (obj1.getName() != null && obj2.getName() != null) {
                return obj1.getName().toUpperCase().compareTo(obj2.getName().toUpperCase());
            }
        }

        //keep existing order
        return -1;
    }
}
