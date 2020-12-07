package businesslayer.messageboardmanager;

import dao.UserPostDao;
import util.Configuration;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class PostManager {
	private ArrayList<Post> posts;
	private Configuration config;
	
	/**
	 * Opens the config file and inits it.
	 * Initializes an ArrayList of posts.
	 */
	public PostManager() {
		//PostManager.init from some sort of DOA
		config = new Configuration("./config.json");
		config.init();
		System.out.println(config.getClassName());
		posts = new ArrayList<Post>();
	}


	/**
	 * Creates a new post. Auto timestamped. The hashtags are extracted by the Hashtag Manager.
	 * @param user Username
	 * @param text Post content
	 * @return the created post
	 */

	public Post createPost(String user, String text, String postedDate, String title, ArrayList<Attachment> attachmentFiles, String uploadPath,String group) {
		UserPostDao upd = new UserPostDao();
		int postId = upd.add(text,user,postedDate,title,group);
		HashMap<Integer,String> attachmentNames = upd.upload(postId, attachmentFiles, uploadPath);

		Post post = new Post(user,text, HashtagManager.getHashtags(text), postId, postedDate, title, attachmentNames, group);
		posts.add(post);
		return post;
	}

	public Post createPost(int postID, String user, String text, String postedDate, String title, HashMap<Integer,String> attachmentNames,boolean updated, String updatedDate, String group) {
		Post post = new Post(user,text, HashtagManager.getHashtags(text), postID, postedDate, title, attachmentNames, updated, updatedDate, group);
		posts.add(post);
		return post;
	}
	
	/**
	 * Delete a post with a specific ID
	 * @param postId ID of the post
	 */
	public boolean validateDelete(int postId){
		UserPostDao upd = new UserPostDao();
		return upd.delete(postId);
	}

	public boolean validateAttachIDDelete(int attachmentID){
		UserPostDao upd = new UserPostDao();
		return upd.deleteAttach(attachmentID);
	}

	public void deletePost(int postId) {
		posts.removeIf(p -> (p.getPostId() == postId));
	}

	public void deleteAttach(int attachmentID){
		for(Post post : posts){
			HashMap<Integer,String> attachmentNames = post.getAttachmentNames();
			if(attachmentNames.containsKey(attachmentID)){
				attachmentNames.remove(attachmentID);
				post.setUpdated(true);
				break;
			}
		}
	}

	/**
	 * Delete posts within a date range.
	 * @param startDate
	 * @param endDate
	 */
	public void deletePost(ZonedDateTime startDate, ZonedDateTime endDate) {
		ArrayList<Post> deleting = new ArrayList<Post>();
		
		for(Post p : this.posts) {
			if(p.getDate().compareTo(startDate) > 0 && p.getDate().compareTo(endDate) < 0) {
				deleting.add(p);
			}
		}
		
		for(Post p: deleting) {
			this.posts.remove(posts.indexOf(p));
		}
	}
	
	/**
	 * Update the paramaters. Post content and hashtags are updated.
	 * @param postId ID of the post to update
	 * @return the updated post
	 */
	public Post updatePost(int postId, String title, String postText, String newPostDate, ArrayList<Attachment> attachmentFiles, String uploadPath, String group) {
		UserPostDao upd = new UserPostDao();

		HashMap<Integer,String> attachmentNames = upd.upload(postId, attachmentFiles, uploadPath);
		upd.update(postId,postText,newPostDate,title, group);
		ArrayList<String> postData = upd.getPost(postId);

		Post updatePost = null;
		for(Post p: posts) {
			if(p.getPostId() == postId) {
				updatePost = p;
			}
		}
		updatePost.setText(postData.get(0));
		updatePost.setHashtags(HashtagManager.getHashtags(postData.get(0)));
		updatePost.setUpdatedDate(ZonedDateTime.parse(postData.get(1),DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss z" )));
		updatePost.setTitle(postData.get(2));
		updatePost.setGroup(postData.get(3));

		HashMap<Integer,String> current = updatePost.getAttachmentNames();
		current.putAll(attachmentNames);
		updatePost.setAttachmentNames(current);
		updatePost.setUpdated(true);
		return updatePost;
	}

	public Post replaceAttachment(int attachmentID, int postID, String newPostDate, ArrayList<Attachment> attachmentFiles, String uploadPath){
		UserPostDao upd = new UserPostDao();
		HashMap<Integer,String> replacedAttach = upd.replaceAttach(attachmentID, attachmentFiles, uploadPath, newPostDate, postID);

		Post updatePost = null;
		for(Post post : posts){
			HashMap<Integer,String> previous = post.getAttachmentNames();
			if(previous.containsKey(attachmentID)){
				previous.put(attachmentID,replacedAttach.get(attachmentID));
				post.setUpdated(true);
				updatePost = post;
				break;
			}
		}
		return updatePost;
	}
	
	/**
	 * Simply lists all of the messages. This search is limited by Maxposts. Starts from the end.
	 * @return ArrayList of the search results.
	 */
	public ArrayList<Post> search(){
		ArrayList<Post> searchResult = new ArrayList<Post>();
		
		for(int i = posts.size() - 1, j = 0; i >= 0 && j != config.getMaxPosts(); i--, j++) {
			searchResult.add(posts.get(i));
		}
		return searchResult;
	}

	/**
	 * Lists posts restricted by group permissions based on user membership
	 * @return ArrayList of the search results.
	 */
	public ArrayList<Post> search_by_groups(ArrayList<String> permissions){
		ArrayList<Post> searchResult = filterPosts(permissions, posts);;

		if(!searchResult.isEmpty()){
			if(searchResult.size() > config.getMaxPosts())
				searchResult = new ArrayList<Post>(searchResult.subList(0, (int) config.getMaxPosts()));
		}

		return searchResult;
	}

	/**
	 * Returns all the posts (not limited by MaxPosts and not sorted)
	 * @return ArrayList of the search results.
	 */
	public ArrayList<Post> getAllPosts(){
		return posts;
	}
	
	/**
	 * Searches all posts made by a user.
	 * @param user username
	 * @return ArrayList of the search results.
	 */
	public ArrayList<Post> searchByUser(String user, ArrayList<String> permissions){
		ArrayList<Post> searchResult = new ArrayList<Post>();
		ArrayList<Post> tempPosts = new ArrayList<Post>();
		for(Post p : posts) {
			if(p.getUser().equals(user)) {
				tempPosts.add(p);
			}
		}

		searchResult = filterPosts(permissions, tempPosts);

		if(!searchResult.isEmpty())
			Collections.reverse(searchResult);
		
		return searchResult;
	}

	/**
	 * Searches a post by post ID.
	 * @param postId Post ID
	 * @return ArrayList of the search results.
	 */
	public Post search(int postId){
		Post post = null;
		for(Post p : posts) {
			if(p.getPostId() == postId) {
				post = p;
			}
		}
		return post;
	}
	
	/**
	 * Searches all posts with any tags found in hashtags.
	 * @param hashtags
	 * @return ArrayList of the search results.
	 */
	public ArrayList<Post> search(ArrayList<String> hashtags, ArrayList<String> permissions){
		ArrayList<Post> searchResult;
		ArrayList<Post> tempPosts = new ArrayList<Post>();
		for(Post p: posts) {
			if(HashtagManager.hashtagsContain(p.getHashtags(), hashtags))
				tempPosts.add(p);
		}
		searchResult = filterPosts(permissions, tempPosts);
		if(!searchResult.isEmpty())
			Collections.reverse(searchResult);

		return searchResult;
	}
	
	/**
	 * Searches all posts within a certain date range.
	 * @param startDate
	 * @param endDate
	 * @return ArrayList of the search results.
	 */
	public ArrayList<Post> search(ZonedDateTime startDate, ZonedDateTime endDate, ArrayList<String> permissions){
		ArrayList<Post> searchResult;
		ArrayList<Post> tempPosts = new ArrayList<Post>();

		for(Post p : posts) {
			//if current post's date > startDate AND current post's date is < endDate	
			if(!(p.getDate().compareTo(startDate) < 0 || p.getDate().compareTo(endDate) > 0)) {
				tempPosts.add(p);
			}
		}

		searchResult = filterPosts(permissions, tempPosts);

		if(!searchResult.isEmpty())
			Collections.reverse(searchResult);

		return searchResult;
	}
	
	/**
	 * Searches all posts within a certain date range and a username.
	 * @param user
	 * @param startDate
	 * @param endDate
	 * @return ArrayList of the search results.
	 */
	public ArrayList<Post> search(String user, ZonedDateTime startDate, ZonedDateTime endDate, ArrayList<String> permissions){
		ArrayList<Post> searchResult;
		ArrayList<Post> tempPosts = new ArrayList<Post>();

		for(Post p : posts) {
			if(p.getUser().equals(user)) {
				if(!(p.getDate().compareTo(startDate) < 0 || p.getDate().compareTo(endDate) > 0)) {
					tempPosts.add(p);
				}
			}
		}

		searchResult = filterPosts(permissions, tempPosts);
		if(!searchResult.isEmpty())
			Collections.reverse(searchResult);

		return searchResult;
	}
	
	/**
	 * Searches all posts made by a certain user and contains tags in hashtags.
	 * @param user
	 * @param hashtags
	 * @return ArrayList of the search results.
	 */
	public ArrayList<Post> search(String user, ArrayList<String> hashtags, ArrayList<String> permissions){
		ArrayList<Post> searchResult;
		ArrayList<Post> tempPosts = new ArrayList<Post>();
		for(Post p : posts) {
			if(p.getUser().equals(user)) {
				if(HashtagManager.hashtagsContain(p.getHashtags(), hashtags)) {
					tempPosts.add(p);
				}
			}
		}

		searchResult = filterPosts(permissions, tempPosts);
		if(!searchResult.isEmpty())
			Collections.reverse(searchResult);

		return searchResult;
	}
	
	/**
	 * Searches all posts containing tags in hastags and within a certain date range.
	 * @param hashtags
	 * @param startDate
	 * @param endDate
	 * @return ArrayList of the search results.
	 */
	public ArrayList<Post> search(ArrayList<String> hashtags, ZonedDateTime startDate, ZonedDateTime endDate, ArrayList<String> permissions){
		ArrayList<Post> searchResult;
		ArrayList<Post> tempPosts = new ArrayList<Post>();
		for(Post p : posts) {
			//if current post's date > startDate AND current post's date is < endDate	
			if(!(p.getDate().compareTo(startDate) < 0 || p.getDate().compareTo(endDate) > 0)) {
				if(HashtagManager.hashtagsContain(p.getHashtags(), hashtags)) {
					tempPosts.add(p);
				}
			}
		}
		searchResult = filterPosts(permissions, tempPosts);
		if(!searchResult.isEmpty())
			Collections.reverse(searchResult);

		return searchResult;
	}

	/**
	 * Searches all posts made by a certain user and contains tags in hashtags and within a certain date range.
	 * @param user
	 * @param hashtags
	 * @param startDate
	 * @param endDate
	 * @return ArrayList of the search results.
	 */
	public ArrayList<Post> search(String user, ArrayList<String> hashtags, ZonedDateTime startDate, ZonedDateTime endDate, ArrayList<String> permissions){
		ArrayList<Post> searchResult;
		ArrayList<Post> tempPosts = new ArrayList<Post>();

		for(Post p : posts) {
			if(p.getUser().equals(user)) {
				if(!(p.getDate().compareTo(startDate) < 0 || p.getDate().compareTo(endDate) > 0)) {
					if(HashtagManager.hashtagsContain(p.getHashtags(), hashtags)) {
						tempPosts.add(p);
					}
				}
			}
		}

		searchResult = filterPosts(permissions, tempPosts);
		if(!searchResult.isEmpty())
			Collections.reverse(searchResult);

		return searchResult;
	}

	private ArrayList<Post> filterPosts(ArrayList<String> permissions, ArrayList<Post> posts){
		ArrayList<Post> filteredPosts = new ArrayList<Post>();

		if(permissions.contains("admin")){
			filteredPosts = posts;
		}
		else{
			for(int i = posts.size() - 1; i >= 0; i--) {
				Post post = posts.get(i);
				String postGroup = post.getGroup();

				if(postGroup.equals("public"))
					filteredPosts.add(post);
				else if(permissions.contains(postGroup))
					filteredPosts.add(post);
			}
		}
		return filteredPosts;
	}


}
