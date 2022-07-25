package com.capgemini.sesp.ast.android.module.util.to;

import java.util.Date;

public class DataTO {

	private Long id;
	private Long idEntity;
	private Date iuTimestamp;
	private String toClass;
	private String data;
	
	public Long getId() {
	    return id;
    }
	public void setId(Long id) {
	    this.id = id;
    }
	public Long getIdEntity() {
	    return idEntity;
    }
	public void setIdEntity(Long idEntity) {
	    this.idEntity = idEntity;
    }
	public Date getIuTimestamp() {
	    return iuTimestamp;
    }
	public void setIuTimestamp(Date iuTimestamp) {
	    this.iuTimestamp = iuTimestamp;
    }
	public String getToClass() {
	    return toClass;
    }
	public void setToClass(String toClass) {
	    this.toClass = toClass;
    }
	public String getData() {
	    return data;
    }
	public void setData(String data) {
	    this.data = data;
    }
}
