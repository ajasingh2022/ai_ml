package com.capgemini.sesp.ast.android.ui.activity.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.SessionState;
import com.capgemini.sesp.ast.android.module.util.WorkorderUtils;
import com.capgemini.sesp.ast.android.ui.adapter.RegisterInternalExternalNotesAdapter;
import com.capgemini.sesp.ast.android.ui.wo.AbstractWokOrderActivity;
import com.skvader.rsp.ast_sep.common.to.custom.WoInfoCustomTO;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep.CONSTANTS;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

//import android.widget.EditText;
//import android.widget.EditText;

@SuppressLint("InflateParams")
public class AddCommentActivity extends AppCompatActivity {


    private EditText edTxtToUtil, edTxtToIntern;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_comment);

            edTxtToUtil = findViewById(R.id.add_comment_finish_button);
            edTxtToIntern = findViewById(R.id.add_comment_save_button);

            // Hiding the logo as requested
            getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

            final ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(false);
        
        /*
         * Setting up custom action bar view
		 */
            final ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            final LayoutInflater layoutManager = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View vw = layoutManager.inflate(R.layout.custom_action_bar_layout, null);

            // -- Customizing the action bar ends -----

            getSupportActionBar().setCustomView(vw, layout);
            getSupportActionBar().setDisplayShowCustomEnabled(true);


            final ListView listview = findViewById(R.id.previouslyRegisteredListView);
            final RegisterInternalExternalNotesAdapter adapter = new RegisterInternalExternalNotesAdapter(this, getPreviousComments());
            listview.setAdapter(adapter);
        } catch (Exception e) {
            writeLog("AddCommentActivity  : onCreate() ", e);
        }
        //performSpecUiChanges();
    }

    /*private void performSpecUiChanges(){
        final ImageButton addCommentButton = (ImageButton)findViewById(R.id.commentButton);
        final ImageButton forwardButton = (ImageButton)findViewById(R.id.forward);
        final ImageButton backButton = (ImageButton)findViewById(R.id.back);

        if(addCommentButton!=null){
            addCommentButton.setEnabled(false);
            addCommentButton.setVisibility(View.INVISIBLE);
        }

        if(forwardButton!=null){
            forwardButton.setEnabled(false);
            forwardButton.setVisibility(View.INVISIBLE);
        }

        if(backButton!=null){
            backButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View v) {
                    // Simulate the back button press event
                    onBackPressed();
                }
            });
        }
    }
    */
    @Override
    public void onBackPressed() {
        preserveComments();
        super.onBackPressed();
        // Save the comments        
    }

    @Override
    public void onPause() {
        super.onPause();
        // Save the comments - Not necessary Comments will be saved only when back button is pressed
        // preserveComments();
    }

    /**
     * TODO Load the comments that have previously been made during the work
     * order. These are to be shown in a listview.
     *
     * @return List<String></String>
     */
    private List<String> getPreviousComments() {

        List<String> commentList = new ArrayList<String>();
        try {
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            if (wo != null) {
                List<WoInfoCustomTO> list = wo.getWorkorderCustomTO().getInfoList();
                if (list != null) {
                    for (WoInfoCustomTO woInfoCustomTO : list) {
                        if (woInfoCustomTO.getInfo().getIdInfoT().equals(CONSTANTS().INFO_T__INFO_TO_UTILITY)) {
                            commentList.add(getText(R.string.comment_to_utility) + " : " + woInfoCustomTO.getInfo().getText());
                        }
                        if (woInfoCustomTO.getInfo().getIdInfoT().equals(CONSTANTS().INFO_T__INFO_TO_OPERATOR)) {
                            commentList.add(getText(R.string.internal_comment) + " : " + woInfoCustomTO.getInfo().getText());
                        }
                    }
                }
            }
        } catch (Exception e) {
            writeLog("AddCommentActivity  : getPreviousComments() ", e);
        }

        return commentList;
    }

    private void preserveComments() {
        try {
            final WorkorderCustomWrapperTO wo = AbstractWokOrderActivity.getWorkorderCustomWrapperTO();
            String noteUtility = "";
            String noteInternal = "";
            if (edTxtToUtil.getText() != null) {
                noteUtility = edTxtToUtil.getText().toString();
            }
            if (edTxtToIntern.getText() != null) {
                noteInternal = edTxtToIntern.getText().toString();
            }
            if (wo != null) {

                if (noteUtility.length() > 0) {
                    WoInfoCustomTO woInfoCustomTO =
                            WorkorderUtils.createInfo(noteUtility,
                                    SessionState.getInstance().getCurrentUserUsername(),
                                    CONSTANTS().INFO_SOURCE_T__SESP_PDA,
                                    CONSTANTS().INFO_T__INFO_TO_UTILITY);
                    WorkorderUtils.deleteAndAddWoInfoCustomMTO(wo, woInfoCustomTO);
                }

                if (noteInternal.length() > 0) {
                    WoInfoCustomTO woInfoCustomTO =
                            WorkorderUtils.createInfo(noteInternal,
                                    SessionState.getInstance().getCurrentUserUsername(),
                                    CONSTANTS().INFO_SOURCE_T__SESP_PDA,
                                    CONSTANTS().INFO_T__INFO_TO_OPERATOR);
                    WorkorderUtils.deleteAndAddWoInfoCustomMTO(wo, woInfoCustomTO);
                }
            }
        } catch (Exception e) {
            writeLog("AddCommentActivity  : preserveCommentsα() ", e);
        }
    }


}
