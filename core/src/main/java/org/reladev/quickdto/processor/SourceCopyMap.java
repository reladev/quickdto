package org.reladev.quickdto.processor;

import java.util.HashMap;

public class SourceCopyMap {
    public SourceDef sourceDef;

    public HashMap<String, MappedAccessor> mappedGetters = new HashMap<>();
    public HashMap<String, MappedAccessor> mappedSetters = new HashMap<>();
}
