package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private Map<Long, Task> tasks = new HashMap<>();
    private Map<Long, Epic> epics = new HashMap<>();
    private Map<Long, Subtask> subtasks = new HashMap<>();

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public Task getTask(Long id) {
        Managers.getDefaultHistory().addTask(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void deleteTask(Long id) {
        tasks.remove(id);
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    @Override
    public Subtask getSubtask(Long id) {
        Managers.getDefaultHistory().addTask(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        subtasks.put(subtask.getId(), subtask);
        epic.setTaskStatus(getEpicStatus(getEpicSubtasks(epic)));
    }

    @Override
    public void deleteSubtask(Long id) {
        subtasks.remove(id);
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
    }

    @Override
    public Epic getEpic(Long id) {
        Managers.getDefaultHistory().addTask(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void deleteEpic(Long id) {
        epics.remove(id);
    }

    @Override
    public List<Subtask> getEpicSubtasks(Epic epic) {
        List<Subtask> epicSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == (epic.getId())) {
                epicSubtasks.add(subtask);
            }
        }
        return epicSubtasks;
    }

    private Status getEpicStatus(List<Subtask> epicSubtasks) {
        if (epicSubtasks.size() == 0) {
            return Status.NEW;
        }
        int countNew = 0;
        int countDone = 0;
        for (Subtask subtask : epicSubtasks) {
            if (subtask.getStatus() == Status.NEW) {
                countNew++;
            }
            if (subtask.getStatus() == Status.DONE) {
                countDone++;
            }
        }

        if (epicSubtasks.size() == countNew) {
            return Status.NEW;
        }
        if (epicSubtasks.size() == countDone) {
            return Status.DONE;
        }
        return Status.IN_PROGRESS;
    }

}