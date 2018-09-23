package org.reladev.quickdto.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used on fields that should be excluded from all copy methods.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface ExcludeCopy {
    /**
     * @return list of classes that the exclude applies to.  If a copy class is not in the list, exclude
     * won't apply unless the list is blank, then the excluded will be applied to all targets/sources.
     */
    Class[] value() default {};
}
