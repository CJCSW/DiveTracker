package org.cjc.mydives.divetracker;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Launch activity
 * @author JuanCarlos
 *
 */
public class DiveTrackerActivity extends TabActivity {	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle(R.string.app_name);
        
        Resources res = getResources();	// Resources object to get access to strings and graphic resources
        TabHost tabHost = getTabHost();	// The TabHost defined in the layout used as content view for this activity
        TabHost.TabSpec tabSpec;		// Reusable TabSpec to define each of the tabs in this activity's TabHost
        Intent tabIntent;				// Reusable Intent to launch the activity for each of the tabs
        
        // Home tab
        // Create an intent to launch the HomeActivity
        tabIntent = new Intent().setClass(this, HomeActivity.class);
        // Create a TabSpec for the Home tab and add it to the TabHos
        tabSpec = tabHost.newTabSpec(res.getString(R.string.main_menu_tab_tag_home))
        		.setIndicator(res.getString(R.string.main_menu_tab_home), res.getDrawable(R.drawable.ic_tab_home))
        		.setContent(tabIntent);
        tabHost.addTab(tabSpec);
        
        // Dives tab
        // Create an intent to launch the DiveListActivity
        tabIntent = new Intent().setClass(this, DiveListActivity.class);
        // Create a TabSpec for the Dives tab and add it to the TabHos
        tabSpec = tabHost.newTabSpec(res.getString(R.string.main_menu_tab_tag_dives))
        		.setIndicator(res.getString(R.string.main_menu_tab_dives), res.getDrawable(R.drawable.ic_tab_dives))
        		.setContent(tabIntent);
        tabHost.addTab(tabSpec);        

        // User tab
        // Create an intent to launch the UserDetailsActivity
        tabIntent = new Intent().setClass(this, UserDetailsActivity.class);
        // Create a TabSpec for the User tab and add it to the TabHos
        tabSpec = tabHost.newTabSpec(res.getString(R.string.main_menu_tab_tag_user))
        		.setIndicator(res.getString(R.string.main_menu_tab_user), res.getDrawable(R.drawable.ic_tab_user))
        		.setContent(tabIntent);
        tabHost.addTab(tabSpec);     
        
        // Set Home as the current tab
        tabHost.setCurrentTab(0);
    }
}