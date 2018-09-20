package org.reladev.quickdto.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class ClassDef {
    private Type type;
    private ClassDef superClassDef;

    private LinkedHashMap<String, Field> nameFieldMap = new LinkedHashMap<>();
    private ConverterMap converterMap = new ConverterMap();
    private Map<Type, Type> genericsMap = new HashMap<>();


    public ClassDef(TypeElement typeElement) {
        type = new Type(typeElement.asType());

        for (Element subElement : typeElement.getEnclosedElements()) {
            if (subElement.getKind() == ElementKind.FIELD) {
                Field field = Field.build((VariableElement) subElement, this);
                nameFieldMap.put(field.getName(), field);

            } else if (subElement.getKind() == ElementKind.METHOD) {
                ExecutableElement execElement = (ExecutableElement) subElement;
                Field field = Field.build(execElement, this);
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
            TypeElement superElement = (TypeElement) superClassType.asElement();
            List<? extends TypeParameterElement> generics = superElement.getTypeParameters();
            List<? extends TypeMirror> genericsType = superClassType.getTypeArguments();

            Iterator<? extends TypeParameterElement> genericsIt = generics.iterator();
            Iterator<? extends TypeMirror> genericsTypeIt = genericsType.iterator();
            while (genericsIt.hasNext() && genericsTypeIt.hasNext()) {
                genericsMap.put(new Type(genericsIt.next().asType()), new Type(genericsTypeIt.next()));
            }

            superClassDef = new ClassDef((TypeElement) superClassType.asElement());
            superClassDef.fixGenericTypes(genericsMap);
            nameFieldMap.putAll(superClassDef.nameFieldMap);
        }
    }

    private void fixGenericTypes(Map<Type, Type> generics) {
        for (Field field: nameFieldMap.values()) {
            field.fixGenericTypes(generics);
        }
    }

    public List<Type> getImports() {
        List<Type> imports = new ArrayList<>();
        for (Field field: nameFieldMap.values()) {
            imports.add(field.getType());
        }
        return imports;
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

    public Map<Type, Type> getGenericsMap() {
        return genericsMap;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
