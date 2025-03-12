package connection.generichttpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class GenericHTTPServer {
    HttpServer server;

    public GenericHTTPServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8090), 0);
    }

    public void addRoute(String url, Controller controller) throws IOException {
        server.createContext(url, new GenericHTTPHandler(controller));
    }

    public void start() throws IOException {
        server.start();
        System.out.println("Server is running on port 8080");
    }

    public void stop() {
        server.stop(0);
        System.out.println("Server stopped");


    }

    private static class GenericHTTPHandler implements HttpHandler {
        Controller controller;

        public GenericHTTPHandler(Controller controller) {
            this.controller = controller;
        }

        @Override
        public void handle(HttpExchange exchange){
            controller.handle(new HTTPIConnection(exchange));
        }
    }
}
