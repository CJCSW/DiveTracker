/**
 * 
 */
package org.cjc.mydives.divetracker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Home activity
 * @author JuanCarlos
 *
 */
public class HomeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO This is provisional. Decide later on what to display on the Home tab
		TextView textView = new TextView(this);
		textView.setText(R.string.hello);
		
		setContentView(textView);
	}
}
