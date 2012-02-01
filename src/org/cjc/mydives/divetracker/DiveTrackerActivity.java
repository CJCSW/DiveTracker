package org.cjc.mydives.divetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        
        // User button
        ((Button)findViewById(R.id.main_menu_button_user)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		    	startActivity(new Intent(v.getContext(), UserDetailsActivity.class));
			}
		});
        
        // Dives button
        ((Button)findViewById(R.id.main_menu_button_dives)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		    	startActivity(new Intent(v.getContext(), DiveListActivity.class));
			}
		});
        
    }
}