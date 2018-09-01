package org.reladev.quickdto.testharness;

import java.util.ArrayList;
import java.util.List;

import org.reladev.quickdto.testharness.impl.ConvertChildImpl;
import org.reladev.quickdto.testharness.impl.ConvertChildImplHelper;

public class ConvertDtoConverter {
    public static ConvertChildImpl convert(ConvertChildDto convertChildDto) {
        if (convertChildDto == null) {
            return null;
        }
        ConvertChildImpl basic = new ConvertChildImpl();
        ConvertChildImplHelper.copy(convertChildDto, basic);
        return basic;
    }

    public static List<ConvertChildImpl> convert(List<ConvertChildDto> convertChildDtoList, List<ConvertChildImpl> existing) {
        if (convertChildDtoList == null) {
            return null;
        }
        List<ConvertChildImpl> basicList = new ArrayList<>();
        for (ConvertChildDto convertChildDto: convertChildDtoList) {
            ConvertChildImpl basic = new ConvertChildImpl();
            ConvertChildImplHelper.copy(convertChildDto, basic);
            basicList.add(basic);
        }
        return basicList;
    }
}
