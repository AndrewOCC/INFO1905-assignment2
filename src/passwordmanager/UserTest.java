package passwordmanager;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {

	@Test
	public void ConstructionTest() {
		User u = new User("A");
		assertEquals("A", u.getUsername());
	}

	@Test
	public void setGetPassword(){
		/**
		 * Adds a sequence of passwords to a user and checks their
		 * values can be accessed
		 */
		User u = new User("A");
		u.setPassword("a", 1L);
		u.setPassword("b", 2L);
		u.setPassword("c", 3L);
		assertEquals(Long.valueOf(1L), u.getPassword("a"));
		assertEquals(Long.valueOf(2L), u.getPassword("b"));
		assertEquals(Long.valueOf(3L), u.getPassword("c"));
	}

	@Test
	public void MaxSizeTest(){
		/**
		 * Adds more than the maximum number of passwords to a user,
		 * ensuring the user acts appropriately
		 */
		User u = new User("A");
		u.setPassword("a", 1L);
		u.setPassword("b", 2L);
		u.setPassword("c", 3L);
		assertEquals(Long.valueOf(1L), u.getPassword("a"));
		assertEquals(Long.valueOf(2L), u.getPassword("b"));
		assertEquals(Long.valueOf(3L), u.getPassword("c"));
	}
	
	
}
