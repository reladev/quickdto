package org.reladev.quickdto.processor;

import java.util.LinkedHashMap;

public class ClassDef {
    boolean makeDto = true;

    ///////////////////////////////////
    // Class definition
    ///////////////////////////////////
    String packageString;
    String name;
    String qualifiedName;

    LinkedHashMap<String, DtoField> fields = new LinkedHashMap<>();

}
