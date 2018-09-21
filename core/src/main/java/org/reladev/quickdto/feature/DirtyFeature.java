package org.reladev.quickdto.feature;

import java.io.IOException;

import org.reladev.quickdto.processor.CopyMap;
import org.reladev.quickdto.processor.Field;
import org.reladev.quickdto.processor.IndentWriter;
import org.reladev.quickdto.processor.ParsedDtoDef;

public class DirtyFeature extends QuickDtoFeature {

    @Override
    public void writeFields(ParsedDtoDef dtoDef, IndentWriter bw) throws IOException {
        bw.line(0, "protected Set<Fields> dirtyFields = new HashSet<Fields>();");
    }

    @Override
    public void writeMethods(ParsedDtoDef dtoDef, IndentWriter bw) throws IOException {
        bw.newLine();
        bw.line(0, "public void markDirty(boolean dirty, Fields... fields) { ");
        bw.line(1, "for (Fields field: fields) {");
        bw.line(2, "if (dirty) {");
        bw.line(3, "dirtyFields.add(field);");
        bw.line(2, "} else {");
        bw.line(3, "dirtyFields.remove(field);");
        bw.line(2, "}");
        // Todo handle dirty for nested
        //bw.line(2, "if (parent != null) {");
        //bw.line(3, "parent.markDirty(fieldInParent, checkDirty());");
        //bw.line(2, "}");
        bw.line(1, "}");
        bw.line(0, "}");
        bw.newLine();
        bw.line(0, "public boolean checkDirty() {");
        bw.line(1, "return !dirtyFields.isEmpty();");
        bw.line(0, "}");
        bw.newLine();
        bw.line(0, "public boolean checkDirty(Fields field) {");
        bw.line(1, "return dirtyFields.contains(field);");
        bw.line(0, "}");
        bw.newLine();
        bw.line(0, "public Set<Fields> listDirtyFields() {");
        bw.line(1, "return dirtyFields;");
        bw.line(0, "}");
        bw.newLine();
        bw.line(0, "public void resetDirty() {");
        bw.line(1, "dirtyFields.clear();");
        bw.line(0, "}");
        bw.newLine();

    }

    @Override
    public void preGetterLogic(Field field, ParsedDtoDef dtoDef, IndentWriter bw) {
    }

    @Override
    public void postGetterLogic(Field field, ParsedDtoDef dtoDef, IndentWriter bw) {
    }

    @Override
    public void preSetterLogic(Field field, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException {
        bw.line(0, "if (this.").append(field.getName()).append(" == null || !Objects.equals(this.").append(field.getName()).append(", ").append(
              field.getName()).append(")) {");
        bw.line(1, "markDirty(true, Fields.").append(field.getEnumName()).append(");");
        bw.line(0, "}");
    }

    @Override
    public void postSetterLogic(Field field, ParsedDtoDef dtoDef, IndentWriter bw) {
    }

    @Override
    public void writeCopyTo(CopyMap copyMap, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException {
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public void copyDirtyTo(").append(dtoDef.getImportSafeType(copyMap.getSourceDef().getType())).append(" dest) {");
        bw.line(1, "copyTo(dest, listDirtyFields());");
        bw.line(0, "}");
        bw.newLine();
    }

    @Override
    public void writeCopyFrom(CopyMap copyMap, ParsedDtoDef dtoDef, IndentWriter bw) {
    }

    @Override
    public void writeHelperCopyTo(CopyMap copyMap, ParsedDtoDef dtoDef, IndentWriter bw) {
        //bw.line(0, "@GwtIncompatible");
        //bw.line(0, "public static void copyDirtyTo(").append(dtoDef.name).append(" dto, ");
        //bw.append(source.sourceDef.type).append(" dest, ").append(dtoDef.name).append(".Fields... fields) {");
        //bw.line(1, "copyTo(dto, dest, dto.listDirtyFields());");
        //bw.line(0, "}");
        //bw.newLine();
    }

    @Override
    public void writeHelperCopyFrom(CopyMap copyMap, ParsedDtoDef dtoDef, IndentWriter bw) {
    }

    @Override
    public void writeInnerClasses(ParsedDtoDef dtoDef, IndentWriter bw) {
    }
}