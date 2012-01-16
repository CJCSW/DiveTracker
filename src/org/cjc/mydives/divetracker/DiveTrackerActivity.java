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
    
    public void onClick_user_button(View view) {
    	Intent userDetailsIntent = new Intent(this, UserDetailsActivity.class);
    	startActivity(userDetailsIntent);
    }
    
}