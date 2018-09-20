package org.reladev.quickdto.jartest;

import org.reladev.quickdto.shared.ExcludeCopyFrom;
import org.reladev.quickdto.shared.QuickDto;

@QuickDto
public class JarredDtoDef {
    float temp;
    int normal;
    @ExcludeCopyFrom(setter = true)
    int readOnlyWithSetter;
    int writeOnly;
    Double temp2;
}
