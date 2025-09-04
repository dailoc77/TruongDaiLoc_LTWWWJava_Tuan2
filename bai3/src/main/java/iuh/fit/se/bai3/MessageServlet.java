package iuh.fit.se.bai3;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


/**
 @author DAILOC
 * */

@WebServlet(name = "messageServlet", value = "/message")
public class MessageServlet extends HttpServlet {

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        Object msgObj = req.getAttribute("message");
        String message = msgObj == null ? "No message available" : msgObj.toString();
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html>");
            out.println("<head><title>MessageServlet</title></head>");
            out.println("<body>");
            out.println("<h1>" + escapeHtml(message) + "</h1>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
