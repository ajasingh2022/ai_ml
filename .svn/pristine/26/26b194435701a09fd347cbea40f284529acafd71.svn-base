package com.capgemini.sesp.ast.android.module.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.text.method.DigitsKeyListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.to.DisplayItem;
import com.skvader.rsp.cft.common.to.custom.base.InfoInterfaceTO;
import com.skvader.rsp.cft.common.to.custom.base.NameInterfaceTO;
import com.skvader.rsp.cft.common.to.custom.base.StaticDataTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class GuIUtil {
    private static final String TAG = GuIUtil.class.getSimpleName();

    /**
     * Checks a String.
     * Returns true if the String is not null and not empty.
     *
     * @param toTest String to test.
     * @return true if not null & not empty
     */
    public static boolean notNullnotEmpty(String toTest) {
        return toTest != null && !"".equals(toTest);
    }

    /**
     * Utility method to create a popup window
     * from the provided layout, width and height
     *
     * @param layout (android.view.View)
     * @param width  (int)
     * @param height (int)
     * @return android.widget.PopupWindow
     * @since 23rd July, 2014
     */

    @SuppressWarnings("deprecation")
    public static final PopupWindow getStdPopUpWindow(final View layout,
                                                      final int width, final int height, final boolean focusable) {
        PopupWindow popupWindow = null;

        if (layout != null) {
            // Creating the PopupWindow
            popupWindow = new PopupWindow(layout, width, height, focusable);
            popupWindow.setContentView(layout);

            popupWindow.setOutsideTouchable(true);

			/*
		    *  Setting the drawable to null or invalid would not allow the popup
		    *  to be auto-dismissed when clicked outside 
		    */
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setTouchable(true);
            popupWindow.setFocusable(false);

        }
        return popupWindow;
    }

    /**
     * Useful method to conditionally dispose
     * the popup window
     *
     * @param popupWindow (android.widget.PopupWindow)
     */
    public static void cleanUpPopUpMenu(final PopupWindow popupWindow) {
        if (popupWindow != null
                && popupWindow.isShowing()) {
    		/* 
    		 * If the popup window is being shown
    		 * currently close it
    		 */
            popupWindow.dismiss();
        }
    }

    /**
     * Utility method to set up Spinner UI item.
     *
     * @param context
     * @param spinner
     * @param clazz
     * @param listener
     * @param <T>
     */
    public static <T extends NameInterfaceTO & InfoInterfaceTO> void setUpSpinner(Context context,
                                                                                  Spinner spinner,
                                                                                  Class<T> clazz,
                                                                                  boolean addBlankObject,
                                                                                  AdapterView.OnItemSelectedListener listener) {
        List<DisplayItem<T>> spinnerList = null;
        List<T> typeTTOs = null;
        if (StaticDataTO.class.isAssignableFrom(clazz)) {
            typeTTOs = ObjectCache.getAllTypes(clazz);
        } else {
            typeTTOs = ObjectCache.getAllIdObjects(clazz);
        }
        spinnerList = DisplayItem.getDisplayItems(typeTTOs, null);
        if (addBlankObject) {
            DisplayItem<T> blankObject = null;
            try {
                blankObject = AndroidUtilsAstSep.newInstance(spinnerList.get(0).getClass(),
                        spinnerList.get(0).getUserObject().getClass().newInstance());
                blankObject.setName("-"+context.getString(R.string.select)+"-");
                blankObject.getUserObject().setId(-1L);
                blankObject.getUserObject().setName("");
                spinnerList.add(0, blankObject);
            } catch (Exception e) {
                writeLog(TAG + " :setUpSpinner()", e);
            }
        }
        SespSpinnerAdapter spinnerArrayAdapter = new SespSpinnerAdapter(context, R.layout.simple_spinner_dropdown_item_multiline, spinnerList);
        spinner.setAdapter(spinnerArrayAdapter);
        if (listener != null) {
            spinner.setOnItemSelectedListener(listener);
        }
    }

    /**
     * Utility method to set up Spinner UI widget.
     *
     * @param context
     * @param spinner
     * @param listItems
     * @param listener
     * @param <T>
     */
    public static <T extends NameInterfaceTO & InfoInterfaceTO> void setUpSpinner(Context context,
                                                                                  Spinner spinner,
                                                                                  List<T> listItems,
                                                                                  boolean addBlankObject,
                                                                                  AdapterView.OnItemSelectedListener listener) {
        List<DisplayItem<T>> spinnerList = null;

        spinnerList = DisplayItem.getDisplayItems(listItems, null);
        if (addBlankObject) {
            DisplayItem<T> blankObject = null;
            try {
                blankObject = AndroidUtilsAstSep.newInstance(spinnerList.get(0).getClass(),
                        spinnerList.get(0).getUserObject().getClass().newInstance());
                blankObject.setName("-"+context.getString(R.string.select)+"-");
                blankObject.getUserObject().setId(-1L);
                blankObject.getUserObject().setName("");
                spinnerList.add(0, blankObject);
            } catch (Exception e) {
                writeLog(TAG + " :setUpSpinner()", e);
            }
        }
        SespSpinnerAdapter spinnerArrayAdapter = new SespSpinnerAdapter(context, R.layout.simple_spinner_dropdown_item_multiline, spinnerList);
        spinner.setAdapter(spinnerArrayAdapter);
        if (listener != null) {
            spinner.setOnItemSelectedListener(listener);
        }
    }

    /**
     * Get Unit Identifier Translated value from resource bundle
     *
     * @param unitIdentifier
     * @return
     */
    public static String displayUnitIdentifierNameText(String unitIdentifier) {
        String unitIdentifierText = null;
        if ("GIAI".equalsIgnoreCase(unitIdentifier))
            unitIdentifierText = ApplicationAstSep.context.getString(R.string.giai);
        else if ("SERIALNO".equalsIgnoreCase(unitIdentifier))
            unitIdentifierText = ApplicationAstSep.context.getString(R.string.serial_number);
        else if ("PROPERTYNO".equalsIgnoreCase(unitIdentifier))
            unitIdentifierText = ApplicationAstSep.context.getString(R.string.property_number);
        return unitIdentifierText;
    }

    /**
     * Get position of item in a spinner
     *
     * @param spinner
     * @param obj
     * @return
     */
    public static int getPositionOfItemInSpinner(Spinner spinner, DisplayItem obj) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (obj != null && spinner.getItemAtPosition(i) != null
                    && spinner.getItemAtPosition(i).equals(obj)) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Get position of item in a spinner
     *
     * @param spinner
     * @param obj
     * @return
     */
    public static <T extends NameInterfaceTO & InfoInterfaceTO> int getPositionOfItemInSpinner(Spinner spinner, T obj) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (obj != null && spinner.getItemIdAtPosition(i) == obj.getId()) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Get position of item in a spinner
     *
     * @param spinner
     * @param idObject
     * @return
     */
    public static int getPositionOfItemInSpinner(Spinner spinner, long idObject) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemIdAtPosition(i) == idObject) {
                index = i;
                break;
            }
        }
        return index;
    }


    /****
     * Method for Setting the Height of the ListView dynamically.
     * *** Hack to fix the issue of not showing all the items of the ListView
     * *** when placed inside a ScrollView
     ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        setListViewHeightBasedOnChildren(listView, 0);
    }

    /****
     * Method for Setting the Height of the ListView dynamically.
     * *** Hack to fix the issue of not showing all the items of the ListView
     * *** when placed inside a ScrollView
     ****/
    public static void setListViewHeightBasedOnChildren(ListView listView, int visibleItemCount) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;
        int adapterCount = listAdapter.getCount();
        if(adapterCount == 0) {
            return;
        }
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        if (adapterCount == 1) {
            params.height = totalHeight;
        } else if (visibleItemCount == 0) {
            params.height = totalHeight + (listView.getDividerHeight() * (adapterCount - 1));
        } else {
            params.height = (totalHeight + (listView.getDividerHeight() * (adapterCount - 1))) / (adapterCount - 1) * visibleItemCount;
        }
        listView.setLayoutParams(params);
    }

    /**
     * Handle coloring and selection of yes no buttons
     *
     * @param positiveBan
     * @param negativeButton
     * @param selectedButton
     */
    public static void handleYesNobutton(final Button positiveBan, final Button negativeButton, final Button selectedButton) {
        if (selectedButton.equals(positiveBan)) {


            positiveBan.setBackgroundResource(R.drawable.ok_enabled);
            //positiveBan.setEnabled(true);

            negativeButton.setBackgroundResource(R.drawable.edit_disabled);
            //negativeButton.setEnabled(false);


        } else {

            negativeButton.setBackgroundResource(R.drawable.edit_enabled);
            //negativeButton.setEnabled(true);

            positiveBan.setBackgroundResource(R.drawable.ok_disabled);
            //positiveBan.setEnabled(false);

        }
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        float px = 0;
        try {
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
             px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        }catch(Exception e)
        {
            writeLog(TAG + ": convertDpToPixel()", e);
        }
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        float dp = 0;
        try {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
         dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        }catch(Exception e)
        {
            writeLog(TAG + ": convertPixelsToDp()", e);
        }
        return dp;
    }

    /**
     * Enable disable button
     * @param button
     * @param enabled
     */
    public static void enableDisableButton(Button button , boolean enabled){
        if(enabled) {
            button.setEnabled(true);
            button.setBackgroundColor(button.getResources().getColor(R.color.colorDisable));

        } else {
            button.setBackgroundColor(button.getResources().getColor(R.color.colorDisable));
            button.setEnabled(false);
        }
    }

    /**
     * Select the button as per boolean
     * @param button
     * @param enabled
     */
    public static void toggleTheButton(Button button , boolean enabled){
        if(enabled) {
            button.setBackgroundColor(button.getResources().getColor(R.color.colorAccent));
            button.setEnabled(true);
        } else {
            button.setBackgroundColor(button.getResources().getColor(R.color.colorDisable));
            button.setEnabled(false);
        }
    }

    /**
     * Check whether the user has selected a valid option in spinner
     * @param input
     * @return
     */
    public static boolean validSpinnerSelection(String input){
        boolean valid = true;
        if(Utils.isEmpty(input) || "-1".equals(input)){
            valid = false;
        }
        return valid;
    }

    /**
     * Clear all child items from a Radio Group
     * Useful for clearing a RadioGroup before initialising
     * @param radioGroup
     */
    public static void clearRadioGroup(RadioGroup radioGroup) {
        if(radioGroup != null) {
            int count = radioGroup.getChildCount();
            if(count>0) {
                for (int i=count-1;i>=0;i--) {
                    View o = radioGroup.getChildAt(i);
                    if (o instanceof RadioButton || o instanceof CheckBox) {
                        radioGroup.removeViewAt(i);
                    }
                }
            }
        }
    }

    public static void fillMeterReadingTable(Context context, Resources resources, TableLayout meterReadingTable, List<String> meterReadings) {
        TableRow row;
        TextView t1, t2, t3, t4;
        //Converting to dip unit
        int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 1, resources.getDisplayMetrics());

        meterReadingTable.removeAllViews();
        for (String meterReading: meterReadings) {
            String [] rowArr = meterReading.split("\\|");
            row = new TableRow(context);

            t1 = new TextView(context);
            t1.setText(rowArr[0]);
            t1.setTypeface(null, 1);
            t1.setTextSize(12);
            t1.setTextColor(resources.getColor(android.R.color.black));
            t1.setWidth(70 * dip);
            t1.setPadding( 5 * dip, 0, 0, 0);
            row.addView(t1);

            t2 = new TextView(context);
            t2.setText(rowArr[2]);
            t2.setTypeface(null, 1);
            t2.setTextSize(12);
            t2.setTextColor(resources.getColor(android.R.color.black));
            t2.setWidth(110 * dip);
            row.addView(t2);

            t3 = new TextView(context);
            t3.setText(rowArr[1]);
            t3.setTypeface(null, 1);
            t3.setTextSize(12);
            t3.setTextColor(resources.getColor(android.R.color.black));
            t3.setWidth(100 * dip);
            row.addView(t3);

            t4 = new TextView(context);
            t4.setText(rowArr[3]);
            t4.setTypeface(null, 1);
            t4.setTextSize(12);
            t4.setTextColor(resources.getColor(android.R.color.black));
            t4.setWidth(120 * dip);
            row.addView(t4);

            meterReadingTable.addView(row, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    public static void checkAndSetDecimalSeparator(EditText editable){
        if(editable.getText().toString().contains(".") || editable.getText().toString().contains(","))
            editable.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        else{
            editable.setKeyListener(DigitsKeyListener.getInstance("0123456789,."));
        }

    }
}
