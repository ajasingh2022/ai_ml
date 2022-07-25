package com.capgemini.sesp.ast.android.ui.activity.material_list;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.ui.layout.HelpDialog;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Created by nalwarsa on 7/26/2018.
 */

public class MaterialListActivity extends AppCompatActivity {

    private Button roleButton;
    private TextView roleTv;
    private Fragment technician;
    private Fragment team;
    private Fragment total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_material_list_view);

            // Hiding the logo as requested
            getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

            final ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(false);

            /*
             * Setting up custom action bar view
             */
            final ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            final LayoutInflater layoutManager = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View vw = layoutManager.inflate(R.layout.custom_action_bar_layout, null);
            ImageButton help_btn = vw.findViewById(R.id.menu_help);
            help_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new HelpDialog(MaterialListActivity.this, ConstantsAstSep.HelpDocumentConstant.MATERIAL_lIST);
                    dialog.show();
                }
            });

            // -- Customizing the action bar ends -----


            getSupportActionBar().setCustomView(vw);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            TextView txtTitleBar = findViewById(R.id.title_text);
            txtTitleBar.setText(R.string.material_list);


          //  getSupportActionBar().setTitle(R.string.material_list);

            technician = new TechnicianFragment();
            team = new TeamFragment();
            total = new TotalFragment();

            replaceFragment(technician);
            roleTv = (TextView) findViewById(R.id.role);
            roleButton = (Button) findViewById(R.id.rolesButton);
            roleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Creating the instance of PopupMenu
                    PopupMenu popup = new PopupMenu(MaterialListActivity.this, roleButton);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.material_list_roles, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            int itemId = item.getItemId();
                            if (itemId == R.id.technician) {
                                roleTv.setText(getResources().getString(R.string.material_list_technician));
                                replaceFragment(technician);
                            } else if (itemId == R.id.team) {
                                roleTv.setText(getResources().getString(R.string.material_list_team));
                                replaceFragment(team);

                            } else if (itemId == R.id.total) {
                                roleTv.setText(getResources().getString(R.string.material_list_total));
                                replaceFragment(total);
                            }
                            return true;
                        }
                    });

                    popup.show();//showing popup menu
                }
            });
        } catch (Exception e) {
            writeLog(" MaterialListActivity : onCreate() ", e);
        }
    }


    public void replaceFragment(Fragment destFragment) {
        // First get FragmentManager object.
        try{
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.roleFragment, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
        } catch (Exception e) {
            writeLog( " MaterialListActivity : replaceFragment() ", e);
        }
    }

}


