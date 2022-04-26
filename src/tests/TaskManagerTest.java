package tests;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

class TaskManagerTest<T extends TaskManager> {

    TaskManager manager = Managers.getDefault();
    Epic epic0 = new Epic(0L, "NEW_0L", "NEW", Status.NEW, null,null);
    Epic epic1 = new Epic(0L, "NEW_0L", "UPDATED", Status.NEW, null,null);
    Task task0 = new Task(1L, "NEW_1L", "NEW", Status.NEW, Duration.ofMinutes(60),
            LocalDateTime.now().plusMinutes(61));
    Task task1 = new Task(2L, "NEW_2L", "NEW", Status.NEW, Duration.ofMinutes(60), LocalDateTime.now());
    Task task2 = new Task(3L, "NEW_3L", "NEW", Status.NEW, Duration.ofMinutes(60),
            LocalDateTime.now().minusMinutes(5));
    Subtask subtask0 = new Subtask(4L, "NEW_4L", "NEW", Status.NEW, Duration.ofMinutes(0),null,0L);
    Subtask subtask1 = new Subtask(4L, "NEW_4L", "INCORRECT EPIC ID", Status.NEW, Duration.ofMinutes(0),
            null,100L);
    Subtask subtask3 = new Subtask(5L, "NEW_5L", "CHANGED/UPDATED", Status.NEW, Duration.ofMinutes(0),
            null, 0L);

