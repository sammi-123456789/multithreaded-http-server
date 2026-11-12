package com.sammi.server.http;

import java.util.Map;

public class MimeTypeResolver {
    private static final Map<String, String> TYPES = Map.of(
            "html", "text/html; charset=UTF-8",
            "css", "text/css; charset=UTF-8",
            "js", "application/javascript; charset=UTF-8",
            "json", "application/json; charset=UTF-8",
            "png", "image/png",
            "jpg", "image/jpeg",
            "jpeg", "image/jpeg",
            "gif", "image/gif",
            "svg", "image/svg+xml",
            "ico", "image/x-icon"
    );

    public static String resolve(String fileName) {
        int dot = fileName.lastIndexOf('.');
        if (dot == -1) {
            return "application/octet-stream";
        }
        String extension = fileName.substring(dot + 1).toLowerCase();
        return TYPES.getOrDefault(extension, "application/octet-stream");
    }
}