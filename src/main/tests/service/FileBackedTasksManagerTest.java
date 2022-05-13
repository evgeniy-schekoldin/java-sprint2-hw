package main.tests.service;

import main.java.service.FileBackedTasksManager;
import main.java.service.HistoryManager;
import main.java.service.Managers;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest {

    private static final String DB_PATH = "db.txt";
    private static final String LINE_DELIMITER = "\n";
    private static final String SECTION_DELIMITER = "\n\n";

    private final HistoryManager history = Managers.getDefaultHistory();

    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager("db.txt"));
    }

    @Test
    void testReadWriteFileBackedTasksManager() throws IOException {
        manager.addTask(task0);
        manager.getTask(task0.getId());
        String db[] = Files.readString(Path.of(DB_PATH)).split(SECTION_DELIMITER);
        String task[] = db[0].split(LINE_DELIMITER);
        String expectedTaskValue = task0.toString();
        String taskValue = task[0];
        String expectedHistoryValue = db[1];
        String historyValue = history.getIds();
        assertEquals(expectedTaskValue, taskValue, "Задача не сохранилась в файл");
        assertEquals(expectedHistoryValue, historyValue, "История не сохранилась в файл");
    }

    @Test
    void testFileClearFileBackedTasksManager() throws IOException {
        String db[] = Files.readString(Path.of(DB_PATH)).split(LINE_DELIMITER);
        int expectedSize = 0;
        int size = db.length;
        assertEquals(expectedSize, size, "После удаления всех задач в @ForEach родительского класса, " +
                "файл не пустой");
    }

}