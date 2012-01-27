package org.cjc.mydives.divetracker;

import org.cjc.mydives.divetracker.db.DiveDbAdapter;
import org.cjc.mydives.divetracker.db.FormatterHelper;

import static org.cjc.mydives.divetracker.db.DiveConstants.*;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class DiveDetailsActivity extends Activity {
	private DiveDbAdapter diveDbAdapter = new DiveDbAdapter(this);

	long diveId;

	private TextView tvName;
	private TextView tvDate;
	private TextView tvTime;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dive_details);

		tvName = (TextView) this.findViewById(R.id.dive_details_name);
		tvDate = (TextView) this.findViewById(R.id.dive_details_date);
		tvTime = (TextView) this.findViewById(R.id.dive_details_time);

		diveId = getIntent().getExtras().getLong("_ID");

		// Title
		((TextView) findViewById(R.id.header_title)).setText(R.string.dive_details_title);

		populateFields();
	}
	
	private void populateFields() {

		// Get the data
		diveDbAdapter.open();	// Open the DB
		Cursor cursor = diveDbAdapter.fetchById(diveId);
		if (cursor.moveToFirst()) {
			tvName.setText(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
			tvDate.setText(FormatterHelper.db2ScrDateFormat(cursor.getString(cursor.getColumnIndex(FIELD_ENTERDATE))));
			tvTime.setText(FormatterHelper.db2ScrTimeFormat(cursor.getString(cursor.getColumnIndex(FIELD_ENTERTIME))));
		}
		
		// Close the DB
		cursor.close();
		diveDbAdapter.close();
	}
}
