/**
 *
 */
package com.capgemini.sesp.ast.android.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.flow.WoAttachmentCompletionCallback;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.skvader.rsp.cft.common.to.cft.table.AttachmentTTO;

import java.lang.ref.SoftReference;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * This adapter shows list of attachment types out of which the
 * field technician has to select one type for attaching the file
 * <p/>
 * <p><b>
 * Implemented for Concentrator Installation flow
 * <br>
 * Can be reused for other flows
 * </b></p>
 *
 * @author Capgemini
 * @version 1.0
 * @since 12th March, 2015
 */
@SuppressLint("InflateParams")
public class AttachmentTypeListAdapter extends BaseAdapter implements OnClickListener {

    private transient List<AttachmentTTO> attachmentTTOs = null;

    private transient SoftReference<Context> contextRef = null;
    private transient SoftReference<Fragment> fragmentRef = null;

    private transient String fileName = null;
    private transient Dialog popupDialog = null;
    private transient DatabaseHandler sqliteHandler = null;

    private transient long caseId = 0;
    private transient long caseTypeId = 0;
    private transient String fieldVisitId;

    public AttachmentTypeListAdapter(final Context context,
                                     final List<AttachmentTTO> attachmentTTOs,
                                     final String fileName,
                                     final String fieldVisitId,
                                     final long caseId, final long caseTypeId) {

        if (context != null) {
            contextRef = new SoftReference<Context>(context);
        }
        if (attachmentTTOs != null && !attachmentTTOs.isEmpty()) {
            this.attachmentTTOs = attachmentTTOs;
        }
        this.fileName = fileName;

        this.caseId = caseId;
        this.caseTypeId = caseTypeId;
        this.fieldVisitId = fieldVisitId;
    }

    public AttachmentTypeListAdapter(final Fragment fragment,
                                     final List<AttachmentTTO> attachmentTTOs,
                                     final String fileName,
                                     final String fieldVisitId,
                                     final long caseId, final long caseTypeId, final Dialog dialog) {

        if (fragment != null) {
            fragmentRef = new SoftReference<Fragment>(fragment);
        }
        if (attachmentTTOs != null && !attachmentTTOs.isEmpty()) {
            this.attachmentTTOs = attachmentTTOs;
        }
        this.fileName = fileName;

        this.caseId = caseId;
        this.caseTypeId = caseTypeId;
        this.popupDialog = dialog;
        this.fieldVisitId = fieldVisitId;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (this.attachmentTTOs != null) {
            count = this.attachmentTTOs.size();
        }
        return count;
    }

    @Override
    public Object getItem(final int position) {
        AttachmentTTO item = null;
        try{
        if (this.attachmentTTOs != null && this.attachmentTTOs.size() > position) {
            item = this.attachmentTTOs.get(position);
        }
        } catch (Exception e) {
            writeLog(" AttachmentTypeListAdapter : getItem() ", e);
        }
        return item;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    /**
     * Inflate the image reason chooser popup based on the available reasons
     * from the adapter
     *
     * @param position    (Position in the view)
     * @param convertView
     */

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View vw = convertView;
        ViewHolder holder = null;
        try{
        if (vw == null) {

            Context context = null;
            if (contextRef != null && contextRef.get() != null) {
                context = contextRef.get();
            } else if (fragmentRef != null
                    && fragmentRef.get() != null
                    && fragmentRef.get().getActivity() != null) {
                context = fragmentRef.get().getActivity();
            }

            if (context != null) {

                final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vw = inflater.inflate(R.layout.wo_attachment_reason_indiv_row, null);

                holder = new ViewHolder();
                holder.tv = vw.findViewById(R.id.reasonTextViewTv);
            }

            vw.setTag(holder);
        } else {
            holder = (ViewHolder) vw.getTag();
        }

        final AttachmentTTO attachmentType = (AttachmentTTO) getItem(position);
        if (attachmentType != null) {

            if (holder.tv != null) {

                holder.tv.setText(attachmentType.getName());
                holder.tv.setOnClickListener(this);
                holder.tv.setTag(attachmentType);
            }
        }
        } catch (Exception e) {
            writeLog(" AttachmentTypeListAdapter : getView() ", e);
        }
        return vw;
    }

    //For recycling the view
    static class ViewHolder {
        TextView tv;
    }

    public DatabaseHandler getSqliteHandler() {
        try{
        if (sqliteHandler == null) {
            sqliteHandler = DatabaseHandler.createDatabaseHandler();
        }
        } catch (Exception e) {
            writeLog(" AttachmentTypeListAdapter : getSqliteHandler() ", e);
        }
        return sqliteHandler;
    }

    @Override
    public void onClick(final View v) {
        try{
        if (v.getTag() instanceof AttachmentTTO) {
            final AttachmentTTO attachmentType = (AttachmentTTO) v.getTag();
            getSqliteHandler().addAttachment(caseId, caseTypeId, fieldVisitId, fileName, attachmentType.getId(), null);

            // Execute the callback
            if (fragmentRef != null && fragmentRef.get() != null && fragmentRef.get() instanceof WoAttachmentCompletionCallback) {
                Log.d("AttachmentTypeListAdapter", "Executing callback after db persistence is completed");
                ((WoAttachmentCompletionCallback) fragmentRef.get()).onAttachmentTypeChoosenCompleted();
            }
        }

        if (popupDialog != null) {
            popupDialog.dismiss();
        }
        } catch (Exception e) {
            writeLog(" AttachmentTypeListAdapter : onClick() ", e);
        }
    }
}
