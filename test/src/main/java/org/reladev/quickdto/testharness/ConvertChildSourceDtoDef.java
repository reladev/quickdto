package org.reladev.quickdto.testharness;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.ConvertChildImpl;

@QuickDto(source = ConvertChildImpl.class)
public class ConvertChildSourceDtoDef {
    String text;
}
