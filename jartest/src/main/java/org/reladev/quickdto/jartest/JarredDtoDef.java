package org.reladev.quickdto.jartest;

import org.reladev.quickdto.shared.CopyFromOnly;
import org.reladev.quickdto.shared.QuickDto;

@QuickDto
public class JarredDtoDef {
    float temp;
    int normal;
    @CopyFromOnly(setter = true) int readOnlyWithSetter;
    int writeOnly;
    Double temp2;
}
