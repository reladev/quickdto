package org.reladev.quickdto.testharness;

import org.reladev.quickdto.shared.CopyFromOnly;
import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.AnnotationImpl;

@QuickDto(source = AnnotationImpl.class)
public class AnnotaitonSourceDtoDef {
    int normal;
    @CopyFromOnly
    int readOnly;
    @CopyFromOnly(setter = true)
    int readOnlyWithSetter;
    int writeOnly;

    String result;
}
