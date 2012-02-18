/**
 * 
 */
package org.cjc.mydives.divetracker.entity;

/**
 * User entity
 * @author JuanCarlos
 *
 */
public class User {
	private int _id;
	private String name;
	private String surname;
	private String profilepic;
	
	/**
	 * Default constructor
	 */
	public User(){
		
	}

	/**
	 * @return the _id
	 */
	public int get_id() {
		return _id;
	}

	/**
	 * @param _id the _id to set
	 */
	public void set_id(int _id) {
		this._id = _id;
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
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * @return the path to the profile picture
	 */
	public String getProfilepic() {
		return profilepic;
	}

	/**
	 * @param profilepic path to the profile picture
	 */
	public void setProfilepic(String profilepic) {
		this.profilepic = profilepic;
	}
}
