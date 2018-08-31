package org.reladev.quickdto.processor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CopyMap {
    private ClassDef sourceDef;
    private ClassDef targetDef;

    private HashMap<String, CopyMapping> sourceToTargetMappings = new HashMap<>();
    private HashMap<String, CopyMapping> targetToSourceMappings = new HashMap<>();
    private Set<Type> imports = new HashSet<>();

    public CopyMap(ClassDef sourceDef, ClassDef targetDef, ConverterMap converterMap) {
        this.targetDef = targetDef;
        this.sourceDef = sourceDef;

        for (Field sourceField: sourceDef.getNameFieldMap().values()) {
            Field targetField = targetDef.getNameFieldMap().get(sourceField.getName());
            CopyMapping sourceToTarget = CopyMapping.build(sourceField, targetField, converterMap);
            if (sourceToTarget != null) {
                sourceToTargetMappings.put(sourceField.getName(), sourceToTarget);
                imports.addAll(sourceToTarget.getImports());
            }
            CopyMapping targetToSource = CopyMapping.build(targetField, sourceField, converterMap);
            if (targetToSource != null) {
                targetToSourceMappings.put(sourceField.getName(), targetToSource);
                imports.addAll(targetToSource.getImports());
            }
        }
    }

    public Set<Type> getImports() {
        return imports;
    }

    public ClassDef getSourceDef() {
        return sourceDef;
    }

    public ClassDef getTargetDef() {
        return targetDef;
    }

    public HashMap<String, CopyMapping> getSourceToTargetMappings() {
        return sourceToTargetMappings;
    }

    public HashMap<String, CopyMapping> getTargetToSourceMappings() {
        return targetToSourceMappings;
    }
}
