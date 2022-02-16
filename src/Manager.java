import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Manager {
    private Integer taskId;
    Map<Long, Task> tasks = new HashMap<>();
    Map<Long, Epic> epics = new HashMap<>();
    Map<Long, Subtask> subtasks = new HashMap<>();

    public Manager() {
        taskId = 0;
    }

    public Integer getNewId() {
        return taskId++;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void removeTasks() {
        tasks.clear();
    }

    public Task getTask(Integer id) {
        return tasks.get(id);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void deleteTask(Integer id) {
        tasks.remove(id);
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void removeSubtasks() {
        subtasks.clear();
    }

    public Subtask getSubtask(Integer id) {
        return subtasks.get(id);
    }

    public void updateSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        subtasks.put(subtask.getId(), subtask);
        epic.setTaskStatus(getEpicStatus(getEpicSubtasks(epic)));
    }

    public void deleteSubtask(Integer id) {
        subtasks.remove(id);
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());

    }

    public void removeEpics() {
        epics.clear();
    }

    public Epic getEpic(Integer id) {
        return epics.get(id);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void deleteEpic(Integer id) {
        epics.remove(id);
    }

    public List<Subtask> getEpicSubtasks(Epic epic) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == (epic.getId())) {
                epicSubtasks.add(subtask);
            }
        }
        return epicSubtasks;
    }

    public Status getEpicStatus(List<Subtask> epicSubtasks) {
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