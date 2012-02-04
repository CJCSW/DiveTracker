package org.cjc.mydives.divetracker;

import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERDATE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERTIME;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_LATITUDE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_LONGITUDE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_MAX_DEEP;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_NAME;

import java.util.Date;

import org.cjc.mydives.divetracker.db.DiveDbAdapter;
import org.cjc.mydives.divetracker.db.FormatterHelper;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class DiveEditActivity extends MapActivity {
	private DiveDbAdapter diveDbAdapter = new DiveDbAdapter(this);

	long diveId = -1;

	// CONTROLS
	private EditText etName;
	private EditText etDate;
	private EditText etTime;
	private EditText etMaxDeep;
	private Button btnSave;
	private MapView mvMap;
	private MapHelper mapHelper;
	
	Double longitude;
	Double latitude;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dive_edit);
		
		if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("_ID")) {
			diveId = getIntent().getExtras().getLong("_ID");
		} else {
			diveId = -1;
		}

		// Title
		((TextView) findViewById(R.id.header_title)).setText(R.string.dive_edit_title);
		
		// Get the controls
		etName = (EditText) findViewById(R.id.dive_edit_field_name);
		etDate = (EditText) findViewById(R.id.dive_edit_field_date);
		etTime = (EditText) findViewById(R.id.dive_edit_field_time);
		etMaxDeep   = (EditText) findViewById(R.id.dive_edit_field_deep);
		btnSave = (Button) findViewById(R.id.btn_save);
		mvMap = (MapView) findViewById(R.id.dive_map);
		mapHelper = new MapHelper(mvMap, getResources());
		
		mvMap.setBuiltInZoomControls(true);
		mvMap.setClickable(true);
		
		// GPS if we are creating...
		if (diveId == -1) {
			enableGPS();
		}

		addListeners();
		populateFields();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * Method used to subscribe to the listeners of the scenario.
	 */
	public void addListeners() {
		// SAVE Button
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveData();
				finish();
			}
		});
	}

	/**
	 * Method enable and get the GPS position.
	 */
    private void enableGPS() {
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
    	LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				longitude = location.getLongitude();
				latitude = location.getLatitude();
				mapHelper.setMapPosition(latitude, longitude);
			}
			
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			
			public void onProviderEnabled(String provider) {}
			
			public void onProviderDisabled(String provider) {}
    	  };

    	// Register the listener with the Location Manager to receive location updates
    	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);    
   	}
    
	/**
	 * Method used to populate the scenario fields with data.
	 */
    private void populateFields() {
    	if (diveId == -1) {
    		// Instance creation
    		etDate.setText(FormatterHelper.packDate4Scr(new Date(System.currentTimeMillis())));
    		etTime.setText(FormatterHelper.packTime4Scr(new Date(System.currentTimeMillis())));
    	}

		// Get the data
		diveDbAdapter.open();	// Open the DB
		Cursor cursor = diveDbAdapter.fetchById(diveId);
		if (cursor.moveToFirst()) {
			String name = cursor.getString(cursor.getColumnIndex(FIELD_NAME));
			if (name != null) {
				etName.setText(name);
			}
			etDate.setText(FormatterHelper.db2ScrDateFormat(cursor.getString(cursor.getColumnIndex(FIELD_ENTERDATE))));
			etTime.setText(FormatterHelper.db2ScrTimeFormat(cursor.getString(cursor.getColumnIndex(FIELD_ENTERTIME))));

			int maxDeep = cursor.getInt(cursor.getColumnIndex(FIELD_MAX_DEEP));

			etMaxDeep.setText(String.valueOf(maxDeep));
			
			// GPS
			latitude = cursor.getDouble(cursor.getColumnIndex(FIELD_LATITUDE));
			longitude = cursor.getDouble(cursor.getColumnIndex(FIELD_LONGITUDE));
			if (latitude != 0.0f && longitude != 0.0f) {
				mapHelper.setMapPosition(latitude, longitude);
			}
		}
		
		// Close the DB
		cursor.close();
		diveDbAdapter.close();
	}
    
	/**
	 * This will store the data to the database.
	 */
	private void saveData() {
		String name = etName.getText().toString();
		Date date = FormatterHelper.parseScrDate(etDate.getText().toString());
		Date time = FormatterHelper.parseScrTime(etTime.getText().toString());
		int maxDeep = 0;

		try {
			maxDeep = Integer.valueOf(etMaxDeep.getText().toString());
		} catch (NumberFormatException e) {
		}
				
		diveDbAdapter.open();
		if (diveId != -1) {
			diveDbAdapter.update(diveId, name, date, time, maxDeep, null, null, null, null, null, latitude, longitude);
		} else {
			diveDbAdapter.insert(name, date, time, maxDeep, null, null, null, null, null, latitude, longitude);
		}
		diveDbAdapter.close();
	}
}
