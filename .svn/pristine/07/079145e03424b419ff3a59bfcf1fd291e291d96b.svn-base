package com.capgemini.sesp.ast.android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class RegisterInternalExternalNotesAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;

    public RegisterInternalExternalNotesAdapter(Context context, List<String> values) {
        super(context, R.layout.register_internal_external_notes_row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View vw = convertView;
        try {
            if (vw == null) {
                final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vw = inflater.inflate(R.layout.register_internal_external_notes_row, parent, false);
            }

            if (vw != null) {
                final TextView textView = vw.findViewById(R.id.registerInternalExternalNoteTextview);
                textView.setText(values.get(position));
            }
        } catch (Exception e) {
            writeLog("RegisterInternalExternalNotesAdapter  : getView() ", e);
        }

        return vw;
    }
}
