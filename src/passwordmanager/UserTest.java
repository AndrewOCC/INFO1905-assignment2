package passwordmanager;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {

	@Test
	public void testConstruction() {
		User u = new User("A");
		assertEquals("A", u.getUsername());
		
		// Test construction with a null username
		User u2 = new User(null);
		assertEquals(null, u2.getUsername());
	}

	
	@Test
	public void testSetGetPassword() {
		/**
		 * Adds a sequence of passwords to a user and checks their values can be
		 * accessed
		 */
		User u = new User("A");
		u.setPassword("a", 1L);
		u.setPassword("b", 2L);
		u.setPassword("c", 3L);
		assertEquals(Long.valueOf(1L), u.getPassword("a"));
		assertEquals(Long.valueOf(2L), u.getPassword("b"));
		assertEquals(Long.valueOf(3L), u.getPassword("c"));

		// Replacing passwords
		u.setPassword("a", 6L);
		assertEquals(Long.valueOf(6L), u.getPassword("a"));

		// Adding null values
		u.setPassword(null, 5L);

		// Getting null values
		assertEquals(null, u.getPassword(null));

	}

	@Test
	public void testMaxSize() {
		/**
		 * Adds more than the maximum number of passwords (20) to a user.
		 */
		User u = new User("A");
		for (int i = 0; i < 19; i++) 
		{
			u.setPassword(String.valueOf(i), Long.valueOf(i));
		}

		try 
		{
			u.setPassword("19", 19L);
		} 
		catch (RuntimeException e) 
		{
			if (!e.getMessage().equals("Double Hashing failed to find a free position")) 
			{
				throw e;
			}
		}
	}

}
