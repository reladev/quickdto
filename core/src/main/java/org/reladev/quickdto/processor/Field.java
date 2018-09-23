package org.reladev.quickdto.processor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.reladev.quickdto.shared.EqualsHashCode;
import org.reladev.quickdto.shared.ExcludeCopy;
import org.reladev.quickdto.shared.ExcludeCopyFrom;
import org.reladev.quickdto.shared.ExcludeCopyTo;

import static org.reladev.quickdto.processor.AnnotationUtil.parseClassNameList;

@SuppressWarnings("WeakerAccess")
public class Field {
    private Type type;
    private String name;
    private String accessorName;
    private String enumName;

    private ClassDef parent;

    private boolean isPublic;
    private boolean hasGetter;
    private boolean hasSetter;
    private boolean excludeSetter;
    private boolean excludeGetter;
    private List<String> notCopyGettable;
    private List<String> notCopySettable;
    private boolean equalsHashCode;

    private List<String> fieldAnnotations = new LinkedList<>();

    public static Field build(VariableElement variableElement, ClassDef parent) {
        Field field = new Field(variableElement.toString(), variableElement.asType(), parent);
        if (parent.getType().isQuickDto()) {
            field.hasGetter = true;
            field.hasSetter = true;
        }
        populateAnnotations(variableElement, field);
        field.isPublic = variableElement.getModifiers().contains(Modifier.PUBLIC);

        return field;
    }

    public static Field build(ExecutableElement element, ClassDef parent) {
        Field field = null;

        ExecutableType t = (ExecutableType) element.asType();
        String accessorName = element.toString();
        if (accessorName.startsWith("get") && t.getReturnType().getKind() != TypeKind.VOID && t.getParameterTypes().size() == 0) {
            field = new Field(accessorName.substring(3, accessorName.indexOf('(')), t.getReturnType(), parent);
            populateAnnotations(element, field);
            field.hasGetter = true;

        } else if (accessorName.startsWith("is") && t.getReturnType().getKind() != TypeKind.VOID && t.getParameterTypes().size() == 0) {
            field = new Field(accessorName.substring(2, accessorName.indexOf('(')), t.getReturnType(), parent);
            populateAnnotations(element, field);
            field.hasGetter = true;

        } else if (accessorName.startsWith("set") && t.getReturnType().getKind() == TypeKind.VOID && t.getParameterTypes().size() == 1) {
            field = new Field(accessorName.substring(3, accessorName.indexOf('(')), t.getParameterTypes().get(0), parent);
            populateAnnotations(element, field);
            field.hasSetter = true;
        }

        return field;
    }

    private static void populateAnnotations(Element subelement, Field field) {
        List<? extends AnnotationMirror> annotationMirrors = subelement.getAnnotationMirrors();
        for (AnnotationMirror am: annotationMirrors) {
            Map<? extends ExecutableElement, ? extends AnnotationValue> annotationParams = am.getElementValues();
            if (!QuickDtoProcessor.isQuickDtoAnntoation(am)) {
                field.fieldAnnotations.add(am.toString());

            } else if (EqualsHashCode.class.getName().equals(am.getAnnotationType().toString())) {
                field.equalsHashCode = true;

            } else if (ExcludeCopy.class.getName().equals(am.getAnnotationType().toString())) {
                List<String> targets = Collections.emptyList();
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry: annotationParams.entrySet()) {
                    switch (entry.getKey().getSimpleName().toString()) {
                        case "value":
                            targets = parseClassNameList(entry.getValue());
                            break;
                    }
                }
                field.notCopyGettable = targets;
                field.notCopySettable = targets;

            } else if (ExcludeCopyTo.class.getName().equals(am.getAnnotationType().toString())) {
                List<String> targets = Collections.emptyList();
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry: annotationParams.entrySet()) {
                    switch (entry.getKey().getSimpleName().toString()) {
                        case "getter":
                            AnnotationValue action = entry.getValue();
                            field.hasGetter = (boolean) action.getValue();
                            break;

                        case "value":
                            targets = parseClassNameList(entry.getValue());
                            break;
                    }
                }
                field.notCopySettable = targets;

            } else if (ExcludeCopyFrom.class.getName().equals(am.getAnnotationType().toString())) {
                List<String> targets = Collections.emptyList();
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry: annotationParams.entrySet()) {
                    switch (entry.getKey().getSimpleName().toString()) {

                        case "setter":
                            AnnotationValue action = entry.getValue();
                            field.hasSetter = (boolean) action.getValue();
                            break;

                        case "value":
                            targets = parseClassNameList(entry.getValue());
                            break;
                    }
                }
                field.notCopyGettable = targets;

            }
        }
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


    public Field(String name, TypeMirror typeMirror, ClassDef parent) {
        type = new Type(typeMirror);
        this.name = normalizeName(name);
        accessorName = normalizeAccessorName(name, type);
        enumName = normalizeEnumName(name);
        this.parent = parent;
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public Field(Field field) {
        type = field.type;
        name = field.name;
        accessorName = field.accessorName;
        enumName = field.enumName;
    }

    public void merge(Field field) {
        assert name.equals(field.name);

        if (!type.equals(field.type)) {
            if (field.hasGetter || field.hasSetter) {
                type = field.type;
            }
        }

        isPublic |= field.isPublic;
        hasGetter |= field.hasGetter;
        hasSetter |= field.hasSetter;
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

    public boolean isCopyGettable(ClassDef copyClass) {
        return checkCopy(copyClass, notCopyGettable);
    }

    public boolean isCopySettable(ClassDef copyClass) {
        return checkCopy(copyClass, notCopySettable);
    }

    private boolean checkCopy(ClassDef copyClass, List<String> notCopy) {
        if (notCopy == null) {
            return true;
        } else if (notCopy.isEmpty()) {
            return false;
        } else {
            return !notCopy.contains(copyClass.getType().getQualifiedName());
        }
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

    public ClassDef getParent() {
        return parent;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean hasGetter() {
        return hasGetter;
    }

    public boolean hasSetter() {
        return hasSetter;
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
