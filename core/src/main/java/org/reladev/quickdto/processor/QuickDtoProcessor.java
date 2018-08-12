package org.reladev.quickdto.processor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import com.sun.tools.javac.code.Symbol.ClassSymbol;
import org.reladev.quickdto.feature.QuickDtoFeature;
import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.shared.QuickDtoHelper;

import static org.reladev.quickdto.processor.Reversed.reversed;

@SupportedAnnotationTypes({"org.reladev.quickdto.shared.QuickDto", "org.reladev.quickdto.shared.QuickDtoHelper"})
public class QuickDtoProcessor extends AbstractProcessor {
    private static final String DefSuffix = "Def";
    private static final String HelperSuffix = "Helper";

    private ProcessingEnvironment processingEnv;
    private ClassAnalyzer classAnalyzer;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        classAnalyzer = new ClassAnalyzer(processingEnv);
        super.init(processingEnv);
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        HashMap<String, DtoDef> defs = new HashMap<>();
        for (Element element : env.getElementsAnnotatedWith(QuickDto.class)) {
            if (!element.getSimpleName().toString().endsWith(DefSuffix)) {
                processingEnv.getMessager().printMessage(Kind.ERROR, element.getSimpleName() + " DtoDef must end in '" + DefSuffix + "'");
            } else {
                DtoDef dtoDef = classAnalyzer.processDtoDef((ClassSymbol) element);
                defs.put(dtoDef.qualifiedName + DefSuffix, dtoDef);
                writeDto(dtoDef);
            }
        }

        for (Element element : env.getElementsAnnotatedWith(QuickDtoHelper.class)) {
            createHelper(element, defs);
        }

        return true;
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
        //final String annotationName = QuickDtoHelper.class.getName();
        //element.getAnnotationMirrors();
        //for (AnnotationMirror am : element.getAnnotationMirrors()) {
        //    AnnotationValue action;
        //    if (annotationName.equals(am.getAnnotationType().toString())) {
        //        boolean strictCopy = true;
        //        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet()) {
        //            if ("strictCopy".equals(entry.getKey().getSimpleName().toString())) {
        //                action = entry.getValue();
        //                strictCopy = (boolean) action.getValue();
        //            }
        //        }
        //        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet()) {
        //            if ("dtoDef".equals(entry.getKey().getSimpleName().toString())) {
        //                action = entry.getValue();
        //                List sources = (List) action.getValue();
        //                for (Object source : sources) {
        //                    String dtoName = annotationParamToClassName(source);
        //                    DtoDef dtoDef = defs.get(dtoName);
        //                    if (dtoDef == null) {
        //                        Elements elementUtils = processingEnv.getElementUtils();
        //                        TypeElement dtoType = elementUtils.getTypeElement(dtoName);
        //                        dtoDef = processDtoDef(dtoType);
        //                        defs.put(dtoName, dtoDef);
        //                        dtoDef.makeDto = false;
        //                    }
        //
        //                    dtoDef.sources.clear();
        //                    dtoDef.strictCopy = strictCopy;
        //                    String sourceClassName = element.toString();
        //                    addSource(dtoDef, sourceClassName);
        //                    helperDefs.add(dtoDef);
        //                }
        //            }
        //        }
        //    }
        //}

