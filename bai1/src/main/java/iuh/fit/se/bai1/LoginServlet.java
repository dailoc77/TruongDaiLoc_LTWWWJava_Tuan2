package iuh.fit.se.bai1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.println("<html><body>");
        Boolean authenticated = (Boolean) req.getAttribute("authenticated");
        if (authenticated != null && authenticated) {
            writer.println("<h1>Login Successful</h1>");
            writer.println("<p>Welcome, " + username + "!</p>");
        } else {
            writer.println("<h1>Authentication Failed</h1>");
            writer.println("<p>Invalid username or password.</p>");
        }
        writer.println("</body></html>");
    }
}
