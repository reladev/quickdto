package org.reladev.quickdto.testharness;

import org.reladev.quickdto.shared.EqualsHashCode;
import org.reladev.quickdto.shared.ExcludeCopyFrom;
import org.reladev.quickdto.shared.ExcludeCopyTo;
import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.AnnotationImpl;

@QuickDto(source = AnnotationImpl.class)
public class AnnotationDtoDef {
    @EqualsHashCode
    int equalsHash;
    @ExcludeCopyFrom
    int readOnly;
    @ExcludeCopyFrom(setter = true)
    int readOnlyWithSetter;
    @ExcludeCopyTo
    int writeOnly;

    String result;
}
