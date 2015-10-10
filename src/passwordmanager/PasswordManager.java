package passwordmanager;

import java.util.List;

import doublehashmap.DoubleHashMap;

public class PasswordManager 
{
	//Finals
	public static final String NAME = "pwdMan";
	public static final String USER_CONFLICT = "User already exists.";
	public static final String NO_USER = "No such user exists.";
	public static final String NO_APP = "No password found.";
	public static final String AUTH_FAILURE = "Failed to authenticate user.";
	public static final String PASSWORD_CONFLICT = "Password already set up.";
	
	
	//Data Stores
	private DoubleHashMap<String, User> userMap;
	
	//Construct a PasswordManager with 4000 places
	//Hash parameters should be multiplier=1 modulus=4271 secondaryModulus=647 
	public PasswordManager() 
	{
		userMap = new DoubleHashMap<String, User>(1, 4271, 647);
	}
	
	//Construct a PasswordManager with size number of places
	//Hash parameters should be multiplier=1 modulus=4271 secondaryModulus=647 
	public PasswordManager(int size) 
	{
		userMap = new DoubleHashMap<String, User>(size, 1, 4271, 647);
	}
	
	//Hash representation of password to be stored on the User object
	// - Uses sdbm hash function
	public Long hash(String password)  
	{
		if(password != null)
		{
			Long hash = 0L;
			for (char c : password.toCharArray()) 
			{
				hash = (int)c + (hash << 6) + (hash << 16) - hash;
			}
			return hash;
		}
		else 
		{
			return null;
		}
	}
	
	//Userbase methods
	
	// - Return an array of the usernames of the users currently stored
	public List<String> listUsers() 
	{
		return this.userMap.keys();
		
	}
	
	// - Return number of users currently stored
	public int numberUsers() 
	{
		return this.userMap.size();
	}
	
	// - Creates a new user with the provided username and internal password and returns username
	// - If the user already exists, returns an error string
	public String addNewUser(String username, String password) 
	{
		if(userMap.get(username) == null) 
		{
			User newUser = new User(username);
			newUser.setPassword(NAME, hash(password));
			userMap.put(username, newUser);
			return username;
		} 
		else
		{
			return USER_CONFLICT;
		}
	}
	
	public String deleteUser(String username, String password) 
	{
		User currentUser = userMap.get(username);
		
		if(currentUser == null) 
		{
			return NO_USER;
		} 
		else
		{
			if (authenticate(username, password) == username) 
			{
				userMap.remove(username);
				return username;
			} 
			else 
			{
				return AUTH_FAILURE;
			}
		}
	}
	
	// interface methods
	public String authenticate(String username, String password)
	{
		User currentUser = userMap.get(username);
		if(currentUser == null) 
		{
			return NO_USER;
		}
		else if (currentUser.getPassword(NAME).equals(hash(password)))
		{
			return username;
		}
		else
		{
			return AUTH_FAILURE;
		}
	}
	
	public String authenticate(String username, String password, String appName) 
	{
		User currentUser = userMap.get(username);
		if (currentUser == null) 
		{
			return NO_USER;
		}
		else
		{
			Long hashPassword = hash(password);
			Long appPassword = currentUser.getPassword(appName);
			if (appPassword == null) {
				return NO_APP;
			}
			
			else if (appPassword.equals(hash(password)))
			{
				return username;
			}
			else
			{
				return AUTH_FAILURE;
			}
		}
	}
	
	public String resetPassword(String username, String oldPassword, String newPassword) 
	{
		User currentUser = userMap.get(username);
		String authResponse = authenticate(username, oldPassword);
		if (authResponse.equals(username)){
			currentUser.setPassword(NAME, hash(newPassword));
		}
		return authResponse;
		
	}
	
	public String resetPassword(String username, String oldPassword, String newPassword, String appName) 
	{
		User currentUser = userMap.get(username);
		String authResponse = authenticate(username, oldPassword, appName);
		if (authResponse.equals(username)){
			currentUser.setPassword(appName, hash(newPassword));
		}
		return authResponse;
	}
	
	public String newAppPassword(String username, String thisPassword, String appPassword, String appName) 
	{
		User currentUser = userMap.get(username);
		String authResponse = authenticate(username, thisPassword);
		if (authResponse.equals(username))
		{
			if (currentUser.getPassword(appName) == null)
			{
				currentUser.setPassword(appName, hash(appPassword));
			}
			else
			{
				authResponse = PASSWORD_CONFLICT;
			}
		}
		return authResponse;
	}
}