package tests;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

abstract class TaskManagerTest<T extends TaskManager> {

    TaskManager manager;

    public TaskManagerTest(T manager) {
        this.manager = manager;
    }

    Epic epic0 = new Epic("NEW", "NEW", null, null);
    Epic epic1 = new Epic("NEW", "NEW", null, null);
    Task task0 = new Task("NEW", "NEW", Status.NEW, Duration.ofMinutes(0), LocalDateTime.now().plusMinutes(90));
    Task task1 = new Task("NEW", "NEW", Status.NEW, Duration.ofMinutes(0), LocalDateTime.now());

    @BeforeEach
    void beforeEach() {
    }

    @AfterEach
    void afterEach() {
        manager.deleteAllTasks();
        manager.deleteAllEpics();
    }

    @Test
    void ShouldEqualsTasksArrays() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task0);
        manager.addTask(task0);
        Assertions.assertArrayEquals(tasks.toArray(), manager.getTasks().toArray(),
                "Списки с объектами-задачами не равны");
    }

    @Test
    void ShouldReturn0TasksCollectionSizeAfterRemove() {
        manager.deleteAllTasks();
        Assertions.assertEquals(manager.getTasks().size(), 0,
                "Список объектов-задач класса Task должен быть пустой");
    }

    @Test
    void ShouldReturnSavedTask() {
        manager.addTask(task0);
        Assertions.assertNotNull(manager.getTask(task0.getId()), "Задача не добавилась");
    }

    @Test
    void ShouldReturnUpdatedTask() {
        manager.addTask(task1);
        Task task2 = new Task("UPDATED", "UPDATED", Status.DONE, Duration.ofMinutes(0), LocalDateTime.now());
        task2.setId(task1.getId());
        Assertions.assertEquals(task1.getStatus(), Status.NEW, "Обновляемая задача уже изменена");
        Assertions.assertEquals(task1.getName(), "NEW", "Обновляемая задача уже изменена");
        Assertions.assertEquals(task1.getDetails(), "NEW", "Обновляемая задача уже изменена");
        manager.updateTask(task2);
        Assertions.assertNotNull(manager.getTask(task1.getId()), "Метод вернул значение null");
        Assertions.assertEquals(task2.getId(), task1.getId(), "Id задачи не совпадает");
        task1 = manager.getTask(task1.getId());
        Assertions.assertEquals(task1.getStatus(), Status.DONE, "Статус задачи не обновился");
        Assertions.assertEquals(task1.getName(), "UPDATED", "Имя задачи не обновилось");
        Assertions.assertEquals(task1.getDetails(), "UPDATED", "Описание задачи не обновилось");
        Assertions.assertDoesNotThrow(
                () -> {
                    manager.updateTask(null);
                });
    }

    @Test
    void ShouldReturnNullTaskAfterDelete() {
        manager.addTask(task0);
        manager.deleteTask(task0.getId());
        Assertions.assertEquals(manager.getTask(task0.getId()), null,
                "Метод вернул значение, отличное от null");
    }

    @Test
    void ShouldEqualsSubtasksArrays() {
        manager.addEpic(epic0);
        Subtask subtask0 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(10), null, epic0.getId());
        Subtask subtask1 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(20), null, epic0.getId());
        manager.addSubtask(subtask0);
        manager.addSubtask(subtask1);
        List<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask0);
        subtasks.add(subtask1);
        Assertions.assertArrayEquals(manager.getSubtasks().toArray(), subtasks.toArray(),
                "Списки с объектами-задачами класса Subtask не равны");
    }

    @Test
    void ShouldReturn0SubtasksCollectionSizeAfterRemove() {
        manager.addEpic(epic0);
        Subtask subtask0 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(10), null, epic0.getId());
        Subtask subtask1 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(20), null, epic0.getId());
        manager.addSubtask(subtask0);
        manager.addSubtask(subtask1);
        manager.deleteAllSubtasks();
        Assertions.assertEquals(manager.getSubtasks().size(), 0,
                "Список объектов-задач класса Subtask должен быть пустой");
    }

    @Test
    void ShouldReturnSavedSubtaskOrExceptionIfSubtaskIsNotCorrect() {
        manager.addEpic(epic0);
        Subtask subtask0 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(10), null, epic0.getId());
        Subtask subtask1 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(20), null, epic0.getId());
        Subtask subtask2 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(20), null, 100);
        manager.addSubtask(subtask0);
        manager.addSubtask(subtask1);
        Assertions.assertEquals(manager.getSubtask(subtask0.getId()), subtask0,
                "Метод вернул объект, не совпадающий с сохраненным");
        InputMismatchException ex = Assertions.assertThrows(
                InputMismatchException.class,
                () -> {
                    manager.addSubtask(subtask2);
                });
        Assertions.assertEquals("Указанный в подзадаче Epic не существует", ex.getMessage());
        manager.deleteEpic(epic0.getId());
        manager.deleteSubtask(subtask0.getId());
    }

    @Test
    void ShouldReturnUpdatedSubtask() {
        manager.addEpic(epic0);
        Subtask subtask0 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(10), null, epic0.getId());
        Subtask subtask1 = new Subtask("UPDATED", "UPDATED", Status.DONE, Duration.ofMinutes(20), null, epic0.getId());
        manager.addSubtask(subtask0);
        subtask1.setId(subtask0.getId());
        manager.updateSubtask(subtask1);
        Assertions.assertNotNull(manager.getSubtask(subtask0.getId()), "Метод вернул значение null");
        Assertions.assertEquals(subtask1.getId(), subtask0.getId(), "Id задачи не совпадает");
        subtask1 = manager.getSubtask(subtask1.getId());
        Assertions.assertEquals(subtask1.getStatus(), Status.DONE, "Статус задачи не обновился");
        Assertions.assertEquals(subtask1.getName(), "UPDATED", "Имя задачи не обновилось");
        Assertions.assertEquals(subtask1.getDetails(), "UPDATED", "Описание задачи не обновилось");
        Assertions.assertDoesNotThrow(
                () -> {
                    manager.updateSubtask(null);
                });
    }

    @Test
    void ShouldReturnNullSubtaskAfterDelete() {
        manager.addEpic(epic0);
        Subtask subtask0 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(10), null, epic0.getId());
        manager.addSubtask(subtask0);
        manager.deleteSubtask(subtask0.getId());
        Assertions.assertEquals(manager.getSubtask(subtask0.getId()), null,
                "Метод вернул значение, отличное от null");
    }

    @Test
    void ShouldEqualsEpicsArrays() {
        manager.addEpic(epic0);
        List<Task> epics = new ArrayList<>();
        epics.add(epic0);
        Assertions.assertArrayEquals(manager.getEpics().toArray(), epics.toArray(),
                "Списки с объектами-задачами класса Epic не равны");
    }

    @Test
    void ShouldReturn0EpicsCollectionSizeAfterRemove() {
        manager.addEpic(epic0);
        Subtask subtask0 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(10), null, epic0.getId());
        manager.addSubtask(subtask0);
        manager.deleteAllEpics();
        Assertions.assertEquals(manager.getEpics().size(), 0,
                "Список объектов-задач класса Epic должен быть пустой");
        Assertions.assertEquals(manager.getSubtasks().size(), 0,
                "Список объектов-задач класса Subtask должен быть пустой");
    }

    @Test
    void ShouldReturnSavedEpic() {
        manager.addEpic(epic0);
        Assertions.assertNotNull(manager.getEpic(epic0.getId()), "Метод вернул значение null");
    }

    @Test
    void updateEpic() {
        manager.addEpic(epic0);
        epic1 = new Epic("UPDATED", "UPDATED", Duration.ofMinutes(0), null);
        epic1.setId(epic0.getId());
        Assertions.assertEquals(epic0.getStatus(), Status.NEW, "Обновляемая задача уже изменена");
        Assertions.assertEquals(epic0.getName(), "NEW", "Обновляемая задача уже изменена");
        Assertions.assertEquals(epic0.getDetails(), "NEW", "Обновляемая задача уже изменена");
        manager.updateEpic(epic1);
        Assertions.assertNotNull(manager.getEpic(epic1.getId()), "Метод вернул значение null");
        Assertions.assertEquals(epic1.getId(), epic1.getId(), "Id задачи не совпадает");
        epic1 = manager.getEpic(epic1.getId());
        Assertions.assertEquals(epic1.getName(), "UPDATED", "Имя задачи не обновилось");
        Assertions.assertEquals(epic1.getDetails(), "UPDATED", "Описание задачи не обновилось");
        Assertions.assertDoesNotThrow(
                () -> {
                    manager.updateEpic(null);
                });
    }

    @Test
    void ShouldReturnNullEpicAndNullSubtaskAfterDelete() {
        manager.addEpic(epic0);
        Subtask subtask0 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(10), null, epic0.getId());
        manager.addSubtask(subtask0);
        manager.deleteEpic(epic0.getId());
        Assertions.assertEquals(manager.getEpic(epic0.getId()), null,
                "Метод вернул значение, отличное от null");
        Assertions.assertEquals(manager.getSubtask(subtask0.getId()), null,
                "Метод вернул значение, отличное от null");
    }

    @Test
    void ShouldReturnSortedTasks() {
        manager.clearSortedTasks();
        manager.addTask(task0);
        manager.addTask(task1);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task0);
        Assertions.assertEquals(manager.getSortedTasks(), tasks, "Элементы не отсортированы по дате");
    }

}