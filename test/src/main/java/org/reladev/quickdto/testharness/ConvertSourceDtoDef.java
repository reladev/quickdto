package org.reladev.quickdto.testharness;

import java.util.List;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.ConvertChildImpl;
import org.reladev.quickdto.testharness.impl.ConvertImpl;
import org.reladev.quickdto.testharness.impl.ConvertPublicImpl;

@QuickDto(source = {ConvertImpl.class, ConvertPublicImpl.class}, converter = ConvertExistingImplConverter.class)
public class ConvertSourceDtoDef {
    ConvertChildSourceDtoDef child;
    List<ConvertChildSourceDtoDef> childList;
    ConvertExistingSourceDtoDef existing;
    List<ConvertExistingSourceDtoDef> existingList;

    //todo support automatic conversion for other common types
    //ConvertChildSourceDtoDef[] childArray;
    //Set<ConvertChildSourceDtoDef> childSet;
    //Map<String, ConvertChildSourceDtoDef> childMap;

    public static ConvertChildImpl convert2ChildImpl(ConvertChildSourceDto original) {
        if (original == null) {
            return null;
        }
        ConvertChildImpl convert = new ConvertChildImpl();
        original.copyTo(convert);
        return convert;
    }
}
