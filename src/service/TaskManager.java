package service;

import model.Epic;
import model.Subtask;
import model.Task;
import java.util.List;

public interface TaskManager {

    List<Task> getTasks();

    void deleteAllTasks();

    Task getTask(Long id);

    void addTask(Task task);

    void updateTask(Task task);

    void deleteTask(Long id);

    List<Subtask> getSubtasks();

    void deleteAllSubtasks();

    Subtask getSubtask(Long id);

    void addSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtask(Long id);

    List<Epic> getEpics();

    void deleteAllEpics();

    Epic getEpic(Long id);

    void addEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpic(Long id);

    List<Task> getSortedTasks();

    void clearSortedTasks();

}