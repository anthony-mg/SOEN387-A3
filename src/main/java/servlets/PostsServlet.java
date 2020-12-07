package servlets;
import businesslayer.messageboardmanager.Attachment;
import businesslayer.messageboardmanager.Post;
import businesslayer.messageboardmanager.PostManager;
import helpers.*;
import dao.UserPostDao;
import helpers.XmlAttachment;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;


@WebServlet(name="PostsServlet", urlPatterns = {"/posts", "/view", "/edit", "/delete", "/deleteAttach", "/newPost", "/searchPage", "/addPost", "/modify","/editAttach", "/searchPosts", "/download", "/viewXml"})
@MultipartConfig(fileSizeThreshold = 1024*1024*10, maxFileSize = 1024*1024*30, maxRequestSize = 1024*1024*50)
public class PostsServlet extends HttpServlet {
    private static final long serialVersionUID = 4L;

    public PostManager pm;
    public ArrayList<Post> test = new ArrayList<Post>();
    public ArrayList<String> tagpool = new ArrayList<String>();
    public ArrayList<String> hashTags = new ArrayList<String>();
    private ZonedDateTime from;
    private ZonedDateTime to;
    private final int BUFFER_SIZE = 4096; //needed to read the chat file as a byte-stream

    //Init method to initialize and setup dummy posts for testing
    public void init() {
        pm = new PostManager();
        UserPostDao upd = new UserPostDao();
        upd.loadAllPost(pm);
    }

    //Post method to modify and add a post
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        checkAccessLevel(request, response);
        String requestAction = request.getServletPath();

