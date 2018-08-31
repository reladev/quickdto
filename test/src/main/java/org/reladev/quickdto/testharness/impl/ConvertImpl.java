package org.reladev.quickdto.testharness.impl;

import java.util.List;

//@QuickDtoHelper(copyClass = ConvertDtoDef.class, converter = ConvertChildDtoConverter.class)
public class ConvertImpl {
    private ConvertChildImpl child;
    private List<ConvertChildImpl> childList;
    private ConvertExistingImpl existing;

    public ConvertChildImpl getChild() {
        return child;
    }

    public void setChild(ConvertChildImpl child) {
        this.child = child;
    }

    public List<ConvertChildImpl> getChildList() {
        return childList;
    }

    public void setChildList(List<ConvertChildImpl> childList) {
        this.childList = childList;
    }

    public ConvertExistingImpl getExisting() {
        return existing;
    }

    public void setExisting(ConvertExistingImpl existing) {
        this.existing = existing;
    }
}
