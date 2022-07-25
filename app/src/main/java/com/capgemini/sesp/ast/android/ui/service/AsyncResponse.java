package com.capgemini.sesp.ast.android.ui.service;

/**
 * Created by samdasgu on 5/11/2016.
 */
public interface AsyncResponse<T> {
    void processFinish(T output);
}
