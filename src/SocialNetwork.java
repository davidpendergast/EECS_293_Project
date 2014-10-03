//David Pendergast
//dlp75@case.edu
//SocialNetwork class.  Links between Users can be established and torn down through this class.

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class SocialNetwork {
	private Map<String,User> user_map;
	private Map<String,Link> link_map;
	
	//everytime a link is established or created, a date is added to dates_of_change.
	public Set<Date> dates_of_change;
	
	/**
	 * Creates a SocialNetwork with no users.  Throws
	 * no exceptions.
	 */
	public SocialNetwork(){
		user_map = new HashMap<String,User>();
		link_map = new HashMap<String,Link>();
		
		dates_of_change = new TreeSet<Date>();
	}
	
	/**
	 * Adds given user to the user set. Will successfully add
	 * only when user is valid, and is not already a member.
	 * @param new_user The user to be added.
	 * @return true if successful addition, false otherwise.
	 * @throws NullPointerException if passed User is null.
	 */
	public boolean addUser(User new_user){
		ErrorHandler.checkIfNull(new_user);
		//if new_user is invalid or already in the network, return false
		if(!new_user.isValid() || isMember(new_user.getID())){
			return false;
		}
		else{
			//new_user is not in network
			user_map.put(new_user.getID(),new_user);
			return true;
		}
	}
	
	/**
	 * Returns true if a user with a given id exists in the 
	 * SocialNetwork.  Returns false otherwise.
	 * @param id The id to search for.
	 * @return true if user exists, false otherwise.
	 */
	public boolean isMember(String id){
		ErrorHandler.checkIfNull(id);
		return user_map.containsKey(id);
	}
	
	/**
	 * Returns user with a given ID, if that user exists in
	 * the SocialNetwork.
	 * @param id The ID of the requested User.
	 * @return User with matching ID, or null if no such user exists.
	 * @throws NullPointerException if passed String is null.
	 */
	public User getUser(String id){
		ErrorHandler.checkIfNull(id);
		return user_map.get(id);
	}
	
	/**
	 * Creates a new Link with given date and Users with given ids, or adds a new establishing date to an already created Link.
	 * @param ids Set containing the IDs of already initialized Users in SocialNetwork.
	 * @param date Establishing Date for the Link.
	 * @param status Value of status is set to reflect success or failure of opperation.
	 */
	public void establishLink(Set<String> ids, Date date, SocialNetworkStatus status){

		addDateToEnd(ids,date,false,status);
		
		if(!status.equals(Status.INVALID_USERS)){
			return;
		}
		
		//link could not be found for some reason
		Set<User> user_set = getUserSet(ids);
		if(user_set == null){
			//users really are invalid.
			return;
		}
		
		//otherwise, link just doesn't exist yet.
		//create the new link.
		addLink(user_set,date);
		status.setValue(Status.SUCCESS);
		
		//record neighborhood change
		dates_of_change.add(date);
		
		return;
	}
	
	/**
	 * Tears down the existing Link between Users with given Ids, on given Date.
	 * @param ids The Ids of the Users in the Link.
	 * @param date The Date to tear down the Link on.
	 * @param status Value of status is set to reflect success or failure of opperation.
	 */
	public void tearDownLink(Set<String> ids, Date date, SocialNetworkStatus status){

		addDateToEnd(ids, date, true, status);
		
		if(status.equals(Status.SUCCESS)){
			dates_of_change.add(date);
		}
	
	}
	
	/**
	 * Helper method to assist establish and teardown.
	 * @param ids User ids.
	 * @param date Date to be added.
	 * @param link_should_be_active true for tearDown, false for establish.
	 * @param status Value is set to reflect success or failure of opperation.
	 */
	private void addDateToEnd(Set<String> ids, Date date, boolean link_should_be_active, SocialNetworkStatus status){
		ErrorHandler.checkIfNull(ids);
		ErrorHandler.checkIfNull(date);
		
		//getting the link between two users.
		Link link = getLinkFromStrings(ids);
		if(link == null){
			status.setValue(Status.INVALID_USERS);
			return;
		}
		
		//link exists
		try {
			link.addDateToEnd(date, status, link_should_be_active);
		
		} catch (UninitializedObjectException e) {
			//Link will always be valid at this point. 
			//This error will never fire.
			e.printStackTrace();
		}
		
		if(status.equals(Status.SUCCESS)){
			dates_of_change.add(date);
		}
	}
	
	/**
	 * Determines if Link containing Users with given Ids was active on a given Date.
	 * @param ids The Ids of the Users.
	 * @param date The Date in question.
	 * @return true if the Link was active, false if it was inactive, or link does not exist.
	 */
	public boolean isActive(Set<String> ids, Date date){
		ErrorHandler.checkIfNull(ids);
		ErrorHandler.checkIfNull(date);
		
		Link link = getLinkFromStrings(ids);
		if(link == null)
			return false;
		
		try {
			return link.isActive(date);
		} catch (UninitializedObjectException e) {
			//error will only occur if uninitialized Link is inserted into link_map, which should not be possible.
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Finds the set of all users connected by links active on passed date to the user with passed id.
	 * @param id Passed user id.
	 * @param date Passed date.
	 * @param status Will be set to reflect success or failure of the opperation.
	 * @return Set containing connected friends.
	 */
	public Set<Friend> neighborhood(String id, Date date, SocialNetworkStatus status){
	
		//returns the neighborhood with max distance equal to the number of users.
		//so it can span across every user in the network.
		return neighborhood(id, date, user_map.size(), status);
	}
	
	/**
	 * Finds the set of all users within a passed distance connected by links active on passed date to the user with passed id.
	 * @param id Passed user id.
	 * @param date Passed date.
	 * @param distance_max Maximum distance of friends in the set.
	 * @param status Will be set to reflect success or failure of the opperation.
	 * @return Set containing connected friends.
	 */
	public Set<Friend> neighborhood(String id, Date date, int distance_max, SocialNetworkStatus status){
		ErrorHandler.checkIfNull(id);
		ErrorHandler.checkIfNull(date);
		ErrorHandler.checkIfNull(status);
		
		User root_user = getUser(id);
		
		//if user is not in network, return null.
		if(root_user == null){
			status.setValue(Status.INVALID_USERS);
			return null;
		}
		
		Set<Friend> friend_set = new HashSet<Friend>();
		
		//creating a queue to hold the neighbors of vistited users.
		LinkedList<Friend> friend_queue = new LinkedList<Friend>();
		//adding root node to queue
		friend_queue.add(new Friend(root_user,0));
		
		Friend current_friend;
		
		try{
			
			going_through_the_friend_queue:
			while(!friend_queue.isEmpty()){
//				System.out.println("SocialNetwork - neighborhood : "+friend_queue);
				//taking the friend at the front of the queue
				current_friend = friend_queue.removeFirst();
				
				if(friend_set.contains(current_friend)){
					//if this friend has already been visited, skip over them
					continue going_through_the_friend_queue;
				}
				
				//otherwise, this is the first time we've encountered this friend.  
				//add them to the result set
				friend_set.add(current_friend);
				
				//if this friend is at the max distance, don't add any of it's neighbors.  
				if(current_friend.getDistance() >= distance_max){
					//just continue
					continue going_through_the_friend_queue;
				}
				
				//goes through active neighbors of current_friend, and adds them to the queue if they haven't been seen before.
				processNeighbors(current_friend, friend_set, friend_queue, date);
				
			}
			
		} catch(UninitializedObjectException e) {
			//if an error was caught here, it means something was previously added to the network incorrectly.
			//this error should never trigger.
			e.printStackTrace();	
		}
		
		status.setValue(Status.SUCCESS);
		return friend_set; 
	}
	
	/**
	 * Helper method for neighborhood methods.  Takes a friend, and adds all the active neighbors of that friend to 
	 * friend_queue if they haven't already been processed.
	 * @param current_friend Friend with neighbors to be added.
	 * @param friend_set Resultant set of friends for neighborhood().
	 * @param friend_queue Queue for the neighbors to be placed so that their neighbors will eventually be processed.
	 * @throws UninitializedObjectException
	 */
	private void processNeighbors(Friend current_friend, Set<Friend> friend_set, LinkedList<Friend> friend_queue, Date date) throws UninitializedObjectException{
		collecting_neighbors:
			for(User neighbor : current_friend.getUser().getActiveNeighbors(date)){
				//creating a new friend from this neighbor with distance incremented by 1.
				Friend new_friend = new Friend(neighbor, current_friend.getDistance() + 1);
				
				//if this new friend has already been dealt with (even at a different distance) skip over them.
				if(friend_set.contains(new_friend) || friend_queue.contains(new_friend))
					continue collecting_neighbors;
				
				//it hasn't been dealt with, so add it to friend_queue
				friend_queue.add(new_friend);
			}
	}
	
	/**
	 *  Returns a map whose keys are the dates when the user’s neighborhood 
	 *  changed and the values are the user’s neighborhood size at that time. 
	 * @param id ID of root user.
	 * @param status Status variable indicating success or failure of the opperation.
	 * @return Map with Date keys and Integer values, or null if id is not valid.
	 */
	public Map<Date,Integer> neighborhoodTrend(String id, SocialNetworkStatus status){
		ErrorHandler.checkIfNull(id);
		ErrorHandler.checkIfNull(status);
		
		User root_user = getUser(id);
		if(root_user == null){
			//if user with given id does not exist
			status.setValue(Status.INVALID_USERS);
			return null;
		}
		//otherwise, user exists in the network.
		
		Map<Date,Integer> result_map = new HashMap<Date,Integer>();
		
		//dates of all link changes in network.
		Set<Friend> prev_neighborhood = null;
		Set<Friend> curr_neighborhood = null;
		SocialNetworkStatus temp_status = new SocialNetworkStatus(Status.UNKNOWN);
		
		//go through every date in socialnetwork.  if a change in user's neighborhood
		//is found, add the new neighborhood to the result map.
		for(Date date : dates_of_change){
			
			curr_neighborhood = neighborhood(id,date,temp_status);
			
			if(!curr_neighborhood.equals(prev_neighborhood)){
				prev_neighborhood = curr_neighborhood;
				result_map.put(date,curr_neighborhood.size());
			}
		}
		
		status.setValue(Status.SUCCESS);
		return result_map;
	}
	
	/**
	 * Takes Set of strings ids_set, and if it has exactly two distict elements, will return a new Set<User> containing
	 * the two users in user_map with the associated IDs, if they exist.  Otherwise returns null. 
	 * @param ids_set The set of Strings containing the two User IDs.
	 * @return	a new Set<User> containing the users with specified IDs, or null if ids_set is invalid or at least one of the users doesn't exist.
	 */
	private Set<User> getUserSet(Set<String> ids_set){
		//if ids does not have 2 distict strings, return null
		if(!Link.isValidLinkSet(ids_set)){
			return null;
		}
		
		//fills string_array with the 2 distict elements of ids.
		String[] string_array = ids_set.toArray(new String[0]);
		
		User user_0 = user_map.get(string_array[0]);	//creates user_0, the user in user_map with the first id in ids_set.
		User user_1 = user_map.get(string_array[1]);	//creates user_1, the user in user_map with the second id in ids_set.
		
		//if either user is not in the map, return null.
		if(user_0 == null || user_1 == null){
			return null;
		}
		
		//create a Set<User>, populate it with user_0 and user_1, and return it.
		Set<User> result = new HashSet<User>();
		result.add(user_0);
		result.add(user_1);
		
		return result;
	}
	/**
	 * Private helper method used to get links out of link_map, given the Set of Users in that Link.
	 * @param users The set of Users.
	 * @return The Link if it's in the HashMap, null if it does not exist, or given Set<User> is invalid.
	 */
	private Link getLinkFromUsers(Set<User> users){
		String key = getKeyString(users);
		if(key == null){
			return null;
		}
		
		return link_map.get(key);
		
	}
	/**
	 * Private helper method used to get Link from link_map that has the Users with the given Ids.
	 * @param ids Ids of Users in requested Link.
	 * @return Link if it exists, returns null if Link does not exist, or ids is invalid.
	 */
	private Link getLinkFromStrings(Set<String> ids){
		Set<User> user_set = getUserSet(ids);
		if(user_set == null)
			return null;
		Link link = getLinkFromUsers(user_set);
		return link;
	}
	/**
	 * Private helper method used to add a new Link to link_map.  Note: This method does not prevent the creation of duplicate Links in link_map.
	 * @param users The Users to be placed in the new Link.
	 */
	private void addLink(Set<User> users, Date establishing_date){
		if(!Link.isValidLinkSet(users)){
			return;
		}
		
		String key = getKeyString(users);
		Link new_link = new Link();
		new_link.setUsers(users, new SocialNetworkStatus(Status.UNKNOWN));
		try {
			new_link.establish(establishing_date, new SocialNetworkStatus(Status.UNKNOWN));
			
			//adding the link to each user
			for(User u : users){
				u.addLink(new_link);
//				System.out.println("SocialNetwork - adding <"+new_link+"> to <"+u+">.");
			}
		} catch (UninitializedObjectException e) {
			//new_link was just initialized, so this error should not be thrown.
			e.printStackTrace();
		}
		
		link_map.put(key,new_link);
	}
	/**
	 * Private helper method that returns the key String for link_map, given a valid Set<User>.  
	 * Method is written such that the order of users in the Set does not change the return value.
	 * @param users The set to be evaluated.
	 * @return The String to be used as a key.
	 */
	private String getKeyString(Set<User> users){
		if(!Link.isValidLinkSet(users)){
			return null;
		}
		
		User[] user_array = users.toArray(new User[2]);
		
		String user0_id = user_array[0].getID();
		String user1_id = user_array[1].getID();
		
		if(user0_id.compareTo(user1_id) < 0){
			return user0_id + user1_id;
		}
		else{
			return user1_id + user0_id;
		}
	}
	
	private int getUserSize(){
		return user_map.size();
	}
	
	private int getLinkSize(){
		return link_map.values().size();
	}
}
