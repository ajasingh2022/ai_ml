/**
 *
 */
package com.capgemini.sesp.ast.android.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.capgemini.sesp.ast.android.R;
import com.skvader.rsp.cft.common.to.cft.table.AttachmentReasonTTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.lang.ref.SoftReference;
import java.util.LinkedList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


/**
 * This adapter shows list of reasons out of which the
 * field technician has to select one reason for taking/choosing this photo
 * <p/>
 * <p><b>
 * Implemented for meter change rollout DM flow only
 * <br>
 * Can be reused for other flows
 * </b></p>
 *
 * @author Capgemini
 * @version 1.0
 * @since 12th March, 2015
 */
@SuppressLint("InflateParams")
public class ImageReasonListAdapter extends BaseAdapter implements OnClickListener {

    private transient List<AttachmentReasonTTO> attachmentReasonTTO = null;
    private transient SoftReference<Context> contextRef = null;
    private transient SoftReference<Fragment> fragmentRef = null;
    private transient boolean[] isChecked = null;
    private List<Long> selectedIdAttachmentReason = null;
    private transient static final String TAG = ImageReasonListAdapter.class.getSimpleName();

    private transient View popupView = null;

    public ImageReasonListAdapter(final Fragment fragment,
                                  final List<AttachmentReasonTTO> attachmentReasonTTOs,
                                  final View popupView,
                                  final List<Long> selectedIdAttachmentReason) {
        try {

            if (fragment != null) {
                fragmentRef = new SoftReference<Fragment>(fragment);
            }
            if (attachmentReasonTTOs != null && !attachmentReasonTTOs.isEmpty()) {
                this.attachmentReasonTTO = attachmentReasonTTOs;
            }
            this.popupView = popupView;
            this.isChecked = new boolean[attachmentReasonTTOs != null ? attachmentReasonTTOs.size() : 0];
            if (Utils.isNotEmpty(selectedIdAttachmentReason)) {
                this.selectedIdAttachmentReason = selectedIdAttachmentReason;
                for (Long idAttachmentReason : selectedIdAttachmentReason) {
                    isChecked[getItemPosition(idAttachmentReason)] = true;
                }
                // Execute the callback
                if (popupView != null) {
                    Button saveButton = popupView.findViewById(R.id.save_btn);
                    if(Utils.isNotEmpty(selectedIdAttachmentReason)){
                        saveButton.setVisibility(View.VISIBLE);
                    }
                    else
                        saveButton.setVisibility(View.GONE);

                    saveButton.setEnabled(Utils.isNotEmpty(selectedIdAttachmentReason));
                }
            } else {
                this.selectedIdAttachmentReason = new LinkedList<Long>();
            }
        } catch (Exception e) {
            writeLog(TAG + " : ImageReasonListAdapter() ", e);
        }
    }

    @Override
    public int getCount() {
        int count = 0;
        try {
            if (this.attachmentReasonTTO != null) {
                count = this.attachmentReasonTTO.size();
            }
        } catch (Exception e) {
            writeLog(TAG + " : getItem() ", e);
        }
        return count;
    }

    @Override
    public AttachmentReasonTTO getItem(final int position) {
        AttachmentReasonTTO item = null;
        try {
            if (this.attachmentReasonTTO != null && this.attachmentReasonTTO.size() > position) {
                item = this.attachmentReasonTTO.get(position);
            }
        } catch (Exception e) {
            writeLog(TAG + " : getItem() ", e);
        }
        return item;
    }

    @Override
    public long getItemId(final int position) {
        AttachmentReasonTTO item = null;
        try {
            if (this.attachmentReasonTTO != null && this.attachmentReasonTTO.size() > position) {
                item = this.attachmentReasonTTO.get(position);
            }
        } catch (Exception e) {
            writeLog(TAG + " : getItemId() ", e);
        }
        return item != null ? item.getId() : 0;
    }

    /**
     * Find the position of selected item
     *
     * @param attachmentReasonId
     * @return
     */
    public int getItemPosition(long attachmentReasonId) {
        for (AttachmentReasonTTO attachmentReasonTTOObj : attachmentReasonTTO) {
            if (attachmentReasonId == attachmentReasonTTOObj.getId()) {
                return attachmentReasonTTO.indexOf(attachmentReasonTTOObj);
            }
        }
        return -1;
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
        try {
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
                }

                vw.setTag(holder);
            } else {
                holder = (ViewHolder) vw.getTag();
            }

            holder.tv = vw.findViewById(R.id.reasonTextViewTv);
            holder.cb = vw.findViewById(R.id.add_photo_reason_checkbox_item);
            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ImageReasonListAdapter.this.isChecked[position] = isChecked;
                }
            });
            holder.cb.setChecked(isChecked[position]);

            final AttachmentReasonTTO reasonTTO = getItem(position);
            if (reasonTTO != null) {
                if (holder.tv != null) {
                    holder.tv.setText(reasonTTO.getName());
                    holder.cb.setOnClickListener(this);
                    holder.cb.setTag(reasonTTO);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + " : getView() ", e);
        }
        return vw;
    }

    //For recycling the view
    static class ViewHolder {
        CheckBox cb;
        TextView tv;
    }

    public List<Long> getSelectedIdAttachmentReason() {
        return this.selectedIdAttachmentReason;
    }

    @Override
    public void onClick(final View view) {
        try {
            AttachmentReasonTTO reasonTTO = null;
            final CheckBox cb = (CheckBox) view;
            if (view.getTag() instanceof AttachmentReasonTTO) {
                reasonTTO = (AttachmentReasonTTO) view.getTag();
            }
            if (reasonTTO != null) {
                if (cb.isChecked()) {
                    selectedIdAttachmentReason.add(reasonTTO.getId());
                    Log.d(TAG, "Adding reason :: " + reasonTTO.getName());
                } else {
                    selectedIdAttachmentReason.remove(reasonTTO.getId());
                    Log.d(TAG, "Removing reason :: " + reasonTTO.getName());
                }
            }

            // Execute the callback
            if (popupView != null) {
                Button saveButton = popupView.findViewById(R.id.save_btn);
                if(Utils.isNotEmpty(selectedIdAttachmentReason)){
                    saveButton.setVisibility(View.VISIBLE);
                }
                else
                    saveButton.setVisibility(View.GONE);

                saveButton.setEnabled(Utils.isNotEmpty(selectedIdAttachmentReason));

            }
        } catch (Exception e) {
            writeLog(TAG + " : onClick() ", e);
        }
    }
}
