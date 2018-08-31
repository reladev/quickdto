package org.reladev.quickdto.feature;

import java.io.IOException;

import org.reladev.quickdto.processor.CopyMap;
import org.reladev.quickdto.processor.Field;
import org.reladev.quickdto.processor.IndentWriter;
import org.reladev.quickdto.processor.ParsedDtoDef;

abstract public class QuickDtoFeature {

    abstract public void writeFields(ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeMethods(ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void preGetterLogic(Field field, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void postGetterLogic(Field field, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void preSetterLogic(Field field, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void postSetterLogic(Field field, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeCopyTo(CopyMap source, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeCopyFrom(CopyMap source, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeHelperCopyTo(CopyMap source, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

    abstract public void writeHelperCopyFrom(CopyMap source, ParsedDtoDef dtoDef, IndentWriter bw) throws IOException;

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