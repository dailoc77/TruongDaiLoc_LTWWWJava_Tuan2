package iuh.fit.se.bai4;

import java.io.*;

import com.google.gson.Gson;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

/**
 * @author Dai Loc
 */

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        User user = new User("Dai Loc", "truongdailoc.dev@gmail.com", 22);
        Gson gson = new Gson();
        String json = gson.toJson(user);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    public void destroy() {
    }
}