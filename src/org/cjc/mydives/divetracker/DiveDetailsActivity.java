package org.cjc.mydives.divetracker;

import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERDATE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ENTERTIME;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_LATITUDE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_LONGITUDE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_NAME;

import java.util.List;

import org.cjc.mydives.divetracker.db.DiveDbAdapter;
import org.cjc.mydives.divetracker.db.FormatterHelper;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class DiveDetailsActivity extends MapActivity {
	private DiveDbAdapter diveDbAdapter = new DiveDbAdapter(this);

	long diveId;
	private TextView tvTitle;

	private TextView tvDate;
	private TextView tvTime;
	
	private ImageView ivEdit;
	private ImageView ivAdd;
	
	private MapView mvMap;
	private MapController mapController;
	private GeoPoint p;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dive_details);

		tvDate = (TextView) this.findViewById(R.id.dive_details_date);
		tvTitle = (TextView) findViewById(R.id.header_title);
		tvTime = (TextView) this.findViewById(R.id.dive_details_time);
		ivEdit = (ImageView) this.findViewById(R.id.header_button_edit);
		ivAdd  = (ImageView) this.findViewById(R.id.header_button_add);
		diveId = getIntent().getExtras().getLong("_ID");
		mvMap = (MapView) findViewById(R.id.dive_map);
		mapController = mvMap.getController();

		// Title
		tvTitle.setText(R.string.dive_details_title);
		// Buttons
		ivAdd.setVisibility(View.GONE);
		ivEdit.setVisibility(View.VISIBLE);

		addListeners();
		populateFields();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public void addListeners() {
		// Edit Dive OnClick
		ivEdit.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// Edit the new dive
				Intent i = new Intent(getBaseContext(), DiveEditActivity.class);
				i.putExtra("_ID", diveId);
				startActivity(i);
			}
		});
	}
	
	private void populateFields() {

		// Get the data
		diveDbAdapter.open();	// Open the DB
		Cursor cursor = diveDbAdapter.fetchById(diveId);
		if (cursor.moveToFirst()) {
			tvTitle.setText(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
			tvDate.setText(FormatterHelper.db2ScrDateFormat(cursor.getString(cursor.getColumnIndex(FIELD_ENTERDATE))));
			tvTime.setText(FormatterHelper.db2ScrTimeFormat(cursor.getString(cursor.getColumnIndex(FIELD_ENTERTIME))));
		}
		
		// GPS
		double latitude = cursor.getDouble(cursor.getColumnIndex(FIELD_LATITUDE));
		double longitude = cursor.getDouble(cursor.getColumnIndex(FIELD_LONGITUDE));

		p = new GeoPoint (
				(int) (latitude * 1E6),
				(int) (longitude * 1E6));

		mapController.animateTo(p);
		mapController.setZoom(13); // TODO: Encontrar el valor adecuado
		
		// Add the pin
		MapPinOverlay pinOverlay = new MapPinOverlay();
		List<Overlay> overlayList = mvMap.getOverlays();
		overlayList.clear();
		overlayList.add(pinOverlay);
		
		mvMap.invalidate();

		// Close the DB
		cursor.close();
		diveDbAdapter.close();
	}

	@Override
	public void onResume() {
		populateFields();
		super.onResume();
	}
	
	class MapPinOverlay extends Overlay {

		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
			super.draw(canvas, mapView, shadow);
			
			// Translate the geopoint to screen pixel
			Point screenPts = new Point();
			mapView.getProjection().toPixels(p, screenPts);
			
			// Add the marker
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pin1);
			canvas.drawBitmap(bmp, screenPts.x + 18, screenPts.y - 50, null);
			return true;
		}
	}
}
