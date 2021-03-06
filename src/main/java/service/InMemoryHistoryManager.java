package main.java.service;

import main.java.model.Node;
import main.java.model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int HISTORY_SIZE = 10;

    private LinkedList<Task> history = new LinkedList<>();
    private Map<Long, Node<Task>> nodes = new HashMap<>();

    public void addTask(Task task) {

        if (task == null) {
            return;
        }

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

    public void clear() {
        nodes.clear();
        history.clear();
    }

    public List<Task> getHistory() {
        return history.getTasks();
    }

    public String getIds() {
        List<Task> history = getHistory();
        StringBuilder sb = new StringBuilder();
        for (Task task : history) {
            sb.append(task.getId() + ",");
        }

        return sb.toString();
    }
}