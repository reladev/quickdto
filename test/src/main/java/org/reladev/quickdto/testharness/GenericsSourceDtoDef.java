package org.reladev.quickdto.testharness;

import org.reladev.quickdto.shared.QuickDto;

@QuickDto(source = GenericsDtoDef.class)
public class GenericsSourceDtoDef extends InheritanceBaseDtoDef {
    String text;
}
