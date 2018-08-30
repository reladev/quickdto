package org.reladev.quickdto.testharness.impl;

//@QuickDtoHelper(copyClass = AnnotaitonDtoDef.class)
public class AnnotationImpl {
    private int normal;
    private int readOnly;
    private int readOnlyWithSetter;
    private int writeOnly;
    private String result;

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(int readOnly) {
        this.readOnly = readOnly;
    }

    public int getReadOnlyWithSetter() {
        return readOnlyWithSetter;
    }

    public void setReadOnlyWithSetter(int readOnlyWithSetter) {
        this.readOnlyWithSetter = readOnlyWithSetter;
    }

    public void setWriteOnly(int writeOnly) {
        this.writeOnly = writeOnly;
    }

    public int checkWriteOnly() {
        return writeOnly;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
