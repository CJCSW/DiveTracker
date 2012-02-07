package org.cjc.mydives.divetracker;

import static org.cjc.mydives.divetracker.db.CertificationConstants.FIELD_DATE;
import static org.cjc.mydives.divetracker.db.CertificationConstants.FIELD_INSTRUCTOR;
import static org.cjc.mydives.divetracker.db.CertificationConstants.FIELD_NUMBER;
import static org.cjc.mydives.divetracker.db.CertificationConstants.FIELD_ORGANIZATION;
import static org.cjc.mydives.divetracker.db.CertificationConstants.FIELD_ROWID;
import static org.cjc.mydives.divetracker.db.CertificationConstants.FIELD_TYPE;

import org.cjc.mydives.divetracker.db.CertificationDbAdapter;
import org.cjc.mydives.divetracker.db.FormatterHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Certification details edition
 * @author JuanCarlos
 *
 */
public class CertificationDetailsActivity extends Activity {
	private CertificationDbAdapter certificationDbAdapter;
	
	private Long rowId;
	private EditText type;
	private EditText number;
	private EditText organization;
	private DatePicker date;
	private EditText instructor;
	
	private boolean isCanceled;
	
	/** Called when the activity is first created */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.certification_details);
		
		type = (EditText) findViewById(R.id.certification_details_field_type);
		number = (EditText) findViewById(R.id.certification_details_field_number);
		organization = (EditText) findViewById(R.id.certification_details_field_organization);
		date = (DatePicker) findViewById(R.id.certification_details_field_date);
		instructor = (EditText) findViewById(R.id.certification_details_field_instructor);
		
		certificationDbAdapter = new CertificationDbAdapter(this);
		
		rowId = null;
		Bundle extras = getIntent().getExtras();
		
		// Set the rowId (in case the intent is for the edition of an existing User)
		rowId = (savedInstanceState == null) ? null : (Long) savedInstanceState.getSerializable(FIELD_ROWID);
		if (extras != null) {
			rowId = extras.getLong(FIELD_ROWID); 
		}
		
		// Set header text
		((TextView) findViewById(R.id.header_title)).setText(R.string.certification_details_title);
		// Hide header buttons
        ((ImageView) findViewById(R.id.header_button_edit)).setVisibility(View.GONE);
		((ImageView) findViewById(R.id.header_button_add)).setVisibility(View.GONE);
		
		// Add listeners
		addListeners();
		
		// Populate fields
		populateFields();
	}
	
	private void addListeners() {
		// Confirm button
		((Button)findViewById(R.id.certification_details_button_confirm)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent resultData = new Intent();
				resultData.putExtra(FIELD_ROWID, rowId);
		    	setResult(RESULT_OK, resultData);
		    	isCanceled = false;
		    	finish();
			}
		});
		
		// Cancel button
		((Button)findViewById(R.id.certification_details_button_cancel)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent resultData = new Intent();
				resultData.putExtra(FIELD_ROWID, rowId);
		    	setResult(RESULT_CANCELED, resultData);
		    	isCanceled = true;
		    	finish();
			}
		});
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(FIELD_ROWID, rowId);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		rowId = (Long) savedInstanceState.getSerializable(FIELD_ROWID);
		isCanceled = true;
		populateFields();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		saveState();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		isCanceled = true;
		populateFields();
	}
	
	private void populateFields() {
		certificationDbAdapter.open();
		if (rowId != null) {
			Cursor certificationCursor = certificationDbAdapter.fetchById(rowId);
			startManagingCursor(certificationCursor);
			
			type.setText(certificationCursor.getString(certificationCursor.getColumnIndexOrThrow(FIELD_TYPE)));
			number.setText(certificationCursor.getString(certificationCursor.getColumnIndexOrThrow(FIELD_NUMBER)));
			organization.setText(certificationCursor.getString(certificationCursor.getColumnIndexOrThrow(FIELD_ORGANIZATION)));
			String dateString = certificationCursor.getString(certificationCursor.getColumnIndexOrThrow(FIELD_DATE));
			date.updateDate(Integer.parseInt(dateString.substring(0, 4)),
						Integer.parseInt(dateString.substring(4,6)) - 1, 
						Integer.parseInt(dateString.substring(6,8)));
			instructor.setText(certificationCursor.getString(certificationCursor.getColumnIndexOrThrow(FIELD_INSTRUCTOR)));
		}
		certificationDbAdapter.close();
	}
	
    public void saveState() {
    	certificationDbAdapter.open();
    	if (!isCanceled) {
    		String type = this.type.getText().toString();
    		String number = this.number.getText().toString();
    		String organization = this.organization.getText().toString();
    		String dateString = this.date.getYear()
    				+ String.format("%02d", this.date.getMonth() + 1)
    				+ String.format("%02d", this.date.getDayOfMonth());
    		java.util.Date date = FormatterHelper.unPackDate(dateString);
			
    		String instructor = this.instructor.getText().toString();
    		if (rowId == null) {
    			long id = certificationDbAdapter.create(type, date, number, organization, instructor);
    			if (id > 0) {
    				rowId = id;
    			}
    		} else {
    			certificationDbAdapter.update(rowId, type, date, number, organization, instructor);
    		}
    	}
    	certificationDbAdapter.close();
    }

}
