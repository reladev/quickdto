package org.reladev.quickdto.processor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.tools.JavaFileObject;

import org.reladev.quickdto.feature.QuickDtoFeature2;

import static org.reladev.quickdto.processor.QuickDtoProcessor.processingEnv;
import static org.reladev.quickdto.processor.Reversed.reversed;

public class CodeGenerator {

    public void writeDto(ParsedDtoDef dtoDef) {
        try {
            Type dtoType = dtoDef.getTargetDef().getType();

            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(dtoType.getPackageString() + "." + dtoType.getName());

            IndentWriter bw = new IndentWriter(new BufferedWriter(jfo.openWriter()), "\t");
            bw.append("package ").append(dtoType.getPackageString()).append(";");
            bw.newLine();
            bw.line("import java.util.Arrays;");
            bw.line("import java.util.HashSet;");
            bw.line("import java.util.List;");
            bw.line("import java.util.Map;");
            bw.line("import java.util.Set;");
            bw.line("import java.util.Objects;");
            bw.line("import org.reladev.quickdto.shared.GwtIncompatible;");
            bw.newLine();
            for (Type type : dtoDef.getImports()) {
                bw.line("import ").append(type.getQualifiedName()).append(";");
            }
            bw.newLine();

            for (String annotation : dtoDef.getTypeAnnotationsToCopy()) {
                bw.append(annotation);
                bw.newLine();
            }
            bw.line("public class ").append(dtoType.getName());
            if (dtoDef.getExtendType() != null) {
                bw.append(" extends ").append(dtoDef.getExtendType().getQualifiedName());
            }
            if (!dtoDef.getImplementTypes().isEmpty()) {
                bw.append(" implements ");
                boolean firstPass = true;
                for (Type implement : dtoDef.getImplementTypes()) {
                    if (!firstPass) {
                        bw.append(", ");
                    }

                    bw.append(implement.getQualifiedName());
                    firstPass = false;
                }
            }
            bw.append(" {");
            bw.indent();

            bw.newLine();
            writeFields(dtoDef.getTargetDef(), dtoDef.isFieldAnnotationsOnGetter(), bw);
            for (QuickDtoFeature2 feature : dtoDef.getFeatures()) {
                feature.writeFields(dtoDef, bw);
            }
            bw.newLine();
            writeGettersSetters(dtoDef, bw);
            for (QuickDtoFeature2 feature : dtoDef.getFeatures()) {
                feature.writeMethods(dtoDef, bw);
            }
            writeEqualsHash(dtoDef.getTargetDef(), bw);
            writeCopyMethods(dtoDef, bw);
            writeFieldsEnum(dtoDef.getTargetDef(), bw);
            for (QuickDtoFeature2 feature : dtoDef.getFeatures()) {
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

    //private void writeHelper(HelperDef helperDef) {
    //    try {
    //        JavaFileObject jfo = processingEnv.getFiler().createSourceFile(helperDef.getPackageString() + "." + helperDef.getName() + HelperSuffix);
    //
    //        IndentWriter bw = new IndentWriter(new BufferedWriter(jfo.openWriter()), "\t");
    //        bw.append("package ").append(helperDef.getPackageString()).append(";");
    //        bw.newLine();
    //        bw.line("import java.util.Arrays;");
    //        bw.line("import java.util.HashSet;");
    //        bw.line("import java.util.List;");
    //        bw.line("import java.util.Map;");
    //        bw.line("import java.util.Set;");
    //        bw.line("import java.util.Objects;");
    //        bw.line("import org.reladev.quickdto.shared.GwtIncompatible;");
    //        bw.newLine();
    //
    //        bw.newLine();
    //        bw.line("public class ").append(helperDef.name).append(HelperSuffix).append(" {");
    //        bw.indent();
    //        bw.newLine();
    //
    //        writeHelperCopyMethods(helperDef, bw);
    //
    //        bw.newLine();
    //
    //        writeFieldsEnum(helperDef, bw);
    //
    //        bw.unindent();
    //        bw.line("}");
    //
    //        bw.flush();
    //        bw.close();
    //
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    }
    //}

    private void writeFieldsEnum(ClassDef2 classDef, IndentWriter bw) throws IOException {
        bw.line(0, "public static enum Fields {");
        bw.startLineList(",");
        for (Field2 field : classDef.getNameFieldMap().values()) {
            bw.line(1, "").append(field.getEnumName()).append("(\"").append(field.getName()).append("\")");
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

    private void writeFields(ClassDef2 classDef, boolean fieldAnnotationsOnGetter, IndentWriter bw) throws IOException {
        for (Field2 field : classDef.getNameFieldMap().values()) {
            Type type = field.getType();
            if (!fieldAnnotationsOnGetter) {
                for (String annotation : field.getFieldAnnotations()) {
                    bw.line(0, "").append(annotation);
                }
            }
            if (type.isPrimitive()) {
                bw.line(0, "private ").append(type.getPrimitiveBoxType()).append(" ").append(field.getName()).append(";");
            } else {
                bw.line(0, "private ").append(type.getQualifiedName()).append(" ").append(field.getName()).append(";");
            }
        }
    }

    private void writeGettersSetters(ParsedDtoDef parsedDtoDef, IndentWriter bw) throws IOException {
        ClassDef2 classDef = parsedDtoDef.getTargetDef();
        for (Field2 field : classDef.getNameFieldMap().values()) {
            Type type = field.getType();
            if (parsedDtoDef.isFieldAnnotationsOnGetter()) {
                for (String annotation : field.getFieldAnnotations()) {
                    bw.line("").append(annotation).append("");
                }
            }
            // todo double check if this is still needed
            //for (String annotation : field.getGetterAnnotations()) {
            //    bw.line("").append(annotation).append("");
            //}
            bw.line("public ").append(type.getQualifiedName()).append(" ").append(field.getAccessorName()).append("() {");

            bw.indent();
            for (QuickDtoFeature2 feature : parsedDtoDef.getFeatures()) {
                feature.preGetterLogic(field, parsedDtoDef, bw);
            }
            if (type.isPrimitive()) {
                bw.line(0, "if (").append(field.getName()).append(" != null) {");
                bw.line(1, "return ").append(field.getName()).append(";");
                bw.line(0, "} else {");
                bw.line(1, "return ").append(type.getDefaultPrimitiveValue()).append(";");
                bw.line(0, "}");

            } else {
                bw.line(0, "return ").append(field.getName()).append(";");
            }
            for (QuickDtoFeature2 feature : reversed(parsedDtoDef.getFeatures())) {
                feature.postGetterLogic(field, parsedDtoDef, bw);
            }
            bw.unindent();

            bw.line("}");
            bw.newLine();
            if (!field.getFlags().isExcludeSetter()) {
                // todo double check if this is still needed
                //for (String annotation : field.getSetterAnnotations()) {
                //    bw.line("").append(annotation).append("");
                //}
                bw.line("public void set").append(field.getAccessorName()).append("(").append(type.getName()).append(" ").append(field.getName()).append(") {");

                bw.indent();
                for (QuickDtoFeature2 feature : parsedDtoDef.getFeatures()) {
                    feature.preSetterLogic(field, parsedDtoDef, bw);
                }
                bw.line("this.").append(field.getName()).append(" = ").append(field.getName()).append(";");
                for (QuickDtoFeature2 feature : reversed(parsedDtoDef.getFeatures())) {
                    feature.postSetterLogic(field, parsedDtoDef, bw);
                }
                bw.unindent();

                bw.line("}");
                bw.newLine();
            }
        }
    }

    private void writeEqualsHash(ClassDef2 dtoDef, IndentWriter bw) throws IOException {
        List<Field2> equalsFields = new LinkedList<>();
        for (Field2 field : dtoDef.getNameFieldMap().values()) {
            if (field.getFlags().isEqualsHashCode()) {
                equalsFields.add(field);
            }
        }
        Collection<Field2> genFields;
        if (equalsFields.isEmpty()) {
            genFields = dtoDef.getNameFieldMap().values();

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
        bw.line(1, "").append(dtoDef.getType().getName()).append(" that = (").append(dtoDef.getType().getName()).append(") o;");
        bw.newLine();

        for (Field2 field : genFields) {
            Type type = field.getType();
            if (type.getName().equals("float")) {
                bw.line(1, "if (Float.compare(").append(field.getName()).append(", that.").append(field.getName()).append(") != 0) {");
            } else if (type.getName().equals("double")) {
                bw.line(1, "if (Double.compare(").append(field.getName()).append(", that.").append(field.getName()).append(") != 0) {");
            } else if (type.isPrimitive()) {
                bw.line(1, "if (").append(field.getName()).append(" != that.").append(field.getName()).append(") {");
            } else if (type.getName().endsWith("[]")) {
                bw.line(1, "if (Arrays.equals(").append(field.getName()).append(", that.").append(field.getName()).append(")) {");
            } else {
                bw.line(1, "if (").append(field.getName()).append(" != null ? !").append(field.getName()).append(".equals(that.").append(field.getName()).append(") : that.").append(field.getName()).append(" != null) {");
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
        for (Field2 field : genFields) {
            Type type = field.getType();
            if (type.getName().equals("double")) {
                bw.line(1, "_l_ = Double.doubleToLongBits(").append(field.getName()).append(");");
            }
            if (first) {
                bw.line(1, "int _r_ = ");
                first = false;
            } else {
                bw.line(1, "_r_ = 31 * _r_ + ");
            }

            if (type.getName().equals("long")) {
                bw.append("(int) (").append(field.getName()).append(" ^ (").append(field.getName()).append(" >>> 32));");
            } else if (type.getName().equals("float")) {
                bw.append("(").append(field.getName()).append(" != +0.0f ? Float.floatToIntBits(").append(field.getName()).append(") : 0);");
            } else if (type.getName().equals("double")) {
                bw.append("(int) (_l_ ^ (_l_ >>> 32));");
            } else if (type.getName().equals("boolean")) {
                bw.append("(").append(field.getName()).append(" ? 1 : 0);");
            } else if (type.isPrimitive()) {
                bw.append(field.getName()).append(";");
            } else {
                bw.append("(").append(field.getName()).append(" != null ? ").append(field.getName()).append(".hashCode() : 0);");
            }
        }

        bw.line(1, "return _r_;");
        bw.line(0, "}");
        bw.newLine();
    }

    private void writeCopyMethods(ParsedDtoDef dtoDef, IndentWriter bw) throws IOException {
        if (!dtoDef.getCopyMaps().isEmpty()) {
            for (CopyMap2 copyMap : dtoDef.getCopyMaps()) {
                writeCopyToSource(copyMap, dtoDef, bw);
                for (QuickDtoFeature2 feature : dtoDef.getFeatures()) {
                    feature.writeCopyTo(copyMap, dtoDef, bw);
                }

                writeCopyFromSource(copyMap, dtoDef, bw);
                for (QuickDtoFeature2 feature : dtoDef.getFeatures()) {
                    feature.writeCopyFrom(copyMap, dtoDef, bw);
                }
            }
        }
    }

    private void writeCopyToSource(CopyMap2 copyMap, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException {
        Type sourceType = copyMap.getSourceDef().getType();
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public void copyTo(").append(sourceType.getName()).append(" dest, Fields... fields) {");
        bw.line(1, "if (fields.length > 0) {");
        bw.line(2, "copyTo(dest, Arrays.asList(fields));");
        bw.line(1, "} else {");
        bw.line(2, "copyTo(dest, Arrays.asList(Fields.values()));");
        bw.line(1, "}");
        bw.line(0, "}");
        bw.newLine();
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public void copyTo(").append(sourceType.getName()).append(" dest, Iterable<Fields> fields) {");
        bw.line(1, "for (Fields field: fields) {");
        bw.line(2, "switch (field) {");
        for (CopyMapping mapping : copyMap.getTargetToSourceMappings().values()) {
            Field2 getField = mapping.getGetField();
            Field2 setField = mapping.getSetField();
            // todo look at moving this to CopyMap
            if (!getField.getFlags().isCopyFrom()) {
                bw.line(3, "case ").append(getField.getEnumName()).append(":");
                bw.line(4, "dest.set").append(setField.getAccessorName()).append("(");
                ConverterMethod2 converter = mapping.getConverterMethod();
                if (converter != null) {
                    bw.append(converter.getClassType().getName()).append(".convert(").append(getField.getName());
                    if (converter.isExistingParam()) {
                        bw.append(", dest.get").append(setField.getAccessorName()).append("()");
                    }
                    bw.append(")");
                } else {
                    bw.append(getField.getName());
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

    private void writeCopyFromSource(CopyMap2 copyMap, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException {
        Type sourceType = copyMap.getSourceDef().getType();
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public void copyFrom(").append(sourceType.getName()).append(" source) {");
        for (CopyMapping mapping : copyMap.getSourceToTargetMappings().values()) {
            Field2 getField = mapping.getGetField();
            Field2 setField = mapping.getSetField();
            bw.line(1, setField.getName()).append(" = ");
            ConverterMethod2 converter = mapping.getConverterMethod();
            if (converter != null) {
                bw.append("source.").append(getField.getFullGetAccessorName()).append("() == null ? null : ").append(converter.getClassType().getName()).append(".convert(");
                bw.append("source.").append(getField.getFullGetAccessorName()).append("()");
                if (converter.isExistingParam()) {
                    bw.append(", ").append(setField.getName());
                }
                bw.append(");");

            } else if (mapping.isQuickDtoConvert()) {
                bw.append("source.").append(getField.getFullGetAccessorName()).append("() == null ? null : new ").append(setField.getType().getName()).append("();");
                bw.line(1, "if (").append(setField.getName()).append(" != null) {");
                bw.line(2, setField.getName()).append(".copyFrom(");
                bw.append("source.").append(getField.getFullGetAccessorName()).append("());");
                bw.line(1, "}");

            } else if (mapping.isQuickDtoListConvert()) {
                bw.append("source.").append(getField.getFullGetAccessorName()).append("() == null ? null : new java.util.ArrayList<>();");
                bw.line(1, "if (").append(setField.getName()).append(" != null) {");
                bw.line(2, "for (").append(getField.getType().getListType().getName()).append(" _i_: source.").append(getField.getFullGetAccessorName()).append("()) {");
                bw.line(3, setField.getType().getListType().getName()).append(" _d_ = _i_ == null ? null : new ").append(setField.getType().getListType().getName()).append("();");
                bw.line(3, setField.getName()).append(".add(_d_);");
                bw.line(3, "if (_d_ != null) {");
                bw.line(4, "_d_.copyFrom(_i_);");
                bw.line(3, "}");
                bw.line(2, "}");
                bw.line(1, "}");

            } else {
                bw.append("source.").append(getField.getFullGetAccessorName()).append("();");
            }
        }

        bw.line(0, "}");
        bw.newLine();
    }

    //private void writeHelperCopyMethods(HelperDef helperDef, IndentWriter bw) throws IOException {
    //    if (!helperDef.getCopyMaps().isEmpty()) {
    //        for (CopyMap source : helperDef.getCopyMaps()) {
    //            writeHelperCopyTo(source, helperDef, bw);
    //            //for (QuickDtoFeature feature : helperDef.features) {
    //            //    feature.writeHelperCopyTo(source, helperDef, bw);
    //            //}
    //
    //            writeHelperCopyFrom(source, helperDef, bw);
    //            //for (QuickDtoFeature feature : helperDef.features) {
    //            //    feature.writeHelperCopyFrom(source, helperDef, bw);
    //            //}
    //        }
    //        if (helperDef.isStrictCopy()) {
    //            writeUnmapped(helperDef, bw);
    //        }
    //    }
    //}

    //private void writeHelperCopyTo(CopyMap source, ClassDef dtoDef, IndentWriter bw) throws IOException {
    //    bw.line(0, "@GwtIncompatible");
    //    bw.line(0, "public static void copy(").append(dtoDef.name).append(" source, ");
    //    bw.append(source.sourceDef.type).append(" dest, Fields... fields) {");
    //    bw.line(1, "if (fields.length > 0) {");
    //    bw.line(2, "copy(source, dest, Arrays.asList(fields));");
    //    bw.line(1, "} else {");
    //    bw.line(2, "copy(source, dest, Fields.values());");
    //    bw.line(1, "}");
    //    bw.line(0, "}");
    //    bw.newLine();
    //    bw.line(0, "@GwtIncompatible");
    //    bw.line(0, "public static void copy(").append(dtoDef.name).append(" source, ");
    //    bw.append(source.sourceDef.type).append(" dest, Iterable<Fields> fields) {");
    //    bw.line(1, "for (Fields field: fields) {");
    //    bw.line(2, "switch (field) {");
    //    for (MappedAccessor setter : source.mappedSetters.values()) {
    //        Field field = setter.field;
    //        if (!field.getFlags().isCopyFrom()) {
    //            bw.line(3, "case ").append(field.getEnumName()).append(":");
    //            bw.line(4, "dest.set").append(field.getAccessorName()).append("(");
    //            if (setter.converterMethod != null) {
    //                bw.append(dtoDef.qualifiedName).append("Def.convert(source.").append(field.getGetAccessorName()).append("())");
    //            } else {
    //                bw.append("source.").append(field.getGetAccessorName()).append("()");
    //            }
    //
    //            bw.append(");");
    //            bw.line(4, "break;");
    //        }
    //    }
    //    bw.line(2, "}");
    //    bw.line(1, "}");
    //    bw.line(0, "}");
    //    bw.newLine();
    //}

    //private void writeHelperCopyFrom(CopyMap source, ClassDef dtoDef, IndentWriter bw) throws IOException {
    //    bw.line(0, "@GwtIncompatible");
    //    bw.line(0, "public static void copy(").append(source.sourceDef.type).append(" source, ");
    //    bw.append(dtoDef.name).append(" dest) {");
    //    for (MappedAccessor getter : source.mappedGetters.values()) {
    //        Field field = getter.field;
    //        bw.line(1, "dest.set").append(field.getAccessorName()).append("(");
    //        if (getter.converterMethod != null) {
    //            bw.append(dtoDef.qualifiedName).append("Def.convert(");
    //        }
    //        bw.append("source.").append(field.getGetAccessorName()).append("()");
    //        if (getter.converterMethod != null) {
    //            bw.append(")");
    //        }
    //        bw.append(");");
    //    }
    //
    //    bw.line(0, "}");
    //    bw.newLine();
    //}
}