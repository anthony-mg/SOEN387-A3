package dao;

import businesslayer.messageboardmanager.Attachment;
import businesslayer.messageboardmanager.PostManager;
import util.JDBCutils;

import java.io.*;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserPostDao {
    public int add(String post,String user,String date,String title, String group){
        int newID = 0;
        String sql = "insert into userpost (post,user,date,title,post_group) values (?,?,?,?,?)";
        JDBCutils util = new JDBCutils();
        PreparedStatement ps = util.createStatementWithKeys(sql);
        try {
            ps.setString(1, post);
            ps.setString(2, user);
            ps.setString(3, date);
            ps.setString(4, title);
            ps.setString(5, group);

            int insertedRow = ps.executeUpdate();

            if (insertedRow == 0) {
                throw new SQLException("Insert failed");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newID = generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            util.close();
        }
        return newID;
    }

    public HashMap<Integer,String> upload(int postID, ArrayList<Attachment> attachments, String uploadPath) {
        HashMap<Integer,String> attachmentNames = new HashMap<Integer, String>();
        String sql = "insert into attachments (post_ID, attachment_name, attachment_file_size, attachment_mediatype, attachment_file) values (?,?,?,?,?)";
        JDBCutils util = new JDBCutils();
        PreparedStatement ps = null;
        try{
            for(Attachment attachment: attachments){
                ps = util.createStatement(sql);

                String fileName = attachment.getFileName();
                long fileSize = attachment.getFileSize();
                String mediatype = attachment.getMediatype();

                ps.setInt(1,postID);
                ps.setString(2,fileName);
                ps.setLong(3,fileSize);
                ps.setString(4,mediatype);

                File file = new File(uploadPath + File.separator + fileName);
                FileInputStream input = null;
                try {
                    input = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                ps.setBinaryStream(5,input);

                ps.executeUpdate();

                // extract attachment ID
                ps = util.creatConnection().prepareStatement("select attachment_ID from attachments order by attachment_ID DESC LIMIT 1");
                ResultSet rs = ps.executeQuery();

                int attachmentID = 0;
                while(rs.next()){
                    attachmentID = rs.getInt(1);
                }

                attachmentNames.put(attachmentID, fileName);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            util.close();
        }

        return attachmentNames;
    }

    public boolean delete(int postId){

        boolean deleted = false;
        String sql = "delete from userpost where id=?";
        JDBCutils util = new JDBCutils();
        PreparedStatement ps = util.createStatement(sql);
        try {
            ps.setInt(1, postId);
            int deletedRow = ps.executeUpdate();
            if(deletedRow == 1)
                deleted = true;

            ps = util.creatConnection().prepareStatement("delete from attachments where post_ID = ?");
            ps.setInt(1, postId);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            util.close();
        }
        return deleted;
    }

    public boolean deleteAttach(int attachmentID){

        boolean deleted = false;
        String sql = "delete from attachments where attachment_ID=?";
        JDBCutils util = new JDBCutils();
        PreparedStatement ps = util.createStatement(sql);
        try {
            ps.setInt(1, attachmentID);
            int deletedRow =ps.executeUpdate();

            if(deletedRow == 1)
                deleted = true;
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            util.close();
        }
        return deleted;
    }

    public void update(int postId,String post,String date, String title, String group){

        String sql = "update userpost set post=?,last_modified=?,title=?,`update`=?, post_group=? where id=?";
        JDBCutils util = new JDBCutils();
        PreparedStatement ps = util.createStatement(sql);
        try {
            ps.setString(1, post);
            //ps.setString(2, user);
            ps.setString(2, date);
            ps.setString(3,title);
            int update = 1;
            ps.setInt(4, update);
            ps.setString(5,group);
            ps.setInt(6,postId);


            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            util.close();
        }

    }

    public HashMap<Integer,String> replaceAttach(int attachmentID, ArrayList<Attachment> attachments, String uploadPath, String date, int postID){
        HashMap<Integer,String> attachmentNames = new HashMap<Integer, String>();
        String sql = "update attachments set attachment_name=null, attachment_file_size = null, attachment_mediatype=null, attachment_file=null where attachment_ID=?";
        JDBCutils util = new JDBCutils();
        PreparedStatement ps = null;
        try{
            for(Attachment attachment: attachments){
                ps = util.createStatement(sql);
                ps.setInt(1,attachmentID);

                ps.executeUpdate();

                ps = util.creatConnection().prepareStatement("update attachments set attachment_name=?, attachment_file_size = ?, attachment_mediatype=?, attachment_file=? where attachment_ID=?");

                String fileName = attachment.getFileName();
                long fileSize = attachment.getFileSize();
                String mediatype = attachment.getMediatype();

                ps.setString(1, fileName);
                ps.setLong(2, fileSize);
                ps.setString(3, mediatype);

                File file = new File(uploadPath + File.separator + fileName);
                FileInputStream input = null;
                try {
                    input = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ps.setBinaryStream(4,input);
                ps.setInt(5,attachmentID);
                ps.executeUpdate();

                attachmentNames.put(attachmentID, fileName);
            }

            ps = util.creatConnection().prepareStatement("update UserPost set last_modified=? where id=?");
            ps.setString(1, date);
            ps.setInt(2, postID);
            ps.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            util.close();
        }

        return attachmentNames;
    }

    public void loadSpecificUserPost(String UserName,PostManager newPM){
        String uid;
        String postMessage;
        String userName;
        String timeDate;
        JDBCutils util = new JDBCutils();
        String sql = "select * from userpost where user=?";
        PreparedStatement ps = util.createStatement(sql);
        ResultSet rs = null;
        try {
            ps.setString(1, UserName);

            rs = ps.executeQuery();
            while(rs.next()){
                uid  = Integer.toString(rs.getInt("id"));
                postMessage = rs.getString("post");
                userName = rs.getString("user");
                timeDate = rs.getString("date");
                //newPM.createPost(userName,postMessage);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            util.close();
        }

    }

    public ArrayList<String> getPost(int postId){
        ArrayList<String> postData = new ArrayList<String>();
        JDBCutils util = new JDBCutils();
        String sql = "select * from userpost where id=?";
        PreparedStatement ps = util.createStatement(sql);
        ResultSet rs = null;
        try {
            ps.setInt(1, postId);

            rs = ps.executeQuery();
            while(rs.next()){
                postData.add(rs.getString("post"));
                postData.add(rs.getString("last_modified"));
                postData.add(rs.getString("title"));
                postData.add(rs.getString("post_group"));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            util.close();
        }
        return postData;
    }

    public String getContentType(int postID, int attachmentID){
        String contentType = null;
        JDBCutils util = new JDBCutils();
        String sql = "select attachment_mediatype from attachments where attachment_ID=? and post_ID=?";
        PreparedStatement ps = util.createStatement(sql);
        ResultSet rs = null;
        try{
            ps.setInt(1,attachmentID);
            ps.setInt(2,postID);

            rs = ps.executeQuery();
            while (rs.next()){
                contentType = rs.getString("attachment_mediatype");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            util.close();
        }
        return contentType;
    }

    public String getFileName(int postID, int attachmentID){
        String fileName = null;
        JDBCutils util = new JDBCutils();
        String sql = "select attachment_name from attachments where attachment_ID=? and post_ID=?";
        PreparedStatement ps = util.createStatement(sql);
        ResultSet rs = null;
        try{
            ps.setInt(1,attachmentID);
            ps.setInt(2,postID);

            rs = ps.executeQuery();
            while (rs.next()){
                fileName = rs.getString("attachment_name");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            util.close();
        }
        return fileName;
    }

    public void downloadFile(int postID, int attachmentID, String fileName, OutputStream out){
        JDBCutils util = new JDBCutils();
        String sql = "select attachment_file from attachments where attachment_ID=? and post_ID=?";
        PreparedStatement ps = util.createStatement(sql);
        ResultSet rs = null;
        try{
            ps.setInt(1,attachmentID);
            ps.setInt(2,postID);

            rs = ps.executeQuery();

            InputStream in = null;

            try{
                if(rs.next()){
                    in = rs.getBinaryStream("attachment_file");

                    byte[] buffer = new byte[1024*10];
                    while(in.read(buffer) > 0){
                        out.write(buffer);
                    }
                }
                in.close();
                out.flush();

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            util.close();
        }
    }

    public void loadAllPost(PostManager pm){

        int postID;
        String postMessage;
        String userName;
        String postedDate;
        String updatedDate = "";
        String postTitle;
        String group;
        int mupdate;
        boolean updated;
        JDBCutils util = new JDBCutils();

        String sql = "select * from UserPost";
        PreparedStatement ps = util.createStatement(sql);
        ResultSet rs = null;
        try {
            rs = ps.executeQuery(sql);

            while(rs.next()){
                postID = rs.getInt("id");
               postMessage = rs.getString("post");
               userName = rs.getString("user");
               postedDate = rs.getString("date");
               postTitle = rs.getString("title");
               mupdate = rs.getInt("update");
               group = rs.getString("post_group");
               if(mupdate==0){
                   updated=false;
               }
               else{
                   updated=true;
                   updatedDate = rs.getString("last_modified");
               }

               HashMap<Integer,String> attachmentNames = new HashMap<Integer, String>();
               ResultSet attachmentIDS = null;
               ps = util.createStatement("select attachment_ID,attachment_name from attachments where post_ID = ?");
               ps.setInt(1, postID);
               attachmentIDS = ps.executeQuery();

               int attachmentID = 0;
               String attachmentName = "";
               while(attachmentIDS.next()){
                   attachmentID = attachmentIDS.getInt(1);
                   attachmentName = attachmentIDS.getString(2);
                   attachmentNames.put(attachmentID, attachmentName);
               }

               pm.createPost(postID, userName, postMessage, postedDate, postTitle, attachmentNames,updated, updatedDate, group);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            util.close();
        }

    }
}
