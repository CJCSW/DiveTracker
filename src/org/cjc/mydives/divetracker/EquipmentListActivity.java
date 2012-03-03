/**
 * 
 */
package org.cjc.mydives.divetracker;

import static org.cjc.mydives.divetracker.db.EquipmentConstants.FIELD_USERID;

import org.cjc.mydives.divetracker.db.EquipmentConstants;
import org.cjc.mydives.divetracker.db.EquipmentDbAdapter;

import android.app.ListActivity;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class EquipmentListActivity extends ListActivity {
	private EquipmentDbAdapter dbAdapter;
	private Long user_id;
	private Cursor cursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.equipment_list);
		this.getListView().setDividerHeight(2);

		// Set header text
		((TextView) findViewById(R.id.header_title)).setText(R.string.user_equipment_list_title);
		
		// Get the user id (in case this list of equipment items is the one associated to the user profile)
		user_id = null;
		Bundle extras = getIntent().getExtras();
		user_id = (savedInstanceState == null) ? null : (Long)savedInstanceState.getSerializable(FIELD_USERID);
		if (user_id == null && extras != null) {
			user_id = extras.getLong(FIELD_USERID);
		}
		
		// Fill list
		dbAdapter = new EquipmentDbAdapter(this);
		dbAdapter.open();
		fillData();
	}
	
	// Life cycle method callback implementation. Called when the activity is about to be destroyed
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dbAdapter != null) {
			dbAdapter.close();
		}
	}
	
	// Activate/deactivate an item in the list
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ImageView image = (ImageView) v.findViewById(R.id.equipment_row_selected);
		Resources res = getResources();	// Resources object to get access to strings and graphic resources
		Drawable itemChecked = res.getDrawable(R.drawable.tick);
		Drawable itemNotChecked = res.getDrawable(R.drawable.cancel);
		
		String tagText = (String) image.getTag();
		
		if (tagText.equals("CHECKED")) {
			image.setImageDrawable(itemNotChecked);
			image.setTag("NOTCHECKED");
		} else {
			image.setImageDrawable(itemChecked);
			image.setTag("CHECKED");
		}
	}
	
	private void fillData() {
		cursor = dbAdapter.fecthAllByUser(user_id);
		startManagingCursor(cursor);

		String[] from = new String[]{EquipmentConstants.FIELD_NAME, EquipmentConstants.FIELD_ACTIVE};
		int[] to = new int[]{R.id.equipment_row_name, R.id.equipment_row_selected};
		
		SimpleCursorAdapter equipmentCursorAdapter = new SimpleCursorAdapter(this, R.layout.equipment_row, cursor, from, to);
		equipmentCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

			Resources res = getResources();	// Resources object to get access to strings and graphic resources
			Drawable itemChecked = res.getDrawable(R.drawable.tick);
			Drawable itemNotChecked = res.getDrawable(R.drawable.cancel);

			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if (columnIndex == cursor.getColumnIndex(EquipmentConstants.FIELD_ACTIVE)) {
					ImageView image = (ImageView) view;
					int active = cursor.getInt(cursor.getColumnIndexOrThrow(EquipmentConstants.FIELD_ACTIVE));
					if (active == 0){
						image.setImageDrawable(itemNotChecked);
						image.setTag("NOTCHECKED");
					} else {
						image.setImageDrawable(itemChecked);
						image.setTag("CHECKED");
					}
					return true;
				}
				return false;
			}
		});
		setListAdapter(equipmentCursorAdapter);
	}
}
