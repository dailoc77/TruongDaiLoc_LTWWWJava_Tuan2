package iuh.fit.se.bai3;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

@WebServlet(name = "uploadServlet", value = "/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {

    private Path uploadDir;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Try context-param first
        String configured = getServletContext().getInitParameter("uploadsPath");
        Path base;
        if (configured != null && !configured.isBlank()) {
            base = Paths.get(configured);
        } else {
            // Attempt to resolve real path inside the deployed webapp
            String real = getServletContext().getRealPath("/uploads");
            if (real != null) {
                base = Paths.get(real);
            } else {
                // Fallback to tmp dir
                base = Paths.get(System.getProperty("java.io.tmpdir"), "uploads");
            }
        }
        try {
            Files.createDirectories(base);
        } catch (IOException e) {
            throw new ServletException("Unable to create upload directory: " + base, e);
        }
        this.uploadDir = base;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String firstName = trimOrNull(req.getParameter("firstName"));
        String lastName = trimOrNull(req.getParameter("lastName"));
        Part filePart = req.getPart("photo");
        String message;

        if (firstName == null || lastName == null) {
            message = "First name and last name are required";
            forwardWithMessage(req, resp, message);
            return;
        }

        byte[] fileBytes = null;
        String submittedName = null;
        if (filePart != null && filePart.getSize() > 0 && filePart.getSubmittedFileName() != null && !filePart.getSubmittedFileName().isBlank()) {
            submittedName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // normalize filename
            try (InputStream in = filePart.getInputStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                in.transferTo(baos);
                fileBytes = baos.toByteArray();
            }
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sqlInsert = "INSERT INTO contacts (first_name, last_name, photo) values (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                if (fileBytes != null) {
                    ps.setBlob(3, new ByteArrayInputStream(fileBytes));
                } else {
                    ps.setNull(3, Types.BLOB);
                }
                int row = ps.executeUpdate();
                if (row > 0) {
                    message = fileBytes != null ? "File uploaded and saved into database" : "Contact saved (no file uploaded)";
                } else {
                    message = "Nothing saved";
                }
            }

            // Write file to disk only if we actually got one
            if (fileBytes != null && submittedName != null) {
                Path target = uploadDir.resolve(submittedName);
                try (OutputStream out = Files.newOutputStream(target)) {
                    out.write(fileBytes);
                }
            }
        } catch (SQLException e) {
            message = "Database error: " + e.getMessage();
        }

        forwardWithMessage(req, resp, message);
    }

    private void forwardWithMessage(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("message", message);
        getServletContext().getRequestDispatcher("/message").forward(req, resp);
    }

    private String trimOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
