package org.reladev.quickdto.testharness;


import org.reladev.quickdto.shared.GwtIncompatible;
import org.reladev.quickdto.shared.QuickDto;

@QuickDto
public class BasicTypesDtoDef {
    byte myByte;
    @GwtIncompatible
    Byte objByte;
    short myShort;
    Short objShort;
    char myChar;
    Character objCharacter;
    int myInt;
	Integer objInteger;
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
