package org.reladev.quickdto.processor;

import java.util.HashMap;

public class CopyMap2 {
    private ClassDef2 sourceDef;
    private ClassDef2 targetDef;

    private HashMap<String, CopyMapping> sourceToTargetMappings = new HashMap<>();
    private HashMap<String, CopyMapping> targetToSourceMappings = new HashMap<>();

    public CopyMap2(ClassDef2 sourceDef, ClassDef2 targetDef, ConverterMap converterMap) {
        this.targetDef = targetDef;
        this.sourceDef = sourceDef;

        for (Field2 sourceField : sourceDef.getNameFieldMap().values()) {
            Field2 targetField = targetDef.getNameFieldMap().get(sourceField.getName());
            CopyMapping sourceToTarget = CopyMapping.build(sourceField, targetField, converterMap);
            if (sourceToTarget != null) {
                sourceToTargetMappings.put(sourceField.getName(), sourceToTarget);
            }
            CopyMapping targetToSource = CopyMapping.build(targetField, sourceField, converterMap);
            if (targetToSource != null) {
                targetToSourceMappings.put(sourceField.getName(), targetToSource);
            }
        }
    }
}
