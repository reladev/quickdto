package org.reladev.quickdto.test_classes;


import java.util.List;

import org.reladev.quickdto.feature.DirtyFeature;
import org.reladev.quickdto.shared.QuickDto;

@QuickDto(source = BasicTypesImpl.class, strictCopy = false, feature = DirtyFeature.class)
public class BasicConvertDtoDef {
    BasicTypesDtoDef basic;
    List<BasicTypesDtoDef> basicList;
    String noConvert;
    List<String> noConvertList;

    public static BasicConvertImpl convert(BasicConvertDtoDef convertChildDto) {
        throw new IllegalStateException("Testing only");
    }

    public static List<BasicConvertImpl> convert(List<BasicConvertDtoDef> convertChildDtoList, List<BasicConvertImpl> existing) {
        throw new IllegalStateException("Testing only");
    }

}
