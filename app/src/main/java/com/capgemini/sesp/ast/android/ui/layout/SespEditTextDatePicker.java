package com.capgemini.sesp.ast.android.ui.layout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Customised Date Picker for Edit Text
 * Created by samdasgu on 6/24/2017.
 */
public class SespEditTextDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    EditText _editText;
    private int _day;
    private int _month;
    private int _year;
    private Context _context;
    private SimpleDateFormat simpleDateFormat;
    private Date date = null;

    public SespEditTextDatePicker(Context context, int editTextViewID, SimpleDateFormat simpleDateFormat)
    {
        Activity act = (Activity)context;
        this._editText = act.findViewById(editTextViewID);
        this._editText.setOnClickListener(this);
        this._context = context;
        this.simpleDateFormat =simpleDateFormat;
        this.date = new Date();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        _year = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        calendar.set(_year, _month,_day);
        date = calendar.getTime();
        updateDisplay();
    }
    @Override
    public void onClick(View v) {
        try{
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(_context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
        } catch (Exception e) {
            writeLog("SespEditTextDatePicker    : onClick() ", e);
        }
    }

    // updates the date in the birth date EditText
    private void updateDisplay() {
        _editText.setText(simpleDateFormat.format(date));
    }

    public Date getDate() {
        return this.date;
    }
}
