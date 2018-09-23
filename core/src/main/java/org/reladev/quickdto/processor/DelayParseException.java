package org.reladev.quickdto.processor;

import javax.lang.model.type.TypeMirror;

public class DelayParseException extends RuntimeException {
    public DelayParseException(TypeMirror typeMirror) {
        super(typeMirror.toString());
    }
}
