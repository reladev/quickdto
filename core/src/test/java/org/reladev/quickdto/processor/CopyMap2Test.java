package org.reladev.quickdto.processor;

import org.junit.Test;
import org.reladev.quickdto.QuickDtoTest;

import static org.junit.Assert.assertEquals;

public class CopyMap2Test extends QuickDtoTest {


    @Test
    public void verify() {
        ClassDef2 targetDef = new ClassDef2(elementBasicTypesDtoDef);
        ClassDef2 sourceDef = new ClassDef2(elementBasicTypesImpl);

        CopyMap2 copyMap = new CopyMap2(sourceDef, targetDef, new ConverterMap());

        assertEquals(17, copyMap.getSourceToTargetMappings().size());
        assertEquals(17, copyMap.getTargetToSourceMappings().size());

    }

}