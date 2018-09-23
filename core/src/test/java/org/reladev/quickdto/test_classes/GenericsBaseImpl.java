package org.reladev.quickdto.test_classes;

public class GenericsBaseImpl<T, K> {
    private T id;
    private K key;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }
}
