package org.reladev.quickdto.feature;

import java.io.IOException;

import org.reladev.quickdto.processor.CopyMap2;
import org.reladev.quickdto.processor.Field2;
import org.reladev.quickdto.processor.IndentWriter;
import org.reladev.quickdto.processor.ParsedDtoDef;

abstract public class QuickDtoFeature2 {

    abstract public void writeFields(ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeMethods(ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void preGetterLogic(Field2 field, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void postGetterLogic(Field2 field, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void preSetterLogic(Field2 field, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void postSetterLogic(Field2 field, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeCopyTo(CopyMap2 source, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeCopyFrom(CopyMap2 source, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeHelperCopyTo(CopyMap2 source, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeHelperCopyFrom(CopyMap2 source, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeInnerClasses(ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    @Override
    final public boolean equals(Object o) {
        return this.getClass().equals(o.getClass());
    }

    @Override
    final public int hashCode() {
        return this.getClass().hashCode();
    }
}