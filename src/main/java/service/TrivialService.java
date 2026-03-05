package service;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TrivialService {

    /**
     * isntancia Gson
     * isntancia cliente
     * Url principal de la Api
     */
    private final Gson gson = new Gson();
    private final HttpClient client = HttpClient.newHttpClient();
    private final String easy = "https://opentdb.com/api.php?amount=10&difficulty=easy";
    private final String medium = "https://opentdb.com/api.php?amount=10&difficulty=medium";
    private final String hard = "https://opentdb.com/api.php?amount=10&difficulty=hard";


    private JsonObject fetchApiData(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) { // error 200
            throw new IOException("HTTP error de la API Dog CEO: " + response.statusCode());
        }

        return gson.fromJson(response.body(), JsonObject.class);
    }



    public void JsonPrinteasy(HttpExchange exchange) throws IOException, InterruptedException {
        JsonObject jsonRaiz = fetchApiData(easy);
        JsonObject message = jsonRaiz.getAsJsonObject("message");

        JsonArray resultado = new JsonArray();
        for (String pregunta : message.keySet()) {
            JsonArray opciones = message.getAsJsonArray(pregunta);

            JsonObject obj = new JsonObject();
            obj.addProperty("preguntas", pregunta);
            obj.add("opciones", opciones);
            resultado.add(obj);
        }

        sendResponse(exchange, 200, gson.toJson(resultado));
    }


    public void JsonPrintmedium(HttpExchange exchange) throws IOException, InterruptedException {
        JsonObject jsonRaiz = fetchApiData(medium);
        JsonObject message = jsonRaiz.getAsJsonObject("message");

        JsonArray resultado = new JsonArray();
        for (String pregunta : message.keySet()) {
            JsonArray opciones = message.getAsJsonArray(pregunta);

            JsonObject obj = new JsonObject();
            obj.addProperty("preguntas", pregunta);
            obj.add("opciones", opciones);
            resultado.add(obj);
        }

        sendResponse(exchange, 200, gson.toJson(resultado));
    }

    public void JsonPrinthard(HttpExchange exchange) throws IOException, InterruptedException {
        JsonObject jsonRaiz = fetchApiData(hard);
        JsonObject message = jsonRaiz.getAsJsonObject("message");

        JsonArray resultado = new JsonArray();
        for (String pregunta : message.keySet()) {
            JsonArray opciones = message.getAsJsonArray(pregunta);

            JsonObject obj = new JsonObject();
            obj.addProperty("preguntas", pregunta);
            obj.add("opciones", opciones);
            resultado.add(obj);
        }

        sendResponse(exchange, 200, gson.toJson(resultado));
    }

    /**
     * Envía una respuesta HTTP al cliente especificando que el contenido es de tipo JSON.
     *
     * @param exchange encapsula la petición del cliente y la respuesta del servidor.
     * @param status   El código de estado HTTP que se enviará al cliente.
     * @param body     El cuerpo de la respuesta en formato de texto que será enviado.
     * @throws IOException Si ocurre un error de entrada/salida al configurar las cabeceras o al escribir en el flujo de respuesta.
     */

    public void sendResponse(HttpExchange exchange, int status, String body) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        byte[] bytes = body.getBytes();
        exchange.sendResponseHeaders(status, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

}