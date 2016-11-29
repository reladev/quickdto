package com.github.quickdto.testharness;

import com.github.quickdto.shared.CopyFromOnly;
import com.github.quickdto.shared.QuickDto;
import com.github.quickdto.testharness.impl.Simple;

@QuickDto(source = Simple.class)
public class SimpleDtoDef {
	int normal;
	@CopyFromOnly int readOnly;
	@CopyFromOnly(setter = true) int readOnlyWithSetter;
	int writeOnly;
}
