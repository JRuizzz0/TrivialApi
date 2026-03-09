package controller;

import com.sun.net.httpserver.HttpExchange;
import service.TrivialService;


import java.io.IOException;
import java.io.OutputStream;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ControllerTrivial {


    //Instancia HttpClient
    private final HttpClient client = HttpClient.newHttpClient();

    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        try {
            String apiUrl = "";
            TrivialService service = new TrivialService();
            if (path.equals("/trivial/easy")) {

                service.JsonPrinteasy(exchange);
                return;

            }
            if (path.equals("/trivial/medium")) {
                service.JsonPrintmedium(exchange);
                return;
            }
            if (path.equals("/trivial/hard")) {
                service.JsonPrinthard(exchange);
                return;
            }

            else {
                service.sendResponse(exchange, 404, "Endpoint trivial no válido");
                return;
            }


            /**
             * request para recorger la url y manejarla con el router y el control
             */

        } catch (Exception e) {
            sendResponse(exchange, 500, "Error llamando a la API trivial"); //error 500
        }
    }


    public void sendResponse(HttpExchange exchange, int status, String body) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        byte[] bytes = body.getBytes();
        exchange.sendResponseHeaders(status, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

}