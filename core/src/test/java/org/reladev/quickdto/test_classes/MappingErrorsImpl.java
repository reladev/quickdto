package org.reladev.quickdto.test_classes;


import java.util.List;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class MappingErrorsImpl {
    // Errors
    private Integer typeMismatch;
    private List<Integer> typeMismatchList;
    private String targetNoGetter;
    private String targetNoSetter;
    private String sourceNoGetter;
    private String sourceNoSetter;
    private Integer convertMethodStatic;

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


    public Integer getConvertMethodStatic() {
        return convertMethodStatic;
    }

    public void setConvertMethodStatic(Integer convertMethodStatic) {
        this.convertMethodStatic = convertMethodStatic;
    }
}
