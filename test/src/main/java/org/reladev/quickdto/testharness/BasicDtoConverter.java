package org.reladev.quickdto.testharness;

import java.util.ArrayList;
import java.util.List;

import org.reladev.quickdto.testharness.impl.BasicImpl;

public class BasicDtoConverter {
    public static BasicImpl convert(BasicDto basicDto) {
        if (basicDto == null) {
            return null;
        }
        BasicImpl basic = new BasicImpl();
        basicDto.copyTo(basic);
        return basic;
    }

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
}
