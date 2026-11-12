package com.sammi.server;

import com.sammi.server.core.TcpServer;
import com.sammi.server.http.HttpResponse;
import com.sammi.server.http.HttpStatus;
import com.sammi.server.routing.Router;

import java.time.LocalDateTime;

public class App {
    public static void main(String[] args) throws Exception {
        Router router = new Router();

        router.addRoute("GET", "/", req -> HttpResponse.text(HttpStatus.OK, "Hello World"));

        router.addRoute("GET", "/health", req -> HttpResponse.json(HttpStatus.OK,
                "{\"status\":\"healthy\",\"timestamp\":\"" + LocalDateTime.now() + "\"}"));

        router.addRoute("GET", "/time", req -> HttpResponse.json(HttpStatus.OK,
                "{\"time\":\"" + LocalDateTime.now() + "\"}"));

        new TcpServer(8080, router).start();
    }
}