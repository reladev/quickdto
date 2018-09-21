package org.reladev.quickdto.testharness.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reladev.quickdto.shared.QuickDtoHelper;
import org.reladev.quickdto.testharness.ConvertChildDto;
import org.reladev.quickdto.testharness.ConvertChildSourceDtoDef;
import org.reladev.quickdto.testharness.ConvertDtoDef;
import org.reladev.quickdto.testharness.ConvertExistingImplConverter;

@QuickDtoHelper(copyClass = ConvertDtoDef.class, converter = ConvertExistingImplConverter.class)
public class ConvertImpl {
    private ConvertChildImpl child;
    private List<ConvertChildImpl> childList;
    private ConvertExistingImpl existing;
    private List<ConvertExistingImpl> existingList;

    // duplicates to test variables in copy
    private ConvertChildImpl child2;
    private List<ConvertChildImpl> childList2;
    private ConvertExistingImpl existing2;
    private List<ConvertExistingImpl> existingList2;

    //future support
    private ConvertChildSourceDtoDef[] childArray;
    private Set<ConvertChildSourceDtoDef> childSet;
    private Map<String, ConvertChildSourceDtoDef> childMap;

    public static ConvertChildImpl convert2ChildImpl(ConvertChildDto source) {
        if (source == null) {
            return null;
        }
        ConvertChildImpl convert = new ConvertChildImpl();
        ConvertChildImplHelper.copy(source, convert);
        return convert;
    }

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

    public List<ConvertExistingImpl> getExistingList() {
        return existingList;
    }

    public void setExistingList(List<ConvertExistingImpl> existingList) {
        this.existingList = existingList;
    }

    public ConvertChildSourceDtoDef[] getChildArray() {
        return childArray;
    }

    public void setChildArray(ConvertChildSourceDtoDef[] childArray) {
        this.childArray = childArray;
    }

    public Set<ConvertChildSourceDtoDef> getChildSet() {
        return childSet;
    }

    public void setChildSet(Set<ConvertChildSourceDtoDef> childSet) {
        this.childSet = childSet;
    }

    public Map<String, ConvertChildSourceDtoDef> getChildMap() {
        return childMap;
    }

    public void setChildMap(Map<String, ConvertChildSourceDtoDef> childMap) {
        this.childMap = childMap;
    }

    public ConvertChildImpl getChild2() {
        return child2;
    }

    public void setChild2(ConvertChildImpl child2) {
        this.child2 = child2;
    }

    public List<ConvertChildImpl> getChildList2() {
        return childList2;
    }

    public void setChildList2(List<ConvertChildImpl> childList2) {
        this.childList2 = childList2;
    }

    public ConvertExistingImpl getExisting2() {
        return existing2;
    }

    public void setExisting2(ConvertExistingImpl existing2) {
        this.existing2 = existing2;
    }

    public List<ConvertExistingImpl> getExistingList2() {
        return existingList2;
    }

    public void setExistingList2(List<ConvertExistingImpl> existingList2) {
        this.existingList2 = existingList2;
    }
}
