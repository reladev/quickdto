package org.reladev.quickdto.testharness.impl;

import org.reladev.quickdto.shared.QuickDtoHelper;
import org.reladev.quickdto.testharness.PojoChild;

@QuickDtoHelper(copyClass = PojoChild.class)
public class PojoChildImpl {
    public String text;
}
