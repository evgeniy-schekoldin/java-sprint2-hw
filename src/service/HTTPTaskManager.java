package service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.*;
import model.Epic;
import model.Subtask;
import model.Task;

public class HTTPTaskManager extends FileBackedTasksManager {

    KVTaskClient kvTaskClient;
    Gson gson;
    HistoryManager historyManager;

    public HTTPTaskManager(String path) {
        super(path);
    }

    @Override
    void loadFromFile(String path) {
        gson = new Gson();
        historyManager = Managers.getDefaultHistory();
        try {
            kvTaskClient = new KVTaskClient(path);
            JsonObject state = gson.fromJson(kvTaskClient.load("taskManager"), JsonObject.class);
            if (state == null) return;
            JsonArray tasks = state.getAsJsonArray("tasks");
            tasks.forEach((t) -> updateTask(gson.fromJson(t, Task.class)));
            JsonArray epics = state.getAsJsonArray("epics");
            epics.forEach((t) -> updateEpic(gson.fromJson(t, Epic.class)));
            JsonArray subtasks = state.getAsJsonArray("subtasks");
            subtasks.forEach((t) -> updateSubtask(gson.fromJson(t, Subtask.class)));
            JsonArray history = state.getAsJsonArray("history");
            history.forEach((t) -> historyManager.addTask(gson.fromJson(t, Task.class)));
         } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    void save() {
        Map<String, List> state = new HashMap<>();
        state.put("epics", getEpics());
        state.put("tasks", getTasks());
        state.put("subtasks", getSubtasks());
        state.put("history", Managers.getDefaultHistory().getHistory());
        try {
            kvTaskClient.put("taskManager", gson.toJson(state));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}