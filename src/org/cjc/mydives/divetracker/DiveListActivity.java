package org.cjc.mydives.divetracker;

import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERDATE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERTIME;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_NAME;

import org.cjc.mydives.divetracker.db.DiveDbAdapter;
import org.cjc.mydives.divetracker.db.FormatterHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class DiveListActivity extends Activity {
	private DiveDbAdapter diveDbAdapter;
	private TextView tvEmptyList;
	private ListView lvDives;
	private ImageView ivNewDive;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dive_list);

		// Get the controls
		diveDbAdapter = new DiveDbAdapter(this);
		lvDives   = (ListView) this.findViewById(R.id.dive_list);
		tvEmptyList = (TextView) this.findViewById(R.id.dive_list_tv_empty);
		ivNewDive = (ImageView) this.findViewById(R.id.header_button_add);
		
		// Title
		((TextView) findViewById(R.id.header_title)).setText(R.string.dive_list_title);
		addListeners();
	}
	
	public void addListeners() {
		// DiveList OnClick 
		lvDives.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				Intent i = new Intent(getBaseContext(), DiveDetailsActivity.class);
				i.putExtra("_ID", id);
		    	startActivity(i);
			}
		});
		
		// NewDive OnClick
		ivNewDive.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// Edit the new dive
				Intent i = new Intent(getBaseContext(), DiveEditActivity.class);
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
					new String[] {FIELD_ENTERDATE,FIELD_ENTERTIME, FIELD_NAME}, 
					new int[] {R.id.dive_row_date, R.id.dive_row_time, R.id.dive_row_name});
			
			// Columns Conversion
			listAdapter.setViewBinder(new ViewBinder() {
				public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
					String strValue = cursor.getString(columnIndex);
					TextView tView = (TextView) view;

					if (columnIndex == 2) {			// DATE
						tView.setText(FormatterHelper.db2ScrDateFormat(strValue));
						return true;
					} else if (columnIndex == 3) {	// TIME
						tView.setText(FormatterHelper.db2ScrTimeFormat(strValue));
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
