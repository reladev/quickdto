package org.reladev.quickdto.test_classes;


import java.util.List;

import org.reladev.quickdto.shared.ExcludeCopy;
import org.reladev.quickdto.shared.ExcludeCopyFrom;
import org.reladev.quickdto.shared.ExcludeCopyTo;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class MappingErrorsDtoDef {
    // Errors
    private String sourceNoField;
    private String typeMismatch;
    private List<String> typeMismatchList;
    private String targetNoGetter;
    private String targetNoSetter;
    private String sourceNoGetter;
    private String sourceNoSetter;

    // Excludes Target
    @ExcludeCopy
    private String sourceNoFieldTargetExclude;
    @ExcludeCopy
    private String fieldTargetExclude;
    @ExcludeCopyFrom
    private String targetNoGetterTargetExcludeTo;
    @ExcludeCopyTo
    private String targetNoSetterTargetExcludeFrom;
    @ExcludeCopyTo
    private String sourceNoGetterTargetExcludeFrom;
    @ExcludeCopyFrom
    private String sourceNoSetterTargetExcludeTo;

    // Excludes Source
    private String fieldSourceExclude;
    private String targetNoGetterSourceExcludeTo;
    private String targetNoSetterSourceExcludeFrom;
    private String sourceNoGetterSourceExcludeFrom;
    private String sourceNoSetterSourceExcludeTo;

    public String getSourceNoField() {
        return sourceNoField;
    }

    public void setSourceNoField(String sourceNoField) {
        this.sourceNoField = sourceNoField;
    }

    public String getTypeMismatch() {
        return typeMismatch;
    }

    public void setTypeMismatch(String typeMismatch) {
        this.typeMismatch = typeMismatch;
    }

    public List<String> getTypeMismatchList() {
        return typeMismatchList;
    }

    public void setTypeMismatchList(List<String> typeMismatchList) {
        this.typeMismatchList = typeMismatchList;
    }

    //public String getTargetNoGetter() {
    //    return targetNoGetter;
    //}

    public void setTargetNoGetter(String targetNoGetter) {
        this.targetNoGetter = targetNoGetter;
    }

    public String getTargetNoSetter() {
        return targetNoSetter;
    }

    //public void setTargetNoSetter(String targetNoSetter) {
    //    this.targetNoSetter = targetNoSetter;
    //}

    public String getSourceNoGetter() {
        return sourceNoGetter;
    }

    public void setSourceNoGetter(String sourceNoGetter) {
        this.sourceNoGetter = sourceNoGetter;
    }

    public String getSourceNoSetter() {
        return sourceNoSetter;
    }

    public void setSourceNoSetter(String sourceNoSetter) {
        this.sourceNoSetter = sourceNoSetter;
    }

    public String getSourceNoFieldTargetExclude() {
        return sourceNoFieldTargetExclude;
    }

    public void setSourceNoFieldTargetExclude(String sourceNoFieldTargetExclude) {
        this.sourceNoFieldTargetExclude = sourceNoFieldTargetExclude;
    }

    public String getFieldTargetExclude() {
        return fieldTargetExclude;
    }

    public void setFieldTargetExclude(String fieldTargetExclude) {
        this.fieldTargetExclude = fieldTargetExclude;
    }

    //public String getTargetNoGetterTargetExcludeTo() {
    //    return targetNoGetterTargetExcludeTo;
    //}

    public void setTargetNoGetterTargetExcludeTo(String targetNoGetterTargetExcludeTo) {
        this.targetNoGetterTargetExcludeTo = targetNoGetterTargetExcludeTo;
    }

    public String getTargetNoSetterTargetExcludeFrom() {
        return targetNoSetterTargetExcludeFrom;
    }

    //public void setTargetNoSetterTargetExcludeFrom(String targetNoSetterTargetExcludeFrom) {
    //    this.targetNoSetterTargetExcludeFrom = targetNoSetterTargetExcludeFrom;
    //}

    public String getSourceNoGetterTargetExcludeFrom() {
        return sourceNoGetterTargetExcludeFrom;
    }

    public void setSourceNoGetterTargetExcludeFrom(String sourceNoGetterTargetExcludeFrom) {
        this.sourceNoGetterTargetExcludeFrom = sourceNoGetterTargetExcludeFrom;
    }

    public String getSourceNoSetterTargetExcludeTo() {
        return sourceNoSetterTargetExcludeTo;
    }

    public void setSourceNoSetterTargetExcludeTo(String sourceNoSetterTargetExcludeTo) {
        this.sourceNoSetterTargetExcludeTo = sourceNoSetterTargetExcludeTo;
    }

    public String getFieldSourceExclude() {
        return fieldSourceExclude;
    }

    public void setFieldSourceExclude(String fieldSourceExclude) {
        this.fieldSourceExclude = fieldSourceExclude;
    }

    //public String getTargetNoGetterSourceExcludeTo() {
    //    return targetNoGetterSourceExcludeTo;
    //}

    public void setTargetNoGetterSourceExcludeTo(String targetNoGetterSourceExcludeTo) {
        this.targetNoGetterSourceExcludeTo = targetNoGetterSourceExcludeTo;
    }

    public String getTargetNoSetterSourceExcludeFrom() {
        return targetNoSetterSourceExcludeFrom;
    }

    //public void setTargetNoSetterSourceExcludeFrom(String targetNoSetterSourceExcludeFrom) {
    //    this.targetNoSetterSourceExcludeFrom = targetNoSetterSourceExcludeFrom;
    //}

    public String getSourceNoGetterSourceExcludeFrom() {
        return sourceNoGetterSourceExcludeFrom;
    }

    public void setSourceNoGetterSourceExcludeFrom(String sourceNoGetterSourceExcludeFrom) {
        this.sourceNoGetterSourceExcludeFrom = sourceNoGetterSourceExcludeFrom;
    }

    public String getSourceNoSetterSourceExcludeTo() {
        return sourceNoSetterSourceExcludeTo;
    }

    public void setSourceNoSetterSourceExcludeTo(String sourceNoSetterSourceExcludeTo) {
        this.sourceNoSetterSourceExcludeTo = sourceNoSetterSourceExcludeTo;
    }
}
