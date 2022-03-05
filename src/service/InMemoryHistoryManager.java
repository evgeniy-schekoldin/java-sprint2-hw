package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int HISTORY_SIZE = 10;
    private List<Task> history = new ArrayList<>();

    public void addTask(Task task) {
        history.add(task);
        if (history.size() > HISTORY_SIZE) { history.remove(0); }
    }

    public List<Task> getHistory() {
        return history;
    }

}