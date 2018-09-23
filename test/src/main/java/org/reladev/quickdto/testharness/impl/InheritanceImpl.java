package org.reladev.quickdto.testharness.impl;

public class InheritanceImpl extends InheritanceBaseImpl {
    private String text = "inheritance";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
