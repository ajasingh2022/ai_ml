package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.custom.WoEventFieldVisitCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 2/22/2018.
 */
@SuppressLint("ValidFragment")
public class RegisterTimePageFragment extends CustomFragment implements View.OnTouchListener, View.OnClickListener {

    transient EditText startTime = null;

    transient EditText endTime = null;

    transient EditText travelTimeHour = null;
    transient EditText travelTimeMinute = null;
    transient EditText distance = null;
    private transient TimePickerDialog timePicker = null;
    private transient AlertDialog dialog = null;
    private static final String HOUR_PATTERN = "([01]?[0-9]|2[0-3])";
    private static final String MINUTE_PATTERN = "[0-9]|[0-5][0-9]";
    private Pattern pattern;
    private Matcher matcher;
    private TimePickerDialog.OnTimeSetListener startTimeListener;
    private TimePickerDialog.OnTimeSetListener endTimeListener;
    public static String TAG = RegisterTimePageFragment.class.getSimpleName();

    static final String USER_ENTERED_START_HOUR = "USER_ENTERED_START_HOUR";
    static final String USER_ENTERED_START_MIN = "USER_ENTERED_START_MIN";
    static final String USER_ENTERED_END_HOUR = "USER_ENTERED_END_HOUR";
    static final String USER_ENTERED_END_MIN = "USER_ENTERED_END_MIN";
    static final String USER_ENTERED_TRAVEL_HOUR = "USER_ENTERED_TRAVEL_HOUR";
    static final String USER_ENTERED_TRAVEL_MIN = "USER_ENTERED_TRAVEL_MIN";
    static final String USER_ENTERED_DISTANCE = "USER_ENTERED_DISTANCE";

    Date orgStartTimeDate = null;
    Date orgEndTimeDate = null;
    Long orgTravelTimeInMinutes = null;
    Long orgTravelKm = null;
    private Date modStartTimeDate = null;
    private Date modEndTimeDate = null;
    private Long modTravelTimeInMinutes = null;
    private Long modTravelKm = null;

    public RegisterTimePageFragment() {
        super();
    }

