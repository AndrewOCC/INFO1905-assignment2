package skiplist;

//Implements comparable so that a compareTo method may be used
public class SkipListNode<K extends Comparable<K>, V> implements Comparable<SkipListNode<K, V>>
{
	//Variables
	private K key;
	private V value;
	private boolean positive;
	//Neighbors
	private SkipListNode<K, V> prev;
	private SkipListNode<K, V> next;
	private SkipListNode<K, V> above;
	private SkipListNode<K, V> below;
	
	//Constructor for a normal node
	//Sets key and value
	SkipListNode(K key, V value)
	{
		this.key = key;
		this.value = value;
		//null if non-sentinel
		//false if negative infinity
		//true if positive infinity
		this.positive = false;
		//Sets neighbors to null
		this.prev = null;
		this.next = null;
		this.above = null;
		this.below = null;
	}
	
	//Constructor for a sentinel node
	//Sets whether its key equates to positive infinity or negative infinity for comparisons
	SkipListNode(boolean positive)
	{
		this.key = null;
		this.value = null;
		this.positive = positive;
		//Sets neighbors to null
		this.prev = null;
		this.next = null;
		this.above = null;
		this.below = null;
	}

	//Get method for the key
	public K getKey()
	{
		return this.key;
	}
	
	//Get method for the value
	public V getValue()
	{
		return this.value;
	}
	
	//Get method for the preceding neighbor
	public SkipListNode<K, V> getPrev()
	{
		return this.prev;
	}
	
	//Get method for the subsequent neighbor
	public SkipListNode<K, V> getNext()
	{
		return this.next;
	}
	
	//Get method for the above neighbor
	public SkipListNode<K, V> getAbove()
	{
		return this.above;
	}
	
	//Get method for the below neighbor
	public SkipListNode<K, V> getBelow()
	{
		return this.below;
	}
	
	//Set method for the value
	public void setValue(V newValue)
	{
		this.value = newValue;
	}
	
	//Set method for the preceding neighbor
	public void setPrev(SkipListNode<K, V> prev)
	{
		this.prev = prev;
	}
	
	//Set method for the subsequent neighbor
	public void setNext(SkipListNode<K, V> next)
	{
		this.next = next;
	}
	
	//Set method for the above neighbor
	public void setAbove(SkipListNode<K, V> above)
	{
		this.above = above;
	}
	
	//Set method for the below neighbor
	public void setBelow(SkipListNode<K, V> below)
	{
		this.below = below;
	}

	//Compares a node to another node
	@Override
	public int compareTo(SkipListNode<K, V> other)
	{
		//If this node is a positive sentinel, it is always greater than the other node
		//If this node is a negative sentinel, it is always lesser than the other node
		if(this.getKey() == null)
		{
			return (this.positive) ? 1 : -1;
		}
		//If the other node is a positive sentinel, this node is always lesser than the other node
		//If the other node is a negative sentinel, this node is always greater than the other node
		else if(other.getKey() == null)
		{
			return (this.positive) ? -1 : 1;
		}
		//If the node is not a sentinel, compare the value of the keys
		else
		{
			return this.getKey().compareTo(other.getKey());
		}
		//Note that this does not consider the possibility that both nodes are
		//positive sentinels, or negative sentinels, as such should never occur
	}
	
	//Compares a node to a key
	public int compareTo(K other)
	{
		//If the node is not a sentinel, compare the value of the keys
		if(this.getKey() != null)
		{
			return this.getKey().compareTo(other);
		}
		//If this node is a positive sentinel, it is always greater than the other node
		//If this node is a negative sentinel, it is always lesser than the other node
		else
		{
			return (this.positive) ? 1 : -1;
		}
		//It is impossible for the other key to be a sentinel
	}
}
