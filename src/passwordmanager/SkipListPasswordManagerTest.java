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
import skiplist.SkipList;

public class SkipListPasswordManagerTest
{
	@Test 
	public void DatasetCTest() throws FileNotFoundException, IOException
	{
		List<String> names = new ArrayList<String>();
		names.add("SMITH");
		names.add("JOHNSON");
		names.add("WILLIAMS");
		names.add("BROWN");
		names.add("JONES");
		names.add("MILLER");
		names.add("DAVIS");
		
		SkipListPasswordManager p1 = stepCount("bin/datasetC.txt");
		SkipListPasswordManager p2 = stepCount("bin/datasetC.txt");
		SkipListPasswordManager p3 = stepCount("bin/datasetC.txt");
		
		for(String name : names)
		{
			System.out.print
			(
					name+
					": "+
					p1.searchSteps(name)+
					", "+
					p2.searchSteps(name)+
					", "+
					p3.searchSteps(name)+
					"\n"
			);
		}
	}
	
	// prints collisions 
	public SkipListPasswordManager stepCount(String pathToFile) throws FileNotFoundException, IOException
	{	
		SkipListPasswordManager skipList = new SkipListPasswordManager();
		BufferedReader reader = new BufferedReader(new FileReader(pathToFile));
		try 
		{
			for(String line = reader.readLine(); line != null; line = reader.readLine()) 
			{
				skipList.addNewUser(line.trim(), "");
			}
		} 
		finally 
		{
			reader.close();
		}
		return skipList;
	}
}
