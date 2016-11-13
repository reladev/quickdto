package com.github.quickdto.testharness;

import com.github.quickdto.shared.QuickDto;
import com.github.quickdto.testharness.impl.Simple;
import com.google.gwt.core.shared.GwtIncompatible;

@QuickDto(source = Simple.class)
public class ConvertDtoDef {
	int normal;

	@GwtIncompatible
	public void someMethod(int foo, boolean bar) {
		System.out.println("something:" + bar);
	}
}
