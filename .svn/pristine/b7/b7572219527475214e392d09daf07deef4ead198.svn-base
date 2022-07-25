package com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar.adapter;

import java.io.Serializable;

public class UnitEntryItem implements Serializable {
    public String getExistingUnit() {
        return ExistingUnit;
    }

    public void setExistingUnit(String existingUnit) {
        ExistingUnit = existingUnit;
    }

    public String getNewUnit() {
        return newUnit;
    }

    public void setNewUnit(String newUnit) {
        this.newUnit = newUnit;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    String ExistingUnit ;
    String newUnit;
    String Model;
    VerificationStatus verificationStatus;


    public enum VerificationStatus{
        SUCCESS,FAILED,IN_PROGRESS,NOT_DONE
    }

}
