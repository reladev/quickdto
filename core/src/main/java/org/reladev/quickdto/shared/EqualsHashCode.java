package org.reladev.quickdto.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used on fields that should be included in the `equals` and `hashCode` methods.  Only fields
 * with this annotation will be included, but if no field have the annotation, then all the
 * fields are included in the `equals` and `hashCode` methods.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface EqualsHashCode {
}
