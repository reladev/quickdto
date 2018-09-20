package org.reladev.quickdto.testharness;

import java.util.ArrayList;
import java.util.List;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.ConvertChildImpl;
import org.reladev.quickdto.testharness.impl.ConvertExistingImpl;

@QuickDto
public class ConvertDtoDef {
    ConvertChildDtoDef child;
    List<ConvertChildDtoDef> childList;
    ConvertExistingDtoDef existing;
    List<ConvertExistingDtoDef> existingList;

    // duplicates to test variables in copy
    ConvertChildDtoDef child2;
    List<ConvertChildDtoDef> childList2;
    ConvertExistingDtoDef existing2;
    List<ConvertExistingDtoDef> existingList2;


    //todo support automatic conversion for other common types
    //ConvertChildSourceDtoDef[] childArray;
    //Set<ConvertChildSourceDtoDef> childSet;
    //Map<String, ConvertChildSourceDtoDef> childMap;

    public static List<ConvertChildImpl> convert(List<ConvertChildSourceDto> convertChildDtoList, List<ConvertChildImpl> existing) {
        if (convertChildDtoList == null) {
            return null;
        }
        List<ConvertChildImpl> basicList = new ArrayList<>();
        for (ConvertChildSourceDto convertChildDto: convertChildDtoList) {
            ConvertChildImpl basic = new ConvertChildImpl();
            convertChildDto.copyTo(basic);
            basicList.add(basic);
        }
        return basicList;
    }

    public static ConvertExistingSourceDto convert(ConvertExistingImpl existingParam, ConvertExistingSourceDto existing) {
        if (existingParam == null) {
            return null;
        }
        if (existing == null) {
            existing = new ConvertExistingSourceDto();
        }
        existing.copyFrom(existingParam);
        return existing;
    }

    public static ConvertExistingImpl convert(ConvertExistingSourceDto existingParamDto, ConvertExistingImpl existing) {
        if (existingParamDto == null) {
            return null;
        }
        if (existing == null) {
            existing = new ConvertExistingImpl();
        }
        existingParamDto.copyTo(existing);
        return existing;
    }
}
