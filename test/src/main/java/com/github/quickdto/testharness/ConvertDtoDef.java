package com.github.quickdto.testharness;

import java.util.List;

import com.github.quickdto.shared.GwtIncompatible;
import com.github.quickdto.shared.QuickDto;
import com.github.quickdto.shared.StrictCopy;
import com.github.quickdto.testharness.impl.Simple;

@QuickDto(source = Simple.class)
@StrictCopy(false)
public class ConvertDtoDef {
	int normal;
	@StrictCopy(false)
	List<SimpleDtoDef> simples;

	public static SimpleDto convert(Simple simple) {
		SimpleDto simpleDto = new SimpleDto();
		simpleDto.copyFrom(simple);
		return simpleDto;
	}
}
