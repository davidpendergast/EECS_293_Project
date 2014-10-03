//David Pendergast
//dlp75@case.edu
//Wrapper class for the Status enum.  Used as a status variable for certain SocialNetwork opperations.
public class SocialNetworkStatus {
	
	Status status;
	
	/**
	 * Constructs a new SocialNetworkStatus object with value s.
	 * @param s
	 */
	public SocialNetworkStatus(Status s){
		status = s;
	}
	
	/**
	 * @return value of SocialNetworkStatus.
	 */
	public Status getValue(){
		return status;
	}
	
	/**
	 * Assigns new Status value to SocialNetworkStatus object.
	 * @param new_status
	 */
	public void setValue(Status new_status){
		status = new_status;
	}
	
	/**
	 * Takes another SocialNetworkStatus or Status, and returns true if it's own value is the same. 
	 */
	public boolean equals(Object o){
		if(o instanceof SocialNetworkStatus){
			return status == ((SocialNetworkStatus)o).status;
		}
		else if(o instanceof Status){
			return this.status == o;
		}
		else return false;
	}
}
