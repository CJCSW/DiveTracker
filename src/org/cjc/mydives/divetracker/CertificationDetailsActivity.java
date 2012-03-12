package org.cjc.mydives.divetracker;

import static org.cjc.mydives.divetracker.db.CertificationConstants.FIELD_DATE;
import static org.cjc.mydives.divetracker.db.CertificationConstants.FIELD_INSTRUCTOR;
import static org.cjc.mydives.divetracker.db.CertificationConstants.FIELD_NUMBER;
import static org.cjc.mydives.divetracker.db.CertificationConstants.FIELD_ORGANIZATION;
import static org.cjc.mydives.divetracker.db.CertificationConstants.FIELD_ROWID;
import static org.cjc.mydives.divetracker.db.CertificationConstants.FIELD_TYPE;

import java.util.Calendar;

import org.cjc.mydives.divetracker.db.CertificationDbAdapter;
import org.cjc.mydives.divetracker.db.FormatterHelper;
import org.cjc.mydives.divetracker.entity.Certification;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
	
	private static final int DATE_DIALOG_ID  = 0;
	
	private Long rowId;
	private Certification certification = new Certification();
	
	private EditText type;
	private EditText number;
	private EditText organization;
	private Button date;
	private EditText instructor;
	
	/** Called when the activity is first created */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.certification_details);
		
		type = (EditText) findViewById(R.id.certification_details_field_type);
		number = (EditText) findViewById(R.id.certification_details_field_number);
		organization = (EditText) findViewById(R.id.certification_details_field_organization);
		date = (Button) findViewById(R.id.certification_details_button_certification_date);
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

	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Calendar date = Calendar.getInstance();
		switch (id) {
		case DATE_DIALOG_ID:
			date.setTimeInMillis(certification.getDate());
			return new DatePickerDialog(
					this, certificationDateSetListener, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener certificationDateSetListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Calendar certificationDate = Calendar.getInstance();
			certificationDate.setTimeInMillis(certification.getDate());
			certificationDate.set(year, monthOfYear, dayOfMonth);
			certification.setDate(certificationDate.getTimeInMillis());
			date.setText(FormatterHelper.formatDate(certification.getDate()));
			
		}
	};
	
	private void addListeners() {
		// Date button
		date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		}
		);
		
		// Confirm button
		((Button)findViewById(R.id.certification_details_button_confirm)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent resultData = new Intent();
				saveState();
				resultData.putExtra(FIELD_ROWID, rowId);
		    	setResult(RESULT_OK, resultData);
		    	finish();
			}
		});
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(FIELD_ROWID, rowId);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		rowId = (Long) savedInstanceState.getSerializable(FIELD_ROWID);
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		populateFields();
	}
	
	private void populateFields() {
		if (rowId != null) {
			certification.set_id(rowId);
			certificationDbAdapter.open();
		
			Cursor certificationCursor = certificationDbAdapter.fetchById(rowId);
			startManagingCursor(certificationCursor);
			
			type.setText(certificationCursor.getString(certificationCursor.getColumnIndexOrThrow(FIELD_TYPE)));
			number.setText(certificationCursor.getString(certificationCursor.getColumnIndexOrThrow(FIELD_NUMBER)));
			organization.setText(certificationCursor.getString(certificationCursor.getColumnIndexOrThrow(FIELD_ORGANIZATION)));
			long certificationDateMillis = certificationCursor.getLong(certificationCursor.getColumnIndexOrThrow(FIELD_DATE));
			date.setText(FormatterHelper.formatDate(certificationDateMillis));
			certification.setDate(certificationDateMillis);
			instructor.setText(certificationCursor.getString(certificationCursor.getColumnIndexOrThrow(FIELD_INSTRUCTOR)));
		
			certificationDbAdapter.close();
		} else {
			Calendar calendar = Calendar.getInstance();
			certification.setDate(calendar.getTimeInMillis());
			date.setText(FormatterHelper.formatDate(certification.getDate()));
		}
	}
	
    public void saveState() {
    	certification.setType(type.getText().toString());
    	certification.setNumber(number.getText().toString());
    	certification.setOrganization(organization.getText().toString());

    	certification.setInstructor(instructor.getText().toString());
    	
    	certificationDbAdapter.open();
    	if (certification.get_id() > 0) {
    		certificationDbAdapter.update(certification);
    	} else {
    		certificationDbAdapter.create(certification);
    	}
    	certificationDbAdapter.close();
    }

}
