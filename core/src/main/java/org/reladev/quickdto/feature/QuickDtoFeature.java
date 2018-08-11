package org.reladev.quickdto.feature;

import java.io.IOException;

import org.reladev.quickdto.processor.DtoDef;
import org.reladev.quickdto.processor.DtoField;
import org.reladev.quickdto.processor.IndentWriter;
import org.reladev.quickdto.processor.Source;

abstract public class QuickDtoFeature {

    abstract public void writeFields(DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeMethods(DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void preGetterLogic(DtoField field, DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void postGetterLogic(DtoField field, DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void preSetterLogic(DtoField field, DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void postSetterLogic(DtoField field, DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeCopyTo(Source source, DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeCopyFrom(Source source, DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeHelperCopyTo(Source source, DtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeHelperCopyFrom(Source source, DtoDef dtoDef, IndentWriter bw) throws IOException;

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