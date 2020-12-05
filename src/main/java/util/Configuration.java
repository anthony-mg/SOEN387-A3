package util;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

final public class  Configuration {
	private long maxPosts;
	private String adminGroupName;
	private String className;
	private JsonObject jo;
	private String filepath;
	
	public Configuration(String filepath) {
		this.filepath = filepath;
	}
	
	public Configuration() {
		filepath = "./config.json";
	}
	
	public void init() {
		try {
			JsonReader jr = Json.createReader(new FileInputStream(filepath));
			jo = jr.readObject();
			maxPosts = (long) jo.getInt("maxposts");
			adminGroupName = jo.getString("adminGroupName");
			className = jo.getString("className");
		} catch (FileNotFoundException e) {
			setDefaults();
		}
	}

	public long getMaxPosts() {
		return maxPosts;
	}

	public String getAdminGroupName() { return adminGroupName; }

	public String getClassName() { return className; }
	
	private void setDefaults() {
		maxPosts = 10;
		adminGroupName = "admin";
		className = "usermanagerIMP.UserManager";
	}
	
	
}
