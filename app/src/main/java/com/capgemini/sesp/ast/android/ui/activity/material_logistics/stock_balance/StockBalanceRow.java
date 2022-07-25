package com.capgemini.sesp.ast.android.ui.activity.material_logistics.stock_balance;

import com.capgemini.sesp.ast.android.ui.layout.SespListItem;

public class StockBalanceRow implements SespListItem {
	
	private String name;
	private String status;
	private Long amount;
	
	public String getName() {
	    return name;
    }
	public void setName(String name) {
	    this.name = name;
    }
	public String getStatus() {
	    return status;
    }
	public void setStatus(String status) {
	    this.status = status;
    }
	public Long getAmount() {
	    return amount;
    }
	public void setAmount(Long amount) {
	    this.amount = amount;
    }

}
