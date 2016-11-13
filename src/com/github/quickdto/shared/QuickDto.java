package com.github.quickdto.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface QuickDto {
    Class[] source() default {};
    Class[] extend() default {};
    Class[] implement() default {};
	boolean strictCopy() default true;
}
