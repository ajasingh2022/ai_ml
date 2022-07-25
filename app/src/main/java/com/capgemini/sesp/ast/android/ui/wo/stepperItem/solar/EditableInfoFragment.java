package com.capgemini.sesp.ast.android.ui.wo.stepperItem.solar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.util.AdditionalDataUtils;
import com.capgemini.sesp.ast.android.module.util.TypeDataUtil;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.capgemini.sesp.ast.android.ui.wo.stepperItem.CustomFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoAdditionalDataTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoAdditionalDataTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.WoAdditionalDataValueTO;
import com.skvader.rsp.ast_sep.common.to.custom.WoAdditionalDataCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


@SuppressLint("ValidFragment")
public class EditableInfoFragment extends CustomFragment {
    protected View fragmentView;
    private ListView editDetailList;
    private EditableInfoAdapter editableInfoAdapter;
    private ArrayList<ListItem> listItemEditList;
    /**********PAGE PREFERENCE KEYS *****************/
    protected static final String EDIT_LIST_DATA = "EDIT_LIST_DATA";
    protected static final String CONTENT_VALUE = "CONTENT_VALUE";
    ArrayList<ListItem> cableDetailItems = new ArrayList<ListItem>();
    Gson gson = new Gson();
    Type type = new TypeToken<ArrayList<ListItem>>() {
    }.getType();

