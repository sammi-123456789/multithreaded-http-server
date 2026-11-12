package com.sammi.server.core;

import com.sammi.server.http.HttpRequest;
import com.sammi.server.http.HttpRequestParser;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler {
    private final Socket client;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    public void handle() {
        try (client) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));

            // Request ko parse karo
            HttpRequest request = HttpRequestParser.parse(in);
            if (request == null) {
                return;
            }
            System.out.println(Thread.currentThread().getName() + " -> "
                    + request.getMethod() + " " + request.getPath());

            // Response ka text banao
            String text = "Method: " + request.getMethod() + "\n"
                    + "Path: " + request.getPath() + "\n"
                    + "Browser: " + request.getHeader("User-Agent");
            byte[] body = text.getBytes(StandardCharsets.UTF_8);

            String headers = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/plain\r\n"
                    + "Content-Length: " + body.length + "\r\n"
                    + "Connection: close\r\n"
                    + "\r\n";

            OutputStream out = client.getOutputStream();
            out.write(headers.getBytes(StandardCharsets.UTF_8));
            out.write(body);
            out.flush();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}