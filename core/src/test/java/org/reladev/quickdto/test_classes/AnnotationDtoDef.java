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

    @ExcludeCopy(AnnotationImpl.class)
    int excludeFromImpl;

    // Excludes Target
    @ExcludeCopy
    private String sourceNoFieldTargetExclude;
    @ExcludeCopy
    private String fieldTargetExclude;
    @ExcludeCopyFrom
    private String targetNoGetterTargetExcludeTo;
    @ExcludeCopyTo
    private String targetNoSetterTargetExcludeFrom;
    @ExcludeCopyTo
    private String sourceNoGetterTargetExcludeFrom;
    @ExcludeCopyFrom
    private String sourceNoSetterTargetExcludeTo;

    // Excludes Source
    private String fieldSourceExclude;
    private String targetNoGetterSourceExcludeTo;
    private String targetNoSetterSourceExcludeFrom;
    private String sourceNoGetterSourceExcludeFrom;
    private String sourceNoSetterSourceExcludeTo;
}
