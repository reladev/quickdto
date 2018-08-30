package org.reladev.quickdto.testharness.impl;

import java.util.List;

//@QuickDtoHelper(copyClass = ConvertDtoDef.class, converter = ConvertChildDtoConverter.class)
public class ConvertImpl {
    private ConvertChildImpl basic;
    private List<ConvertChildImpl> basicList;
    private ConvertExistingImpl existing;

    public ConvertChildImpl getBasic() {
        return basic;
    }

    public void setBasic(ConvertChildImpl basic) {
        this.basic = basic;
    }

    public List<ConvertChildImpl> getBasicList() {
        return basicList;
    }

    public void setBasicList(List<ConvertChildImpl> basicList) {
        this.basicList = basicList;
    }

    public ConvertExistingImpl getExisting() {
        return existing;
    }

    public void setExisting(ConvertExistingImpl existing) {
        this.existing = existing;
    }
}
