package main.java.service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.model.Epic;
import main.java.model.Subtask;
import main.java.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TasksHandler implements HttpHandler {

    private TaskManager manager = Managers.getDefault();
    private HistoryManager history = Managers.getDefaultHistory();
    private static Gson gson = new Gson();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String response = "";
        String method = httpExchange.getRequestMethod();
        String fullUri = httpExchange.getRequestURI().toString();
        String partUri[] = fullUri.split("/");
        long id = -1;

        if (partUri.length > 3) {
            id = Long.parseLong(partUri[3].split("id=")[1]);
        }

        switch (method) {
            case "GET":
                if (partUri[2].equals("history")) {
                    response = gson.toJson(history.getHistory());
                }
                if (partUri[2].equals("task")) {
                    response = (id == -1) ? gson.toJson(manager.getTasks()) : gson.toJson(manager.getTask(id));
                }
                if (partUri[2].equals("epic")) {
                    response = (id == -1) ? gson.toJson(manager.getEpics()) : gson.toJson(manager.getEpic(id));
                }
                if (partUri[2].equals("subtask")) {
                    response = (id == -1) ? gson.toJson(manager.getSubtasks()) : gson.toJson(manager.getSubtask(id));
                }
                httpExchange.sendResponseHeaders(response.equals("null") ? 404 : 200, 0);
                break;
            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes());
                if (partUri[2].equals("task")) {
                    Task task = gson.fromJson(body, Task.class);
                     if (id == -1) {
                        manager.addTask(task);
                    } else {
                        task.setId(id);
                        manager.updateTask(task);
                    }
                }
                if (partUri[2].equals("epic")) {
                    Epic epic = gson.fromJson(body, Epic.class);
                    if (id == -1) {
                        manager.addEpic(epic);
                    } else {
                        epic.setId(id);
                        manager.updateEpic(epic);
                    }
                }
                if (partUri[2].equals("subtask")) {
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    if (id == -1) {
                        manager.addSubtask(subtask);
                    } else {
                        subtask.setId(id);
                        manager.updateSubtask(subtask);
                    }
                }
                httpExchange.sendResponseHeaders(201, 0);
                break;
            case "DELETE":
                if (partUri[2].equals("task")) {
                    if ((id == -1)) manager.deleteAllTasks();
                    else manager.deleteTask(id);
                }
                if (partUri[2].equals("epic")) {
                    if ((id == -1)) manager.deleteAllEpics();
                    else manager.deleteEpic(id);
                }
                if (partUri[2].equals("subtask")) {
                    if ((id == -1)) manager.deleteAllSubtasks();
                    else manager.deleteSubtask(id);
                }
                httpExchange.sendResponseHeaders(204, 0);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + method);
        }
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}