package main.tests.service;

import main.java.model.Status;
import main.java.model.Task;
import main.java.service.FileBackedTasksManager;
import main.java.service.HistoryManager;
import main.java.service.Managers;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest {

    private static final String DB_PATH = "db.txt";
    private static final String LINE_DELIMITER = "\n";
    private static final String SECTION_DELIMITER = "\n\n";

    private final HistoryManager history = Managers.getDefaultHistory();

    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager(DB_PATH));
    }

    @Test
    void testReadWriteFileBackedTasksManager() throws IOException {
        int taskSection = 0;
        int historySection = 1;
        int taskIndex = 0;
        Task task0 = new Task("NEW", "NEW", Status.NEW, Duration.ofMinutes(0), LocalDateTime.now().plusMinutes(90));
        manager.addTask(task0);
        manager.getTask(task0.getId());
        String savedState[] = Files.readString(Path.of(DB_PATH)).split(SECTION_DELIMITER);
        String task[] = savedState[taskSection].split(LINE_DELIMITER);
        String expectedTaskValue = task0.toString();
        String taskValue = task[taskIndex];
        String expectedHistoryValue = savedState[historySection];
        String historyValue = history.getIds();
        assertEquals(expectedTaskValue, taskValue, "Задача не сохранилась в файл");
        assertEquals(expectedHistoryValue, historyValue, "История не сохранилась в файл");
    }

    @Test
    void testFileClearFileBackedTasksManager() throws IOException {
        String savedState[] = Files.readString(Path.of(DB_PATH)).split(LINE_DELIMITER);
        int expectedSize = 0;
        int size = savedState.length;
        assertEquals(expectedSize, size, "После удаления всех задач в @ForEach родительского класса, " +
                "файл не пустой");
    }

}