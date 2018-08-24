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
            HelperDef helperDef = classAnalyzer.createHelperDef(element, defs);
            writeHelper(helperDef);
        }

        return true;
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

    private void writeHelper(HelperDef helperDef) {
        try {
            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(helperDef.getPackageString() + "." + helperDef.getName() + HelperSuffix);

            IndentWriter bw = new IndentWriter(new BufferedWriter(jfo.openWriter()), "\t");
            bw.append("package ").append(helperDef.getPackageString()).append(";");
            bw.newLine();
            bw.line("import java.util.Arrays;");
            bw.line("import java.util.HashSet;");
            bw.line("import java.util.List;");
            bw.line("import java.util.Map;");
            bw.line("import java.util.Set;");
            bw.line("import java.util.Objects;");
            bw.line("import org.reladev.quickdto.shared.GwtIncompatible;");
            bw.newLine();

            bw.newLine();
            bw.line("public class ").append(helperDef.name).append(HelperSuffix).append(" {");
            bw.indent();
            bw.newLine();

            writeHelperCopyMethods(helperDef, bw);

            bw.newLine();

            writeFieldsEnum(helperDef, bw);

            bw.unindent();
            bw.line("}");

            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFieldsEnum(ClassDef classDef, IndentWriter bw) throws IOException {
        bw.line(0, "public static enum Fields {");
        bw.startLineList(",");
        for (Field field : classDef.getFields().values()) {
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
        for (Field field : dtoDef.getFields().values()) {
            DtoField dtoField = (DtoField) field;
            if (!dtoDef.fieldAnnotationsOnGetter) {
                for (String annotation : dtoField.getFieldAnnotations()) {
                    bw.line(0, "").append(annotation);
                }
            }
            if (dtoField.isPrimitive()) {
                bw.line(0, "private ").append(dtoField.getPrimitiveTypeString()).append(" ").append(dtoField.getFieldName()).append(";");
            } else {
                bw.line(0, "private ").append(dtoField.getTypeString()).append(" ").append(dtoField.getFieldName()).append(";");
            }
        }
    }

    private void writeGettersSetters(DtoDef dtoDef, IndentWriter bw) throws IOException {
        for (Field field : dtoDef.getFields().values()) {
            DtoField dtoField = (DtoField) field;
            if (dtoDef.fieldAnnotationsOnGetter) {
                for (String annotation : dtoField.getFieldAnnotations()) {
                    bw.line("").append(annotation).append("");
                }
            }
            for (String annotation : dtoField.getGetterAnnotations()) {
                bw.line("").append(annotation).append("");
            }
            bw.line("public ").append(dtoField.getTypeString()).append(" ").append(dtoField.getGetAccessorName()).append("() {");

            bw.indent();
            for (QuickDtoFeature feature : dtoDef.features) {
                feature.preGetterLogic(dtoField, dtoDef, bw);
            }
            if (dtoField.isPrimitive()) {
                bw.line(0, "if (").append(dtoField.getFieldName()).append(" != null) {");
                bw.line(1, "return ").append(dtoField.getFieldName()).append(";");
                bw.line(0, "} else {");
                bw.line(1, "return ").append(dtoField.getDefaultValue()).append(";");
                bw.line(0, "}");

            } else {
                bw.line(0, "return ").append(dtoField.getFieldName()).append(";");
            }
            for (QuickDtoFeature feature : reversed(dtoDef.features)) {
                feature.postGetterLogic(dtoField, dtoDef, bw);
            }
            bw.unindent();

            bw.line("}");
            bw.newLine();
            if (!dtoField.getFlags().isExcludeSetter()) {
                for (String annotation : dtoField.getSetterAnnotations()) {
                    bw.line("").append(annotation).append("");
                }
                bw.line("public void set").append(dtoField.getAccessorName()).append("(").append(dtoField.getTypeString()).append(" ").append(dtoField.getFieldName()).append(") {");

                bw.indent();
                for (QuickDtoFeature feature : dtoDef.features) {
                    feature.preSetterLogic(dtoField, dtoDef, bw);
                }
                bw.line("this.").append(dtoField.getFieldName()).append(" = ").append(dtoField.getFieldName()).append(";");
                for (QuickDtoFeature feature : reversed(dtoDef.features)) {
                    feature.postSetterLogic(dtoField, dtoDef, bw);
                }
                bw.unindent();

                bw.line("}");
                bw.newLine();
            }
        }
    }

    private void writeEqualsHash(DtoDef dtoDef, IndentWriter bw) throws IOException {
        List<Field> equalsFields = new LinkedList<>();
        for (Field field : dtoDef.getFields().values()) {
            if (field.getFlags().isEqualsHashCode()) {
                equalsFields.add(field);
            }
        }
        Collection<Field> genFields;
        if (equalsFields.isEmpty()) {
            genFields = dtoDef.getFields().values();

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

        for (Field field : genFields) {
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
        for (Field field : genFields) {
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
        if (!dtoDef.getCopyMaps().isEmpty()) {
            for (CopyMap source : dtoDef.getCopyMaps()) {
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

    private void writeCopyTo(CopyMap source, DtoDef dtoDef, IndentWriter bw) throws IOException {
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public void copyTo(").append(source.sourceDef.type).append(" dest, Fields... fields) {");
        bw.line(1, "if (fields.length > 0) {");
        bw.line(2, "copyTo(dest, Arrays.asList(fields));");
        bw.line(1, "} else {");
        bw.line(2, "copyTo(dest, Arrays.asList(Fields.values()));");
        bw.line(1, "}");
        bw.line(0, "}");
        bw.newLine();
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public void copyTo(").append(source.sourceDef.type).append(" dest, Iterable<Fields> fields) {");
        bw.line(1, "for (Fields field: fields) {");
        bw.line(2, "switch (field) {");
        for (MappedAccessor setter : source.mappedSetters.values()) {
            Field field = setter.field;
            if (!field.getFlags().isCopyFrom()) {
                bw.line(3, "case ").append(field.getEnumName()).append(":");
                bw.line(4, "dest.set").append(field.getAccessorName()).append("(");
                ConverterMethod converter = setter.converterMethod;
                if (converter != null) {
                    bw.append(converter.classTypeString).append(".convert(").append(field.getFieldName());
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

    private void writeCopyFrom(CopyMap source, DtoDef dtoDef, IndentWriter bw) throws IOException {
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public void copyFrom(").append(source.sourceDef.type).append(" source) {");
        for (MappedAccessor getter : source.mappedGetters.values()) {
            Field field = getter.field;
            bw.line(1, field.getFieldName()).append(" = ");
            ConverterMethod converter = getter.converterMethod;
            if (converter != null) {
                bw.append("source.").append(field.getGetAccessorName()).append("() == null ? null : ").append(converter.classTypeString).append(".convert(");
                bw.append("source.").append(field.getGetAccessorName()).append("()");
                if (converter.existingParam) {
                    bw.append(", ").append(field.getFieldName());
                }
                bw.append(");");

            } else if (field.isQuickDto()) {
                bw.append("source.").append(field.getGetAccessorName()).append("() == null ? null : new ").append(field.getTypeString()).append("();");
                bw.line(1, "if (").append(field.getFieldName()).append(" != null) {");
                bw.line(2, field.getFieldName()).append(".copyFrom(");
                bw.append("source.").append(field.getGetAccessorName()).append("());");
                bw.line(1, "}");

            } else if (field.isQuickDtoList()) {
                bw.append("source.").append(field.getGetAccessorName()).append("() == null ? null : new java.util.ArrayList<>();");
                bw.line(1, "if (").append(field.getFieldName()).append(" != null) {");
                bw.line(2, "for (").append(getter.accessorMethod.getListTypeString()).append(" _i_: source.").append(field.getGetAccessorName()).append("()) {");
                bw.line(3, field.getListTypeString()).append(" _d_ = _i_ == null ? null : new ").append(field.getListTypeString()).append("();");
                bw.line(3, field.getFieldName()).append(".add(_d_);");
                bw.line(3, "if (_d_ != null) {");
                bw.line(4, "_d_.copyFrom(_i_);");
                bw.line(3, "}");
                bw.line(2, "}");
                bw.line(1, "}");

            } else {
                bw.append("source.").append(field.getGetAccessorName()).append("();");
            }
        }

        bw.line(0, "}");
        bw.newLine();
    }

    private void writeHelperCopyMethods(HelperDef helperDef, IndentWriter bw) throws IOException {
        if (!helperDef.getCopyMaps().isEmpty()) {
            for (CopyMap source : helperDef.getCopyMaps()) {
                writeHelperCopyTo(source, helperDef, bw);
                //for (QuickDtoFeature feature : helperDef.features) {
                //    feature.writeHelperCopyTo(source, helperDef, bw);
                //}

                writeHelperCopyFrom(source, helperDef, bw);
                //for (QuickDtoFeature feature : helperDef.features) {
                //    feature.writeHelperCopyFrom(source, helperDef, bw);
                //}
            }
            if (helperDef.isStrictCopy()) {
                writeUnmapped(helperDef, bw);
            }
        }
    }

    private void writeHelperCopyTo(CopyMap source, ClassDef dtoDef, IndentWriter bw) throws IOException {
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public static void copy(").append(dtoDef.name).append(" source, ");
        bw.append(source.sourceDef.type).append(" dest, Fields... fields) {");
        bw.line(1, "if (fields.length > 0) {");
        bw.line(2, "copy(source, dest, Arrays.asList(fields));");
        bw.line(1, "} else {");
        bw.line(2, "copy(source, dest, Fields.values());");
        bw.line(1, "}");
        bw.line(0, "}");
        bw.newLine();
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public static void copy(").append(dtoDef.name).append(" source, ");
        bw.append(source.sourceDef.type).append(" dest, Iterable<Fields> fields) {");
        bw.line(1, "for (Fields field: fields) {");
        bw.line(2, "switch (field) {");
        for (MappedAccessor setter : source.mappedSetters.values()) {
            Field field = setter.field;
            if (!field.getFlags().isCopyFrom()) {
                bw.line(3, "case ").append(field.getEnumName()).append(":");
                bw.line(4, "dest.set").append(field.getAccessorName()).append("(");
                if (setter.converterMethod != null) {
                    bw.append(dtoDef.qualifiedName).append("Def.convert(source.").append(field.getGetAccessorName()).append("())");
                } else {
                    bw.append("source.").append(field.getGetAccessorName()).append("()");
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

    private void writeHelperCopyFrom(CopyMap source, ClassDef dtoDef, IndentWriter bw) throws IOException {
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public static void copy(").append(source.sourceDef.type).append(" source, ");
        bw.append(dtoDef.name).append(" dest) {");
        for (MappedAccessor getter : source.mappedGetters.values()) {
            Field field = getter.field;
            bw.line(1, "dest.set").append(field.getAccessorName()).append("(");
            if (getter.converterMethod != null) {
                bw.append(dtoDef.qualifiedName).append("Def.convert(");
            }
            bw.append("source.").append(field.getGetAccessorName()).append("()");
            if (getter.converterMethod != null) {
                bw.append(")");
            }
            bw.append(");");
        }

        bw.line(0, "}");
        bw.newLine();
    }

    private void writeUnmapped(ClassDef dtoDef, IndentWriter bw) throws IOException {
        bw.line(0, "public void fieldsNotMappedToSource() {");

        for (Field field : dtoDef.getFields().values()) {
            if (!field.isSourceMapped()) {
                if (field.getFlags().isStrictCopy(dtoDef)) {
                    bw.line(1, "//").append(field.getFieldName()).append(";");
                } else {
                    bw.line(1, field.getFieldName()).append(";");

                }
            }
        }

        bw.line(0, "}");
        bw.newLine();
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