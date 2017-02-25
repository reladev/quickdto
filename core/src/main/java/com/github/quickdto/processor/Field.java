package com.github.quickdto.processor;

import java.util.LinkedList;
import java.util.List;

import javax.lang.model.type.TypeMirror;

@SuppressWarnings("WeakerAccess")
public class Field implements Component {
	private TypeMirror type;
	private String typeString;
	private String fieldName;
	private String enumName;
	private String accessorName;
	private boolean primitive;
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

	public String getTypeString() {
		return typeString;
	}

	public TypeMirror getType() {
		return type;
	}

	public void setType(TypeMirror type) {
		this.type = type;
		this.typeString = type.toString();
		if (typeString.endsWith("DtoDef")) {
			typeString = typeString.substring(0, typeString.length() - 3);
		}
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
		createNames();
	}

	private void createNames() {
		char firstChar = fieldName.charAt(0);
		if (Character.isUpperCase(firstChar)) {
			accessorName = fieldName;
			firstChar = Character.toLowerCase(firstChar);
			fieldName = firstChar + fieldName.substring(1);

		} else {
			firstChar = Character.toUpperCase(firstChar);
			accessorName = firstChar + fieldName.substring(1);
		}
		enumName = fieldName.replaceAll("(.)([A-Z])", "$1_$2").toUpperCase();
	}

	public String getEnumName() {
		return enumName;
	}

	public String getAccessorName() {
		return accessorName;
	}

	public boolean isPrimitive() {
		return primitive;
	}

	public void setPrimitive() {
		this.primitive = true;
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

	public String toString() {
		return type + " " + fieldName;
	}
}
