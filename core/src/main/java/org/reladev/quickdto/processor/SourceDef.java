package org.reladev.quickdto.processor;

import java.util.HashMap;

public class SourceDef {
    public String type;

    /**
     * Maps accessorName of the Field/Accessor to the MappedAccessor for use creating
     * copy methods;
     */
    public HashMap<String, AccessorMethod> getters = new HashMap<>();
    public HashMap<String, AccessorMethod> setters = new HashMap<>();
}
