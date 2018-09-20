package org.reladev.quickdto.testharness.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reladev.quickdto.shared.QuickDtoHelper;
import org.reladev.quickdto.testharness.ConvertChildSourceDtoDef;
import org.reladev.quickdto.testharness.ConvertDtoConverter;
import org.reladev.quickdto.testharness.ConvertDtoDef;
import org.reladev.quickdto.testharness.ConvertExistingDto;

@QuickDtoHelper(copyClass = ConvertDtoDef.class, converter = ConvertDtoConverter.class)
public class ConvertImpl {
    private ConvertChildImpl child;
    private List<ConvertChildImpl> childList;
    private ConvertExistingImpl existing;
    List<ConvertExistingImpl> existingList;

    ConvertChildSourceDtoDef[] childArray;
    Set<ConvertChildSourceDtoDef> childSet;
    Map<String, ConvertChildSourceDtoDef> childMap;

    public static ConvertExistingDto convert(ConvertExistingImpl source, ConvertExistingDto existing) {
        if (source == null) {
            return null;
        }
        if (existing == null) {
            existing = new ConvertExistingDto();
        }
        ConvertExistingImplHelper.copy(source, existing);
        return existing;
    }

    public static ConvertExistingImpl convert(ConvertExistingDto source, ConvertExistingImpl existing) {
        if (source == null) {
            return null;
        }
        if (existing == null) {
            existing = new ConvertExistingImpl();
        }
        ConvertExistingImplHelper.copy(source, existing);
        return existing;
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
}
