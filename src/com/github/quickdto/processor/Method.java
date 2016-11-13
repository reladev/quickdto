package com.github.quickdto.processor;

public class Method implements Component {
	String body;
	boolean converter;
	String outType;
	String inType;


	public String toString() {
		return body;
	}
}
