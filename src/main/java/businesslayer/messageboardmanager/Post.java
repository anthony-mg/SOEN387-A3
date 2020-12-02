package businesslayer.messageboardmanager;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class which represents a post. An ID is kept for every post and it autoincremented. ArrayList of hashtags are kept.
 * Similar to the message class from the previous assignment.
 *
 */
public class Post {
	private final int postId;
	private String text;
	private String title;
	private String user;
	private ArrayList<String> hashtags;
	private HashMap<Integer,String> attachmentNames;
	private ZonedDateTime date;
	private ZonedDateTime updatedDate;
	private String updatedDateString;
	private String dateString;
	//private String group;
	private boolean updated;
	
	/**
	 * 
	 * @param user Username
	 * @param text Content of the post, the hashtags are left in plaintext
	 * @param hashtags ArrayList of hashtags. This is generated by the Hashtag Mananger.
	 */
	public Post(String user, String text, ArrayList<String> hashtags, int postID, String postedDate, String title, HashMap<Integer,String> attachmentNames){
		this.user = user;
		this.text = text;
		this.postId = postID;
		this.title = title;
		this.attachmentNames = attachmentNames;

		updated = false;
		date = ZonedDateTime.parse(postedDate, DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss z" ));
		dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"));

		if(hashtags == null) {
			this.hashtags = new ArrayList<String>();
		}
		else
			this.hashtags = hashtags;


	}

	public Post(String user, String text, ArrayList<String> hashtags, int postID, String postedDate, String title, HashMap<Integer,String> attachmentNames, boolean updated, String updatedDate){
		this.user = user;
		this.text = text;
		this.postId = postID;
		this.title = title;
		this.attachmentNames = attachmentNames;
		if(updatedDate != null && !updatedDate.isEmpty()){
			this.updatedDate = ZonedDateTime.parse(updatedDate, DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss z" ));
			updatedDateString = this.updatedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"));
		}


		this.updated = updated;
		date = ZonedDateTime.parse(postedDate, DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss z" ));
		dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"));

		if(hashtags == null) {
			this.hashtags = new ArrayList<String>();
		}
		else
			this.hashtags = hashtags;


	}
	//UNCOMMENT ONCE THE GROUP FUNCTIONALITY IS ADDED
//	public Post(String user, String text, ArrayList<String> hashtags, int postID, String postedDate, String title, String group, HashMap<Integer,String> attachmentNames){
//		this.user = user;
//		this.text = text;
//		this.postId = postID;
//		this.title = title;
//		this.attachmentNames = attachmentNames;
//
//		updated = false;
//		date = ZonedDateTime.parse(postedDate, DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss z" ));
//		dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"));
//
//		if(hashtags == null) {
//			this.hashtags = new ArrayList<String>();
//		}
//		else
//			this.hashtags = hashtags;
//
//
//	}
//
//	public Post(String user, String text, ArrayList<String> hashtags, int postID, String postedDate, String title, String group, HashMap<Integer,String> attachmentNames, boolean updated, String updatedDate){
//		this.user = user;
//		this.text = text;
//		this.postId = postID;
//		this.title = title;
//		this.attachmentNames = attachmentNames;
//		if(updatedDate != null && !updatedDate.isEmpty()){
//			this.updatedDate = ZonedDateTime.parse(updatedDate, DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss z" ));
//			updatedDateString = this.updatedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"));
//		}
//
//
//			this.updated = updated;
//		date = ZonedDateTime.parse(postedDate, DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss z" ));
//		dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"));
//
//		if(hashtags == null) {
//			this.hashtags = new ArrayList<String>();
//		}
//		else
//			this.hashtags = hashtags;
//
//
//	}

	/**
	 * FOR TESTING
	 * @param weeks
	 * @return
	 */
	public Post addWeeks(int weeks) {
		date = date.plusWeeks(weeks);
		dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"));
		
		return this;
	}
	/**
	 * FOR TESTING
	 * @param days
	 * @return
	 */
	public Post addDays(int days) {
		date = date.plusDays(days);
		dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"));

		return this;
	}
	public String toString() {
		return "(" + dateString + ") " + user + ": " + text;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ZonedDateTime getDate() {
		return date;
	}

	public void setDate(ZonedDateTime date) {
		this.date = date;
		this.dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"));
	}

	public String getDateString() {
		return dateString;
	}

	public String getUpdatedDate() {
		return updatedDateString;
	}

	public void setUpdatedDate(ZonedDateTime newDate){
		this.updatedDate = newDate;
		this.updatedDateString = updatedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"));
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public int getPostId() {
		return postId;
	}

	public ArrayList<String> getHashtags() {
		return hashtags;
	}

	public void setHashtags(ArrayList<String> hashtags) {
		this.hashtags = hashtags;
	}


	public String getTitle() { return title; }

	public void setTitle(String title){ this.title = title; }

	public HashMap<Integer, String> getAttachmentNames(){return attachmentNames;}

	public void setAttachmentNames(HashMap<Integer,String> attachmentNames){this.attachmentNames = attachmentNames;}

}
