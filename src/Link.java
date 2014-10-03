//David Pendergast
//dlp75@case.edu
//Link class used by SocialNetwork to 'link' two Users together. 

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class Link {
	private boolean isValid;
	
	private Set<User> user_set;
	private static int number_of_users = 2;

	private ArrayList<Date> date_array;
	
	/**
	 * Creates a new, invalid link with no users or dates.
	 */
	public Link(){
		isValid = false;
		date_array = new ArrayList<Date>();
	}
	
	/**
	 * Sets the Users for an uninitiated Link, and initiates it.  
	 * If the Link was already initialized, or new_users is not valid, this method does nothing.
	 * @param new_users The new Users for the Link.
	 * @param status Value of status is set to reflect success or failure of opperation.
	 */
	public void setUsers(Set<User> new_users, SocialNetworkStatus status){
		ErrorHandler.checkIfNull(new_users);
		
		//if new_users is not valid
		if(!isValidLinkSet(new_users)){
			status.setValue(Status.INVALID_USERS);
			return;
		}
		
		//if Link is invalid
		if(!isValid()){
			//creates and populates user_array with the contents of (Set<User>)users. 
			user_set = new HashSet<User>(new_users);
			isValid = true;
			status.setValue(Status.SUCCESS);
			return;
		}
		else{
			//link was already valid
			status.setValue(Status.ALREADY_VALID);
			return;
		}
	}
	
	/**
	 * 
	 * @return All establish and teardown links in the link.
	 */
	public ArrayList<Date> getDates(){
		return new ArrayList<Date>(date_array);
	}
	
	/**
	 * @return true if link is valid, otherwise false.
	 */
	public boolean isValid(){
		return isValid;
	}
	
	/**
	 * @return A Set<User> containing the users of the link
	 * @throws UninitializedObjectException
	 */
	public Set<User> getUsers() throws UninitializedObjectException{
		ErrorHandler.checkIfValid(isValid, "Cannot call getUsers() on an uninitialized Link.");

		return new HashSet<User>(user_set);
	}
	
	/**
	 * Establishes the Link on a given Date, if the Link is inactive and Date is valid.
	 * @param new_date Passed in Date parameter.  Must be after all other dates the Link contains.
	 * @param status Value of status is set to reflect success or failure of opperation.
	 * @throws UninitializedObjectException if Link is not initialized.
	 */
	public void establish(Date new_date, SocialNetworkStatus status) throws UninitializedObjectException{

		ErrorHandler.checkIfValid(isValid, "Cannot call establish(Date) on an uninitialized Link.");
		
		addDateToEnd(new_date,status,false);

	}
	
	/**
	 * Deactivates Link at a given Date.
	 * @param new_date Date to deactivate on.  Must be after all other Dates the Link has.  Link must also be active.
	 * @param status Value of status is set to reflect success or failure of opperation.
	 * @throws UninitializedObjectException if Link is not initialized.
	 */
	public void tearDown(Date new_date, SocialNetworkStatus status) throws UninitializedObjectException{
		
		ErrorHandler.checkIfValid(isValid, "Cannot call tearDown(Date) on an uninitialized Link.");
		
		addDateToEnd(new_date,status,true);

	}
	
	/**
	 * Helper method to add date to the end of Link, either activating it or deactivating it.
	 * @param new_date New date to be added.
	 * @param status Value set to reflect success/failure of opperation.
	 * @param link_should_be_active True if tearing down link, false if establishing link.
	 * @throws UninitializedObjectException
	 */
	public void addDateToEnd(Date new_date, SocialNetworkStatus status, boolean link_should_be_active) throws UninitializedObjectException{
		ErrorHandler.checkIfValid(isValid, "Cannot call tearDown(Date) on an uninitialized Link.");
		ErrorHandler.checkIfNull(new_date);
		
		if(nextEvent(new_date) != null ){
			status.setValue(Status.INVALID_DATE);
			return;
		}
		
		//if link is correctly active or inactive, as described by link_should_be_active
		if(isActive(new_date) == link_should_be_active ){
			date_array.add(new_date);
			status.setValue(Status.SUCCESS);
			return;
		}
		
		//date is not valid, decide correct error for status
		status.setValue(isActive(new_date) ? Status.ALREADY_ACTIVE : Status.ALREADY_INACTIVE);	
		
	}
	
	/**
	 * Determines whether the Link was active on a given Date
	 * @param new_date The passed Date.
	 * @return true if the Link was active, otherwise false
	 * @throws UninitializedObjectException If Link is not initialized.
	 */
	public boolean isActive(Date new_date) throws UninitializedObjectException{
		ErrorHandler.checkIfNull(new_date);
		ErrorHandler.checkIfValid(isValid, "Cannot call isActive(Date) on an uninitialized Link.");
		
		//if there are no dates yet, link is never active
		if(date_array.size() == 0){
			return false;
		}
		
		//if new_date is before earliest date in date_array, link was inactive at new_date
		if(new_date.before(date_array.get(0))){
			return false;
		}
		
		//if new_date comes after latest date, link is active if there are an odd number of dates in date_array, otherwise inactive.
		if(!new_date.before(date_array.get(date_array.size() - 1))){
			return (date_array.size() % 2 == 1);
		}
		
		//gets index of Date in date_array that follows new_date.  
		//Because of previous checks, index must be 1 <= index <= date_array.size - 1.
		int index = getIndexOfDateAfter(new_date);
		//backs up to the Date immediately before new_date
		index--;
		//if the Date immediately before new_date has an even index, the Link was active, otherwise it was inactive.
		return (index % 2 == 0);
	
	}
	
	/**
	 * Gets the first Date of the Link.
	 * @return Earliest Date of the Link, or null if the Link has no dates.
	 * @throws UninitializedObjectException
	 */
	public Date firstEvent() throws UninitializedObjectException{

		ErrorHandler.checkIfValid(isValid, "Cannot call firstEvent() on an uninitialized Link.");
		
		if(date_array.size() == 0){
			return null;
		}
		else{ 
			//date_array is non-empty
			return date_array.get(0);
		}
	}
	
	/**
	 * Finds and returns the Date directly after param date if it exists.
	 * @param date Method returns the Date directly after this passed value.
	 * @return Date after passed date, or null if it doesn't exist.
	 * @throws UninitializedObjectException 
	 */
	public Date nextEvent(Date new_date) throws UninitializedObjectException{
		
		ErrorHandler.checkIfValid(isValid, "Cannot call nextEvent() on an uninitialized Link.");
		ErrorHandler.checkIfNull(new_date);
		
		//if there are no dates, there is no next date.
		if(date_array.size() == 0)
			return null;
		
		int index = getIndexOfDateAfter(new_date);
		
		if(index == -1){
			//no Date after new_date exists in date_array
			return null;
		}
		else{
			//a date was found
			return date_array.get(index);
		}
	}
	
	/**
	 * Returns a string representation of the Link.
	 * @return the string.
	 */
	public String toString(){
		if(isValid){
			String result = "Link between users:\t";
			for(User u : user_set){
				result += u.toString() + ", ";
			}
			return result;
		}
		else{
			//link is not valid
			return "Invalid Link: Uninitialized IDs";
		}
	}
	
	/**
	 * Private static helper method used to determine if a Set<String> has
	 * two distinct elements.  
	 * @param set is the set to be tested.  Can be parameterized with anything
	 * @return true if set contains two distinct elements, false otherwise
	 */
	public static boolean isValidLinkSet(Set<?> set){
		if(set.size() != number_of_users){
			return false;
		}	
		//no need to check for duplicate entries in set becuase Sets prohibit duplicate entries.	
		return true;
	}
	
	/**
	 * Private helper method that returns the index of the Date in date_array immediately after given Date.
	 * @param new_date Passed Date object.
	 * @return Index of found Date, or -1 if no such date exists.
	 */
	private int getIndexOfDateAfter(Date new_date){
		ErrorHandler.checkIfNull(new_date);
		
		//if there are no dates, there is no next date.
		if(date_array.size() == 0)
			return -1;
		
		//iterates through date_array, if a Date after param date is found, returns that date.
		for(int i = 0; i < date_array.size(); i++){
			if(new_date.before(date_array.get(i))){
				return i;
			}
		}
		
		//no Date in date_array after date exists
		return -1;
	}
	
	/**
	 * Gives the other user in the link
	 * @param user_1 The known user.
	 * @return The unknown user.
	 */
	public User getOtherUser(User user_1){
		//if the link doesn't have given user, return null.
		if(!user_set.contains(user_1)){
			return null;
		}
		
		User[] user_array = user_set.toArray(new User[2]);
		
		//returns the user in the set that does not equal user_1.
		return user_array[0].equals(user_1) ? user_array[1] : user_array[0];
	}
}
