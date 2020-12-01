package businesslayer.authentification;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;


public class UserAuthentificator {
	private HashMap<String,String> users = new HashMap<String,String>();
	private String filepath = "";
	
	public UserAuthentificator(String filepath) {
		this.filepath = filepath;
		/*
		 * Constructor will read from the users.json file and compile all usernames
		 * and passwords into the HashMap "users"
		 */
		try {
			JsonReader jr = Json.createReader(new FileInputStream(filepath));
			JsonObject jo = jr.readObject();
			JsonArray usersArray = (JsonArray) jo.get("users");

			for(int i = 0; i < usersArray.size();i++) {
				JsonObject userToAdd = (JsonObject) usersArray.get(i);
				String email = userToAdd.getString("email");
				String pass = userToAdd.getString("password");
				users.put(email, pass);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		 }
	}
	
	public boolean verifyUser(String email, String plainPasswordEntered) {
		/*
		 * Takes the plain text password and hashes it. It then compared this hashed password with
		 * the appropriate password in the json file. If there is no user with the username
		 * entered, return false.
		 */
		System.out.println(users.get("ABC"));
		if(users.containsKey(email)) {
			String hashedPass = users.get(email);
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

	public String getUsernameFromEmail(String email){
		String username = "shit";
		try {
			JsonReader jr = Json.createReader(new FileInputStream(filepath));
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

	public String getEmailFromUsername(String username){
		String email = "";

		try {
			JsonReader jr = Json.createReader(new FileInputStream(filepath));
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
