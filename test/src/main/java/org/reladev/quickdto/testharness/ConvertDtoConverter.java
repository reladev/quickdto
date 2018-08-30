package org.reladev.quickdto.testharness;

import java.util.ArrayList;
import java.util.List;

import org.reladev.quickdto.testharness.impl.ConvertChildImpl;

public class ConvertDtoConverter {
    public static ConvertChildImpl convert(ConvertChildSourceDto convertChildDto) {
        if (convertChildDto == null) {
            return null;
        }
        ConvertChildImpl basic = new ConvertChildImpl();
        convertChildDto.copyTo(basic);
        return basic;
    }

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
}