    @Test
    void ShouldEqualsTasksArrays() {
        manager.updateTask(task0);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task0);
        Assertions.assertArrayEquals(tasks.toArray(), manager.getTasks().toArray(),
                "Списки с объектами-задачами класса Task не равны");
        manager.deleteTask(task0.getId());
    }

    @Test
    void ShouldReturn0TasksCollectionSizeAfterRemove() {
        manager.updateTask(task0);
        manager.deleteAllTasks();
        Assertions.assertEquals(manager.getTasks().size(), 0,
                "Список объектов-задач класса Task должен быть пустой");
    }

    @Test
    void ShouldReturnSavedTask() {
        manager.updateTask(task0);
        Assertions.assertNotNull(manager.getTask(task0.getId()), "Метод вернул значение null");
        Assertions.assertEquals(manager.getTask(task0.getId()), task0,
                "Метод вернул объект, не совпадающий с сохраненным");
        manager.deleteTask(task0.getId());
    }

    @Test
    void ShouldReturnUpdatedTask() {
        manager.updateTask(task0);
        Task updatedTask = new Task(1L, "NEW_1L", "UPDATED", Status.NEW, Duration.ofMinutes(0),
                null);
        manager.updateTask(updatedTask);
        Assertions.assertEquals(manager.getTask(1L), updatedTask,
                "Метод вернул объект, не совпадающий с измененным");
        manager.deleteTask(updatedTask.getId());
    }

    @Test
    void ShouldReturnNullTaskAfterDelete() {
        manager.updateTask(task0);
        manager.deleteTask(task0.getId());
        Assertions.assertEquals(manager.getTask(task0.getId()), null,
                "Метод вернул значение, отличное от null");
    }

    @Test
    void ShouldEqualsSubtasksArrays() {
        manager.updateEpic(epic0);
        manager.updateSubtask(subtask0);
        List<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask0);
        Assertions.assertArrayEquals(manager.getSubtasks().toArray(), subtasks.toArray(),
                "Списки с объектами-задачами класса Subtask не равны");
        manager.deleteEpic(epic0.getId());
        manager.deleteSubtask(subtask0.getId());
    }

    @Test
    void ShouldReturn0SubtasksCollectionSizeAfterRemove() {
        manager.updateEpic(epic0);
        manager.updateSubtask(subtask0);
        manager.deleteAllSubtasks();
        Assertions.assertEquals(manager.getSubtasks().size(), 0,
                "Список объектов-задач класса Subtask должен быть пустой");
        manager.deleteEpic(epic0.getId());
        manager.deleteSubtask(subtask0.getId());
    }

    @Test
    void ShouldReturnSavedSubtaskOrExceptionIfSubtaskIsNotCorrect() {
        manager.updateEpic(epic0);
        manager.updateSubtask(subtask0);
        Assertions.assertEquals(manager.getSubtask(subtask0.getId()), subtask0,
                "Метод вернул объект, не совпадающий с сохраненным");
        InputMismatchException ex = Assertions.assertThrows(
                InputMismatchException.class,
                () -> {
                    manager.updateSubtask(subtask1);
                });
        Assertions.assertEquals("Указанный в подзадаче Epic не существует", ex.getMessage());
        manager.deleteEpic(epic0.getId());
        manager.deleteSubtask(subtask0.getId());
    }

    @Test
    void ShouldReturnUpdatedSubtask() {
        manager.updateEpic(epic0);
        manager.updateSubtask(subtask0);
        manager.updateSubtask(subtask3);
        Assertions.assertEquals(manager.getSubtask(subtask3.getId()), subtask3,
                "Метод вернул объект, не совпадающий с измененным");
        manager.deleteEpic(epic0.getId());
        manager.deleteSubtask(subtask0.getId());
    }

    @Test
    void ShouldReturnNullSubtaskAfterDelete() {
        manager.updateEpic(epic0);
        manager.updateSubtask(subtask0);
        manager.deleteSubtask(subtask0.getId());
        Assertions.assertEquals(manager.getSubtask(subtask0.getId()), null,
                "Метод вернул значение, отличное от null");
        manager.deleteEpic(epic0.getId());
        manager.deleteSubtask(subtask0.getId());
    }

    @Test
    void ShouldEqualsEpicsArrays() {
        manager.updateEpic(epic0);
        List<Task> epics = new ArrayList<>();
        epics.add(epic0);
        Assertions.assertArrayEquals(manager.getEpics().toArray(), epics.toArray(),
                "Списки с объектами-задачами класса Epic не равны");
        manager.deleteEpic(epic0.getId());
    }

    @Test
    void ShouldReturn0EpicsCollectionSizeAfterRemove() {
        manager.updateEpic(epic0);
        manager.deleteAllEpics();
        Assertions.assertEquals(manager.getEpics(), new ArrayList<>(),
                "Список объектов-задач класса Subtask должен быть пустой");
        manager.deleteEpic(epic0.getId());
    }

    @Test
    void ShouldReturnSavedEpic() {
        manager.updateEpic(epic0);
        Assertions.assertNotNull(manager.getEpic(epic0.getId()), "Метод вернул значение null");
        Assertions.assertEquals(manager.getEpic(epic0.getId()), epic0,
                "Метод вернул объект, не совпадающий с сохраненным");
        manager.deleteEpic(epic0.getId());
    }

    @Test
    void updateEpic() {
        manager.updateEpic(epic0);
        manager.updateEpic(epic1);
        Assertions.assertEquals(manager.getEpic(0L), epic1, "Метод вернул объект, не совпадающий с измененным");
        manager.deleteEpic(epic1.getId());
    }

    @Test
    void ShouldReturnNullEpicAndNullSubtaskAfterDelete() {
        manager.updateEpic(epic0);
        manager.updateSubtask(subtask0);
        manager.deleteEpic(epic0.getId());
        Assertions.assertEquals(manager.getEpic(epic0.getId()), null,
                "Метод вернул значение, отличное от null");
        Assertions.assertEquals(manager.getSubtask(subtask0.getId()), null,
                "Метод вернул значение, отличное от null");
    }

    @Test
    void ShouldReturnSortedTasks() {
        manager.clearSortedTasks();
        manager.updateTask(task0);
        manager.updateTask(task1);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task0);
        Assertions.assertEquals(manager.getSortedTasks(), tasks, "Элементы не отсортированы по дате");
        manager.deleteTask(task0.getId());
        manager.deleteTask(task1.getId());
    }

    @Test
    void ShouldNotAddTaskWithIntersectedDateTime() {
        manager.updateTask(task1);
        manager.updateTask(task2);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        Assertions.assertEquals(manager.getTasks().size(), 1,
                "Проверка на пересечение задач по времени не работает");
        manager.deleteTask(task1.getId());
        manager.deleteTask(task2.getId());
    }

}