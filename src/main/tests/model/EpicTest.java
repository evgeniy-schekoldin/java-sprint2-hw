package main.tests.model;

import main.java.model.Epic;
import main.java.model.Status;
import main.java.model.Subtask;

import org.junit.jupiter.api.*;
import main.java.service.Managers;
import main.java.service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    TaskManager manager = Managers.getDefault();
    Epic epic = new Epic("NEW_0L", "NEW", null,null);
    Subtask subtaskNew = new Subtask( "SUBTASK_NAME_1", "DETAILS", Status.NEW,
            Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());

    @BeforeEach
    void beforeEach() {
        manager.addEpic(epic);
    }

    @AfterEach
    void afterEach() {
        manager.deleteEpic(epic.getId());
    }

    @Test
    void testEpicStatusWithoutSubtasks() {
        Status exceptedStatus = Status.NEW;
        Status status = epic.getStatus();
        assertEquals(exceptedStatus, status, "Статус задачи не равен NEW");
    }

    @Test
    void testEpicStatusWithNewSubtasks() {
        Subtask subtask1 = new Subtask( "SUBTASK_NAME_1", "DETAILS", Status.NEW,
                Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("SUBTASK_NAME_2", "DETAILS", Status.NEW,
                Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());
        manager.addSubtask(subtask2);
        Status exceptedStatus = Status.NEW;
        Status status = epic.getStatus();
        assertEquals(exceptedStatus, status, "Статус задачи не равен NEW");
    }

    @Test
    void testEpicStatusWithDoneSubtasks() {
        Subtask subtask1 = new Subtask("SUBTASK_NAME_1", "DETAILS", Status.DONE,
                Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("SUBTASK_NAME_2", "DETAILS", Status.DONE,
                Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());
        manager.addSubtask(subtask2);
        Status exceptedStatus = Status.DONE;
        Status status = epic.getStatus();
        assertEquals(exceptedStatus, status, "Статус задачи не равен DONE");
    }

    @Test
    void testEpicStatusWithDoneAndNewSubtasks() {
        Subtask subtask1 = new Subtask("SUBTASK_NAME_1", "DETAILS", Status.NEW,
                Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("SUBTASK_NAME_2", "DETAILS", Status.DONE,
                Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());
        manager.addSubtask(subtask2);
        Status exceptedStatus = Status.IN_PROGRESS;
        Status status = epic.getStatus();
        assertEquals(exceptedStatus, status, "Статус задачи не равен IN_PROGRESS");
    }

    @Test
    void testEpicStatusWithInProgressSubtasks() {
        Subtask subtask1 = new Subtask("SUBTASK_NAME_1", "DETAILS", Status.IN_PROGRESS,
                Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("SUBTASK_NAME_2", "DETAILS", Status.IN_PROGRESS,
                Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());
        manager.addSubtask(subtask2);
        Status exceptedStatus = Status.IN_PROGRESS;
        Status status = epic.getStatus();
        assertEquals(exceptedStatus, status, "Статус задачи не равен IN_PROGRESS");
    }

}