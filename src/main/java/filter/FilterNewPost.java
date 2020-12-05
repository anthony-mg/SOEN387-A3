package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/newPost")
public class FilterNewPost implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        //String uri = request.getRequestURI();
        String refer = request.getHeader("Referer");

        if(refer==null){
            request.setAttribute("errorMsg", "Cannot do post! Coz you skip the login process!!");
            request.getRequestDispatcher("/Error.jsp").forward(request,resp);
        }else{
            Object user = request.getSession().getAttribute("user");
            if(user!=null){
                chain.doFilter(req,resp);
            }else{
                request.setAttribute("errorMsg", "Invalid user wants to add new post!");
                request.getRequestDispatcher("/Error.jsp").forward(request,resp);
            }
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
