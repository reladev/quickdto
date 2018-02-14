package org.reladev.quickdto.processor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.type.UnionType;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleTypeVisitor7;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import com.sun.source.tree.MethodTree;
import com.sun.source.util.Trees;
import org.reladev.quickdto.shared.CopyFromOnly;
import org.reladev.quickdto.shared.CopyToOnly;
import org.reladev.quickdto.shared.EqualsHashCode;
import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.shared.QuickDtoHelper;
import org.reladev.quickdto.shared.StrictCopy;

@SupportedAnnotationTypes({"org.reladev.quickdto.shared.QuickDto", "org.reladev.quickdto.shared.QuickDtoHelper"})
public class QuickDtoProcessor extends AbstractProcessor {
    private static final String DefSuffix = "Def";
    private static final String HelperSuffix = "Helper";

    private ProcessingEnvironment processingEnv;
    private Trees trees;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        super.init(processingEnv);
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        HashMap<String, DtoDef> defs = new HashMap<>();
        for (Element element : env.getElementsAnnotatedWith(QuickDto.class)) {
            if (!element.getSimpleName().toString().endsWith(DefSuffix)) {
                processingEnv.getMessager().printMessage(Kind.ERROR, element.getSimpleName() + " DtoDef must end in '" + DefSuffix + "'");
            } else {
                DtoDef dtoDef = processDtoDef(element);
                defs.put(dtoDef.qualifiedName + DefSuffix, dtoDef);
                processingEnv.getMessager().printMessage(Kind.NOTE, dtoDef.qualifiedName + " added to Defs");
                writeDto(dtoDef);
            }
        }

        for (Element element : env.getElementsAnnotatedWith(QuickDtoHelper.class)) {
            createHelper(element, defs);
        }

