package org.reladev.quickdto.testharness;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.InheritanceImpl;

@QuickDto(sources = InheritanceImpl.class)
public class InheritanceSourceDtoDef extends InheritanceBaseDtoDef {
    String text;
}
