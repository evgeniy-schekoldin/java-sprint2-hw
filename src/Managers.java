import java.util.List;

public class Managers {

    private static TaskManager manager = new InMemoryTaskManager();;
    private static HistoryManager history = new InMemoryHistoryManager();;

    public static TaskManager getDefault() {
        return manager;
    }

    public static HistoryManager getDefaultHistory() {
        return history;
    }
}

