package org.cjc.mydives.divetracker;

import org.cjc.mydives.divetracker.db.DiveDbAdapter;
import static org.cjc.mydives.divetracker.db.DiveConstants.*;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class DiveDetailsActivity extends Activity {
	private DiveDbAdapter diveDbAdapter = new DiveDbAdapter(this);
	private TextView tvName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dive_details);
		
		initialize();
	}
	
	private void initialize() {
		tvName = (TextView) this.findViewById(R.id.dive_details_name);
		long id = getIntent().getExtras().getLong("_ID");
		
		// Get the data
		diveDbAdapter.open();	// Open the DB
		Cursor cursor = diveDbAdapter.fetchById(id);
		if (cursor.moveToFirst()) {
			tvName.setText(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
		}
		
		// Close the DB
		cursor.close();
		diveDbAdapter.close();
	}
}
