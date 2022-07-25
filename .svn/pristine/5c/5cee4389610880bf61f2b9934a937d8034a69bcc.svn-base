package com.capgemini.sesp.ast.android.ui.activity.order;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by rkumari2 on 29-11-2019.
 */
public class OrderInfoModel {

    public enum STATE {
        CLOSED,
        OPENED
    }

    String mTitle;
    STATE state = STATE.CLOSED;
    List<String> mValues=new ArrayList<>();
    Long id;
    ArrayList<OrderInfoModel> orderModels = new ArrayList<>();

    public OrderInfoModel(String title, List<String> value) {
        this.mTitle = title;
        this.mValues = value;
    }

}
