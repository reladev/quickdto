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
import org.reladev.quickdto.shared.QuickDto;

import static org.reladev.quickdto.processor.QuickDtoProcessor.*;

public class ParsedDtoDef {
    // QuickDto Params
    private boolean fieldAnnotationsOnGetter = false;
    private String extendClassName;
    private List<String> implementClassNames = new ArrayList<>();
    private List<String> sourceClassNames = new ArrayList<>();
    private List<String> converterClassNames = new ArrayList<>();
    private List<String> featureClassNames = new ArrayList<>();

    // Definitions
    private ClassDef targetDef;
    private LinkedList<String> typeAnnotationsToCopy = new LinkedList<>();
    private Type extendType;
    private ArrayList<Type> implementTypes = new ArrayList<>();
    private List<CopyMap> copyMaps = new ArrayList<>();
    private ConverterMap converterMap = new ConverterMap();
    private List<QuickDtoFeature> features = new ArrayList<>();
    private Set<Type> imports = new HashSet<>();

    public ParsedDtoDef(TypeElement element) {
        messager.printMessage(Kind.NOTE, "QuickDto:" + element);

        targetDef = new ClassDef(element);
        imports.addAll(targetDef.getImports());
        parseQuickDtoParams(element);

        addClassAnnotations(element);
        createExtendType(element);
        createImplementTypes();
        createConverterMap();
        createCopyMaps(converterMap);
        createFeatures();
    }

    private void parseQuickDtoParams(TypeElement element) {
        final String annotationName = QuickDto.class.getName();
        element.getAnnotationMirrors();
        for (AnnotationMirror am : element.getAnnotationMirrors()) {
            AnnotationValue action;
            if (annotationName.equals(am.getAnnotationType().toString())) {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet()) {
                    if ("fieldAnnotationsOnGetter".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        fieldAnnotationsOnGetter = (boolean) action.getValue();

                    } else if ("extend".equals(entry.getKey().getSimpleName().toString())) {
                        extendClassName = parseClassName(entry.getValue());

                    } else if ("implement".equals(entry.getKey().getSimpleName().toString())) {
                        implementClassNames = parseClassNameList(entry.getValue());

                    } else if ("source".equals(entry.getKey().getSimpleName().toString())) {
                        sourceClassNames = parseClassNameList(entry.getValue());

                    } else if ("converter".equals(entry.getKey().getSimpleName().toString())) {
                        converterClassNames = parseClassNameList(entry.getValue());

                    } else if ("feature".equals(entry.getKey().getSimpleName().toString())) {
                        featureClassNames = parseClassNameList(entry.getValue());
                    }
                }
            }
        }
    }

    private String parseClassName(AnnotationValue action) {
        Object param = action.getValue();
        return annotationParamToClassName(param);
    }

    private List<String> parseClassNameList(AnnotationValue action) {
        ArrayList<String> classNames = new ArrayList<>();
        List params = (List) action.getValue();
        for (Object param : params) {
            String className = annotationParamToClassName(param);
            classNames.add(className);
        }
        return classNames;
    }

    private String annotationParamToClassName(Object source) {
        String className = source.toString();
        className = className.substring(0, className.length() - 6);
        return className;
    }

    private void addClassAnnotations(TypeElement subelement) {
        List<? extends AnnotationMirror> annotationMirrors = subelement.getAnnotationMirrors();
        for (AnnotationMirror am : annotationMirrors) {
            if (!isQuickDtoAnntoation(am)) {
                typeAnnotationsToCopy.add(am.toString());
            }
        }
    }

    private void createExtendType(TypeElement element) {
        if (targetDef.getSuperClassDef() != null) {
            extendType = targetDef.getSuperClassDef().getType();

        } else if (extendClassName != null && !extendClassName.equals(Object.class.getCanonicalName())) {
            TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(extendClassName);
            extendType = new Type(typeElement.asType());
        }
    }

    private void createImplementTypes() {
        if (implementClassNames != null) {
            for (String className : implementClassNames) {
                TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(className);
                implementTypes.add(new Type(typeElement.asType()));
            }
        }
    }

    private void createCopyMaps(ConverterMap converterMap) {
        if (sourceClassNames != null) {
            for (String className : sourceClassNames) {
                TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(className);
                ClassDef sourceDef = new ClassDef(typeElement);
                imports.add(sourceDef.getType());
                CopyMap copyMap = new CopyMap(sourceDef, targetDef, converterMap);
                copyMaps.add(copyMap);
                imports.addAll(copyMap.getImports());
            }
        }
    }

    private void createConverterMap() {
        converterMap.addAll(targetDef.getConverterMap());

        if (converterClassNames != null) {
            for (String className : converterClassNames) {
                TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(className);
                ClassDef classDef = new ClassDef(typeElement);
                converterMap.addAll(classDef.getConverterMap());
            }
        }
    }

    private void createFeatures() {
        for (String className : featureClassNames) {
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

    public ClassDef getTargetDef() {
        return targetDef;
    }

    public boolean isFieldAnnotationsOnGetter() {
        return fieldAnnotationsOnGetter;
    }

    public LinkedList<String> getTypeAnnotationsToCopy() {
        return typeAnnotationsToCopy;
    }

    public Type getExtendType() {
        return extendType;
    }

    public ArrayList<Type> getImplementTypes() {
        return implementTypes;
    }

    public List<CopyMap> getCopyMaps() {
        return copyMaps;
    }

    public ConverterMap getConverterMap() {
        return converterMap;
    }

    public List<QuickDtoFeature> getFeatures() {
        return features;
    }

    public Set<Type> getImports() {
        return imports;
    }

    public String toString() {
        return targetDef.toString();
    }
}
