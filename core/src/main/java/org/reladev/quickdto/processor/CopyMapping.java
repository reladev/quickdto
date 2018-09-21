package org.reladev.quickdto.processor;

import java.util.ArrayList;
import java.util.Collection;

public class CopyMapping {
    private String name;
    private Field getField;
    private Field setField;
    private ConverterMethod converterMethod;
    private boolean isCollectionConvert;
    private boolean isQuickDtoConvert;
    private boolean isQuickDtoListConvert;
    private String errorMessage;

    public CopyMapping(Field getField, Field setField, ConverterMap converterMap) {
        this.getField = getField;
        this.setField = setField;

        if (getField != null) {
            name = getField.getName();
        } else {
            name = setField.getName();
        }

        if (getField == null) {
            this.getField = new Field(setField);
            errorMessage = "No matching property for '" + setField.getType() + " " + name + "'. Add @ExcludeCopy to fix.";

        } else if (setField == null) {
            this.setField = new Field(getField);
            errorMessage = "No matching property for '" + getField.getType() + " " + name + "'. Add @ExcludeCopy to fix.";

        } else if (!getField.isPublic() && !getField.hasGetter()) {
            errorMessage = "Property '" + name + "' doesn't have a getter or isn't public. Add @ExcludeCopy to fix.";

        } else if (!setField.isPublic() && !setField.hasSetter()) {
            errorMessage = "Property '" + name + "' doesn't have a getter or isn't public. Either make public, add getter or @ExcludeCopy to fix.";

        } else {
            Type getType = getField.getType();
            Type setType = setField.getType();

            if (!getType.equals(setType)) {
                // try to find converter
                converterMethod = converterMap.get(getType, setType);
                if (converterMethod == null && getType.isCollection() && setType.isCollection()) {
                    converterMethod = converterMap.get(getType.getListType(), setType.getListType());
                    if (converterMethod != null) {
                        isCollectionConvert = true;
                    }
                }

                if (converterMethod == null) {
                    if (setField.getType().isQuickDto()) {
                        isQuickDtoConvert = true;

                    } else if (setField.getType().isQuickDtoList()) {
                        isQuickDtoListConvert = true;

                    } else {
                        errorMessage = "Type Mismatch(" + setType + ":" + getType + "). Either add Converter or @ExcludeCopy to fix.";
                    }
                }
            }
        }
    }

    public Collection<Type> getImports() {
        ArrayList<Type> imports = new ArrayList<>();
        if (getField != null) {
            imports.add(getField.getType());
        }
        if (setField != null) {
            imports.add(setField.getType());
        }
        return imports;
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

    public boolean isCollectionConvert() {
        return isCollectionConvert;
    }

    public boolean isQuickDtoConvert() {
        return isQuickDtoConvert;
    }

    public boolean isQuickDtoListConvert() {
        return isQuickDtoListConvert;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return name + "(" + getField.getType() + "->" + setField.getType() + ")";
    }
}
