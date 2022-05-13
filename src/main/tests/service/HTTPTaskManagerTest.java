package main.tests.service;

import main.java.service.Managers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import main.java.service.HTTPTaskManager;
import main.java.service.KVServer;
import main.java.service.TaskManager;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPTaskManagerTest extends TaskManagerTest  {

public HTTPTaskManagerTest() {
    super(Managers.getDefault());
}

    @BeforeAll
    static void beforeAll() throws IOException {
    new KVServer().start();
    }

    @Test
    void testNewHTTPTaskManagerLoadFromKVServer() {
    manager.addTask(task0);
    manager.addTask(task1);
    TaskManager newManager = new HTTPTaskManager("http://localhost:8078");
    int expectedSize = 2;
    int size = newManager.getTasks().size();
    assertEquals(expectedSize, size, "Загрузка с KvServer не удалась.");
    }

}