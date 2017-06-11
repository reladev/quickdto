package com.github.quickdto.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used on fields that should only be copied from the source to the DTO.  This
 * is useful for ID fields that are created on the server and need to be sent out.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface CopyToOnly {
	/**
	 * Defines whether the getter method should be generated in the DTO.
	 * <p>
	 * Defaults to true.
	 */
	boolean getter() default true;
}
