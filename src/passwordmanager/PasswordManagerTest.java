package passwordmanager;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import doublehashmap.DoubleHashMap;

public class PasswordManagerTest {

	@Test 
	public void DatasetCTest() throws FileNotFoundException, IOException {
		int collisions = printHashCollisions("bin/datasetB.txt");
		System.out.println(collisions);
	}
	
	
	
	// prints collisions 
	public int printHashCollisions(String pathToFile) throws FileNotFoundException, IOException  {
		
		DoubleHashMap<Long, List<String>> map = new DoubleHashMap<Long, List<String>>(15000, 1, 50000, 56897);
        PasswordManager spm = new PasswordManager();
        
        BufferedReader br = new BufferedReader(new FileReader(pathToFile));
        
        int collisions = 0;
        
        try 
        {
            String line = br.readLine();
            //Loops through dataset, placing passwords in 'map' based on their passwordHash
            while (line != null) 
            {
                String password = line.trim();
                Long passwordHash = spm.hash(password);

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
