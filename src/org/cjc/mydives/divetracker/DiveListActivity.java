package org.cjc.mydives.divetracker;

import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERDATE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERTIME;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_NAME;

import org.cjc.mydives.divetracker.db.DiveDbAdapter;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class DiveListActivity extends Activity {
	private DiveDbAdapter diveDbAdapter;
	private TextView tvEmptyList;
	private ListView lvDives;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dive_list);

		diveDbAdapter = new DiveDbAdapter(this);
		lvDives   = (ListView) this.findViewById(R.id.dive_list);
		tvEmptyList = (TextView) this.findViewById(R.id.dive_list_tv_empty);
		
		initialize();
	}
	
	/**
	 * Initializes the activity.
	 */
	public void initialize() {
		diveDbAdapter.open();	// Open the DB
		
		Cursor cursor = diveDbAdapter.fetchAll();	// Fetch all the instance
		if (cursor.moveToFirst()) {
			// Hide the text for empty lists
			tvEmptyList.setVisibility(TextView.INVISIBLE);
			
			// Populate the list
			ListAdapter listAdapter = new SimpleCursorAdapter(this, R.layout.dive_row, cursor, 
					new String[] {FIELD_ENTERDATE,FIELD_ENTERTIME, FIELD_NAME}, 
					new int[] {R.id.dive_row_date, R.id.dive_row_time, R.id.dive_row_name});
			lvDives.setAdapter(listAdapter);
		}

		// Close the DB
		diveDbAdapter.close();
	}
}
