package org.reladev.quickdto.testharness;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.GenericsImpl;

@QuickDto(sources = GenericsImpl.class)
public class GenericsSourceDtoDef {
    private String text;
    private String id;
    private Integer key;
}
