package org.cjc.mydives.divetracker;

import static org.cjc.mydives.divetracker.db.UserConstants.FIELD_NAME;
import static org.cjc.mydives.divetracker.db.UserConstants.FIELD_PROFILEPIC;
import static org.cjc.mydives.divetracker.db.UserConstants.FIELD_ROWID;
import static org.cjc.mydives.divetracker.db.UserConstants.FIELD_SURNAME;

import org.cjc.mydives.divetracker.db.EquipmentConstants;
import org.cjc.mydives.divetracker.db.UserDbAdapter;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;


/**
 * This activity will display the user profile
 * along with buttons to edit it and navigate
 * to the equipment and certificates
 * @author JuanCarlos
 *
 */
public class UserDetailsActivity extends TabActivity {
	private UserDbAdapter userDbAdapter;
	
	private Long rowId;
	private TextView name;
	private TextView surname;
	private ImageView profilepic;
	private String profilepic_path;
	
	private ImageView ivEdit;
	private ImageView ivAdd;
	//JC-INI
	/*
	private Button btEquipment;
	private Button btCertifications;
	*/
	//JC-FIN
	
	private static int ACTION_EDIT = 10;
	
	/** Called when the activity is first created */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_details);
		
        name = (TextView)findViewById(R.id.user_details_field_name);
        surname = (TextView)findViewById(R.id.user_details_field_surname);
        profilepic = (ImageView)findViewById(R.id.user_details_field_profilepic);
        
        ivEdit = (ImageView) this.findViewById(R.id.header_button_edit);
		ivAdd  = (ImageView) this.findViewById(R.id.header_button_add);
		//JC-INI
		/*
		btEquipment = (Button)findViewById(R.id.user_details_button_equipment);
		btCertifications = (Button)findViewById(R.id.user_details_button_certifications);
		*/
		//JC-FIN
        
        userDbAdapter = new UserDbAdapter(this);

        // Set the rowId (in case the intent is for the edition of an existing User profile)
        rowId = null;
        Bundle extras = getIntent().getExtras();
		
		rowId = (savedInstanceState == null) ? null : (Long) savedInstanceState.getSerializable(FIELD_ROWID);
		if (extras != null) {
			rowId = extras.getLong(FIELD_ROWID); 
		}
		
		// Set header text
		((TextView) findViewById(R.id.header_title)).setText(R.string.user_details_title);
		
		// Add listeners
		addListeners();
		
		// Populate fields
		populateFields();
		
		// Set up tabbed view
		setUpTabs();
	}
	
	private void setUpTabs() {
		// Tabbed view
        Resources res = getResources();	// Resources object to get access to strings and graphic resources
        TabHost tabHost = getTabHost();	// The TabHost defined in the layout used as content view for this activity
        TabHost.TabSpec tabSpec;		// Reusable TabSpec to define each of the tabs in this activity's TabHost
        Intent tabIntent;				// Reusable Intent to launch the activity for each of the tabs
        
        // Certifications tab
        // Create an intent to launch the CertificationListActivity
        tabIntent = new Intent().setClass(this, CertificationListActivity.class);
        // Create a TabSpec for the Dives tab and add it to the TabHos
        tabSpec = tabHost.newTabSpec(res.getString(R.string.user_details_tab_tag_certifications))
        		.setIndicator(res.getString(R.string.user_details_tab_certifications), res.getDrawable(R.drawable.ic_tab_certifications))
        		.setContent(tabIntent);
        tabHost.addTab(tabSpec);        

        // Equipment tab
        // Create an intent to launch the EquipmentListActivity
        tabIntent = new Intent().setClass(this, EquipmentListActivity.class);
        tabIntent.putExtra(EquipmentConstants.FIELD_USERID, rowId);
        // Create a TabSpec for the User tab and add it to the TabHos
        tabSpec = tabHost.newTabSpec(res.getString(R.string.user_details_tab_tag_equipment))
        		.setIndicator(res.getString(R.string.user_details_tab_equipment), res.getDrawable(R.drawable.ic_tab_equipment))
        		.setContent(tabIntent);
        tabHost.addTab(tabSpec);     
        
        // Set Home as the current tab
        tabHost.setCurrentTab(0);
	}
	
	private void addListeners(){
		// Add button
		ivAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent userEditIntent = new Intent(v.getContext(), UserEditActivity.class);
				if (rowId != null) {
					userEditIntent.putExtra(FIELD_ROWID, rowId);
				}
				startActivityForResult(userEditIntent, ACTION_EDIT);
			}
		});
		
		
		// Edit button
		ivEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent userEditIntent = new Intent(v.getContext(), UserEditActivity.class);
				if (rowId != null) {
					userEditIntent.putExtra(FIELD_ROWID, rowId);
				}
				startActivityForResult(userEditIntent, ACTION_EDIT);
			}
		});
		
		//JC-INI
		/*
		// Equipment button
		btEquipment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), EquipmentListActivity.class);
				intent.putExtra(EquipmentConstants.FIELD_USERID, rowId);
				startActivity(intent);
			}
		});
		
		// Certifications button
		btCertifications.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(v.getContext(), CertificationListActivity.class));
			}
		});
		*/
		//JC-FIN
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
     		ivAdd.setVisibility(View.GONE);
     		ivEdit.setVisibility(View.VISIBLE);
     		//JC-INI
     		/*
     		btEquipment.setEnabled(true);
     		btCertifications.setEnabled(true);
     		*/
     		//JC-FIN
    	} else {
     		ivAdd.setVisibility(View.VISIBLE);
     		ivEdit.setVisibility(View.GONE);
     		//JC-INI
     		/*
     		btEquipment.setEnabled(false);
     		btCertifications.setEnabled(false);
     		*/
     		//JC-FIN
    	}
    	userCursor.close();
    	userDbAdapter.close();
    }
}
