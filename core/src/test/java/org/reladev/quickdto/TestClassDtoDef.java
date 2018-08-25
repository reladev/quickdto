package org.reladev.quickdto;

import java.util.List;

import org.reladev.quickdto.shared.QuickDto;

@QuickDto(source = TestClassImpl.class, converter = TestClassImpl.class)
public class TestClassDtoDef {

    private int primitive;
    private Integer boxed;
    private String string;
    private List<String> isStrings;
}
