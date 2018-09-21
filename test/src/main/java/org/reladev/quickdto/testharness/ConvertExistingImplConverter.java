package org.reladev.quickdto.testharness;

import java.util.ArrayList;
import java.util.List;

import org.reladev.quickdto.testharness.impl.ConvertExistingImpl;

public class ConvertExistingImplConverter {
    public static ConvertExistingImpl convert2ExistingImpl(ConvertExistingSourceDto original, ConvertExistingImpl existing) {
        if (original == null) {
            return null;
        }
        if (existing == null) {
            existing = new ConvertExistingImpl();
        }
        original.copyTo(existing);
        return existing;
    }

    public static List<ConvertExistingImpl> convert2ExistingImplListSource(List<ConvertExistingSourceDto> original,
          List<ConvertExistingImpl> existing)
    {
        if (original == null) {
            return null;
        }
        List<ConvertExistingImpl> impls = new ArrayList<>();
        for (ConvertExistingSourceDto dto: original) {
            ConvertExistingImpl impl = new ConvertExistingImpl();
            dto.copyTo(impl);
            impls.add(impl);
        }
        return impls;
    }

    public static ConvertExistingImpl convert2ExistingImpl(ConvertExistingDto original, ConvertExistingImpl existing) {
        if (original == null) {
            return null;
        }
        if (existing == null) {
            existing = new ConvertExistingImpl();
        }
        //ConvertExistingImplHelper.copy(original, existing);
        return existing;
    }

    public static List<ConvertExistingImpl> convert2ExistingImplList(List<ConvertExistingDto> original, List<ConvertExistingImpl> existing) {
        if (original == null) {
            return null;
        }
        List<ConvertExistingImpl> impls = new ArrayList<>();
        for (ConvertExistingDto dto: original) {
            ConvertExistingImpl impl = new ConvertExistingImpl();
            //ConvertExistingImplHelper.copy(dto, impl);
            impls.add(impl);
        }
        return impls;
    }
}
