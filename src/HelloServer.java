import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HelloServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on http://localhost:" + port);

            while (true) {
                try (Socket client = serverSocket.accept()) {
                    // 1. Request padho
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(client.getInputStream()));
                    String requestLine = in.readLine();
                    System.out.println("Request: " + requestLine);

                    // 2. Response banao
                    byte[] body = "Hello World".getBytes(StandardCharsets.UTF_8);
                    String headers = "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: text/plain\r\n"
                            + "Content-Length: " + body.length + "\r\n"
                            + "Connection: close\r\n"
                            + "\r\n";

                    // 3. Response bhejo
                    OutputStream out = client.getOutputStream();
                    out.write(headers.getBytes(StandardCharsets.UTF_8));
                    out.write(body);
                    out.flush();
                }
            }
        }
    }
}