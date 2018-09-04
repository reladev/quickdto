package org.reladev.quickdto.test_classes;

import org.reladev.quickdto.shared.EqualsHashCode;
import org.reladev.quickdto.shared.ExcludeCopyFrom;
import org.reladev.quickdto.shared.ExcludeCopyTo;
import org.reladev.quickdto.shared.QuickDto;

@QuickDto(source = AnnotationImpl.class)
public class AnnotationDtoDef {
    @EqualsHashCode
    int equalsHash;
    @ExcludeCopyTo
    int readOnly;
    @ExcludeCopyTo(setter = true)
    int readOnlyWithSetter;
    @ExcludeCopyFrom
    int writeOnly;

    String result;
}
