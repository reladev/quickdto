package com.github.quickdto.processor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.lang.model.type.TypeMirror;

public class Source {
	String type;
	HashMap<String, Method> getters = new HashMap<>();
	HashMap<String, Method> setters = new HashMap<>();
}
