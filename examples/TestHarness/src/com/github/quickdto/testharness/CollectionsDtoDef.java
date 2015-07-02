package com.github.quickdto.testharness;


import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.quickdto.shared.QuickDto;
import com.github.quickdto.testharness.impl.TestImpl;

@QuickDto(source = TestImpl.class)
public class CollectionsDtoDef {
    int[] intArray;
    float[] floatArray;
    boolean[] booleanArray;
    String[] stringArray;
    BasicTypesDtoDef[] objArray;

    List<String> stringList;
    Set<String> stringSet;

    List<BasicTypesDtoDef> objList;
    Set<BasicTypesDtoDef> objSet;

    Map<Integer, Integer> integerMap;
    Map<Long, Long> longMap;
    Map<Float, Float> floatMap;
    Map<Double, Double> doubleMap;
    Map<Boolean, Boolean> booleanMap;
    Map<String, String> stringMap;
    Map<BasicTypesDtoDef, BasicTypesDtoDef> objMap;

}
