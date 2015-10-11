package passwordmanager;

import java.util.List;

import skiplist.SkipList;

public class SkipListPasswordManager 
{
	//Finals
	public static final String NAME = "pwdMan";
	public static final String USER_CONFLICT = "User already exists.";
	public static final String NO_USER = "No such user exists.";
	public static final String NO_APP = "No password found.";
	public static final String AUTH_FAILURE = "Failed to authenticate user.";
	public static final String PASSWORD_CONFLICT = "Password already set up.";
	
	
	//Data Stores
	private SkipList<String, User> userList;
	
	//Construct a SkipListPasswordManager
	public SkipListPasswordManager() 
	{
		userList = new SkipList<String, User>();
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
		return this.userList.keys();
		
	}
	
	// - Return number of users currently stored
	public int numberUsers() 
	{
		return this.userList.size();
	}
	
	// - Creates a new user with the provided username and internal password and returns username
	// - If the user already exists, returns an error string
	public String addNewUser(String username, String password) 
	{
		if(userList.get(username) == null) 
		{
			User newUser = new User(username);
			newUser.setPassword(NAME, hash(password));
			userList.put(username, newUser);
			return username;
		} 
		else
		{
			return USER_CONFLICT;
		}
	}
	
	public String deleteUser(String username, String password) 
	{
		User currentUser = userList.get(username);
		
		if(currentUser == null) 
		{
			return NO_USER;
		} 
		else
		{
			if (authenticate(username, password) == username) 
			{
				userList.remove(username);
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
		User currentUser = userList.get(username);
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
		User currentUser = userList.get(username);
		if (currentUser == null) 
		{
			return NO_USER;
		}
		else
		{
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
		User currentUser = userList.get(username);
		String authResponse = authenticate(username, oldPassword);
		if (authResponse.equals(username)){
			currentUser.setPassword(NAME, hash(newPassword));
		}
		return authResponse;
		
	}
	
	public String resetPassword(String username, String oldPassword, String newPassword, String appName) 
	{
		User currentUser = userList.get(username);
		String authResponse = authenticate(username, oldPassword, appName);
		if (authResponse.equals(username)){
			currentUser.setPassword(appName, hash(newPassword));
		}
		return authResponse;
	}
	
	public String newAppPassword(String username, String thisPassword, String appPassword, String appName) 
	{
		User currentUser = userList.get(username);
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
	
	public int searchSteps(String username)
	{
		return this.userList.countedSearch(username);
	}
}