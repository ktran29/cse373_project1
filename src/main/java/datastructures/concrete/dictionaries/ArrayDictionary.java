package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import misc.exceptions.NotYetImplementedException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;

    // You're encouraged to add extra fields (and helper methods) though!

    public ArrayDictionary() {
        this.pairs = [];
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }

    @Override
    public V get(K key) {
        for (int i = 0; i < pairs.length; i++) {
            if(pairs[i].key == key) {
                return pairs[i].value;
            }
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        int index = 0;
        boolean foundKey = false;
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i].key == key) {
                foundKey = true;
                index = i;
            }
        }
        if (foundKey) {
            pairs[i].value = value;
        } else {
            pairs[pairs.length+1] = new Pair(key, value);
        }
    }

    @Override
    public V remove(K key) {
        int index = 0;
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i].key == key) {
                index = i;
                pairs.splice(index, 1);
            }
        }
        Pair removedValue = pairs[i];
        return removedValue.value;
    }

    @Override
    public boolean containsKey(K key) {
        for (int i = 0; i < pairs.length; i++) {
            if(pairs[i].key == key) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return pairs.length;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
