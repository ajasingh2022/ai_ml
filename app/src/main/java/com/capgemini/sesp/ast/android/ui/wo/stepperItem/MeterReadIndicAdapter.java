package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;

import java.lang.ref.SoftReference;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class MeterReadIndicAdapter extends BaseAdapter {

    private transient SoftReference<Context> contextRef = null;
    private transient List<String> readingCounts = null;
    static String TAG = MeterReadIndicAdapter.class.getSimpleName();
    public MeterReadIndicAdapter(final Context context, final List<String> readingCounts){
        if(context!=null){
            this.contextRef = new SoftReference<Context>(context);
        }
        this.readingCounts = readingCounts;
    }

    @Override
    public int getCount() {
        int count = 0;
        if(this.readingCounts!=null){
            count = this.readingCounts.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        Object value = null;
        try{
        if(this.readingCounts!=null
                && this.readingCounts.size()>position
                && position>=0){
            value = this.readingCounts.get(position);
        }
        } catch (Exception e) {
            writeLog(TAG + "  : initialize() ", e);
        }
        return value;
    }

    public int getPosition() {
        int count = 0;
        try{
        if(this.readingCounts!=null){
            count = this.readingCounts.size();
        }
        } catch (Exception e) {
            writeLog(TAG + "  : getPosition() ", e);
        }
        return count;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View vw = convertView;
        try{
        if(vw==null
                && this.contextRef!=null
                && this.contextRef.get()!=null){
            final String value = (String) getItem(position);

            final LayoutInflater inflater
                    = (LayoutInflater)this.contextRef.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vw = inflater.inflate(R.layout.adapter_text_view, null);
            final TextView tv = vw.findViewById(R.id.textViewRow);
            tv.setText(value);
        }
        } catch (Exception e) {
            writeLog(TAG + "  : getView() ", e);
        }
        return vw;
    }

}