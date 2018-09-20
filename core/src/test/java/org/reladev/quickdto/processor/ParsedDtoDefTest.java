package org.reladev.quickdto.processor;

import java.util.List;

import org.junit.Test;
import org.reladev.quickdto.QuickDtoTest;
import org.reladev.quickdto.test_classes.BasicTypesImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class ParsedDtoDefTest extends QuickDtoTest {
    @Test
    public void verifyCopyMapForBasicConvert() {
        ParsedDtoDef parsedDtoDef = new ParsedDtoDef(elementBasicConvertDtoDef);

        assertEquals(1, parsedDtoDef.getCopyMaps()
                                    .size());

        assertThat(parsedDtoDef.getConverterMap()
                               .getMap()).hasSize(4)
                                         .containsKeys(new Type(String.class))
                                         .containsKeys(new Type(Integer.class))
                                         .containsKeys(new Type(BasicTypesImpl.class))
                                         .containsKeys(new Type(List.class, BasicTypesImpl.class));

        CopyMap copyMap = parsedDtoDef.getCopyMaps()
                                        .get(0);
        assertEquals(3, copyMap.getSourceToTargetMappings()
                                .size());
        assertEquals(3, copyMap.getTargetToSourceMappings()
                                .size());

        System.out.println();

    }

}