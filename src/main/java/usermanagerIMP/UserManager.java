package usermanagerIMP;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import businesslayer.interfaces.usermanagement.*;
import exceptions.*;

public class UserManager implements UserManagerInterface{	
	private HashMap<String,String> users = null;
	private final String userFilepath;
	private final String groupFilepath;
	
	public UserManager(String userFilepath, String groupFilepath) {
		this.userFilepath = userFilepath;
		this.groupFilepath = groupFilepath;
		loadUsers();
	}

	private void loadUsers() {
		this.users = new HashMap<String,String>();
		try {
			JsonReader jr = Json.createReader(new FileInputStream(userFilepath));
			JsonObject jo = jr.readObject();
			JsonArray usersArray = (JsonArray) jo.get("users");
			
			for(int i = 0; i < usersArray.size();i++) {
				JsonObject userToAdd = (JsonObject) usersArray.get(i);
				String username = userToAdd.getString("name");
				String pass = userToAdd.getString("password");
				users.put(username, pass);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		 }
		
	}
	
	@Override
	public boolean verifyUser(String username, String plainPasswordEntered) {
		/*
		 * Takes the plain text password and hashes it. It then compared this hashed password with
		 * the appropriate password in the json file. If there is no user with the username
		 * entered, return false.
		 */
		
		if(users.containsKey(username)) {
			String hashedPass = users.get(username);
			if(hashedPass.equals(Hasher.hash(plainPasswordEntered))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	@Override
	public ArrayList<String> getGroupsForUser(String username) throws CircularGroupDefinitionException {
		ArrayList<String> groups = new ArrayList<String>();
		try {
			JsonReader jr = Json.createReader(new FileInputStream(groupFilepath));
			JsonObject jo = jr.readObject();
			JsonArray groupNames = jo.getJsonArray("group_names");
			JsonObject groupDefs = jo.getJsonObject("group_definitions");
			JsonObject membership = jo.getJsonObject("membership");
			//The array in the membership object in group_definitions.json 
			JsonArray explicitGroups = membership.getJsonArray(username);
			
			//Explicitly stated group memberships
			for(int i = 0; i < explicitGroups.size();i++) {
				String currentExplicitGroup = explicitGroups.getString(i);
				groups.add(currentExplicitGroup);
				
				//Get the children of the groups
				try {
				getChildren(currentExplicitGroup,groupDefs,groupNames,groups);
				
				} catch(StackOverflowError e) {
					
					throw new CircularGroupDefinitionException("Circular Group Definiton in " + groupFilepath + ". Probable group cause: \"" + currentExplicitGroup + "\"",
							new Throwable(currentExplicitGroup));
				}
			}			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		 }
		return groups;
	}
	
	/*
	 * Goes down the list of the group names. For each group, check if the parent of that group is the 
	 * groupName we are getting the children if. If true, add it to the children ArrayList and get its 
	 * children.
	 */
	private void getChildren(String groupName, JsonObject groupDefs, JsonArray groupNames, ArrayList<String> children) {
		
		for(int i = 0; i < groupNames.size(); i++) {
			String currentGroupName = groupNames.getString(i);
			
			//If currentGroup name is equal to the groupName we are getting the children of, continue
			if(groupName.equals(currentGroupName)) 
				continue;
			
			String parent  = groupDefs.getString(currentGroupName);
			
			if(groupName.equals(parent)) {
				children.add(currentGroupName);
				getChildren(currentGroupName, groupDefs, groupNames,children);
			}
		}
	}
	
	@Override
	public boolean verifyGroupDefinitonsFile() throws UndefinedUserException, UndefinedGroupException, MissingGroupDefinitionException {
		try {
			JsonReader groupReader = Json.createReader(new FileInputStream(groupFilepath));
			JsonObject groupsJson = groupReader.readObject();
			JsonArray groupNames = groupsJson.getJsonArray("group_names");
			JsonObject groupDefs = groupsJson.getJsonObject("group_definitions");
			JsonObject membership = groupsJson.getJsonObject("membership");
			
			//Goes through every user in the membership JSON object
			for(String user: membership.keySet()) {
				
				//Gets the list of groups for the current iterated user
				JsonArray currentUserGroups = membership.getJsonArray(user);
				//For each group in the user's list of memberships
				for(int i = 0; i < currentUserGroups.size(); i++) {
					//check if each group membership is in the global list of groups
					JsonValue currentGroup = currentUserGroups.get(i);
					if(!(groupNames.contains(currentGroup))) {
						throw new UndefinedGroupException("Undefined Group Membership: " + currentUserGroups.get(i) + " in " + groupFilepath
								,new Throwable(currentGroup.toString().substring(1, currentGroup.toString().length()-1)));
					}
				}
				//Check if the current iterated user is in the global list of users
				if(!(users.containsKey(user))) {
					throw new UndefinedUserException("Undefined user: \"" + user + "\" in " + groupFilepath
							, new Throwable(user));
				}
			}
			
			//For each parent group in group_definitons JSON object
			for(JsonValue group: groupDefs.values()) {
				//If no parent group, continue
				if(group.toString().equals("\"\""))
					continue;
				//If parent group is not in the global list of groups
				if(!(groupNames.contains(group))) {
					throw new UndefinedGroupException("Undefined Parent Group: " + group + " in " + groupFilepath,
							new Throwable(group.toString().substring(1, group.toString().length()-1)));
				}
			}

			for(JsonValue group: groupNames) {

				JsonValue temp = groupDefs.get(group.toString());
				if(temp == null) {
					throw new MissingGroupDefinitionException("Missing Group Definiton for " + group + " in " + groupFilepath,
							new Throwable(group.toString().substring(1,group.toString().length()-1)));
				}
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return true;
	}

	

	@Override
	public String getUsernameFromEmail(String email) {
		String username = "";
		try {
			JsonReader jr = Json.createReader(new FileInputStream(userFilepath));
			JsonObject jo = jr.readObject();
			JsonArray usersArray = (JsonArray) jo.get("users");

			for (int i = 0; i < usersArray.size(); i++) {
				JsonObject tempUser = (JsonObject) usersArray.get(i);
				if (tempUser.getString("email").equals(email)) {
					username = tempUser.getString(("name"));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return username;
	}

	@Override
	public String getEmailFromUsername(String username) {
		String email = "";

		try {
			JsonReader jr = Json.createReader(new FileInputStream(userFilepath));
			JsonObject jo = jr.readObject();
			JsonArray usersArray = (JsonArray) jo.get("users");
			for (int i = 0; i < usersArray.size(); i++) {
				JsonObject tempUser = (JsonObject) usersArray.get(i);
				if (tempUser.getString("username").equals(username)) {
					username = tempUser.getString(("email"));
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return email;
	}


	
	
}
