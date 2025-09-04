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


/**
 * @author DAILOC
 * **/

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 50 * 1024 * 1024,
        maxRequestSize = 200 * 1024 * 1024
)
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain; charset=UTF-8");

        String ct = req.getContentType();
        if (ct == null || !ct.toLowerCase().startsWith("multipart/")) {
            resp.getWriter().println("Invalid request (not multipart)");
            return;
        }

        String uploadsPath = getServletContext().getRealPath("/WEB-INF/uploads");
        File uploadsDir = new File(uploadsPath);
        if (!uploadsDir.exists()) uploadsDir.mkdirs();

        int saved = 0;
        for (Part part : req.getParts()) {
            if (!"file".equals(part.getName())) continue;
            if (part.getSize() <= 0) continue; // empty or no selection
            String submitted = part.getSubmittedFileName();
            if (submitted == null || submitted.isBlank()) continue;
            String safe = submitted.replaceAll("[\\\\/]+", "_");
            File target = resolveUnique(new File(uploadsDir, safe));
            try (InputStream in = part.getInputStream(); FileOutputStream out = new FileOutputStream(target)) {
                in.transferTo(out);
                saved++;
                System.out.println("Saved file: " + target.getAbsolutePath() + " (" + target.length() + " bytes)");
            }
        }

        if (saved > 0) {
            resp.getWriter().println("Uploaded " + saved + " file(s).");
        } else {
            resp.getWriter().println("No file uploaded.");
        }
    }

    private File resolveUnique(File base) {
        if (!base.exists()) return base;
        String name = base.getName();
        int dot = name.lastIndexOf('.');
        String baseName = (dot == -1) ? name : name.substring(0, dot);
        String ext = (dot == -1) ? "" : name.substring(dot);
        int i = 1;
        File parent = base.getParentFile();
        while (true) {
            File cand = new File(parent, baseName + "(" + i + ")" + ext);
            if (!cand.exists()) return cand;
            i++;
        }
    }
}