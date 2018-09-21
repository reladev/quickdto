package org.reladev.quickdto.test_dto;

import java.util.List;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.test_classes.TestClassImpl;

@QuickDto(source = TestClassImpl.class, converter = TestClassImpl.class)
public class TestClassDtoDef {

    private int primitive;
    private Integer boxed;
    private String string;
    private List<String> isStrings;
}
