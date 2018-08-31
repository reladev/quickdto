package org.reladev.quickdto.processor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class ConverterMap {
    private HashMap<Type, Set<ConverterMethod2>> toTypeConverterMap = new HashMap<>();

    public void add(ConverterMethod2 converter) {
        Set<ConverterMethod2> converterList = getConverterList(converter.getToType());
        converterList.add(converter);
    }

    public ConverterMethod2 get(Type from, Type to) {
        Set<ConverterMethod2> converterList = toTypeConverterMap.get(to);
        if (converterList != null) {
            for (ConverterMethod2 converter: converterList) {
                if (converter.getFromType()
                             .equals(from)) {
                    return converter;
                }
            }
        }
        return null;
    }

    public void addAll(ConverterMap map) {
        for (Entry<Type, Set<ConverterMethod2>> entry: map.toTypeConverterMap.entrySet()) {
            Set<ConverterMethod2> converterList = getConverterList(entry.getKey());
            converterList.addAll(entry.getValue());
        }
    }

    private Set<ConverterMethod2> getConverterList(Type toType) {
        return toTypeConverterMap.computeIfAbsent(toType, key -> new HashSet<>());
    }

    public HashMap<Type, Set<ConverterMethod2>> getMap() {
        return toTypeConverterMap;
    }

    @Override
    public String toString() {
        return "ConverterMap{" + "size=" + toTypeConverterMap.size() + '}';
    }
}
