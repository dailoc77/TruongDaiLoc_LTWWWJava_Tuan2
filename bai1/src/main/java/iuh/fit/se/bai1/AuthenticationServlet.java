package iuh.fit.se.bai1;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpFilter;

import java.io.IOException;

public class AuthenticationServlet extends HttpFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String usernameConfig = getFilterConfig().getInitParameter("username");
        String passwordConfig = getFilterConfig().getInitParameter("password");

        boolean authenticated = usernameConfig.equals(username) && passwordConfig.equals(password);
        req.setAttribute("authenticated", authenticated);
        chain.doFilter(req, res);
    }
}
