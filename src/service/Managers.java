package service;

public class Managers {

    private static HistoryManager historyManager = new InMemoryHistoryManager();
    private static TaskManager taskManager = new InMemoryTaskManager();

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
}