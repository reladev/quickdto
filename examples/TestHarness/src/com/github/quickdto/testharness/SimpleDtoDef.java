package com.github.quickdto.testharness;

import com.github.quickdto.shared.QuickDto;
import com.github.quickdto.shared.ReadOnly;
import com.github.quickdto.testharness.impl.Simple;

@QuickDto(source = Simple.class)
public class SimpleDtoDef {
	int normal;
	@ReadOnly int readOnly;
	@ReadOnly(setter = true) int readOnlyWithSetter;
}
