//David Pendergast
//dlp75@case.edu
//JUnit file for testing the public methods of the User class.

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class UserJUnitTesting {
	public SocialNetworkStatus status = new SocialNetworkStatus(Status.UNKNOWN);
	
	@Test
	public void testConstructor(){
		User u = new User();
		assertNull(u.getID());		//making sure the id of an uninitialized user is null
		assertFalse(u.isValid());	//making sure an uninitialized user is not valid
	}
	@Test
	public void testSetID(){
		User u = new User();
		assertTrue(u.setID("arbitrary string"));		//setting id and initializing user
		
		assertTrue(u.isValid());						//making sure user was set to valid
		assertEquals("arbitrary string",u.getID());		//making sure id was set to given string
		
		assertFalse(u.setID("different arbitrary string"));	//making sure setID doesn't work on an already initialized User
		
		assertTrue(u.isValid());							//making sure user remains valid
		assertEquals("arbitrary string",u.getID());			//making sure id remains the same
		
	}
	
	@Test
	public void testNaming() throws UninitializedObjectException{
		User u = new User();
		u.setID("any id");
		
		assertNull(u.getFirstName());
		assertNull(u.getLastName());
		assertNull(u.getFirstName());
		assertNull(u.getMiddleName());
		assertNull(u.getEmail());
		assertNull(u.getPhoneNumber());
		
		User u2 = new User();
		u2.setID("any id");
		
		assertEquals(u2,u.setFirstName(		"first name"	));
		assertEquals(u,u.setMiddleName(		"middle name"	));
		assertEquals(u2,u.setLastName(		"last name"		));
		assertEquals(u,u.setEmail(			"email address"	));
		assertEquals(u2,u.setPhoneNumber(	"phone number"	));
		
		assertEquals("first name"	,u.getFirstName()	);
		assertEquals("middle name"	,u.getMiddleName()	);
		assertEquals("last name"	,u.getLastName()	);
		assertEquals("email address",u.getEmail()		);
		assertEquals("phone number"	,u.getPhoneNumber()	);
		
		assertEquals(u,u.setFirstName(		"new first name"	));
		assertEquals(u2,u.setMiddleName(	"new middle name"	));
		assertEquals(u,u.setLastName(		"new last name"		));
		assertEquals(u2,u.setEmail(			"new email address"	));
		assertEquals(u,u.setPhoneNumber(	"new phone number"	));
		
		assertEquals("new first name"	,u.getFirstName()	);
		assertEquals("new middle name"	,u.getMiddleName()	);
		assertEquals("new last name"	,u.getLastName()	);
		assertEquals("new email address",u.getEmail()		);
		assertEquals("new phone number"	,u.getPhoneNumber()	);
	}
	
	@Test
	public void testToString(){
		User u = new User();
		
		assertEquals("Invalid User: Uninitialized ID", u.toString());	//making sure toString returns "Invalid ... ID" on an uninitialized user
		
		u.setID("user's specific ID");	//setting user's ID
		
		try {
			u.setFirstName("david");
			u.setEmail("dlp75@case.edu");
			u.setFirstName("dave");
			u.setPhoneNumber("4405555555");
		} catch (UninitializedObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(u);
	}
	
	@Test
	public void testAddLink() throws UninitializedObjectException{
		User u1 = createUser("u1");
		User u2 = createUser("u2");
		User u3 = createUser("u3");
		
		Link l12 = new Link();
		l12.setUsers(getSet(u1,u2), status);
		Link l23 = new Link();
		l23.setUsers(getSet(u2,u3), status);
		Link l31 = new Link();
		l31.setUsers(getSet(u3,u1), status);
		
		assertTrue(u1.addLink(l12));
		assertFalse(u1.addLink(l12));
		assertFalse(u1.addLink(l23));
		
		
		
	}
	
	@Test
	public void testEquals(){
		User u1 = new User();
		User u2 = new User();
		User u3 = new User();
		User u4 = new User();
		User u5 = new User();
		User u6 = null;
		Object obj = new Object();
		
		u1.setID("ABC");
		u2.setID("ABC");
		u3.setID("DEF");
		//dont initialize u4
		//dont initialize u5
		
		assertTrue(u1.equals(u2));	//two initialized users with same ids
		assertTrue(u2.equals(u1));
		
		assertFalse(u1.equals(u3));	//two initialized users with different ids
		assertFalse(u3.equals(u1));
		
		assertFalse(u1.equals(u4));	//initialized user with uninitialized user
		assertFalse(u4.equals(u1));	
		
		assertTrue(u4.equals(u5)); //two uninitialized users
		assertTrue(u5.equals(u4));
		
		assertFalse(u1.equals(u6)); //initialized user with null
		assertFalse(u4.equals(u6)); //uninitialized user with null
		
		assertFalse(u1.equals(obj)); //initialized user with non-User object
		assertFalse(u4.equals(obj)); //uninitialized user with non-User object
	}
	
	@Test
	public void testHashCode(){
		User u = new User();
		assertEquals(0,u.hashCode());	//hashcode of uninitialized user == 0
		
		u.setID("arbitrary string");
		
		assertEquals("arbitrary string".hashCode(), u.hashCode());	//hashcode of initialized user returns hashcode of it's id
	}
	
	private Set<String> createSet(String ... id){
		Set<String> result = new HashSet<String>();
		
		for(String str : id){
			result.add(str);
		}
		
		return result;
	}
	
	private User createUser(String id){
		User result = new User();
		result.setID(id);
		return result;
	}
	
	private Set<User> getSet(User a, User b){
		Set<User> result = new HashSet<User>();
		result.add(a);
		result.add(b);
		
		return result;
	}
}
