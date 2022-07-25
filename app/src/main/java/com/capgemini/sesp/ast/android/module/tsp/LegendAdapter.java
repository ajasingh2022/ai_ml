package com.capgemini.sesp.ast.android.module.tsp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.capgemini.sesp.ast.android.R;

public class LegendAdapter extends RecyclerView.Adapter<LegendAdapter.ViewHolder> {


    private LayoutInflater layoutInflater;
    Drawable drawable;
    String[] itemNames ;
    Context mContext;

    public LegendAdapter(Context context) {
        mContext=context;
        layoutInflater = LayoutInflater.from(context);
        itemNames = context.getResources().getStringArray(R.array.items_legend_tsp);
         drawable = AppCompatResources.getDrawable(context, R.drawable.ic_fiber_manual_record_24dp);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem= layoutInflater.inflate(R.layout.dialog_legend_row, parent, false);
        LinearLayout linearLayout = listItem.findViewById(R.id.expandedView);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int sampler = (position+1)*90;
        holder.desc.setText(itemNames[position]);
        VectorDrawable vectorDrawable = (VectorDrawable) mContext.getDrawable(R.drawable.ic_fiber_manual_record_24dp);
        vectorDrawable.setTint(Color.HSVToColor(new float[]{sampler, 1.0F, 0.6F}));
        vectorDrawable.setAlpha(0xFF);

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);

        holder.colorD.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return itemNames.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView desc;
        ImageView colorD;


        public ViewHolder(View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.desc);
            colorD = itemView.findViewById(R.id.colo);

        }
    }
}

    /*only sla 270
        sla &tr 360
        only tr 270
        no sla no tr 360*/