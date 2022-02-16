import java.util.HashMap;
import java.util.ArrayList;

public class Manager {
    private Integer taskId;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Manager() {
        taskId = 0;
    }

    public Integer getNewId() {
        return taskId++;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> list = new ArrayList<>(tasks.values());
        return list;
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

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> list = new ArrayList<>(subtasks.values());
        return list;
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

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> list = new ArrayList<>(epics.values());
        return list;
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

    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epic.getId()) {
                epicSubtasks.add(subtask);
            }
        }
        return epicSubtasks;
    }

    public Task.Status getEpicStatus(ArrayList<Subtask> epicSubtasks) {
        if (epicSubtasks.size() == 0) {
            return Task.Status.NEW;
        } else {
            Integer countNew = 0;
            Integer countDone = 0;
            for (Subtask subtask : epicSubtasks) {
                if (subtask.getStatus() == Task.Status.NEW) {
                    countNew++;
                } else if (subtask.getStatus() == Task.Status.DONE) {
                    countDone++;
                }
            }
            if (epicSubtasks.size() == countNew) {
                return Task.Status.NEW;
            } else if (epicSubtasks.size() == countDone) {
                return Task.Status.DONE;
            }
        }
        return Task.Status.IN_PROGRESS;
    }

}





