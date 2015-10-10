package doublehashmap;

public class HashMapNode<K extends Comparable<K>, V>
{
	private K key;
	private V value;
	
	//Constructor
	public HashMapNode(K key, V value)
	{
		this.key = key;
		this.value = value;
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

	//Set value for the method
	public void setValue(V newValue)
	{
		this.value = newValue;
	}
}
