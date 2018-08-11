package org.reladev.quickdto.testharness;

import org.reladev.quickdto.feature.DirtyFeature;
import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.Basic;

@QuickDto(source = Basic.class, feature = DirtyFeature.class)
public class BasicDtoDef {
    String text;
}
