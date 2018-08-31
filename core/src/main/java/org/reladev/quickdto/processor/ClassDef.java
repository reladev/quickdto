package org.reladev.quickdto.processor;

import java.util.LinkedHashMap;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class ClassDef {
    private Type type;
    private ClassDef superClassDef;

    private LinkedHashMap<String, Field> nameFieldMap = new LinkedHashMap<>();
    private ConverterMap converterMap = new ConverterMap();


    public ClassDef(TypeElement typeElement) {
        type = new Type(typeElement.asType());

        for (Element subElement : typeElement.getEnclosedElements()) {
            if (subElement.getKind() == ElementKind.FIELD) {
                Field field = Field.build((VariableElement) subElement, type.isQuickDto());
                nameFieldMap.put(field.getName(), field);

            } else if (subElement.getKind() == ElementKind.METHOD) {
                ExecutableElement execElement = (ExecutableElement) subElement;
                Field field = Field.build(execElement);
                if (field != null) {
                    Field existing = nameFieldMap.get(field.getName());
                    if (existing != null) {
                        existing.merge(field);
                    } else {
                        nameFieldMap.put(field.getName(), field);
                    }

                } else {
                    ConverterMethod converter = ConverterMethod.build(execElement, type);
                    if (converter != null) {
                        converterMap.add(converter);
                    }
                }
            }
        }

        TypeMirror possibleSuperClass = typeElement.getSuperclass();
        if (possibleSuperClass != null && possibleSuperClass.getKind() == TypeKind.DECLARED && !possibleSuperClass.toString().equals(
              "java.lang.Object")) {
            DeclaredType superClassType = (DeclaredType) possibleSuperClass;
            superClassDef = new ClassDef((TypeElement) superClassType.asElement());
            nameFieldMap.putAll(superClassDef.nameFieldMap);
        }
    }

    public Type getType() {
        return type;
    }

    public ClassDef getSuperClassDef() {
        return superClassDef;
    }

    public LinkedHashMap<String, Field> getNameFieldMap() {
        return nameFieldMap;
    }

    public ConverterMap getConverterMap() {
        return converterMap;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
