package org.reladev.quickdto.test_classes;

public class AnnotationImpl {
    private int equalsHash;
    private int readOnly;
    private int readOnlyWithSetter;
    private int writeOnly;
    private String result;

    public int getEqualsHash() {
        return equalsHash;
    }

    public void setEqualsHash(int equalsHash) {
        this.equalsHash = equalsHash;
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

    public int getWriteOnly() {
        return writeOnly;
    }

    public void setWriteOnly(int writeOnly) {
        this.writeOnly = writeOnly;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
