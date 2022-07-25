package com.capgemini.sesp.ast.android.module.others;

import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.OrderListKeys;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoUnitTO;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

/**
 * This is a light weight presentation of the WorkorderCustomWrapperTO, used to save memory.
 * 
 * @author avallda
 */
@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkorderLiteTO implements Serializable {
	
	private Long id;
	private Long idCaseT;
	private String instCode;
	private String keyInfo;
	private String keyNumber;
	private String addressStreet;
	private String addressCity;
	private String customerName;
	private Date slaDeadline;
	private Date timeReservationStart;
	private Date timeReservationEnd;
	private boolean started;
	private boolean assigned;
	private Long antIdUnitModel;
	private Long idUnitModelCfgT;
	private Long idPlanningPeriod;
	private WoUnitTO meterWoUnitTO;
	private String netStationId;
	public Long getIdPlanningPeriod() {
		return idPlanningPeriod;
	}
	private String priority;
	private String priorityShortName;

	public void setIdPlanningPeriod(Long idPlanningPeriod) {
		this.idPlanningPeriod = idPlanningPeriod;
	}

	public String getNetStationId() {
		return netStationId;
	}

	public void setNetStationId(String netStId) {
		this.netStationId = netStId;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getIdCaseT() {
		return idCaseT;
	}

	public void setIdCaseT(Long idCaseT) {
		this.idCaseT = idCaseT;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getKeyInfo() {
		return keyInfo;
	}

	public void setKeyInfo(String keyInfo) {
		this.keyInfo = keyInfo;
	}

	public String getKeyNumber() {
		return keyNumber;
	}

	public void setKeyNumber(String keyNumber) {
		this.keyNumber = keyNumber;
	}

	public String getAddressStreet() {
		return addressStreet;
	}

	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}
	
	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getSlaDeadline() {
		return slaDeadline;
	}

	public void setSlaDeadline(Date slaDeadline) {
		this.slaDeadline = slaDeadline;
	}

	public Date getTimeReservationStart() {
		return timeReservationStart;
	}

	public void setTimeReservationStart(Date timeReservationStart) {
		this.timeReservationStart = timeReservationStart;
	}

	public Date getTimeReservationEnd() {
		return timeReservationEnd;
	}

	public void setTimeReservationEnd(Date timeReservationEnd) {
		this.timeReservationEnd = timeReservationEnd;
	}
	
	public boolean isTimeReservationCloseInTime() {
		if(timeReservationStart != null) {
			long timeLeft = timeReservationStart.getTime() - System.currentTimeMillis();
			return (timeLeft < ConstantsAstSep.THRESHOLD_TIME_RESERAVTION_WARNING);
		}
		return false;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}

	public Long getAntIdUnitModel() {
		return antIdUnitModel;
	}

	public void setAntIdUnitModel(Long antIdUnitModel) {
		this.antIdUnitModel = antIdUnitModel;
	}

	public Long getIdUnitModelCfgT() {
		return idUnitModelCfgT;
	}

	public void setIdUnitModelCfgT(Long idUnitModelCfgT) {
		this.idUnitModelCfgT = idUnitModelCfgT;
	}
	
	public WoUnitTO getMeterWoUnitTO() {
		return meterWoUnitTO;
	}

	public void setMeterWoUnitTO(WoUnitTO meterWoUnitTO) {
		this.meterWoUnitTO = meterWoUnitTO;
	}
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getPriorityShortName() {
		return priorityShortName;
	}

	public void setPriorityShortName(String priorityShortName) {
		this.priorityShortName = priorityShortName;
	}
	public static class SortByInstCode implements Comparator<WorkorderLiteTO> {
		private int mod = 1;

		OrderListKeys sortOnAttribute = OrderListKeys.INST_CODE;

		public SortByInstCode(boolean isDesc, OrderListKeys sortOnAttribute) {
			this.sortOnAttribute = sortOnAttribute;
			if (isDesc)
				mod = -1;
		}

		@Override
		public int compare(WorkorderLiteTO obj1, WorkorderLiteTO obj2) {
			switch(sortOnAttribute){
			case INST_CODE:
				if(obj1.instCode== null && obj2.instCode==null)
					return mod * 0;
				else if(obj1.instCode == null)
					return mod * -1;
				else if(obj2.instCode == null)
					return mod * 1;
				else				
					return mod * obj1.instCode.compareTo(obj2.instCode);
			case METER_SRNO:
				try {
					obj1.meterWoUnitTO.getSerialNumber();
				}catch (NullPointerException e)
				{
					try {
									obj2.meterWoUnitTO.getSerialNumber();
						}catch (NullPointerException e1)
						{
							return mod*0;
						}
						return mod*1;
				}
				try {
						obj2.meterWoUnitTO.getSerialNumber();
					}catch (NullPointerException e)
					{
						return mod*-1;
					}
				return mod * obj1.meterWoUnitTO.getSerialNumber().compareTo(obj2.meterWoUnitTO.getSerialNumber());
			case METER_MODEL:
				try {
						obj1.meterWoUnitTO.getUnitModel();
					}catch (NullPointerException e)
					{
						try {
								obj2.meterWoUnitTO.getUnitModel();
							}catch (NullPointerException e1)
							{
								return mod*0;
							}
							return mod*1;
					}
					try {
							obj2.meterWoUnitTO.getUnitModel();
						}catch (NullPointerException e)
						{
							return mod*-1;
						}
					return mod * obj1.meterWoUnitTO.getUnitModel().compareTo(obj2.meterWoUnitTO.getUnitModel());
			case CUST_NAME:
				if(obj1.customerName== null && obj2.customerName==null)
					return mod * 0;
				else if(obj1.customerName == null)
					return mod * 1;
				else if(obj2.customerName == null)
					return mod * -1;
				else				
					return mod * obj1.customerName.compareTo(obj2.customerName);
			case ADDRESS:
				if(obj1.addressStreet == null && obj2.addressStreet == null)
					return mod * 0;
				else if(obj1.addressStreet == null)
					return mod * 1;
				else if(obj2.addressStreet == null)
					return mod * -1;
				else				
					return mod * obj1.addressStreet.compareTo(obj2.addressStreet);
			case TIME_RESERVATION:
				if(obj1.timeReservationStart== null && obj2.timeReservationStart==null)
					return mod * 0;
				else if(obj1.timeReservationStart == null)
					return mod * 1;
				else if(obj2.timeReservationStart == null)
					return mod * -1;
				else				
					return mod * obj1.timeReservationStart.compareTo(obj2.timeReservationStart);
			case SLA_DEADLINE:
				if(obj1.slaDeadline== null && obj2.slaDeadline==null)
					return mod * 0;
				else if(obj1.slaDeadline == null)
					return mod * 1;
				else if(obj2.slaDeadline == null)
					return mod * -1;
				else				
					return mod * obj1.slaDeadline.compareTo(obj2.slaDeadline);
			case KEY_NUMBER:
				if(obj1.keyNumber== null && obj2.keyNumber==null)
					return mod * 0;
				else if(obj1.keyNumber == null)
					return mod * 1;
				else if(obj2.keyNumber == null)
					return mod * -1;
				else
					return mod * obj1.keyNumber.compareTo(obj2.keyNumber);	
			case KEY_INFO:
				if(obj1.keyInfo== null && obj2.keyInfo==null)
					return mod * 0;
				else if(obj1.keyInfo == null)
					return mod * 1;
				else if(obj2.keyInfo == null)
					return mod * -1;
				else
					return mod * obj1.keyInfo.compareTo(obj2.keyInfo);
				case WO_NUMBER:
					if(obj1.id== null && obj2.id==null)
						return mod * 0;
					else if(obj1.id == null)
						return mod * 1;
					else if(obj2.id == null)
						return mod * -1;
					else
						return mod * obj1.id.compareTo(obj2.id);
				default:
				if(obj1.instCode== null && obj2.instCode==null)
					return mod * 0;
				else if(obj1.instCode == null)
					return mod * 1;
				else if(obj2.instCode == null)
					return mod * -1;
				else					
					return mod * obj1.instCode.compareTo(obj2.instCode);
			}
			

		}
	}

}
