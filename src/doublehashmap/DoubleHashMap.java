package doublehashmap;

import java.util.ArrayList;
import java.util.List;

public class DoubleHashMap<K extends Comparable<K>, V>
{
	private HashMapNode<K, V>[] array;
	
	//Parameters
	private int multiplier;
	private int modulus;
	private int secondaryModulus;
	
	//Collision statistics
	private int collisions;
	private int probes;
	private int maxProbes;
	private int failures;
	
	//Sentinel value that removed values are replaced with
	private HashMapNode<K, V> SENTINEL;
	{
		SENTINEL = new HashMapNode<K, V>(null, null);
	}
	
	//Construct a DoubleHashMap with 4000 places and given hash parameters
	@SuppressWarnings("unchecked")
	public DoubleHashMap(int multiplier, int modulus, int secondaryModulus)
	{
		this.array = (HashMapNode<K, V>[]) new HashMapNode[4000];
		this.multiplier = multiplier;
		this.modulus = modulus;
		this.secondaryModulus = secondaryModulus;
		this.collisions = 0;
		this.probes = 0;
		this.maxProbes = 0;
		this.failures = 0;
	}
	
	//Construct a DoubleHashMap with given capacity and given hash parameters
	@SuppressWarnings("unchecked")
	public DoubleHashMap(int hashMapSize, int multiplier, int modulus, int secondaryModulus)
	{
		this.array = (HashMapNode<K, V>[]) new HashMapNode[hashMapSize];
		this.multiplier = multiplier;
		this.modulus = modulus;
		this.secondaryModulus = secondaryModulus;
		this.collisions = 0;
		this.probes = 0;
		this.maxProbes = 0;
	}

	//Returns the primary hash of the given key
	public int hash(K key)
	{
		return Math.abs(this.multiplier * key.hashCode()) % this.modulus % this.array.length;
	}
	
	//Returns the secondary hash of the given key
	public int secondaryHash(K key)
	{
		return (this.secondaryModulus - (Math.abs(key.hashCode()) % this.secondaryModulus)) % this.array.length;
	}
	
	//Return the number of stored values
	public int size()
	{
		int count = 0;
		//Counts the number of nodes that are not empty and which do not contain sentinels
		for(HashMapNode<K, V> node : this.array)
		{
			if(node != null && node != this.SENTINEL)
			{
				count++;
			}
		}
		return count;
	}
	
	//Returns whether or not the hashmap is empty
	public boolean isEmpty()
	{
		//The hashmap is empty if it has a size of 0
		return (this.size() == 0);
	}
	
	//Returns a list of keys
	public List<K> keys()
	{
		List<K> keys = new ArrayList<K>();
		//Adds each node that is not empty and which does not contain a sentinel
		for(HashMapNode<K, V> node : this.array)
		{
			if(node != null && node != this.SENTINEL)
			{
				keys.add(node.getKey());
			}
		}
		return keys;
	}
	
	//Updates the three collision statistics given the number of failed probes
	private void updateStatistics(int tries)
	{
		//Updates the maximum probe count if the current failed probe count exceeds it
		if(tries > this.maxProbes)
		{
			this.maxProbes = tries;
		}
		//Add the current failed probe count to the total probe count
		this.probes += tries;
		//Increments collisions if there have been failed probes
		this.collisions += Integer.signum(tries);
	}
	
	//Inserts a node, returns the previous value stored in the key (if any)
	public V put(K key, V value)
	{
		//Calculates the primary and secondary hash values
		int index = this.hash(key);
		int increment = this.secondaryHash(key);
		int i = index;
		int tries = 0;
		V result = null;
		//Goes through the elements, starts at the primary hash, increments by secondary hash
		//Loops back to the beginning
		//Terminates when it reaches the primary hash again
		while(true)
		{
			//If the current element is empty, stores a new node with the specified parameters
			if(this.array[i] == null || this.array[i] == this.SENTINEL)
			{
				this.array[i] = new HashMapNode<K, V>(key, value);
				break;
			}
			//If the current element has the same key, overwrite its value and store it
			else if(key.equals(this.array[i].getKey()))
			{
				result = this.array[i].getValue();
				this.array[i].setValue(value);
				tries = 0;
				break;
			}
			tries++;
			i = (i + increment) % this.array.length;
			//If it reaches the beginning again, the put has failed
			if(i == index)
			{
				this.failures++;
				this.updateStatistics(tries);
				throw new RuntimeException("Double Hashing failed to find a free position");
			}
		}
		this.updateStatistics(tries);
		return result;
	}
	
	//Given a key, returns the index it is stored under it if it exists, else returns null
	private int find(K key)
	{
		//Calculates the primary and secondary hash values
		int index = this.hash(key);
		int increment = this.secondaryHash(key);
		int i = index;
		//Goes through the elements, starts at the primary hash, increments by secondary hash
		//Loops back to the beginning
		//Terminates when it reaches the primary hash again
		do
		{
			//If there is an empty element, then it means that the key has not been stored
			if(this.array[i] == null)
			{
				break;
			}
			//If the element
			if(key.equals(this.array[i].getKey()))
			{
				return i;
			}
			i = (i + increment) % this.array.length;
		}
		while(i != index);
		//If it has returned to the initial index, then the key has not been stored
		return -1;
	}
	
	//Returns a stored value given its key
	public V get(K key)
	{
		int index = this.find(key);
		//Returns null if the key has not been stored
		if(index == -1)
		{
			return null;
		}
		//Returns the value of the key if it has been stored
		else
		{
			return this.array[index].getValue();
		}
	}
	
	//Removes a stored value and returns it, given its key
	public V remove(K key)
	{
		int index = this.find(key);
		//Returns null if the key has not been stored
		if(index == -1)
		{
			return null;
		}
		//Replaces the element stored under the key with a sentinel if has been stored
		//Then, returns the value that was stored under it
		else
		{
			V result = this.array[index].getValue();
			this.array[index] = this.SENTINEL;
			return result;
		}
	}
	
	//Returns the number of collisions
	public int putCollisions()
	{
		return this.collisions;
	}
	
	//Returns the total number of probes
	public int totalCollisions()
	{
		return this.probes;
	}
	
	//Returns the largest amount of probes needed for a single collision
	public int maxCollisions()
	{
		return this.maxProbes;
	}

	//Returns the largest amount of probes needed for a single collision
	public int putFailures()
	{
		return this.failures;
	}
	
	//Resets all the statistics
	public void resetStatistics()
	{
		this.collisions = 0;
		this.probes = 0;
		this.maxProbes = 0;
		this.failures = 0;
	}
}