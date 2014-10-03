//David Pendergast
//dlp75@case.edu
//Friend class is used to hold a user and a distance.  Used by SocialNetwork class to construct neighborhoods.


public class Friend {
	
	private User user;
	private int distance;
	private boolean isValid;
	
	/**
	 * Constructs an invalid friend with no user or distance.
	 */
	public Friend(){
		isValid = false;
	}
	
	/**
	 * Constructs a new valid friend with given user and distance.
	 * @param u
	 * @param distance
	 */
	public Friend(User u, int distance){
		super();
		set(u,distance);
	}
	/**
	 * Sets the user and distance of an uninitialized friend.
	 * @param user New user.
	 * @param distance New distance.
	 * @return	true if opperation was successful, false if friend was already initialized.
	 */
	public boolean set(User user, int distance){
		ErrorHandler.checkIfNull(user);
		if(isValid){
			return false;
		}
		
		//friend is not already valid
		this.user = user;
		this.distance = distance;
		isValid = true;
		return true;
	}
	
	/**
	 * Gets this friend's user.
	 * @return That user.
	 * @throws UninitializedObjectException if friend is not initialized.
	 */
	public User getUser() throws UninitializedObjectException{
		ErrorHandler.checkIfValid(isValid);
		
		//friend is valid, so return user.
		return user;
		
	}
	
	/**
	 * Gets this friend's distance.
	 * @return That distance.
	 * @throws UninitializedObjectException if friend is not initialized.
	 */
	public int getDistance() throws UninitializedObjectException{
		ErrorHandler.checkIfValid(isValid);
		
		//friend is valid, so return distance.
		return distance;
	}
	
	/**
	 * Returns a string representation of the friend.
	 * @return That String.
	 */
	public String toString(){
		if(isValid){
			return "Valid Friend: user = <"+user+">, distance =  <"+distance+">";
		}
		else{
			return "Invalid Friend";
		}
	}
	
	public int hashCode(){
		if(isValid)
			return user.hashCode();
		else return 0;
	}
	
	/**
	 * returns true if o is a Friend with the same User and distance.
	 */
	public boolean equals(Object o){
		if(!(o instanceof Friend)){
			return false;
		}
		
		//if validities don't match, return false.
		if(this.isValid != ((Friend)o).isValid)
			return false;
		
		//if both are invalid, return true;
		if(!this.isValid && !((Friend)o).isValid){
			return true;
		}
		
		//returns true if both users are equal, and both distances.
		return this.user.equals(((Friend)o).user);
	}

}
