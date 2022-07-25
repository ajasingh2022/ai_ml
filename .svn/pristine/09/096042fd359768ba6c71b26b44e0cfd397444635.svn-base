/**
 *  @copyright Capgemini
 */
package com.capgemini.sesp.ast.android.ui.activity.flow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.ui.fragments.StandardWorkorderChooseExistImageFragment;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * This activity would display all images taken from SESP
 * application plus those taken outside SESP and available in standard gallery
 * 
 * The images taken by SESP would be shown first and the last taken
 * photo would be shown first
 * 
 * @author Capgemini
 * @since 20th Nov, 2014
 * @version 1.0
 *
 */
@SuppressLint("InflateParams")
public class StandardWOImageCaptureActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
		
		setContentView(R.layout.activity_image);
		
		// Show the custom action bar as usual
		// Hiding the logo as requested
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        final ActionBar actionBar = getSupportActionBar();
      	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
      	actionBar.setDisplayShowTitleEnabled(false);

        /*
		 * Setting up custom action bar view
		 */
		final ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		final LayoutInflater layoutManager = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vw = layoutManager.inflate(R.layout.custom_action_bar_layout, null);

		// -- Customizing the action bar ends -----

        getSupportActionBar().setCustomView(vw, layout);
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new StandardWorkorderChooseExistImageFragment()).commit();
		}
		} catch (Exception e) {
			writeLog("StandardWOImageCaptureActivity  :onCreate()", e);
		}
	}
	
}
