package org.reladev.quickdto.processor;

import javax.lang.model.type.TypeMirror;

public class AccessorMethod implements Component {
    public TypeMirror type;
    public String accessorName;
    public String methodName;
    public String typeString;

    public boolean isGetter;
}
