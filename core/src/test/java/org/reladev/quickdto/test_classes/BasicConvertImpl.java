package org.reladev.quickdto.test_classes;


import java.util.List;

//@QuickDtoHelper(copyClass = BasicTypesDtoDef.class)
public class BasicConvertImpl {
    BasicTypesImpl basic;
    List<BasicTypesImpl> basicList;
    Integer value;
    Integer toTargetOnly;
    Object noConvert;
    List<Object> noConvertList;

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

    public Object getNoConvert() {
        return noConvert;
    }

    public void setNoConvert(Object noConvert) {
        this.noConvert = noConvert;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getToTargetOnly() {
        return toTargetOnly;
    }

    public List<Object> getNoConvertList() {
        return noConvertList;
    }

    public void setNoConvertList(List<Object> noConvertList) {
        this.noConvertList = noConvertList;
    }
}