    public RegisterTimePageFragment(String stepIdentifier) {
        super(stepIdentifier);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.flow_fragment_time_register, null);
        initializeUI();
        initializePageValues();
        setupListeners();
        populateData();

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateData();


    }

    public void initializePageValues() {

        orgStartTimeDate = null;
        orgEndTimeDate = null;
        orgTravelTimeInMinutes = null;
        orgTravelKm = null;
        modStartTimeDate = null;
        modEndTimeDate = null;
        modTravelTimeInMinutes = null;
        modTravelKm = null;
        WorkorderCustomWrapperTO woModifiable = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
        WoEventFieldVisitCustomTO woEventFieldVisitCustomTO = WorkorderUtils.getWoEventFieldVisitCustomTO(woModifiable);
        try {
            if (null != woEventFieldVisitCustomTO
                    && null != woEventFieldVisitCustomTO.getWoEventFvTO()) {
                try {
                    String fieldVisitId = AbstractWokOrderActivity.workorderCustomWrapperTO.getFieldVisitID();
                    String[] splits = fieldVisitId.split(":");
                    orgStartTimeDate = new Date(Long.parseLong(splits[splits.length-1]));
                }catch (Exception e){

                }
                if (woEventFieldVisitCustomTO.getWoEventFvTO().getFieldVisitEnd() == null) {
                    orgEndTimeDate = new Date();
                } else {
                    orgEndTimeDate = woEventFieldVisitCustomTO.getWoEventFvTO().getFieldVisitEnd();
                }
                orgTravelTimeInMinutes = woEventFieldVisitCustomTO.getWoEventFvTO().getTravelTime();
                orgTravelKm = woEventFieldVisitCustomTO.getWoEventFvTO().getTravelDistance();
            }
            if (null == orgStartTimeDate) {
                orgStartTimeDate = new Date();
            }
        } catch (Exception ex) {
            writeLog(TAG + "  ::initializePageValues()", ex);
        }
    }

    /**
     * Initialize the UI components
     */
    private void initializeUI() {
        final View parentView = fragmentView;
        if (null != parentView) {
            startTime = parentView.findViewById(R.id.registerStartTimeHHEditText);
            endTime = parentView.findViewById(R.id.registerEndTimeHHEditText);
            travelTimeHour = parentView.findViewById(R.id.registerTravelTimeHHEditText);
            travelTimeMinute = parentView.findViewById(R.id.registerTravelTimeMMEditText);
            distance = parentView.findViewById(R.id.registerDistanceEditText);
        }
    }

    /**
     * Set up listeners for UI component
     */
    private void setupListeners() {
        try{
        startTime.setOnTouchListener(this);
        endTime.setOnTouchListener(this);
        startTimeListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay,
                                  int minuteOfDay) {
                startTime.setText(new StringBuilder().append(String.valueOf(hourOfDay)).append(":").append(String.valueOf(minuteOfDay)));
                stepfragmentValueArray.put(USER_ENTERED_START_HOUR, String.valueOf(hourOfDay));
                stepfragmentValueArray.put(USER_ENTERED_START_MIN, String.valueOf(minuteOfDay));

            }
        };
        endTimeListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay,
                                  int minuteOfDay) {
                endTime.setText(new StringBuilder().append(String.valueOf(hourOfDay)).append(":").append(String.valueOf(minuteOfDay)));

                stepfragmentValueArray.put(USER_ENTERED_END_HOUR, String.valueOf(hourOfDay));
                stepfragmentValueArray.put(USER_ENTERED_END_MIN, String.valueOf(minuteOfDay));
            }
        };

        startTime.addTextChangedListener(new EditTextWatcher(startTime));
        endTime.addTextChangedListener(new EditTextWatcher(endTime));
        travelTimeHour.addTextChangedListener(new EditTextWatcher(travelTimeHour));
        travelTimeMinute.addTextChangedListener(new EditTextWatcher(travelTimeMinute));

        distance.addTextChangedListener(new EditTextWatcher(distance));
        } catch (Exception e) {
            writeLog(TAG + "  : setupListeners() ", e);
        }
    }

    private void populateData() {

        Calendar calendarObj = Calendar.getInstance();
        calendarObj.setTime(orgStartTimeDate);
        try {
            String startTimeHour = Utils.isNotEmpty((String) stepfragmentValueArray.get(USER_ENTERED_START_HOUR)) ? (String) stepfragmentValueArray.get(USER_ENTERED_START_HOUR) : String.valueOf(calendarObj.get(Calendar.HOUR_OF_DAY));
            String startTimeMinute = Utils.isNotEmpty((String) stepfragmentValueArray.get(USER_ENTERED_START_MIN)) ? (String) stepfragmentValueArray.get(USER_ENTERED_START_MIN) : String.valueOf(calendarObj.get(Calendar.MINUTE));
            startTime.setText(new StringBuilder().append(startTimeHour).append(":").append(startTimeMinute));
        } catch (Exception e) {
            writeLog(TAG + "  : populateData() ", e);
        }
        try {
            if (orgEndTimeDate != null) {
                calendarObj.setTime(orgEndTimeDate);
                endTime.setText(new StringBuilder().append(Utils.isNotEmpty((String) stepfragmentValueArray.get(USER_ENTERED_END_HOUR)) ? (String) stepfragmentValueArray.get(USER_ENTERED_END_HOUR) : String.valueOf(calendarObj.get(Calendar.HOUR_OF_DAY))).append(":").append(Utils.isNotEmpty((String) stepfragmentValueArray.get(USER_ENTERED_END_MIN)) ? (String) stepfragmentValueArray.get(USER_ENTERED_END_MIN) : String.valueOf(calendarObj.get(Calendar.MINUTE))));

            } else {
                endTime.setText(new StringBuilder().append(Utils.isNotEmpty((String) stepfragmentValueArray.get(USER_ENTERED_END_HOUR)) ? (String) stepfragmentValueArray.get(USER_ENTERED_END_HOUR) : "0").append(":").append(Utils.isNotEmpty((String) stepfragmentValueArray.get(USER_ENTERED_END_MIN)) ? (String) stepfragmentValueArray.get(USER_ENTERED_END_MIN) : "0"));

            }

            if (orgTravelTimeInMinutes != null) {
                travelTimeHour.setText(Utils.isNotEmpty((String) stepfragmentValueArray.get(USER_ENTERED_TRAVEL_HOUR)) ? (String) stepfragmentValueArray.get(USER_ENTERED_TRAVEL_HOUR) : String.valueOf(orgTravelTimeInMinutes / 60));
                travelTimeMinute.setText(Utils.isNotEmpty((String) stepfragmentValueArray.get(USER_ENTERED_TRAVEL_MIN)) ? (String) stepfragmentValueArray.get(USER_ENTERED_TRAVEL_MIN) : String.valueOf(orgTravelTimeInMinutes % 60));
            } else {
                travelTimeHour.setText(Utils.isNotEmpty((String) stepfragmentValueArray.get(USER_ENTERED_TRAVEL_HOUR)) ? (String) stepfragmentValueArray.get(USER_ENTERED_TRAVEL_HOUR) : "0");
                travelTimeMinute.setText(Utils.isNotEmpty((String) stepfragmentValueArray.get(USER_ENTERED_TRAVEL_MIN)) ? (String) stepfragmentValueArray.get(USER_ENTERED_TRAVEL_MIN) : "0");
            }


            if (orgTravelKm != null) {
                distance.setText(Utils.isNotEmpty((String) stepfragmentValueArray.get(USER_ENTERED_DISTANCE)) ? (String) stepfragmentValueArray.get(USER_ENTERED_DISTANCE) : String.valueOf(orgTravelKm));
            } else {
                distance.setText(Utils.isNotEmpty((String) stepfragmentValueArray.get(USER_ENTERED_DISTANCE)) ? (String) stepfragmentValueArray.get(USER_ENTERED_DISTANCE) : "");
            }
        } catch (Exception e) {
            writeLog(TAG + "  : populateData() ", e);
        }
    }

    public boolean validateHour(String time) {
        pattern = Pattern.compile(HOUR_PATTERN);
        matcher = pattern.matcher(time);
        return matcher.matches();
    }

    public boolean validateMinute(String time) {
        pattern = Pattern.compile(MINUTE_PATTERN);
        matcher = pattern.matcher(time);
        return matcher.matches();
    }

    public boolean validateDistance(String distance) {
        return distance != null && !"".equalsIgnoreCase(distance);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try{
        Log.d(getClass().getName(), "Inside On Touch method");
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        if (v.getId() == R.id.registerStartTimeHHEditText && event.getAction() == MotionEvent.ACTION_DOWN) {
            timePicker = new TimePickerDialog(v.getContext(), startTimeListener, hour, minute, true);
            timePicker.setTitle(R.string.select_time);
            timePicker.show();
            return true;
        } else if (v.getId() == R.id.registerEndTimeHHEditText && event.getAction() == MotionEvent.ACTION_DOWN) {
            timePicker = new TimePickerDialog(v.getContext(), endTimeListener, hour, minute, true);
            timePicker.setTitle(R.string.select_time);
            timePicker.show();
            return true;
        }
        } catch (Exception e) {
            writeLog(TAG + "  : onTouch() ", e);
        }
        return false;
    }

    public void showPromptUserAction() {

			/*
             *  If status is false then it is coming from flow engine stack rebuild
			 *  and does not require user attention
                 */

        final View alertView
                = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.prompt_user_response_layout, null);
        if (alertView != null) {
            final Button okButton = alertView.findViewById(R.id.okButtonYesNoPage);
            okButton.setOnClickListener(this);

            final AlertDialog.Builder builder = GuiController.getCustomAlertDialog(getActivity(),
                    alertView, getString(R.string.one_or_more_entries_have_invalid_inputs), null);
            dialog = builder.create();
            dialog.show();
        }
    }


    public boolean validateUserInput() {
        String startTimeValidation = startTime.getText().toString();
        String endTimeValidation = endTime.getText().toString();
        boolean validationStatus = false;
        try {
            validationStatus = validateHour(startTimeValidation.substring(0, startTimeValidation.indexOf(":")))
                    && validateHour(endTimeValidation.substring(0, endTimeValidation.indexOf(":")))
                    && validateMinute(travelTimeMinute.getText().toString())
                    && validateHour(travelTimeHour.getText().toString())
                    && validateMinute(startTimeValidation.substring(startTimeValidation.indexOf(":") + 1, startTimeValidation.length()))
                    && validateMinute(endTimeValidation.substring(endTimeValidation.indexOf(":") + 1, endTimeValidation.length()))
                    && validateDistance(distance.getText().toString())
                    && !distance.getText().toString().startsWith("-")
                    && validateTravelTimeWithTotalTime();
            if (validationStatus) {
                applyChangesToModifiableWO();
            }
        } catch (Exception e) {
            writeLog(TAG + "  : populateData() ", e);
        }
        return validationStatus;
    }

    public void applyChangesToModifiableWO() {
        try {
            WorkorderCustomWrapperTO woModifiable = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            WorkorderUtils.setFieldVisit(woModifiable, modStartTimeDate, modEndTimeDate, modTravelTimeInMinutes, modTravelKm);
        } catch (Exception e) {
            writeLog(TAG + " :applyChangesToModifiableWO() ", e);
        }

    }

    public void getRegisterTimePageFragmentValues() {

        String formatStartTime;
        String formatEndTime;
        String startTimeValue = startTime.getText().toString();
        String endTimeValue = endTime.getText().toString();
        String startTimeHour=startTimeValue.substring(0, startTimeValue.indexOf(":"));
        String startTimeMinute=startTimeValue.substring(startTimeValue.indexOf(":") + 1, startTimeValue.length());
        String endTimeHour= endTimeValue.substring(0, endTimeValue.indexOf(":"));
        String endTimeMinute=endTimeValue.substring(endTimeValue.indexOf(":") + 1, endTimeValue.length());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String theDate = sdf.format(new Date());
        formatStartTime = sdf.format(orgStartTimeDate).substring(0, 8)
                + (Integer.parseInt(startTimeHour)<10 ? "0"+startTimeHour:startTimeHour)
                +(Integer.parseInt(startTimeMinute)<10 ? "0"+startTimeMinute:startTimeMinute) /*fragment.startTimeMinute.getText().toString()*/;
        formatEndTime = theDate.substring(0, 8)
                + (Integer.parseInt(endTimeHour)<10 ? "0"+endTimeHour:endTimeHour)
                +(Integer.parseInt(endTimeMinute)<10 ? "0"+endTimeMinute:endTimeMinute);
        try {

            String hours_ = travelTimeHour.getText().toString();
            String minutes_ = travelTimeMinute.getText().toString();
            if (hours_ == null || hours_.equals("")) {
                hours_ = "0";
            }
            if (minutes_ == null || minutes_.equals("")) {
                minutes_ = "0";
            }

            modStartTimeDate = sdf.parse(formatStartTime);
            modEndTimeDate = sdf.parse(formatEndTime);

            int hours = Integer.parseInt(hours_);
            int minutes = Integer.parseInt(minutes_);

            modTravelTimeInMinutes = new Long(((hours * 60) + minutes));
            modTravelKm = new Long(distance.getText().toString());
        } catch (Exception e) {
            writeLog(TAG + " :getRegisterTimePageFragmentValues() ", e);
        }
    }

    private boolean validateTravelTimeWithTotalTime() {
        getRegisterTimePageFragmentValues();
        int travelTimeInMinute = 0;
        long totaltimeinSEC = 10L;
        long travelTimeInSEC = 10L;
        try {
            totaltimeinSEC = (modEndTimeDate.getTime() - modStartTimeDate.getTime()) / 1000;

            travelTimeInMinute = Integer.parseInt(travelTimeHour.getText().toString()) * 60 +
                    Integer.parseInt(travelTimeMinute.getText().toString());
            travelTimeInSEC = Long.valueOf(travelTimeInMinute * 60);

        } catch (Exception e) {
            writeLog(TAG + "  : logout() ", e);
        }

        return  travelTimeInMinute>0 &&(totaltimeinSEC >= travelTimeInSEC);
    }

    @Override
    public void onClick(View v) {
        if (v != null && v.getId() == R.id.okButtonYesNoPage
                && dialog != null) {
            // Time to dismiss the alert
            dialog.dismiss();
            dialog = null;
        }
    }

    class EditTextWatcher implements TextWatcher {

        private View view;

        EditTextWatcher(View view) {
            this.view = view;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        /**
         * After Text change event
         *
         * @param editable
         */
        @Override
        public void afterTextChanged(Editable editable) {
            if (view.getId() == R.id.registerTravelTimeHHEditText) {
                stepfragmentValueArray.put(USER_ENTERED_TRAVEL_HOUR, editable.toString());
            } else if (view.getId() == R.id.registerTravelTimeMMEditText) {
                stepfragmentValueArray.put(USER_ENTERED_TRAVEL_MIN, editable.toString());
            } else if (view.getId() == R.id.registerDistanceEditText) {
                stepfragmentValueArray.put(USER_ENTERED_DISTANCE, editable.toString());
            }
        }
    }
}


