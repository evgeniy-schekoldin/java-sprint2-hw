package tests;

import model.Epic;
import model.Status;
import model.Subtask;

import org.junit.jupiter.api.*;
import service.Managers;
import service.TaskManager;
import java.time.Duration;
import java.time.LocalDateTime;

class EpicTest {

    TaskManager manager = Managers.getDefault();
    Epic epic = new Epic("NEW_0L", "NEW", null,null);

    @BeforeEach
    void beforeEach() {
        manager.addEpic(epic);
    }

    @AfterEach
    void afterEach() {
        manager.deleteEpic(epic.getId());
    }

    @Test
    void shouldReturnNewWithNoSubtasks() {
         Assertions.assertEquals(epic.getStatus(), Status.NEW, "Статус задачи не равен NEW");
    }

    @Test
    void shouldReturnNewFor2NewSubtasks() {
        Subtask subtask1 = new Subtask( "SUBTASK_NAME_1", "DETAILS", Status.NEW,
                Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("SUBTASK_NAME_2", "DETAILS", Status.NEW,
                Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());
        manager.addSubtask(subtask2);
        Assertions.assertEquals(epic.getStatus(), Status.NEW, "Статус задачи не равен NEW");
    }

    @Test
    void shouldReturnDoneForAllDoneSubtasks() {
        Subtask subtask1 = new Subtask("SUBTASK_NAME_1", "DETAILS", Status.DONE,
                Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("SUBTASK_NAME_2", "DETAILS", Status.DONE,
                Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());
        manager.addSubtask(subtask2);
        Assertions.assertEquals(epic.getStatus(), Status.DONE, "Статус задачи не равен DONE");
    }

    @Test
    void shouldReturnInProgressForNewAndDoneSubtasks() {
        Subtask subtask1 = new Subtask("SUBTASK_NAME_1", "DETAILS", Status.NEW,
                Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("SUBTASK_NAME_2", "DETAILS", Status.DONE,
                Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());
        manager.addSubtask(subtask2);
        Assertions.assertEquals(epic.getStatus(), Status.IN_PROGRESS, "Статус задачи не равен IN_PROGRESS");
    }

    @Test
    void shouldSetEpicStatusToInProgressAllInProgress() {
        Subtask subtask1 = new Subtask("SUBTASK_NAME_1", "DETAILS", Status.IN_PROGRESS,
                Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("SUBTASK_NAME_2", "DETAILS", Status.IN_PROGRESS,
                Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());
        manager.addSubtask(subtask2);
        Assertions.assertEquals(epic.getStatus(), Status.IN_PROGRESS, "Статус задачи не равен IN_PROGRESS");
    }

}