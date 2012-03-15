package org.cjc.mydives.divetracker;

import org.cjc.mydives.divetracker.actionbar.ActionBarMapActivity;
import org.cjc.mydives.divetracker.db.DiveDbAdapter;
import org.cjc.mydives.divetracker.db.FormatterHelper;
import org.cjc.mydives.divetracker.entity.Dive;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.MapView;

public class DiveDetailsActivity extends ActionBarMapActivity {
	private DiveDbAdapter diveDbAdapter = new DiveDbAdapter(this);

	private Dive dive = new Dive();

	private TextView tvDate;
	private TextView tvTime;
	private TextView tvDepth;
	private TextView tvBottomTime;
	private TextView tvVisibility;
	private TextView tvPgIn;
	private TextView tvPgOut;
	
	private MapView mvMap;
	private MapHelper mapHelper;
	
	private AlertDialog alertDialog ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dive_details);

		dive.set_id(getIntent().getExtras().getInt("_ID"));

		tvDate = (TextView) this.findViewById(R.id.dive_details_date);
		tvTime = (TextView) this.findViewById(R.id.dive_details_time);
		tvDepth = (TextView) this.findViewById(R.id.dive_details_maxDeep);
		tvBottomTime = (TextView) this.findViewById(R.id.dive_details_bottomTime);
		tvVisibility = (TextView) this.findViewById(R.id.dive_details_visibility);
		tvPgIn  = (TextView) this.findViewById(R.id.dive_details_pgIn);
		tvPgOut = (TextView) this.findViewById(R.id.dive_details_pgOut);
		mvMap = (MapView) findViewById(R.id.dive_map);
		mapHelper = new MapHelper(mvMap, getResources());

		// Title
		setTitle(R.string.dive_details_title);
		
		// Create the alert dialog for deletion
    	// Delete this dive
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(R.string.msg_dive_deletion_confirmation)
    		.setCancelable(false)
    		.setPositiveButton(R.string.btn_confirmation_afirmative, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					deleteDive();
					finish();
				}
			})
			.setNegativeButton(R.string.btn_confirmation_negative, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

    	alertDialog = builder.create();
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dive_details, menu);

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

            case R.id.menu_edit:
				// Edit the dive
            	Intent i = new Intent(getBaseContext(), DiveEditActivity.class);
				i.putExtra("_ID", dive.get_id());
				startActivity(i);
                break;
            case R.id.menu_delete:
            	alertDialog.show();
            	break;
        }
        return super.onOptionsItemSelected(item);
    }
	
	private void populateFields() {

		// Get the data
		diveDbAdapter.open();	// Open the DB
		Cursor cursor = diveDbAdapter.fetchById(dive.get_id());		
		if (cursor.moveToFirst()) {
			diveDbAdapter.loadInstance(cursor, dive);	// Load the instance
			if (dive.getName() != null && !dive.getName().isEmpty()) {
				setTitle(dive.getName());
			}
			
			tvDate.setText(FormatterHelper.formatDate(dive.getTimeIn()));
			tvTime.setText(FormatterHelper.formatTime(dive.getTimeIn()));

			// PGs
			tvPgIn.setText(dive.getGpIn());
			tvPgOut.setText(dive.getGpOut());
			
			// Max Depth
			double depth = dive.getDepth();
			if (depth > 0.0f) {
				tvDepth.setText(String.valueOf(depth));				
			}
			
			// Visibility
			double visibility = dive.getVisibility();
			if (visibility > 0.0f) {
				tvVisibility.setText(String.valueOf(visibility));
			}
			
			// BottomTime
			if (dive.getBottomTime() > 0) {
				tvBottomTime.setText(FormatterHelper.formatTime(dive.getBottomTime()));
			}
		}
		
		// GPS
		mapHelper.setMapPosition(dive.getLatitude(), dive.getLongitude());

		// Close the DB
		cursor.close();
		diveDbAdapter.close();
	}
	
	/**
	 * Deletes the dive from the database
	 */
	private void deleteDive() {
		diveDbAdapter.open();
		diveDbAdapter.delete(dive.get_id());	// The actual deletion
		diveDbAdapter.close();
	}

	@Override
	public void onResume() {
		populateFields();
		super.onResume();
	}	
}
