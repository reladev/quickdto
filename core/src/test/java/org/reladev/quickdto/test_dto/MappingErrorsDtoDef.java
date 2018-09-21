package org.reladev.quickdto.test_dto;


import java.util.List;

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
}
