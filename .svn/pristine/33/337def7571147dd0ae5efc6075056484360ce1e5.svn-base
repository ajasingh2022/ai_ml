package com.capgemini.sesp.ast.android.ui.activity.material_logistics.register_new_broken_device;

import com.capgemini.sesp.ast.android.ui.layout.SespListItem;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitStatusReasonTTO;


public class BrokenNewDeviceItem implements SespListItem {
	private String identifier;
	private UnitStatusReasonTTO unitStatusReasonTTO;
	private Long idUnitIdentifierT;
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

	public BrokenNewDeviceItem(String identifier, UnitStatusReasonTTO unitStatusReasonTTO, Long idUnitIdentifierT, Long idUnitT, Long idUnitModelT) {
		this.identifier = identifier;
		this.unitStatusReasonTTO = unitStatusReasonTTO;
		this.idUnitIdentifierT = idUnitIdentifierT;
		this.idUnitT = idUnitT;
		this.idUnitModelT = idUnitModelT;
	}

	public BrokenNewDeviceItem(String identifier, UnitStatusReasonTTO unitStatusReasonTTO, Long idUnitIdentifierT) {
		this.identifier = identifier;
		this.unitStatusReasonTTO = unitStatusReasonTTO;
		this.idUnitIdentifierT = idUnitIdentifierT;
	}

	public String getIdentifier() {
		return identifier;
	}

	public UnitStatusReasonTTO getUnitStatusReasonTTO() {
		return unitStatusReasonTTO;
	}

	public Long getIdUnitIdentifierT() {
		return idUnitIdentifierT;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BrokenNewDeviceItem){
			BrokenNewDeviceItem that = (BrokenNewDeviceItem) obj;
			return (this.identifier.equals(that.identifier) &&
					this.idUnitIdentifierT.equals(that.idUnitIdentifierT));
		} else {
			return false;
		}
	}

	@Override
	public int hashCode(){
		return identifier.hashCode() + unitStatusReasonTTO.hashCode();
	}

}
