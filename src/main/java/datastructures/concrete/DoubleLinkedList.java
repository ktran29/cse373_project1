package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
//import misc.exceptions.NotYetImplementedException;

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
            Node<T> newNode = new Node<T>(item);
            front = newNode;
            back = newNode;
        } else {
            Node<T> newNode = new Node<T>(back, item, null);
            Node<T> temp = back;
            back = newNode;
            temp.next = newNode;
        }
        size++;
    }

    @Override
    public T remove() {
        if (size == 0) {
            throw new EmptyContainerException();
        } else if (front != null) {
            T returnedValue = back.data;
            back = back.prev;
            size--;
            return returnedValue;
        } else {
            return null;
        }
    }

    @Override
    public T get(int index) {
        if (index > size - 1) {
            throw new IndexOutOfBoundsException();
        }
        if (front != null) {
            Node<T> temp = front;
            for (int i = 0; i < index - 1; i++) {
                temp = temp.next;
            }
            return temp.data;
        }
        return null;
    }

    @Override
    public void set(int index, T item) {
        if (index > size - 1) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> temp = front;
        for (int i = 0; i < index; i++) {
        		if (temp.next != null) {
        			temp = temp.next;	
        		}
        }
        Node<T> newEntry = new Node<T>(temp.prev, item, temp.next);
        if (temp.prev != null) {
        		temp.prev.next = newEntry;
        } else {
        		front = newEntry;
        }
        if(temp.next != null) {
        		temp.next.prev = newEntry;
        } else {
        		back = newEntry;
        }
    }

    @Override
    public void insert(int index, T item) {
    		if (item != null) {
    			Node<T> temp = front;
    	        if (index == 0) {
    	            Node<T> newEntry = new Node<T>(null, item, front);
    	            front = newEntry;
    	        } else if (index == size) {
    	        		Node<T> newEntry = new Node<T>(back, item, null);
    	        		back = newEntry;
    			} else if (index > size / 2) {
    				temp = back;
    				for (int i = size - 1; i > index; i--) {
    	                temp = temp.prev;
    	            }
    	            Node<T> newEntry = new Node<T>(temp.prev, item, temp);
    	            temp.prev = newEntry;
    			} else {
    	            for (int i = 0; i < index; i++) {
    	                temp = temp.next;
    	            }
    	            Node<T> newEntry = new Node<T>(temp, item, temp.next);
    	            temp.next = newEntry;
    	        }
    		}
    		size++;
    }

    @Override
    public T delete(int index) {
        Node<T> temp = front;
        for (int i = 0; i < index; i++) {
        		if(temp.next != null) {
        			temp = temp.next;
        		}
        }
        T returnedValue = temp.data;
        if (temp.next == null) {
        		temp.prev = null;
        } else {
        		temp.prev = temp.next;
        }
        size--;
        return returnedValue;
    }

    @Override
    public int indexOf(T item) {
        if (front != null) {
            int index = 0;
            Node<T> temp = front;
            while (temp != null) {
                if (temp.data == item) {
                    return index;
                } else {
                		index++;
                		temp = temp.next;
                }
            }
        }
        return -1;

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
            return current != null && current.next != null;
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
