package org.reladev.quickdto.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;

import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Type.ClassType;
import org.reladev.quickdto.feature.QuickDtoFeature;
import org.reladev.quickdto.shared.CopyFromOnly;
import org.reladev.quickdto.shared.CopyToOnly;
import org.reladev.quickdto.shared.EqualsHashCode;
import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.shared.StrictCopy;

public class ClassAnalyzer {
    private ProcessingEnvironment processingEnv;
    private Trees trees;

    public ClassAnalyzer(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    public DtoDef processDtoDef(ClassSymbol defElement) {
        processingEnv.getMessager().printMessage(Kind.NOTE, "QuickDto: " + defElement.getSimpleName());
        DtoDef dtoDef = new DtoDef();
        PackageElement packageElement = (PackageElement) defElement.getEnclosingElement();
        dtoDef.packageString = packageElement.getQualifiedName().toString();
        dtoDef.name = defElement.getSimpleName().toString();
        dtoDef.name = dtoDef.name.substring(0, dtoDef.name.length() - 3);
        dtoDef.qualifiedName = dtoDef.packageString + "." + dtoDef.name;

        ClassType superClassType = (ClassType) defElement.getSuperclass();
        if (superClassType != null) {
            TypeElement superElement = processingEnv.getElementUtils().getTypeElement(superClassType.toString());
            if (superElement.getAnnotation(QuickDto.class) != null) {
                dtoDef.dtoExtend = superClassType.toString();
                dtoDef.dtoExtend = dtoDef.dtoExtend.substring(0, dtoDef.dtoExtend.length() - 3);
            }
        }

        addClassAnnotations(defElement, dtoDef);
        addFieldMethods(defElement, dtoDef);
        addAnnotationParams(defElement, dtoDef);

        return dtoDef;
    }

    private void addClassAnnotations(Element subelement, DtoDef dtoDef) {
        List<? extends AnnotationMirror> annotationMirrors = subelement.getAnnotationMirrors();
        for (AnnotationMirror am : annotationMirrors) {
            if (!isQuickDtoAnntoation(am)) {
                dtoDef.annotations.add(am.toString());
            }
        }
    }


    private boolean isQuickDtoAnntoation(AnnotationMirror an) {
        return an.toString().startsWith("@org.reladev.quickdto");
    }

    private void addFieldMethods(Element defElement, DtoDef dtoDef) {
        TypeVisitor<Component, Element> getType = new FieldVisitor(processingEnv, trees);

        TypeElement sourceType = (TypeElement) defElement;
        while (sourceType != null) {
            for (Element subelement : sourceType.getEnclosedElements()) {
                if (subelement.getKind() != ElementKind.CONSTRUCTOR) {
                    TypeMirror mirror = subelement.asType();
                    Component component = mirror.accept(getType, subelement);
                    if (component instanceof DtoField) {
                        addField(subelement, (DtoField) component, dtoDef);

                    } else if (component instanceof Method) {
                        Method method = (Method) component;
                        dtoDef.methods.add(method);
                        if (method.converter) {
                            dtoDef.addConverter(method);
                        }
                    }
                }
            }
            if (sourceType.getSuperclass() instanceof NoType) {
                sourceType = null;
            } else {
                sourceType = (TypeElement) ((DeclaredType) sourceType.getSuperclass()).asElement();
                if ("java.lang.Object".equals(sourceType.toString())) {
                    sourceType = null;
                }
            }
        }
    }

    private void addField(Element subelement, DtoField field, DtoDef dtoDef) {
        if (subelement.getAnnotation(EqualsHashCode.class) != null) {
            field.setEqualsHashCode();
        }
        CopyFromOnly copyFromOnly = subelement.getAnnotation(CopyFromOnly.class);
        if (copyFromOnly != null) {
            field.setCopyFrom();
            if (!copyFromOnly.setter()) {
                field.setExcludeSetter();
            }
        }
        CopyToOnly copyToOnly = subelement.getAnnotation(CopyToOnly.class);
        if (copyToOnly != null) {
            field.setCopyTo();
            if (!copyToOnly.getter()) {
                field.setExcludeGetter();
            }
        }
        StrictCopy strictCopy = subelement.getAnnotation(StrictCopy.class);
        if (strictCopy != null) {
            field.setStrictCopy(strictCopy.value());

        } else {
            field.setStrictCopy(dtoDef.strictCopy);
        }

        addAnnotations(subelement, field);

        DtoField existing = dtoDef.fields.get(field.getAccessorName());
        if (existing == null) {
            dtoDef.fields.put(field.getAccessorName(), field);
        } else {
            processingEnv.getMessager().printMessage(Kind.WARNING, "Skipping duplicated field definition:" + field.getAccessorName());
        }
    }

    private void addAnnotationParams(Element element, DtoDef dtoDef) {
        final String annotationName = QuickDto.class.getName();
        element.getAnnotationMirrors();
        for (AnnotationMirror am : element.getAnnotationMirrors()) {
            AnnotationValue action;
            if (annotationName.equals(am.getAnnotationType().toString())) {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet()) {
                    if ("source".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        List sources = (List) action.getValue();
                        for (Object source : sources) {
                            String className = annotationParamToClassName(source);
                            //todo this is in the wrong location
                            addSource(dtoDef, className);
                            SourceDef sourceDef = createSourceDef(className);
                            SourceCopyMap sourceCopyMap = createSourceCopyMap(dtoDef, sourceDef);
                            dtoDef.sourceMaps.add(sourceCopyMap);
                        }

                    } else if ("strictCopy".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        dtoDef.strictCopy = (boolean) action.getValue();

                    } else if ("fieldAnnotationsOnGetter".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        dtoDef.fieldAnnotationsOnGetter = (boolean) action.getValue();

                    } else if ("implement".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        List implementList = (List) action.getValue();
                        for (Object implement : implementList) {
                            String className = annotationParamToClassName(implement);
                            dtoDef.implementList.add(className);
                        }

                    } else if ("extend".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        Object extend = action.getValue();
                        String className = extend.toString();
                        if (!className.equals(Object.class.getCanonicalName())) {
                            dtoDef.extend = className;
                        }

                    } else if ("copyMethods".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        boolean copyMethods = (boolean) action.getValue();
                        if (copyMethods) {
                            trees = Trees.instance(processingEnv);
                        }
                    } else if ("feature".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        List implementList = (List) action.getValue();
                        for (Object implement : implementList) {
                            String className = annotationParamToClassName(implement);
                            try {
                                Class featureClass = Class.forName(className);
                                QuickDtoFeature feature = (QuickDtoFeature) featureClass.newInstance();
                                if (!dtoDef.features.contains(feature)) {
                                    dtoDef.features.add(feature);
                                }
                            } catch (Exception e) {
                                processingEnv.getMessager().printMessage(Kind.WARNING, "Can't create feature:" + className);
                            }
                        }
                    }
                }
            }
        }
    }

