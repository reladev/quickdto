package org.reladev.quickdto.testharness.impl;

import org.reladev.quickdto.jartest.JarredDtoDef;
import org.reladev.quickdto.shared.QuickDtoHelper;

@QuickDtoHelper(dtoDef = {JarredDtoDef.class})
public class Simple {
    private int normal;
    private int readOnly;
    private int readOnlyWithSetter;
    private int writeOnly;

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
}
