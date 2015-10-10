package passwordmanager;

import doublehashmap.DoubleHashMap;

public class User {
	
	//Parameters
	private String username;
	
	//Data Stores
	private DoubleHashMap<String, Long> passwordMap;
	
	//Construct a new User with given username and empty password store
	//Store should have size 20, and use multiplier=1 modulus=23 secondaryModulus=11 
	public User(String username) 
	{
		this.username = username;
		this.passwordMap = new DoubleHashMap<String, Long>(1, 24, 11);
	}
	
	//Returns username
	public String getUsername() 
	{
		return this.username;
	}
	
	//Returns the password hash for a given app (if present)
	public Long getPassword(String appName) 
	{
		return passwordMap.get(appName);
	}
	
	
	// set method
	public void setPassword(String appName, Long passwordHash)
	{
		passwordMap.put(appName, passwordHash);
	}

}