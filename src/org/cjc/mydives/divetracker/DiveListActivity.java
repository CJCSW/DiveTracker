package org.cjc.mydives.divetracker;

import org.cjc.mydives.divetracker.db.DiveDbAdapter;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class DiveListActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dive_list);
/*
		DiveDbAdapter diveAdapter = new DiveDbAdapter(this.getBaseContext());
		if (diveAdapter.open() != null) {
			Cursor cursor = diveAdapter.fetchAll();
			if (cursor.getCount() > 0) {
//				TextView empty = this.findViewById(R.id.dive_list_tv_empty);
			}
		}
*/
	}
}
