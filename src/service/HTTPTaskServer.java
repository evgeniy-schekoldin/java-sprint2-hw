package service;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HTTPTaskServer {

    private static final int PORT = 8080;

    public HTTPTaskServer() {
        try {
            HttpServer httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);
            System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
            httpServer.createContext("/tasks", new TasksHandler());
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}