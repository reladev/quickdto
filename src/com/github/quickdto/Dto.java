package com.github.quickdto;

import java.util.HashSet;
import java.util.Set;

public class Dto<T> {
    private Set<T> dirtyFields = new HashSet<T>();
    private Dto parent;
    private Enum fieldInParent;

    public Dto() {
    }

    public Dto(Dto parent, Enum fieldInParent) {
        this.parent = parent;
        this.fieldInParent = fieldInParent;
    }

    public void setDirty(T field, boolean dirty) {
        if (dirty) {
            dirtyFields.add(field);
        } else {
            dirtyFields.remove(field);
        }
        if (parent != null) {
            parent.setDirty(fieldInParent, isDirty());
        }
    }

    public boolean isDirty() {
        return !dirtyFields.isEmpty();
    }

    public boolean isDirty(T field) {
        return dirtyFields.contains(field);
    }

    public static boolean safeEquals(Object s1, Object s2) {
        if (s1 != null && s2 != null) {
            return s1.equals(s2);

        } else if (s1 == null && s2 == null) {
            return true;

        } else {
            return false;
        }
    }
}
