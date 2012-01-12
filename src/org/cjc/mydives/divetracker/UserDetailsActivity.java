package org.cjc.mydives.divetracker;

import android.app.Activity;
import android.os.Bundle;

/**
 * This activity will display the user profile
 * along with buttons to edit it and navigate
 * to the equipment and certificates
 * @author JuanCarlos
 *
 */
public class UserDetailsActivity extends Activity {
	/** Called when the activity is first created */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_details);
	}
}
