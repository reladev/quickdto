package org.reladev.quickdto.processor;

import java.util.HashMap;

public class SourceDef {
    public String type;

    /**
     * Maps accessorName of the Field/Accessor to the MappedAccessor for use creating
     * copy methods;
     */
    public HashMap<String, Field> getters = new HashMap<>();
    public HashMap<String, Field> setters = new HashMap<>();

    @Override
    public String toString() {
        return type;
    }
}
