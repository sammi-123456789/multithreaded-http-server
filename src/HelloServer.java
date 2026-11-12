import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        int poolSize = Runtime.getRuntime().availableProcessors() * 2;
        ExecutorService threadPool = Executors.newFixedThreadPool(poolSize);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on http://localhost:" + port
                    + " with " + poolSize + " threads");

            while (true) {
                Socket client = serverSocket.accept();
                threadPool.submit(() -> handleClient(client));
            }
        }
    }

    private static void handleClient(Socket client) {
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