package org.reladev.quickdto.processor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic.Kind;

import org.reladev.quickdto.feature.QuickDtoFeature;
import org.reladev.quickdto.shared.QuickDto;

import static org.reladev.quickdto.processor.QuickDtoProcessor.isQuickDtoAnntoation;
import static org.reladev.quickdto.processor.QuickDtoProcessor.processingEnv;

public class ParsedDtoDef {
    // QuickDto Params
    private boolean fieldAnnotationsOnGetter = false;
    private String extendClassName;
    private List<String> implementClassNames = new ArrayList<>();
    private List<String> sourceClassNames = new ArrayList<>();
    private List<String> converterClassNames = new ArrayList<>();
    private List<String> featureClassNames = new ArrayList<>();

    // Definitions
    private ClassDef2 targetDef;
    private LinkedList<String> typeAnnotationsToCopy = new LinkedList<>();
    private Type extendType;
    private ArrayList<Type> implementTypes = new ArrayList<>();
    public List<CopyMap2> copyMaps = new ArrayList<>();
    public ConverterMap converterMap = new ConverterMap();
    private List<QuickDtoFeature> features = new ArrayList<>();

    public ParsedDtoDef(TypeElement element) {
        targetDef = new ClassDef2(element);
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
        DeclaredType superClassType = (DeclaredType) element.getSuperclass();
        if (superClassType != null) {
            Type superType = new Type(superClassType);
            if (superType.isQuickDto()) {
                extendType = superType;
            }

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
                ClassDef2 sourceDef = new ClassDef2(typeElement);
                CopyMap2 copyMap = new CopyMap2(sourceDef, targetDef, converterMap);
                copyMaps.add(copyMap);
            }
        }
    }

    private void createConverterMap() {
        converterMap.addAll(targetDef.getConverterMap());

        if (converterClassNames != null) {
            for (String className : converterClassNames) {
                TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(className);
                ClassDef2 classDef = new ClassDef2(typeElement);
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
                processingEnv.getMessager().printMessage(Kind.WARNING, "Can't create feature:" + className);
            }
        }
    }

}
