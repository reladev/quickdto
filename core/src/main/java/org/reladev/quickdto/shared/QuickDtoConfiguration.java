package org.reladev.quickdto.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.reladev.quickdto.feature.QuickDtoFeature;


/**
 * Annotation for global configuration of the QuickDto and QuickCopy.
 * <p>
 * This annotation can only exist on one class or an error is thrown.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface QuickDtoConfiguration {
    /**
     * @return The suffix for all QuickDto Definitions.  The suffix will be removed to for the Dto's name.
     */
    String quickDtoDefSuffix() default "Def";

    /**
     * @return The suffix that will be added to the annotated class's name to form the QuickCopy class.
     */
    String quickCopySuffix() default "CopyUtil";

    /**
     * @return Features that should be included on every QuickDto/QuickCopy.
     */
    Class<? extends QuickDtoFeature>[] globalFeatures() default {};
}
