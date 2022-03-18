package service;

public class Node<T> {

    protected T data;
    protected Node<T> next;
    protected Node<T> prev;

    protected Node(Node<T> prev, T data, Node<T> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

}

