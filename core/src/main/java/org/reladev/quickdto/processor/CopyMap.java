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
        imports.add(targetDef.getType());
        this.sourceDef = sourceDef;
        imports.add(sourceDef.getType());

        for (Field targetField: targetDef.getNameFieldMap().values()) {
            String name = targetField.getName();
            Field sourceField = sourceDef.getNameFieldMap().get(name);

            if (targetField.isCopySettable(sourceDef) && (sourceField == null || sourceField.isCopyGettable(targetDef))) {
                CopyMapping sourceToTarget = new CopyMapping(sourceField, targetField, converterMap);
                sourceToTargetMappings.put(sourceToTarget.getName(), sourceToTarget);
                imports.addAll(sourceToTarget.getImports());
            }

            if (targetField.isCopyGettable(sourceDef) && (sourceField == null || sourceField.isCopySettable(targetDef))) {
                CopyMapping targetToSource = new CopyMapping(targetField, sourceField, converterMap);
                targetToSourceMappings.put(targetToSource.getName(), targetToSource);
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
