package org.cjc.mydives.divetracker;

import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapHelper {
	public static final int DEFAULT_ZOOM = 13;	// TODO: Encontrar el valor adecuado
	
	private MapView map;
	private MapController mapController;
	private int zoom = DEFAULT_ZOOM;
	GeoPoint p;
	private Resources res;

	public MapHelper(MapView mvMap, Resources resources) {
		map = mvMap;
		res = resources;
		mapController = map.getController();
	}
    /**
     * Set the map position to the one indicated
     * @param latitude Latitude coordinate
     * @param longitude longitude coordinate
     */
    public void setMapPosition(Double latitude, Double longitude) {
    	if (latitude == null || longitude == null) {
    		return;
    	}

		p = new GeoPoint (
				(int) (latitude * 1E6),
				(int) (longitude * 1E6));

		mapController.animateTo(p);

		// We have a position
		if (latitude != 0.0f && longitude != 0.0f) {
			mapController.setZoom(zoom);

			// Add the pin
			MapPinOverlay pinOverlay = new MapPinOverlay();
			List<Overlay> overlayList = map.getOverlays();
			overlayList.clear();
			overlayList.add(pinOverlay);
		}
		map.invalidate();    	
    }

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	class MapPinOverlay extends Overlay {

		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
			super.draw(canvas, mapView, shadow);
			
			// Translate the geopoint to screen pixel
			Point screenPts = new Point();
			mapView.getProjection().toPixels(p, screenPts);
			
			// Add the marker
			Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.pin1);
			canvas.drawBitmap(bmp, screenPts.x + 18, screenPts.y - 50, null);
			return true;
		}
	}
}
