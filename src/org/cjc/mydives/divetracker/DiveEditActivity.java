package org.cjc.mydives.divetracker;

import java.util.Calendar;

import org.cjc.mydives.divetracker.actionbar.ActionBarMapActivity;
import org.cjc.mydives.divetracker.db.DiveDbAdapter;
import org.cjc.mydives.divetracker.db.FormatterHelper;
import org.cjc.mydives.divetracker.entity.Dive;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.maps.MapView;

/**
 * Scenario for editing and creating Dives.
 * If the activity receives a valid dive id, it is an
 * edition scenario. If not, it will create a new dive.
 * @author carlos
 *
 */
public class DiveEditActivity extends ActionBarMapActivity {
    private static final int DATE_DIALOG_ID  = 0;
    private static final int TIME_IN_DIALOG_ID  = 1;
    private static final int TIME_OUT_DIALOG_ID = 2;

    private static final double MAX_DEPTH = 64.0f;
    private static final double MAX_VISIBILITY = 100.0f;

    private DiveDbAdapter diveDbAdapter = new DiveDbAdapter(this);

    // CONTROLS
    private EditText etName;
    private EditText etAirTemp;
    private EditText etWaterTemp;
    private Button btnDate;
    private Button btnTimeIn;
    private Button btnTimeOut;
    private MapView mvMap;
    private MapHelper mapHelper;
    private Spinner spPgIn;        	// PGs IN
    private SeekBar sbDepth;        // Depth bar
    private TextView tvDepth;       // Depth text
    private SeekBar sbVisibility;   // Visibility bar
    private TextView tvVisibility;  // Visibility text

