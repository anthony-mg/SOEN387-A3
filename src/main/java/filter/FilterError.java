package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/Error.jsp")
public class FilterError implements Filter {


    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        String uri = request.getRequestURI();
        String refer = request.getHeader("Referer");
        System.out.println(uri);
        System.out.println(refer);
        request.setAttribute("errorMsg", "Register is not required...");
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }
    public void destroy() {

    }

}
