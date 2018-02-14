package org.reladev.quickdto.processor;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class DtoField extends Field implements Component {
    private String enumName;
    private String accessorName;
    private String primitiveTypeString;

	private boolean copyFrom;
	private boolean copyTo;
	private boolean excludeSetter;
	private boolean excludeGetter;
	private boolean equalsHashCode;
	private boolean sourceMapped;
	private boolean strictCopy;
	private List<String> fieldAnnotations = new LinkedList<>();
	private List<String> setterAnnotations = new LinkedList<>();
	private List<String> getterAnnotations = new LinkedList<>();

	public String getEnumName() {
        if (enumName == null) {
            enumName = getFieldName().replaceAll("(.)([A-Z])", "$1_$2").toUpperCase();
        }
        return enumName;
	}

	public String getAccessorName() {
        if (accessorName == null) {
            char firstChar = getFieldName().charAt(0);
            firstChar = Character.toUpperCase(firstChar);
            accessorName = firstChar + getFieldName().substring(1);
        }
        return accessorName;
	}

    public String getGetAccessorName() {
        if ("boolean".equals(getTypeString()) || "java.lang.Boolean".equals(getTypeString())) {
            return "is" + getAccessorName();
        } else {
            return "get" + getAccessorName();
        }
    }

    public String getPrimitiveTypeString() {
        if (primitiveTypeString == null) {
            String typeString = getTypeString();
            if ("int".equals(typeString)) {
                primitiveTypeString = "Integer";
            } else if ("boolean".equals(typeString)) {
                primitiveTypeString = "Boolean";
            } else if ("long".equals(typeString)) {
                primitiveTypeString = "Long";
            } else if ("double".equals(typeString)) {
                primitiveTypeString = "Double";
            } else if ("float".equals(typeString)) {
                primitiveTypeString = "Float";
            } else if ("char".equals(typeString)) {
                primitiveTypeString = "Character";
            } else if ("byte".equals(typeString)) {
                primitiveTypeString = "Byte";
            } else if ("short".equals(typeString)) {
                primitiveTypeString = "Short";
            }
        }
        return primitiveTypeString;
    }

    public boolean isCopyFrom() {
        return copyFrom;
	}

	public void setCopyFrom() {
		this.copyFrom = true;
	}

	public boolean isCopyTo() {
		return copyTo;
	}

	public void setCopyTo() {
		this.copyTo = true;
	}

	public boolean isExcludeSetter() {
		return excludeSetter;
	}

	public void setExcludeSetter() {
		this.excludeSetter = true;
	}

	public boolean isExcludeGetter() {
		return excludeGetter;
	}

	public void setExcludeGetter() {
		this.excludeGetter = true;
	}

	public boolean isEqualsHashCode() {
		return equalsHashCode;
	}

	public void setEqualsHashCode() {
		this.equalsHashCode = true;
	}

	public boolean isSourceMapped() {
		return sourceMapped;
	}

	public void setSourceMapped() {
		this.sourceMapped = true;
	}

	public boolean isStrictCopy() {
		return strictCopy;
	}

	public void setStrictCopy(boolean strictCopy) {
		this.strictCopy = strictCopy;
	}

	public List<String> getFieldAnnotations() {
		return fieldAnnotations;
	}

	public void addFieldAnnotation(String fieldAnnotation) {
		this.fieldAnnotations.add(fieldAnnotation);
	}

	public List<String> getSetterAnnotations() {
		return setterAnnotations;
	}

	public void addSetterAnnotation(String setterAnnotation) {
		this.setterAnnotations.add(setterAnnotation);
	}

	public List<String> getGetterAnnotations() {
		return getterAnnotations;
	}

	public void addGetterAnnotation(String getterAnnotation) {
		this.getterAnnotations.add(getterAnnotation);
	}

	public String getDefaultValue() {
        if ("boolean".equals(getTypeString())) {
            return "false";
        } else {
            return "0";
        }
    }
}
