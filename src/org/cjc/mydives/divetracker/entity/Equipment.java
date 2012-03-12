/**
 * 
 */
package org.cjc.mydives.divetracker.entity;

/**
 * Equipment entity
 * @author JuanCarlos
 *
 */
public class Equipment {
	private long _id;
	private Long user_id; // Nullable fk to User entity
	private Long dive_id; // Nullable fk to Dive entity
	private String name;
	private boolean active;
	
	/**
	 * Default constructor
	 */
	public Equipment(){
		
	}
	
	/**
	 * Constructor
	 * @param user_id Reference to the user this equipment item belongs to
	 * @param dive_id Reference to the dive this equipment item belongs to
	 * @param name Name of this equipment item
	 * @param active Whether this equipment item is active/used or not
	 */
	public Equipment(Long user_id, Long dive_id, String name, boolean active){
		this.user_id = user_id;
		this.dive_id = dive_id;
		this.name = name;
		this.active = active;
	}

	/**
	 * @return the _id
	 */
	public long get_id() {
		return _id;
	}

	/**
	 * @param _id the _id to set
	 */
	public void set_id(long _id) {
		this._id = _id;
	}

	/**
	 * @return the user_id
	 */
	public Long getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	/**
	 * @return the dive_id
	 */
	public Long getDive_id() {
		return dive_id;
	}

	/**
	 * @param dive_id the dive_id to set
	 */
	public void setDive_id(Long dive_id) {
		this.dive_id = dive_id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}
