package org.reladev.quickdto.processor;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.reladev.quickdto.QuickDtoTest;

import static org.assertj.core.api.Assertions.assertThat;

public class CopyMapTest extends QuickDtoTest {


    @Test
    public void verifyBasicTypes() {
        ClassDef targetDef = new ClassDef(elementBasicTypesDtoDef);
        ClassDef sourceDef = new ClassDef(elementBasicTypesImpl);

        CopyMap copyMap = new CopyMap(sourceDef, targetDef, new ConverterMap());

        assertThat(copyMap.getSourceToTargetMappings()).hasSize(17);
        assertThat(copyMap.getTargetToSourceMappings()).hasSize(17);

    }

    @Test
    public void verifyBasicConvert() {
        ClassDef targetDef = new ClassDef(elementBasicConvertDtoDef);
        ClassDef sourceDef = new ClassDef(elementBasicConvertImpl);
        ConverterMap converterMap = new ConverterMap();
        converterMap.add(new ConverterMethod(Integer.class, String.class));
        converterMap.add(new ConverterMethod(String.class, Integer.class));
        converterMap.addAll(targetDef.getConverterMap());
        converterMap.addAll(sourceDef.getConverterMap());


        CopyMap copyMap = new CopyMap(sourceDef, targetDef, converterMap);

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