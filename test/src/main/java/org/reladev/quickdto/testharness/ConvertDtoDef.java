package org.reladev.quickdto.testharness;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.Basic;
import org.reladev.quickdto.testharness.impl.Convert;
import org.reladev.quickdto.testharness.impl.ExistingParam;

@QuickDto(source = Convert.class)
public class ConvertDtoDef {
    BasicDtoDef basic;
    ExistingParamDtoDef existing;

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

    public static ExistingParamDto convert(ExistingParam existingParam, ExistingParamDto existing) {
        if (existingParam == null) {
            return null;
        }
        existing.copyFrom(existingParam);
        return existing;
    }

    public static ExistingParam convert(ExistingParamDto existingParamDto, ExistingParam existing) {
        if (existingParamDto == null) {
            return null;
        }
        existingParamDto.copyTo(existing);
        return existing;
    }
}
