package org.reladev.quickdto.testharness;


import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.CollectionsImpl;

@QuickDto(sources = CollectionsImpl.class)
public class CollectionsDirtySourceDtoDef {
    int[] intArray;
    float[] floatArray;
    boolean[] booleanArray;
    String[] stringArray;

    List<Integer> integerList;
    List<Float> floatList;
    List<Boolean> booleanList;
    List<String> stringList;

    Set<Integer> integerSet;
    Set<Float> floatSet;
    Set<Boolean> booleanSet;
    Set<String> stringSet;

    Map<Integer, Integer> integerMap;
    Map<Float, Float> floatMap;
    Map<Boolean, Boolean> booleanMap;
    Map<String, String> stringMap;
}
