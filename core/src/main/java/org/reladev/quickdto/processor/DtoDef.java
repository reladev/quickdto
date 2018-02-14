package org.reladev.quickdto.processor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.reladev.quickdto.shared.QuickDto;

public class DtoDef extends ClassDef {
    boolean makeDto = true;

    LinkedList<String> annotations = new LinkedList<>();
    String extend;
    LinkedList<String> implementList = new LinkedList<>();

    boolean fieldAnnotationsOnGetter = false;

    boolean strictCopy = true;
    LinkedList<Source> sources = new LinkedList<>();
    private HashMap<String, List<Method>> converters = new HashMap<>();

    /**
     * List of methods in the DtoDef that should be copied to the DTO.
     *
     * @see QuickDto#copyMethods()
     */
    LinkedList<Method> methods = new LinkedList<>();


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