    private void addSource(DtoDef dtoDef, String className) {
        Source sourceDef = new Source();
        dtoDef.sources.add(sourceDef);
        Elements elementUtils = processingEnv.getElementUtils();
        sourceDef.type = className;
        TypeElement sourceType = elementUtils.getTypeElement(className);
        while (sourceType != null) {
            for (Element sourceSubEl : sourceType.getEnclosedElements()) {
                if (sourceSubEl instanceof ExecutableElement) {
                    String name = sourceSubEl.getSimpleName().toString();
                    int numParams = ((ExecutableElement) sourceSubEl).getParameters().size();

                    if (name.startsWith("set") && numParams == 1) {
                        String accessorName = name.substring(3);
                        DtoField field = dtoDef.fields.get(accessorName);
                        if (field != null) {
                            VariableElement paramElement = ((ExecutableElement) sourceSubEl).getParameters().get(0);
                            String toType = paramElement.asType().toString();
                            String fromType = field.getTypeString();
                            mapAccessorConverter(dtoDef, field, className, sourceSubEl, toType, fromType, sourceDef.setters, accessorName);
                        }
                    }
                    if (name.startsWith("get") && numParams == 0) {
                        String accessorName = name.substring(3);
                        DtoField field = dtoDef.fields.get(accessorName);
                        if (field != null) {
                            String fromType = ((ExecutableElement) sourceSubEl).getReturnType().toString();
                            String toType = field.getTypeString();
                            mapAccessorConverter(dtoDef, field, className, sourceSubEl, toType, fromType, sourceDef.getters, accessorName);
                        }
                    }
                    if (name.startsWith("is") && numParams == 0) {
                        String accessorName = name.substring(2);
                        DtoField field = dtoDef.fields.get(accessorName);
                        if (field != null) {
                            String fromType = ((ExecutableElement) sourceSubEl).getReturnType().toString();
                            String toType = field.getTypeString();
                            mapAccessorConverter(dtoDef, field, className, sourceSubEl, toType, fromType, sourceDef.getters, accessorName);
                        }
                    }
                }
            }

            if (sourceType.getSuperclass() instanceof NoType) {
                sourceType = null;
            } else {
                sourceType = (TypeElement) ((DeclaredType) sourceType.getSuperclass()).asElement();
            }
        }
    }

