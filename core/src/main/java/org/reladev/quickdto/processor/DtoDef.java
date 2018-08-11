package org.reladev.quickdto.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.reladev.quickdto.feature.QuickDtoFeature;
import org.reladev.quickdto.shared.QuickDto;

public class DtoDef extends ClassDef {
    public boolean makeDto = true;
    public List<QuickDtoFeature> features = new ArrayList<>();

    public LinkedList<String> annotations = new LinkedList<>();
    public String extend;
    public LinkedList<String> implementList = new LinkedList<>();

    public boolean fieldAnnotationsOnGetter = false;

    public boolean strictCopy = true;
    public LinkedList<Source> sources = new LinkedList<>();
    public HashMap<String, List<Method>> converters = new HashMap<>();

    public DtoDef() {
        //features.add(new DirtyFeature());
    }

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
