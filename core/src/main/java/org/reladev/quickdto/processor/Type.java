package org.reladev.quickdto.processor;

import java.util.Objects;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.reladev.quickdto.shared.QuickDto;

import static org.reladev.quickdto.processor.QuickDtoProcessor.processingEnv;

public class Type {
    private TypeMirror typeMirror;
    private TypeKind typeKind;

    private String name;
    private String packageString;
    private String qualifiedName;

    private boolean isQuickDto;

    //transient
    private String primitiveBoxType;

    public Type(TypeMirror typeMirror) {
        this.typeMirror = typeMirror;
        typeKind = typeMirror.getKind();

        if (typeKind == TypeKind.DECLARED) {
            TypeElement typeElement = (TypeElement) processingEnv.getTypeUtils().asElement(typeMirror);
            name = typeElement.getSimpleName().toString();
            packageString = typeElement.getEnclosingElement().toString();
            qualifiedName = typeElement.getQualifiedName().toString();

            QuickDto[] annotations = typeElement.getAnnotationsByType(QuickDto.class);
            if (annotations.length > 0) {
                isQuickDto = true;
            }

        } else {
            name = typeMirror.toString();
            packageString = "";
            qualifiedName = name;
        }
    }

    public boolean isQuickDto() {
        return isQuickDto;
    }

    public boolean isQuickDtoList() {
        // todo implement
        return false;
    }

    public boolean isPrimitive() {
        return typeKind.isPrimitive();
    }

    public String getPrimitiveBoxType() {
        if (primitiveBoxType == null && isPrimitive()) {
            if ("int".equals(name)) {
                primitiveBoxType = "Integer";
            } else if ("boolean".equals(name)) {
                primitiveBoxType = "Boolean";
            } else if ("long".equals(name)) {
                primitiveBoxType = "Long";
            } else if ("double".equals(name)) {
                primitiveBoxType = "Double";
            } else if ("float".equals(name)) {
                primitiveBoxType = "Float";
            } else if ("char".equals(name)) {
                primitiveBoxType = "Character";
            } else if ("byte".equals(name)) {
                primitiveBoxType = "Byte";
            } else if ("short".equals(name)) {
                primitiveBoxType = "Short";
            }
        }
        return primitiveBoxType;
    }


    public boolean isBoolean() {
        return "boolean".equals(name) || "Boolean".equals(name);
    }

    public boolean isList() {
        return false;
    }

    public Type getListType() {
        return null;
    }

    public String getName() {
        return name;
    }

    public String getPackageString() {
        return packageString;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Type)) {
            return false;
        }
        Type type = (Type) o;
        return Objects.equals(packageString, type.packageString) && Objects.equals(name, type.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageString, name);
    }
}
