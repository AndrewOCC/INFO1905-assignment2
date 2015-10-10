package doublehashmap;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class DoubleHashMapTest
{
	@Test
	public void testConstruction()
	{
		DoubleHashMap<String, String> h = new DoubleHashMap<String, String>(1, 1, 1);
		assertTrue(h.isEmpty());
		assertEquals(0, h.size());
		assertEquals(0, h.putCollisions());
		assertEquals(0, h.totalCollisions());
		assertEquals(0, h.maxCollisions());
		assertEquals(0, h.putFailures());
		assertEquals(new ArrayList<String>(), h.keys());
	}
	
	@Test
	public void testCollision()
	{
		DoubleHashMap<String, String> h = new DoubleHashMap<String, String>(4, 1, 2, 1);		
		/**
		 * [ ][ ][ ][ ]
		 * [ ][ ][ ][ ]
		 */
		assertEquals(0, h.putCollisions());
		assertEquals(0, h.totalCollisions());
		assertEquals(0, h.maxCollisions());
		assertEquals(0, h.putFailures());
		/**
		 * "a" has a primary hash of 1, and a secondary hash of 1
		 * 
		 *     a
		 *     A
		 *     |
		 * [ ][ ][ ][ ]
		 * [ ][ ][ ][ ]
		 * 
		 * [ ][a][ ][ ]
		 * [ ][A][ ][ ]
		 */
		h.put("a", "A");
		assertEquals(0, h.putCollisions());
		assertEquals(0, h.totalCollisions());
		assertEquals(0, h.maxCollisions());
		assertEquals(0, h.putFailures());
		/**
		 * "b" has a primary hash of 0, and a secondary hash of 1
		 * 
		 *  b
		 *  B
		 *  |
		 * [ ][a][ ][ ]
		 * [ ][A][ ][ ]
		 * 
		 * [b][a][ ][ ]
		 * [B][A][ ][ ]
		 */
		h.put("b", "B");
		assertEquals(0, h.putCollisions());
		assertEquals(0, h.totalCollisions());
		assertEquals(0, h.maxCollisions());
		assertEquals(0, h.putFailures());
		/**
		 * "c" has a primary hash of 1, and a secondary hash of 1
		 * 
		 *     c
		 *     C
		 *     |
		 * [b][a][ ][ ]
		 * [B][A][ ][ ]
		 * 
		 *        c
		 *        C
		 *        |
		 * [b][a][ ][ ]
		 * [B][A][ ][ ]
		 * 
		 * [b][a][c][ ]
		 * [B][A][C][ ]
		 */
		h.put("c", "C");
		assertEquals(1, h.putCollisions());
		assertEquals(1, h.totalCollisions());
		assertEquals(1, h.maxCollisions());
		assertEquals(0, h.putFailures());
		/**
		 * "d" has a primary hash of 0, and a secondary hash of 1
		 * 
		 *  d
		 *  D
		 *  |
		 * [b][a][c][ ]
		 * [B][A][C][ ]
		 * 
		 *     d
		 *     D
		 *     |
		 * [b][a][c][ ]
		 * [B][A][C][ ]
		 * 
		 *        d
		 *        D
		 *        |
		 * [b][a][c][ ]
		 * [B][A][C][ ]
		 * 
		 *           d
		 *           D
		 *           |
		 * [b][a][c][ ]
		 * [B][A][C][ ]
		 * 
		 * [b][a][c][d]
		 * [B][A][C][D]
		 */
		h.put("d", "D");
		assertEquals(2, h.putCollisions());
		assertEquals(4, h.totalCollisions());
		assertEquals(3, h.maxCollisions());
		assertEquals(0, h.putFailures());
		/**
		 * Probe and collisions statistics should not change
		 * when replacing the value of a key already in the array
		 */
		h.put("d", "F");
		assertEquals(2, h.putCollisions());
		assertEquals(4, h.totalCollisions());
		assertEquals(3, h.maxCollisions());
		assertEquals(0, h.putFailures());
		/**
		 * "f" has a primary hash of 0, and a secondary hash of 1
		 * 
		 *  f
		 *  A
		 *  |
		 * [b][ ][c][ ]
		 * [B][ ][C][ ]
		 * 
		 *     f
		 *     A
		 *     |
		 * [b][ ][c][ ]
		 * [B][ ][C][ ]
		 * 
		 * [b][f][c][d]
		 * [B][A][C][D]
		 */
		h.remove("a");
		h.put("f", "A");
		assertEquals(3, h.putCollisions());
		assertEquals(5, h.totalCollisions());
		assertEquals(3, h.maxCollisions());
		assertEquals(0, h.putFailures());
		/**
		 * "g" has a primary hash of 1, and a secondary hash of 1
		 * 
		 *     g
		 *     G
		 *     |
		 * [b][f][c][d]
		 * [B][A][C][D]
		 * 
		 *        g
		 *        G
		 *        |
		 * [b][f][c][d]
		 * [B][A][C][D]
		 * 
		 *           g
		 *           G
		 *           |
		 * [b][f][c][d]
		 * [B][A][C][D]
		 * 
		 *  g
		 *  G
		 *  |
		 * [b][f][c][d]
		 * [B][A][C][D]
		 * 
		 *     g
		 *     G
		 *     |
		 * [b][f][c][d]
		 * [B][A][C][D]
		 * 
		 * Exception is thrown
		 */
		try
		{
			h.put("g", "G");
		}
		catch(RuntimeException e)
		{
			if(!e.getMessage().equals("Double Hashing failed to find a free position"))
			{
				throw e;
			}
		}
		assertEquals(4, h.putCollisions());
		assertEquals(9, h.totalCollisions());
		assertEquals(4, h.maxCollisions());
		assertEquals(1, h.putFailures());
		
		/**
		 * Reset should set all statistics to 0
		 */
		h.resetStatistics();
		assertEquals(0, h.putCollisions());
		assertEquals(0, h.totalCollisions());
		assertEquals(0, h.maxCollisions());
		assertEquals(0, h.putFailures());
	}

	@Test
	public void testKeys()
	{
		DoubleHashMap<String, String> h = new DoubleHashMap<String, String>(1, 427, 647);
		List<String> keys = new ArrayList<String>();
		List<String> test = new ArrayList<String>();
		
		h.put("a", "B");
		test.add("a");
		keys = h.keys();
		Collections.sort(test);
		Collections.sort(keys);
		assertEquals(test, keys);
		
		h.put("b", "C");
		test.add("b");
		keys = h.keys();
		Collections.sort(test);
		Collections.sort(keys);
		assertEquals(test, keys);
		
		h.put("c", "D");
		test.add("c");
		keys = h.keys();
		Collections.sort(test);
		Collections.sort(keys);
		assertEquals(test, keys);
		
		h.put("d", "E");
		test.add("d");
		keys = h.keys();
		Collections.sort(test);
		Collections.sort(keys);
		assertEquals(test, keys);
		
		h.put("e", "F");
		test.add("e");
		keys = h.keys();
		Collections.sort(test);
		Collections.sort(keys);
		assertEquals(test, keys);
		
		h.put("a", "A");
		keys = h.keys();
		Collections.sort(test);
		Collections.sort(keys);
		assertEquals(test, keys);
	}
	
	@Test
	public void testPut()
	{
		DoubleHashMap<String, String> h = new DoubleHashMap<String, String>(1, 427, 647);

		h.put("a", "B");
		assertEquals(1, h.size());
		assertEquals("B", h.get("a"));
		assertEquals(null, h.get("A"));
		
		h.put("b", "C");
		assertEquals(2, h.size());
		assertEquals("C", h.get("b"));
		
		h.put("c", "D");
		assertEquals(3, h.size());
		assertEquals("D", h.get("c"));
		
		h.put("d", "E");
		assertEquals(4, h.size());
		assertEquals("E", h.get("d"));
		
		h.put("e", "F");
		h.put("E", "a");
		assertEquals(6, h.size());
		assertEquals("F", h.get("e"));
		assertEquals("a", h.get("E"));
		
		h.put("a", "C");
		assertEquals(6, h.size());
		assertEquals("C", h.get("a"));
	}
	
	@Test
	public void testRemove()
	{
		DoubleHashMap<String, String> h = new DoubleHashMap<String, String>(1, 427, 647);

		h.put("a", "B");
		h.put("b", "C");
		h.put("c", "D");
		h.put("d", "E");
		h.put("e", "F");
		h.put("E", "a");
		
		assertEquals(null, h.remove("A"));
		assertEquals(null, h.get("A"));
		assertEquals(6, h.size());
		assertEquals("B", h.remove("a"));
		assertEquals(null, h.get("a"));
		assertEquals(5, h.size());
		assertEquals("C", h.remove("b"));
		assertEquals(null, h.get("b"));
		assertEquals(4, h.size());
		assertEquals("D", h.remove("c"));
		assertEquals(null, h.get("c"));
		assertEquals(3, h.size());
		assertEquals("E", h.remove("d"));
		assertEquals(null, h.get("d"));
		assertEquals(2, h.size());
		assertEquals(null, h.remove("d"));
		assertEquals(null, h.get("d"));
		assertEquals(2, h.size());
		assertEquals("F", h.remove("e"));
		assertEquals(null, h.get("e"));
		assertEquals(1, h.size());
		assertEquals("a", h.remove("E"));
		assertEquals(null, h.get("E"));
		assertEquals(0, h.size());
	}
	
	
	@Test
	public void testExploreData() throws FileNotFoundException, IOException {
		exploreData("bin/datasetA.txt");
	}
	
	/**
	 * Inserts "datasetA.txt" into a hash map and reports the collision statistics
	 * @param pathToFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void exploreData(String pathToFile) throws FileNotFoundException, IOException {
	    
		DoubleHashMap<String, Double> h = new DoubleHashMap<String, Double>(2000, 1, 4271, 223);
	    BufferedReader br = new BufferedReader(new FileReader(pathToFile));
	    try {
	        String line = br.readLine();
	        while (line != null) {
	            String[] pieces = line.trim().split("\\s+");
	            if (pieces.length == 4){
	                 h.put(pieces[0], Double.valueOf(pieces[1]));
	                 System.out.println(pieces[0] + " --- " + pieces[1]);
	                 System.out.println(h.size());
	                 if(h.size() == 1642)
	                 {
	                	 System.out.println("about to break");
	                 }
	            }
	            line = br.readLine();
	        }
	    } finally {
	        br.close();
	    }
	    
	    System.out.println("Put Collisions: " + h.putCollisions());
	    System.out.println("Put Failures: " + h.putFailures());
	    System.out.println("Total Collisions: " + h.totalCollisions());   
        System.out.println("Max Collisions: " + h.maxCollisions());
	        
	}
	
}
