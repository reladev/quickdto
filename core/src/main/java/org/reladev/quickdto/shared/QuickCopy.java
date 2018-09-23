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
public @interface QuickCopy {
    /**
     * @return the list of classes that should have copy to and from methods.
     */
    Class[] targets() default {};

    /**
     * @return the list of converter classes that should be used for any type mismatches.
     */
    Class[] converters() default {};
}
