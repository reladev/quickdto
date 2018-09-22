package org.reladev.quickdto.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.reladev.quickdto.feature.QuickDtoFeature;


/**
 * Main annotation needed for QuickDto and triggers the APT processing.
 * <p>
 * Included this annotation on any class, and QuickDto will generate a DTO class
 * based on the fields included.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface QuickDto {
	/**
	 * @return the class the DTO should extend.
	 */
	Class extend() default Object.class;

	/**
	 * @return the interfaces the DTO should implement.
	 */
	Class[] implement() default {};

	/**
	 * @return the source class should be used in the copy methods.
	 */
    Class[] sources() default {};

    /**
     * @return the list of converter classes that should be used for any type mismatches.
     */
    Class[] converters() default {};

    /**
     * @return the list of features that should be included in when generating the DTO.
     */
    Class<? extends QuickDtoFeature>[] features() default {};

	/**
	 * @return whether non QuickDto annotations on fields should be placed on the property or the getter method.
	 *
	 * Defaults to false.
	 */
	boolean fieldAnnotationsOnGetter() default false;

	/**
	 * @return whether QuickDto should copy methods defined in the DtoDef
	 * into the DTO class.  This functionality uses JDK classes that are not guaranteed to exist in
	 * all JDKs, so it isn't included by default. This is a compile time dependency, so if the copy
	 * is successful, then it can be used on any JVM.
	 *
	 * Defaults to false.
	 */
	boolean copyMethods() default false;

}
