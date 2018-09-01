package org.reladev.quickdto.testharness;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.ConvertChildImpl;
import org.reladev.quickdto.testharness.impl.ConvertExistingImpl;
import org.reladev.quickdto.testharness.impl.ConvertImpl;

@QuickDto(source = ConvertImpl.class, converter = ConvertSourceDtoConverter.class)
public class ConvertSourceDtoDef {
    ConvertChildSourceDtoDef child;
    ConvertChildSourceDtoDef[] childArray;
    List<ConvertChildSourceDtoDef> childList;
    Set<ConvertChildSourceDtoDef> childSet;
    Map<String, ConvertChildSourceDtoDef> childMap;
    ConvertExistingSourceDtoDef existing;
    List<ConvertExistingSourceDtoDef> existingList;

    //public static BasicImpl convert(ConvertChildDto convertChildDto) {
    //    if (convertChildDto == null) {
    //        return null;
    //    }
    //    BasicImpl basic = new BasicImpl();
    //    convertChildDto.copyTo(basic);
    //    return basic;
    //}

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
