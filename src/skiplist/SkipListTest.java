package skiplist;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SkipListTest
{
	@Test
	public void testConstruction()
	{
		SkipList<Integer, String> s = new SkipList<Integer, String>();
		assertEquals(0, s.size());
		assertEquals(new ArrayList<Integer>(), s.keys());
		//If the correct result (the lower leftmost node) is returned
		//it should have preceding nodes
		assertEquals(null, s.search(0).getPrev());
	}
	
	@Test
	public void testSearch()
	{
		SkipList<Integer, String> s = new SkipList<Integer, String>();
		s.put(-13, "-13");
		s.put(-11, "-11");
		s.put(-7, "-7");
		s.put(0, "0");
		s.put(2, "2");
		s.put(3, "3");
		s.put(5, "5");
		s.put(7, "7");
		//Checks nodes in the list
		assertEquals("-13", s.search(-13).getValue());
		assertEquals("-11", s.search(-11).getValue());
		assertEquals("-7", s.search(-7).getValue());
		assertEquals("0", s.search(0).getValue());
		assertEquals("2", s.search(2).getValue());
		assertEquals("3", s.search(3).getValue());
		assertEquals("5", s.search(5).getValue());
		assertEquals("7", s.search(7).getValue());
		//Checks nodes that are not in the list
		assertEquals(null, s.search(-14).getValue());
		assertEquals("-13", s.search(-12).getValue());
		assertEquals("7", s.search(8).getValue());
	}
	
	@Test
	public void testGet()
	{
		SkipList<Integer, String> s = new SkipList<Integer, String>();
		s.put(-13, "-13");
		s.put(-11, "-11");
		s.put(-7, "-7");
		s.put(0, "0");
		s.put(2, "2");
		s.put(3, "3");
		s.put(5, "5");
		s.put(7, "7");
		//Checks nodes in the list
		assertEquals("-13", s.get(-13));
		assertEquals("-11", s.get(-11));
		assertEquals("-7", s.get(-7));
		assertEquals("0", s.get(0));
		assertEquals("2", s.get(2));
		assertEquals("3", s.get(3));
		assertEquals("5", s.get(5));
		assertEquals("7", s.get(7));
		//Checks nodes that are not in the list
		assertEquals(null, s.get(-14));
		assertEquals(null, s.get(-12));
		assertEquals(null, s.get(8));
	}
	
	@Test
	public void testRemove()
	{
		SkipList<Integer, String> s = new SkipList<Integer, String>();
		s.put(-13, "-13");
		s.put(-11, "-11");
		s.put(-7, "-7");
		s.put(0, "0");
		s.put(2, "2");
		s.put(3, "3");
		s.put(5, "5");
		s.put(7, "7");
		//Checks nodes in the list
		assertEquals("-13", s.remove(-13));
		assertEquals("-11", s.remove(-11));
		assertEquals("-7", s.remove(-7));
		assertEquals("0", s.remove(0));
		assertEquals("2", s.remove(2));
		assertEquals("3", s.remove(3));
		assertEquals("5", s.remove(5));
		assertEquals("7", s.remove(7));
		//Checks a node that is not in the list
		assertEquals(null, s.remove(-14));
		//Checks a node that is no longer in the list
		assertEquals(null, s.remove(-13));
	}
	
	@Test
	public void testSize()
	{
		SkipList<Integer, String> s = new SkipList<Integer, String>();
		assertEquals(0, s.size());
		
		s.put(-13, "-13");
		assertEquals(1, s.size());
		
		s.put(-11, "-11");
		assertEquals(2, s.size());
		
		s.put(-7, "-7");
		assertEquals(3, s.size());
		
		s.put(0, "0");
		assertEquals(4, s.size());
		
		s.put(2, "2");
		assertEquals(5, s.size());
		
		s.put(3, "3");
		assertEquals(6, s.size());
		
		s.put(5, "5");
		assertEquals(7, s.size());
		
		s.put(7, "7");
		assertEquals(8, s.size());
		
		//Checks that node replacement works
		s.put(7, "SEVEN");
		assertEquals(8, s.size());
		assertEquals("SEVEN", s.get(7));
		
		//Checks that node removal works
		s.remove(3);
		assertEquals(7, s.size());
	}
	
	@Test
	public void testKeys()
	{
		SkipList<Integer, String> s = new SkipList<Integer, String>();
		List<Integer> l = new ArrayList<Integer>();
		
		s.put(3, "3");
		l.add(3);
		assertEquals(l, s.keys());
		
		s.put(0, "0");
		l.add(0, 0);
		assertEquals(l, s.keys());
		
		s.put(-11, "-11");
		l.add(0, -11);
		assertEquals(l, s.keys());
		
		s.put(-13, "-13");
		l.add(0, -13);
		assertEquals(l, s.keys());
		
		s.put(2, "2");
		l.add(3, 2);
		assertEquals(l, s.keys());
		
		s.put(5, "5");
		l.add(5, 5);
		assertEquals(l, s.keys());
		
		s.put(7, "7");
		l.add(6, 7);
		assertEquals(l, s.keys());
		
		s.put(-7, "-7");
		l.add(2, -7);
		assertEquals(l, s.keys());
	}
}
