package org.reladev.quickdto.processor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class DtoDef {
	String packageString;
	String name;
	String qualifiedName;
	String extend;
	boolean strictCopy = true;
	boolean fieldAnnotationsOnGetter = false;
	LinkedList<String> annotations = new LinkedList<>();
	LinkedList<Source> sources = new LinkedList<>();
	LinkedList<String> implementList = new LinkedList<>();
	LinkedHashMap<String, Field> fields = new LinkedHashMap<>();
	LinkedList<Method> methods = new LinkedList<>();

	private HashMap<String, List<Method>> converters = new HashMap<>();

	public Method getConverter(String toType, String fromType) {
		List<Method> methods = converters.get(toType);
		if (methods == null) {
			String simpleToType = toType.substring(toType.lastIndexOf(".") + 1);
			methods = converters.get(simpleToType);
		}
		if (methods != null) {
			for (Method method: methods) {
				if (method.fromType.equals(fromType)) {
					return method;
				} else {
					String simpleFromType = fromType.substring(fromType.lastIndexOf(".") + 1);
					if (method.fromType.equals(simpleFromType)) {
						return method;
					}
				}
			}
		}
		return null;
	}

	public void addConverter(Method method) {
		List<Method> methods = converters.get(method.toType);
		if (methods == null) {
			methods = new LinkedList<>();
			converters.put(method.toType, methods);
		}
		methods.add(method);
	}
}
