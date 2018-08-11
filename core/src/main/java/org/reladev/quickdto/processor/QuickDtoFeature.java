package org.reladev.quickdto.processor;

import java.io.IOException;

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
}