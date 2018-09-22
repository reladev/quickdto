package org.reladev.quickdto.test_dto;


import java.util.List;
import java.util.Set;

import org.reladev.quickdto.feature.DirtyFeature;
import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.test_classes.BasicConvertImpl;
import org.reladev.quickdto.test_classes.BasicTypesImpl;
import org.reladev.quickdto.test_classes.StringIntConverter;

@QuickDto(source = BasicConvertImpl.class, feature = DirtyFeature.class, converter = StringIntConverter.class)
public class BasicConvertDtoDef {
    BasicTypesDtoDef basic;
    List<BasicTypesDtoDef> basicList;
    Set<BasicTypesDtoDef> basicSet;
    String value;

    public static BasicTypesImpl convert(BasicTypesDto convertChildDto) {
        throw new IllegalStateException("Testing only");
    }
}
