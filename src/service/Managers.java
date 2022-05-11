package service;

import java.io.IOException;

public class Managers {

    private static HistoryManager historyManager;
    private static TaskManager taskManager;

    public static TaskManager getDefault() {
        if (taskManager == null) {
            taskManager = new HTTPTaskManager("http://localhost:8078");
        }
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }
        return historyManager;
    }

}