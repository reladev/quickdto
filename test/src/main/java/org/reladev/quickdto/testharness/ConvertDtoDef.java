package org.reladev.quickdto.testharness;

import java.util.ArrayList;
import java.util.List;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.BasicImpl;
import org.reladev.quickdto.testharness.impl.ConvertImpl;
import org.reladev.quickdto.testharness.impl.ExistingParamImpl;

@QuickDto(source = ConvertImpl.class, converter = BasicDtoConverter.class)
public class ConvertDtoDef {
    BasicDtoDef basic;
    List<BasicDtoDef> basicList;
    ExistingParamDtoDef existing;

    //public static BasicImpl convert(BasicDto basicDto) {
    //    if (basicDto == null) {
    //        return null;
    //    }
    //    BasicImpl basic = new BasicImpl();
    //    basicDto.copyTo(basic);
    //    return basic;
    //}

    public static List<BasicImpl> convert(List<BasicDto> basicDtoList, List<BasicImpl> existing) {
        if (basicDtoList == null) {
            return null;
        }
        List<BasicImpl> basicList = new ArrayList<>();
        for (BasicDto basicDto : basicDtoList) {
            BasicImpl basic = new BasicImpl();
            basicDto.copyTo(basic);
            basicList.add(basic);
        }
        return basicList;
    }

    public static ExistingParamDto convert(ExistingParamImpl existingParam, ExistingParamDto existing) {
        if (existingParam == null) {
            return null;
        }
        if (existing == null) {
            existing = new ExistingParamDto();
        }
        existing.copyFrom(existingParam);
        return existing;
    }

    public static ExistingParamImpl convert(ExistingParamDto existingParamDto, ExistingParamImpl existing) {
        if (existingParamDto == null) {
            return null;
        }
        if (existing == null) {
            existing = new ExistingParamImpl();
        }
        existingParamDto.copyTo(existing);
        return existing;
    }
}
