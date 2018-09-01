package org.reladev.quickdto.testharness.impl;

import org.reladev.quickdto.shared.QuickDtoHelper;
import org.reladev.quickdto.testharness.ConvertChildDtoDef;

@QuickDtoHelper(copyClass = ConvertChildDtoDef.class)
public class ConvertChildImpl {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
