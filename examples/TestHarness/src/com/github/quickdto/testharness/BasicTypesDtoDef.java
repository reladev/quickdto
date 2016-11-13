package com.github.quickdto.testharness;


import com.github.quickdto.shared.QuickDto;
import com.github.quickdto.testharness.impl.TestImpl;

@QuickDto(source = {TestImpl.class, CollectionsDtoDef.class}, strictCopy = false)
public class BasicTypesDtoDef {
    byte myByte;
    Byte objByte;
    short myShort;
    Short objShort;
    char myChar;
    Character objCharacter;
    int myInt;
    Integer oInteger;
    long myLong;
    Long objLong;
    float myFloat;
    Float objFloat;
    double myDouble;
    Double objDouble;
    boolean myBoolean;
    Boolean objBoolean;
    String myString;
}
