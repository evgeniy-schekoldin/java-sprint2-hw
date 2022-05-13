package main.java.service;

import main.java.model.Node;

import java.util.ArrayList;
import java.util.List;

public class LinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size = 0;

    public Node<T> linkLast(T element) {
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

    public void removeNode(Node<T> node) {
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

    public void clear() {
        for (Node<T> node = head; node != null; ) {
            Node<T> next = node.getNext();
            node.setData(null);
            node.setNext(null);
            node.setPrev(null);
            node = next;
        }
        head = tail = null;
        size = 0;
    }

}
