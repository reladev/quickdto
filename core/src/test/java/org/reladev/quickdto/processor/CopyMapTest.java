package org.reladev.quickdto.processor;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.reladev.quickdto.QuickDtoTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
              .hasSize(3)
              .containsKeys("basic", "basicList", "value"));

        CopyMapping mapping = copyMap.getTargetToSourceMappings().get("basicList");
        assertNotNull(mapping);
        assertTrue(mapping.isCollectionConvert());

        SoftAssertions.assertSoftly(soft -> soft.assertThat(copyMap.getTargetToSourceMappings())
              .hasSize(3)
              .containsKeys("basic", "basicList", "value"));
    }

    @Test
    public void verifyMappingErrors() {
        ClassDef targetDef = new ClassDef(elementMappingErrorsDtoDef);
        ClassDef sourceDef = new ClassDef(elementMappingErrorsImpl);
        ConverterMap converterMap = new ConverterMap();

        CopyMap copyMap = new CopyMap(sourceDef, targetDef, converterMap);
        CopyMapping mapping;

        mapping = copyMap.getSourceToTargetMappings().get("sourceNoField");
        assertThat(mapping.getErrorMessage()).isNotNull();
        mapping = copyMap.getTargetToSourceMappings().get("sourceNoField");
        assertThat(mapping.getErrorMessage()).isNotNull();

        mapping = copyMap.getSourceToTargetMappings().get("typeMismatch");
        assertThat(mapping.getErrorMessage()).isNotNull();
        mapping = copyMap.getTargetToSourceMappings().get("typeMismatch");
        assertThat(mapping.getErrorMessage()).isNotNull();

        mapping = copyMap.getSourceToTargetMappings().get("typeMismatchList");
        assertThat(mapping.getErrorMessage()).isNotNull();
        mapping = copyMap.getTargetToSourceMappings().get("typeMismatchList");
        assertThat(mapping.getErrorMessage()).isNotNull();

        mapping = copyMap.getSourceToTargetMappings().get("targetNoGetter");
        assertThat(mapping.getErrorMessage()).isNull();
        mapping = copyMap.getTargetToSourceMappings().get("targetNoGetter");
        assertThat(mapping.getErrorMessage()).isNotNull();

        mapping = copyMap.getSourceToTargetMappings().get("targetNoSetter");
        assertThat(mapping.getErrorMessage()).isNotNull();
        mapping = copyMap.getTargetToSourceMappings().get("targetNoSetter");
        assertThat(mapping.getErrorMessage()).isNull();

        mapping = copyMap.getSourceToTargetMappings().get("sourceNoGetter");
        assertThat(mapping.getErrorMessage()).isNotNull();
        mapping = copyMap.getTargetToSourceMappings().get("sourceNoGetter");
        assertThat(mapping.getErrorMessage()).isNull();

        mapping = copyMap.getSourceToTargetMappings().get("sourceNoSetter");
        assertThat(mapping.getErrorMessage()).isNull();
        mapping = copyMap.getTargetToSourceMappings().get("sourceNoSetter");
        assertThat(mapping.getErrorMessage()).isNotNull();

        assertThat(copyMap.getSourceToTargetMappings().get("targetNoField")).isNull();
        assertThat(copyMap.getTargetToSourceMappings().get("targetNoField")).isNull();

        assertThat(copyMap.getSourceToTargetMappings().get("fieldTargetExclude")).isNull();
        assertThat(copyMap.getTargetToSourceMappings().get("fieldTargetExclude")).isNull();

        assertThat(copyMap.getSourceToTargetMappings().get("targetNoGetterTargetExcludeTo")).isNotNull();
        assertThat(copyMap.getTargetToSourceMappings().get("targetNoGetterTargetExcludeTo")).isNull();

        assertThat(copyMap.getSourceToTargetMappings().get("targetNoSetterTargetExcludeFrom")).isNull();
        assertThat(copyMap.getTargetToSourceMappings().get("targetNoSetterTargetExcludeFrom")).isNotNull();

        assertThat(copyMap.getSourceToTargetMappings().get("sourceNoGetterTargetExcludeFrom")).isNull();
        assertThat(copyMap.getTargetToSourceMappings().get("sourceNoGetterTargetExcludeFrom")).isNotNull();

        assertThat(copyMap.getSourceToTargetMappings().get("sourceNoSetterTargetExcludeTo")).isNotNull();
        assertThat(copyMap.getTargetToSourceMappings().get("sourceNoSetterTargetExcludeTo")).isNull();

        assertThat(copyMap.getSourceToTargetMappings().get("fieldSourceExclude")).isNull();
        assertThat(copyMap.getTargetToSourceMappings().get("fieldSourceExclude")).isNull();

        assertThat(copyMap.getSourceToTargetMappings().get("targetNoGetterSourceExcludeTo")).isNotNull();
        assertThat(copyMap.getTargetToSourceMappings().get("targetNoGetterSourceExcludeTo")).isNull();

        assertThat(copyMap.getSourceToTargetMappings().get("targetNoSetterSourceExcludeFrom")).isNull();
        assertThat(copyMap.getTargetToSourceMappings().get("targetNoSetterSourceExcludeFrom")).isNotNull();

        assertThat(copyMap.getSourceToTargetMappings().get("sourceNoGetterSourceExcludeFrom")).isNull();
        assertThat(copyMap.getTargetToSourceMappings().get("sourceNoGetterSourceExcludeFrom")).isNotNull();

        assertThat(copyMap.getSourceToTargetMappings().get("sourceNoSetterSourceExcludeTo")).isNotNull();
        assertThat(copyMap.getTargetToSourceMappings().get("sourceNoSetterSourceExcludeTo")).isNull();
    }

    @Test
    public void verifyAnnotationMaps() {
        ClassDef targetDef = new ClassDef(elementAnnotationDtoDef);
        ClassDef sourceDef = new ClassDef(elementAnnotationImpl);
        ConverterMap converterMap = new ConverterMap();

        CopyMap copyMap = new CopyMap(sourceDef, targetDef, converterMap);

        SoftAssertions.assertSoftly(soft -> soft.assertThat(copyMap.getSourceToTargetMappings())
              .hasSize(3)
              .containsKeys("equalsHash", "readOnly", "result"));

        SoftAssertions.assertSoftly(soft -> soft.assertThat(copyMap.getTargetToSourceMappings())
              .hasSize(3)
              .containsKeys("equalsHash", "writeOnly", "result"));
    }

}