        switch (requestAction) {
            case "/modify":
                modifyPost(request, response);
                break;
            case "/addPost":
                addPost(request, response);
                break;
            case "/editAttach":
                editAttachment(request, response);
                break;
            case "/searchPosts":
                searchPosts(request, response);
                break;
        }
    }

    //Get method to display posts when app loads from first time.
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        checkAccessLevel(request, response);
        String path = request.getServletPath();

        //Switch on every path to display posts, edit post form, add new post form and delete a post
        switch (path) {
            case "/posts":
                displayPosts(request, response);
                break;
            case "/view":
                viewPost(request, response);
                break;
            case "/edit":
                displayEditPost(request, response);
                break;
            case "/newPost":
                displayNewPost(request, response);
                break;
            case "/delete":
                deletePost(request, response);
                break;
            case "/deleteAttach":
                deleteAttach(request, response);
                break;
            case "/searchPage":
                displaySearchPage(request, response);
                break;
            case "/download":
                downloadPost(request, response);
                break;
            case "/viewXml":
                viewXml(request, response);
                break;
            default:
                request.getRequestDispatcher("posts/list-posts.jsp").forward(request, response);
                break;
        }

    }

    public void checkAccessLevel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession ses = request.getSession();
        String userName = (String)ses.getAttribute("user");
        if(userName == null || userName.isEmpty()){
            try {
                request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }

    //Method to display posts
    private void displayPosts(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ArrayList<String> permissions = getUserPermissions(request);
        ArrayList<Post> posts = pm.search_by_groups(permissions);
        if(posts.isEmpty()){
            HttpSession session = request.getSession();
            session.setAttribute("emptyPosts", true);
        }

        request.setAttribute("posts", posts);
        request.getRequestDispatcher("posts/list-posts.jsp").forward(request, response);
    }

    //Method to display a new post form
    private void displayNewPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<String> permissions = getUserPermissions(request);
        request.setAttribute("groups", permissions);
        request.getRequestDispatcher("/posts/post-data.jsp").forward(request, response);
    }

    private void viewPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Post post = pm.search(Integer.parseInt(request.getParameter("postId")));
        HttpSession ses = request.getSession();
        String userName = (String)ses.getAttribute("user");
        ArrayList<String> permissions = getUserPermissions(request);
        boolean isAdmin = permissions.contains("admin");

        request.setAttribute("helper", new PostHelper(post, userName, isAdmin));
        request.getRequestDispatcher("posts/post-view.jsp").forward(request, response);
    }

    private PostXML transformXml(Post post, HashMap<Integer, String> attachments){
        PostXML postXml = new PostXML();
        postXml.setPostId(post.getPostId());
        postXml.setUser(post.getUser());
        postXml.setTitle(post.getTitle());
        postXml.setGroup(post.getGroup());
        postXml.setCreatedDate(post.getDateString());
        postXml.setText(post.getText());
        if(post.isUpdated())
            postXml.setUpdatedDate(post.getUpdatedDate());

        if(!attachments.isEmpty()) {
            for (Map.Entry file : attachments.entrySet()) {
                XmlAttachment attachment = new XmlAttachment();
                attachment.setAttachmentId((Integer) file.getKey());
                attachment.setAttachmentName((String) file.getValue());
                postXml.addAttachment(attachment);
            }
        }
        return postXml;
    }

    private void viewXml(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Post post = pm.search(Integer.parseInt(request.getParameter("postId")));
        HashMap<Integer, String> attachments = post.getAttachmentNames();
        ByteArrayOutputStream postOutput = new ByteArrayOutputStream();
        PostXML postXml = transformXml(post, attachments);

        try {
            response.setContentType("application/xml;charset=UTF-8");
            JAXBContext jaxbContext = JAXBContext.newInstance(PostXML.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.marshal(postXml, postOutput);
            jaxbMarshaller.marshal(postXml, System.out);
            OutputStream os = response.getOutputStream();
            postOutput.writeTo(os);

        } catch (JAXBException e){
            e.printStackTrace();
        }

    }

    private void downloadPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Post post = pm.search(Integer.parseInt(request.getParameter("postId")));
        HashMap<Integer, String> attachments = post.getAttachmentNames();
        PostXML postXml = transformXml(post, attachments);

        try {
            File postFile = new File("post.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(PostXML.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.marshal(postXml, postFile);
            jaxbMarshaller.marshal(postXml, System.out);
            FileInputStream fileStream = new FileInputStream(postFile);
            String mimeType = (getServletContext().getMimeType("post.xml") == null) ? "application/octet-stream" : getServletContext().getMimeType("post.xml");

            response.setContentType(mimeType);
            response.setContentLength((int) postFile.length());
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", postFile.getName()));
            disableCache(response);

            //Write the file as a stream of bytes
            OutputStream fileOutput = response.getOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = fileStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bytesRead);
            }

            fileStream.close();
            fileOutput.close();
        } catch (JAXBException e){
            e.printStackTrace();
        }

        //request.getRequestDispatcher("posts/post-view.jsp").forward(request, response);
    }

    public void disableCache(HttpServletResponse response){
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setDateHeader("Expires", 0);
    }

    //Method to display the edit post form
    private void displayEditPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Post post = pm.search(Integer.parseInt(request.getParameter("postId")));
        ArrayList<String> permissions = getUserPermissions(request);
        request.setAttribute("groups", permissions);
        request.setAttribute("attachmentNames", post.getAttachmentNames());
        request.setAttribute("post", post);
        request.getRequestDispatcher("posts/post-data.jsp").forward(request, response);
    }

    //Method to display a new post form
    private void displaySearchPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getHashTags();
        request.setAttribute("hashTags", hashTags);
        request.getRequestDispatcher("posts/search.jsp").forward(request, response);
    }

    //Method to get all the hashTags
    private void getHashTags(){
        ArrayList<Post> posts = pm.getAllPosts();
        hashTags.clear();

        for(Post p: posts) {
            if(!p.getHashtags().isEmpty())
                hashTags.addAll(p.getHashtags());
		}
    }

    //Get user group permissions
    @SuppressWarnings("unchecked")
    private ArrayList<String> getUserPermissions(HttpServletRequest request){
        ArrayList<String> permissions = new ArrayList<>();
        if(request.getSession().getAttribute("permissions") instanceof ArrayList) {
            permissions = (ArrayList<String>)request.getSession().getAttribute("permissions");
        }

        return permissions;
    }

    //Method to modify post
    private void modifyPost(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException {
        // new attachments
        ArrayList<Attachment> attachmentFiles = new ArrayList<Attachment>();
        String uploadPath = getServletConfig().getServletContext().getRealPath("WEB-INF" + File.separator+"attachments");
        attachmentFiles = uploadFile(uploadPath, request, attachmentFiles);
        int postID = Integer.parseInt(request.getParameter("postId"));
        String postText = request.getParameter("postText");
        String title = request.getParameter("title");
        ZonedDateTime date = ZonedDateTime.now();
        String newPostDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'").format(date);
        String group = request.getParameter("groups");

        pm.updatePost(postID, title, postText, newPostDate, attachmentFiles, uploadPath, group);
        HttpSession session = request.getSession();
        session.setAttribute("modified", true);
        response.sendRedirect("posts");
    }

    //Method to add a new post
    private void addPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        ArrayList<Attachment> attachmentFiles = new ArrayList<Attachment>();

        String title = request.getParameter("title");
        String postText = request.getParameter("postText");
        String user = (String)request.getSession().getAttribute("user");
        ZonedDateTime date = ZonedDateTime.now();
        String newPostDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'").format(date);
        String group = request.getParameter("groups");

        // Upload the file into a directory
        String uploadPath = getServletConfig().getServletContext().getRealPath("WEB-INF" + File.separator+"attachments");
        attachmentFiles = uploadFile(uploadPath, request, attachmentFiles);

        HttpSession session = request.getSession();
        Post newPost = pm.createPost(user, postText, newPostDate, title, attachmentFiles, uploadPath, group);
        if(newPost != null)
            session.setAttribute("added", true);

        response.sendRedirect("posts");
    }

    public void editAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException{
        ArrayList<Attachment> attachmentFiles = new ArrayList<Attachment>();
        int attachmentID = Integer.parseInt(request.getParameter("attachmentId"));
        int postID = Integer.parseInt(request.getParameter("postId"));
        ZonedDateTime date = ZonedDateTime.now();
        String newPostDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'").format(date);

        // Save the new attachment in a dir.
        String uploadPath = getServletConfig().getServletContext().getRealPath("WEB-INF" + File.separator+"attachments");
        attachmentFiles = uploadFile(uploadPath, request, attachmentFiles);

        HttpSession session = request.getSession();
        session.setAttribute("modified", true);
        pm.replaceAttachment(attachmentID, postID, newPostDate, attachmentFiles, uploadPath);

        String currentPage = "edit?postId=" + postID;
        response.sendRedirect(currentPage);
    }

    //Method to delete a post
    private void deletePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int postID = Integer.parseInt(request.getParameter("postId"));

        if(pm.validateDelete(postID)){
            pm.deletePost(postID);
            HttpSession session = request.getSession();
            session.setAttribute("deleted", true);
        }

        response.sendRedirect("posts");
    }

    private void deleteAttach(HttpServletRequest request, HttpServletResponse response) throws IOException{
        int attachmentID = Integer.parseInt(request.getParameter("attachmentID"));
        int postID = Integer.parseInt(request.getParameter("postId"));

        if(pm.validateAttachIDDelete(attachmentID)){
            pm.deleteAttach(attachmentID);
            HttpSession session = request.getSession();
            session.setAttribute("deletedAttachment", true);
        }

        String currentPage = "edit?postId=" + postID;
        response.sendRedirect(currentPage);
    }

    private void searchPosts(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ArrayList<Post> posts = null;
        ArrayList<String> searchTags = new ArrayList<String>();
        ArrayList<String> permissions = getUserPermissions(request);
        String user = request.getParameter("user");
        String dateFrom = request.getParameter("from");
        String dateTo = request.getParameter("to");
        boolean datesSelected = dateFrom != null && !dateFrom.isEmpty() && dateTo != null && !dateTo.isEmpty();
        if(datesSelected)
            setDateRange(dateFrom, dateTo);

        if(!(request.getParameterValues("hashTags") == null))
            Collections.addAll(searchTags, request.getParameterValues("hashTags"));

        //If the user, date range and hashtags were selected, the results will be processed here
        if(user != null && !user.isEmpty()){
            if(datesSelected){
                if(!searchTags.isEmpty())
                    posts = pm.search(user, searchTags, from, to, permissions);
                else
                    posts = pm.search(user, from, to, permissions);
            }
            else if(!searchTags.isEmpty()){
                posts = pm.search(user, searchTags, permissions);
            }

            else
                posts = pm.searchByUser(user, permissions);
        }

        else if(datesSelected){
            if(!searchTags.isEmpty()){
                posts = pm.search(searchTags, from, to, permissions);
            }
            else
                posts = pm.search(from, to, permissions);
        }

        else if(!searchTags.isEmpty()){
            posts = pm.search(searchTags, permissions);
        }

        else
            posts = pm.search_by_groups(permissions);

        if(posts == null || posts.isEmpty()){
            HttpSession session = request.getSession();
            session.setAttribute("emptyPosts", true);
        }

        request.setAttribute("posts", posts);
        request.getRequestDispatcher("posts/list-posts.jsp").forward(request, response);
    }

    private void setDateRange(String dateFrom, String dateTo){
        ZoneId timeZone = ZoneId.of("UCT");
        from = LocalDate.parse(dateFrom,
                DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay().atZone(timeZone);
        to = LocalDate.parse(dateTo,
                DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(LocalTime.MAX).atZone(timeZone);
    }

    private ArrayList<Attachment> uploadFile(String uploadPath, HttpServletRequest request, ArrayList<Attachment> attachmentFiles) throws ServletException, IOException{
        File attachments = new File(uploadPath);
        Attachment attachment = null;

        if(!attachments.exists())
            attachments.mkdirs();

        Collection<Part> parts = request.getParts();

        for(Part part : parts){
            String fileName = part.getSubmittedFileName();

            if(fileName == null || fileName.isEmpty()){
                continue;
            }

            long fileSize = part.getSize();
            String mediaType = part.getContentType();

            attachment = new Attachment(fileName, fileSize, mediaType);

            attachmentFiles.add(attachment);
            part.write(uploadPath + File.separator + fileName);
        }

        return attachmentFiles;
    }
}
