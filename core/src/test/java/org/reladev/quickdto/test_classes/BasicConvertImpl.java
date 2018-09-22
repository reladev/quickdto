package org.reladev.quickdto.test_classes;


import java.util.List;
import java.util.Set;

//@QuickDtoHelper(copyClass = BasicTypesDtoDef.class)
public class BasicConvertImpl {
    private BasicTypesImpl basic;
    private List<BasicTypesImpl> basicList;
    private Set<BasicTypesImpl> basicSet;
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

    public Set<BasicTypesImpl> getBasicSet() {
        return basicSet;
    }

    public void setBasicSet(Set<BasicTypesImpl> basicSet) {
        this.basicSet = basicSet;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
