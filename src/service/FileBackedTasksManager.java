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

    private Map<Long, TaskType> typeMap = new HashMap<>();
    private String path;

    public FileBackedTasksManager(String path) {
        this.path = path;
        loadFromFile(path);
    }

    private void loadFromFile(String path) {

        try {
            String db[] = Files.readString(Path.of(path)).split("\\n\\n");
            String tasks[] = db[0].split("\\n");

            for (String task : tasks) {
                fromString(task);
            }

            if (db.length > 1) {
                historyFromString(db[1]);
            }

        } catch (IOException e) {
            System.out.println("Произошла ошибка при чтении данных из файла");
        } catch (Exception e) {
            System.out.println("Ошибка при загрузке данных из файла");
        }

    }

    private void historyFromString(String value) {

        System.out.println(value);
        String[] lines = value.split(",");
        Long id;

        for (String line : lines ) {
            id = Long.parseLong(line);
            switch (typeMap.get(id)) {
                case TASK:
                    getTask(id);
                    break;
                case EPIC:
                    getEpic(id);
                    break;
                case SUBTASK:
                    getSubtask(id);
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
            fw.write(Managers.getDefaultHistory().toString());
        } catch (IOException e) {
            System.out.println("Ошибка при сохранение данных в файл");
        }
    }

    private Task fromString(String value) {
        String[] split = value.split(",");
        switch (TaskType.valueOf(split[1])) {
            case TASK:
                Task task = new Task(Long.parseLong(split[0]), split[2], split[4], Status.valueOf(split[3]));
                updateTask(task);
                typeMap.put(task.getId(), TaskType.TASK);
                return task;
            case EPIC:
                Epic epic = new Epic(Long.parseLong(split[0]), split[2], split[4], Status.valueOf(split[3]));
                updateEpic(epic);
                typeMap.put(epic.getId(), TaskType.EPIC);
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask(Long.parseLong(split[0]), split[2], split[4], Status.valueOf(split[3]),
                        Long.parseLong(split[5]));
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