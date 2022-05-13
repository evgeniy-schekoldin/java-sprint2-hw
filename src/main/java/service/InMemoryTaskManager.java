package main.java.service;

import main.java.model.Epic;
import main.java.model.Status;
import main.java.model.Subtask;
import main.java.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private Map<Long, Task> tasks = new HashMap<>();
    private Map<Long, Epic> epics = new HashMap<>();
    private Map<Long, Subtask> subtasks = new HashMap<>();
    private final HistoryManager history = Managers.getDefaultHistory();
    private final Set<Task> sortedTasks = new TreeSet<>(new TaskDateComparator());

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks()  {
        List<Task> tasksList = new ArrayList<>(getTasks());
        for (Task task : tasksList) {
            deleteTask(task.getId());
        }
    }

    @Override
    public Task getTask(Long id) {
        history.addTask((tasks.get(id)));
        return tasks.get(id);
    }

    @Override
    public void addTask(Task task) {
        if (isTaskDateTimeIntersect(task) != null) {
            return;
        }
        task.setId(IdGenerator.generate());
        sortedTasks.add(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task task) {
        if (task == null) {
            return;
        }
        if (isTaskDateTimeIntersect(task) != null) {
            return;
        }
        Task old = tasks.get(task.getId());
        if (old != null) {
            if (sortedTasks.contains(old)) {
                sortedTasks.remove(old);
            }
        }
        sortedTasks.add(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void deleteTask(Long id) {
        if (tasks.containsKey(id)) {
            sortedTasks.remove(tasks.get(id));
            tasks.remove(id);
            history.removeTask(id);
           }
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        List<Task> subtasksList = new ArrayList<>(getSubtasks());
        for (Task subtask : subtasksList) {
            deleteSubtask(subtask.getId());
        }
    }

    @Override
    public Subtask getSubtask(Long id) {
        history.addTask(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (isTaskDateTimeIntersect(subtask) != null) {
            return;
        };
        if (!epics.containsKey(subtask.getEpicId())) {
            throw new InputMismatchException("Указанный в подзадаче Epic не существует");
        }
        subtask.setId(IdGenerator.generate());
        Epic epic = epics.get(subtask.getEpicId());
        if (sortedTasks.contains(epic)) {
            sortedTasks.remove(epic);
        }
        sortedTasks.add(subtask);
        subtasks.put(subtask.getId(), subtask);
        updateEpic(epic);

    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask == null) {
            return;
        }
        if (isTaskDateTimeIntersect(subtask) != null) {
            return;
        };
        if (epics.containsKey(subtask.getEpicId())) {
            Epic epic = epics.get(subtask.getEpicId());
            Subtask old = subtasks.get(subtask.getId());
            if (old != null) {
                if (sortedTasks.contains(old)) {
                    sortedTasks.remove(old);
                }
            }
            if (sortedTasks.contains(epic)) {
                sortedTasks.remove(epic);
            }
            sortedTasks.add(subtask);
            subtasks.put(subtask.getId(), subtask);
            updateEpic(epic);
        } else {
            throw new InputMismatchException("Указанный в подзадаче Epic не существует");
        }
    }

    @Override
    public void deleteSubtask(Long id) {
        if (subtasks.containsKey(id)) {
            Epic epic = epics.get(subtasks.get(id).getEpicId());
            sortedTasks.remove(subtasks.get(id));
            subtasks.remove(id);
            history.removeTask(id);
            updateEpic(epic);
        }
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        List<Task> epicsList = new ArrayList<>(getEpics());
        for (Task epic : epicsList) {
            deleteEpic(epic.getId());
        }
    }

    @Override
    public Epic getEpic(Long id) {
        history.addTask(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(IdGenerator.generate());
        epic.setStatus(Status.NEW);
        epic.setDuration(getEpicDuration(epic));
        epic.setStartTime(getEpicStartTime(epic));
        sortedTasks.add(epic);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic == null) {
            return;
        }
        Epic old = epics.get(epic.getId());
        if (old !=null) {
            if (sortedTasks.contains(old)) {
            sortedTasks.remove(old);
            }
        }
        epic.setStartTime(getEpicStartTime(epic));
        epic.setDuration(getEpicDuration(epic));
        epic.setStatus(getEpicStatus(getEpicSubtasks(epic)));
        sortedTasks.add(epic);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void deleteEpic(Long id) {
        if (epics.containsKey(id)) {
            sortedTasks.remove(epics.get(id));
            history.removeTask(id);
            List<Subtask> subtaskList = getSubtasks();
            for (Subtask subtask : subtaskList) {
                if (subtask.getEpicId() == id) {
                    deleteSubtask(subtask.getId());
                }
            }
            epics.remove(id);
        }
    }

    @Override
    public List<Task> getSortedTasks() {
        return new ArrayList<>(sortedTasks);
    }

    @Override
    public void clearSortedTasks() {
        sortedTasks.clear();
    }

    private List<Subtask> getEpicSubtasks(Epic epic) {
        List<Subtask> epicSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == (epic.getId())) {
                epicSubtasks.add(subtask);
            }
        }
        return epicSubtasks;
    }

    private Task isTaskDateTimeIntersect(Task task) {
        if (task.getEndTime() == null) return null;
        for (Task sortedTask : sortedTasks) {
            if (sortedTask.getStartTime() != null & sortedTask.getEndTime() !=null) {
                if (task.getEndTime().isAfter(sortedTask.getStartTime()) &
                        task.getEndTime().isBefore(sortedTask.getEndTime())) {
                    return sortedTask;
                }
            }

        }
        return null;
    }

    protected List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getEpics());
        allTasks.addAll(getSubtasks());
        allTasks.addAll(getTasks());
        return allTasks;
    }

    private Duration getEpicDuration(Epic epic) {
        List<Subtask> subtasks = getEpicSubtasks(epic);
        Duration duration = Duration.ZERO;
        if (subtasks.size() > 0) {
            for (Subtask subtask : subtasks) {
                duration = duration.plus(subtask.getDuration());
            }
        }
        return duration;
    }

    private LocalDateTime getEpicStartTime(Epic epic) {
        LocalDateTime startTime = null;
        List<Subtask> subtasks = getEpicSubtasks(epic);
        if (subtasks.size() > 0) {
            for (Subtask subtask : subtasks) {
                if (subtask.getStartTime() != null) {
                    if (startTime == null) {
                        startTime = subtask.getStartTime();
                    }
                    if (subtask.getStartTime().isBefore(startTime)) {
                        startTime = subtask.getStartTime();
                    }
                }
            }
        }
        return startTime;
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
            else if (subtask.getStatus() == Status.DONE) {
                countDone++;
            } else {
                return Status.IN_PROGRESS;
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