package com.github.quickdto.processor;

import java.util.LinkedList;
import java.util.List;

public class Field implements Component {
	String type;
	String fieldName;
	String enumName;
	String accessorName;
	boolean primitive;
	boolean copyFrom;
	boolean copyTo;
	boolean excludeSetter;
	boolean excludeGetter;
	boolean equalsHashCode;
	boolean sourceMapped;
	boolean strictCopy;
	List<String> fieldAnnotations = new LinkedList<>();
	List<String> setterAnnotations = new LinkedList<>();
	List<String> getterAnnotations = new LinkedList<>();


	public String toString() {
		return type + " " + fieldName;
	}
}
