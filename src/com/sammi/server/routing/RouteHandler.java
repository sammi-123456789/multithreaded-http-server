package com.sammi.server.routing;

import com.sammi.server.http.HttpRequest;
import com.sammi.server.http.HttpResponse;

@FunctionalInterface
public interface RouteHandler {
    HttpResponse handle(HttpRequest request);
}