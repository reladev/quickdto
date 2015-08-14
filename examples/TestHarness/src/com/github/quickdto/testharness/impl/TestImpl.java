package com.github.quickdto.testharness.impl;

public class TestImpl extends SuperImpl {
    private int myInt;
    private String myString;

    public int getMyInt() {
        return myInt;
    }

    public void setMyInt(int myInt) {
        this.myInt = myInt;
    }

    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }

    public void setReadOnly(int readOnly) {

    }

    public int getReadOnly() {
        return 0;
    }

    public boolean isMyBoolean() {
        return false;
    }
}
