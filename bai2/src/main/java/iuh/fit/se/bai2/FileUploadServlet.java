package iuh.fit.se.bai2;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uploadsPath = getServletContext().getRealPath("/WEB-INF/uploads");
        File uploads = new File(uploadsPath);
        if (!uploads.exists()) uploads.mkdirs();

        System.out.println("Upload folder: " + uploads.getAbsolutePath());

        boolean fileSaved = false;

        // Debug: Print all part names
        for (Part part : req.getParts()) {
            System.out.println("Part name: " + part.getName() + ", size: " + part.getSize());
        }

        for (Part part : req.getParts()) {
            if (part.getName().equals("file") && part.getSize() > 0) {
                String filename = part.getSubmittedFileName();
                File outFile = new File(uploads, filename);
                try (InputStream fileContent = part.getInputStream();
                     FileOutputStream out = new FileOutputStream(outFile)) {
                    fileContent.transferTo(out);
                    fileSaved = true;
                    System.out.println("Saved file: " + outFile.getAbsolutePath());
                } catch (Exception ex) {
                    System.err.println("Error saving file: " + ex.getMessage());
                    resp.getWriter().println("Error saving file: " + ex.getMessage());
                    return;
                }
            }
        }
        if (fileSaved) {
            resp.getWriter().println("Files uploaded successfully");
        } else {
            resp.getWriter().println("No file uploaded or file part not found.");
        }
    }
}