/**
 * 
 */
package com.capgemini.sesp.ast.android.ui.wo.bean;

import java.util.Date;

/**
 * @author nirmchak
 *
 */
public class WoAuditLogBean /*implements Parcelable*/ {

	private long id;
	private long caseId;
	private long caseTypeId;
	private Boolean status=null;
	private Date startDate = null;
	private Date endDate = null;
	private int synchAttemptNum=1;
	private String errorInfo = null;
	
	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
	public int getSynchAttemptNum() {
		return synchAttemptNum;
	}

	public void setSynchAttemptNum(int synchAttemptNum) {
		this.synchAttemptNum = synchAttemptNum;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCaseId() {
		return caseId;
	}

	public void setCaseId(long caseId) {
		this.caseId = caseId;
	}

	public long getCaseTypeId() {
		return caseTypeId;
	}

	public void setCaseTypeId(long caseTypeId) {
		this.caseTypeId = caseTypeId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
