package com.sammi.server;

import com.sammi.server.core.TcpServer;

public class App {
    public static void main(String[] args) throws Exception {
        new TcpServer(8080).start();
    }
}