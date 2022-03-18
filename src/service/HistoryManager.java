package service;

import model.Task;
import java.util.List;

public interface HistoryManager {

    void addTask(Task task);
    void removeTask(Long id);
    List<Task> getHistory();

}