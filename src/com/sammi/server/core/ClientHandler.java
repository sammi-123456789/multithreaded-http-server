package com.sammi.server.core;

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
            String requestLine = in.readLine();
            System.out.println(Thread.currentThread().getName() + " -> " + requestLine);

            byte[] body = "Hello World".getBytes(StandardCharsets.UTF_8);
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