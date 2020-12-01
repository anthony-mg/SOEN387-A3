package businesslayer.messageboardmanager;

import java.util.ArrayList;
/**
 * 
 * Utility class to parse and compare hashtags
 *
 */
public final class HashtagManager {
	 /**
	  * Parses the text of a post and returns an ArrayList of the tags.
	  * @param text
	  * @return
	  */
	 public static ArrayList<String> getHashtags(String text){
		//This will give an array of Strings with the first word of every entry is the hashtag
		String[] unextracted = text.replaceFirst("^" + "#", "").split("#");
		ArrayList<String> hashtags = new ArrayList<String>();
			
		for(int i = 1; i < unextracted.length;i++) {
			//Splits each string in the array along whitespace and gets the first element returned.
			hashtags.add(unextracted[i].split("\\s")[0]);
		}
		
		return hashtags;
	 }
	 
	 /**
	  * Given two ArrayLists of hashtags, checks in any tags in "search" are in "postTags"
	  * @param search list of search tags
	  * @param postTags list of tags in the post
	  * @return
	  */
	 public static boolean hashtagsContain(ArrayList<String> search, ArrayList<String> postTags) {
		 
		 for(String s: search) {
			if(postTags.contains(s))
				return true;
		 }
		 return false;
	 }
	 
	
}
