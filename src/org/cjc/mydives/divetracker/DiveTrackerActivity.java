package org.cjc.mydives.divetracker;

import org.cjc.mydives.divetracker.actionbar.ActionBarActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Launch activity
 * @author JuanCarlos
 *
 */
public class DiveTrackerActivity extends ActionBarActivity {	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle(R.string.app_name);
        
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dive_list, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_refresh:
                Toast.makeText(this, "Fake refreshing...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_create:
				// Edit the new dive
				Intent i = new Intent(getBaseContext(), DiveEditActivity.class);
				startActivity(i);
                break;

        }
        return super.onOptionsItemSelected(item);
    }    
}