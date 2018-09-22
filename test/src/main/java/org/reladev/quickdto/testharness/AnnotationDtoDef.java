package org.reladev.quickdto.testharness;

import org.reladev.quickdto.shared.EqualsHashCode;
import org.reladev.quickdto.shared.ExcludeCopyFrom;
import org.reladev.quickdto.shared.ExcludeCopyTo;
import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.AnnotationImpl;

@QuickDto(sources = AnnotationImpl.class)
public class AnnotationDtoDef {
    @EqualsHashCode
    int equalsHash;
    @ExcludeCopyFrom
    int readOnly;
    @ExcludeCopyTo
    int writeOnly;

    String result;
}
