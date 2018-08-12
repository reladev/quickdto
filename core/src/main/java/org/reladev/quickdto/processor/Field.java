package org.reladev.quickdto.processor;

import javax.lang.model.type.TypeMirror;

@SuppressWarnings("WeakerAccess")
public class Field implements Component {
    private TypeMirror type;
    private String typeString;
    private String primitiveTypeString;
    private String fieldName;
    private String accessorName;
    private String enumName;
    private QuickDtoFlags flags = new QuickDtoFlags();

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

    public static String normalizeFieldName(String fieldName) {
        char firstChar = fieldName.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            firstChar = Character.toLowerCase(firstChar);
            fieldName = firstChar + fieldName.substring(1);
        }
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = normalizeFieldName(fieldName);
    }

    public boolean isPrimitive() {
        return type.getKind().isPrimitive();
    }

    public String getPrimitiveTypeString() {
        if (primitiveTypeString == null) {
            String typeString = getTypeString();
            if ("int".equals(typeString)) {
                primitiveTypeString = "Integer";
            } else if ("boolean".equals(typeString)) {
                primitiveTypeString = "Boolean";
            } else if ("long".equals(typeString)) {
                primitiveTypeString = "Long";
            } else if ("double".equals(typeString)) {
                primitiveTypeString = "Double";
            } else if ("float".equals(typeString)) {
                primitiveTypeString = "Float";
            } else if ("char".equals(typeString)) {
                primitiveTypeString = "Character";
            } else if ("byte".equals(typeString)) {
                primitiveTypeString = "Byte";
            } else if ("short".equals(typeString)) {
                primitiveTypeString = "Short";
            }
        }
        return primitiveTypeString;
    }


    public String getAccessorName() {
        if (accessorName == null) {
            char firstChar = getFieldName().charAt(0);
            firstChar = Character.toUpperCase(firstChar);
            accessorName = firstChar + getFieldName().substring(1);
        }
        return accessorName;
    }

    public String getGetAccessorName() {
        if ("boolean".equals(getTypeString()) || "java.lang.Boolean".equals(getTypeString())) {
            return "is" + getAccessorName();
        } else {
            return "get" + getAccessorName();
        }
    }

    public String getEnumName() {
        if (enumName == null) {
            enumName = getFieldName().replaceAll("(.)([A-Z])", "$1_$2").toUpperCase();
        }
        return enumName;
    }

    public boolean isSourceMapped() {
        return sourceMapped;
    }

    public void setSourceMapped() {
        this.sourceMapped = true;
    }

    public QuickDtoFlags getFlags() {
        return flags;
    }

    public String toString() {
        return type + " " + fieldName;
    }
}
