package passwordmanager;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import doublehashmap.DoubleHashMap;
import skiplist.SkipList;

public class PasswordManagerTest {

	@Test 
	public void testHash() throws FileNotFoundException, IOException {
		int collisions = printHashCollisions("bin/datasetB.txt");
		System.out.println(collisions);
	}
	
	@Test
	public void testConstruction() {
		PasswordManager pm = new PasswordManager();
		assertEquals(new ArrayList<String>(), pm.listUsers());
		assertEquals(0, pm.numberUsers());
	}
	
	@Test
	public void testConstructionSize() {
		PasswordManager pm;
		pm = new PasswordManager(0);
		pm = new PasswordManager(-4);
	}
	
	@Test
	public void testListUsers() {
		PasswordManager pm = new PasswordManager();
		List<String> l = new ArrayList<String>();
		
		pm.addNewUser("A", "a");
		l.add("A");
		List<String> listedUsers = pm.listUsers();
		Collections.sort(l);
		Collections.sort(listedUsers);
		assertEquals(l, listedUsers);
		
		pm.addNewUser("B", "b");
		l.add("B");
		listedUsers = pm.listUsers();
		Collections.sort(l);
		Collections.sort(listedUsers);
		assertEquals(l, listedUsers);		
		
		pm.addNewUser("C", "c");
		l.add("C");
		listedUsers = pm.listUsers();
		Collections.sort(l);
		Collections.sort(listedUsers);
		assertEquals(l, listedUsers);		
		
		pm.addNewUser("D", "d");
		l.add("D");
		listedUsers = pm.listUsers();
		Collections.sort(l);
		Collections.sort(listedUsers);
		assertEquals(l, listedUsers);		
		
		pm.addNewUser("E", "e");
		l.add("E");
		listedUsers = pm.listUsers();
		Collections.sort(l);
		Collections.sort(listedUsers);
		assertEquals(l, listedUsers);		
		
		pm.deleteUser("A", "a");
		l.remove("A");
		listedUsers = pm.listUsers();
		Collections.sort(l);
		Collections.sort(listedUsers);
		assertEquals(l, listedUsers);		
		
		pm.deleteUser("E", "e");
		l.remove("E");
		listedUsers = pm.listUsers();
		Collections.sort(l);
		Collections.sort(listedUsers);
		assertEquals(l, listedUsers);		
		
		pm.deleteUser("D", "d");
		l.remove("D");
		listedUsers = pm.listUsers();
		Collections.sort(l);
		Collections.sort(listedUsers);
		assertEquals(l, listedUsers);		
		
	}
	
	@Test
	public void testAddNewUser() {
		PasswordManager pm = new PasswordManager();
		
		//Adding regular users ***(check passwords here?)***
		assertEquals("A", pm.addNewUser("A", "a"));
		assertEquals("B", pm.addNewUser("B", "b"));
		assertEquals("C", pm.addNewUser("C", "a"));
		assertEquals("D", pm.addNewUser("D", "c"));
		assertEquals("E", pm.addNewUser("E", "b"));
		
		//Adding users that already exist
		assertEquals("User already exists.", pm.addNewUser("A", "a"));
		
	}
	
	@Test
	public void testDeleteUser() {
		PasswordManager pm = new PasswordManager();
		List<String> l = new ArrayList<String>();
		
		// Building user list
		assertEquals("A", pm.addNewUser("A", "a"));
		l.add("A");
		assertEquals("B", pm.addNewUser("B", "b"));
		l.add("B");
		assertEquals("C", pm.addNewUser("C", "a"));
		l.add("C");
		assertEquals("D", pm.addNewUser("D", "c"));
		l.add("D");
		assertEquals("E", pm.addNewUser("E", "b"));
		l.add("E");

		// Testing deletion
		assertEquals("A", pm.deleteUser("A", "a"));
		l.remove("A");
		List<String> listedUsers = pm.listUsers();
		Collections.sort(l);
		Collections.sort(listedUsers);
		assertEquals(l, listedUsers);
		
		assertEquals("C", pm.deleteUser("C", "a"));
		l.remove("C");
		listedUsers = pm.listUsers();
		Collections.sort(l);
		Collections.sort(listedUsers);
		assertEquals(l, listedUsers);
		
		//Deleting non-existent user
		assertEquals("No such user exists.", pm.deleteUser("A", "a"));
		listedUsers = pm.listUsers();
		Collections.sort(listedUsers);
		assertEquals(l, listedUsers);
		
		//Deleting users with incorrect password
		assertEquals("Failed to authenticate user.", pm.deleteUser("D", "d"));
		listedUsers = pm.listUsers();
		Collections.sort(listedUsers);
		assertEquals(l, listedUsers);
	}
	
