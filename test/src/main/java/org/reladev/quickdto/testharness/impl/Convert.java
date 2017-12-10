package org.reladev.quickdto.testharness.impl;

import java.util.List;

import org.reladev.quickdto.shared.QuickDtoUtil;
import org.reladev.quickdto.testharness.ConvertDtoDef;

@QuickDtoUtil(dtoDef = {ConvertDtoDef.class})
public class Convert {
    private int normal;
    private Simple simple;
    private List<Simple> simpleList;
    private boolean bool;

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public Simple getSimple() {
        return simple;
    }

    public void setSimple(Simple simple) {
        this.simple = simple;
    }

    public List<Simple> getSimpleList() {
        return simpleList;
    }

    public void setSimpleList(List<Simple> simpleList) {
        this.simpleList = simpleList;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }
}
