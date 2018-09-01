package org.reladev.quickdto.testharness.impl;

import org.reladev.quickdto.shared.QuickDtoHelper;
import org.reladev.quickdto.testharness.ConvertExistingDtoDef;

@QuickDtoHelper(copyClass = ConvertExistingDtoDef.class)
public class ConvertExistingImpl {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
