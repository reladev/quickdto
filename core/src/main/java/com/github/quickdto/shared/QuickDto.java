package com.github.quickdto.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface QuickDto {
	Class extend() default Object.class;
	Class[] implement() default {};
    Class[] source() default {};
	boolean strictCopy() default true;
	boolean fieldAnnotationsOnGetter() default false;
	boolean copyMethods() default false;
}
