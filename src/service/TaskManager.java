package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getTasks();

    void deleteAllTasks();

    Task getTask(Long id);

    void updateTask(Task task);

    void deleteTask(Long id);

    List<Subtask> getSubtasks();

    void deleteAllSubtasks();

    Subtask getSubtask(Long id);

    void updateSubtask(Subtask subtask);

    void deleteSubtask(Long id);

    List<Epic> getEpics();

    void deleteAllEpics();

    Epic getEpic(Long id);

    void updateEpic(Epic epic);

    void deleteEpic(Long id);

    List<Subtask> getEpicSubtasks(Epic epic);

}