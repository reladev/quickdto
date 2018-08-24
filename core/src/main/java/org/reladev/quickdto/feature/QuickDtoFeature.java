package org.reladev.quickdto.feature;

import java.io.IOException;

import org.reladev.quickdto.processor.CopyMap;
import org.reladev.quickdto.processor.DtoDef;
import org.reladev.quickdto.processor.DtoField;
import org.reladev.quickdto.processor.IndentWriter;

abstract public class QuickDtoFeature {

    abstract public void writeFields(DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeMethods(DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void preGetterLogic(DtoField field, DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void postGetterLogic(DtoField field, DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void preSetterLogic(DtoField field, DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void postSetterLogic(DtoField field, DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeCopyTo(CopyMap source, DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeCopyFrom(CopyMap source, DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeHelperCopyTo(CopyMap source, DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeHelperCopyFrom(CopyMap source, DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeInnerClasses(DtoDef dtoDef, IndentWriter bw) throws IOException;

    @Override
    final public boolean equals(Object o) {
        return this.getClass().equals(o.getClass());
    }

    @Override
    final public int hashCode() {
        return this.getClass().hashCode();
    }
}