package com.capgemini.sesp.ast.android.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.cache.ObjectCache;
import com.capgemini.sesp.ast.android.module.others.AddrRemoveUnitInterface;
import com.capgemini.sesp.ast.android.ui.activity.material_logistics.common.UnitItem;
import com.capgemini.sesp.ast.android.ui.layout.SespListAdapter;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitIdentifierTTO;
import com.skvader.rsp.ast_sep.common.to.ast.table.UnitTTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.UnitModelCustomTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class UnitAdapter<T extends Context & AddrRemoveUnitInterface> extends SespListAdapter<UnitItem>{

	private final T addRemoveInterface;
	
	public UnitAdapter(T addRemoveInterface, List<UnitItem> items, Integer layout) {
	    super(addRemoveInterface, items, layout);
	    this.addRemoveInterface = addRemoveInterface;
    }

	@Override
    protected View buildView(UnitItem item, View convertView, final int position) {
		try{
		TextView idTextView = convertView.findViewById(R.id.activityAddRemoveListCountRowId);
		idTextView.setText(item.getTitle());
		
		TextView identifierTextView = convertView.findViewById(R.id.activityAddRemoveListCountRowIdentifier);
		TextView actitivityUnitTypeUnitModelTextView = convertView.findViewById(R.id.activityAddRemoveListCountRowUnitTypeUnitModel);
		String unitType = ObjectCache.getTypeName(UnitTTO.class, item.getIdUnitT());
		String unitModel = ObjectCache.getTypeName(UnitModelCustomTO.class, item.getIdUnitModelT());

		identifierTextView.setText(ObjectCache.getTypeName(UnitIdentifierTTO.class, item.getIdUnitIdentifierT()));
		actitivityUnitTypeUnitModelTextView.setText((Utils.isNotEmpty(unitType) ? unitType : "") + "   " + (Utils.isNotEmpty(unitModel) ? unitModel : ""));

		TextView amountTextView = convertView.findViewById(R.id.activityAddRemoveListCountRowAmount);
		amountTextView.setText(item.getAmount()+"");
		
		ImageView iconView = convertView.findViewById(R.id.activityAddRemoveListCountRowIcon);
		
		iconView.setOnClickListener(new View.OnClickListener() {
	    	 public void onClick(View v) {
	    		 addRemoveInterface.removeItem(position);
	    	 }
	     });
		} catch (Exception e) {
			writeLog("UnitAdapter  : buildView() ", e);
		}
		return convertView;
    }

}
