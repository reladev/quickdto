package com.github.quickdto.testharness.impl;

public class Simple {
	private int normal;
	private int readOnly;
	private int readOnlyWithSetter;

	public int getNormal() {
		return normal;
	}

	public void setNormal(int normal) {
		this.normal = normal;
	}

	public int getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(int readOnly) {
		this.readOnly = readOnly;
	}

	public int getReadOnlyWithSetter() {
		return readOnlyWithSetter;
	}

	public void setReadOnlyWithSetter(int readOnlyWithSetter) {
		this.readOnlyWithSetter = readOnlyWithSetter;
	}
}
