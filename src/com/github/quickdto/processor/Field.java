package com.github.quickdto.processor;

import java.util.LinkedList;
import java.util.List;

public class Field implements Component {
	String type;
	String fieldName;
	String enumName;
	String accessorName;
	String importString;
	String body;
	boolean primitive;
	boolean readOnly;
	boolean excludeSetter;
	boolean ignoreDirty;
	boolean equalsHashCode;
	Class[] jsonExclude;
	Class[] jsonInclude;
	List<String> fieldAnnotations = new LinkedList<>();
	List<String> setterAnnotations = new LinkedList<>();
	List<String> getterAnnotations = new LinkedList<>();


	public String toString() {
		return type + " " + fieldName;
	}
}
