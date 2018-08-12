package org.reladev.quickdto.processor;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class DtoField extends Field implements Component {
    private List<String> fieldAnnotations = new LinkedList<>();
	private List<String> setterAnnotations = new LinkedList<>();
	private List<String> getterAnnotations = new LinkedList<>();

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
