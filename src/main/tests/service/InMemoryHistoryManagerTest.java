package main.tests.service;

import main.java.model.Epic;
import org.junit.jupiter.api.*;
import main.java.service.HistoryManager;
import main.java.service.Managers;
import main.java.service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    HistoryManager history = Managers.getDefaultHistory();
    TaskManager manager = Managers.getDefault();
    Epic epic0 = new Epic( "NEW_0L", "NEW",  null,null);
    Epic epic1 = new Epic( "NEW_1L", "NEW",  null,null);

    @BeforeEach
    void beforeEach() {
        manager.addEpic(epic0);
        manager.addEpic(epic1);
    }

    @AfterEach
    void afterEach() {
        history.clear();
        manager.deleteEpic(epic0.getId());
        manager.deleteEpic(epic1.getId());
    }

    @Test
    public void testEmptyHistoryManager() {
        int expectedSize = 0;
        int size = history.getHistory().size();
        assertEquals(expectedSize, size, "История не пустая");
    }

    @Test
    void testEqualsHistoryManager() {
        manager.getEpic(epic0.getId());
        manager.getEpic(epic0.getId());
        int expectedSize = 1;
        int size = history.getHistory().size();
        assertEquals(expectedSize, size, "Размер истории не равен 1");
    }

    @Test
    void testSeparatedIdsReturnHistoryManager() {
        manager.getEpic(epic0.getId());
        manager.getEpic(epic1.getId());
        String expectedValue = epic1.getId() + "," + epic0.getId() + ",";
        String value = history.getIds();
        assertEquals(expectedValue, value,"История в виде строки возвращена не корректно");
    }

}