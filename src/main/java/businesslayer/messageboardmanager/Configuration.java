package businesslayer.messageboardmanager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

final public class  Configuration {
	private long maxPosts;
	private String adminGroupName;
	private JsonObject jo;
	private String filepath;
	
	public Configuration(String filepath) {
		this.filepath = filepath;
	}
	
	public Configuration() {
		filepath = "config.json";
	}
	
	public void init() {
		try {
			JsonReader jr = Json.createReader(new FileInputStream(filepath));
			jo = jr.readObject();
			maxPosts = (long) jo.getInt("maxposts");
			adminGroupName = jo.getString("adminGroupName");
		} catch (FileNotFoundException e) {
			setDefaults();
		}
	}

	public long getMaxPosts() {
		return maxPosts;
	}

	public String getAdminGroupName() { return adminGroupName; }
	
	private void setDefaults() {
		maxPosts = 10;
	}
	
	
}
