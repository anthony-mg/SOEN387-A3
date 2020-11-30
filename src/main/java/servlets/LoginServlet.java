package servlets;



import businesslayer.authentification.UserAuthentificator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(value = "/LoginServlet")
public class LoginServlet extends HttpServlet {

    public UserAuthentificator ua;
    public void init() {
        
        ua = new UserAuthentificator(getServletContext().getRealPath("/WEB-INF/users.json").toString());

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean result = false;
        String email = request.getParameter("email");
        String username = ua.getUsernameFromEmail(email);
        String password = request.getParameter("password");

        System.out.println("In Login Servlet:" + password);
        HttpSession session = request.getSession();
        result = ua.verifyUser(email,password);
        if(result) {
            session.setAttribute("email",email);
            session.setAttribute("user", username);
            session.setAttribute("welcome", true);
            response.sendRedirect(request.getContextPath() + "/posts");
        }else{
            request.setAttribute("loginError","Email or Password is incorrect");
            request.getRequestDispatcher("/LoginPage.jsp").forward(request,response);
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
