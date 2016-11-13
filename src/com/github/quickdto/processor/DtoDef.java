package com.github.quickdto.processor;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class DtoDef {
	String packageString;
	String name;
	String qualifiedName;
	String extend;
	LinkedList<Source> sources = new LinkedList<>();
	LinkedList<String> implementList = new LinkedList<>();
	LinkedHashMap<String, Field> fields = new LinkedHashMap<String, Field>();
	LinkedList<Method> methods = new LinkedList<>();
}
