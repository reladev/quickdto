package org.reladev.quickdto.testharness;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.ExistingParam;

@QuickDto(source = ExistingParam.class)
public class ExistingParamDtoDef {
    String text;
}
