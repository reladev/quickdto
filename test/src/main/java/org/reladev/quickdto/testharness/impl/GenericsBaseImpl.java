package org.reladev.quickdto.testharness.impl;

public class GenericsBaseImpl<T> {
    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
