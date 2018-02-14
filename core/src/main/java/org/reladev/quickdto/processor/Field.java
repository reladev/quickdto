package org.reladev.quickdto.processor;

import javax.lang.model.type.TypeMirror;

@SuppressWarnings("WeakerAccess")
public class Field implements Component {
    private TypeMirror type;
    private String typeString;
    private String fieldName;
    private boolean primitive;

    private boolean hasGetter;
    private boolean hasSetter;

    private boolean sourceMapped;

    public String getTypeString() {
        return typeString;
    }

    public TypeMirror getType() {
        return type;
    }

    public void setType(TypeMirror type) {
        this.type = type;
        this.typeString = type.toString();
        if (typeString.endsWith("DtoDef")) {
            typeString = typeString.substring(0, typeString.length() - 3);
        }
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        char firstChar = fieldName.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            firstChar = Character.toLowerCase(firstChar);
            fieldName = firstChar + fieldName.substring(1);
        }
        this.fieldName = fieldName;
    }

    public boolean isPrimitive() {
        return primitive;
    }

    public void setPrimitive() {
        this.primitive = true;

    }

    public boolean isSourceMapped() {
        return sourceMapped;
    }

    public void setSourceMapped() {
        this.sourceMapped = true;
    }

    public String toString() {
        return type + " " + fieldName;
    }
}
