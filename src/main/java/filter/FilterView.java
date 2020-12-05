package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/view")
public class FilterView implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        String refer = request.getHeader("Referer");

        if(refer==null){
            request.setAttribute("errorMsg", "Please login for viewing the page");
            request.getRequestDispatcher("/Error.jsp").forward(request,resp);
        }else{
            Object user = request.getSession().getAttribute("user");
            if(user!=null){
                chain.doFilter(req,resp);
            }else{
                request.setAttribute("errorMsg", "Invalid user wants to view the page");
                request.getRequestDispatcher("/Error.jsp").forward(request,resp);
            }
        }

    }

    public void init(FilterConfig config) throws ServletException {

    }

}
