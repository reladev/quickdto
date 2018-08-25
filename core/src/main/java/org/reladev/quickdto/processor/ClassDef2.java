package org.reladev.quickdto.processor;

import java.util.LinkedHashMap;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

public class ClassDef2 {
    private Type type;

    private LinkedHashMap<String, Field2> nameFieldMap = new LinkedHashMap<>();
    private ConverterMap converterMap = new ConverterMap();


    public ClassDef2(TypeElement typeElement) {
        type = new Type(typeElement.asType());

        for (Element subElement : typeElement.getEnclosedElements()) {
            if (subElement.getKind() == ElementKind.FIELD) {
                Field2 field = Field2.build((VariableElement) subElement);
                nameFieldMap.put(field.getName(), field);

            } else if (subElement.getKind() == ElementKind.METHOD) {
                ExecutableElement execElement = (ExecutableElement) subElement;
                Field2 field = Field2.build(execElement);
                if (field != null) {
                    Field2 existing = nameFieldMap.get(field.getName());
                    if (existing != null) {
                        existing.merge(field);
                    } else {
                        nameFieldMap.put(field.getName(), field);
                    }

                } else {
                    ConverterMethod2 converter = ConverterMethod2.build(execElement, type);
                    if (converter != null) {
                        converterMap.add(converter);
                    }
                }
            }
        }
    }

    public Type getType() {
        return type;
    }

    public LinkedHashMap<String, Field2> getNameFieldMap() {
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
