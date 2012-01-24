package org.cjc.mydives.divetracker;

import static org.cjc.mydives.divetracker.db.UserConstants.FIELD_NAME;
import static org.cjc.mydives.divetracker.db.UserConstants.FIELD_ROWID;
import static org.cjc.mydives.divetracker.db.UserConstants.FIELD_SURNAME;

import org.cjc.mydives.divetracker.db.UserDbAdapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * @author JuanCarlos
 *
 */
public class UserEditActivity extends Activity {
	private UserDbAdapter userDbAdapter;
	
	private Long rowId;
	private EditText name;
	private EditText surname;
	private ImageView profilepic;
	
	private boolean isCanceled;

	
	/** Called when the activity is first created */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_edit);
        
        name = (EditText)findViewById(R.id.user_edit_field_name);
        surname = (EditText)findViewById(R.id.user_edit_field_surname);
        profilepic = (ImageView)findViewById(R.id.user_edit_field_profilepic);
        
        userDbAdapter = new UserDbAdapter(this);

        rowId = null;
        Bundle extras = getIntent().getExtras();
		
		// Set the rowId (in case the intent is for the edition of an existing User)
		rowId = (savedInstanceState == null) ? null : (Long) savedInstanceState.getSerializable(FIELD_ROWID);
		if (extras != null) {
			rowId = extras.getLong(FIELD_ROWID); 
		}
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
	}
	
	@Override
	public void onPause() {
		super.onPause();
		saveState();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		isCanceled = false;
		populateFields();
	}
    
    public void populateFields() {
    	userDbAdapter.open();
    	Cursor userCursor = (rowId == null) ? userDbAdapter.fetchAll() : userDbAdapter.fetchById(rowId);
    	
    	if (userCursor.moveToFirst()) {
    		name.setText(userCursor.getString(userCursor.getColumnIndexOrThrow(FIELD_NAME)));
    		surname.setText(userCursor.getString(userCursor.getColumnIndexOrThrow(FIELD_SURNAME)));
    		// TODO : How do we set the image of an ImageView?
    		//profilepic = userCursor.getBlob(userCursor.getColumnIndexOrThrow(FIELD_PROFILEPIC));
    	}
    	userDbAdapter.close();
    }
    
    public void saveState() {
    	userDbAdapter.open();
    	if (!isCanceled) {
    		String name = this.name.getText().toString();
    		String surname = this.surname.getText().toString();
    		// TODO: How do we get the image in the ImageView?
    		//byte[] profilepic = this.profilepic.getDrawable();
    		byte[] profilepic = null;
    		if (rowId == null) {
    			long id = userDbAdapter.create(name, surname, profilepic);
    			if (id > 0) {
    				rowId = id;
    			}
    		} else {
    			userDbAdapter.update(rowId, name, surname, profilepic);
    		}
    	}
    	userDbAdapter.close();
    }
    
    public void onClick_button_confirm(View view) {
    	Intent resultData = new Intent();
    	resultData.putExtra(FIELD_ROWID, rowId);
    	setResult(RESULT_OK, resultData);
    	isCanceled = false;
    	finish();
    }
    
    public void onClick_button_cancel(View view) {
    	Intent resultData = new Intent();
    	resultData.putExtra(FIELD_ROWID, rowId);
    	setResult(RESULT_CANCELED, resultData);
    	isCanceled = true;
    	finish();
    }
}