	@Test
	public void testAuthenticateUser() {
		PasswordManager pm = new PasswordManager();
		
		// Building user list
		pm.addNewUser("A", "a");
		pm.addNewUser("B", "b");
		pm.addNewUser("C", "a");
		pm.addNewUser("D", "c");
		pm.addNewUser("E", "b");
		
		// Testing user authentication
		assertEquals("A", pm.authenticate("A", "a"));
		assertEquals("B", pm.authenticate("B", "b"));
		assertEquals("C", pm.authenticate("C", "a"));
		assertEquals("D", pm.authenticate("D", "c"));
		assertEquals("E", pm.authenticate("E", "b"));
		
		// Authenticating a non-existent user
		assertEquals("No such user exists.", pm.authenticate("F", "f"));
		
		// Authenticating with incorrect password
		assertEquals("Failed to authenticate user.", pm.authenticate("D", "wrong"));
	}
	
	@Test
	public void testAuthenticateApp() {
		
		PasswordManager pm = new PasswordManager();
		
		// Building user list
		pm.addNewUser("A", "a");
		pm.newAppPassword("A", "a", "pw1", "app1");
		pm.newAppPassword("A", "a", "pw2", "app2");
		pm.newAppPassword("A", "a", "pw3", "app3");
		pm.newAppPassword("A", "a", "pw4", "app4");
		
		// Testing app authentication
		assertEquals("A", pm.authenticate("A", "pw1", "app1"));
		assertEquals("A", pm.authenticate("A", "pw2", "app2"));
		assertEquals("A", pm.authenticate("A", "pw3", "app3"));
		assertEquals("A", pm.authenticate("A", "pw4", "app4"));
		
		// Authenticating a non-existent user
		assertEquals("No such user exists.", pm.authenticate("B", "pw1", "app2"));
		
		// Authenticating with no password for the given app
		assertEquals("No password found.", pm.authenticate("A", "pw2", "app5"));
		
		// Authenticating with incorrect password
		assertEquals("Failed to authenticate user.", pm.authenticate("A", "wrong", "app1"));
		
	}
	
	@Test
	public void testResetUserPassword() {
		PasswordManager pm = new PasswordManager();
		
		// Building user list
		pm.addNewUser("A", "a");
		pm.addNewUser("B", "b");
		pm.addNewUser("C", "a");

		// Testing user password reset
		assertEquals("A", pm.resetPassword("A", "a", "aa"));
		assertEquals("A", pm.authenticate("A", "aa"));
		assertEquals("Failed to authenticate user.", pm.authenticate("A", "a"));
		
		assertEquals("B", pm.resetPassword("B", "b", "bb"));
		assertEquals("B", pm.authenticate("B", "bb"));
		assertEquals("Failed to authenticate user.", pm.authenticate("B", "b"));
		
		assertEquals("C", pm.resetPassword("C", "a", "cc"));
		assertEquals("C", pm.authenticate("C", "cc"));
		assertEquals("Failed to authenticate user.", pm.authenticate("C", "a"));
		
		// Resetting a non-existent user
		assertEquals("No such user exists.", pm.resetPassword("X", "x", "xx"));
		
		// Resetting with incorrect password
		assertEquals("Failed to authenticate user.", pm.resetPassword("A", "wrong", "xx"));
	}
	
