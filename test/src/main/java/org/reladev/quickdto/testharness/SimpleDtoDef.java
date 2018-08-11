package org.reladev.quickdto.testharness;

import org.reladev.quickdto.feature.DirtyFeature;
import org.reladev.quickdto.shared.CopyFromOnly;
import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.testharness.impl.Simple;

@QuickDto(source = Simple.class, feature = DirtyFeature.class)
public class SimpleDtoDef {
	int normal;
	@CopyFromOnly int readOnly;
	@CopyFromOnly(setter = true) int readOnlyWithSetter;
	int writeOnly;

    String result;
}
