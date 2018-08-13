package org.reladev.quickdto.processor;

import java.util.ArrayList;
import java.util.List;

public class HelperDef extends ClassDef {
    private boolean strictCopy = true;
    private List<String> copyToClasses = new ArrayList<>();
    private SourceDef sourceDef;


    public String getPackageString() {
        return packageString;
    }

    public void setPackageString(String packageString) {
        this.packageString = packageString;
    }

    public boolean isStrictCopy() {
        return strictCopy;
    }

    public void setStrictCopy(boolean strictCopy) {
        this.strictCopy = strictCopy;
    }

    public List<String> getCopyToClasses() {
        return copyToClasses;
    }

    public void setCopyToClasses(List<String> copyToClasses) {
        this.copyToClasses = copyToClasses;
    }

    public SourceDef getSourceDef() {
        return sourceDef;
    }

    public void setSourceDef(SourceDef sourceDef) {
        this.sourceDef = sourceDef;
        getFields().putAll(sourceDef.getters);
        getFields().putAll(sourceDef.setters);
    }

    @Override
    public String toString() {
        return sourceDef.toString();
    }
}
