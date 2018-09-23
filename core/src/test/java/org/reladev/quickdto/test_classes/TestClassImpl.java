package org.reladev.quickdto.test_classes;

import java.util.List;

public class TestClassImpl {
    private int primitive;
    private Integer boxed;
    private String string;
    private List<String> isStrings;

    public int getPrimitive() {
        return primitive;
    }

    public void setPrimitive(int primitive) {
        this.primitive = primitive;
    }

    public Integer getBoxed() {
        return boxed;
    }

    public void setBoxed(Integer boxed) {
        this.boxed = boxed;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public List<String> getIsStrings() {
        return isStrings;
    }

    public void setIsStrings(List<String> isStrings) {
        this.isStrings = isStrings;
    }
}
