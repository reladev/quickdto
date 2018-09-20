package org.reladev.quickdto.test_classes;


import java.util.List;

import org.reladev.quickdto.feature.DirtyFeature;
import org.reladev.quickdto.shared.QuickDto;

@QuickDto(source = BasicConvertImpl.class, feature = DirtyFeature.class, converter = StringIntConverter.class)
public class BasicConvertDtoDef {
    BasicTypesDtoDef basic;
    List<BasicTypesDtoDef> basicList;
    String value;

    public static BasicTypesImpl convert(BasicTypesDto convertChildDto) {
        throw new IllegalStateException("Testing only");
    }

    public static List<BasicTypesImpl> convert(List<BasicTypesDto> convertChildDtoList, List<BasicTypesImpl> existing) {
        throw new IllegalStateException("Testing only");
    }

}
