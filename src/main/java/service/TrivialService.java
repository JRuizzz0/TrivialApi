package service;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class TrivialService {
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
        if (response.statusCode() != 200) {
            throw new IOException("HTTP error: " + response.statusCode());
        }
        return gson.fromJson(response.body(), JsonObject.class);
    }

    public void JsonPrinteasy(HttpExchange exchange) throws IOException, InterruptedException {
        JsonObject raiz = fetchApiData(easy);
        JsonArray results = raiz.getAsJsonArray("results");


        JsonArray data = new JsonArray();
        for (JsonElement element : results) {
            JsonObject obj = element.getAsJsonObject();
            JsonObject dataObj = new JsonObject();
            dataObj.addProperty("question", obj.get("question").getAsString());
            dataObj.addProperty("correct_answer", obj.get("correct_answer").getAsString());
            JsonArray answers = new JsonArray();
            dataObj.add("answers", answers);
            data.add(dataObj);


        }
        sendResponse(exchange, 200, gson.toJson(data));
    }

    public void JsonPrintmedium(HttpExchange exchange) throws IOException, InterruptedException {
        JsonObject raiz = fetchApiData(medium);
        JsonArray results = raiz.getAsJsonArray("results");


        JsonArray data = new JsonArray();
        for (JsonElement element : results) {
            JsonObject obj = element.getAsJsonObject();
            JsonObject dataObj = new JsonObject();
            dataObj.addProperty("question", obj.get("question").getAsString());
            dataObj.addProperty("correct_answer", obj.get("correct_answer").getAsString());
            dataObj.addProperty("incorrect_answers", String.valueOf(obj.get("incorrect_answers").getAsJsonArray()));
            data.add(dataObj);
        }
        sendResponse(exchange, 200, gson.toJson(data));
    }

    public void JsonPrinthard(HttpExchange exchange) throws IOException, InterruptedException {
        JsonObject raiz = fetchApiData(hard);
        JsonArray results = raiz.getAsJsonArray("results");


        JsonArray data = new JsonArray();
        for (JsonElement element : results) {
            JsonObject obj = element.getAsJsonObject();
            JsonObject dataObj = new JsonObject();
            dataObj.addProperty("question", obj.get("question").getAsString());
            dataObj.addProperty("correct_answer", obj.get("correct_answer").getAsString());
            dataObj.addProperty("incorrect_answers", String.valueOf(obj.get("incorrect_answers").getAsJsonArray()));
            data.add(dataObj);
        }
        sendResponse(exchange, 200, gson.toJson(data));
    }

    public void sendResponse(HttpExchange exchange, int status, String body) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        byte[] bytes = body.getBytes();
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}