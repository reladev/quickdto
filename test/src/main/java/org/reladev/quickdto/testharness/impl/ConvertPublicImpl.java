package org.reladev.quickdto.testharness.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reladev.quickdto.shared.QuickCopy;
import org.reladev.quickdto.testharness.ConvertChildDto;
import org.reladev.quickdto.testharness.ConvertDtoDef;
import org.reladev.quickdto.testharness.ConvertExistingImplConverter;

@QuickCopy(targets = ConvertDtoDef.class, converters = ConvertExistingImplConverter.class)
public class ConvertPublicImpl {
    public ConvertChildImpl child;
    public List<ConvertChildImpl> childList;
    public ConvertExistingImpl existing;
    public List<ConvertExistingImpl> existingList;

    // duplicates to test variables in copy
    public ConvertChildImpl child2;
    public List<ConvertChildImpl> childList2;
    public ConvertExistingImpl existing2;
    public List<ConvertExistingImpl> existingList2;

    //future support
    public ConvertChildImpl[] childArray;
    public Set<ConvertChildImpl> childSet;
    public Map<String, ConvertChildImpl> childMap;

    public static ConvertChildImpl convert2ChildImpl(ConvertChildDto source) {
        if (source == null) {
            return null;
        }
        ConvertChildImpl convert = new ConvertChildImpl();
        ConvertChildImplCopyUtil.copy(source, convert);
        return convert;
    }
}