	@Test
	public void testResetAppPassword() {
		
		PasswordManager pm = new PasswordManager();
		
		// Building user list
		pm.addNewUser("A", "a");
		// Building app list
		pm.newAppPassword("A", "a", "pw1", "app1");
		pm.newAppPassword("A", "a", "pw2", "app2");
		pm.newAppPassword("A", "a", "pw3", "app3");
		pm.newAppPassword("A", "a", "pw4", "app4");

		// Testing app password reset
		assertEquals("A", pm.resetPassword("A", "pw1", "pw11", "app1"));
		assertEquals("A", pm.authenticate("A", "pw11", "app1"));
		assertEquals("Failed to authenticate user.", pm.authenticate("A", "pw1", "app1"));
		
		assertEquals("A", pm.resetPassword("A", "pw2", "pw22", "app2"));
		assertEquals("A", pm.authenticate("A", "pw22", "app2"));
		assertEquals("Failed to authenticate user.", pm.authenticate("A", "pw2", "app2"));
		
		assertEquals("A", pm.resetPassword("A", "pw3", "pw33", "app3"));
		assertEquals("A", pm.authenticate("A", "pw33", "app3"));
		assertEquals("Failed to authenticate user.", pm.authenticate("A", "pw3", "app3"));
		
		// Resetting a non-existent user
		assertEquals("No such user exists.", pm.resetPassword("X", "x", "xx"));
		
		// Resetting with no password for the given app
		assertEquals("No password found.", pm.resetPassword("A", "pw2", "pw3", "app5"));
		
		// Resetting with incorrect password
		assertEquals("Failed to authenticate user.", pm.resetPassword("A", "wrong", "pw2", "app1"));
		
	}

	@Test
	public void testNewAppPassword() {
		
		PasswordManager pm = new PasswordManager();

		pm.addNewUser("A", "a");
		
		// Building app list
		assertEquals("A", pm.newAppPassword("A", "a", "pw1", "app1"));
		assertEquals("A", pm.newAppPassword("A", "a", "pw2", "app2"));
		assertEquals("A", pm.newAppPassword("A", "a", "pw3", "app3"));
		assertEquals("A", pm.newAppPassword("A", "a", "pw4", "app4"));
		
		// Adding to a non-existent user
		assertEquals("No such user exists.", pm.newAppPassword("B", "b", "pw1", "app1"));
				
		// Adding to an already created application
		assertEquals("Password already set up.", pm.newAppPassword("A", "a", "pw1", "app1"));
		
		// Adding with incorrect password
		assertEquals("Failed to authenticate user.", pm.newAppPassword("A", "b", "pw1", "app1"));
		
		// Adding with invalid app password
		//assertEquals("Password cannot be null.", pm.newAppPassword("A", "a", "pw1", null));
	}
	
	@Test
	public void testOverFlow() {
		/**
		 * Adds more than the maximum number of users to a password manager.
		 */
		PasswordManager pm = new PasswordManager(3);
		pm.addNewUser("A", "a");
		pm.addNewUser("B", "b");
		pm.addNewUser("C", "c");		
		
		try 
		{
			pm.addNewUser("D", "d");
		} 
		catch (RuntimeException e) 
		{
			if (!e.getMessage().equals("Double Hashing failed to find a free position")) 
			{
				throw e;
			}
		}
	}
	
	// prints collisions 
	public int printHashCollisions(String pathToFile) throws FileNotFoundException, IOException  {
		
		DoubleHashMap<Long, List<String>> map = new DoubleHashMap<Long, List<String>>(15000, 1, 50000, 56897);
        PasswordManager pm = new PasswordManager();
        
        BufferedReader br = new BufferedReader(new FileReader(pathToFile));
        
        int collisions = 0;
        
        try 
        {
            String line = br.readLine();
            //Loops through dataset, placing passwords in 'map' based on their passwordHash
            while (line != null) 
            {
                String password = line.trim();
                Long passwordHash = pm.hash(password);

                //Creates a new ArrayList or adds the password to an existing array.
                if (map.get(passwordHash) == null)
                {
                	map.put(passwordHash, new ArrayList<String>());
                	map.get(passwordHash).add(password);
                }
                else
                {
                	map.get(passwordHash).add(password);
                }
                line = br.readLine();
			}
		} 
        finally 
        {
            br.close();
        }  
        
        // adds up any occurring password collisions.
        List<Long> hashes = map.keys();
        for (Long hash : hashes)
        {
        	
        	List<String> passwords = map.get(hash);
        	if (passwords.size() > 1)
            {
        		collisions += passwords.size();
            }
        }
        
        // debug
        //System.out.println(hashes.size());
        
        return collisions;
        
	}

}
