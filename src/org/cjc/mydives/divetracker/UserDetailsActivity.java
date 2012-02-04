package org.cjc.mydives.divetracker;

import static org.cjc.mydives.divetracker.db.UserConstants.FIELD_NAME;
import static org.cjc.mydives.divetracker.db.UserConstants.FIELD_PROFILEPIC;
import static org.cjc.mydives.divetracker.db.UserConstants.FIELD_ROWID;
import static org.cjc.mydives.divetracker.db.UserConstants.FIELD_SURNAME;

import org.cjc.mydives.divetracker.db.UserDbAdapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * This activity will display the user profile
 * along with buttons to edit it and navigate
 * to the equipment and certificates
 * @author JuanCarlos
 *
 */
public class UserDetailsActivity extends Activity {
	private UserDbAdapter userDbAdapter;
	
	private Long rowId;
	private TextView name;
	private TextView surname;
	private ImageView profilepic;
	private String profilepic_path;
	
	private static int ACTION_EDIT = 10;
	
	/** Called when the activity is first created */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_details);
		
        name = (TextView)findViewById(R.id.user_details_field_name);
        surname = (TextView)findViewById(R.id.user_details_field_surname);
        profilepic = (ImageView)findViewById(R.id.user_details_field_profilepic);
        
        userDbAdapter = new UserDbAdapter(this);

        rowId = null;
        Bundle extras = getIntent().getExtras();
		
		// Set the rowId (in case the intent is for the edition of an existing todo item)
		rowId = (savedInstanceState == null) ? null : (Long) savedInstanceState.getSerializable(FIELD_ROWID);
		if (extras != null) {
			rowId = extras.getLong(FIELD_ROWID); 
		}
		
		// Edit button
		((Button)findViewById(R.id.user_details_button_edit)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent userEditIntent = new Intent(v.getContext(), UserEditActivity.class);
				if (rowId != null) {
					userEditIntent.putExtra(FIELD_ROWID, rowId);
				}
				startActivityForResult(userEditIntent, ACTION_EDIT);
			}
		});
		
		// Equipment button
		((Button)findViewById(R.id.user_details_button_equipment)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(v.getContext(), EquipmentListActivity.class));
			}
		});
		
		// Certifications button
		((Button)findViewById(R.id.user_details_button_certifications)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(v.getContext(), CertificationListActivity.class));
			}
		});
		
		// Populate fields
		populateFields();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
    
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == ACTION_EDIT && resultCode == RESULT_OK) {
    		if (data.hasExtra(FIELD_ROWID)) {
    			rowId = data.getExtras().getLong(FIELD_ROWID);
    			populateFields();
    		}
    	}
    }
	
    private void populateFields() {
    	userDbAdapter.open();
    	Cursor userCursor = (rowId == null) ? userDbAdapter.fetchAll() : userDbAdapter.fetchById(rowId);
    	
    	if (userCursor.moveToFirst()) {
    		rowId = userCursor.getLong(userCursor.getColumnIndexOrThrow(FIELD_ROWID));
    		name.setText(userCursor.getString(userCursor.getColumnIndexOrThrow(FIELD_NAME)));
    		surname.setText(userCursor.getString(userCursor.getColumnIndexOrThrow(FIELD_SURNAME)));
    		profilepic_path = userCursor.getString(userCursor.getColumnIndexOrThrow(FIELD_PROFILEPIC));
    		if (profilepic_path != null && profilepic_path != ""){
    			Bitmap bitmap = BitmapFactory.decodeFile(profilepic_path);
    			if (bitmap != null) {
    				profilepic.setImageBitmap(bitmap);
    			}
    		}
    	}
    	userCursor.close();
    	userDbAdapter.close();
    }
}
