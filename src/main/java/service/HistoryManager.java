package main.java.service;

import main.java.model.Task;
import java.util.List;

public interface HistoryManager {

    void addTask(Task task);
    void removeTask(Long id);
    void clear();
    List<Task> getHistory();
    String getIds();

}