//David Pendergast
//dlp75@case.edu
//JUnit file used to test the public methods of the Link class.

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LinkJUnitTesting {
	
	private static SocialNetworkStatus status = new SocialNetworkStatus(Status.UNKNOWN);
	
	Set<User> user_set_AA;
	Set<User> user_set_AB;
	Set<User> user_set_B1A1;
	Set<User> user_set_ABC;
	Set<User> user_set_A;
	Set<User> user_set_AUi;
	Set<User> user_set_UiUi;
	Set<User> user_set_empty;
	
	Date date_1 	= new Date(10, 6, 3);
	Date date_1a 	= new Date(15, 3, 3);
	Date date_2 	= new Date(50, 7, 24);
	Date date_2a 	= new Date(50, 7, 25);
	Date date_3 	= new Date(76, 2, 8);
	Date date_3a 	= new Date(80, 6, 14);
	Date date_4 	= new Date(97, 11, 30);
	Date date_4a 	= new Date(100, 0, 1);
	Date date_5 	= new Date(114, 9, 10);
	Date date_5a 	= new Date(115, 9, 11);
	Date date_6		= new Date(134, 2, 16);
	Date date_6a	= new Date(154, 1, 7);
	
	User u_A 	= new User();
	User u_A1 	= new User();
	User u_B 	= new User();
	User u_B1 	= new User();
	User u_C 	= new User();
	User u_Ui 	= new User();
	User u_Ui1 	= new User();
	
	@Before
	public void fillSets(){
		
		u_A.setID("AAA");
		u_A1.setID("AAA");
		u_B.setID("BBB");
		u_B1.setID("BBB");
		u_C.setID("CCC");
		//dont initialize Ui or Ui1
		
		user_set_AA = getSet(u_A,u_A1);
		//.add(u_A);		//set with two identical users
//		user_set_AA.add(u_A1);
		
		user_set_AB = getSet(u_A,u_B);
//		user_set_AB.add(u_A);		//set with two distict users
//		user_set_AB.add(u_B);
		
		user_set_B1A1 = getSet(u_B1, u_A1);
//		user_set_BA1.add(u_B1);	//set with equivalent users to user_set_AB
//		user_set_BA1.add(u_A1);
		
		user_set_ABC = new HashSet<User>();
		user_set_ABC.add(u_A);	//set with 3 users
		user_set_ABC.add(u_B);
		user_set_ABC.add(u_C);
		
		user_set_A = new HashSet<User>();
		user_set_A.add(u_A);		//set with one user
		
		user_set_AUi = getSet(u_A, u_Ui);
//		user_set_AUi.add(u_A);	//set with one initialized, and one uninitialized user
//		user_set_AUi.add(u_Ui);
		
		user_set_UiUi = getSet(u_Ui, u_Ui1);	
//		user_set_UiUi.add(u_Ui);	//set with two uninitialized users
//		user_set_UiUi.add(u_Ui1);
		user_set_empty = new HashSet<User>();
		//add nothing to user_set_	//set with no users
		
	}
	
	@Test
	public void testConstructor(){
		Link l = new Link();
		assertFalse(l.isValid());	//testing to see if link is invalid
		assertEquals("Invalid Link: Uninitialized IDs", l.toString());	//makes sure toString returns "Invalid ... IDs"
	}
	
	@Test
	public void testSetUsers(){
		Link l_AA = new Link();
		Link l_AB = new Link();
		Link l_ABC = new Link();
		Link l_A = new Link();
		Link l_AUi = new Link();
		Link l_UiUi = new Link();
		Link l_empty = new Link();
		
		l_AB.setUsers(	user_set_AB, status);		//setting two distict users
			assertEquals(new SocialNetworkStatus(Status.SUCCESS), status);
		l_AUi.setUsers(	user_set_AUi, status);		//setting 1 initialized user, and one uninitialized user
			assertEquals(new SocialNetworkStatus(Status.SUCCESS), status);
		l_AA.setUsers(		user_set_AA, status);		//setting two identical users
		assertEquals(new SocialNetworkStatus(Status.INVALID_USERS), status);
		l_ABC.setUsers(		user_set_ABC, status);		//setting 3 users
		assertEquals(new SocialNetworkStatus(Status.INVALID_USERS), status);
		l_A.setUsers(		user_set_A, status);		//setting 1 user
		assertEquals(new SocialNetworkStatus(Status.INVALID_USERS), status);
		l_UiUi.setUsers(	user_set_UiUi, status);	//setting 2 uninitialized users
		assertEquals(new SocialNetworkStatus(Status.INVALID_USERS), status);
		l_empty.setUsers(	user_set_empty, status);		//setting an empty set
		assertEquals(new SocialNetworkStatus(Status.INVALID_USERS), status);
		
		assertTrue( 	l_AB.isValid());		//making sure validity of links is correct after setUsers attempts
		assertTrue(		l_AUi.isValid());
		
		assertFalse(	l_AA.isValid());	
		assertFalse(	l_ABC.isValid());
		assertFalse(	l_A.isValid());
		assertFalse(	l_UiUi.isValid());
		assertFalse(	l_empty.isValid());
		
		l_AB.setUsers(getSet(u_A,u_B), status);	//makes sure you can't set users of an already initialized Link.
		assertEquals(new SocialNetworkStatus(Status.ALREADY_VALID), status);
	}
	
	@Test (expected = UninitializedObjectException.class)
	public void testErrorGetUsers() throws UninitializedObjectException{
		Link l = new Link();
		l.getUsers();					//calling getUsers on an uninitialized link should throw error
	}
	@Test
	public void testGetUsers() throws UninitializedObjectException{
		Link l = getABLink();
		assertEquals(user_set_AB,l.getUsers());		//should return a Set equal to AB and BA1
		assertEquals(user_set_B1A1,l.getUsers());
	}
	
	@Test(expected = UninitializedObjectException.class)
	public void testErrorEstablish() throws UninitializedObjectException{
		Link l = new Link();
		l.establish(date_1, status);	//invalid link should throw exception
	}
	@Test
	public void testEstablish() throws UninitializedObjectException{
		Link l = getABLink();
		l.establish(date_3a, status);
		assertEquals(new SocialNetworkStatus(Status.SUCCESS), status);
		
		l.establish(date_1, status);	//establishing before first date
		assertEquals(new SocialNetworkStatus(Status.INVALID_DATE), status);
		l.establish(date_4, status);	//establishing an already active link
		assertEquals(new SocialNetworkStatus(Status.ALREADY_ACTIVE), status);
	}
	
	@Test(expected = UninitializedObjectException.class)
	public void testErrorTearDown() throws UninitializedObjectException{
		Link l = new Link();
		l.tearDown(date_1, status);		//tearing down an invalid link -- should throw error
	}
	@Test
	public void testTearDown() throws UninitializedObjectException{
		Link l = getABLink();
		l.tearDown(date_4, status);	//tearing down an inactive link -- shouldn't work
		assertEquals(new SocialNetworkStatus(Status.ALREADY_INACTIVE), status);
		
		l.establish(date_2, status);				//establishing on date_2					true
		
		l.tearDown(date_1, status);	//tearing down before first date			false
		assertEquals(new SocialNetworkStatus(Status.INVALID_DATE), status);
		
		l.tearDown(date_4, status);		//tearing down an active link				true
		assertEquals(new SocialNetworkStatus(Status.SUCCESS), status);
		
		l.tearDown(date_3a, status);	//tearing down before most recent teardown	false
			assertEquals(new SocialNetworkStatus(Status.INVALID_DATE), status);
		l.tearDown(date_5, status);	//tearing down an already torn down link	false
			assertEquals(new SocialNetworkStatus(Status.ALREADY_INACTIVE), status);
		
		l.establish(date_1, status);
			assertEquals(new SocialNetworkStatus(Status.INVALID_DATE), status);
		l.establish(date_2, status);
			assertEquals(new SocialNetworkStatus(Status.INVALID_DATE), status);
		l.establish(date_3a, status);
			assertEquals(new SocialNetworkStatus(Status.INVALID_DATE), status);
		
		l.establish(date_6, status);	//re-establishing after most recent tear down.
			assertEquals(new SocialNetworkStatus(Status.SUCCESS), status);
		
		l.tearDown(date_1, status);
			assertEquals(new SocialNetworkStatus(Status.INVALID_DATE), status);
		l.tearDown(date_2, status);
			assertEquals(new SocialNetworkStatus(Status.INVALID_DATE), status);
		l.tearDown(date_3a, status);
			assertEquals(new SocialNetworkStatus(Status.INVALID_DATE), status);
		l.tearDown(date_4, status);
			assertEquals(new SocialNetworkStatus(Status.INVALID_DATE), status);
		l.tearDown(date_5, status);
			assertEquals(new SocialNetworkStatus(Status.INVALID_DATE), status);
	}
	
	@Test(expected = UninitializedObjectException.class)
	public void testErrorIsActive() throws UninitializedObjectException{
		Link l = new Link();
		l.isActive(date_1);
	}
	@Test
	public void testIsActive() throws UninitializedObjectException{
		Link l = getABLink();
		assertFalse(l.isActive(date_3));	//link should be inactive before any dates are added
		
		l.establish(date_2, status);
		
		assertFalse(l.isActive(date_1));
		assertTrue(l.isActive(date_2a));
		
		l.tearDown(date_3, status);
		
		assertFalse(l.isActive(date_1));
		assertTrue(l.isActive(date_2a));
		assertFalse(l.isActive(date_3a));
		
		l.establish(date_4, status);
		
		l.tearDown(date_3, status);		//invalid tearDown
		l.establish(date_5, status);	//invalid establish
		
		assertFalse(l.isActive(date_1));
		assertTrue(l.isActive(date_2a));
		assertFalse(l.isActive(date_3a));
		assertTrue(l.isActive(date_4a));
		
		l.tearDown(date_5, status);
		l.establish(date_6, status);
		
		assertFalse(	l.isActive(date_1));		//testing all teardown/establish dates, and in between each one
		assertFalse(	l.isActive(date_1a));
		assertTrue(		l.isActive(date_2));		//established on date_2
		assertTrue(		l.isActive(date_2a));
		assertFalse(	l.isActive(date_3));		//torn down on date_3
		assertFalse(	l.isActive(date_3a));
		assertTrue(		l.isActive(date_4));		//established on date_4
		assertTrue(		l.isActive(date_4a));
		assertFalse(	l.isActive(date_5));		//torn down on date_5
		assertFalse(	l.isActive(date_5a));
		assertTrue(		l.isActive(date_6));		//established on date_6
		assertTrue(		l.isActive(date_6a));
	}
	
	@Test (expected = UninitializedObjectException.class)
	public void testErrorFirstDate() throws UninitializedObjectException{
		Link l = new Link();
		l.firstEvent();
	}
	@Test 
	public void testFirstDate() throws UninitializedObjectException{
		Link l = getABLink();
		
		assertNull(	l.firstEvent());	//link with no dates, firstDate returns null
		l.tearDown(date_2, status);				//invalid teardown
		assertNull(	l.firstEvent());	//still has no dates, should return null
		
		l.establish(date_2, status);	//establish on date_2
		
		assertEquals(date_2, l.firstEvent());	//one date in link, returns that date
		
		l.tearDown(date_3, status);		
		l.tearDown(date_3a, status);	//invalid teardown
		l.establish(date_4, status);
		l.tearDown(date_6, status);
		
		assertEquals(date_2, l.firstEvent());	//firstDate should still return date_2, even after adding more dates.
	}
	
	@Test(expected = UninitializedObjectException.class)
	public void testErrorNextEvent() throws UninitializedObjectException{
		Link l = new Link();
		l.nextEvent(date_2);
	}
	@Test
	public void testNextEvent() throws UninitializedObjectException{
		Link l = getABLink();
		
		assertNull(l.nextEvent(date_2));
		
		l.establish(date_2, status);			//establishing on date_2
		assertEquals(date_2,l.nextEvent(date_1));
		assertNull(l.nextEvent(date_2a));
		
		l.tearDown(date_3, status);				//tearing down on date_3
		assertEquals(date_2,l.nextEvent(date_1));
		assertEquals(date_3,l.nextEvent(date_2a));
		assertNull(l.nextEvent(date_3a));
		
		l.establish(date_4, status);			//establishing on date_4
		l.tearDown(date_5, status);				//tearing down on date_5
		l.establish(date_6, status);			//establishing on date_6
		
		assertEquals(date_2, l.nextEvent(date_1));
		assertEquals(date_3, l.nextEvent(date_2));
		assertEquals(date_3, l.nextEvent(date_2a));
		assertEquals(date_4, l.nextEvent(date_3));
		assertEquals(date_4, l.nextEvent(date_3a));
		assertEquals(date_5, l.nextEvent(date_4));
		assertEquals(date_5, l.nextEvent(date_4a));
		assertEquals(date_6, l.nextEvent(date_5));
		assertEquals(date_6, l.nextEvent(date_5a));
		assertNull(l.nextEvent(date_6));
		assertNull(l.nextEvent(date_6a));
	}
	
	@Test
	public void testToString(){
		Link l = new Link();
		assertEquals("Invalid Link: Uninitialized IDs", l.toString());
		l = getABLink();
	}
	
	//Private helper methods
	
	private Link getABLink(){
		Link result = new Link();
		result.setUsers(user_set_AB, status);
		return result;
	}
	
	private Set<User> getSet(User a, User b){
		Set<User> result = new HashSet<User>();
		result.add(a);
		result.add(b);
		
		return result;
	}
	
	
	

}
