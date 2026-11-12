package com.sammi.server.core;

import com.sammi.server.routing.Router;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer {
    private final int port;
    private final Router router;
    private final ExecutorService threadPool;

    public TcpServer(int port, Router router) {
        this.port = port;
        this.router = router;
        int poolSize = Runtime.getRuntime().availableProcessors() * 2;
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on http://localhost:" + port);

            while (true) {
                Socket client = serverSocket.accept();
                ClientHandler handler = new ClientHandler(client, router);
                threadPool.submit(handler::handle);
            }
        }
    }
}