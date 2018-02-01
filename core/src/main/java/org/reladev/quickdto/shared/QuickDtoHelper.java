package org.reladev.quickdto.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for source objects to create a Source Util object for copy methods
 * to and from the Dtos listed.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface QuickDtoHelper {
    /**
     * @return the source class should be used in the copy methods.
     */
    Class[] dtoDef() default {};

    /**
     * @return whether the QuickDto should enforce that all the fields in the
     * DTO are copied to at least one source.  If not copied, then a compile error is created.  This
     * is beneficial for ensuring that name refactoring doesn't break copy methods.
     * <p>
     * Defaults to true.
     */
    boolean strictCopy() default true;
}
