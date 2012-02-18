package org.cjc.mydives.divetracker.entity;

public class Dive {

	private int _id;
	private String name;
	private long timeIn;
	private long timeOut;
	private double depth;
	private int tempAir;
	private int tempWater;
	private int waterType;	// 0-->FreshWater 1-->SaltWater
	private int rating;
	private double longitude;
	private double latitude;
	private double visibility;
	
	/**
	 * Default constructor.
	 */
	public Dive () {
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimeIn() {
		return timeIn;
	}

	public void setTimeIn(long timeIn) {
		this.timeIn = timeIn;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	public double getDepth() {
		return depth;
	}

	public void setDepth(double depth) {
		this.depth = depth;
	}

	public int getTempAir() {
		return tempAir;
	}

	public void setTempAir(int tempAir) {
		this.tempAir = tempAir;
	}

	public int getTempWater() {
		return tempWater;
	}

	public void setTempWater(int tempWater) {
		this.tempWater = tempWater;
	}

	public int getWaterType() {
		return waterType;
	}

	public void setWaterType(int waterType) {
		this.waterType = waterType;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getVisibility() {
		return visibility;
	}

	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}

	// COMPUTED FIELDS
	public long getDuration() {
		if (timeOut == 0 || timeIn > timeOut) {
			return 0;
		}
		return timeOut - timeIn;
	}
}
