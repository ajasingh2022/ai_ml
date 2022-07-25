package com.capgemini.sesp.ast.android.ui.activity.material_logistics.common;

import com.capgemini.sesp.ast.android.ui.layout.SespListItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.skvader.rsp.cft.common.to.custom.base.InfoInterfaceTO;
import com.skvader.rsp.cft.common.to.custom.base.NameInterfaceTO;

public class UnitItem<T extends NameInterfaceTO & InfoInterfaceTO> implements SespListItem {

	public enum Type {
		BULK,
		NORMAL,
		SEQUENCE
	}

	@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
	private T obj;
	private long amount;
	private Long idUnitIdentifierT;
	private String id;
	private Long idUnitT;
	private Long idUnitModelT;

	public Long getIdUnitT() {
		return idUnitT;
	}

	public void setIdUnitT(Long idUnitT) {
		this.idUnitT = idUnitT;
	}

	public Long getIdUnitModelT() {
		return idUnitModelT;
	}

	public void setIdUnitModelT(Long idUnitModelT) {
		this.idUnitModelT = idUnitModelT;
	}




	private String idFrom;
	private Boolean overwriteExisting;


	private String idTo;
	private boolean saved = false;
	private UnitItem.Type type;

	//Default dummy constructor
	public UnitItem(){}

	public UnitItem(T obj, int amount, Boolean overwriteExisting) {
		this.obj = obj;
		this.amount = amount;
		this.overwriteExisting = overwriteExisting;
		this.type = Type.BULK;
	}
	public UnitItem(String id, Long idUnitType, Long idUnitModel, Long idUnitIdentifierT) {
		this.id = id;
		this.amount = 1;
		this.idUnitIdentifierT=idUnitIdentifierT;
		this.type = Type.NORMAL;
		this.idUnitT = idUnitType;
		this.idUnitModelT = idUnitModel;
	}


	public UnitItem(Long id, T obj, int amount, Boolean overwriteExisting) {
		this.id = id.toString();
		this.obj = obj;
		this.amount = amount;
		this.overwriteExisting = overwriteExisting;
		this.type = Type.BULK;
	}

	public UnitItem(String idFrom, String idTo, long amount, Long idUnitIdentifierT) {
		this.idFrom = idFrom;
		this.idTo = idTo;
		this.amount = amount;
		this.idUnitIdentifierT=idUnitIdentifierT;
		this.type = Type.SEQUENCE;
	}

	public UnitItem(String id, Long idUnitIdentifierT) {
		this.id = id;
		this.amount = 1;
		this.idUnitIdentifierT=idUnitIdentifierT;
		this.type = Type.NORMAL;
	}

	public T getObj() {
		return this.obj;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public boolean isSaved() {
		return this.saved;
	}

	public Long getIdUnitIdentifierT(){
		return idUnitIdentifierT;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public long getAmount() {
		return amount;
	}

	public String getId() {
		return id;
	}

	@JsonIgnore
	public String getTitle() {
		switch(type) {
			case SEQUENCE:
				return idFrom + " - " + idTo;
			case NORMAL:
				return id;
			case BULK:
				return obj.getName();
			default:
				return "";
		}
	}

	public String getIdFrom() {
		return idFrom;
	}

	public String getIdTo() {
		return idTo;
	}

	public boolean equals(Object obj) {
		if (obj instanceof UnitItem) {
			UnitItem that = (UnitItem) obj;
			if(that.type == this.type) {
				switch(type) {
					case SEQUENCE:
						return this.idFrom.equals(that.idFrom) && this.idTo.equals(that.idTo) && this.idUnitIdentifierT.equals(that.idUnitIdentifierT);
					case NORMAL:
						return this.id.equals(that.id) && this.idUnitIdentifierT.equals(that.idUnitIdentifierT);
					case BULK:
						return this.obj.getName().equals(that.obj.getName()) && this.getAmount() == that.getAmount();
				}
			}
		}
		return false;
	}

	public int hashCode() {
		switch(type) {
			case SEQUENCE:
				return idFrom.hashCode();
			case NORMAL:
				return id.hashCode();
			case BULK:
			default:
				return super.hashCode();
		}
	}

	public Type getType() {
		return type;
	}

	public Boolean isOverwriteExisting() {
		return overwriteExisting;
	}

	public void setOverwriteExisting(Boolean overwriteExisting) {
		this.overwriteExisting = overwriteExisting;
	}

}
