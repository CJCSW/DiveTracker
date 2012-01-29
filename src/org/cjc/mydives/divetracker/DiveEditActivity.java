package org.cjc.mydives.divetracker;

import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERDATE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERTIME;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_NAME;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_MAX_DEEP;

import java.util.Date;

import org.cjc.mydives.divetracker.db.DiveDbAdapter;
import org.cjc.mydives.divetracker.db.FormatterHelper;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class DiveEditActivity extends Activity {
	private DiveDbAdapter diveDbAdapter = new DiveDbAdapter(this);

	long diveId = -1;

	private EditText etName;
	private EditText etDate;
	private EditText etTime;
	private EditText etMaxDeep;
	
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
		
		etName = (EditText) findViewById(R.id.dive_edit_field_name);
		etDate = (EditText) findViewById(R.id.dive_edit_field_date);
		etTime = (EditText) findViewById(R.id.dive_edit_field_time);
		etMaxDeep = (EditText) findViewById(R.id.dive_edit_field_deep);

		populateFields();
	}

    public void onClickSave(View view) {
    	saveData();
    	finish();
    }

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
		}
		
		// Close the DB
		cursor.close();
		diveDbAdapter.close();
	}
	
    // Save the data from the screen to the database
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
			diveDbAdapter.update(diveId, name, date, time, maxDeep, null, null, null, null, null);
		} else {
			diveDbAdapter.insert(name, date, time, maxDeep, null, null, null, null, null);
		}
		diveDbAdapter.close();
	}
}
