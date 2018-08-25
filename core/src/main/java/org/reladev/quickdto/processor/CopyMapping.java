package org.reladev.quickdto.processor;

import javax.tools.Diagnostic.Kind;

import static org.reladev.quickdto.processor.QuickDtoProcessor.processingEnv;

public class CopyMapping implements Component {
    private Field2 field;
    private Field2 accessorMethod;
    private ConverterMethod2 converterMethod;
    private boolean isQuickDtoConvert;
    private boolean isQuickDtoListConvert;

    public static CopyMapping build(Field2 getField, Field2 setField, ConverterMap converterMap) {
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

    public CopyMapping(Field2 field, Field2 accessorMethod) {
        this.field = field;
        this.accessorMethod = accessorMethod;
    }
}
