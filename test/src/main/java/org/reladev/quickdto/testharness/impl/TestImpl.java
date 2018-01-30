package org.reladev.quickdto.testharness.impl;

import org.reladev.quickdto.shared.QuickDtoUtil;
import org.reladev.quickdto.testharness.BasicTypesDtoDef;
import org.reladev.quickdto.testharness.CollectionsDtoDef;

@QuickDtoUtil(dtoDef = {BasicTypesDtoDef.class, CollectionsDtoDef.class}, strictCopy = false)
public class TestImpl extends SuperImpl {
    private int myInt;
    private String myString;
    private int readOnly;
    private boolean myBoolean;

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

    public int getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(int readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isMyBoolean() {
        return myBoolean;
    }

    public void setMyBoolean(boolean myBoolean) {
        this.myBoolean = myBoolean;
    }
}
