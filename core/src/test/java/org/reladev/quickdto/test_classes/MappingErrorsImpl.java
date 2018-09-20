package org.reladev.quickdto.test_classes;


import java.util.List;

import org.reladev.quickdto.shared.ExcludeCopy;
import org.reladev.quickdto.shared.ExcludeCopyFrom;
import org.reladev.quickdto.shared.ExcludeCopyTo;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class MappingErrorsImpl {
    // Errors
    private Integer typeMismatch;
    private List<Integer> typeMismatchList;
    private String targetNoGetter;
    private String targetNoSetter;
    private String sourceNoGetter;
    private String sourceNoSetter;

    // Excludes Target
    private String targetNoField;  //Automatically excluded
    private String fieldTargetExclude;
    private String targetNoGetterTargetExcludeTo;
    private String targetNoSetterTargetExcludeFrom;
    private String sourceNoGetterTargetExcludeFrom;
    private String sourceNoSetterTargetExcludeTo;

    // Excludes Source
    @ExcludeCopy
    private String fieldSourceExclude;
    @ExcludeCopyTo
    private String targetNoGetterSourceExcludeTo;
    @ExcludeCopyFrom
    private String targetNoSetterSourceExcludeFrom;
    @ExcludeCopyTo
    private String sourceNoSetterSourceExcludeTo;
    @ExcludeCopyFrom
    private String sourceNoGetterSourceExcludeFrom;

    public String getTargetNoField() {
        return targetNoField;
    }

    public void setTargetNoField(String targetNoField) {
        this.targetNoField = targetNoField;
    }

    public Integer getTypeMismatch() {
        return typeMismatch;
    }

    public void setTypeMismatch(Integer typeMismatch) {
        this.typeMismatch = typeMismatch;
    }

    public List<Integer> getTypeMismatchList() {
        return typeMismatchList;
    }

    public void setTypeMismatchList(List<Integer> typeMismatchList) {
        this.typeMismatchList = typeMismatchList;
    }

    public String getTargetNoGetter() {
        return targetNoGetter;
    }

    public void setTargetNoGetter(String targetNoGetter) {
        this.targetNoGetter = targetNoGetter;
    }

    public String getTargetNoSetter() {
        return targetNoSetter;
    }

    public void setTargetNoSetter(String targetNoSetter) {
        this.targetNoSetter = targetNoSetter;
    }

    //public String getSourceNoGetter() {
    //    return sourceNoGetter;
    //}

    public void setSourceNoGetter(String sourceNoGetter) {
        this.sourceNoGetter = sourceNoGetter;
    }

    public String getSourceNoSetter() {
        return sourceNoSetter;
    }

    //public void setSourceNoSetter(String sourceNoSetter) {
    //    this.sourceNoSetter = sourceNoSetter;
    //}

    public String getFieldTargetExclude() {
        return fieldTargetExclude;
    }

    public void setFieldTargetExclude(String fieldTargetExclude) {
        this.fieldTargetExclude = fieldTargetExclude;
    }

    public String getTargetNoGetterTargetExcludeTo() {
        return targetNoGetterTargetExcludeTo;
    }

    public void setTargetNoGetterTargetExcludeTo(String targetNoGetterTargetExcludeTo) {
        this.targetNoGetterTargetExcludeTo = targetNoGetterTargetExcludeTo;
    }

    public String getTargetNoSetterTargetExcludeFrom() {
        return targetNoSetterTargetExcludeFrom;
    }

    public void setTargetNoSetterTargetExcludeFrom(String targetNoSetterTargetExcludeFrom) {
        this.targetNoSetterTargetExcludeFrom = targetNoSetterTargetExcludeFrom;
    }

    //public String getSourceNoGetterTargetExcludeFrom() {
    //    return sourceNoGetterTargetExcludeFrom;
    //}

    public void setSourceNoGetterTargetExcludeFrom(String sourceNoGetterTargetExcludeFrom) {
        this.sourceNoGetterTargetExcludeFrom = sourceNoGetterTargetExcludeFrom;
    }

    public String getSourceNoSetterTargetExcludeTo() {
        return sourceNoSetterTargetExcludeTo;
    }

    //public void setSourceNoSetterTargetExcludeTo(String sourceNoSetterTargetExcludeTo) {
    //    this.sourceNoSetterTargetExcludeTo = sourceNoSetterTargetExcludeTo;
    //}

    public String getFieldSourceExclude() {
        return fieldSourceExclude;
    }

    public void setFieldSourceExclude(String fieldSourceExclude) {
        this.fieldSourceExclude = fieldSourceExclude;
    }

    public String getTargetNoGetterSourceExcludeTo() {
        return targetNoGetterSourceExcludeTo;
    }

    public void setTargetNoGetterSourceExcludeTo(String targetNoGetterSourceExcludeTo) {
        this.targetNoGetterSourceExcludeTo = targetNoGetterSourceExcludeTo;
    }

    public String getTargetNoSetterSourceExcludeFrom() {
        return targetNoSetterSourceExcludeFrom;
    }

    public void setTargetNoSetterSourceExcludeFrom(String targetNoSetterSourceExcludeFrom) {
        this.targetNoSetterSourceExcludeFrom = targetNoSetterSourceExcludeFrom;
    }

    //public String getSourceNoGetterSourceExcludeFrom() {
    //    return sourceNoGetterSourceExcludeFrom;
    //}

    public void setSourceNoGetterSourceExcludeFrom(String sourceNoGetterSourceExcludeFrom) {
        this.sourceNoGetterSourceExcludeFrom = sourceNoGetterSourceExcludeFrom;
    }

    public String getSourceNoSetterSourceExcludeTo() {
        return sourceNoSetterSourceExcludeTo;
    }

    //public void setSourceNoSetterSourceExcludeTo(String sourceNoSetterSourceExcludeTo) {
    //    this.sourceNoSetterSourceExcludeTo = sourceNoSetterSourceExcludeTo;
    //}
}
