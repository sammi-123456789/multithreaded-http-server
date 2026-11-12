package com.sammi.server.core;

import com.sammi.server.http.HttpRequest;
import com.sammi.server.http.HttpRequestParser;
import com.sammi.server.http.HttpResponse;
import com.sammi.server.http.HttpStatus;
import com.sammi.server.routing.Router;

import java.io.*;
import java.net.Socket;

public class ClientHandler {
    private final Socket client;
    private final Router router;

    public ClientHandler(Socket client, Router router) {
        this.client = client;
        this.router = router;
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
                response = router.route(request);
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
}