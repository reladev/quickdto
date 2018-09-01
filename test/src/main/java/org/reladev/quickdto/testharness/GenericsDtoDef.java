package org.reladev.quickdto.testharness;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.BasicTypesImpl;

@QuickDto
public class GenericsDtoDef {
    String text;
    private String id;
    private BasicTypesImpl key;
}
