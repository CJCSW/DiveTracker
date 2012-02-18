package org.cjc.mydives.divetracker;

import static org.cjc.mydives.divetracker.db.UserConstants.FIELD_NAME;
import static org.cjc.mydives.divetracker.db.UserConstants.FIELD_PROFILEPIC;
import static org.cjc.mydives.divetracker.db.UserConstants.FIELD_ROWID;
import static org.cjc.mydives.divetracker.db.UserConstants.FIELD_SURNAME;

import java.io.File;

import org.cjc.mydives.divetracker.db.UserDbAdapter;
import org.cjc.mydives.divetracker.entity.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author JuanCarlos
 *
 */
public class UserEditActivity extends Activity {
	private UserDbAdapter userDbAdapter;

	private Uri imageCaptureUri;
	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;
	
	private Long rowId;
	private User user = new User();
	
	private EditText name;
	private EditText surname;
	private ImageView profilepic;
	private String profilepic_path;

	
	/** Called when the activity is first created */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
		
		// Set header text
		((TextView) findViewById(R.id.header_title)).setText(R.string.user_edit_title);

		// Add listeners
		addListeners();
		
		((ImageView) findViewById(R.id.header_button_edit)).setVisibility(View.GONE);
		((ImageView) findViewById(R.id.header_button_add)).setVisibility(View.GONE);
		
		// Populate fields
		populateFields();
	
    }
	
	private void addListeners() {
		// Dialog associated to the profile picture selection button
		final String[] imageSelectionItems = new String[]{"From camera", "From SD Card"};
		ArrayAdapter<String> optionsAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, imageSelectionItems);
		AlertDialog.Builder optionsDialogBuilder = new AlertDialog.Builder(this);
		optionsDialogBuilder.setTitle("Select Image");
		optionsDialogBuilder.setAdapter(optionsAdapter, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (item == 0) {
					// First item selected --> From camera
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file = new File(Environment.getExternalStorageDirectory(), "DiveTrackerUserAvatar.jpg");
					imageCaptureUri = Uri.fromFile(file);
					try {
						intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageCaptureUri);
						intent.putExtra("return-data", true);
						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (Exception e) {
						e.printStackTrace();
					}
					//dialog.cancel();
				} else {
					// Second item selected --> From SD card
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
				}
				
			}
		});
		
		final AlertDialog profilePictureSelectDialog = optionsDialogBuilder.create();
		
		// Profile picture selection button
		((Button)findViewById(R.id.user_edit_button_profilepic_select)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				profilePictureSelectDialog.show();
				
			}
		});
		
		// Confirm button
		((Button)findViewById(R.id.user_edit_button_confirm)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		    	saveState();
		    	Intent resultData = new Intent();
		    	resultData.putExtra(FIELD_ROWID, rowId);
		    	setResult(RESULT_OK, resultData);
		    	finish();
			}
		});
	}
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode != RESULT_OK) return;
	   
		Bitmap bitmap = null;
		String path	= "";
		
		if (requestCode == PICK_FROM_FILE) {
			imageCaptureUri = data.getData(); 
			path = getRealPathFromURI(imageCaptureUri); //from Gallery 
		
			if (path == null)
				path = imageCaptureUri.getPath(); //from File Manager
			
			if (path != null) {
				bitmap = BitmapFactory.decodeFile(path);
				profilepic_path = path;
			}
		} else {
			File file = new File(Environment.getExternalStorageDirectory(), "DiveTrackerUserAvatar.jpg");
			imageCaptureUri = Uri.fromFile(file);
			if (imageCaptureUri != null) {
				path = imageCaptureUri.getPath();
				profilepic_path = path;
				bitmap = BitmapFactory.decodeFile(path);
			}
		}
		
		if (bitmap != null)
			profilepic.setImageBitmap(bitmap);		
    }
	
    /**
     * Transforms a URI into a path
     * @param contentUri URI to transform
     * @return Path corresponding to the URI
     */
	private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( contentUri, proj, null, null,null);
        
        if (cursor == null) return null;
        
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        
        cursor.moveToFirst();

        return cursor.getString(column_index);
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
    
    private void populateFields() {
    	userDbAdapter.open();
    	Cursor userCursor = (rowId == null) ? userDbAdapter.fetchAll() : userDbAdapter.fetchById(rowId);
    	
    	if (userCursor.moveToFirst()) {
    		user.set_id(new Long(rowId).intValue());
    		name.setText(userCursor.getString(userCursor.getColumnIndexOrThrow(FIELD_NAME)));
    		surname.setText(userCursor.getString(userCursor.getColumnIndexOrThrow(FIELD_SURNAME)));
    		profilepic_path = userCursor.getString(userCursor.getColumnIndexOrThrow(FIELD_PROFILEPIC));
    		if (profilepic_path != null && profilepic_path != "") {
    			Bitmap bitmap = BitmapFactory.decodeFile(profilepic_path);
    			if (bitmap != null) {
    				profilepic.setImageBitmap(bitmap);
    			}
    		}
    	}
    	userDbAdapter.close();
    }
    
    private void saveState() {
    	user.setName(name.getText().toString());
		user.setSurname(surname.getText().toString());
		user.setProfilepic(profilepic_path);
		
		userDbAdapter.open();
		if (user.get_id() > 0) {
			userDbAdapter.update(user);
		} else {
			rowId = userDbAdapter.create(user);
		}
    	userDbAdapter.close();
    }
    
}
