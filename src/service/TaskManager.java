package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getTasks();

    void removeTasks();

    Task getTask(Long id);

    void updateTask(Task task);

    void deleteTask(Long id);

    List<Subtask> getSubtasks();

    void removeSubtasks();

    Subtask getSubtask(Long id);

    void updateSubtask(Subtask subtask);

    void deleteSubtask(Long id);

    List<Epic> getEpics();

    void removeEpics();

    Epic getEpic(Long id);

    void updateEpic(Epic epic);

    void deleteEpic(Long id);

    List<Subtask> getEpicSubtasks(Epic epic);

}