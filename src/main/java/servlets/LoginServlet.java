package servlets;



import businesslayer.factory.UserManagerFactory;
import exceptions.CircularGroupDefinitionException;
import usermanagerIMP.*;
import util.Configuration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(value = "/LoginServlet")
public class LoginServlet extends HttpServlet {


    //public UserAuthentificator ua;
    public UserManager um;
    public void init() {
        //ua = new UserAuthentificator(getServletContext().getRealPath("/WEB-INF/users.json").toString());
        //um = new UserManager(getServletContext().getRealPath("/WEB-INF/users.json").toString(),getServletContext().getRealPath("/WEB-INF/groups_definition.json").toString());
        um = UserManagerFactory.getInstance().create(getServletContext().getRealPath("/WEB-INF/users.json").toString(),getServletContext().getRealPath("/WEB-INF/groups_definitions.json").toString());
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean result = false;
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if(um==null) {
            if (email.contains("@gmail.com")) {

            } else {
                int i = 1 / 0;
            }
        }else{
            String username = um.getUsernameFromEmail(email);
            HttpSession session = request.getSession();
            result = um.verifyUser(username,password);
            if(result) {
                try {
                    ArrayList<String> permissions = um.getGroupsForUser(username);
                    session.setAttribute("permissions",permissions);
                } catch (CircularGroupDefinitionException e) {
                    e.printStackTrace();
                }
                session.setAttribute("email",email);
                session.setAttribute("user", username);
                session.setAttribute("welcome", true);
                response.sendRedirect(request.getContextPath() + "/posts");
            }else{
                request.setAttribute("loginError","Email or Password is incorrect");
                request.getRequestDispatcher("/LoginPage.jsp").forward(request,response);
            }
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
