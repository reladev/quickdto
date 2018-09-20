package org.reladev.quickdto.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used on fields that should only be copied from the DTO to the source.  This
 * is useful for password fields that are sent in, but never sent out.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface ExcludeCopyFrom {
    Class[] value() default {};
}