    /**
     * Creates a map of fields to accessor methods and adds a converter if on is needed.
     */
    private SourceCopyMap createSourceCopyMap(DtoDef dtoDef, SourceDef sourceDef) {
        SourceCopyMap sourceCopyMap = new SourceCopyMap();
        sourceCopyMap.sourceDef = sourceDef;

        for (AccessorMethod getter : sourceDef.getters.values()) {
            DtoField field = dtoDef.fields.get(getter.accessorName);
            MappedAccessor mappedGetter = mapFieldToAccessor(true, field, getter, dtoDef);
            if (mappedGetter != null) {
                field.setSourceMapped();
                sourceCopyMap.mappedGetters.put(field.getAccessorName(), mappedGetter);
            }
        }

        for (AccessorMethod setter : sourceDef.setters.values()) {
            DtoField field = dtoDef.fields.get(setter.accessorName);
            MappedAccessor mappedSetter = mapFieldToAccessor(false, field, setter, dtoDef);
            if (mappedSetter != null) {
                field.setSourceMapped();
                sourceCopyMap.mappedSetters.put(field.getAccessorName(), mappedSetter);
            }
        }

        return sourceCopyMap;
    }

    /**
     * Attempts to associate a Field with an Accessor by match types.  If types mismatch,
     * attempts to find a converter.
     *
     * @return 'null' if type mismatch and no converter found.
     */
    private MappedAccessor mapFieldToAccessor(boolean getter, DtoField field, AccessorMethod method, DtoDef converterList) {
        if (field == null) {
            return null;
        }

        MappedAccessor mappedAccessor = new MappedAccessor(field, method);

        String fromType;
        String toType;
        if (getter) {
            fromType = method.typeString;
            toType = field.getTypeString();
        } else {
            toType = method.typeString;
            fromType = field.getTypeString();
        }

        boolean map;
        if (fromType.equals(toType)) {
            map = true;

        } else { // try to find converter
            Method converter = converterList.getConverter(toType, fromType);
            if (converter != null) {
                map = true;
                mappedAccessor.converterMethod = converter;

            } else {
                map = false;
                processingEnv.getMessager().printMessage(Kind.WARNING, "Type Mismatch(" + toType + ":" + fromType + ") for " + field.getAccessorName());
            }
        }

        if (map) {
            return mappedAccessor;
        } else {
            return null;
        }
    }

