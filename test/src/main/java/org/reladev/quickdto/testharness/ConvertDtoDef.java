package org.reladev.quickdto.testharness;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.Basic;
import org.reladev.quickdto.testharness.impl.Convert;

@QuickDto(source = Convert.class)
public class ConvertDtoDef {
    BasicDtoDef basic;

    public static BasicDto convert(Basic basic) {
        BasicDto basicDto = new BasicDto();
        basicDto.copyFrom(basic);
        return basicDto;
    }

    public static Basic convert(BasicDto basicDto) {
        Basic basic = new Basic();
        basicDto.copyTo(basic);
        return basic;
    }
}
