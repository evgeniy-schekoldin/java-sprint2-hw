package main.tests.service;

import main.java.model.Epic;
import main.java.model.Status;
import main.java.model.Subtask;
import main.java.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.java.service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    final TaskManager manager;

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
        manager.deleteAllSubtasks();
    }

    @Test
    void testAddTaskTaskManager() {
        manager.addTask(task0);
        int expectedSize = 1;
        int size = manager.getTasks().size();
        assertEquals(expectedSize, size, "Задача не добавилась");
    }

    @Test
    void testEmptyTasksTaskManager() {
        manager.deleteAllTasks();
        int expectedSize = 0;
        int size = manager.getTasks().size();
        assertEquals(expectedSize, size, "Список объектов-задач класса Task должен быть пустой");
    }

    @Test
    void testGetTaskTaskManager() {
        manager.addTask(task0);
        Task task = manager.getTask(task0.getId());
        assertNotNull(task, "Задача не добавилась");
    }

    @Test
    void testUpdateTaskTaskManager() {
        manager.addTask(task1);
        Task task2 = new Task("UPDATED", "UPDATED", Status.DONE, Duration.ofMinutes(0), LocalDateTime.now());
        task2.setId(task1.getId());
        Status expectedStatus = Status.NEW;
        Status status = task1.getStatus();
        assertEquals(expectedStatus, status, "Обновляемая задача уже изменена");
        String expectedName = "NEW";
        String name = task1.getName();
        assertEquals(expectedName, name, "Обновляемая задача уже изменена");
        String expectedDetails = "NEW";
        String details = task1.getDetails();
        assertEquals(expectedDetails, details, "Обновляемая задача уже изменена");
        manager.updateTask(task2);
        Task task = manager.getTask(task1.getId());
        assertNotNull(task, "Метод вернул значение null");
        long expectedId = task2.getId();
        long id = task1.getId();
        assertEquals(expectedId, id, "Id задачи не совпадает");
        task1 = manager.getTask(task1.getId());
        expectedStatus = Status.DONE;
        status = task1.getStatus();
        assertEquals(expectedStatus, status, "Статус задачи не обновился");
        expectedName = "UPDATED";
        name = task1.getName();
        assertEquals(expectedName, name, "Имя задачи не обновилось");
        expectedDetails = "UPDATED";
        details = task1.getDetails();
        assertEquals(expectedDetails, details, "Описание задачи не обновилось");
        assertDoesNotThrow(
                () -> {
                    manager.updateTask(null);
                });
    }

    @Test
    void testDeleteTaskTaskManager() {
        manager.addTask(task0);
        manager.deleteTask(task0.getId());
        Task task = manager.getTask(task0.getId());
        assertNull(task,"Метод вернул значение, отличное от null");
    }

    @Test
    void testAddSubtaskTaskManager() {
        manager.addEpic(epic0);
        Subtask subtask0 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(10), null, epic0.getId());
        Subtask subtask1 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(20), null, epic0.getId());
        manager.addSubtask(subtask0);
        manager.addSubtask(subtask1);
        List<Subtask> expected = List.of(subtask0, subtask1);
        List<Subtask> actual = manager.getSubtasks();
        assertTrue(expected.containsAll(actual),"Списки с объектами-задачами класса Subtask не равны");
    }

    @Test
    void testEmptySubtasksTaskManager() {
        manager.addEpic(epic0);
        Subtask subtask0 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(10), null, epic0.getId());
        Subtask subtask1 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(20), null, epic0.getId());
        manager.addSubtask(subtask0);
        manager.addSubtask(subtask1);
        manager.deleteAllSubtasks();
        int expectedSize = 0;
        int size = manager.getSubtasks().size();
        assertEquals(expectedSize, size,"Список объектов-задач класса Subtask должен быть пустой");
    }

    @Test
    void testAddCorrectAndIncorrectSubtaskTaskManager() {
        manager.addEpic(epic0);
        Subtask subtask0 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(10), null, epic0.getId());
        Subtask subtask1 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(20), null, epic0.getId());
        Subtask subtask2 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(20), null, 100);
        manager.addSubtask(subtask0);
        manager.addSubtask(subtask1);
        int expectedSize = 2;
        int size = manager.getSubtasks().size();
        assertEquals(expectedSize, size, "Задачи не добавились");
        InputMismatchException ex = assertThrows(
                InputMismatchException.class,
                () -> {
                    manager.addSubtask(subtask2);
                });
        String expectedMessage = "Указанный в подзадаче Epic не существует";
        String message = ex.getMessage();
        assertEquals(expectedMessage, message, "Текст исключения отличается от ожидаемого");
        manager.deleteEpic(epic0.getId());
        manager.deleteSubtask(subtask0.getId());
    }

    @Test
    void testUpdateSubtaskTaskManager() {
        manager.addEpic(epic0);
        Subtask subtask0 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(10), null, epic0.getId());
        Subtask subtask1 = new Subtask("UPDATED", "UPDATED", Status.DONE, Duration.ofMinutes(20), null, epic0.getId());
        manager.addSubtask(subtask0);
        subtask1.setId(subtask0.getId());
        manager.updateSubtask(subtask1);
        Subtask subtask = manager.getSubtask(subtask0.getId());
        assertNotNull((subtask), "Метод вернул значение null");
        long expectedId = subtask1.getId();
        long id = subtask0.getId();
        Assertions.assertEquals(expectedId, id, "Id задачи не совпадает");
        subtask1 = manager.getSubtask(subtask1.getId());
        Status expectedStatus = Status.DONE;
        Status status = subtask1.getStatus();
        assertEquals(expectedStatus, status, "Статус задачи не обновился");
        String name = "UPDATED";
        String expectedName = subtask1.getName();
        assertEquals(expectedName, name, "Имя задачи не обновилось");
        String expectedDetails = "UPDATED";
        String details = subtask1.getDetails();
        assertEquals(expectedDetails, details, "Описание задачи не обновилось");
        assertDoesNotThrow(
                () -> {
                    manager.updateSubtask(null);
                });
    }

    @Test
    void testDeleteSubtaskTaskManager() {
        manager.addEpic(epic0);
        Subtask subtask0 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(10), null, epic0.getId());
        manager.addSubtask(subtask0);
        manager.deleteSubtask(subtask0.getId());
        Task task = manager.getSubtask(subtask0.getId());
        assertNull(task, "Метод вернул значение, отличное от null");
     }

    @Test
    void testAddEpicTaskManager() {
        manager.addEpic(epic0);
        int expectedSize = 1;
        int size = manager.getEpics().size();
        assertEquals(expectedSize, size, "Списки с объектами-задачами класса Epic не равны");
    }

    @Test
    void testDeleteEpicsTaskManager() {
        manager.addEpic(epic0);
        Subtask subtask0 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(10), null, epic0.getId());
        manager.addSubtask(subtask0);
        manager.deleteAllEpics();
        int expectedSize = 0;
        int size = manager.getEpics().size();
        assertEquals(expectedSize, size,"Список объектов-задач класса Epic должен быть пустой");
        size = manager.getSubtasks().size();
        assertEquals(expectedSize, size, "Список объектов-задач класса Subtask должен быть пустой");
    }

    @Test
    void testGetEpicTaskManager() {
        manager.addEpic(epic0);
        Task task = manager.getEpic(epic0.getId());
        assertNotNull(task, "Метод вернул значение null");
    }

    @Test
    void testUpdateEpicTaskManager() {
        manager.addEpic(epic0);
        epic1 = new Epic("UPDATED", "UPDATED", Duration.ofMinutes(0), null);
        epic1.setId(epic0.getId());
        Status expectedStatus = Status.NEW;
        Status status = epic0.getStatus();
        assertEquals(expectedStatus, status, "Обновляемая задача уже изменена");
        String expectedName = "NEW";
        String name = epic0.getName();
        assertEquals(expectedName, name, "Обновляемая задача уже изменена");
        String expectedDetails = "NEW";
        String details = epic0.getDetails();
        assertEquals(expectedDetails, details, "Обновляемая задача уже изменена");
        manager.updateEpic(epic1);
        assertNotNull(manager.getEpic(epic1.getId()), "Метод вернул значение null");
        long expectedId = epic1.getId();
        long id = epic0.getId();
        assertEquals(expectedId, id, "Id задачи не совпадает");
        epic1 = manager.getEpic(epic1.getId());
        expectedName = "UPDATED";
        name = epic1.getName();
        assertEquals(expectedName, name, "Имя задачи не обновилось");
        expectedDetails = "UPDATED";
        details = epic1.getDetails();
        assertEquals(expectedDetails, details, "Описание задачи не обновилось");
        assertDoesNotThrow(
                () -> {
                    manager.updateEpic(null);
                });
    }

    @Test
    void testDeleteEpicTaskManager() {
        manager.addEpic(epic0);
        Subtask subtask0 = new Subtask("NEW", "NEW", Status.NEW, Duration.ofMinutes(10), null, epic0.getId());
        manager.addSubtask(subtask0);
        manager.deleteEpic(epic0.getId());
        Epic epic = manager.getEpic(epic0.getId());
        Subtask subtask = manager.getSubtask(subtask0.getId());
        assertNull(epic,"Метод вернул значение, отличное от null");
        assertNull(subtask, "Метод вернул значение, отличное от null");
    }

    @Test
    void testSortedTasksTaskManager() {
        manager.clearSortedTasks();
        manager.addTask(task0);
        manager.addTask(task1);
        List<Task> expected = new ArrayList<>();
        expected.add(task1);
        expected.add(task0);
        List<Task> tasks = manager.getSortedTasks();
        Assertions.assertEquals(expected, tasks, "Элементы не отсортированы по дате");
    }

}