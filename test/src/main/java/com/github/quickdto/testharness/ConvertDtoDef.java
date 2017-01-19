package com.github.quickdto.testharness;

import java.util.List;

import com.github.quickdto.shared.GwtIncompatible;
import com.github.quickdto.shared.QuickDto;
import com.github.quickdto.shared.StrictCopy;
import com.github.quickdto.testharness.impl.Convert;
import com.github.quickdto.testharness.impl.Simple;

@QuickDto(source = Convert.class)
@StrictCopy(false)
public class ConvertDtoDef {
	int normal;
	SimpleDtoDef simple;
	//List<SimpleDtoDef> simpleList;

	public static SimpleDto convert(Simple simple) {
		SimpleDto simpleDto = new SimpleDto();
		simpleDto.copyFrom(simple);
		return simpleDto;
	}

	public static Simple convert(SimpleDto simpleDto) {
		Simple simple = new Simple();
		simpleDto.copyTo(simple);
		return simple;
	}
}
