/**
 * @copyright Capgemini
 */
package com.capgemini.sesp.ast.android.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;

import java.io.File;

/**
 * The adapter that loads and displays the files
 * and place them to file fragment. This class has all the
 * implementation to show images in various ways in the list view
 *
 * @author Capgemini
 * @version 1.0
 * @since 27th April, 2016
 */
@SuppressLint("InflateParams")
public class AttachmentAdapter extends BaseAdapter {

    private transient File[] files = null;
    private transient LayoutInflater inflater = null;

    public AttachmentAdapter(final File[] files, final LayoutInflater inflater) {
        super();
        this.files = files;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (files != null) {
            count = files.length;
        }
        return count;
    }

    @Override
    public Object getItem(final int position) {
        return null;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup container) {

        View vw = convertView;
        ViewHolder holder = null;

        if (vw == null) {

            vw = inflater.inflate(R.layout.add_attachment_row, null);

            holder = new ViewHolder();
            holder.fileNameHolder = vw.findViewById(R.id.add_attachment_filename);

            vw.setTag(holder);

        } else {
            holder = (ViewHolder) vw.getTag();
        }
        if (files != null) {
            final File file = files[position];
            holder.fileNameHolder.setText(file.getName());
        }
        return vw;
    }

    static class ViewHolder {
        TextView fileNameHolder;
    }
}
