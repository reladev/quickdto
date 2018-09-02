package org.reladev.quickdto.testharness.impl;

import java.util.List;

import org.reladev.quickdto.shared.QuickDtoHelper;
import org.reladev.quickdto.testharness.ConvertDtoConverter;
import org.reladev.quickdto.testharness.ConvertDtoDef;
import org.reladev.quickdto.testharness.ConvertExistingDto;

@QuickDtoHelper(copyClass = ConvertDtoDef.class, converter = ConvertDtoConverter.class)
public class ConvertPublicImpl {
    public ConvertChildImpl child;
    public List<ConvertChildImpl> childList;
    public ConvertExistingImpl existing;

    public static ConvertExistingDto convert(ConvertExistingImpl source, ConvertExistingDto existing) {
        if (source == null) {
            return null;
        }
        if (existing == null) {
            existing = new ConvertExistingDto();
        }
        ConvertExistingImplHelper.copy(source, existing);
        return existing;
    }

    public static ConvertExistingImpl convert(ConvertExistingDto source, ConvertExistingImpl existing) {
        if (source == null) {
            return null;
        }
        if (existing == null) {
            existing = new ConvertExistingImpl();
        }
        ConvertExistingImplHelper.copy(source, existing);
        return existing;
    }

}
