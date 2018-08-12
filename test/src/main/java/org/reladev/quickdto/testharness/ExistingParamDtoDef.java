package org.reladev.quickdto.testharness;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.ExistingParamImpl;

@QuickDto(source = ExistingParamImpl.class)
public class ExistingParamDtoDef {
    String text;
}
