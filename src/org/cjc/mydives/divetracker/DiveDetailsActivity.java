package org.cjc.mydives.divetracker;

import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERDATE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERTIME;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_NAME;

import org.cjc.mydives.divetracker.db.DiveDbAdapter;
import org.cjc.mydives.divetracker.db.FormatterHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class DiveDetailsActivity extends Activity {
	private DiveDbAdapter diveDbAdapter = new DiveDbAdapter(this);

	long diveId;

	private TextView tvName;
	private TextView tvDate;
	private TextView tvTime;
	
	private ImageView ivEdit;
	private ImageView ivAdd;
	
	private TextView tvTitle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dive_details);

		tvName = (TextView) this.findViewById(R.id.dive_details_name);
		tvDate = (TextView) this.findViewById(R.id.dive_details_date);
		tvTitle = (TextView) findViewById(R.id.header_title);
		tvTime = (TextView) this.findViewById(R.id.dive_details_time);
		ivEdit = (ImageView) this.findViewById(R.id.header_button_edit);
		ivAdd  = (ImageView) this.findViewById(R.id.header_button_add);
		diveId = getIntent().getExtras().getLong("_ID");

		// Title
		tvTitle.setText(R.string.dive_details_title);
		// Buttons
		ivAdd.setVisibility(View.GONE);
		ivEdit.setVisibility(View.VISIBLE);

		// Edit Dive OnClick
		ivEdit.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// Edit the new dive
				Intent i = new Intent(getBaseContext(), DiveEditActivity.class);
				i.putExtra("_ID", diveId);
				startActivity(i);
			}
		});

		populateFields();
	}
	
	private void populateFields() {

		// Get the data
		diveDbAdapter.open();	// Open the DB
		Cursor cursor = diveDbAdapter.fetchById(diveId);
		if (cursor.moveToFirst()) {
			tvTitle.setText(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
			tvName.setText(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
			tvDate.setText(FormatterHelper.db2ScrDateFormat(cursor.getString(cursor.getColumnIndex(FIELD_ENTERDATE))));
			tvTime.setText(FormatterHelper.db2ScrTimeFormat(cursor.getString(cursor.getColumnIndex(FIELD_ENTERTIME))));
		}
		
		// Close the DB
		cursor.close();
		diveDbAdapter.close();
	}

	@Override
	public void onResume() {
		populateFields();
		super.onResume();
	}
}
