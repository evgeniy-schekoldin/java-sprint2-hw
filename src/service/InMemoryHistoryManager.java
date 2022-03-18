package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int HISTORY_SIZE = 10;
    private LinkedHistoryList history = new LinkedHistoryList();
    private Map<Long, Node> nodes = new HashMap<>();

    public void addTask(Task task) {
        if (history.getTasks().contains(task)) {
            history.removeNode(nodes.get(task.getId()));
            nodes.remove(task.getId());
        }

        nodes.put(task.getId(), history.linkLast(task));

        if (history.size() > HISTORY_SIZE) {
            Task data = (Task) history.getFirst();
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

    public class LinkedHistoryList<T> {
        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        public Node linkLast(T element) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, element, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            size++;
            return newNode;
        }

        public List<T> getTasks() {
            Node<T> node = tail;
            List<T> result = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                result.add(node.data);
                node = node.prev;
            }
            return result;
        }

        public void removeHead() {
            head = head.next;
            head.prev = null;
        }

        public void removeNode(Node node) {
            Node<T> next = node.next;
            Node<T> prev = node.prev;

            if (prev != null) {
                prev.next = next;
            } else {
                head = next;
            }

            if (next != null) {
                next.prev = prev;
            } else {
                tail = prev;
            }

            size--;
        }

        public int size() {
            return size;
        }

        public T getFirst() {
            return head.data;
        }
    }
}