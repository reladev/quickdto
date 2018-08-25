package org.reladev.quickdto.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class ConverterMap {
    private HashMap<Type, List<ConverterMethod2>> toTypeConverterMap = new HashMap<>();

    public void add(ConverterMethod2 converter) {
        List<ConverterMethod2> converterList = getConverterList(converter.getToType());
        converterList.add(converter);
    }

    public ConverterMethod2 get(Type from, Type to) {
        List<ConverterMethod2> converterList = getConverterList(to);
        for (ConverterMethod2 converter : converterList) {
            if (converter.getFromType().equals(from)) {
                return converter;
            }
        }
        return null;
    }

    public void addAll(ConverterMap map) {
        for (Entry<Type, List<ConverterMethod2>> entry : map.toTypeConverterMap.entrySet()) {
            List<ConverterMethod2> converterList = getConverterList(entry.getKey());
            converterList.addAll(entry.getValue());
        }
    }

    private List<ConverterMethod2> getConverterList(Type toType) {
        return toTypeConverterMap.computeIfAbsent(toType, key -> new ArrayList<>());
    }
}
