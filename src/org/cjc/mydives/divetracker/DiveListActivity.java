package org.cjc.mydives.divetracker;

import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_NAME;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_TIME_IN;

import org.cjc.mydives.divetracker.db.DiveDbAdapter;
import org.cjc.mydives.divetracker.db.FormatterHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class DiveListActivity extends Activity {
	private DiveDbAdapter diveDbAdapter;
	private TextView tvEmptyList;
	private ListView lvDives;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dive_list);

		// Get the controls
		diveDbAdapter = new DiveDbAdapter(this);
		lvDives   = (ListView) this.findViewById(R.id.dive_list);
		tvEmptyList = (TextView) this.findViewById(R.id.dive_list_tv_empty);
		
		// Title
		//setTitle(R.string.dive_list_title);
		addListeners();
	}
	

    public void addListeners() {
		// DiveList OnClick 
		lvDives.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				Intent i = new Intent(getBaseContext(), DiveDetailsActivity.class);
				i.putExtra("_ID", new Long(id).intValue());
		    	startActivity(i);
			}
		});	
	}
	
	/**
	 * Initializes the activity.
	 */
	public void populateFields() {		
		diveDbAdapter.open();	// Open the DB
		
		Cursor cursor = diveDbAdapter.fetchAll();	// Fetch all the instances
		if (cursor.moveToFirst()) {
			// Hide the text for empty lists
			tvEmptyList.setVisibility(TextView.INVISIBLE);

			// Populate the list
			SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this, R.layout.dive_row, cursor, 
					new String[] {FIELD_NAME, FIELD_TIME_IN, FIELD_TIME_IN}, 
					new int[] {R.id.dive_row_name, R.id.dive_row_date, R.id.dive_row_time});
			
			// Columns Conversion
			listAdapter.setViewBinder(new ViewBinder() {
				public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
					TextView tView = (TextView) view;

					if (tView.getId() == R.id.dive_row_date) {	// DATE
						long value = cursor.getLong(columnIndex);
						tView.setText(FormatterHelper.formatDate(value));
						return true;
					} else if (tView.getId() == R.id.dive_row_time) {	// TIME
						long value = cursor.getLong(columnIndex);
						tView.setText(FormatterHelper.formatTime(value));
						return true;
					} else if (tView.getId() == R.id.dive_row_name) {	// NAME
						tView.setText(cursor.getString(columnIndex));
						return true;
					}
					return false;
				}
			});
			lvDives.setAdapter(listAdapter);
		}

		// Close the DB
		diveDbAdapter.close();
	}

	////////////////
	// LIFECYCLE  //
	////////////////
	@Override
	public void onResume() {
		super.onResume();
		populateFields();
	}
}
