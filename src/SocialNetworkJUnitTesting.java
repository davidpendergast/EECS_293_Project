//David Pendergast
//dlp75@case.edu
//JUnit file used to test the public methods of SocialNetwork.

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkJUnitTesting {
	
	private static SocialNetworkStatus status = new SocialNetworkStatus(Status.UNKNOWN);
	
	@Test
	public void testAddUser(){
		SocialNetwork sn = new SocialNetwork();
		
		assertTrue(		sn.addUser(createUser("User ID")));	//adding new initialized user
		assertTrue(		sn.addUser(createUser("Different User ID")));	//adding new initialized user
		
		assertFalse(	sn.addUser(new User()));		//adding uninitialized user to the network
		assertFalse(	sn.addUser(createUser("User ID")));	//adding a duplicate user
		
		assertTrue(		sn.addUser(createUser("Aa")));		//adding two users with same hash code
		assertTrue(		sn.addUser(createUser("BB")));
	}
	
	@Test
	public void testIsMember(){
		SocialNetwork sn = new SocialNetwork();
		
		assertFalse(sn.isMember("User ID"));	//searching empty network
		
		sn.addUser(createUser("User ID"));
		
		assertTrue(sn.isMember("User ID"));		//searching for user that's in the network
		
		sn.addUser(createUser("Aa"));	//adding two users with same hash
		sn.addUser(createUser("BB"));
		
		assertTrue(sn.isMember("Aa"));	//finding both users
		assertTrue(sn.isMember("BB"));
		
	}
	
	@Test
	public void testGetUser(){
		SocialNetwork sn = new SocialNetwork();
		
		assertNull(sn.getUser("User ID"));	//searching empty network
		
		User user_1 = createUser("User ID");
		User user_aa = createUser("Aa");
		User user_bb = createUser("BB");
		
		sn.addUser(user_1);
		
		assertEquals(user_1, sn.getUser("User ID"));		//searching for user that's in the network
		
		sn.addUser(user_aa);	//adding two users with same hash
		sn.addUser(user_bb);
		
		assertEquals(user_aa, sn.getUser("Aa"));	//finding both users
		assertEquals(user_bb, sn.getUser("BB"));
	}
	
	@Test
	public void testEstablishLink(){
		SocialNetwork sn = new SocialNetwork();
		sn.establishLink(createSet("user 1","user 2"), new Date(1,2,3), status);	//creating a link between nonexistant users
			assertEquals(new SocialNetworkStatus(Status.INVALID_USERS),status);
		
		sn.addUser(createUser("user 1"));
		sn.establishLink(createSet("user 1","user 2"), new Date(1,2,3), status);	//creating link between 1 existing user and one nonexistant
		assertEquals(new SocialNetworkStatus(Status.INVALID_USERS),status);
		sn.establishLink(createSet("user 2","user 1"), new Date(1,2,3), status);
		assertEquals(new SocialNetworkStatus(Status.INVALID_USERS),status);
		
		sn.addUser(createUser("user 2"));
		Date link_0_date = new Date(3,5,7);
		Date link_1_date = new Date(4,5,6);
		Date link_2_date = new Date(5,5,6);
		
		sn.establishLink(createSet("user 1","user 2"), link_1_date, status);	//establishing a link on two valid users
		assertEquals(new SocialNetworkStatus(Status.SUCCESS),status);
		
		sn.establishLink(createSet("user 1","user 2"), link_2_date, status);	//re-establishing an active link
		assertEquals(new SocialNetworkStatus(Status.ALREADY_ACTIVE),status);
		sn.establishLink(createSet("user 1","user 2"), link_0_date, status);
		assertEquals(new SocialNetworkStatus(Status.INVALID_DATE),status);
		
		sn.addUser(createUser("user 3"));
		
		sn.establishLink(createSet("user 1","user 2","user 3"), link_0_date, status);	//link with 3 users
		assertEquals(new SocialNetworkStatus(Status.INVALID_USERS),status);
		sn.establishLink(createSet("user 1"), link_0_date, status);					//link with 1 user
		assertEquals(new SocialNetworkStatus(Status.INVALID_USERS),status);
		
	}
	
	@Test 
	public void testTearDownLink(){
		SocialNetwork sn = new SocialNetwork();
		
		Date date_1 = new Date(1,0,0);
		Date date_2 = new Date(2,0,0);
		Date date_3 = new Date(3,0,0);
		Date date_4 = new Date(4,6,7);
		Date date_5 = new Date(5,6,7);
		
		sn.tearDownLink(createSet("user 1","user 2"), date_1, status);	//nonexistant users
		assertEquals(new SocialNetworkStatus(Status.INVALID_USERS),status);
		
		sn.addUser(createUser("user 1"));
		sn.addUser(createUser("user 2"));
		
		sn.tearDownLink(createSet("user 1","user 2"), date_1, status);	//tearing down a non-existant link with valid users
		assertEquals(new SocialNetworkStatus(Status.INVALID_USERS),status);
		
		sn.establishLink(createSet("user 1","user 2"), date_2, status);		//creating valid link
		
		sn.tearDownLink(createSet("user 1","user 2"), date_1, status); //attempting to tear down an active link on a Date which is too early
		assertEquals(new SocialNetworkStatus(Status.INVALID_DATE),status);
		sn.tearDownLink(createSet("user 2","user 1"), date_3, status);	//tearing down link on an appropriate date
		assertEquals(new SocialNetworkStatus(Status.SUCCESS),status);
		
		sn.establishLink(createSet("user 1","user 2"), date_4, status);		//re-establishing link
		
		sn.tearDownLink(createSet("user 1","user 2"), date_5, status);	//tearing down link again
		assertEquals(new SocialNetworkStatus(Status.SUCCESS),status);
		
	}
	
	@Test
	public void testIsActive(){
		SocialNetwork sn = new SocialNetwork();
		
		Date date_1 	= new Date(1,0,0);
		Date date_1a 	= new Date(1,5,0);
		Date date_2 	= new Date(2,0,0);
		Date date_2a 	= new Date(2,5,0);
		Date date_3 	= new Date(3,0,0);
		Date date_3a 	= new Date(3,2,0);
		Date date_4 	= new Date(4,2,7);
		Date date_4a 	= new Date(4,6,0);
		Date date_5 	= new Date(5,6,7);
		Date date_5a 	= new Date(5,11,0);
		
		assertFalse(sn.isActive(createSet("user 1","user 2"),date_1));	//nonexistant users
		
		sn.addUser(createUser("user 1"));
		sn.addUser(createUser("user 2"));
		
		sn.establishLink(createSet("user 1","user 2"), date_2, status);		//creating valid link
		
		assertFalse(sn.isActive(createSet("user 1","user 2"),date_1));	//checking before link establishment
		assertTrue(sn.isActive(createSet("user 1","user 2"),date_2a));	//checking after link establishment
		
		sn.tearDownLink(createSet("user 1","user 2"), date_3, status); 
		
		assertFalse(sn.isActive(createSet("user 2","user 1"),date_1));	//checking before link establishment
		assertTrue(sn.isActive(createSet("user 1","user 2"),date_2a));	//checking between estab and tear
		assertFalse(sn.isActive(createSet("user 2","user 1"),date_3a));	//checking after final tear
		
		sn.establishLink(createSet("user 1","user 2"), date_4, status);
		sn.tearDownLink(createSet("user 1","user 2"), date_5, status); 
		
		assertFalse(sn.isActive(	createSet("user 2","user 1"),date_1));	
		assertFalse(sn.isActive(	createSet("user 1","user 2"),date_1a));	
		assertTrue(sn.isActive(		createSet("user 2","user 1"),date_2));	//establishing link
		assertTrue(sn.isActive(		createSet("user 2","user 1"),date_2a));
		assertFalse(sn.isActive(	createSet("user 2","user 1"),date_3));	//tear down
		assertFalse(sn.isActive(	createSet("user 1","user 2"),date_3a));	
		assertTrue(sn.isActive(		createSet("user 2","user 1"),date_4));	//establish
		assertTrue(sn.isActive(		createSet("user 1","user 2"),date_4a));
		assertFalse(sn.isActive(	createSet("user 2","user 1"),date_5));	//tear down
		assertFalse(sn.isActive(	createSet("user 2","user 1"),date_5a));
		
	}
	
	@Test
	public void testNeighborhood(){
		SocialNetwork sn = new SocialNetwork();
		Date pre_date = new Date(100, 4, 3);
		Date mid_date = new Date(104, 3, 2);
		Date aft_date = new Date(106, 3, 2);
		Date date = new Date(-5000);
		
		User[] user_array = new User[10];
		Friend[] friend_array = new Friend[10];
		
		for(int i = 0; i < user_array.length; i++){
			user_array[i] = createUser("User "+i);
			sn.addUser(user_array[i]);
		}
		
		friend_array[5] = new Friend(createUser("User 5"), 0);
		
		assertEquals(createSet(friend_array[5]), sn.neighborhood("User 5", mid_date, status));	//testing user with no links
		assertEquals(createSet(friend_array[5]), sn.neighborhood("User 5", mid_date, 5, status));
		
		sn.establishLink(createSet("User 1", "User 3"), mid_date, status);	//setting up a link between two users
		friend_array[1] = new Friend(createUser("User 1"), 1);
		friend_array[3] = new Friend(user_array[3], 0);
		
		assertEquals(createSet(friend_array[1], friend_array[3]), sn.neighborhood("User 3", aft_date, status));	
		assertEquals(createSet(friend_array[3]), sn.neighborhood("User 3", pre_date, status));	//inactive dated link not included
		
		//setting a bunch of links
		int[] new_links = {1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9};
		for(int i = 0; i < new_links.length-1; i += 2){
			
			sn.establishLink(createSet("User "+new_links[i], "User "+new_links[i+1]), mid_date, status);	//setting up a link between two users
	//		System.out.println(new_links[i] + " "+new_links[i+1]);
		}
		
		friend_array[0] = new Friend(createUser("User 2"), 0);
		friend_array[1] = new Friend(createUser("User 8"), 6);
		friend_array[2] = new Friend(createUser("User 7"), 5);
		friend_array[3] = new Friend(createUser("User 6"), 4);
		friend_array[4] = new Friend(createUser("User 5"), 3);
		friend_array[5] = new Friend(createUser("User 4"), 2);
		friend_array[6] = new Friend(createUser("User 3"), 1);
		friend_array[7] = new Friend(createUser("User 1"), 1);
		friend_array[8] = new Friend(createUser("User 9"), 7);
		
		Set<Friend> friend_set1 = new HashSet<Friend>();
		for(int i = 0; i < 9; i++){
			friend_set1.add(friend_array[i]);
		}
		
//		System.out.println(getString(friend_set1));
//		System.out.println("exp:	"+getString(sn.neighborhood("User 2", aft_date, status)));
		
		assertEquals(friend_set1,sn.neighborhood("User 2", aft_date, status));
	}
	
	@Test
	public void testNeighborhoodTrend(){
		SocialNetwork sn = new SocialNetwork();
		sn.addUser(createUser("User 1"));
		sn.addUser(createUser("User 2"));
		sn.addUser(createUser("User 3"));
		sn.addUser(createUser("User 4"));
		
		Date date_1 = new Date(101, 1 ,2);
		Date date_2 = new Date(102, 2, 5);
		Date date_3 = new Date(103, 3 ,2);
		Date date_4 = new Date(104, 4, 5);
		
		Map<Date,Integer> result_map = new HashMap<Date,Integer>();
		
		assertEquals(result_map,sn.neighborhoodTrend("User 1", status)); //user with no links gives empty map
		
		sn.establishLink(createSet("User 1","User 2"), date_1, status);	//establishing and tearing down on various dates
		sn.establishLink(createSet("User 2","User 3"), date_2, status);
		sn.tearDownLink(createSet("User 1","User 2"), date_4, status);
		sn.establishLink(createSet("User 4","User 2"),date_3,status);
		
		result_map.put(date_1, 2);	//populating what the neighborhood trend should be
		result_map.put(date_2, 3);
		result_map.put(date_3, 4);
		result_map.put(date_4, 1);
		
		assertEquals(result_map,sn.neighborhoodTrend("User 1", status));

	}
	
	//Private helper methods
	
	private Set<String> createSet(String ... id){
		Set<String> result = new HashSet<String>();
		
		for(String str : id){
			result.add(str);
		}
		
		return result;
	}
	
	private Set<Friend> createSet(Friend ... fr){
		Set<Friend> result = new HashSet<Friend>();
		
		for(Friend friend : fr){
			result.add(friend);
		}
		
		return result;
	}
	
	private String getString(Set set){
		StringBuilder result = new StringBuilder();
		result.append("[");
		for(Object o : set){
			result.append("("+o.toString() +"), ");
		}
		result.append("]");
		
		return result.toString();
	}
	
	private User createUser(String id){
		User result = new User();
		result.setID(id);
		return result;
	}
}
	