package com.capgemini.sesp.ast.android.ui.wo.bean;

import com.skvader.rsp.ast_sep.common.to.ast.table.WoAdditionalDataTTO;

/**
 * Created by nagravi on 20-06-2019.
 */

public class CheckListBean {

    private boolean isSelected;
    private String checkComment;
    private String checkTitle;
    private Long checkId;

    public Long getCheckId() {
        return checkId;
    }

    public void setCheckId(Long checkId) {
        this.checkId = checkId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    private String info;
    private String typeOfControl = "CHECK";

    public String getCheckComment() {
        return checkComment;
    }

    public void setCheckComment(String checkComment) {
        this.checkComment = checkComment;
    }

    public String getCheckTitle() {
        return checkTitle;
    }

    public void setCheckTitle(String checkTitle) {
        this.checkTitle = checkTitle;
    }


    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
