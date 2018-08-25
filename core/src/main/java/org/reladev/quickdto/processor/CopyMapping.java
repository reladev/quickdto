package org.reladev.quickdto.processor;

import java.util.Arrays;
import java.util.Collection;

import javax.tools.Diagnostic.Kind;

import static org.reladev.quickdto.processor.QuickDtoProcessor.processingEnv;

public class CopyMapping implements Component {
    private String name;
    private Field2 getField;
    private Field2 setField;
    private ConverterMethod2 converterMethod;
    private boolean isQuickDtoConvert;
    private boolean isQuickDtoListConvert;

    public static CopyMapping build(Field2 getField, Field2 setField, ConverterMap converterMap) {
        if (getField == null || setField == null || (!getField.isGettable() && !setField.isSettable())) {
            return null;
        }

        CopyMapping copyMapping = new CopyMapping(getField, setField);

        Type getType = getField.getType();
        Type setType = setField.getType();

        boolean map;
        if (getType.equals(setType)) {
            map = true;

        } else { // try to find converter
            ConverterMethod2 converter = converterMap.get(getType, setType);
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

    public CopyMapping(Field2 getField, Field2 setField) {
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

    public Field2 getGetField() {
        return getField;
    }

    public Field2 getSetField() {
        return setField;
    }

    public ConverterMethod2 getConverterMethod() {
        return converterMethod;
    }

    public boolean isQuickDtoConvert() {
        return isQuickDtoConvert;
    }

    public boolean isQuickDtoListConvert() {
        return isQuickDtoListConvert;
    }
}
