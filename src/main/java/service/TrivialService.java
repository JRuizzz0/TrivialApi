package service;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;

import javax.swing.text.html.parser.Parser;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TrivialService {
    Random random = new Random();
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

            String respuesta_correcta = obj.get("correct_answer").getAsString();
            String question = obj.get("question").getAsString();
            JsonArray incorrect_answers = obj.get("incorrect_answers").getAsJsonArray();


            dataObj.addProperty("question", question);
            dataObj.addProperty("correct_answer", respuesta_correcta);
            dataObj.add("incorrect_answers", incorrect_answers);

            List<String> listAns = new ArrayList<>();

            for (JsonElement incorrect_answer : incorrect_answers) {
                listAns.add(incorrect_answer.getAsString());
            }
            int numberRandom = random.nextInt(listAns.size());
            listAns.add(numberRandom, respuesta_correcta);

            JsonArray answers = new JsonArray();
            for (String answerJson : listAns) {
                answers.add(answerJson);
            }

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

            String respuesta_correcta = obj.get("correct_answer").getAsString();
            String question = obj.get("question").getAsString();
            JsonArray incorrect_answers = obj.get("incorrect_answers").getAsJsonArray();


            dataObj.addProperty("question", question);
            dataObj.addProperty("correct_answer", respuesta_correcta);
            dataObj.add("incorrect_answers", incorrect_answers);

            List<String> listAns = new ArrayList<>();

            for (JsonElement incorrect_answer : incorrect_answers) {
                listAns.add(incorrect_answer.getAsString());
            }
            int numberRandom = random.nextInt(listAns.size());
            listAns.add(numberRandom, respuesta_correcta);

            JsonArray answers = new JsonArray();
            for (String answerJson : listAns) {
                answers.add(answerJson);
            }

            dataObj.add("answers", answers);

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

            String respuesta_correcta = obj.get("correct_answer").getAsString();
            String question = obj.get("question").getAsString();
            JsonArray incorrect_answers = obj.get("incorrect_answers").getAsJsonArray();


            dataObj.addProperty("question", question);
            dataObj.addProperty("correct_answer", respuesta_correcta);
            dataObj.add("incorrect_answers", incorrect_answers);

            List<String> listAns = new ArrayList<>();

            for (JsonElement incorrect_answer : incorrect_answers) {
                listAns.add(incorrect_answer.getAsString());
            }
            int numberRandom = random.nextInt(listAns.size());
            listAns.add(numberRandom, respuesta_correcta);

            JsonArray answers = new JsonArray();
            for (String answerJson : listAns) {
                answers.add(answerJson);
            }

            dataObj.add("answers", answers);

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