        return true;
    }

    private DtoDef processDtoDef(Element defElement) {
        processingEnv.getMessager().printMessage(Kind.NOTE, "QuickDto: " + defElement.getSimpleName());
        DtoDef dtoDef = new DtoDef();
        PackageElement packageElement = (PackageElement) defElement.getEnclosingElement();
        dtoDef.packageString = packageElement.getQualifiedName().toString();
        dtoDef.name = defElement.getSimpleName().toString();
        dtoDef.name = dtoDef.name.substring(0, dtoDef.name.length() - 3);
        dtoDef.qualifiedName = dtoDef.packageString + "." + dtoDef.name;

        addClassAnnotations(defElement, dtoDef);
        addFieldMethods(defElement, dtoDef);
        addSources(defElement, dtoDef);

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
        for (Element subelement : defElement.getEnclosedElements()) {
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

    private void addSources(Element element, DtoDef dtoDef) {
        final String annotationName = QuickDto.class.getName();
        element.getAnnotationMirrors();
        for(AnnotationMirror am : element.getAnnotationMirrors() ) {
            AnnotationValue action;
            if(annotationName.equals(am.getAnnotationType().toString()) ) {
                for(Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet() ) {
                    if("source".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        List sources = (List) action.getValue();
                        for (Object source: sources) {
                            String className = annotationParamToClassName(source);
                            addSource(dtoDef, className);
                        }

                    } else if("strictCopy".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        dtoDef.strictCopy = (boolean) action.getValue();

                    } else if("fieldAnnotationsOnGetter".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        dtoDef.fieldAnnotationsOnGetter = (boolean) action.getValue();

                    } else if("implement".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        List implementList = (List) action.getValue();
                        for (Object implement : implementList) {
                            String className = annotationParamToClassName(implement);
                            dtoDef.implementList.add(className);
                        }

                    } else if("extend".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        Object extend = action.getValue();
                        String className = extend.toString();
                        if (!className.equals(Object.class.getCanonicalName())) {
                            dtoDef.extend = className;
                        }

                    } else if("copyMethods".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        boolean copyMethods = (boolean) action.getValue();
                        if (copyMethods) {
                            trees = Trees.instance(processingEnv);
                        }
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

    private boolean mapAccessorConverter(DtoDef dtoDef, DtoField field, String sourceClass, Element sourceSubEl, String toType, String fromType, HashMap<String, Method> accessorMap, String accessorName) {
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

        return map;
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

    private void createHelper(Element element, HashMap<String, DtoDef> defs) {
        String className = element.getSimpleName().toString();
        PackageElement packageElement = (PackageElement) element.getEnclosingElement();
        String packageString = packageElement.getQualifiedName().toString();
        processingEnv.getMessager().printMessage(Kind.NOTE, "QuickDtoHelper: " + className);

        List<DtoDef> dtoDefs = addHelperDto(element, defs);

        if (dtoDefs.isEmpty()) {
            processingEnv.getMessager().printMessage(Kind.NOTE, "QuickDtoHelper: " + className + " Skipped (No Dtos found).");
        } else {
            writeHelper(packageString, className, dtoDefs);
        }


    }

    private List<DtoDef> addHelperDto(Element element, HashMap<String, DtoDef> defs) {
        List<DtoDef> helperDefs = new LinkedList<>();
        final String annotationName = QuickDtoHelper.class.getName();
        element.getAnnotationMirrors();
        for (AnnotationMirror am : element.getAnnotationMirrors()) {
            AnnotationValue action;
            if (annotationName.equals(am.getAnnotationType().toString())) {
                boolean strictCopy = true;
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet()) {
                    if ("strictCopy".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        strictCopy = (boolean) action.getValue();
                    }
                }
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet()) {
                    if ("dtoDef".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        List sources = (List) action.getValue();
                        for (Object source : sources) {
                            String dtoName = annotationParamToClassName(source);
                            DtoDef dtoDef = defs.get(dtoName);
                            if (dtoDef == null) {
                                Elements elementUtils = processingEnv.getElementUtils();
                                TypeElement dtoType = elementUtils.getTypeElement(dtoName);
                                dtoDef = processDtoDef(dtoType);
                                defs.put(dtoName, dtoDef);
                                dtoDef.makeDto = false;
                            }

                            dtoDef.sources.clear();
                            dtoDef.strictCopy = strictCopy;
                            String sourceClassName = element.toString();
                            addSource(dtoDef, sourceClassName);
                            helperDefs.add(dtoDef);
                        }
                    }
                }
            }
        }

        return helperDefs;
    }



    private void writeDto(DtoDef dtoDef)  {
        if (!dtoDef.makeDto) {
            return;
        }
        try {
            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(dtoDef.packageString + "." + dtoDef.name);

            BufferedWriter bw = new BufferedWriter(jfo.openWriter());
            bw.append("package ").append(dtoDef.packageString).append(";\n");
            bw.newLine();
            bw.append("import java.util.Arrays;\n");
            bw.append("import java.util.HashSet;\n");
            bw.append("import java.util.List;\n");
            bw.append("import java.util.Map;\n");
            bw.append("import java.util.Set;\n");
            bw.append("import java.util.Objects;\n");
            bw.append("import org.reladev.quickdto.shared.GwtIncompatible;\n");
            bw.newLine();
            for (String annotation: dtoDef.annotations) {
                bw.append(annotation);
                bw.newLine();
            }
            bw.append("public class ").append(dtoDef.name);
            if (dtoDef.extend != null) {
                bw.append(" extends ").append(dtoDef.extend);
            }
            if (!dtoDef.implementList.isEmpty()) {
                bw.append(" implements ");
                boolean firstPass = true;
                for (String implement : dtoDef.implementList) {
                    if (!firstPass) {
                        bw.append(", ");
                    }

                    bw.append(implement);
                    firstPass = false;
                }
            }
            bw.append(" {\n");
            bw.newLine();
            writeFieldsEnum(dtoDef, bw);
            bw.newLine();
            writeFields(dtoDef, bw);
            bw.newLine();
            writeDirty(bw);
            writeGettersSetters(dtoDef, bw);
            writeEqualsHash(dtoDef, bw);
            writeCopyMethods(dtoDef, bw);
            writeOtherMethods(dtoDef, bw);
            bw.append("}\n");

            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHelper(String packageString, String sourceClassName, List<DtoDef> dtoDefs) {
        try {
            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(packageString + "." + sourceClassName + HelperSuffix);

            BufferedWriter bw = new BufferedWriter(jfo.openWriter());
            bw.append("package ").append(packageString).append(";\n");
            bw.newLine();
            bw.append("import java.util.Arrays;\n");
            bw.append("import java.util.HashSet;\n");
            bw.append("import java.util.List;\n");
            bw.append("import java.util.Map;\n");
            bw.append("import java.util.Set;\n");
            bw.append("import java.util.Objects;\n");
            bw.append("import org.reladev.quickdto.shared.GwtIncompatible;\n");
            bw.newLine();
            for (DtoDef dtoDef : dtoDefs) {
                bw.append("import ").append(dtoDef.qualifiedName).append(";\n");
            }
            bw.newLine();
            bw.append("public class ").append(sourceClassName).append(HelperSuffix).append(" {\n");
            bw.newLine();
            writeHelperCopyMethods(dtoDefs, bw);
            bw.append("}\n");

            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFieldsEnum(DtoDef dtoDef, BufferedWriter bw) throws IOException {
        bw.append("\tpublic static enum Fields {\n");
        boolean first = true;
        for (DtoField field : dtoDef.fields.values()) {
            if (!first) {
                bw.append(",\n");
            } else {
                first = false;
            }
            bw.append("\t\t").append(field.getEnumName()).append("(\"").append(field.getFieldName()).append("\")");
        }
        bw.append(";\n");
        bw.newLine();
        bw.append("\t\tprivate String name;\n");
        bw.newLine();
        bw.append("\t\tFields(String name) {\n");
        bw.append("\t\t\tthis.name = name;\n");
        bw.append("\t\t}\n");
        bw.newLine();
        bw.append("\t\tpublic String toString() {\n");
        bw.append("\t\t\treturn name;\n");
        bw.append("\t\t}\n");
        bw.newLine();
        bw.append("\t\tpublic static Fields get(String fieldName) {\n");
        bw.append("\t\t\tfor (Fields field: Fields.values()) {\n");
        bw.append("\t\t\t\tif (field.name.equals(fieldName)) {\n");
        bw.append("\t\t\t\t\treturn field;\n");
        bw.append("\t\t\t\t}\n");
        bw.append("\t\t\t}\n");
        bw.append("\t\t\treturn null;\n");
        bw.append("\t\t}\n");
        bw.append("\t}\n");
    }

    private void writeFields(DtoDef dtoDef, BufferedWriter bw) throws IOException {
        for (DtoField field : dtoDef.fields.values()) {
            if (!dtoDef.fieldAnnotationsOnGetter) {
                for (String annotation : field.getFieldAnnotations()) {
                    bw.append("\t").append(annotation).append("\n");
                }
            }
            if (field.isPrimitive()) {
                bw.append("\tprivate ").append(field.getPrimitiveTypeString()).append(" ").append(field.getFieldName()).append(";\n");
            } else {
                bw.append("\tprivate ").append(field.getTypeString()).append(" ").append(field.getFieldName()).append(";\n");
            }
        }
    }

    private void writeDirty(BufferedWriter bw) throws IOException {
        bw.append("\tprotected Set<Fields> dirtyFields = new HashSet<Fields>();\n");
        bw.newLine();
        bw.append("\tpublic void setDirty(Fields field, boolean dirty) {\n");
        bw.append("\t\tif (dirty) {\n");
        bw.append("\t\t\tdirtyFields.add(field);\n");
        bw.append("\t\t} else {\n");
        bw.append("\t\t\tdirtyFields.remove(field);\n");
        bw.append("\t\t}\n");
        // Todo handle dirty for nested
        //bw.append("\t\tif (parent != null) {\n");
        //bw.append("\t\t\tparent.setDirty(fieldInParent, isDirty());\n");
        //bw.append("\t\t}\n");
        bw.append("\t}\n");
        bw.newLine();
        bw.append("\tpublic boolean isDirty() {\n");
        bw.append("\t\treturn !dirtyFields.isEmpty();\n");
        bw.append("\t}\n");
        bw.newLine();
        bw.append("\tpublic boolean isDirty(Fields field) {\n");
        bw.append("\t\treturn dirtyFields.contains(field);\n");
        bw.append("\t}\n");
        bw.newLine();
        bw.append("\tpublic void setDirtyFields(Set<Fields> dirtyFields) { \n");
        bw.append("\t\tthis.dirtyFields = dirtyFields;\n");
        bw.append("\t}\n");
        bw.newLine();
        bw.append("\tpublic Set<Fields> getDirtyFields() {\n");
        bw.append("\t\treturn dirtyFields;\n");
        bw.append("\t}\n");
        bw.newLine();
        bw.append("\tpublic void resetDirty() {\n");
        bw.append("\t\tdirtyFields.clear();\n");
        bw.append("\t}\n");
        bw.newLine();
    }

    private void writeGettersSetters(DtoDef dtoDef, BufferedWriter bw) throws IOException {
        for (DtoField field : dtoDef.fields.values()) {
            if (dtoDef.fieldAnnotationsOnGetter) {
                for (String annotation : field.getFieldAnnotations()) {
                    bw.append("\t").append(annotation).append("\n");
                }
            }
            for (String annotation : field.getGetterAnnotations()) {
                bw.append("\t").append(annotation).append("\n");
            }
            bw.append("\tpublic ").append(field.getTypeString());
            if ("boolean".equals(field.getTypeString()) || "java.lang.Boolean".equals(field.getTypeString())) {
                bw.append(" is");
            } else {
                bw.append(" get");
            }
            bw.append(field.getAccessorName()).append("() {\n");
            if (field.isPrimitive()) {
                bw.append("\t\tif (").append(field.getFieldName()).append(" != null) {\n");
                bw.append("\t\t\treturn ").append(field.getFieldName()).append(";\n");
                bw.append("\t\t} else {\n");
                bw.append("\t\t\treturn ").append(field.getDefaultValue()).append(";\n");
                bw.append("\t\t}\n");

            } else {
                bw.append("\t\treturn ").append(field.getFieldName()).append(";\n");
            }
            bw.append("\t}\n");
            bw.newLine();
            if (!field.isExcludeSetter()) {
                for (String annotation : field.getSetterAnnotations()) {
                    bw.append("\t").append(annotation).append("\n");
                }
                bw.append("\tpublic void set").append(field.getAccessorName()).append("(").append(field.getTypeString()).append(" ").append(field.getFieldName()).append(") {\n");
                bw.append("\t\tif (this.").append(field.getFieldName()).append(" == null || !Objects.equals(this.").append(field.getFieldName()).append(", ").append(field.getFieldName()).append(")) {\n");
                bw.append("\t\t\tsetDirty(Fields.").append(field.getEnumName()).append(", true);\n");
                bw.append("\t\t\tthis.").append(field.getFieldName()).append(" = ").append(field.getFieldName()).append(";\n");
                bw.append("\t\t}\n");
                bw.append("\t}\n");
                bw.newLine();
            }
        }
    }

    private void writeEqualsHash(DtoDef dtoDef, BufferedWriter bw) throws IOException {
        List<DtoField> equalsFields = new LinkedList<>();
        for (DtoField field : dtoDef.fields.values()) {
            if (field.isEqualsHashCode()) {
                equalsFields.add(field);
            }
        }
        Collection<DtoField> genFields;
        if (equalsFields.isEmpty()) {
            genFields = dtoDef.fields.values();

        } else {
            genFields = equalsFields;
        }

        bw.append("\tpublic boolean equals(Object o) {\n");
        bw.append("\t\tif (this == o) {\n");
        bw.append("\t\t\treturn true;\n");
        bw.append("\t\t}\n");
        bw.newLine();
        bw.append("\t\tif (o == null || getClass() != o.getClass()) {\n");
        bw.append("\t\t\treturn false;\n");
        bw.append("\t\t}\n");
        bw.newLine();
        bw.append("\t\t").append(dtoDef.name).append(" that = (").append(dtoDef.name).append(") o;\n");
        bw.newLine();

        for (DtoField field : genFields) {
            if (field.getTypeString().equals("float")) {
                bw.append("\t\tif (Float.compare(").append(field.getFieldName()).append(", that.").append(field.getFieldName()).append(") != 0) {\n");
            } else if (field.getTypeString().equals("double")) {
                bw.append("\t\tif (Double.compare(").append(field.getFieldName()).append(", that.").append(field.getFieldName()).append(") != 0) {\n");
            } else if (field.isPrimitive()) {
                bw.append("\t\tif (").append(field.getFieldName()).append(" != that.").append(field.getFieldName()).append(") {\n");
            } else if (field.getTypeString().endsWith("[]")) {
                bw.append("\t\tif (Arrays.equals(").append(field.getFieldName()).append(", that.").append(field.getFieldName()).append(")) {\n");
            } else {
                bw.append("\t\tif (").append(field.getFieldName()).append(" != null ? !").append(field.getFieldName()).append(".equals(that.").append(field.getFieldName()).append(") : that.").append(field.getFieldName()).append(" != null) {\n");
            }

            bw.append("\t\t\treturn false;\n");
            bw.append("\t\t}\n");
        }

        bw.newLine();
        bw.append("\t\treturn true;\n");
        bw.append("\t}\n");
        bw.newLine();

        bw.append("\tpublic int hashCode() {\n");
        bw.append("\t\tlong temp;\n\n");
        boolean first = true;
        for (DtoField field : genFields) {
            if (field.getTypeString().equals("double")) {
                bw.append("\t\ttemp = Double.doubleToLongBits(").append(field.getFieldName()).append(");\n");
            }
            if (first) {
                bw.append("\t\tint result = ");
                first = false;
            } else {
                bw.append("\t\tresult = 31 * result + ");
            }

            if (field.getTypeString().equals("long")) {
                bw.append("(int) (").append(field.getFieldName()).append(" ^ (").append(field.getFieldName()).append(" >>> 32));\n");
            } else if (field.getTypeString().equals("float")) {
                bw.append("(").append(field.getFieldName()).append(" != +0.0f ? Float.floatToIntBits(").append(field.getFieldName()).append(") : 0);");
            } else if (field.getTypeString().equals("double")) {
                bw.append("(int) (temp ^ (temp >>> 32));\n");
            } else if (field.getTypeString().equals("boolean")) {
                bw.append("(").append(field.getFieldName()).append(" ? 1 : 0);\n");
            } else if (field.isPrimitive()) {
                bw.append(field.getFieldName()).append(";\n");
            } else {
                bw.append("(").append(field.getFieldName()).append(" != null ? ").append(field.getFieldName()).append(".hashCode() : 0);\n");
            }
        }

        bw.append("\t\treturn result;\n");
        bw.append("\t}\n");
        bw.newLine();
    }

    private void writeCopyMethods(DtoDef dtoDef, BufferedWriter bw) throws IOException {
        if (!dtoDef.sources.isEmpty()) {
            for (Source source : dtoDef.sources) {
                writeCopyTo(source, dtoDef, bw);
                writeCopyFrom(source, dtoDef, bw);
            }
            if (dtoDef.strictCopy) {
                writeUnmapped(dtoDef, bw);
            }
        }
    }

    private void writeCopyTo(Source source, DtoDef dtoDef, BufferedWriter bw) throws IOException {
        bw.append("\t@GwtIncompatible\n");
        bw.append("\tpublic void copyTo(").append(source.type).append(" dest, Fields... fields) {\n");
        bw.append("\t\tif (fields.length > 0) {\n");
        bw.append("\t\t\tcopyTo(dest, Arrays.asList(fields));\n");
        bw.append("\t\t} else {\n");
        bw.append("\t\t\tcopyTo(dest, getDirtyFields());\n");
        bw.append("\t\t}\n");
        bw.append("\t}\n");
        bw.newLine();
        bw.append("\t@GwtIncompatible\n");
        bw.append("\tpublic void copyTo(").append(source.type).append(" dest, Iterable<Fields> fields) {\n");
        bw.append("\t\tfor (Fields field: fields) {\n");
        bw.append("\t\t\tswitch (field) {\n");
        for (Entry<String, Method> setter: source.setters.entrySet()) {
            DtoField field = dtoDef.fields.get(setter.getKey());
            if (!field.isCopyFrom()) {
                bw.append("\t\t\t\tcase ").append(field.getEnumName()).append(":\n");
                bw.append("\t\t\t\t\tdest.set").append(field.getAccessorName()).append("(");
                if (setter.getValue() != null) {
                    bw.append(dtoDef.name).append("Def.convert(").append(field.getFieldName()).append(")");
                } else {
                    bw.append(field.getFieldName());
                }
                bw.append(");\n");
                bw.append("\t\t\t\t\tbreak;\n");
            }
        }
        bw.append("\t\t\t}\n");
        bw.append("\t\t}\n");
        bw.append("\t}\n");
        bw.newLine();
    }

    private void writeCopyFrom(Source source, DtoDef dtoDef, BufferedWriter bw) throws IOException {
        bw.append("\t@GwtIncompatible\n");
        bw.append("\tpublic void copyFrom(").append(source.type).append(" source) {\n");
        for (Entry<String, Method> getter: source.getters.entrySet()) {
            DtoField field = dtoDef.fields.get(getter.getKey());
            bw.append("\t\t").append(field.getFieldName()).append(" = ");
            if (getter.getValue() != null) {
                bw.append(dtoDef.name).append("Def.convert(");
            }
            bw.append("source.").append(field.getGetAccessorName()).append("()");
            if (getter.getValue() != null) {
                bw.append(")");
            }
            bw.append(";\n");
        }

        bw.append("\t}\n");
        bw.newLine();
    }

    private void writeHelperCopyMethods(List<DtoDef> dtoDefs, BufferedWriter bw) throws IOException {
        for (DtoDef dtoDef : dtoDefs) {
            if (!dtoDef.sources.isEmpty()) {
                for (Source source : dtoDef.sources) {
                    writeHelperCopyTo(source, dtoDef, bw);
                    writeHelperCopyFrom(source, dtoDef, bw);
                }
                if (dtoDef.strictCopy) {
                    writeUnmapped(dtoDef, bw);
                }
            }
        }
    }

    private void writeHelperCopyTo(Source source, DtoDef dtoDef, BufferedWriter bw) throws IOException {
        bw.append("\t@GwtIncompatible\n");
        bw.append("\tpublic static void copyTo(").append(dtoDef.name).append(" dto, ");
        bw.append(source.type).append(" dest, ").append(dtoDef.name).append(".Fields... fields) {\n");
        bw.append("\t\tif (fields.length > 0) {\n");
        bw.append("\t\t\tcopyTo(dto, dest, Arrays.asList(fields));\n");
        bw.append("\t\t} else {\n");
        bw.append("\t\t\tcopyTo(dto, dest, dto.getDirtyFields());\n");
        bw.append("\t\t}\n");
        bw.append("\t}\n");
        bw.newLine();
        bw.append("\t@GwtIncompatible\n");
        bw.append("\tpublic static void copyTo(").append(dtoDef.name).append(" dto, ");
        bw.append(source.type).append(" dest, Iterable<").append(dtoDef.name).append(".Fields> fields) {\n");
        bw.append("\t\tfor (").append(dtoDef.name).append(".Fields field: fields) {\n");
        bw.append("\t\t\tswitch (field) {\n");
        for (Entry<String, Method> setter : source.setters.entrySet()) {
            DtoField field = dtoDef.fields.get(setter.getKey());
            if (!field.isCopyFrom()) {
                bw.append("\t\t\t\tcase ").append(field.getEnumName()).append(":\n");
                bw.append("\t\t\t\t\tdest.set").append(field.getAccessorName()).append("(");
                if (setter.getValue() != null) {
                    bw.append(dtoDef.qualifiedName).append("Def.convert(dto.").append(field.getGetAccessorName()).append("())");
                } else {
                    bw.append("dto.").append(field.getGetAccessorName()).append("()");
                }

                bw.append(");\n");
                bw.append("\t\t\t\t\tbreak;\n");
            }
        }
        bw.append("\t\t\t}\n");
        bw.append("\t\t}\n");
        bw.append("\t}\n");
        bw.newLine();
    }

    private void writeHelperCopyFrom(Source source, DtoDef dtoDef, BufferedWriter bw) throws IOException {
        bw.append("\t@GwtIncompatible\n");
        bw.append("\tpublic static void copyFrom(").append(dtoDef.name).append(" dto, ");
        bw.append(source.type).append(" source) {\n");
        for (Entry<String, Method> getter : source.getters.entrySet()) {
            DtoField field = dtoDef.fields.get(getter.getKey());
            bw.append("\t\tdto.set").append(field.getAccessorName()).append("(");
            if (getter.getValue() != null) {
                bw.append(dtoDef.qualifiedName).append("Def.convert(");
            }
            bw.append("source.").append(field.getGetAccessorName()).append("()");
            if (getter.getValue() != null) {
                bw.append(")");
            }
            bw.append(");\n");
        }

        bw.append("\t}\n");
        bw.newLine();
    }

    private void writeUnmapped(DtoDef dtoDef, BufferedWriter bw) throws IOException {
        LinkedList<DtoField> unmapped = new LinkedList<>();
        for (DtoField field : dtoDef.fields.values()) {
            if (field.isStrictCopy() && !field.isSourceMapped()) {
                unmapped.add(field);
            }
        }
        if (!unmapped.isEmpty()) {
            bw.append("\tpublic void fieldsNotMappedToSource() {\n");
            for (DtoField field : unmapped) {
                bw.append("\t\t");
                bw.append(field.getFieldName());
                bw.append(";\n");
            }
            bw.append("\t}\n");

        }
    }

    private void writeOtherMethods(DtoDef dtoDef, BufferedWriter bw) throws IOException {
        for (Method method : dtoDef.methods) {
            if (method.body != null) {
                bw.append(method.body);
                bw.newLine();
            }
        }
    }

    private final TypeVisitor<Component, Element> getType = new SimpleTypeVisitor7<Component, Element>() {
        @Override
        public Component visitPrimitive(PrimitiveType t, Element element) {
            DtoField field = new DtoField();
            field.setType(t);
            field.setFieldName(element.toString());
            field.setPrimitive();
            return field;
        }

        @Override
        public Component visitArray(ArrayType t, Element element) {
            DtoField field = new DtoField();
            field.setType(t);
            field.setFieldName(element.toString());
            return field;
        }

        @Override
        public Component visitDeclared(DeclaredType t, Element element) {
            DtoField field = new DtoField();
            field.setType(t);
            field.setFieldName(element.toString());
            return field;
        }

        @Override
        public Component visitTypeVariable(TypeVariable t, Element element) {
            processingEnv.getMessager().printMessage(Kind.ERROR, "Couldn't process Type:" + element);
            return super.visitTypeVariable(t, element);
        }

        @Override
        public Component visitUnknown(TypeMirror t, Element element) {
            processingEnv.getMessager().printMessage(Kind.ERROR, "Couldn't process Unknown:" + element);
            return super.visitUnknown(t, element);
        }

        @Override
        public Component visitUnion(UnionType t, Element element) {
            processingEnv.getMessager().printMessage(Kind.ERROR, "Couldn't process Union:" + element);
            return super.visitUnion(t, element);
        }

        @Override
        protected Component defaultAction(TypeMirror e, Element element) {
            if (element.toString().endsWith("Dto")) {
                processingEnv.getMessager().printMessage(Kind.ERROR, "Couldn't process Dto:" + element + ".  Use DtoDef instead.");
            } else {
                processingEnv.getMessager().printMessage(Kind.ERROR, "Couldn't process Action:" + element);
            }
            return super.defaultAction(e, element);
        }

        @Override
        public Component visitNull(NullType t, Element element) {
            processingEnv.getMessager().printMessage(Kind.ERROR, "Couldn't process Null:" + element);
            return super.visitNull(t, element);
        }

        @Override
        public Component visitWildcard(WildcardType t, Element element) {
            processingEnv.getMessager().printMessage(Kind.ERROR, "Couldn't process Wildcard:" + element);
            return super.visitWildcard(t, element);
        }

        @Override
        public Component visitNoType(NoType t, Element element) {
            processingEnv.getMessager().printMessage(Kind.ERROR, "Couldn't process NoType:" + element);
            return super.visitNoType(t, element);
        }

        @Override
        public Component visitExecutable(ExecutableType t, Element element) {
            Component component = null;

            String name = element.toString();
            if ((name.startsWith("get") || name.startsWith("is")) && t.getReturnType().getKind() != TypeKind.VOID && t.getParameterTypes().size() == 0) {
                component = t.getReturnType().accept(getType, element);

            } else if (name.startsWith("set") && t.getReturnType().getKind() == TypeKind.VOID && t.getParameterTypes().size() == 1) {
                component = t.getParameterTypes().get(0).accept(getType, element);
            }

            if (component instanceof DtoField) {
                int start = 0;
                if (name.startsWith("set") || name.startsWith("get")) {
                    start = 3;
                } else if (name.startsWith("is")) {
                    start = 2;
                }
                int end = name.indexOf('(');
                if (end == -1) {
                    end = name.length();
                }
                ((DtoField) component).setFieldName(name.substring(start, end));

            } else if (element.toString().startsWith("convert(") && t.getParameterTypes().size() == 1) {
                Method method = new Method();
                method.converter = true;
                method.toType = t.getReturnType().toString();
                method.fromType = t.getParameterTypes().get(0).toString();

                if (trees != null) {
                    MethodScanner methodScanner = new MethodScanner();
                    MethodTree methodTree = methodScanner.scan((ExecutableElement) element, trees);
                    method.body = "\t" + methodTree.toString().replace("\n", "\n\t");
                    method.isStatic = element.getModifiers().contains(Modifier.STATIC);

                } else if (element.getModifiers().contains(Modifier.STATIC)) {
                    method.isStatic = true;

                } else {
                    processingEnv.getMessager().printMessage(Kind.WARNING, "IGNORING (" + element + ") it must be 'static' or copyMethod true");
                    method = null;
                }
                component = method;

            } else if (trees != null) {
                Method method = new Method();
                MethodScanner methodScanner = new MethodScanner();
                MethodTree methodTree = methodScanner.scan((ExecutableElement) element, trees);
                method.body = "\t" + methodTree.toString().replace("\n", "\n\t");

                component = method;

            } else {
                processingEnv.getMessager().printMessage(Kind.WARNING, "--IGNORING Method:" + element + " - If method should be copied, set \"copyMethod\" in @QuickDto");
            }

            return component;
        }

    };
}