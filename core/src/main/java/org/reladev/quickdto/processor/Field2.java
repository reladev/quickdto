package org.reladev.quickdto.processor;

import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

@SuppressWarnings("WeakerAccess")
public class Field2 {
    private Type type;
    private String name;
    private String accessorName;
    private String enumName;

    private boolean isPublic;
    private boolean isGettable;
    private boolean isSettable;

    //todo populate the annotations
    private List<String> fieldAnnotations = new LinkedList<>();
    private QuickDtoFlags flags = new QuickDtoFlags();


    //Transient fields


    private boolean sourceMapped;

    public static Field2 build(VariableElement variableElement, boolean isQuickDto) {
        Field2 field = new Field2(variableElement.toString(), variableElement.asType());
        if (isQuickDto) {
            //todo add support to fix this
            field.isGettable = true;
            field.isSettable = true;
        }
        return field;
    }

    public static Field2 build(ExecutableElement element) {
        Field2 field = null;

        ExecutableType t = (ExecutableType) element.asType();
        String accessorName = element.toString();
        if (accessorName.startsWith("get") && t.getReturnType().getKind() != TypeKind.VOID && t.getParameterTypes().size() == 0) {
            field = new Field2(accessorName.substring(3, accessorName.indexOf('(')), t.getReturnType());
            field.isGettable = true;

        } else if (accessorName.startsWith("is") && t.getReturnType().getKind() != TypeKind.VOID && t.getParameterTypes().size() == 0) {
            field = new Field2(accessorName.substring(2, accessorName.indexOf('(')), t.getReturnType());
            field.isGettable = true;

        } else if (accessorName.startsWith("set") && t.getReturnType().getKind() == TypeKind.VOID && t.getParameterTypes().size() == 1) {
            field = new Field2(accessorName.substring(3, accessorName.indexOf('(')), t.getParameterTypes().get(0));
            field.isSettable = true;
        }

        return field;
    }

    private String getFieldName(String name) {
        int start = 3;
        if (name.startsWith("is")) {
            start = 2;
        }
        int end = name.indexOf('(');
        if (end == -1) {
            end = name.length();
        }
        return name.substring(start, end);
    }


    public Field2(String name, TypeMirror typeMirror) {
        type = new Type(typeMirror);
        this.name = normalizeName(name);
        accessorName = normalizeAccessorName(name, type);
        enumName = normalizeEnumName(name);
    }

    public void merge(Field2 field) {
        assert name.equals(field.name);

        if (!type.equals(field.type)) {
            if (field.isGettable || field.isSettable) {
                type = field.type;
            }
        }

        isPublic |= field.isPublic;
        isGettable |= field.isGettable;
        isSettable |= field.isSettable;
    }

    public static String normalizeName(String fieldName) {
        char firstChar = fieldName.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            firstChar = Character.toLowerCase(firstChar);
            fieldName = firstChar + fieldName.substring(1);
        }
        return fieldName;
    }

    protected String normalizeAccessorName(String fieldName, Type type) {
        if (type.isBoolean() && fieldName.startsWith("is") && Character.isUpperCase(fieldName.charAt(3))) {
            fieldName = fieldName.substring(2);
        }
        char firstChar = fieldName.charAt(0);
        if (Character.isLowerCase(firstChar)) {
            firstChar = Character.toUpperCase(firstChar);
            fieldName = firstChar + fieldName.substring(1);
        }
        return fieldName;
    }

    protected static String normalizeEnumName(String fieldName) {
        return fieldName.replaceAll("(.)([A-Z])", "$1_$2").toUpperCase();
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getAccessorName() {
        return accessorName;
    }

    public String getFullGetAccessorName() {
        return type.getGetAccessorPrefix() + accessorName;
    }

    public String getEnumName() {
        return enumName;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isGettable() {
        return isGettable;
    }

    public boolean isSettable() {
        return isSettable;
    }

    public String getGetterPrefix() {
        if ("boolean".equals(type.getName())) {
            return "is" + getAccessorName();
        } else {
            return "get" + getAccessorName();
        }
    }

    public List<String> getFieldAnnotations() {
        return fieldAnnotations;
    }

    public QuickDtoFlags getFlags() {
        return flags;
    }

    public String toString() {
        return type + " " + name;
    }
}