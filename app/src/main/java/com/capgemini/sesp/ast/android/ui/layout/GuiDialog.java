package com.capgemini.sesp.ast.android.ui.layout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.capgemini.sesp.ast.android.R;

/*	Use this to create a dialog that jumps to one activity on yes and another on no
		params: 
			Activity class on no (mandatory)
			Activity class on yes (mandatory)
			context (mandatory)
			R.id for string title (optional)
			Message to display (optional)
*/

public class GuiDialog extends Dialog implements DialogInterface.OnClickListener {

	private Context context;
	private String message;
	private Class<?> noActivity;
	private Class<?> yesActivity;
	private int titleId;
	
	public GuiDialog(Class<?> noActivity, Class<?> yesActivity, Context context,int titleId, String message) {
		super(context);
		this.context = context;
		this.message = message;
		this.titleId = titleId;
		this.noActivity = noActivity;
		this.yesActivity = yesActivity;
		createDialog().show();
	}
	public GuiDialog(Class<?> noActivity, Class<?> yesActivity, Context context,int titleId) {
		super(context);
		this.context = context;
		this.message = "";
		this.titleId = titleId;
		this.noActivity = noActivity;
		this.yesActivity = yesActivity;
		createDialog().show();
	}
	public GuiDialog(Class<?> noActivity, Class<?> yesActivity, Context context,String message) {
		super(context);
		this.context = context;
		this.message = message;
		this.titleId = 0;
		this.noActivity = noActivity;
		this.yesActivity = yesActivity;
		createDialog().show();
	}

	private AlertDialog createDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (titleId!=0)
			builder.setTitle(titleId);
		if (!message.equals(""))
			builder.setMessage(message);
		builder.setNegativeButton(R.string.no, this);
		builder.setPositiveButton(R.string.yes, this);
		return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		
		if (which == Dialog.BUTTON_POSITIVE) {
			Intent yesIntent = new Intent(context, yesActivity);
			context.startActivity(yesIntent);
		} else if (which == Dialog.BUTTON_NEGATIVE) {
			Intent noIntent = new Intent(context, noActivity);
			context.startActivity(noIntent);
		}
	}
}
