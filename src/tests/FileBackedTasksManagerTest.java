package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class FileBackedTasksManagerTest extends TaskManagerTest {

    private final HistoryManager history = Managers.getDefaultHistory();

    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager("db.txt"));
    }

    @Test
    void readWriteTest() throws IOException {
        manager.addTask(task0);
        manager.addEpic(epic0);
        manager.getTask(task0.getId());
        manager.getEpic(epic0.getId());
        String db[] = Files.readString(Path.of("db.txt")).split("\n\n");
        String task[] = db[0].split("\n");
        Assertions.assertEquals(task[0], epic0.toString(), "Задача не сохранилась в файл");
        Assertions.assertEquals(task[1], task0.toString(), "Задача не сохранилась в файл");
        Assertions.assertEquals(db[1], history.getIds(), "История не сохранилась в файл");
    }

    @Test
    void afterEachWriteTest() throws IOException {
        String db[] = Files.readString(Path.of("db.txt")).split("\n");
        Assertions.assertEquals(db.length, 0, "После удаления всех задач в @ForEach родительского класса, " +
                "файл не пустой");
    }

}