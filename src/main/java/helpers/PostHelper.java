package helpers;

import businesslayer.messageboardmanager.Post;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class PostHelper implements java.io.Serializable{
    private Post post;
    private int postId;
    private String user;
    private String title;
    private String createdDate;
    private String text;
    private String group;
    private boolean isAdmin;

    public PostHelper(){

    }

    public PostHelper(Post post, String userName, boolean isAdmin){
        setPost(post);
        setUser(userName);
        setPostId(post.getPostId());
        setTitle(post.getTitle());
        setText(post.getText());
        setGroup(post.getGroup());
        setCreatedDate(post.getDateString());
        setIsAdmin(isAdmin);
    }

    public void setPost(Post post)
    {
        this.post = post;
    }

    public Post getPost(){
        return this.post;
    }

    public int getPostId(){
        return postId;
    }

    public void setPostId(int postId){
        this.postId = postId;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public String getUser(){
        return post.getUser();
    }

    public void setUser(String user){
        this.user = user;
    }

    public String getCreatedDate(){
        return createdDate;
    }

    public void setCreatedDate(String createdDate){
        this.createdDate = createdDate;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getGroup(){
        return group;
    }

    public void setGroup(String group){
        this.group = group;
    }

    public boolean getIsAdmin(){
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }

    public String getUpdatedDate(){
        StringBuilder result = new StringBuilder();
        if(post.isUpdated()){
            result.append("<div class='form-group'><label for='updated'>Updated Date</label><input type='text' class='form-control' name='updated' id='updated' value='")
                    .append(post.getUpdatedDate()).append("'></div>");
        }
        return result.toString();
    }

    public String getAttachments(){
        StringBuilder result = new StringBuilder();
        HashMap<Integer, String> attachments = post.getAttachmentNames();
        if(!attachments.isEmpty()){
            result.append("<div class='form-group'><label>Attachments</label>");
            for (Map.Entry file : attachments.entrySet()) {
                String listElement = "<li class='list-group-item d-flex justify-content-between lh-condensed'>" +
                        "<a target='_blank' href='FileDownloadServlet?postId=" + post.getPostId() + "&attachmentId=" +
                        file.getKey() + "'>" +file.getValue() + "</a></li>";
                result.append(listElement);
            }
            result.append("</div>");
        }
        return result.toString();
    }

    public String showEdit() throws UnsupportedEncodingException {
        String rawPath = this.getClass().getClassLoader().getResource("").getPath();
        String decodedPath = URLDecoder.decode(rawPath, "UTF-8");
        String cleanBase[] = decodedPath.split("/WEB-INF/classes/");
        decodedPath = cleanBase[0];
        String contextPath = decodedPath.substring(decodedPath.lastIndexOf("/"));

        StringBuilder result = new StringBuilder();
        if(post.getUser().equals(user) || isAdmin){
            String editButton = "<a class='btn btn-info btn-lg btn-secondary btn-block' href='"+ contextPath + "/edit?postId=" + post.getPostId() + "'>edit</a>";
            result.append(editButton);
        }

        return result.toString();
    }
}
