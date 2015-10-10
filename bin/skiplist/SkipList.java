package skiplist;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SkipList<K extends Comparable<K>, V>
{
	private Random rng;
	private int height;
	//Upper left node
	private SkipListNode<K, V> head;
	//Upper left node
	private SkipListNode<K, V> tail;
	//Lower left node
	private SkipListNode<K, V> init;
	
	//Constructor
	public SkipList()
	{
		this.rng = new Random();
		this.height = 0;
		//Creates a positive sentinel and a negative sentinel
		this.head = new SkipListNode<K, V>(false);
		this.init = this.head;
		this.tail = new SkipListNode<K, V>(true);
		//Links the sentinels so that the negative precedes the positive
		this.horiLink(this.head, this.tail);
	}
	
	//Helper method that links two nodes so that one is above the other
	private void vertLink(SkipListNode<K, V> upper, SkipListNode<K, V> lower)
	{
		upper.setBelow(lower);
		lower.setAbove(upper);
	}
	
	//Helper method that links two nodes so that one preceeds the other
	private void horiLink(SkipListNode<K, V> preceeding, SkipListNode<K, V> subsequent)
	{
		preceeding.setNext(subsequent);
		subsequent.setPrev(preceeding);
	}
	
	//Helper method that adds a new empty level
	private void grow()
	{
		//Creates a positive and a negative sentinel
		SkipListNode<K, V> nextHead = new SkipListNode<K, V>(false);
		SkipListNode<K, V> nextTail = new SkipListNode<K, V>(true);
		//Links the sentinels so that the negative preceeds the positive
		this.horiLink(nextHead, nextTail);
		//The negative sentinel goes on top of the head
		this.vertLink(nextHead, this.head);
		//The positive sentinel goes on top of the tail
		this.vertLink(nextTail, this.tail);
		//The negative sentinel becomes the new head, the positive sentinel becomes the new tail
		this.head = nextHead;
		this.tail = nextTail;
		//Increments height
		this.height++;
	}
	
	//Returns the number of elements in the skip list
	public int size()
	{
		//Count is initialized as -2 to account for the positive and negative sentinels
		int count = -2;
		//For each node, increment the count
		for(SkipListNode<K, V> node = this.init; node != null; node = node.getNext())
		{
			count++;
		}
		return count;
	}
	
	//Returns whether or not the skip list is empty
	public boolean isEmpty()
	{
		//The skip list is empty if it has no elements
		return (this.size() == 0);
	}
	
	//Returns a list of all the stored keys
	public List<K> keys()
	{
		ArrayList<K> result = new ArrayList<K>();
		//For every node (excluding the negative sentinel) add its key to the list
		for(SkipListNode<K, V> node = this.init.getNext(); node != null; node = node.getNext())
		{
			result.add(node.getKey());
		}
		//Remove the null key of the positive sentinel
		result.remove(result.size() - 1);
		return result;
	}
	
	//Returns the rightmost element with a key that is less than or equal to the given key
	public SkipListNode<K, V> search(K key)
	{
		SkipListNode<K, V> node = this.head;
		//Moves down to the lowest level of the skip list
		while(true)
		{
			//Moves as far right as possible in the given level
			//while still satisfying the less than or equal to condition
			for(; node.getNext().compareTo(key) <= 0; node = node.getNext());
			if(node.getBelow() == null)
			{
				break;
			}
			node = node.getBelow();
		}
		return node;
	}
	
	//A variant of search that returns the number of non-sentinel nodes it had to check
	public int countedSearch(K key)
	{
		SkipListNode<K, V> node = this.head;
		int count = 0;
		//Moves down to the lowest level of the skip list
		while(true)
		{
			//Moves as far right as possible in the given level
			//while still satisfying the less than or equal to condition
			for(; node.getNext().compareTo(key) <= 0; node = node.getNext())
			{
				//Increments the counter if the node is not a sentinel
				if(node.getKey() != null)
				{
					count++;
				}
			}
			if(node.getBelow() == null)
			{
				break;
			}
			node = node.getBelow();
		}
		return count;
	}
	
	//Helper method that grows a tower of given height for the given node
	private void retrace(SkipListNode<K, V> node, int height)
	{
		SkipListNode<K, V> next;
		SkipListNode<K, V> below;
		SkipListNode<K, V> prev = node.getPrev();
		//While the height has not been reached, inserts a new level to the tower
		while(height > 0)
		{
			//Moves backwards if it cannot ascend
			if(prev.getAbove() == null)
			{
				prev = prev.getPrev();
			}
			//If it can ascend, add a new level
			else
			{
				prev = prev.getAbove();
				next = prev.getNext();
				//Places a new node in the tower
				below = node;
				node = new SkipListNode<K, V>(node.getKey(), node.getValue());
				this.vertLink(node, below);
				//Links the new node to adjacent nodes in the same level
				next = prev.getNext();
				this.horiLink(prev, node);
				this.horiLink(node, next);
				height--;
			}
		}
	}
	
	//Inserts an element into the skip list, returning its previous value (if any)
	public V put(K key, V value)
	{
		V result = null;
		SkipListNode<K, V> prev = this.search(key);
		//If the element already exists, overwrite its stored value
		if(prev.compareTo(key) == 0)
		{
			result = prev.getValue();
			for(; prev != null; prev = prev.getAbove())
			{
				prev.setValue(value);
			}
		}
		//If the element does not already exist, insert it and give it a tower
		else
		{
			int height = 0;
			//Randomly assigns a tower height
			//Probability of a tower height of at least h is 2^(-h)
			for(; this.rng.nextBoolean(); height++);
			//Grows sentinel towers until they exceed the height of the tower being constructed
			while(this.height <= height)
			{
				this.grow();
			}
			//Inserts a node for the element at the lowest level
			SkipListNode<K, V> node = new SkipListNode<K, V>(key, value);
			SkipListNode<K, V> next = prev.getNext();
			this.horiLink(prev, node);
			this.horiLink(node, next);
			//Retraces the steps of the search function to construct the tower
			retrace(node, height);
		}
		return result;
	}
	
	//Returns the value stored under a key, returning null the key is not present
	public V get(K key)
	{
		SkipListNode<K, V> node = this.search(key);
		//If the key is present, return its value
		if(node.compareTo(key) == 0)
		{
			return node.getValue();
		}
		return null;
	}
	
	//Removes the element stored under the given key, returning its value
	public V remove(K key)
	{
		SkipListNode<K, V> node = this.search(key);
		//If the key is not present, return null
		if(node.compareTo(key) != 0)
		{
			return null;
		}
		V result = node.getValue();
		//Move up the key's tower, removing each node
		for(; node != null; node = node.getAbove())
		{
			this.horiLink(node.getPrev(), node.getNext());
		}
		return result;
	}
}