package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/FileDownloadServlet")
public class FilterDownLoad implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        String refer = request.getHeader("Referer");

        if(refer==null){
            request.setAttribute("errorMsg", "Wants to directly download? Go Login Page!!");
            request.getRequestDispatcher("/Error.jsp").forward(request,resp);
        }else{
            Object user = request.getSession().getAttribute("user");
            if(user!=null){
                chain.doFilter(req,resp);
            }else{
                request.setAttribute("errorMsg", "Invalid user wants to enter page");
                request.getRequestDispatcher("/Error.jsp").forward(request,resp);
            }
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }
    public void destroy() {

    }

}
