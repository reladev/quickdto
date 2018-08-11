package org.reladev.quickdto.testharness;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.Basic;

@QuickDto(source = Basic.class)
public class BasicDtoDef {
    String text;
}
