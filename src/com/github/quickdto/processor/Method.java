package com.github.quickdto.processor;

public class Method implements Component {
	String body;
	boolean converter;
	boolean isStatic;
	String outType;
	String inType;


	public String toString() {
		return body;
	}
}
