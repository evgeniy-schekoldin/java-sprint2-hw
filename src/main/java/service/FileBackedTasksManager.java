package main.java.service;

import main.java.model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static final String LINE_DELIMITER = "\n";
    private static final String SECTION_DELIMITER = "\n\n";
    private static final String VALUE_DELIMITER = ",";
    private static final int TYPE_COLUMN_INDEX = 0;
    private static final int SECTION_TASKS = 0;
    private static final int SECTION_HISTORY = 1;
    private static final int TYPE_COLUMN_TYPE = 1;
    private static final int TYPE_COLUMN_NAME = 2;
    private static final int TYPE_COLUMN_STATUS = 3;
    private static final int TYPE_COLUMN_DETAILS = 4;
    private static final int TYPE_COLUMN_DURATION = 5;
    private static final int TYPE_COLUMN_STARTTIME = 6;
    private static final int TYPE_COLUMN_EPICID = 7;

    private final HistoryManager history = Managers.getDefaultHistory();
    private final Map<Long, TaskType> taskTypes = new HashMap<>();
    private final String path;

    public FileBackedTasksManager(String path) {
        this.path = path;
        loadFromFile(path);
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public Task getTask(Long id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(Long id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Subtask getSubtask(Long id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtask(Long id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Epic getEpic(Long id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(Long id) {
        super.deleteEpic(id);
        save();
    }

    void loadFromFile(String path) {
        if (path.startsWith("http")) return;
        try {
            String db[] = Files.readString(Path.of(path)).split(SECTION_DELIMITER);
            String tasks[] = db[SECTION_TASKS].split(LINE_DELIMITER);
            for (String task : tasks) {
                taskFromString(task);
            }

            if (taskTypes.size() > 0) {
                IdGenerator.setNextId(Collections.max(taskTypes.keySet()) + 1);
            }

            if (db.length > 1) {
                historyFromString(db[SECTION_HISTORY]);
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка при чтении данных из файла");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка при загрузке данных истории из файла: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка при загрузке данных о задачах из файла: " + e.getMessage());
        }
    }

    private void historyFromString(String historyCsv) {
        String[] ids = historyCsv.split(VALUE_DELIMITER);

        for (String id : ids) {
            switch (taskTypes.get(Long.parseLong(id))) {
                case TASK:
                    getTask(Long.parseLong(id));
                    break;
                case EPIC:
                    getEpic(Long.parseLong(id));
                    break;
                case SUBTASK:
                    getSubtask(Long.parseLong(id));
                    break;
            }
        }
    }

    void save() {
        List<Task> tasks = new ArrayList<>(getAllTasks());

        try (Writer fw = new FileWriter(path)) {
            for (Task task : tasks) {
                fw.write(task + "\n");
            }
            fw.write("\n");
            fw.write(history.getIds());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл");
        }
    }

    private LocalDateTime parseDateTime(String line) {
        LocalDateTime startTime = null;
        if (!line.equals("null")) {
            startTime = LocalDateTime.parse(line);
        }
        return startTime;
    }

    private Task taskFromString(String taskCsv) {
        String[] taskValues = taskCsv.split(VALUE_DELIMITER);
        if (taskCsv.length() == 0) return null;
        switch (TaskType.valueOf(taskValues[TYPE_COLUMN_TYPE])) {
            case TASK:
                Task task = new Task(
                        taskValues[TYPE_COLUMN_NAME],
                        taskValues[TYPE_COLUMN_DETAILS],
                        Status.valueOf(taskValues[TYPE_COLUMN_STATUS]),
                        Duration.parse(taskValues[TYPE_COLUMN_DURATION]),
                        parseDateTime(taskValues[TYPE_COLUMN_STARTTIME])
                );
                task.setId(Long.parseLong(taskValues[TYPE_COLUMN_INDEX]));
                updateTask(task);
                taskTypes.put(task.getId(), TaskType.TASK);
                return task;
            case SUBTASK:
                Subtask subtask = new Subtask(
                        taskValues[TYPE_COLUMN_NAME],
                        taskValues[TYPE_COLUMN_DETAILS],
                        Status.valueOf(taskValues[TYPE_COLUMN_STATUS]),
                        Duration.parse(taskValues[TYPE_COLUMN_DURATION]),
                        parseDateTime(taskValues[TYPE_COLUMN_STARTTIME]),
                        Long.parseLong(taskValues[TYPE_COLUMN_EPICID])
                );
                subtask.setId(Long.parseLong(taskValues[TYPE_COLUMN_INDEX]));
                updateSubtask(subtask);
                taskTypes.put(subtask.getId(), TaskType.SUBTASK);
                return subtask;
            case EPIC:
                Epic epic = new Epic(
                        taskValues[TYPE_COLUMN_NAME],
                        taskValues[TYPE_COLUMN_DETAILS],
                        null,
                        null
                );
                epic.setId(Long.parseLong(taskValues[TYPE_COLUMN_INDEX]));
                updateEpic(epic);
                taskTypes.put(epic.getId(), TaskType.EPIC);
                return epic;
            default:
                return null;
        }
    }

}