    /**
     * Finds all the accessor methods for a specified class
     */
    private SourceDef createSourceDef(String className) {
        SourceDef sourceDef = new SourceDef();
        Elements elementUtils = processingEnv.getElementUtils();
        sourceDef.type = className;
        TypeElement sourceType = elementUtils.getTypeElement(className);
        while (sourceType != null) {
            for (Element sourceSubEl : sourceType.getEnclosedElements()) {
                if (sourceSubEl instanceof ExecutableElement) {

                    AccessorMethod method = createAccessorMethod(sourceSubEl);
                    if (method != null) {
                        if (method.isGetter) {
                            sourceDef.getters.put(method.accessorName, method);
                        } else {
                            sourceDef.setters.put(method.accessorName, method);
                        }
                    }
                }
            }

            if (sourceType.getSuperclass() instanceof NoType) {
                sourceType = null;
            } else {
                sourceType = (TypeElement) ((DeclaredType) sourceType.getSuperclass()).asElement();
            }
        }

        return sourceDef;
    }

    /**
     * Collects information about an accessor method.
     *
     * @return 'null' if wasn't an accessor.
     */
    private AccessorMethod createAccessorMethod(Element sourceSubEl) {
        AccessorMethod method = new AccessorMethod();
        method.methodName = sourceSubEl.getSimpleName().toString();

        int numParams = ((ExecutableElement) sourceSubEl).getParameters().size();
        if (method.methodName.startsWith("set") && numParams == 1) {
            method.isGetter = false;
            method.accessorName = method.methodName.substring(3);

            VariableElement paramElement = ((ExecutableElement) sourceSubEl).getParameters().get(0);
            method.type = paramElement.asType();
            method.typeString = method.type.toString();

        } else if (method.methodName.startsWith("get") && numParams == 0) {
            method.isGetter = true;
            method.accessorName = method.methodName.substring(3);

            method.type = ((ExecutableElement) sourceSubEl).getReturnType();
            method.typeString = method.type.toString();

        } else if (method.methodName.startsWith("is") && numParams == 0) {
            method.isGetter = true;
            method.accessorName = method.methodName.substring(2);

            method.type = ((ExecutableElement) sourceSubEl).getReturnType();
            method.typeString = method.type.toString();

        } else {
            method = null;
        }
        return method;
    }


    private void mapAccessorConverter(DtoDef dtoDef, DtoField field, String sourceClass, Element sourceSubEl, String toType, String fromType, HashMap<String, Method> accessorMap, String accessorName) {
        boolean map = false;
        Method converter = null;
        if (!fromType.equals(toType)) {
            converter = dtoDef.getConverter(toType, fromType);
            if (converter != null) {
                map = true;
            } else {
                processingEnv.getMessager().printMessage(Kind.WARNING, "Type Mismatch(" + toType + ":" + fromType + ") for " + sourceClass + "." + sourceSubEl);
            }
        } else {
            map = true;
        }

        if (map) {
            field.setSourceMapped();
            accessorMap.put(accessorName, converter);
        }
    }

    private void addAnnotations(Element subelement, DtoField field) {
        List<? extends AnnotationMirror> annotationMirrors = subelement.getAnnotationMirrors();
        for (AnnotationMirror am : annotationMirrors) {
            if (!isQuickDtoAnntoation(am)) {
                if (subelement.getKind() == ElementKind.FIELD) {
                    field.addFieldAnnotation(am.toString());

                } else if (subelement.getKind() == ElementKind.METHOD) {
                    if (subelement.getSimpleName().toString().startsWith("get")) {
                        field.addGetterAnnotation(am.toString());

                    } else if (subelement.getSimpleName().toString().startsWith("set")) {
                        field.addSetterAnnotation(am.toString());
                    }
                }

            }
        }
    }

    private String annotationParamToClassName(Object source) {
        String className = source.toString();
        className = className.substring(0, className.length() - 6);
        return className;
    }
}
