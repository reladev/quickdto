package org.reladev.quickdto.processor;

import java.io.IOException;

public class DirtyFeature extends QuickDtoFeature {


    public void writeFields(DtoDef dtoDef, IndentWriter bw) throws IOException {
        bw.line(0, "protected Set<Fields> dirtyFields = new HashSet<Fields>();");
    }

    public void writeMethods(DtoDef dtoDef, IndentWriter bw) throws IOException {
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

    public void preGetterLogic(DtoField field, DtoDef dtoDef, IndentWriter bw) throws IOException {
    }

    public void postGetterLogic(DtoField field, DtoDef dtoDef, IndentWriter bw) throws IOException {
    }

    public void preSetterLogic(DtoField field, DtoDef dtoDef, IndentWriter bw) throws IOException {
        bw.line(0, "if (this.").append(field.getFieldName()).append(" == null || !Objects.equals(this.").append(field.getFieldName()).append(", ").append(field.getFieldName()).append(")) {");
        bw.line(1, "markDirty(true, Fields.").append(field.getEnumName()).append(");");
        bw.line(0, "}");
    }

    public void postSetterLogic(DtoField field, DtoDef dtoDef, IndentWriter bw) throws IOException {
    }

    public void writeCopyTo(Source source, DtoDef dtoDef, IndentWriter bw) throws IOException {
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public void copyDirtyTo(").append(source.type).append(" dest) {");
        bw.line(1, "copyTo(dest, listDirtyFields());");
        bw.line(0, "}");
        bw.newLine();
    }

    public void writeCopyFrom(Source source, DtoDef dtoDef, IndentWriter bw) throws IOException {
    }

    public void writeHelperCopyTo(Source source, DtoDef dtoDef, IndentWriter bw) throws IOException {
        bw.line(0, "@GwtIncompatible");
        bw.line(0, "public static void copyDirtyTo(").append(dtoDef.name).append(" dto, ");
        bw.append(source.type).append(" dest, ").append(dtoDef.name).append(".Fields... fields) {");
        bw.line(1, "copyTo(dto, dest, dto.listDirtyFields());");
        bw.line(0, "}");
        bw.newLine();
    }

    public void writeHelperCopyFrom(Source source, DtoDef dtoDef, IndentWriter bw) throws IOException {
    }

    public void writeInnerClasses(DtoDef dtoDef, IndentWriter bw) throws IOException {
    }
}