package com.github.quickdto.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used on fields to enforce or not enforce strict copy.
 * <p>
 * Most commonly used to override the strict copy set in {@link QuickDto}.
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface StrictCopy {
	/**
	 * Defines whether this field should enforce strict copy.
	 */
	boolean value() default true;
}
