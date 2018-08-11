package org.reladev.quickdto.processor;

public class Method implements Component {
	String body;
	boolean converter;
	boolean isStatic;
    boolean existingParam;
	String toType;
	String fromType;


	public String toString() {
		return body;
	}
}
