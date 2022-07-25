package com.capgemini.sesp.ast.android.ui.activity.material_logistics.stock_balance;

import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.StockBalanceKeys;

import java.util.Comparator;

public class StockBalanceSort implements Comparator<StockBalanceRow>{
	
	private int desc = 1;
	private StockBalanceKeys sortOnAttribute = StockBalanceKeys.NAME;
	
	public StockBalanceSort(boolean isDesc, StockBalanceKeys sortOnAttribute) {
		desc = (isDesc) ? -1 : 1;
		this.sortOnAttribute = sortOnAttribute;
	}

	@Override
    public int compare(StockBalanceRow lhs, StockBalanceRow rhs) {
		switch(sortOnAttribute){
		case NAME:
			return desc * lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
		case STATUS:
			return desc * lhs.getStatus().toLowerCase().compareTo(rhs.getStatus().toLowerCase());
		case AMOUNT:
			return desc * lhs.getAmount().compareTo(rhs.getAmount());
		}
	    return 0;
    }

}
