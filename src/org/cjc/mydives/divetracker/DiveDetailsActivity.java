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

		initialize();
	}
	
	private void initialize() {
		// Title
		((TextView) findViewById(R.id.header_title)).setText(R.string.dive_details_title);
		
		//////////////////////////////////
		//  LOAD DATA
		//////////////////////////////////
		long id = getIntent().getExtras().getLong("_ID");
		
		// Get the data
		diveDbAdapter.open();	// Open the DB
		Cursor cursor = diveDbAdapter.fetchById(id);
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
