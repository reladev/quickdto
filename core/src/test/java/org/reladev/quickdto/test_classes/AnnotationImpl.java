package org.reladev.quickdto.test_classes;

import org.reladev.quickdto.shared.ExcludeCopy;
import org.reladev.quickdto.shared.ExcludeCopyFrom;
import org.reladev.quickdto.shared.ExcludeCopyTo;

public class AnnotationImpl {
    private int equalsHash;

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


    public int getEqualsHash() {
        return equalsHash;
    }

    public void setEqualsHash(int equalsHash) {
        this.equalsHash = equalsHash;
    }

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
