package org.reladev.quickdto.processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Collectors;

public class ImportsList implements Iterable<Type> {
    private HashMap<String, Type> imports = new HashMap<>();

    public void add(Type type) {
        if (!imports.containsKey(type.getName())) {
            imports.put(type.getName(), type);

        } else {
            Type importType = imports.get(type.getName());
            if (importType != null && importType.getQualifiedName().equals(type.getQualifiedName())) {
                imports.put(type.getName(), type);

            } else {
                imports.put(type.getName(), null);
            }
        }

        if (type.getGenericTypes() != null) {
            for (Type genericType: type.getGenericTypes()) {
                add(genericType);
            }
        }
    }

    public void addAll(Iterable<Type> types) {
        for (Type type: types) {
            add(type);
        }
    }

    public String getImportSafeType(Type type) {
        String typeString;
        Type importType = imports.get(type.getName());

        if (importType == null) {
            typeString = type.getQualifiedName();
        } else {
            typeString = type.getName();
        }

        if (type.getGenericTypes() != null) {
            String generics = type.getGenericTypes().stream().map(this::getImportSafeType).collect(Collectors.joining(","));
            typeString += "<" + generics + ">";
        }

        return typeString;
    }

    @Override
    public Iterator<Type> iterator() {
        return imports.values().stream().filter(Objects::nonNull).collect(Collectors.toList()).iterator();
    }
}
