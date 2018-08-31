package org.reladev.quickdto.processor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class ConverterMap {
    private HashMap<Type, Set<ConverterMethod>> toTypeConverterMap = new HashMap<>();

    public void add(ConverterMethod converter) {
        Set<ConverterMethod> converterList = getConverterList(converter.getToType());
        converterList.add(converter);
    }

    public ConverterMethod get(Type from, Type to) {
        Set<ConverterMethod> converterList = toTypeConverterMap.get(to);
        if (converterList != null) {
            for (ConverterMethod converter: converterList) {
                if (converter.getFromType()
                             .equals(from)) {
                    return converter;
                }
            }
        }
        return null;
    }

    public void addAll(ConverterMap map) {
        for (Entry<Type, Set<ConverterMethod>> entry: map.toTypeConverterMap.entrySet()) {
            Set<ConverterMethod> converterList = getConverterList(entry.getKey());
            converterList.addAll(entry.getValue());
        }
    }

    private Set<ConverterMethod> getConverterList(Type toType) {
        return toTypeConverterMap.computeIfAbsent(toType, key -> new HashSet<>());
    }

    public HashMap<Type, Set<ConverterMethod>> getMap() {
        return toTypeConverterMap;
    }

    @Override
    public String toString() {
        return "ConverterMap{" + "size=" + toTypeConverterMap.size() + '}';
    }
}
