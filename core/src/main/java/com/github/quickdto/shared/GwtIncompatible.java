package com.github.quickdto.shared;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used by the GWT compiler to prevent any thing marked with this annotation from
 * being included in the compilation to Javascript.  This annotation doesn't have
 * any dependency to GWT and will have no effect on non GWT projects.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD })
@Documented
public @interface GwtIncompatible {
	/**
	 * An attribute that can be used to explain why the code is incompatible.
	 * A GwtIncompatible annotation can have any number of attributes; attributes
	 * are for documentation purposes and are ignored by the GWT compiler.
	 */
	String value() default "";
}
