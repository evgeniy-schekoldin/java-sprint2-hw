package tests;

import model.Epic;
import model.Status;
import model.Task;
import org.junit.jupiter.api.*;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager history = Managers.getDefaultHistory();
    TaskManager manager = Managers.getDefault();
    Epic epic0 = new Epic(0L, "NEW_0L", "NEW", Status.NEW, null,null);
    Epic epic1 = new Epic(1L, "NEW_1L", "NEW", Status.NEW, null,null);

    @BeforeEach
    void beforeEach() {
        manager.updateEpic(epic0);
        manager.updateEpic(epic1);
    }

    @AfterEach
    void afterEach() {
        history.clear();
        manager.deleteEpic(epic0.getId());
        manager.deleteEpic(epic1.getId());
    }

    @Test
    void shouldReturn0ForEmptyHistory() {
        Assertions.assertEquals(history.getHistory().size(), 0);
    }

    @Test
    void shouldReturn1ForEqualItems() {
        manager.getEpic(0L);
        manager.getEpic(0L);
        Assertions.assertEquals(history.getHistory().size(), 1);
    }

    @Test
    void shouldReturnSeparatedHistoryIdValues() {
        manager.getEpic(0L);
        manager.getEpic(1L);
        Assertions.assertEquals(history.getIds(), "1,0,");
    }
}