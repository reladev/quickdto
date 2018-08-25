package org.reladev.quickdto.processor;

import java.util.List;

import org.reladev.quickdto.shared.QuickDto;

@QuickDto(source = ClassTesterImpl.class, converter = ClassTesterImpl.class)
public class ClassTesterDtoDef {

    private int primitive;
    private Integer boxed;
    private String string;
    private List<String> isStrings;
}
