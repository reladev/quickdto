package org.reladev.quickdto.processor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.reladev.quickdto.shared.EqualsHashCode;
import org.reladev.quickdto.shared.ExcludeCopyFrom;
import org.reladev.quickdto.shared.ExcludeCopyTo;

@SuppressWarnings("WeakerAccess")
public class Field {
    private Type type;
    private String name;
    private String accessorName;
    private String enumName;

    private boolean isPublic;
    private boolean isGettable;
    private boolean isSettable;
    private boolean excludeSetter;
    private boolean excludeGetter;
    private boolean equalsHashCode;


    private List<String> fieldAnnotations = new LinkedList<>();
    //private QuickDtoFlags flags = new QuickDtoFlags();

    public static Field build(VariableElement variableElement, boolean isQuickDto) {
        Field field = new Field(variableElement.toString(), variableElement.asType());
        if (isQuickDto) {
            field.isGettable = true;
            field.isSettable = true;
        }
        populateAnnotations(variableElement, field);
        field.isPublic = variableElement.getModifiers().contains(Modifier.PUBLIC);

        return field;
    }

    public static Field build(ExecutableElement element) {
        Field field = null;

        ExecutableType t = (ExecutableType) element.asType();
        String accessorName = element.toString();
        if (accessorName.startsWith("get") && t.getReturnType().getKind() != TypeKind.VOID && t.getParameterTypes().size() == 0) {
            field = new Field(accessorName.substring(3, accessorName.indexOf('(')), t.getReturnType());
            populateAnnotations(element, field);
            field.isGettable = true;

        } else if (accessorName.startsWith("is") && t.getReturnType().getKind() != TypeKind.VOID && t.getParameterTypes().size() == 0) {
            field = new Field(accessorName.substring(2, accessorName.indexOf('(')), t.getReturnType());
            populateAnnotations(element, field);
            field.isGettable = true;

        } else if (accessorName.startsWith("set") && t.getReturnType().getKind() == TypeKind.VOID && t.getParameterTypes().size() == 1) {
            field = new Field(accessorName.substring(3, accessorName.indexOf('(')), t.getParameterTypes().get(0));
            populateAnnotations(element, field);
            field.isSettable = true;
        }

        return field;
    }

    private static void populateAnnotations(Element subelement, Field field) {
        List<? extends AnnotationMirror> annotationMirrors = subelement.getAnnotationMirrors();
        for (AnnotationMirror am: annotationMirrors) {
            if (!QuickDtoProcessor.isQuickDtoAnntoation(am)) {
                field.fieldAnnotations.add(am.toString());
            }
        }

        if (subelement.getAnnotation(EqualsHashCode.class) != null) {
            field.equalsHashCode = true;
        }
        ExcludeCopyTo excludeCopyTo = subelement.getAnnotation(ExcludeCopyTo.class);
        if (excludeCopyTo != null) {
            field.isGettable = false;
            if (!excludeCopyTo.setter()) {
                field.excludeSetter = true;
            }
        }
        ExcludeCopyFrom copyToOnly = subelement.getAnnotation(ExcludeCopyFrom.class);
        if (copyToOnly != null) {
            field.isSettable = false;
            if (!copyToOnly.getter()) {
                field.excludeGetter = true;
            }
        }
        //todo handle strictCopy
        //StrictCopy strictCopy = subelement.getAnnotation(StrictCopy.class);
        //if (strictCopy != null) {
        //    field.flags.setStrictCopy(strictCopy.value());
        //}
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


    public Field(String name, TypeMirror typeMirror) {
        type = new Type(typeMirror);
        this.name = normalizeName(name);
        accessorName = normalizeAccessorName(name, type);
        enumName = normalizeEnumName(name);
    }

    public void merge(Field field) {
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

    public boolean isExcludeSetter() {
        return excludeSetter;
    }

    public boolean isExcludeGetter() {
        return excludeGetter;
    }

    public boolean isEqualsHashCode() {
        return equalsHashCode;
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

    //public QuickDtoFlags getFlags() {
    //    return flags;
    //}

    public String toString() {
        return type + " " + name;
    }

    public void fixGenericTypes(Map<Type, Type> generics) {
        Type genericType = generics.get(this.type);
        if (genericType != null) {
            type = genericType;
        }
    }
}
