package org.cjc.mydives.divetracker;

import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERDATE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERTIME;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_NAME;

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

	long diveId;

	private EditText etName;
	private EditText etDate;
	private EditText etTime;
	
	// Says if the dive has been saved
	private boolean isSaved = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dive_edit);
		
		diveId = getIntent().getExtras().getLong("_ID");

		// Title
		((TextView) findViewById(R.id.header_title)).setText(R.string.dive_edit_title);
		
		etName = (EditText) findViewById(R.id.dive_edit_field_name);
		etDate = (EditText) findViewById(R.id.dive_edit_field_date);
		etTime = (EditText) findViewById(R.id.dive_edit_field_time);

		populateFields();
	}

	// Discard button clicked
    public void onClickDiscard(View view) {
    	isSaved = false;
    	finish();
    }

    public void onClickSave(View view) {
    	isSaved = true;
    	finish();
    }

    private void populateFields() {

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

		diveDbAdapter.open();
		diveDbAdapter.update(diveId, name, date, time, null, null, null, null, null);
		diveDbAdapter.close();
	}
	
	// Deletes the data
	private void deleteData() {
		diveDbAdapter.open();
		diveDbAdapter.delete(diveId);
		diveDbAdapter.close();
		
	}
	
	////////////////
	// LIFECYCLE  //
	////////////////
	@Override
	public void onPause() {
		if (isSaved) {
			saveData();
		} else {
			deleteData();
		}
		super.onPause();
	}
}
