package org.reladev.quickdto.jartest;

import org.reladev.quickdto.shared.ExcludeCopyTo;
import org.reladev.quickdto.shared.QuickDto;

@QuickDto
public class JarredDtoDef {
    float temp;
    int normal;
    @ExcludeCopyTo(setter = true)
    int readOnlyWithSetter;
    int writeOnly;
    Double temp2;
}
