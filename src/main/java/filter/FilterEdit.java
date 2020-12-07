package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/edit")
public class FilterEdit implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        //String uri = request.getRequestURI();
        String refer = request.getHeader("Referer");
        if(refer==null){
            request.setAttribute("errorMsg", "Cannot edit anything before you login!!!");
            request.getRequestDispatcher("/Error.jsp").forward(request,resp);
        }else{
            Object user = request.getSession().getAttribute("user");
            if(user!=null){
                chain.doFilter(req,resp);
            }else{
                request.setAttribute("errorMsg", "Invalid user cannot do edit!");
                request.getRequestDispatcher("/Error.jsp").forward(request,resp);
            }
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