        return helperDefs;
    }



    private void writeDto(DtoDef dtoDef)  {
        if (!dtoDef.makeDto) {
            return;
        }
        try {
            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(dtoDef.packageString + "." + dtoDef.name);

            IndentWriter bw = new IndentWriter(new BufferedWriter(jfo.openWriter()), "\t");
            bw.append("package ").append(dtoDef.packageString).append(";");
            bw.newLine();
            bw.line("import java.util.Arrays;");
            bw.line("import java.util.HashSet;");
            bw.line("import java.util.List;");
            bw.line("import java.util.Map;");
            bw.line("import java.util.Set;");
            bw.line("import java.util.Objects;");
            bw.line("import org.reladev.quickdto.shared.GwtIncompatible;");
            bw.newLine();
            for (String annotation: dtoDef.annotations) {
                bw.append(annotation);
                bw.newLine();
            }
            bw.line("public class ").append(dtoDef.name);
            if (dtoDef.dtoExtend != null) {
                bw.append(" extends ").append(dtoDef.dtoExtend);

            } else if (dtoDef.extend != null) {
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
            bw.append(" {");
            bw.indent();

            bw.newLine();
            writeFields(dtoDef, bw);
            for (QuickDtoFeature feature : dtoDef.features) {
                feature.writeFields(dtoDef, bw);
            }
            bw.newLine();
            writeGettersSetters(dtoDef, bw);
            for (QuickDtoFeature feature : dtoDef.features) {
                feature.writeMethods(dtoDef, bw);
            }
            writeEqualsHash(dtoDef, bw);
            writeCopyMethods(dtoDef, bw);
            writeOtherMethods(dtoDef, bw);
            writeFieldsEnum(dtoDef, bw);
            for (QuickDtoFeature feature : dtoDef.features) {
                feature.writeInnerClasses(dtoDef, bw);
            }

            bw.unindent();
            bw.line("}");

            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHelper(String packageString, String sourceClassName, List<DtoDef> dtoDefs) {
        try {
            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(packageString + "." + sourceClassName + HelperSuffix);

            IndentWriter bw = new IndentWriter(new BufferedWriter(jfo.openWriter()), "\t");
            bw.append("package ").append(packageString).append(";");
            bw.newLine();
            bw.line("import java.util.Arrays;");
            bw.line("import java.util.HashSet;");
            bw.line("import java.util.List;");
            bw.line("import java.util.Map;");
            bw.line("import java.util.Set;");
            bw.line("import java.util.Objects;");
            bw.line("import org.reladev.quickdto.shared.GwtIncompatible;");
            bw.newLine();
            for (DtoDef dtoDef : dtoDefs) {
                bw.line("import ").append(dtoDef.qualifiedName).append(";");
            }
            bw.newLine();
            bw.line("public class ").append(sourceClassName).append(HelperSuffix).append(" {");
            bw.newLine();
            writeHelperCopyMethods(dtoDefs, bw);
            bw.line("}");

            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFieldsEnum(DtoDef dtoDef, IndentWriter bw) throws IOException {
        bw.line(0, "public static enum Fields {");
        bw.startLineList(",");
        for (DtoField field : dtoDef.fields.values()) {
            bw.line(1, "").append(field.getEnumName()).append("(\"").append(field.getFieldName()).append("\")");
        }
        bw.endLineList();
        bw.append(";");
        bw.newLine();
        bw.line(1, "private String name;");
        bw.newLine();
        bw.line(1, "Fields(String name) {");
        bw.line(2, "this.name = name;");
        bw.line(1, "}");
        bw.newLine();
        bw.line(1, "public String toString() {");
        bw.line(2, "return name;");
        bw.line(1, "}");
        bw.newLine();
        bw.line(1, "public static Fields get(String fieldName) {");
        bw.line(2, "for (Fields field: Fields.values()) {");
        bw.line(3, "if (field.name.equals(fieldName)) {");
        bw.line(4, "return field;");
        bw.line(3, "}");
        bw.line(2, "}");
        bw.line(2, "return null;");
        bw.line(1, "}");
        bw.line(0, "}");
    }

    private void writeFields(DtoDef dtoDef, IndentWriter bw) throws IOException {
        for (DtoField field : dtoDef.fields.values()) {
            if (!dtoDef.fieldAnnotationsOnGetter) {
                for (String annotation : field.getFieldAnnotations()) {
                    bw.line(0, "").append(annotation);
                }
            }
            if (field.isPrimitive()) {
                bw.line(0, "private ").append(field.getPrimitiveTypeString()).append(" ").append(field.getFieldName()).append(";");
            } else {
                bw.line(0, "private ").append(field.getTypeString()).append(" ").append(field.getFieldName()).append(";");
            }
        }
    }

    private void writeGettersSetters(DtoDef dtoDef, IndentWriter bw) throws IOException {
        for (DtoField field : dtoDef.fields.values()) {
            if (dtoDef.fieldAnnotationsOnGetter) {
                for (String annotation : field.getFieldAnnotations()) {
                    bw.line("").append(annotation).append("");
                }
            }
            for (String annotation : field.getGetterAnnotations()) {
                bw.line("").append(annotation).append("");
            }
            bw.line("public ").append(field.getTypeString());
            if ("boolean".equals(field.getTypeString()) || "java.lang.Boolean".equals(field.getTypeString())) {
                bw.append(" is");
            } else {
                bw.append(" get");
            }
            bw.append(field.getAccessorName()).append("() {");

            bw.indent();
            for (QuickDtoFeature feature : dtoDef.features) {
                feature.preGetterLogic(field, dtoDef, bw);
            }
            if (field.isPrimitive()) {
                bw.line(0, "if (").append(field.getFieldName()).append(" != null) {");
                bw.line(1, "return ").append(field.getFieldName()).append(";");
                bw.line(0, "} else {");
                bw.line(1, "return ").append(field.getDefaultValue()).append(";");
                bw.line(0, "}");

            } else {
                bw.line(0, "return ").append(field.getFieldName()).append(";");
            }
            for (QuickDtoFeature feature : reversed(dtoDef.features)) {
                feature.postGetterLogic(field, dtoDef, bw);
            }
            bw.unindent();

            bw.line("}");
            bw.newLine();
            if (!field.isExcludeSetter()) {
                for (String annotation : field.getSetterAnnotations()) {
                    bw.line("").append(annotation).append("");
                }
                bw.line("public void set").append(field.getAccessorName()).append("(").append(field.getTypeString()).append(" ").append(field.getFieldName()).append(") {");

                bw.indent();
                for (QuickDtoFeature feature : dtoDef.features) {
                    feature.preSetterLogic(field, dtoDef, bw);
                }
                bw.line("this.").append(field.getFieldName()).append(" = ").append(field.getFieldName()).append(";");
                for (QuickDtoFeature feature : reversed(dtoDef.features)) {
                    feature.postSetterLogic(field, dtoDef, bw);
                }
                bw.unindent();

                bw.line("}");
                bw.newLine();
            }
        }
    }

    private void writeEqualsHash(DtoDef dtoDef, IndentWriter bw) throws IOException {
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

        bw.line(0, "public boolean equals(Object o) {");
        bw.line(1, "if (this == o) {");
        bw.line(2, "return true;");
        bw.line(1, "}");
        bw.newLine();
        bw.line(1, "if (o == null || getClass() != o.getClass()) {");
        bw.line(2, "return false;");
        bw.line(1, "}");
        bw.newLine();
        bw.line(1, "").append(dtoDef.name).append(" that = (").append(dtoDef.name).append(") o;");
        bw.newLine();

        for (DtoField field : genFields) {
            if (field.getTypeString().equals("float")) {
                bw.line(1, "if (Float.compare(").append(field.getFieldName()).append(", that.").append(field.getFieldName()).append(") != 0) {");
            } else if (field.getTypeString().equals("double")) {
                bw.line(1, "if (Double.compare(").append(field.getFieldName()).append(", that.").append(field.getFieldName()).append(") != 0) {");
            } else if (field.isPrimitive()) {
                bw.line(1, "if (").append(field.getFieldName()).append(" != that.").append(field.getFieldName()).append(") {");
            } else if (field.getTypeString().endsWith("[]")) {
                bw.line(1, "if (Arrays.equals(").append(field.getFieldName()).append(", that.").append(field.getFieldName()).append(")) {");
            } else {
                bw.line(1, "if (").append(field.getFieldName()).append(" != null ? !").append(field.getFieldName()).append(".equals(that.").append(field.getFieldName()).append(") : that.").append(field.getFieldName()).append(" != null) {");
            }

            bw.line(2, "return false;");
            bw.line(1, "}");
        }

        bw.newLine();
        bw.line(1, "return true;");
        bw.line(0, "}");
        bw.newLine();

        bw.line(0, "public int hashCode() {");
        bw.line(1, "long _l_;");
        bw.newLine();
        boolean first = true;
        for (DtoField field : genFields) {
            if (field.getTypeString().equals("double")) {
                bw.line(1, "_l_ = Double.doubleToLongBits(").append(field.getFieldName()).append(");");
            }
            if (first) {
                bw.line(1, "int _r_ = ");
                first = false;
            } else {
                bw.line(1, "_r_ = 31 * _r_ + ");
            }

            if (field.getTypeString().equals("long")) {
                bw.append("(int) (").append(field.getFieldName()).append(" ^ (").append(field.getFieldName()).append(" >>> 32));");
            } else if (field.getTypeString().equals("float")) {
                bw.append("(").append(field.getFieldName()).append(" != +0.0f ? Float.floatToIntBits(").append(field.getFieldName()).append(") : 0);");
            } else if (field.getTypeString().equals("double")) {
                bw.append("(int) (_l_ ^ (_l_ >>> 32));");
            } else if (field.getTypeString().equals("boolean")) {
                bw.append("(").append(field.getFieldName()).append(" ? 1 : 0);");
            } else if (field.isPrimitive()) {
                bw.append(field.getFieldName()).append(";");
            } else {
                bw.append("(").append(field.getFieldName()).append(" != null ? ").append(field.getFieldName()).append(".hashCode() : 0);");
            }
        }

        bw.line(1, "return _r_;");
        bw.line(0, "}");
        bw.newLine();
    }

    private void writeCopyMethods(DtoDef dtoDef, IndentWriter bw) throws IOException {
        if (!dtoDef.sourceMaps.isEmpty()) {
            for (SourceCopyMap source : dtoDef.sourceMaps) {
                writeCopyTo(source, dtoDef, bw);
                for (QuickDtoFeature feature : dtoDef.features) {
                    feature.writeCopyTo(source, dtoDef, bw);
                }

                writeCopyFrom(source, dtoDef, bw);
                for (QuickDtoFeature feature : dtoDef.features) {
                    feature.writeCopyFrom(source, dtoDef, bw);
                }
            }
            if (dtoDef.strictCopy) {
                writeUnmapped(dtoDef, bw);
            }
        }
    }

    private void writeCopyTo(SourceCopyMap source, DtoDef dtoDef, IndentWriter bw) throws IOException {
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public void copyTo(").append(source.sourceDef.type).append(" dest, Fields... fields) {");
        bw.line(1, "if (fields.length > 0) {");
        bw.line(2, "copyTo(dest, Arrays.asList(fields));");
        bw.line(1, "} else {");
        bw.line(2, "copyTo(dest, Fields.values());");
        bw.line(1, "}");
        bw.line(0, "}");
        bw.newLine();
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public void copyTo(").append(source.sourceDef.type).append(" dest, Iterable<Fields> fields) {");
        bw.line(1, "for (Fields field: fields) {");
        bw.line(2, "switch (field) {");
        for (MappedAccessor setter : source.mappedSetters.values()) {
            DtoField field = setter.dtoField;
            if (!field.isCopyFrom()) {
                bw.line(3, "case ").append(field.getEnumName()).append(":");
                bw.line(4, "dest.set").append(field.getAccessorName()).append("(");
                ConverterMethod converter = setter.converterMethod;
                if (converter != null) {
                    bw.append(dtoDef.name).append("Def.convert(").append(field.getFieldName());
                    if (converter.existingParam) {
                        bw.append(", dest.get").append(field.getAccessorName()).append("()");
                    }
                    bw.append(")");
                } else {
                    bw.append(field.getFieldName());
                }
                bw.append(");");
                bw.line(4, "break;");
            }
        }
        bw.line(2, "}");
        bw.line(1, "}");
        bw.line(0, "}");
        bw.newLine();
    }

    private void writeCopyFrom(SourceCopyMap source, DtoDef dtoDef, IndentWriter bw) throws IOException {
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public void copyFrom(").append(source.sourceDef.type).append(" source) {");
        for (MappedAccessor getter : source.mappedGetters.values()) {
            DtoField field = getter.dtoField;
            bw.line(1, "").append(field.getFieldName()).append(" = ");
            ConverterMethod converter = getter.converterMethod;
            if (converter != null) {
                bw.append(dtoDef.name).append("Def.convert(");
            }
            bw.append("source.").append(field.getGetAccessorName()).append("()");
            if (converter != null) {
                if (converter.existingParam) {
                    bw.append(", ").append(field.getFieldName());
                }
                bw.append(")");
            }
            bw.append(";");
        }

        bw.line(0, "}");
        bw.newLine();
    }

    private void writeHelperCopyMethods(List<DtoDef> dtoDefs, IndentWriter bw) throws IOException {
        for (DtoDef dtoDef : dtoDefs) {
            if (!dtoDef.sourceMaps.isEmpty()) {
                for (SourceCopyMap source : dtoDef.sourceMaps) {
                    writeHelperCopyTo(source, dtoDef, bw);
                    for (QuickDtoFeature feature : dtoDef.features) {
                        feature.writeHelperCopyTo(source, dtoDef, bw);
                    }

                    writeHelperCopyFrom(source, dtoDef, bw);
                    for (QuickDtoFeature feature : dtoDef.features) {
                        feature.writeHelperCopyFrom(source, dtoDef, bw);
                    }
                }
                if (dtoDef.strictCopy) {
                    writeUnmapped(dtoDef, bw);
                }
            }
        }
    }

    private void writeHelperCopyTo(SourceCopyMap source, DtoDef dtoDef, IndentWriter bw) throws IOException {
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public static void copyTo(").append(dtoDef.name).append(" dto, ");
        bw.append(source.sourceDef.type).append(" dest, ").append(dtoDef.name).append(".Fields... fields) {");
        bw.line(1, "if (fields.length > 0) {");
        bw.line(2, "copyTo(dto, dest, Arrays.asList(fields));");
        bw.line(1, "} else {");
        bw.line(2, "copyTo(dto, dest, ").append(dtoDef.name).append(".Fields.values());");
        bw.line(1, "}");
        bw.line(0, "}");
        bw.newLine();
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public static void copyTo(").append(dtoDef.name).append(" dto, ");
        bw.append(source.sourceDef.type).append(" dest, Iterable<").append(dtoDef.name).append(".Fields> fields) {");
        bw.line(1, "for (").append(dtoDef.name).append(".Fields field: fields) {");
        bw.line(2, "switch (field) {");
        for (MappedAccessor setter : source.mappedSetters.values()) {
            DtoField field = setter.dtoField;
            if (!field.isCopyFrom()) {
                bw.line(3, "case ").append(field.getEnumName()).append(":");
                bw.line(4, "dest.set").append(field.getAccessorName()).append("(");
                if (setter.converterMethod != null) {
                    bw.append(dtoDef.qualifiedName).append("Def.convert(dto.").append(field.getGetAccessorName()).append("())");
                } else {
                    bw.append("dto.").append(field.getGetAccessorName()).append("()");
                }

                bw.append(");");
                bw.line(4, "break;");
            }
        }
        bw.line(2, "}");
        bw.line(1, "}");
        bw.line(0, "}");
        bw.newLine();
    }

    private void writeHelperCopyFrom(SourceCopyMap source, DtoDef dtoDef, IndentWriter bw) throws IOException {
        //todo Helpers
        //bw.line(0, "@GwtIncompatible");
        //bw.line(0, "public static void copyFrom(").append(dtoDef.name).append(" dto, ");
        //bw.append(source.sourceDef.type).append(" source) {");
        //for (MappedAccessor getter : source.mappedGetters.values()) {
        //    DtoField field = getter.dtoField;
        //    bw.line(1, "dto.set").append(field.getAccessorName()).append("(");
        //    if (getter.getValue() != null) {
        //        bw.append(dtoDef.qualifiedName).append("Def.convert(");
        //    }
        //    bw.append("source.").append(field.getGetAccessorName()).append("()");
        //    if (getter.getValue() != null) {
        //        bw.append(")");
        //    }
        //    bw.append(");");
        //}
        //
        //bw.line(0, "}");
        //bw.newLine();
    }

    private void writeUnmapped(DtoDef dtoDef, IndentWriter bw) throws IOException {
        LinkedList<DtoField> unmapped = new LinkedList<>();
        for (DtoField field : dtoDef.fields.values()) {
            if (field.isStrictCopy(dtoDef) && !field.isSourceMapped()) {
                unmapped.add(field);
            }
        }
        if (!unmapped.isEmpty()) {
            bw.line(0, "public void fieldsNotMappedToSource() {");
            for (DtoField field : unmapped) {
                bw.line(1, "");
                bw.append(field.getFieldName());
                bw.append(";");
            }
            bw.line(0, "}");

        }
    }

    private void writeOtherMethods(DtoDef dtoDef, IndentWriter bw) throws IOException {
        for (ConverterMethod method : dtoDef.methods) {
            if (method.body != null) {
                bw.append(method.body);
                bw.newLine();
            }
        }
    }

}