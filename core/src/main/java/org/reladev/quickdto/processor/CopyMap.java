package org.reladev.quickdto.processor;

import java.util.HashMap;

public class CopyMap {
    public AccessorDef sourceDef;
    public AccessorDef transferDef;

    public HashMap<String, MappedAccessor> mappedGetters = new HashMap<>();
    public HashMap<String, MappedAccessor> mappedSetters = new HashMap<>();
}
