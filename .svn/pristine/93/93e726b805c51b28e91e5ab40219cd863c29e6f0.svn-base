package com.capgemini.sesp.ast.android.ui.activity.material_logistics.common;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.others.AddrRemoveUnitInterface;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitIdentifierTTO;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class AddSequenceDialog<T extends AppCompatActivity & AddrRemoveUnitInterface> extends Dialog {
    private T activityList;
    private static final int SCAN_REQUEST = 1;
    private static final String FROM_SCAN_BUTTON = "fromScanButton";
    private static final String TO_SCAN_BUTTON = "toScanButton";
    private static String whichScanOn;
    static String TAG = AddSequenceDialog.class.getSimpleName();

    public AddSequenceDialog(T palletTakingEditActivity) {
        super(palletTakingEditActivity);
        activityList = palletTakingEditActivity;

        setContentView(R.layout.dialog_add_sequence);
        setTitle(R.string.add_sequence);

        final EditText identifierFromEditText = findViewById(R.id.dialogAddSequenceFromEditText);
        final EditText identifierToEditText = findViewById(R.id.dialogAddSequenceToEditText);
        Long idUnitIdentifier = SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER);
        identifierFromEditText.setHint(ObjectCache.getTypeName(UnitIdentifierTTO.class, idUnitIdentifier));
        identifierToEditText.setHint(ObjectCache.getTypeName(UnitIdentifierTTO.class, idUnitIdentifier));

        identifierFromEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int characterCount) {
                if (SESPPreferenceUtil.isMLUnitIdentifierLengthUsed()) {
                    Long identifierLengthSetting = SESPPreferenceUtil.getIdentifierLengthNullIfOff();
                    if (identifierLengthSetting != null) { // Setting is on
                        if (characterCount == identifierLengthSetting.intValue() &&
                                identifierToEditText.getText().toString().length() == identifierLengthSetting.intValue()) {
                            nextButtonClicked(null);
                        } else if (characterCount > identifierLengthSetting.intValue()) {
                            Toast.makeText(activityList, activityList.getString(R.string.error_entered_indentifier_is_too_long), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });

        identifierToEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int characterCount) {
                if (SESPPreferenceUtil.isMLUnitIdentifierLengthUsed()) {
                    Long identifierLengthSetting = SESPPreferenceUtil.getIdentifierLengthNullIfOff();
                    if (identifierLengthSetting != null) { // Setting is on
                        if (characterCount == identifierLengthSetting.intValue() &&
                                identifierFromEditText.getText().toString().length() == identifierLengthSetting.intValue()) {
                            nextButtonClicked(null);
                        } else if (characterCount > identifierLengthSetting.intValue()) {
                            Toast.makeText(activityList, activityList.getString(R.string.error_entered_indentifier_is_too_long), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });

        /** Listeners for button clicks */
        Button addButton = findViewById(R.id.dialogAddRemoveButton);
        addButton.setText(activityList.getString(R.string.add_and_close));
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addButtonClicked(null);
            }
        });
        Button nextButton = findViewById(R.id.dialogNextButton);
        nextButton.setText(activityList.getString(R.string.add_more));
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextButtonClicked(null);
            }
        });
        Button fromScanButton = findViewById(R.id.dialogAddSequenceIdentifierFromScanButton);
        fromScanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                whichScanOn = FROM_SCAN_BUTTON;
                scanButtonClicked(v);
            }
        });

        Button toScanButton = findViewById(R.id.dialogAddSequenceIdentifierToScanButton);
        toScanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                whichScanOn = TO_SCAN_BUTTON;
                scanButtonClicked(v);
            }
        });
    }

    public void scanButtonClicked(View view) {
        BarcodeListener.barcodeListener = new BarcodeDetector() {
            @Override
            public void scannedCodeItem(String scannedCode) {
                if (whichScanOn.equals(FROM_SCAN_BUTTON)) {
                    final EditText identifierFromEditText = findViewById(R.id.dialogAddSequenceFromEditText);
                    identifierFromEditText.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(scannedCode, true));
                } else if (whichScanOn.equals(TO_SCAN_BUTTON)) {
                    final EditText identifierToEditText = findViewById(R.id.dialogAddSequenceToEditText);
                    identifierToEditText.setText(AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(scannedCode, true));
                }
            }
        };
        Intent barCodeScanner = new Intent(activityList, MaterialLogisticsDialogBarcodeScanner.class);
        activityList.startActivityForResult(barCodeScanner, SCAN_REQUEST);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addButtonClicked(View view) {
        UnitItem newItem = getInput();
        if (newItem != null) {
            if (activityList.checkExistingSequenceUnit(newItem.getIdFrom(), newItem.getIdTo())) {
                Toast.makeText(activityList, activityList.getString(R.string.error_device_already_in_list), Toast.LENGTH_SHORT).show();
            } else {
                activityList.addItem(newItem);
            }
            dismiss();
        }
    }

    public void nextButtonClicked(View view) {
        UnitItem newItem = getInput();
        if (newItem != null) {
            if (activityList.checkExistingSequenceUnit(newItem.getIdFrom(), newItem.getIdTo())) {
                Toast.makeText(activityList, activityList.getString(R.string.error_device_already_in_list), Toast.LENGTH_SHORT).show();
            } else {
                activityList.addItem(newItem);
                EditText identifierFromEditText = findViewById(R.id.dialogAddSequenceFromEditText);
                identifierFromEditText.setText("");
                EditText identifierToEditText = findViewById(R.id.dialogAddSequenceToEditText);
                identifierToEditText.setText("");
                identifierFromEditText.requestFocus();
            }
        }
    }

    private UnitItem getInput() {
        Long identifierLengthSetting = SESPPreferenceUtil.getIdentifierLengthNullIfOff();
        EditText identifierToEditText = findViewById(R.id.dialogAddSequenceToEditText);
        String identifierTo = identifierToEditText.getText().toString().trim();
        EditText identifierFromEditText = findViewById(R.id.dialogAddSequenceFromEditText);
        String identifierFrom = identifierFromEditText.getText().toString().trim();
        Long units = unitsInSequence(identifierFrom, identifierTo);
        try {
            if (identifierFrom.isEmpty()) {
                Toast.makeText(activityList, activityList.getString(R.string.error_identifier_is_missing), Toast.LENGTH_SHORT).show();
                return null;
            } else if (identifierLengthSetting != null && identifierFrom.length() < identifierLengthSetting.intValue()) {
                Toast.makeText(activityList, activityList.getString(R.string.error_identifier_from_is_too_short), Toast.LENGTH_SHORT).show();
                return null;
            } else if (identifierLengthSetting != null && identifierFrom.length() > identifierLengthSetting.intValue()) {
                Toast.makeText(activityList, activityList.getString(R.string.error_identifier_from_is_too_long), Toast.LENGTH_SHORT).show();
                return null;
            } else if (identifierFrom.startsWith("-")) {
                Toast.makeText(activityList, activityList.getString(R.string.unit_identifier_can_not_be_negative), Toast.LENGTH_SHORT).show();
                return null;
            }


            if (identifierTo.isEmpty()) {
                Toast.makeText(activityList, activityList.getString(R.string.error_identifier_is_missing), Toast.LENGTH_SHORT).show();
                return null;
            } else if (identifierLengthSetting != null && identifierTo.length() < identifierLengthSetting.intValue()) {
                Toast.makeText(activityList, activityList.getString(R.string.error_identifier_to_is_too_short), Toast.LENGTH_SHORT).show();
                return null;
            } else if (identifierLengthSetting != null && identifierTo.length() > identifierLengthSetting.intValue()) {
                Toast.makeText(activityList, activityList.getString(R.string.error_identifier_to_is_too_long), Toast.LENGTH_SHORT).show();
                return null;
            } else if (identifierTo.startsWith("-")) {
                Toast.makeText(activityList, activityList.getString(R.string.unit_identifier_can_not_be_negative), Toast.LENGTH_SHORT).show();
                return null;
            }


            if (units == null) {
                Toast.makeText(activityList, activityList.getString(R.string.error_sequence_is_empty_or_faulty), Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (Exception e) {
            writeLog(TAG + " : scanButtonClicked()", e);
        }
        return new UnitItem(identifierFrom, identifierTo, units,
                SESPPreferenceUtil.getPreference(SharedPreferenceKeys.MATERIAL_LOGISTICS_ID_IDENTIFIER));
    }

    public static List<String> getUnitsInSequence(String prefix, Long from, Long to) {
        List<String> units = new ArrayList<String>();
        try {
            for (long i = from; i <= to; i++) {
                units.add(prefix + i);
            }
        } catch (Exception e) {
            writeLog(TAG + " : scanButtonClicked()", e);
        }
        return units;
    }

    public static List<String> getUnitsInSequence(String from, String to) {
        boolean sameIdentifier = true;
        try {
            long f = Long.parseLong(from);
            long t = Long.parseLong(to);
            return getUnitsInSequence("", f, t);
        } catch (Exception e) {
            if (from.length() == to.length()) {
                // get prefix
                StringBuffer prefix = new StringBuffer();
                for (int i = 0; i < from.length(); i++) {
                    if (from.charAt(i) == to.charAt(i)) {
                        prefix.append(from.charAt(i));
                    } else {
                        sameIdentifier = false;
                        break;
                    }
                }
                try {
                    long f = Long.parseLong(from.substring(prefix.length()));
                    long t = Long.parseLong(to.substring(prefix.length()));
                    return getUnitsInSequence(prefix.toString(), f, t);
                } catch (Exception ex) {
                    if (sameIdentifier) {
                        List<String> unitsInSequence = new ArrayList<String>();
                        unitsInSequence.add(from);
                        return unitsInSequence;
                    }
                }
            }
        }
        return null;

    }

    private Long unitsInSequence(String from, String to) {
        Long result = null;
        boolean sameIdentifier = true;
        try {
            long f = Long.parseLong(from);
            long t = Long.parseLong(to);
            result = t - f + 1;
        } catch (Exception e) {
            if (from.length() == to.length()) {
                // get prefix
                StringBuffer prefix = new StringBuffer();
                for (int i = 0; i < from.length(); i++) {
                    if (from.charAt(i) == to.charAt(i)) {
                        prefix.append(from.charAt(i));
                    } else {
                        sameIdentifier = false;
                        break;
                    }
                }
                try {
                    long f = Long.parseLong(from.substring(prefix.length()));
                    long t = Long.parseLong(to.substring(prefix.length()));
                    result = t - f + 1;
                    if (result.equals(1L)) {
                        result = null;
                    }
                } catch (Exception ex) {
                    //same to and from sequence
                    if (sameIdentifier) {
                        result = 1L;
                    }
                }
            }
        }
        if (result != null && result > 0) {
            return result;
        } else {
            return null;
        }
    }
}
