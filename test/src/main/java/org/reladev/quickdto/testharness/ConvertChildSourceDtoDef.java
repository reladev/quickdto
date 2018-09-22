package org.reladev.quickdto.testharness;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.ConvertChildImpl;

@QuickDto(sources = ConvertChildImpl.class)
public class ConvertChildSourceDtoDef {
    String text;
}
