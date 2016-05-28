package web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        boolean allowFilterChain = clearSessionId((HttpServletRequest) req, (HttpServletResponse) res);
        if (allowFilterChain) {
            chain.doFilter(req, res);
        }
    }

    public static boolean clearSessionId(HttpServletRequest req, HttpServletResponse res) {
        String requestURI = req.getRequestURI();
        int index = requestURI.indexOf(";JSESSIONID=");
        if (index > 0) {
            try {
                res.sendRedirect(requestURI.substring(0, index));
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}