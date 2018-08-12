package org.reladev.quickdto.testharness;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.BasicImpl;

@QuickDto(source = BasicImpl.class)
public class BasicDtoDef {
    String text;
}
