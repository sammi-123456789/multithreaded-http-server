package com.sammi.server.http;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {
    private final HttpStatus status;
    private final Map<String, String> headers = new LinkedHashMap<>();
    private byte[] body = new byte[0];

    public HttpResponse(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    // Plain text response banane ka shortcut
    public static HttpResponse text(HttpStatus status, String text) {
        HttpResponse response = new HttpResponse(status);
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        response.setBody(text.getBytes(StandardCharsets.UTF_8));
        return response;
    }

    // JSON response banane ka shortcut
    public static HttpResponse json(HttpStatus status, String json) {
        HttpResponse response = new HttpResponse(status);
        response.setHeader("Content-Type", "application/json; charset=UTF-8");
        response.setBody(json.getBytes(StandardCharsets.UTF_8));
        return response;
    }

    // Response ko HTTP format mein client ko bhejo
    public void send(OutputStream out) throws IOException {
        headers.put("Content-Length", String.valueOf(body.length));
        headers.put("Connection", "close");

        StringBuilder head = new StringBuilder();
        head.append("HTTP/1.1 ").append(status.getCode())
                .append(" ").append(status.getReason()).append("\r\n");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            head.append(header.getKey()).append(": ")
                    .append(header.getValue()).append("\r\n");
        }
        head.append("\r\n");

        out.write(head.toString().getBytes(StandardCharsets.UTF_8));
        out.write(body);
        out.flush();
    }
}