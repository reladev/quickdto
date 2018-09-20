package org.reladev.quickdto.test_classes;

import org.reladev.quickdto.shared.EqualsHashCode;
import org.reladev.quickdto.shared.ExcludeCopy;
import org.reladev.quickdto.shared.ExcludeCopyFrom;
import org.reladev.quickdto.shared.ExcludeCopyTo;
import org.reladev.quickdto.shared.QuickDto;

@QuickDto(source = AnnotationImpl.class)
public class AnnotationDtoDef {
    @EqualsHashCode
    int equalsHash;
    @ExcludeCopyFrom
    int readOnly;
    @ExcludeCopyTo
    int writeOnly;
    @ExcludeCopy
    int exclude;
    @ExcludeCopy(AnnotationImpl.class)
    int excludeFromImpl;

    String result;
}
