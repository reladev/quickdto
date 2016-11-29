package com.github.quickdto.testharness;

import com.github.quickdto.shared.GwtIncompatible;
import com.github.quickdto.shared.QuickDto;
import com.github.quickdto.testharness.impl.Simple;

@QuickDto(source = Simple.class)
public class ConvertDtoDef {
	int normal;

	@GwtIncompatible
	public void someMethod(int foo, boolean bar) {
		System.out.println("something:" + bar);
	}
}
