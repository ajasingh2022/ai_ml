package com.capgemini.sesp.ast.android.module.tsp;

import java.util.ArrayList;

/** Call back interface for handling asynchtasks

 */
public interface DownloadCallBack {

    public  void DownloadCallBackListener(String returnValue);

    public void ComputeCallBack(ArrayList<WorkOrder> returnValues);


}
