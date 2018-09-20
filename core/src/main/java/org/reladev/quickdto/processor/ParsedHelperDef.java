package org.reladev.quickdto.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.reladev.quickdto.feature.QuickDtoFeature;
import org.reladev.quickdto.shared.QuickDtoHelper;

import static org.reladev.quickdto.processor.AnnotationUtil.parseClassNameList;
import static org.reladev.quickdto.processor.QuickDtoProcessor.*;

public class ParsedHelperDef {
    // QuickDto Params
    private boolean fieldAnnotationsOnGetter = false;
    private List<String> targetClassNames = new ArrayList<>();
    private List<String> converterClassNames = new ArrayList<>();
    private List<String> featureClassNames = new ArrayList<>();

    // Definitions
    private ClassDef sourceDef;
    private LinkedList<String> typeAnnotationsToCopy = new LinkedList<>();
    private List<CopyMap> copyMaps = new ArrayList<>();
    private ConverterMap converterMap = new ConverterMap();
    private List<QuickDtoFeature> features = new ArrayList<>();
    private Set<Type> imports = new HashSet<>();

    public ParsedHelperDef(TypeElement element) {
        sourceDef = new ClassDef(element);
        parseQuickDtoParams(element);

        addClassAnnotations(element);
        createConverterMap();
        createCopyMaps(converterMap);
        createFeatures();
    }

    private void parseQuickDtoParams(TypeElement element) {
        final String annotationName = QuickDtoHelper.class.getName();
        element.getAnnotationMirrors();
        for (AnnotationMirror am: element.getAnnotationMirrors()) {
            AnnotationValue action;
            if (annotationName.equals(am.getAnnotationType().toString())) {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry: am.getElementValues().entrySet()) {
                    if ("fieldAnnotationsOnGetter".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        fieldAnnotationsOnGetter = (boolean) action.getValue();

                    } else if ("copyClass".equals(entry.getKey().getSimpleName().toString())) {
                        targetClassNames = parseClassNameList(entry.getValue());

                    } else if ("converter".equals(entry.getKey().getSimpleName().toString())) {
                        converterClassNames = parseClassNameList(entry.getValue());

                    } else if ("feature".equals(entry.getKey().getSimpleName().toString())) {
                        featureClassNames = parseClassNameList(entry.getValue());
                    }
                }
            }
        }
    }

    private void addClassAnnotations(TypeElement subelement) {
        List<? extends AnnotationMirror> annotationMirrors = subelement.getAnnotationMirrors();
        for (AnnotationMirror am: annotationMirrors) {
            if (!isQuickDtoAnntoation(am)) {
                typeAnnotationsToCopy.add(am.toString());
            }
        }
    }

    private void createCopyMaps(ConverterMap converterMap) {
        if (targetClassNames != null) {
            for (String className: targetClassNames) {
                TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(className);
                ClassDef targetDef = new ClassDef(typeElement);
                imports.add(targetDef.getType());
                CopyMap copyMap = new CopyMap(this.sourceDef, targetDef, converterMap);
                copyMaps.add(copyMap);
                imports.addAll(copyMap.getImports());
            }
        }
    }

    private void createConverterMap() {
        converterMap.addAll(sourceDef.getConverterMap());

        if (converterClassNames != null) {
            for (String className: converterClassNames) {
                TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(className);
                ClassDef classDef = new ClassDef(typeElement);
                converterMap.addAll(classDef.getConverterMap());
                imports.addAll(converterMap.getImports());
            }
        }
    }

    private void createFeatures() {
        for (String className: featureClassNames) {
            try {
                Class featureClass = Class.forName(className);
                QuickDtoFeature feature = (QuickDtoFeature) featureClass.newInstance();
                if (!features.contains(feature)) {
                    features.add(feature);
                }
            } catch (Exception e) {
                messager.printMessage(Kind.WARNING, "Can't create feature:" + className);
            }
        }
    }

    public ClassDef getSourceDef() {
        return sourceDef;
    }

    public boolean isFieldAnnotationsOnGetter() {
        return fieldAnnotationsOnGetter;
    }

    public LinkedList<String> getTypeAnnotationsToCopy() {
        return typeAnnotationsToCopy;
    }

    public List<CopyMap> getCopyMaps() {
        return copyMaps;
    }

    public List<QuickDtoFeature> getFeatures() {
        return features;
    }

    public Set<Type> getImports() {
        return imports;
    }

    public String toString() {
        return sourceDef.toString();
    }
}
