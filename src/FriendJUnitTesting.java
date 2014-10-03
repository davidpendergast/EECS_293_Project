import static org.junit.Assert.*;

import org.junit.Test;

public class FriendJUnitTesting {
	@Test
	public void testConstructor() throws UninitializedObjectException{
		Friend f = new Friend();
		assertEquals("Invalid Friend", f.toString());
		User u = new User();
		u.setID("User 1");
		
		f.set(u, 5);
		
		assertEquals(u,f.getUser());
		assertEquals(5,f.getDistance());
	}
	
	@Test
	public void testHashCode(){
		Friend f = new Friend();
		assertEquals(0,f.hashCode());
		User u = new User();
		u.setID("user 1");
		
		f.set(u,8);
		assertEquals(u.hashCode(),f.hashCode());
	}
	
	@Test 
	public void testEquals(){
		Friend f1 = new Friend();
		Friend f2 = new Friend();
		Friend f3 = new Friend();
		
		assertEquals(f1,f2);
		
		User u1 = new User();
		User u2 = new User();
		User u3 = new User();
		
		u1.setID("User 1");
		f1.set(u1, 5);
		
		assertFalse(f1.equals(f2));
		assertFalse(f2.equals(f1));
		
		u2.setID("User 1");
		u3.setID("User 3");
		
		f2.set(u2, 7);
		f3.set(u3, 2);
		
		assertEquals(f1,f2);
		assertEquals(f2,f1);
		
		assertFalse(f1.equals(f3));
		assertFalse(f3.equals(f1));
	}
	
}