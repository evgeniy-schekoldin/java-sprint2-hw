import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> history = new ArrayList<>();

    private static int HISTORY_SIZE = 10;

    public void addTask(Task task) {
        if (history.size() < HISTORY_SIZE) {
            history.add(task);
        } else {
            history.add(task);
            history.remove(0);
        }

    }

    public List<Task> getHistory() {
        return history;
    }

}
