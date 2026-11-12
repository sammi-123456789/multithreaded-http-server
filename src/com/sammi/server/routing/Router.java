package com.sammi.server.routing;

import com.sammi.server.http.HttpRequest;
import com.sammi.server.http.HttpResponse;
import com.sammi.server.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, Map<String, RouteHandler>> routes = new HashMap<>();
    private StaticFileHandler staticFiles;

    public void addRoute(String method, String path, RouteHandler handler) {
        routes.computeIfAbsent(path, p -> new HashMap<>()).put(method, handler);
    }

    public void setStaticFileHandler(StaticFileHandler staticFiles) {
        this.staticFiles = staticFiles;
    }

    public HttpResponse route(HttpRequest request) {
        Map<String, RouteHandler> handlersForPath = routes.get(request.getPath());

        if (handlersForPath == null) {
            // Koi route nahi mila: static file try karo
            if (staticFiles != null && request.getMethod().equals("GET")) {
                HttpResponse fileResponse = staticFiles.serve(request.getPath());
                if (fileResponse != null) {
                    return fileResponse;
                }
            }
            return HttpResponse.text(HttpStatus.NOT_FOUND,
                    "404 Not Found: " + request.getPath());
        }

        RouteHandler handler = handlersForPath.get(request.getMethod());
        if (handler == null) {
            return HttpResponse.text(HttpStatus.METHOD_NOT_ALLOWED,
                    "405 Method Not Allowed");
        }

        return handler.handle(request);
    }
}