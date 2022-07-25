/**
 * 
 */
package com.capgemini.sesp.ast.android.module.util.to;

import com.skvader.rsp.cft.common.to.cft.table.AttachmentReasonTTO;
import com.skvader.rsp.cft.common.to.cft.table.AttachmentTTO;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * @author Capgemini
 * @version 1.0
 * @since 30th December, 2014
 *
 */

public class AndroidWOAttachmentBean {

	private String fileName = null;
	private String fieldVisitId = null;
	private Long caseId = null;
	private Long caseTypeId = null;
	private String reasonTypeId = null;
	private Long attachmentTypeId = null;
	private AttachmentTTO attachmentTTO = null;
	private List<AttachmentReasonTTO> attachmentReasonTTOs = null;
	
	public String getFileName() {return fileName;}
	public void setFileName(String fileName) {this.fileName = fileName;}

	public AttachmentTTO getAttachmentTTO() {
		return attachmentTTO;
	}
	public void setAttachmentTTO(AttachmentTTO attachmentTTO) {
		this.attachmentTTO = attachmentTTO;
	}

	public List<AttachmentReasonTTO> getAttachmentReasonTTO() {
		return attachmentReasonTTOs;
	}
	public void setAttachmentReasonTTO(List<AttachmentReasonTTO> attachmentReasonTTOs) {this.attachmentReasonTTOs = attachmentReasonTTOs;}

	public Long getCaseId() {
		return caseId;
	}
	public void setCaseId(Long caseId) {this.caseId = caseId;}

	public Long getCaseTypeId() {
		return caseTypeId;
	}
	public void setCaseTypeId(Long caseTypeId) {
		this.caseTypeId = caseTypeId;
	}

	public Long getAttachmentTypeId() {
		return attachmentTypeId;
	}
	public void setAttachmentTypeId(Long attachmentTypeId) {this.attachmentTypeId = attachmentTypeId;}

	public String getReasonTypeId() {
		return reasonTypeId;
	}
	public void setReasonTypeId(String reasonTypeId) {
		this.reasonTypeId = reasonTypeId;
	}


	@Override
	public String toString() {
		return "AndroidWOAttachmentBean{" +
				"fileName='" + fileName + '\'' +
				", fieldVisitId='" + fieldVisitId + '\'' +
				", caseId=" + caseId +
				", caseTypeId=" + caseTypeId +
				", reasonTypeId=" + reasonTypeId +
				", attachmentTypeId=" + attachmentTypeId +
				", attachmentTTO=" + attachmentTTO +
				", attachmentReasonTTO=" + attachmentReasonTTOs +
				'}';
	}

	@Override
	public boolean equals(final Object beanObject){
		boolean status = false;
		try{
		if(beanObject!=null && beanObject instanceof AndroidWOAttachmentBean){
			final AndroidWOAttachmentBean bean = (AndroidWOAttachmentBean)beanObject;
			/*
			 *  Match if following are true
			 *  
			 *  attachment path are equal
			 *  attachment reason type are equal
			 *  attachment mime type are equal
			 *  
			 *  work order case and case type ids are identical
			 */
			if((caseId!=null && bean.getCaseId()!=null && bean.getCaseId().equals(caseId))
					&&
				(caseTypeId!=null && bean.getCaseTypeId()!=null && bean.getCaseTypeId().equals(caseTypeId))
					&&
				(fileName!=null && bean.getFileName()!= null && bean.getFileName().equalsIgnoreCase(fileName))
					&&
				(attachmentTTO!=null && bean.getAttachmentTTO()!=null && attachmentTTO.equals(bean.getAttachmentTTO()))
					){
				status  = true;
			}
		}
		} catch (Exception e) {
			writeLog("AndroidWOAttachmentBean : equals()", e);
		}
		return status;
	}

	public String getFieldVisitId() {
		return fieldVisitId;
	}

	public void setFieldVisitId(String fieldVisitId) {
		this.fieldVisitId = fieldVisitId;
	}
}
