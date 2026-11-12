# Multi-Threaded HTTP Server in Java

A lightweight HTTP/1.1 server built from scratch in core Java, without any web framework (no Spring Boot, no Tomcat). It uses raw TCP sockets, a hand-written HTTP parser, a thread pool for concurrent requests, a simple router, and static file serving.

This is a learning project, built to understand how web servers work under the hood.

## Features

- **Raw socket networking** with `ServerSocket` and `Socket`
- **Concurrent request handling** with a fixed thread pool sized at `2 × CPU cores`
- **Hand-written HTTP/1.1 parser** for the request line and headers
- **Router** with lambda-based handlers, returning `404` for unknown paths and `405` for unsupported methods
- **JSON endpoints**: `/health` and `/time`
- **Static file serving** for HTML, CSS, JS and images, with correct MIME types
- **Path traversal protection**: requests like `/../secret` return `403 Forbidden`
- **Error handling**: malformed requests return `400`, unexpected errors return `500`

## Architecture

```
Browser / curl
  -> TcpServer        (accepts connections, hands them to the thread pool)
    -> ClientHandler  (one request per thread)
      -> HttpRequestParser  (raw text -> HttpRequest object)
        -> Router     (matches method + path)
          -> RouteHandler lambda  or  StaticFileHandler
            -> HttpResponse  (status line + headers + body -> socket)
```

## Project Structure

```
src/com/sammi/server/
├── App.java                     # Entry point, route registration
├── core/
│   ├── TcpServer.java           # Accept loop + thread pool
│   └── ClientHandler.java       # Handles one client connection
├── http/
│   ├── HttpRequest.java         # Request model
│   ├── HttpRequestParser.java   # Parses raw HTTP into HttpRequest
│   ├── HttpResponse.java        # Builds and sends responses
│   ├── HttpStatus.java          # Status code enum
│   └── MimeTypeResolver.java    # File extension -> Content-Type
└── routing/
    ├── RouteHandler.java        # Functional interface for routes
    ├── Router.java              # Route registry and dispatch
    └── StaticFileHandler.java   # Serves files from /static safely
static/                          # Demo website (HTML, CSS, SVG)
```

## Run Locally

Requires Java 17+. From the project root (Windows PowerShell):

```powershell
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out com.sammi.server.App
```

On macOS/Linux:

```bash
javac -d out $(find src -name "*.java")
java -cp out com.sammi.server.App
```

Then open http://localhost:8080

## Try It

```bash
curl -i http://localhost:8080/health                 # 200, JSON
curl -i http://localhost:8080/does-not-exist         # 404
curl -i -X POST http://localhost:8080/health         # 405
curl -i --path-as-is http://localhost:8080/../src/com/sammi/server/App.java   # 403
```

## Design Decisions

| Decision | Reason |
|---|---|
| No framework | To learn TCP, HTTP and concurrency at a low level |
| Fixed thread pool | Reuses threads and avoids creating unlimited threads under load |
| Pool size `2 × CPU` | Request handling mixes CPU work with waiting on network I/O |
| Blocking I/O | Simpler to understand than non-blocking NIO at this scale |
| `normalize()` + `startsWith()` for files | Prevents path traversal outside the static folder |

## Future Improvements

- HTTP keep-alive connections
- POST request body parsing
- JUnit tests for the parser and router
- Graceful shutdown

## Acknowledgements

The overall structure was inspired by [ayushTripathy-o1/Multi-threaded-HTTP-Server](https://github.com/ayushTripathy-o1/Multi-threaded-HTTP-Server). The code in this repository was written step by step as a learning exercise.