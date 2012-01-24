package org.cjc.mydives.divetracker;

import org.cjc.mydives.divetracker.db.DiveDbAdapter;

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
        populateDB();
    }
    
    public void onClick_button_user(View view) {
    	Intent userDetailsIntent = new Intent(this, UserDetailsActivity.class);
    	startActivity(userDetailsIntent);
    }

    public void onClick_button_dives(View view) {
    	startActivity(new Intent(this, DiveListActivity.class));
    }
    
    public void populateDB() {
    	// Dives
    	DiveDbAdapter diveAdapter = new DiveDbAdapter(this);
    	if (diveAdapter.open() != null) {
    		diveAdapter.insert("Las Catedrales", System.currentTimeMillis(), System.currentTimeMillis(), null, null, null, null, 0);
    		diveAdapter.insert("LAs Rotas", System.currentTimeMillis(), System.currentTimeMillis(), null, null, null, null, 3);
    		diveAdapter.close();
    	}
    	
    }
}