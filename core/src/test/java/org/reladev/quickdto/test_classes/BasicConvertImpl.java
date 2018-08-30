package org.reladev.quickdto.test_classes;


import java.util.List;

//@QuickDtoHelper(copyClass = BasicTypesDtoDef.class)
public class BasicConvertImpl {
    BasicTypesImpl basic;
    List<BasicTypesImpl> basicList;
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
}
