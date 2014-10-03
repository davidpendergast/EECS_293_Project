import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

//David Pendergast
//dlp75@case.edu
//User class stores a String ID.  Used by SocialNetwork with Links to create network of people.

public class User {
	private String id;
	private boolean isValid;
	
	private String first_name;
	private String middle_name;
	private String last_name;
	private String email;
	private String phone_number;
	
	private Set<Link> link_set;
	
	/**
	 * Creates an uninitialized User.
	 */
	public User(){
		isValid = false;
		id = null;
		
		link_set = new HashSet<Link>();
	}
	
	/**
	 * Initializes User with given ID.
	 * @param new_id ID for the User.
	 * @return true if User is successfully initialized, false if User is already initialized.
	 */
	public boolean setID(String new_id){
		
		ErrorHandler.checkIfNull(new_id);
		
		//if user id is already set, return false 
		if(isValid){
			return false;
		}
		else{
			//user is not valid yet
			id = new_id;
			isValid = true;
			return true;
		}
		
	}
	
	/**
	 * @return the ID of the user, or null if User is not initialized.
	 */
	public String getID(){
		return id;
	}
	
	/**
	 * @return true if User is initialized, false otherwise.
	 */
	public boolean isValid(){
		return isValid;
	}
	
	/**
	 * @return String representation of User.
	 */
	public String toString(){
		if(!isValid){
			return "Invalid User: Uninitialized ID";
		}
		else{
			//user is valid
			return "Valid User: ID = " + id	;
		}
	}
	
	/**
	 * Setter for the first name of the user.
	 * @param new_name	new first name.
	 * @return	the user.
	 * @throws UninitializedObjectException	if user is invalid.
	 */
	public User setFirstName(String new_name) throws UninitializedObjectException{
		ErrorHandler.checkIfValid(isValid);
		ErrorHandler.checkIfNull(new_name);
		first_name = new_name;
		return this;
	}
	
	/**
	 * Gets first name of user.
	 * @return	first name of user.
	 * @throws UninitializedObjectException if user is invalid.
	 */
	public String getFirstName() throws UninitializedObjectException{
		ErrorHandler.checkIfValid(isValid);
		return first_name;
	}
	
	/**
	 * Setter for the middle name of the user.
	 * @param new_name	new middle name.
	 * @return	the user.
	 * @throws UninitializedObjectException	if user is invalid.
	 */
	public User setMiddleName(String new_name) throws UninitializedObjectException{
		ErrorHandler.checkIfValid(isValid);
		ErrorHandler.checkIfNull(new_name);
		middle_name = new_name;
		return this;
	}
	/**
	 * Gets middle name of user.
	 * @return	middle name of user.
	 * @throws UninitializedObjectException if user is invalid.
	 */
	public String getMiddleName() throws UninitializedObjectException{
		ErrorHandler.checkIfValid(isValid);
		return middle_name;
	}
	
	/**
	 * Setter for the last name of the user.
	 * @param new_name	new last name.
	 * @return	the user.
	 * @throws UninitializedObjectException	if user is invalid.
	 */
	public User setLastName(String new_name) throws UninitializedObjectException{
		ErrorHandler.checkIfValid(isValid);
		ErrorHandler.checkIfNull(new_name);
		last_name = new_name;
		return this;
	}
	/**
	 * Gets last name of user.
	 * @return	last name of user.
	 * @throws UninitializedObjectException if user is invalid.
	 */
	public String getLastName() throws UninitializedObjectException{
		ErrorHandler.checkIfValid(isValid);
		return last_name;
	}
	
	/**
	 * Setter for the email of the user.
	 * @param new_name	new email.
	 * @return	the user.
	 * @throws UninitializedObjectException	if user is invalid.
	 */
	public User setEmail(String new_name) throws UninitializedObjectException{
		ErrorHandler.checkIfValid(isValid);
		ErrorHandler.checkIfNull(new_name);
		email = new_name;
		return this;
	}
	/**
	 * Gets email of user.
	 * @return	email of user.
	 * @throws UninitializedObjectException if user is invalid.
	 */
	public String getEmail() throws UninitializedObjectException{
		ErrorHandler.checkIfValid(isValid);
		return email;
	}
	
	/**
	 * Setter for the phone number of the user.
	 * @param new_name	new phone number.
	 * @return	the user.
	 * @throws UninitializedObjectException	if user is invalid.
	 */
	public User setPhoneNumber(String new_number) throws UninitializedObjectException{
		ErrorHandler.checkIfValid(isValid);
		ErrorHandler.checkIfNull(new_number);
		phone_number = new_number;
		return this;
	}
	/**
	 * Gets phone number of user.
	 * @return	phone number of user.
	 * @throws UninitializedObjectException if user is invalid.
	 */
	public String getPhoneNumber() throws UninitializedObjectException{
		ErrorHandler.checkIfValid(isValid);
		return phone_number;
	}
	
	/**
	 * Adds a link to the user's link set.  Does nothing if user is not in the link, or if the user already has a link with the other user.
	 * @param new_link The new link to be added
	 * @return True if addition was successful, otherwise false.
	 * @throws UninitializedObjectException 
	 */
	public boolean addLink(Link new_link) throws UninitializedObjectException{
		ErrorHandler.checkIfNull(new_link);
		ErrorHandler.checkIfValid(isValid);
		
		if(!new_link.isValid())
			return false;
		
		//if link doesn't contain this user
		if(!new_link.getUsers().contains(this)){
			return false;
		}
		
		//attempt to add new_link to link_set. return whether it was successful.
		return link_set.add(new_link);
		
	}
	/**
	 * @return All links user is in.
	 * @throws UninitializedObjectException 
	 */
	public Set<Link> getLinkSet() throws UninitializedObjectException{
		ErrorHandler.checkIfValid(isValid);
		return new HashSet<Link>(link_set);
	}
	
	/**
	 * 
	 * @return All the users this user is linked to.
	 * @throws UninitializedObjectException 
	 */
	public Set<User> getAllNeighbors() throws UninitializedObjectException{
		ErrorHandler.checkIfValid(isValid);
		Set<User> result = new HashSet<User>();
		
		for(Link link : link_set){
			//adds the other users from each link to result.
			result.add(link.getOtherUser(this));
		}
		
		return result;
	}
	
	/**
	 * Gets the set of active users linked to this user whose links were active on given date.
	 * @param date The date.
	 * @return The set of actively linked users
	 * @throws UninitializedObjectException
	 */
	public Set<User> getActiveNeighbors(Date date) throws UninitializedObjectException{
		ErrorHandler.checkIfValid(isValid);
		Set<User> result = new HashSet<User>();
		
		for(Link link : link_set){
			//if link was active on given date
			if(link.isActive(date)){
				//adds the other users from each link to result.
				result.add(link.getOtherUser(this));
			}
		}
		
		return result;
	}
	

	
	public boolean equals(Object obj){
		if(obj instanceof User == false){
			return false;
		}
		else{
			//obj is a User object
			User test_user = (User)obj;
			
			if(test_user.isValid() && this.isValid()){
				return test_user.getID().equals(getID());
			}
			else{
				//one of the two users is not valid.  if both IDs are null, return true, else return false.
				return test_user.getID() == getID();
			}
		}
	}
	
	public int hashCode(){
		if(isValid()){
			return id.hashCode();
		}
		else{
			//user is not valid.
			return 0;
		}
	}
}
