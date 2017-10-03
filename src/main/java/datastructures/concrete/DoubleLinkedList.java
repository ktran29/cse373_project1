package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (front == null && back == null) {
            Node<T> temp = new Node<T>(item);
            front.next = temp;
            front.prev = null;
            back.prev = temp;
            back.next = null;
        } else {
            Node<T> temp = new Node<T>(front.next, item, back.prev);
            back.prev = temp;
            front.next = temp;
        }
        size++;
    }

    @Override
    public T remove() {
        if (back.prev != null) {
            back = back.prev;
            back.next = null;
            size--;
        }
        return front.data;
    }

    @Override
    public T get(int index) {
        Node<T> temp = front;
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        return temp.data;
    }

    @Override
    public void set(int index, T item) {
        Node<T> temp = front;
        for (int i = 0; i < index - 1; i++) {
            temp = temp.next;
        }
        temp.data = item;
    }

    @Override
    public void insert(int index, T item) {
        Node<T> temp = front;
        for (int i = 0; i < index - 1; i++) {
            temp = temp.next;
        }
        Node<T> nextTemp = temp.next;
        Node<T> newEntry = new Node<T>(temp, item, nextTemp);
        temp.next = newEntry;
        nextTemp.prev = newEntry;
    }

    @Override
    public T delete(int index) {
        Node<T> temp = front;
        for (int i = 0; i < index - 1; i++) {
            temp = temp.next;
        }
        Node<T> nextTemp = temp.next;
        if (nextTemp.next != null) {
            nextTemp = nextTemp.next;
        }
        temp.next = nextTemp;
        nextTemp.prev = temp;
        return front.data;
    }

    @Override
    public int indexOf(T item) {
        Node<T> temp = front;
        boolean foundIndex = false;
        int index = 0;
        while (!foundIndex) {
        		index++;
        		if(temp.data == item) {
        			foundIndex = true;
        		}
            temp = temp.next;
        }
        return index;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T other) {
        Node<T> temp = front;
        while (temp.next != null) {
            if (temp.data == other) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current.next != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                current = current.next;
                return current.data;
            }
        }
    }
}
