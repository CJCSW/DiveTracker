package org.cjc.mydives.divetracker;

import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_DURATION;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERDATE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERTIME;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_LATITUDE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_LONGITUDE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_MAX_DEEP;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_NAME;

import java.util.Date;

import org.cjc.mydives.divetracker.db.DiveDbAdapter;
import org.cjc.mydives.divetracker.db.FormatterHelper;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class DiveDetailsActivity extends MapActivity {
	private DiveDbAdapter diveDbAdapter = new DiveDbAdapter(this);

	long diveId;
	private TextView tvTitle;

	private TextView tvDate;
	private TextView tvTime;
	private TextView tvMaxDeep;
	private TextView tvDuration;
	
	private ImageView ivEdit;
	private ImageView ivAdd;
	
	private MapView mvMap;
	MapHelper mapHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dive_details);

		tvTitle = (TextView) findViewById(R.id.header_title);
		tvDate = (TextView) this.findViewById(R.id.dive_details_date);
		tvTime = (TextView) this.findViewById(R.id.dive_details_time);
		tvMaxDeep = (TextView) this.findViewById(R.id.dive_details_maxDeep);
		tvDuration = (TextView) this.findViewById(R.id.dive_details_duration);
		ivEdit = (ImageView) this.findViewById(R.id.header_button_edit);
		ivAdd  = (ImageView) this.findViewById(R.id.header_button_add);
		diveId = getIntent().getExtras().getLong("_ID");
		mvMap = (MapView) findViewById(R.id.dive_map);
		mapHelper = new MapHelper(mvMap, getResources());

		// Title
		tvTitle.setText(R.string.dive_details_title);
		// Buttons
		ivAdd.setVisibility(View.GONE);
		ivEdit.setVisibility(View.VISIBLE);

		addListeners();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public void addListeners() {
		// Edit Dive OnClick
		ivEdit.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// Edit the new dive
				Intent i = new Intent(getBaseContext(), DiveEditActivity.class);
				i.putExtra("_ID", diveId);
				startActivity(i);
			}
		});
	}
	
	private void populateFields() {

		// Get the data
		diveDbAdapter.open();	// Open the DB
		Cursor cursor = diveDbAdapter.fetchById(diveId);
		if (cursor.moveToFirst()) {
			tvTitle.setText(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
			tvDate.setText(FormatterHelper.db2ScrDateFormat(cursor.getString(cursor.getColumnIndex(FIELD_ENTERDATE))));
			tvTime.setText(FormatterHelper.db2ScrTimeFormat(cursor.getString(cursor.getColumnIndex(FIELD_ENTERTIME))));
			
			// Max Deep
			double maxDeep = cursor.getDouble(cursor.getColumnIndex(FIELD_MAX_DEEP));
			if (maxDeep > 0.0f) {
				tvMaxDeep.setText(String.valueOf(maxDeep));				
			}
			
			// Duration
			long duration = cursor.getLong(cursor.getColumnIndex(FIELD_DURATION));
			if (duration > 0) {
				tvDuration.setText(FormatterHelper.packTime4Scr(new Date(duration)));
			}
		}
		
		// GPS
		double latitude = cursor.getDouble(cursor.getColumnIndex(FIELD_LATITUDE));
		double longitude = cursor.getDouble(cursor.getColumnIndex(FIELD_LONGITUDE));
		
		mapHelper.setMapPosition(latitude, longitude);

		// Close the DB
		cursor.close();
		diveDbAdapter.close();
	}

	@Override
	public void onResume() {
		populateFields();
		super.onResume();
	}	
}
