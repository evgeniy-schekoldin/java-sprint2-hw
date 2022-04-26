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
    Epic epic = new Epic(0L, "NEW_0L", "NEW", Status.NEW, null,null);

    @BeforeEach
    void BeforeAll() {
        manager.updateEpic(epic);
    }

    @AfterEach
    void AfterAll() {
        manager.deleteEpic(epic.getId());
    }

    @Test
    void shouldReturnNewWithNoSubtasks() {
        Assertions.assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    void shouldReturnNewFor2NewSubtasks() {
        Subtask subtask1 = new Subtask(1L, "SUBTASK_NAME_1", "DETAILS", Status.NEW,
                Duration.ofMinutes(20), LocalDateTime.now(), 0L);
        manager.updateSubtask(subtask1);
        Subtask subtask2 = new Subtask(2L, "SUBTASK_NAME_2", "DETAILS", Status.NEW,
                Duration.ofMinutes(20), LocalDateTime.now(), 0L);
        manager.updateSubtask(subtask2);
        Assertions.assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    void shouldReturnDoneForAllDoneSubtasks() {
        Subtask subtask1 = new Subtask(1L, "SUBTASK_NAME_1", "DETAILS", Status.DONE,
                Duration.ofMinutes(20), LocalDateTime.now(), 0L);
        manager.updateSubtask(subtask1);
        Subtask subtask2 = new Subtask(2L, "SUBTASK_NAME_2", "DETAILS", Status.DONE,
                Duration.ofMinutes(20), LocalDateTime.now(), 0L);
        manager.updateSubtask(subtask2);
        Assertions.assertEquals(epic.getStatus(), Status.DONE);
    }

    @Test
    void shouldReturnInProgressForNewAndDoneSubtasks() {
        Subtask subtask1 = new Subtask(1L, "SUBTASK_NAME_1", "DETAILS", Status.NEW,
                Duration.ofMinutes(20), LocalDateTime.now(), 0L);
        manager.updateSubtask(subtask1);
        Subtask subtask2 = new Subtask(2L, "SUBTASK_NAME_2", "DETAILS", Status.DONE,
                Duration.ofMinutes(20), LocalDateTime.now(), 0L);
        manager.updateSubtask(subtask2);
        Assertions.assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void shouldSetEpicStatusToInProgressAllInProgress() {
        Subtask subtask1 = new Subtask(1L, "SUBTASK_NAME_1", "DETAILS", Status.IN_PROGRESS,
                Duration.ofMinutes(20), LocalDateTime.now(), 0L);
        manager.updateSubtask(subtask1);
        Subtask subtask2 = new Subtask(2L, "SUBTASK_NAME_2", "DETAILS", Status.IN_PROGRESS,
                Duration.ofMinutes(20), LocalDateTime.now(), 0L);
        manager.updateSubtask(subtask2);
        Assertions.assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }
}