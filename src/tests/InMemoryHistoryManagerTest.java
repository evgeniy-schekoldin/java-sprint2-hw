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
    void shouldReturn0ForEmptyHistory() {
        Assertions.assertEquals(history.getHistory().size(), 0, "Размер истории не равен 0");
    }

    @Test
    void shouldReturn1ForEqualItems() {
        manager.getEpic(epic0.getId());
        manager.getEpic(epic0.getId());
        Assertions.assertEquals(history.getHistory().size(), 1, "Размер истории не равен 1");
    }

    @Test
    void shouldReturnSeparatedHistoryIdValues() {
        manager.getEpic(epic0.getId());
        manager.getEpic(epic1.getId());
        String ids = epic1.getId() + "," + epic0.getId() + ",";
        Assertions.assertEquals(ids, history.getIds(),"История в виде строки возвращена не корректно");
    }

}