    public EditableInfoFragment(String stepId) {
        super(stepId);
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            fragmentView = inflater.inflate(R.layout.fragment_cable_details, container, false);
            intializeUI();
            populateData();
        } catch (Exception e) {
            writeLog("EditableInfoFragment  : onCreateView() ", e);
        }
        return fragmentView;
    }

    private void intializeUI() {
        try {
            final View parentView = fragmentView;
            if (parentView != null) {
                editDetailList = parentView.findViewById(R.id.EditDetailList);
            }
        } catch (Exception e) {
            writeLog("EditableInfoFragment  : intializeUI() ", e);
        }
    }


    @Override
    public boolean validateUserInput() {
        boolean status = false;
        Gson gson = new Gson();
        String string = gson.toJson(cableDetailItems);

        stepfragmentValueArray.put(CONTENT_VALUE, string);
        for (int i = 0; i < cableDetailItems.size(); i++) {
            if (Utils.isNotEmpty(cableDetailItems.get(i).getContent())) {
                status = true;
            } else
                return false;
        }
        if (status) {
            applyChangesToModifiableWO();
        }
        return status;
    }

    public void applyChangesToModifiableWO() {
        try {
            final WorkorderCustomWrapperTO workorderCustomWrapperTO = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            List<WoAdditionalDataCustomTO> woAdditionalDataCustomToList = new ArrayList<WoAdditionalDataCustomTO>();
            for (ListItem listItem : cableDetailItems) {

                List<WoAdditionalDataValueTO> woAdditionalDataValueTOS = new ArrayList<WoAdditionalDataValueTO>();
                WoAdditionalDataTO woAdditionalDataTO = new WoAdditionalDataTO();
                WoAdditionalDataValueTO woAdditionalDataValueTO = new WoAdditionalDataValueTO();
                WoAdditionalDataCustomTO woAdditionalDataCustomTO = new WoAdditionalDataCustomTO();
                woAdditionalDataValueTO.setIdWoAdditionalData(listItem.getEditId());
                if (Utils.isNotEmpty(listItem.getContent())) {
                    woAdditionalDataValueTO.setAdditionalDataValueO(listItem.getContent());
                   // TypeDataUtil.mapValueIntoDOV(listItem.getContent(), woAdditionalDataValueTO, WoAdditionalDataValueTO.ADDITIONAL_DATA_VALUE_D);
                }
                woAdditionalDataValueTO.setIdCase(workorderCustomWrapperTO.getIdCase());
                woAdditionalDataTO.setIdWoAdditionalDataT(listItem.getEditId());
                woAdditionalDataTO.setIdCase(workorderCustomWrapperTO.getIdCase());
                woAdditionalDataValueTOS.add(woAdditionalDataValueTO);
                woAdditionalDataCustomTO.setAdditionalData(woAdditionalDataTO);
                woAdditionalDataCustomTO.setAdditionalDataValue(woAdditionalDataValueTOS);
                woAdditionalDataCustomToList.add(woAdditionalDataCustomTO);
            }

            workorderCustomWrapperTO.getWorkorderCustomTO().getAdditionalDataList().addAll(woAdditionalDataCustomToList);

        } catch (Exception e) {
            writeLog("EditableInfoFragment  : applyChangesToModifiableWO() ", e);
        }
    }

    private void populateData() {
        try {
            if (Utils.isNotEmpty(stepfragmentValueArray.get(CONTENT_VALUE))) {

                cableDetailItems = gson.fromJson(stepfragmentValueArray.get(CONTENT_VALUE).toString(), type);
            } else {
                cableDetailItems = getCableDetails();
            }
            editDetailList.setItemsCanFocus(true);
            editableInfoAdapter = new EditableInfoAdapter(cableDetailItems);
            editDetailList.setAdapter(editableInfoAdapter);

        } catch (Exception e) {
            writeLog("EditableInfoFragment: populateData()", e);
        }
    }

    /*
     *
     * to filter the value
     */
    private List<WoAdditionalDataTTO> editFilterControl() {
        ArrayList<WoAdditionalDataTTO> editList = new ArrayList<>();
        try {
            List<WoAdditionalDataTTO> woAdditionalDataTTOS = ObjectCache.getAllTypes(WoAdditionalDataTTO.class);

            if (woAdditionalDataTTOS != null) {
                for (WoAdditionalDataTTO wo : woAdditionalDataTTOS) {
                    if (wo.getInfo() != null) {
                        String[] str = wo.getInfo().toString().split(";");
                        if (str.length == 2 && wo != null) {
                            if (str[0].equalsIgnoreCase("SOLAR_INST") && str[1].equalsIgnoreCase("EDIT"))
                                editList.add(wo);
                            stepfragmentValueArray.put(EDIT_LIST_DATA, editList);
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog("EditableInfoFragment: filterControl()", e);
        }
        return editList;
    }

    private ArrayList<ListItem> getCableDetails() {

        ArrayList<ListItem> cableDetail = new ArrayList<>();
        try {
            List<WoAdditionalDataTTO> editList = AdditionalDataUtils.filterControl("EDIT");
            if (editList != null) {
                for (int i = 0; i < editList.size(); i++) {
                    ListItem listItem = new ListItem();
                    listItem.setCaption(editList.get(i).getName());
                    listItem.setEditId(editList.get(i).getId());
                    cableDetail.add(listItem);
                }
            }
        } catch (Exception e) {
            writeLog("EditableInfoFragment  : CableDetailAdapter() ", e);
        }
        return cableDetail;
    }

    public class EditableInfoAdapter extends BaseAdapter {
        ViewHolder holder;


        public EditableInfoAdapter(ArrayList<ListItem> listItems) {
            cableDetailItems = listItems;

        }


        @Override
        public int getCount() {
            return cableDetailItems.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService((Context.LAYOUT_INFLATER_SERVICE));
                    convertView = layoutInflater.inflate(R.layout.cableitem, null);
                    holder.caption = (TextView) convertView.findViewById(R.id.EditItemCaption);
                    holder.content = (EditText) convertView.findViewById(R.id.EditItemContent);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
            } catch (Exception e) {
                writeLog("EditableInfoFragment:getView()", e);
            }
            //Fill EditText with the value you have in data source
            holder.caption.setText(cableDetailItems.get(position).caption);
            holder.caption.setId(position);
            holder.content.setId(position);
            holder.content.setText(cableDetailItems.get(position).content);
            holder.content.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    cableDetailItems.get(position).setContent(s.toString());

                }
            });

            //we need to update adapter once we finish with editing
            holder.caption.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        final int position = v.getId();
                        final TextView Caption = (TextView) v;
                        cableDetailItems.get(position).caption = Caption.getText().toString();
                    }
                }
            });
            //we need to update adapter once we finish with editing
            holder.content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        final int position = v.getId();
                        final EditText Content = (EditText) v;
                        cableDetailItems.get(position).content = Content.getText().toString();
                    }
                }
            });
            return convertView;
        }

    }

    class ViewHolder {
        TextView caption;
        EditText content;
    }

    class ListItem {
        String caption;
        String content;
        Long editId;

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }


        public Long getEditId() {
            return editId;
        }

        public void setEditId(Long editId) {
            this.editId = editId;
        }
    }


}

