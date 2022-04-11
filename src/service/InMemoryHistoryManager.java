package service;

import model.Node;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int HISTORY_SIZE = 10;

    private LinkedList<Task> history = new LinkedList<>();
    private Map<Long, Node<Task>> nodes = new HashMap<>();

    public void addTask(Task task) {
        if (history.getTasks().contains(task)) {
            history.removeNode(nodes.get(task.getId()));
            nodes.remove(task.getId());
        }

        nodes.put(task.getId(), history.linkLast(task));

        if (history.size() > HISTORY_SIZE) {
            Task data = history.getFirst();
            history.removeHead();
            nodes.remove(data.getId());
        }

    }

    public void removeTask(Long id) {
        if (nodes.containsKey(id)) {
            history.removeNode(nodes.get(id));
        }
    }

    public List<Task> getHistory() {
        return history.getTasks();
    }

    @Override
    public String toString() {
        List<Task> history = getHistory();
        StringBuilder sb = new StringBuilder();
        for (Task task : history) {
            sb.append(task.getId() + ",");
        }
        return sb.toString();
    }
}