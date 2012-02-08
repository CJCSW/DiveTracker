package org.cjc.mydives.divetracker;

import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERDATE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERTIME;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_LATITUDE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_LONGITUDE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_MAX_DEEP;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_NAME;

import java.util.Calendar;
import java.util.Date;

import org.cjc.mydives.divetracker.db.DiveDbAdapter;
import org.cjc.mydives.divetracker.db.FormatterHelper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class DiveEditActivity extends MapActivity {
	private static final int TIME_DIALOG_ID = 0;
	private static final int DATE_DIALOG_ID = 1;
	
	TimePicker timePicker;
	DatePicker datePicker;
	
	private DiveDbAdapter diveDbAdapter = new DiveDbAdapter(this);

	long diveId = -1;

	// CONTROLS
	private EditText etName;
	private Button btnDate;
	private Button btnTime;
	private EditText etMaxDeep;
	private Button btnSave;
	private MapView mvMap;
	private MapHelper mapHelper;
	
	Double longitude;
	Double latitude;
	
	Calendar date = Calendar.getInstance();

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
		btnDate = (Button) findViewById(R.id.dive_edit_date);
		btnTime = (Button) findViewById(R.id.dive_edit_time);
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
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, timeSetListener, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), true);
		case DATE_DIALOG_ID:
			return new DatePickerDialog(
					this, dateSetListener, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
			date.set(Calendar.HOUR_OF_DAY,	hourOfDay);
			date.set(Calendar.MINUTE, minuteOfHour);
			btnTime.setText(FormatterHelper.packTime(date.getTime()));
		}
	};
	
	private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			date.set(year, monthOfYear, dayOfMonth);
			btnDate.setText(FormatterHelper.packDate4Scr(date.getTime()));
		}
	};
	
	/**
	 * Method used to subscribe to the listeners of the scenario.
	 */
	public void addListeners() {
		// DATE Button
		btnDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
		
		// TIME Button
		btnTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
			}
		});

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
    		date = Calendar.getInstance();
    		btnDate.setText(FormatterHelper.packDate4Scr(date.getTime()));
    		btnTime.setText(FormatterHelper.packTime4Scr(date.getTime()));
    	}

		// Get the data
		diveDbAdapter.open();	// Open the DB
		Cursor cursor = diveDbAdapter.fetchById(diveId);
		if (cursor.moveToFirst()) {
			String name = cursor.getString(cursor.getColumnIndex(FIELD_NAME));
			if (name != null) {
				etName.setText(name);
			}
			Date dbDate = FormatterHelper.unPackDate(cursor.getString(cursor.getColumnIndex(FIELD_ENTERDATE)));
			Date dbTime = FormatterHelper.unPackTime(cursor.getString(cursor.getColumnIndex(FIELD_ENTERTIME)));
			date.setTime(dbDate);
			date.set(Calendar.HOUR_OF_DAY, dbTime.getHours());
			date.set(Calendar.MINUTE, dbTime.getMinutes());
			btnDate.setText(FormatterHelper.packDate4Scr(dbDate));
			btnTime.setText(FormatterHelper.packTime4Scr(dbTime));

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
		Date dbDate = date.getTime(); //FormatterHelper.parseScrDate(btnDate.getText().toString());
		int maxDeep = 0;

		try {
			maxDeep = Integer.valueOf(etMaxDeep.getText().toString());
		} catch (NumberFormatException e) {
		}
				
		diveDbAdapter.open();
		if (diveId != -1) {
			diveDbAdapter.update(diveId, name, dbDate, dbDate, maxDeep, null, null, null, null, null, latitude, longitude);
		} else {
			diveDbAdapter.insert(name, dbDate, dbDate, maxDeep, null, null, null, null, null, latitude, longitude);
		}
		diveDbAdapter.close();
	}
}
