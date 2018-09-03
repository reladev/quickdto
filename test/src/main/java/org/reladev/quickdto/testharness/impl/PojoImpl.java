package org.reladev.quickdto.testharness.impl;

import org.reladev.quickdto.shared.QuickDtoHelper;
import org.reladev.quickdto.testharness.Pojo;

@QuickDtoHelper(copyClass = Pojo.class)
public class PojoImpl {
    public String text;
    public PojoChildImpl child;
}
