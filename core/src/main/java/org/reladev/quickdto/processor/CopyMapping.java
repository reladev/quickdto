package org.reladev.quickdto.processor;

import java.util.Arrays;
import java.util.Collection;

import javax.tools.Diagnostic.Kind;

import static org.reladev.quickdto.processor.QuickDtoProcessor.processingEnv;

public class CopyMapping {
    private String name;
    private Field getField;
    private Field setField;
    private ConverterMethod converterMethod;
    private boolean isQuickDtoConvert;
    private boolean isQuickDtoListConvert;

    public static CopyMapping build(Field getField, Field setField, ConverterMap converterMap) {
        if (getField == null || setField == null || !getField.isGettable() || !setField.isSettable()) {
            return null;
        }

        CopyMapping copyMapping = new CopyMapping(getField, setField);

        Type getType = getField.getType();
        Type setType = setField.getType();

        boolean map;
        if (getType.equals(setType)) {
            map = true;

        } else { // try to find converter
            ConverterMethod converter = converterMap.get(getType, setType);
            if (converter != null) {
                map = true;
                copyMapping.converterMethod = converter;

            } else if (setField.getType().isQuickDto()) {
                copyMapping.isQuickDtoConvert = true;
                map = true;

            } else if (setField.getType().isQuickDtoList()) {
                copyMapping.isQuickDtoListConvert = true;
                map = true;

            } else {
                map = false;
                processingEnv.getMessager().printMessage(Kind.WARNING, "Type Mismatch(" + setType + ":" + getType + ") for " + getField.getAccessorName());
            }
        }

        if (map) {
            return copyMapping;
        } else {
            return null;
        }
    }

    public CopyMapping(Field getField, Field setField) {
        this.getField = getField;
        this.setField = setField;

        name = getField.getName();
    }

    public Collection<Type> getImports() {
        return Arrays.asList(getField.getType(), setField.getType());
    }

    public String getName() {
        return name;
    }

    public Field getGetField() {
        return getField;
    }

    public Field getSetField() {
        return setField;
    }

    public ConverterMethod getConverterMethod() {
        return converterMethod;
    }

    public boolean isQuickDtoConvert() {
        return isQuickDtoConvert;
    }

    public boolean isQuickDtoListConvert() {
        return isQuickDtoListConvert;
    }

    @Override
    public String toString() {
        return name + "(" + getField.getType() + "->" + setField.getType() + ")";
    }
}
