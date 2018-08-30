package org.reladev.quickdto.testharness;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.ConvertExistingImpl;

@QuickDto(source = ConvertExistingImpl.class)
public class ConvertExistingSourceDtoDef {
    String text;
}
