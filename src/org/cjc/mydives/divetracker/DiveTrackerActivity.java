package org.cjc.mydives.divetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Launch activity
 * @author JuanCarlos
 *
 */
public class DiveTrackerActivity extends Activity {	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void onClick_button_user(View view) {
    	Intent userDetailsIntent = new Intent(this, UserDetailsActivity.class);
    	startActivity(userDetailsIntent);
    }

    public void onClick_button_dives(View view) {
    	startActivity(new Intent(this, DiveListActivity.class));
    }
}