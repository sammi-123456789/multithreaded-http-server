package com.sammi.server.routing;

import com.sammi.server.http.HttpResponse;
import com.sammi.server.http.HttpStatus;
import com.sammi.server.http.MimeTypeResolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StaticFileHandler {
    private final Path rootDir;

    public StaticFileHandler(String directory) {
        this.rootDir = Paths.get(directory).toAbsolutePath().normalize();
    }

    // File mili toh response, nahi mili toh null
    public HttpResponse serve(String requestPath) {
        String relativePath = requestPath.equals("/") ? "index.html" : requestPath.substring(1);
        Path file = rootDir.resolve(relativePath).normalize();

        // Security: static folder ke bahar ki file kabhi mat do
        if (!file.startsWith(rootDir)) {
            return HttpResponse.text(HttpStatus.FORBIDDEN, "403 Forbidden");
        }

        if (!Files.exists(file) || Files.isDirectory(file)) {
            return null;
        }

        try {
            byte[] bytes = Files.readAllBytes(file);
            HttpResponse response = new HttpResponse(HttpStatus.OK);
            response.setHeader("Content-Type",
                    MimeTypeResolver.resolve(file.getFileName().toString()));
            response.setBody(bytes);
            return response;
        } catch (IOException e) {
            return HttpResponse.text(HttpStatus.INTERNAL_SERVER_ERROR,
                    "500 Internal Server Error");
        }
    }
}