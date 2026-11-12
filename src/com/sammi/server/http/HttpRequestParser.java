package com.sammi.server.http;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequestParser {

    public static HttpRequest parse(BufferedReader reader) throws IOException {
        // 1. Request line: "GET /hello HTTP/1.1"
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isBlank()) {
            return null; // client ne kuch nahi bheja
        }

        String[] parts = requestLine.split(" ");
        if (parts.length != 3) {
            throw new IOException("Malformed request line: " + requestLine);
        }

        HttpRequest request = new HttpRequest();
        request.setMethod(parts[0]);
        request.setPath(parts[1]);
        request.setVersion(parts[2]);

        // 2. Headers: khaali line aane tak padho
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            int colon = line.indexOf(':');
            if (colon > 0) {
                String name = line.substring(0, colon).trim();
                String value = line.substring(colon + 1).trim();
                request.addHeader(name, value);
            }
        }

        return request;
    }
}