    private Dive dive = new Dive(); // The dive being edited;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dive_edit);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("_ID")) {
            dive.set_id(getIntent().getExtras().getInt("_ID"));
        } else {
            dive.set_id(-1);
        }

        // Title
        setTitle(R.string.dive_edit_title);

        // Get the controls
        etName = (EditText) findViewById(R.id.dive_edit_field_name);
        etAirTemp = (EditText) findViewById(R.id.dive_edit_airTemp);
        etWaterTemp = (EditText) findViewById(R.id.dive_edit_waterTemp);
        btnDate = (Button) findViewById(R.id.dive_edit_date);
        btnTimeIn = (Button) findViewById(R.id.dive_edit_timeIn);
        btnTimeOut = (Button) findViewById(R.id.dive_edit_timeOut);
        mvMap = (MapView) findViewById(R.id.dive_map);
        mapHelper = new MapHelper(mvMap, getResources());
        sbDepth = (SeekBar) findViewById(R.id.dive_seekBar_depth);
        tvDepth = (TextView) findViewById(R.id.dive_edit_field_depth);
        sbVisibility = (SeekBar) findViewById(R.id.dive_seekBar_visibility);
        tvVisibility = (TextView) findViewById(R.id.dive_edit_field_visibility);
        spPgIn = (Spinner) findViewById(R.id.dive_edit_pgin);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.pg, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPgIn.setAdapter(adapter);

        mvMap.setBuiltInZoomControls(true);
        mvMap.setClickable(true);

        // GPS if we are creating...
        if (dive.get_id() == -1) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dive_edit, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.menu_save) {
            // Save the data
            saveData();
            finish();
       	}
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar date = Calendar.getInstance();
        switch (id) {
        case TIME_IN_DIALOG_ID:
            date.setTimeInMillis(dive.getTimeIn());
            return new TimePickerDialog(this, timeInSetListener, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), true);
        case TIME_OUT_DIALOG_ID:
            date.setTimeInMillis(dive.getTimeOut());
            return new TimePickerDialog(this, timeOutSetListener, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), true);
        case DATE_DIALOG_ID:
            date.setTimeInMillis(dive.getTimeIn());
            return new DatePickerDialog(
                    this, dateSetListener, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timeInSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
            Calendar dateIn = Calendar.getInstance();
            dateIn.setTimeInMillis(dive.getTimeIn());
            dateIn.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateIn.set(Calendar.MINUTE, minuteOfHour);
            dive.setTimeIn(dateIn.getTimeInMillis());
            btnTimeIn.setText(FormatterHelper.formatTime(dive.getTimeIn()));
        }
    };

    private TimePickerDialog.OnTimeSetListener timeOutSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
            Calendar dateOut = Calendar.getInstance();
            dateOut.setTimeInMillis(dive.getTimeOut());
            dateOut.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateOut.set(Calendar.MINUTE, minuteOfHour);
            dive.setTimeOut(dateOut.getTimeInMillis());
            btnTimeOut.setText(FormatterHelper.formatTime(dive.getTimeOut()));
        }
    };

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {
            Calendar dateIn = Calendar.getInstance();
            dateIn.setTimeInMillis(dive.getTimeIn());
            dateIn.set(year, monthOfYear, dayOfMonth);
            dive.setTimeIn(dateIn.getTimeInMillis());
            btnDate.setText(FormatterHelper.formatDate(dive.getTimeIn()));
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

        // TIME IN Button
        btnTimeIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_IN_DIALOG_ID);
            }
        });

        // TIME OUT Button
        btnTimeOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dive.getTimeOut() == 0 && dive.getTimeIn() > 0) {
                    dive.setTimeOut(dive.getTimeIn());
                } else {
                    dive.setTimeOut(System.currentTimeMillis());
                }
                showDialog(TIME_OUT_DIALOG_ID);
            }
        });

        // PG-In SPINNER
        spPgIn.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                    int position, long id) {
                String selectedPgIn = parentView.getItemAtPosition(position).toString();
                if (dive.getGpIn() != null && dive.getGpIn().equalsIgnoreCase(selectedPgIn)) {
                    return;
                }
                dive.setGpIn(selectedPgIn);
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        // DEPTH SeekBar
        sbDepth.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                if (fromUser) {
                    setDepth(progress);
                }
            }
        });

        // VISIBILITY SeekBar
        sbVisibility.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                if (fromUser) {
                    setVisibility(progress);
                }
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
                dive.setLongitude(location.getLongitude());
                dive.setLatitude(location.getLatitude());
                mapHelper.setMapPosition(dive.getLatitude(), dive.getLongitude());
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
    @SuppressWarnings("unchecked")
    private void populateFields() {
        if (dive.get_id() == -1) {
            // Instance creation
            dive.setTimeIn(System.currentTimeMillis());
            btnDate.setText(FormatterHelper.formatDate(dive.getTimeIn()));
            btnTimeIn.setText(FormatterHelper.formatTime(dive.getTimeIn()));
        }

        // Get the data
        diveDbAdapter.open();    // Open the DB
        Cursor cursor = diveDbAdapter.fetchById(dive.get_id());
        if (cursor.moveToFirst()) {
            diveDbAdapter.loadInstance(cursor, dive);
            if (dive.getName() != null) {
                etName.setText(dive.getName());
            }
            btnDate.setText(FormatterHelper.formatDate(dive.getTimeIn()));
            btnTimeIn.setText(FormatterHelper.formatTime(dive.getTimeIn()));

            if (dive.getTimeOut() > 0) {
                btnTimeOut.setText(FormatterHelper.formatTime(dive.getTimeOut()));
            }

            tvDepth.setText(String.valueOf(dive.getDepth()) + "m");
            int progress = new Double(dive.getDepth() * 100 / MAX_DEPTH).intValue();
            sbDepth.setProgress(progress);

            tvVisibility.setText(String.valueOf(dive.getVisibility())+ "m");
            sbVisibility.setProgress(new Double(dive.getVisibility()).intValue());

            // GPS
            if (dive.getLatitude() != 0.0f && dive.getLongitude() != 0.0f) {
                mapHelper.setMapPosition(dive.getLatitude(), dive.getLongitude());
            }

            // PG - IN
            if (dive.getGpIn() != null) {
                spPgIn.setSelection(((ArrayAdapter<String>)spPgIn.getAdapter()).getPosition(dive.getGpIn()));
            } else {
                spPgIn.setSelection(0);
            }

            // Temperature
            etWaterTemp.setText(String.valueOf(dive.getTempWater()));
            etAirTemp.setText(String.valueOf(dive.getTempAir()));
        }

        // Close the DB
        cursor.close();
        diveDbAdapter.close();
    }

    private void setDepth(int relDepth) {
        double depth = relDepth * MAX_DEPTH / 100;
        tvDepth.setText(String.valueOf(depth) + "m");
        dive.setDepth(depth);
    }

    private void setVisibility(int relVisibility) {
        double visibility = relVisibility * MAX_VISIBILITY / 100;
        tvVisibility.setText(String.valueOf(visibility) + "m");
        dive.setVisibility(visibility);
    }

    /**
     * This will store the data to the database.
     */
    private void saveData() {
        dive.setName(etName.getText().toString());
        dive.setTempAir(Integer.valueOf(etAirTemp.getText().toString()));
        dive.setTempWater(Integer.valueOf(etWaterTemp.getText().toString()));

        diveDbAdapter.open();
        if (dive.get_id() != -1) {
            diveDbAdapter.update(dive);
        } else {
            diveDbAdapter.insert(dive);
        }
        diveDbAdapter.close();
    }
}
