import java.util.List;

public interface TaskManager {

    Integer getNewId();

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

    Status getEpicStatus(List<Subtask> epicSubtasks);

}