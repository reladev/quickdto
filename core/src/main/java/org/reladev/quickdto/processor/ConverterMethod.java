package org.reladev.quickdto.processor;

public class ConverterMethod implements MethodType {
	String body;
    String classTypeString;
	boolean converter;
	boolean isStatic;
    boolean existingParam;
	String toType;
	String fromType;

	public String toString() {
        return toType;
	}
}
