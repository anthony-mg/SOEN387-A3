package servlets;

import dao.UserPostDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/FileDownloadServlet")
public class FileDownloadServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int postID = Integer.parseInt(request.getParameter("postId"));
        int attachmentID = Integer.parseInt(request.getParameter("attachmentId"));

        UserPostDao upd = new UserPostDao();
        String fileName = upd.getFileName(postID, attachmentID);
        response.setContentType(upd.getContentType(postID, attachmentID));
        response.setHeader("Content-Disposition", "attachment; fileName= " + fileName);
        disableCache(response);

        OutputStream out = response.getOutputStream();

        upd.downloadFile(postID, attachmentID, fileName, out);


    }

    public void disableCache(HttpServletResponse response){
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setDateHeader("Expires", 0);
    }
}
