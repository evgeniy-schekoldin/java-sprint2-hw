package service;

import model.Node;

import java.util.ArrayList;
import java.util.List;

/* Если назвать этот класс также, как "нормальный" LinkedList, не будет ли потом путаницы? Может быть поэтому
* в ТЗ было указано размещать его не в отдельном файле, а внутри класса InMemoryHistoryManager? */

public class LinkedList<T> {
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
            oldTail.setNext(newNode);
        }
        size++;
        return newNode;
    }

    public List<T> getTasks() {
        Node<T> node = tail;
        List<T> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(node.getData());
                node = node.getPrev();
        }
        return result;
    }

    public void removeHead() {
        head = head.getNext();
        head.setPrev(null);
        size--;
    }

    public void removeNode(Node node) {
        Node<T> next = node.getNext();
        Node<T> prev = node.getPrev();

        if (prev != null) {
            prev.setNext(next);
        } else {
            head = next;
        }

        if (next != null) {
            next.setPrev(prev);
        } else {
            tail = prev;
        }

        size--;
    }

    public int size() {
        return size;
    }

    public T getFirst() {
        return head.getData();
    }
}
