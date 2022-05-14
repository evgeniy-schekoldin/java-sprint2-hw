package main.tests.service;

import main.java.model.Status;
import main.java.model.Task;
import main.java.service.Managers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import main.java.service.HTTPTaskManager;
import main.java.service.KVServer;
import main.java.service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPTaskManagerTest extends TaskManagerTest {

    public HTTPTaskManagerTest() {
        super(Managers.getDefault());
    }

    private static final String KVSERVER_URL = "http://localhost:8078";

    @BeforeAll
    static void beforeAll() throws IOException {
        new KVServer().start();
    }

    @Test
    void testNewHTTPTaskManagerLoadFromKVServer() {
        Task task0 = new Task("NEW", "NEW", Status.NEW, Duration.ofMinutes(0), LocalDateTime.now().plusMinutes(90));
        Task task1 = new Task("NEW", "NEW", Status.NEW, Duration.ofMinutes(0), LocalDateTime.now());
        manager.addTask(task0);
        manager.addTask(task1);
        TaskManager newManager = new HTTPTaskManager(KVSERVER_URL);
        int expectedSize = 2;
        int size = newManager.getTasks().size();
        assertEquals(expectedSize, size, "Загрузка с KvServer не удалась.");
    }

}