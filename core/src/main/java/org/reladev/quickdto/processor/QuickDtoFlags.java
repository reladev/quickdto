package org.reladev.quickdto.processor;

@SuppressWarnings("WeakerAccess")
public class QuickDtoFlags {
    private boolean copyFrom;
    private boolean copyTo;
    private boolean excludeSetter;
    private boolean excludeGetter;
    private boolean equalsHashCode;
    private Boolean strictCopy;

    public boolean isCopyFrom() {
        return copyFrom;
    }

    public void setCopyFrom() {
        this.copyFrom = true;
    }

    public boolean isCopyTo() {
        return copyTo;
    }

    public void setCopyTo() {
        this.copyTo = true;
    }

    public boolean isExcludeSetter() {
        return excludeSetter;
    }

    public void setExcludeSetter() {
        this.excludeSetter = true;
    }

    public boolean isExcludeGetter() {
        return excludeGetter;
    }

    public void setExcludeGetter() {
        this.excludeGetter = true;
    }

    public boolean isEqualsHashCode() {
        return equalsHashCode;
    }

    public void setEqualsHashCode() {
        this.equalsHashCode = true;
    }

    //public boolean isStrictCopy(ClassDef dtoDef) {
    //    if (strictCopy != null) {
    //        return strictCopy;
    //    } else {
    //        return dtoDef.strictCopy;
    //    }
    //}

    public void setStrictCopy(boolean strictCopy) {
        this.strictCopy = strictCopy;
    }
}
