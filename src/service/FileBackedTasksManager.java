package service;

import model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager  {

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
    private static final int TYPE_COLUMN_EPICID = 5;

    private final HistoryManager history = Managers.getDefaultHistory();
    private final Map<Long, TaskType> typeMap = new HashMap<>();
    private final String path;
    
    public FileBackedTasksManager(String path) {
        this.path = path;
        loadFromFile(path);
    }

    private void loadFromFile(String path) {
        try {
            String db[] = Files.readString(Path.of(path)).split(SECTION_DELIMITER);
            String tasks[] = db[SECTION_TASKS].split(LINE_DELIMITER);

            for (String task : tasks) {
                taskFromString(task);
            }

            if (db.length > 1) {
                historyFromString(db[SECTION_HISTORY]);
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка при чтении данных из файла");
        } catch (Exception e) {
            System.out.println("Ошибка при загрузке данных из файла");
        }

    }

    private void historyFromString(String historyCsv) {
        String[] ids = historyCsv.split(VALUE_DELIMITER);

        for (String id : ids) {
             switch (typeMap.get(Long.parseLong(id))) {
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

    private void save() {
        List<Task> tasks = new ArrayList<>();
        tasks.addAll(getTasks());
        tasks.addAll(getEpics());
        tasks.addAll(getSubtasks());

        try(Writer fw = new FileWriter(path)) {
            for (Task task : tasks) {
                fw.write(task + "\n");
            }
            fw.write("\n");
            fw.write(history.getIds());
        } catch (IOException e) {
            System.out.println("Ошибка при сохранение данных в файл");
        }
    }

    private Task taskFromString(String taskCsv) {
        String[] taskValues = taskCsv.split(VALUE_DELIMITER);

        switch (TaskType.valueOf(taskValues[TYPE_COLUMN_TYPE])) {
            case TASK:
                Task task = new Task(Long.parseLong(taskValues[TYPE_COLUMN_INDEX]), taskValues[TYPE_COLUMN_NAME],
                        taskValues[TYPE_COLUMN_DETAILS], Status.valueOf(taskValues[TYPE_COLUMN_STATUS]));
                updateTask(task);
                typeMap.put(task.getId(), TaskType.TASK);
                return task;
            case EPIC:
                Epic epic = new Epic(Long.parseLong(taskValues[TYPE_COLUMN_INDEX]), taskValues[TYPE_COLUMN_NAME],
                        taskValues[TYPE_COLUMN_DETAILS], Status.valueOf(taskValues[TYPE_COLUMN_STATUS]));
                updateEpic(epic);
                typeMap.put(epic.getId(), TaskType.EPIC);
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask(Long.parseLong(taskValues[TYPE_COLUMN_INDEX]), taskValues[TYPE_COLUMN_NAME],
                        taskValues[TYPE_COLUMN_DETAILS], Status.valueOf(taskValues[TYPE_COLUMN_STATUS]),
                        Long.parseLong(taskValues[TYPE_COLUMN_EPICID]));
                updateSubtask(subtask);
                typeMap.put(subtask.getId(), TaskType.SUBTASK);
                return subtask;
            default:
                return null;
        }
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
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
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(Long id) {
        super.deleteEpic(id);
        save();
    }

}