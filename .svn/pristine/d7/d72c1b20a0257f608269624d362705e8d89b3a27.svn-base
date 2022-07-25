package com.capgemini.sesp.ast.android.ui.wo.stepperItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.skvader.rsp.ast_sep.common.to.ast.table.InstAntPlmtTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitModelTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;

import java.lang.ref.SoftReference;
import java.util.List;

public class CustomSpinnerAdapter extends BaseAdapter {

    private transient SoftReference<Context> contextRef = null;

    private transient List<UnitModelCustomTO> unitModels = null;
    private transient List<InstAntPlmtTTO> antPlmtTList = null;

    public CustomSpinnerAdapter(final Context context){
        if(context!=null){
            this.contextRef = new SoftReference<Context>(context);
        }
    }
    public void setUnitModelList(final List<UnitModelCustomTO> unitModels){
        if(unitModels!=null && !unitModels.isEmpty()){
            this.unitModels = unitModels;
        }
    }
    public void setAntPlmtTList(final List<InstAntPlmtTTO> antPlmtTList){
        if(antPlmtTList!=null && !antPlmtTList.isEmpty()){
            this.antPlmtTList = antPlmtTList;
        }
    }
    @Override
    public int getCount() {
        int count = 0;
        if(this.unitModels !=null && this.antPlmtTList==null){
            count = this.unitModels.size();
        } else if (this.unitModels ==null && this.antPlmtTList!=null){
            count = this.antPlmtTList.size();
        }
        return count;
    }
    public int getPosition(long idTag) {
        int index = -1;
        if(unitModels !=null && !unitModels.isEmpty()){
            for(int counter =0; counter < unitModels.size()-1;counter++){
                UnitModelCustomTO unitModel = unitModels.get(counter);
                if(unitModel.getId().longValue() ==  idTag){
                    index = counter;
                    break;
                }
            }
        } else if (antPlmtTList!=null && !antPlmtTList.isEmpty()){
            for(int counter =0; counter < antPlmtTList.size()-1;counter++){
                InstAntPlmtTTO instAntPlmtTTO = antPlmtTList.get(counter);
                if(instAntPlmtTTO.getId().longValue() ==  idTag){
                    index = counter;
                    break;
                }
            }
        }
        return index;
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        if(this.unitModels !=null
                && this.unitModels.size()>position
                && this.antPlmtTList==null){
            item = this.unitModels.get(position);
        } else if (this.antPlmtTList!=null
                && this.antPlmtTList.size()>position
                && this.unitModels ==null){
            item = this.antPlmtTList.get(position);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View vw = convertView;
        if(vw==null){
            final LayoutInflater inflater = (LayoutInflater) this.contextRef.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vw = inflater.inflate(R.layout.adapter_text_view,null);

            if(this.unitModels !=null && this.antPlmtTList==null){
                ((TextView)vw.findViewById(R.id.textViewRow)).setText(((UnitModelTO)getItem(position)).getName());
            } else if (this.unitModels ==null && this.antPlmtTList!=null){
                ((TextView)vw.findViewById(R.id.textViewRow)).setText(((InstAntPlmtTTO)getItem(position)).getName());
            }
        }
        return vw;
    }
}