package org.reladev.quickdto.processor;

import java.util.LinkedHashMap;

public class ClassDef {
    ///////////////////////////////////
    // Class definition
    ///////////////////////////////////
    public String packageString;
    public String name;
    public String qualifiedName;

    public LinkedHashMap<String, DtoField> fields = new LinkedHashMap<>();

}
