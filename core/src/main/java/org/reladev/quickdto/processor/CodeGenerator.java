package org.reladev.quickdto.processor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.tools.JavaFileObject;

import org.reladev.quickdto.feature.QuickDtoFeature;

import static org.reladev.quickdto.processor.QuickDtoProcessor.HelperSuffix;
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
                if (type.isImportable()) {
                    bw.line("import ").append(type.getQualifiedName()).append(";");
                }
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
            writeFields(dtoDef.getTargetDef(), dtoDef.isFieldAnnotationsOnGetter(), dtoDef, bw);
            for (QuickDtoFeature feature: dtoDef.getFeatures()) {
                feature.writeFields(dtoDef, bw);
            }
            bw.newLine();
            writeGettersSetters(dtoDef, bw);
            for (QuickDtoFeature feature: dtoDef.getFeatures()) {
                feature.writeMethods(dtoDef, bw);
            }
            writeEqualsHash(dtoDef.getTargetDef(), bw);
            writeCopyMethods(dtoDef, bw);
            writeFieldsEnum(dtoDef.getTargetDef(), bw);
            for (QuickDtoFeature feature: dtoDef.getFeatures()) {
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

    public void writeHelper(ParsedHelperDef helperDef) {
        try {
            Type sourceType = helperDef.getSourceDef().getType();

            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(sourceType.getPackageString() + "." + sourceType.getName() + HelperSuffix);

            IndentWriter bw = new IndentWriter(new BufferedWriter(jfo.openWriter()), "\t");
            bw.append("package ").append(sourceType.getPackageString()).append(";");
            bw.newLine();
            bw.line("import java.util.Arrays;");
            bw.line("import java.util.HashSet;");
            bw.line("import java.util.List;");
            bw.line("import java.util.Map;");
            bw.line("import java.util.Set;");
            bw.line("import java.util.Objects;");
            bw.line("import org.reladev.quickdto.shared.GwtIncompatible;");
            bw.newLine();
            for (Type type: helperDef.getImports()) {
                if (type.isImportable()) {
                    bw.line("import ").append(type.getQualifiedName()).append(";");
                }
            }

            bw.newLine();
            bw.line("public class ").append(sourceType.getName()).append(HelperSuffix).append(" {");
            bw.indent();
            bw.newLine();

            writeHelperCopyMethods(helperDef, bw);

            bw.newLine();

            writeFieldsEnum(helperDef.getSourceDef(), bw);

            bw.unindent();
            bw.line("}");

            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFieldsEnum(ClassDef classDef, IndentWriter bw) {
        bw.line(0, "public static enum Fields {");
        bw.startLineList(",");
        for (Field field: classDef.getNameFieldMap().values()) {
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

    private void writeFields(ClassDef classDef, boolean fieldAnnotationsOnGetter, ParsedDtoDef dtoDef, IndentWriter bw) {
        for (Field field: classDef.getNameFieldMap().values()) {
            Type type = field.getType();
            if (!fieldAnnotationsOnGetter) {
                for (String annotation : field.getFieldAnnotations()) {
                    bw.line(0, "").append(annotation);
                }
            }
            if (type.isPrimitive()) {
                bw.line(0, "private ").append(type.getPrimitiveBoxType()).append(" ").append(field.getName()).append(";");
            } else {
                bw.line(0, "private ").append(dtoDef.getImportSafeType(type)).append(" ").append(field.getName()).append(";");
            }
        }
    }

    private void writeGettersSetters(ParsedDtoDef parsedDtoDef, IndentWriter bw) throws IOException {
        ClassDef classDef = parsedDtoDef.getTargetDef();
        for (Field field: classDef.getNameFieldMap().values()) {
            Type type = field.getType();
            if (parsedDtoDef.isFieldAnnotationsOnGetter()) {
                for (String annotation : field.getFieldAnnotations()) {
                    bw.line("").append(annotation).append("");
                }
            }
            bw.line("public ").append(parsedDtoDef.getImportSafeType(type)).append(" ").append(field.getFullGetAccessorName()).append("() {");

            bw.indent();
            for (QuickDtoFeature feature: parsedDtoDef.getFeatures()) {
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
            for (QuickDtoFeature feature: reversed(parsedDtoDef.getFeatures())) {
                feature.postGetterLogic(field, parsedDtoDef, bw);
            }
            bw.unindent();

            bw.line("}");
            bw.newLine();
            if (!field.isExcludeSetter()) {
                bw.line("public void set")
                      .append(field.getAccessorName())
                      .append("(")
                      .append(parsedDtoDef.getImportSafeType(type))
                      .append(" ")
                      .append(field.getName())
                      .append(") {");

                bw.indent();
                for (QuickDtoFeature feature: parsedDtoDef.getFeatures()) {
                    feature.preSetterLogic(field, parsedDtoDef, bw);
                }
                bw.line("this.").append(field.getName()).append(" = ").append(field.getName()).append(";");
                for (QuickDtoFeature feature: reversed(parsedDtoDef.getFeatures())) {
                    feature.postSetterLogic(field, parsedDtoDef, bw);
                }
                bw.unindent();

                bw.line("}");
                bw.newLine();
            }
        }
    }

    private void writeEqualsHash(ClassDef dtoDef, IndentWriter bw) {
        List<Field> equalsFields = new LinkedList<>();
        for (Field field: dtoDef.getNameFieldMap().values()) {
            if (field.isEqualsHashCode()) {
                equalsFields.add(field);
            }
        }
        Collection<Field> genFields;
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

        for (Field field: genFields) {
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
        for (Field field: genFields) {
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
            for (CopyMap copyMap: dtoDef.getCopyMaps()) {
                writeCopyToSource(copyMap, dtoDef, bw);
                for (QuickDtoFeature feature: dtoDef.getFeatures()) {
                    feature.writeCopyTo(copyMap, dtoDef, bw);
                }

                writeCopyFromSource(copyMap, dtoDef, bw);
                for (QuickDtoFeature feature: dtoDef.getFeatures()) {
                    feature.writeCopyFrom(copyMap, dtoDef, bw);
                }
            }
        }
    }

    private void writeCopyToSource(CopyMap copyMap, ParsedDtoDef dtoDef, IndentWriter bw) {
        Type sourceType = copyMap.getSourceDef().getType();
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public void copyTo(").append(dtoDef.getImportSafeType(sourceType)).append(" dest, Fields... fields) {");
        bw.line(1, "if (fields.length > 0) {");
        bw.line(2, "copyTo(dest, Arrays.asList(fields));");
        bw.line(1, "} else {");
        bw.line(2, "copyTo(dest, Arrays.asList(Fields.values()));");
        bw.line(1, "}");
        bw.line(0, "}");
        bw.newLine();
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public void copyTo(").append(dtoDef.getImportSafeType(sourceType)).append(" dest, Iterable<Fields> fields) {");
        bw.line(1, "for (Fields field: fields) {");
        bw.line(2, "switch (field) {");
        for (CopyMapping mapping : copyMap.getTargetToSourceMappings().values()) {
            Field getField = mapping.getGetField();
            Field setField = mapping.getSetField();
            bw.line(3, "case ").append(getField.getEnumName()).append(":");
            if (mapping.getErrorMessage() != null) {
                bw.line(4, "// ").append(mapping.getErrorMessage());
            }

            ConverterMethod converter = mapping.getConverterMethod();
            if (converter != null) {
                if (mapping.isCollectionConvert()) {
                    bw.append(" {");
                    bw.line(4, dtoDef.getImportSafeType(setField.getType()))
                          .append(" _l_ = ")
                          .append(getField.getName())
                          .append(" == null ? null : new java.util.ArrayList<>();");
                    bw.line(4, "dest.");
                    writeSet(setField, bw, () -> {
                        bw.append("_l_");
                    });
                    bw.append(";");

                    bw.line(4, "if (_l_ != null) {");
                    bw.line(5, "for (").append(dtoDef.getImportSafeType(getField.getType().getListType()));
                    bw.append(" _i_: ").append(getField.getName()).append(") {");
                    bw.line(6, "_l_.add(")
                          .append(converter.getClassType().getOriginalName())
                          .append(".")
                          .append(converter.getName())
                          .append("(_i_));");
                    bw.line(5, "}");
                    bw.line(4, "}");
                    bw.line(4, "break;");
                    bw.line(3, "}");

                } else {
                    bw.line(4, "dest.");
                    writeSet(setField, bw, () -> {
                        bw.append(converter.getClassType().getOriginalName())
                              .append(".")
                              .append(converter.getName())
                              .append("(")
                              .append(getField.getName());
                        if (converter.isExistingParam()) {
                            bw.append(", dest.").append(generateGet(setField));
                        }
                        bw.append(")");
                    });
                    bw.append(";");
                    bw.line(4, "break;");
                }

            } else {
                bw.line(4, "dest.");
                writeSet(setField, bw, () -> {
                    if (getField.getType().isPrimitive()) {
                        bw.append(getField.getName()).append(" != null ? ").append(getField.getName()).append(" : ").append(getField.getType()
                              .getDefaultPrimitiveValue());

                    } else {
                        bw.append(getField.getName());
                    }
                });
                bw.append(";");
                bw.line(4, "break;");
            }
        }
        bw.line(2, "}");
        bw.line(1, "}");
        bw.line(0, "}");
        bw.newLine();
    }

    private void writeCopyFromSource(CopyMap copyMap, ParsedDtoDef dtoDef, IndentWriter bw) {
        Type sourceType = copyMap.getSourceDef().getType();
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public void copyFrom(").append(dtoDef.getImportSafeType(sourceType)).append(" source) {");
        for (CopyMapping mapping : copyMap.getSourceToTargetMappings().values()) {
            Field getField = mapping.getGetField();
            Field setField = mapping.getSetField();
            bw.line(1, setField.getName()).append(" = ");
            ConverterMethod converter = mapping.getConverterMethod();
            if (converter != null) {
                bw.append("source.").append(generateGet(getField)).append(" == null ? null : ");
                bw.append(converter.getClassType().getOriginalName()).append(".").append(converter.getName()).append("(");
                bw.append("source.").append(generateGet(getField));
                if (converter.isExistingParam()) {
                    bw.append(", ").append(setField.getName());
                }
                bw.append(");");

            } else if (mapping.isQuickDtoConvert()) {
                bw.append("source.")
                      .append(generateGet(getField))
                      .append(" == null ? null : new ").append(dtoDef.getImportSafeType(setField.getType()))
                      .append("();");
                bw.line(1, "if (").append(setField.getName()).append(" != null) {");
                bw.line(2, setField.getName()).append(".copyFrom(");
                bw.append("source.").append(generateGet(getField)).append(");");
                bw.line(1, "}");

            } else if (mapping.isQuickDtoListConvert()) {
                bw.append("source.").append(generateGet(getField)).append(" == null ? null : new java.util.ArrayList<>();");
                bw.line(1, "if (").append(setField.getName()).append(" != null) {");
                bw.line(2, "for (").append(dtoDef.getImportSafeType(getField.getType().getListType()))
                      .append(" _i_: source.")
                      .append(generateGet(getField))
                      .append(") {");
                bw.line(3, dtoDef.getImportSafeType(setField.getType().getListType()))
                      .append(" _d_ = _i_ == null ? null : new ")
                      .append(dtoDef.getImportSafeType(setField.getType().getListType()))
                      .append("();");
                bw.line(3, setField.getName()).append(".add(_d_);");
                bw.line(3, "if (_d_ != null) {");
                bw.line(4, "_d_.copyFrom(_i_);");
                bw.line(3, "}");
                bw.line(2, "}");
                bw.line(1, "}");

            } else {
                bw.append("source.").append(generateGet(getField)).append(";");
            }
        }

        bw.line(0, "}");
        bw.newLine();
    }

    private void writeHelperCopyMethods(ParsedHelperDef helperDef, IndentWriter bw) {
        if (!helperDef.getCopyMaps().isEmpty()) {
            for (CopyMap copyMap: helperDef.getCopyMaps()) {
                writeHelperCopy(copyMap.getSourceDef(), copyMap.getTargetDef(), copyMap.getSourceToTargetMappings(), helperDef, bw);
                writeHelperCopy(copyMap.getTargetDef(), copyMap.getSourceDef(), copyMap.getTargetToSourceMappings(), helperDef, bw);

                //todo add feature stuff
                //for (QuickDtoFeature feature : helperDef.features) {
                //    feature.writeHelperCopyTo(source, helperDef, bw);
                //}

                //writeHelperCopyFrom(source, helperDef, bw);
                //for (QuickDtoFeature feature : helperDef.features) {
                //    feature.writeHelperCopyFrom(source, helperDef, bw);
                //}
            }
        }
    }

    private void writeHelperCopy(ClassDef sourceDef, ClassDef targetDef, HashMap<String, CopyMapping> sourceToTargetMappings,
          ParsedHelperDef helperDef, IndentWriter bw)
    {
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public static void copy(").append(helperDef.getImportSafeType(sourceDef.getType())).append(" source, ");
        bw.append(helperDef.getImportSafeType(targetDef.getType())).append(" dest, Fields... fields) {");
        bw.line(1, "if (fields.length > 0) {");
        bw.line(2, "copy(source, dest, Arrays.asList(fields));");
        bw.line(1, "} else {");
        bw.line(2, "copy(source, dest, Fields.values());");
        bw.line(1, "}");
        bw.line(0, "}");
        bw.newLine();
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public static void copy(").append(helperDef.getImportSafeType(sourceDef.getType())).append(" source, ");
        bw.append(helperDef.getImportSafeType(targetDef.getType())).append(" dest, Iterable<Fields> fields) {");
        bw.line(1, "for (Fields field: fields) {");
        bw.line(2, "switch (field) {");
        for (CopyMapping mapping: sourceToTargetMappings.values()) {
            Field getField = mapping.getGetField();
            Field setField = mapping.getSetField();

            bw.line(3, "case ").append(getField.getEnumName()).append(":");
            if (mapping.getErrorMessage() != null) {
                bw.line(4, "// ").append(mapping.getErrorMessage());
            }
            ConverterMethod converter = mapping.getConverterMethod();
            if (converter != null) {
                if (mapping.isCollectionConvert()) {
                    bw.append(" {");
                    bw.line(4, helperDef.getImportSafeType(setField.getType()))
                          .append(" _l_ = ")
                          .append("source.")
                          .append(generateGet(getField))
                          .append(" == null ? null : new java.util.ArrayList<>();");
                    bw.line(4, "dest.");
                    writeSet(setField, bw, () -> {
                        bw.append("_l_");
                    });
                    bw.append(";");
                    bw.line(4, "if (_l_ != null) {");
                    bw.line(5, "for (").append(helperDef.getImportSafeType(getField.getType().getListType()));
                    bw.append(" _i_: ").append("source.").append(generateGet(getField)).append(") {");
                    bw.line(6, "_l_.add(")
                          .append(converter.getClassType().getOriginalName())
                          .append(".")
                          .append(converter.getName())
                          .append("(_i_));");
                    bw.line(5, "}");
                    bw.line(4, "}");
                    bw.line(4, "break;");
                    bw.line(3, "}");

                } else {
                    bw.line(4, "dest.");
                    writeSet(setField, bw, () -> {
                        bw.append("source.").append(generateGet(getField)).append(" == null ? null : ");
                        bw.append(converter.getClassType().getOriginalName()).append(".").append(converter.getName()).append("(");
                        bw.append("source.").append(generateGet(getField));
                        if (converter.isExistingParam()) {
                            bw.append(", dest.").append(generateGet(setField));
                        }
                        bw.append(")");
                    });
                    bw.append(";");
                    bw.line(4, "break;");
                }

            } else if (mapping.isQuickDtoConvert()) {
                bw.append(" {");
                bw.line(4, helperDef.getImportSafeType(setField.getType())).append(" _d_ = source.").append(generateGet(getField));
                bw.append(" == null ? null : new ").append(helperDef.getImportSafeType(setField.getType())).append("();");
                bw.line(4, "dest.");
                writeSet(setField, bw, () -> bw.append("_d_"));
                bw.append(";");
                bw.line(4, "if (_d_ != null) {");

                String helperType = "Missing";
                if (getField.getType().isQuickHelper()) {
                    helperType = getField.getType().getQualifiedName();
                } else if (setField.getType().isQuickHelper()) {
                    helperType = setField.getType().getQualifiedName();
                }
                bw.line(5, helperType).append(HelperSuffix).append(".copy(").append("source.");
                bw.append(generateGet(getField)).append(", _d_);");
                bw.line(4, "}");
                bw.line(4, "break;");
                bw.line(3, "}");

            } else if (mapping.isQuickDtoListConvert()) {
                bw.append(" {");
                bw.line(4, "java.util.List<").append(helperDef.getImportSafeType(setField.getType().getListType()))
                      .append("> _a_ = source.")
                      .append(generateGet(getField))
                      .append(" == null ? null : new java.util.ArrayList<>();");
                bw.line(4, "dest.");
                writeSet(setField, bw, () -> bw.append("_a_"));
                bw.append(";");
                bw.line(4, "if (_a_ != null) {");
                bw.line(5, "for (").append(helperDef.getImportSafeType(getField.getType().getListType())).append(" _i_: source.");
                bw.append(generateGet(getField)).append(") {");
                bw.line(6, helperDef.getImportSafeType(setField.getType().getListType())).append(" _d_ = _i_ == null ? null : new ");
                bw.append(helperDef.getImportSafeType(setField.getType().getListType())).append("();");
                bw.line(6, "_a_.add(_d_);");
                bw.line(6, "if (_d_ != null) {");
                String helperType = "Missing";
                if (getField.getType().getListType().isQuickHelper()) {
                    helperType = getField.getType().getListType().getQualifiedName();
                } else if (setField.getType().getListType().isQuickHelper()) {
                    helperType = setField.getType().getListType().getQualifiedName();
                }
                bw.line(7, helperType).append(HelperSuffix).append(".copy(_i_, _d_);");
                bw.line(6, "}");
                bw.line(5, "}");
                bw.line(4, "}");
                bw.line(4, "break;");
                bw.line(3, "}");

            } else {
                bw.line(4, "dest.");
                writeSet(setField, bw, () -> bw.append("source.").append(generateGet(getField)));
                bw.append(";");
                bw.line(4, "break;");
            }

        }
        bw.line(2, "}");
        bw.line(1, "}");
        bw.line(0, "}");
        bw.newLine();
    }

    private String generateGet(Field getField) {
        if (getField.hasGetter()) {
            return getField.getFullGetAccessorName() + "()";

        } else { //isPublic
            return getField.getName();
        }
    }

    private void writeSet(Field setField, IndentWriter bw, Runnable setValue) {
        if (setField.hasSetter()) {
            bw.append("set").append(setField.getAccessorName()).append("(");
            setValue.run();
            bw.append(")");

        } else { //isPublic
            bw.append(setField.getName()).append(" = ");
            setValue.run();
        }
    }
}