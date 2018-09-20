package org.reladev.quickdto.test_classes;


import java.util.List;

//@QuickDtoHelper(copyClass = BasicTypesDtoDef.class)
public class BasicConvertImpl {
    private BasicTypesImpl basic;
    private List<BasicTypesImpl> basicList;
    private Integer value;

    public BasicTypesImpl getBasic() {
        return basic;
    }

    public void setBasic(BasicTypesImpl basic) {
        this.basic = basic;
    }

    public List<BasicTypesImpl> getBasicList() {
        return basicList;
    }

    public void setBasicList(List<BasicTypesImpl> basicList) {
        this.basicList = basicList;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
