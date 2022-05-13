package main.java.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.*;
import main.java.model.Epic;
import main.java.model.Subtask;
import main.java.model.Task;

public class HTTPTaskManager extends FileBackedTasksManager {

    private final HistoryManager historyManager;
    private final KVTaskClient kvTaskClient;
    private final Gson gson = new Gson();

    public HTTPTaskManager(String path) {
        super(path);
        historyManager = Managers.getDefaultHistory();
        kvTaskClient = new KVTaskClient(path);
        loadFromKVServer();
    }

    void loadFromKVServer() {
        try {
            JsonObject state = gson.fromJson(kvTaskClient.load("taskManager"), JsonObject.class);
            if (state == null) return;
            JsonArray tasks = state.getAsJsonArray("tasks");
            tasks.forEach(task -> updateTask(gson.fromJson(task, Task.class)));
            JsonArray epics = state.getAsJsonArray("epics");
            epics.forEach(epic -> updateEpic(gson.fromJson(epic, Epic.class)));
            JsonArray subtasks = state.getAsJsonArray("subtasks");
            subtasks.forEach(subtask -> updateSubtask(gson.fromJson(subtask, Subtask.class)));
            JsonArray history = state.getAsJsonArray("history");
            history.forEach(task -> historyManager.addTask(gson.fromJson(task, Task.class)));
         } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    void save() {
        Map<String, List<Task>> state = new HashMap<>();
        List<Task> epics = new ArrayList<>();
        List<Task> subtasks = new ArrayList<>();
        epics.addAll(getEpics());
        subtasks.addAll(getSubtasks());
        state.put("epics", epics);
        state.put("tasks", getTasks());
        state.put("subtasks", subtasks);
        state.put("history", Managers.getDefaultHistory().getHistory());
        try {
            kvTaskClient.put("taskManager", gson.toJson(state));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}