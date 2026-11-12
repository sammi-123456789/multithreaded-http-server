package com.sammi.server.core;

import com.sammi.server.http.HttpRequest;
import com.sammi.server.http.HttpRequestParser;
import com.sammi.server.http.HttpResponse;
import com.sammi.server.http.HttpStatus;

import java.io.*;
import java.net.Socket;

public class ClientHandler {
    private final Socket client;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    public void handle() {
        try (client) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            OutputStream out = client.getOutputStream();

            HttpResponse response;
            try {
                HttpRequest request = HttpRequestParser.parse(in);
                if (request == null) {
                    return;
                }
                response = buildResponse(request);
                System.out.println(Thread.currentThread().getName() + " -> "
                        + request.getMethod() + " " + request.getPath()
                        + " " + response.getStatus().getCode());
            } catch (IOException e) {
                response = HttpResponse.text(HttpStatus.BAD_REQUEST, "400 Bad Request");
            } catch (Exception e) {
                response = HttpResponse.text(HttpStatus.INTERNAL_SERVER_ERROR,
                        "500 Internal Server Error");
            }

            response.send(out);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Abhi ke liye simple logic. Agle step mein iski jagah Router aayega.
    private HttpResponse buildResponse(HttpRequest request) {
        if (!request.getMethod().equals("GET")) {
            return HttpResponse.text(HttpStatus.METHOD_NOT_ALLOWED, "405 Method Not Allowed");
        }
        if (request.getPath().equals("/")) {
            return HttpResponse.text(HttpStatus.OK, "Hello World");
        }
        return HttpResponse.text(HttpStatus.NOT_FOUND, "404 Not Found: " + request.getPath());
    }
}