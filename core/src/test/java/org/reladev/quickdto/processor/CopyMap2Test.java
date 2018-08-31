package org.reladev.quickdto.processor;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.reladev.quickdto.QuickDtoTest;

import static org.assertj.core.api.Assertions.assertThat;

public class CopyMap2Test extends QuickDtoTest {


    @Test
    public void verifyBasicTypes() {
        ClassDef2 targetDef = new ClassDef2(elementBasicTypesDtoDef);
        ClassDef2 sourceDef = new ClassDef2(elementBasicTypesImpl);

        CopyMap2 copyMap = new CopyMap2(sourceDef, targetDef, new ConverterMap());

        assertThat(copyMap.getSourceToTargetMappings()).hasSize(17);
        assertThat(copyMap.getTargetToSourceMappings()).hasSize(17);

    }

    @Test
    public void verifyBasicConvert() {
        ClassDef2 targetDef = new ClassDef2(elementBasicConvertDtoDef);
        ClassDef2 sourceDef = new ClassDef2(elementBasicConvertImpl);
        ConverterMap converterMap = new ConverterMap();
        converterMap.add(new ConverterMethod2(Integer.class, String.class));
        converterMap.add(new ConverterMethod2(String.class, Integer.class));
        converterMap.addAll(targetDef.getConverterMap());
        converterMap.addAll(sourceDef.getConverterMap());


        CopyMap2 copyMap = new CopyMap2(sourceDef, targetDef, converterMap);

        SoftAssertions.assertSoftly(soft -> soft.assertThat(copyMap.getSourceToTargetMappings())
                                                .hasSize(4)
                                                .containsKeys("basic", "basicList", "value", "toTargetOnly")
                                                .doesNotContainKeys("noConvert", "noConvertList"));

        SoftAssertions.assertSoftly(soft -> soft.assertThat(copyMap.getTargetToSourceMappings())
                                                .hasSize(3)
                                                .containsKeys("basic", "basicList", "value")
                                                .doesNotContainKeys("toTargetOnly", "noConvert", "noConvertList"));
    }

}