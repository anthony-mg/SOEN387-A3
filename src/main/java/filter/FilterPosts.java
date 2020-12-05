package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/posts")
public class FilterPosts implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        //String uri = request.getRequestURI();
        String refer = request.getHeader("Referer");
//        System.out.println("Enter filterDemo 1");
//        System.out.println(uri);
//        System.out.println(refer);
//        System.out.println("-----------");
        if(refer==null){
            request.setAttribute("errorMsg", "Enter from invalid page");
            request.getRequestDispatcher("/Error.jsp").forward(request,resp);
        }else{
            Object user = request.getSession().getAttribute("user");
            if(user!=null){
                chain.doFilter(req,resp);
            }else{
                request.setAttribute("errorMsg", "Invalid user enter page");
                request.getRequestDispatcher("/Error.jsp").forward(request,resp);
            }
        }
        //chain.doFilter(req, resp);
//        System.out.println("A");
//        System.out.println("|");
//        System.out.println("|");
//        System.out.println("Enter again filterDemo 1");
//        System.out.println("-----------");
    }

    public void init(FilterConfig config) throws ServletException {

    }

    public void destroy() {
    }

}
