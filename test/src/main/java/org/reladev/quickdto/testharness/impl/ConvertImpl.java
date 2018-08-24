package org.reladev.quickdto.testharness.impl;

import java.util.List;

//@QuickDtoHelper(copyClass = ConvertDtoDef.class, converter = BasicDtoConverter.class, strictCopy = false)
public class ConvertImpl {
    private BasicImpl basic;
    private List<BasicImpl> basicList;
    private ExistingParamImpl existing;

    public BasicImpl getBasic() {
        return basic;
    }

    public void setBasic(BasicImpl basic) {
        this.basic = basic;
    }

    public List<BasicImpl> getBasicList() {
        return basicList;
    }

    public void setBasicList(List<BasicImpl> basicList) {
        this.basicList = basicList;
    }

    public ExistingParamImpl getExisting() {
        return existing;
    }

    public void setExisting(ExistingParamImpl existing) {
        this.existing = existing;
    }
}
