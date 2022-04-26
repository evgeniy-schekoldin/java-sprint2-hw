package service;

public class Managers {

    private static HistoryManager historyManager;
    private static TaskManager taskManager;

    public static TaskManager getDefault() {
        if (taskManager == null) {
            taskManager = new FileBackedTasksManager("db.txt");
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