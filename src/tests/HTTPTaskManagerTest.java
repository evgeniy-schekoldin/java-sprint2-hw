package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.HTTPTaskManager;
import service.TaskManager;

class HTTPTaskManagerTest extends TaskManagerTest  {

public HTTPTaskManagerTest() {
    super(new HTTPTaskManager("http://localhost:8078"));
}

    @Test
    void tasksSizeShouldEquals2AfterLoadFromKVServer() {
    manager.addTask(task0);
    manager.addTask(task1);
    TaskManager newManager = new HTTPTaskManager("http://localhost:8078");
    Assertions.assertEquals(2, newManager.getTasks().size(), "Загрузка с KvServer не удалась.");
    }

}