package org.reladev.quickdto.testharness;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.shared.StrictCopy;
import org.reladev.quickdto.testharness.impl.Convert;
import org.reladev.quickdto.testharness.impl.Simple;

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
