package org.reladev.quickdto.processor;

import java.util.AbstractMap.SimpleEntry;

import org.junit.Test;
import org.reladev.quickdto.QuickDtoTest;
import org.reladev.quickdto.test_classes.BasicTypesImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassDefTest extends QuickDtoTest {

    @Test
    public void verifyConvertMap() {
        ClassDef classDef = new ClassDef(elementBasicConvertDtoDef);

        assertThat(classDef.getConverterMap().getMap()).hasSize(1).containsKeys(new Type(BasicTypesImpl.class));
    }


    @Test
    public void verifyGenericsMap() {
        ClassDef classDef = new ClassDef(elementGenericsImpl);

        assertThat(classDef.getGenericsMap()).hasSize(2)
              .contains(new SimpleEntry<>(new Type("", "T"), new Type(String.class)))
              .contains(new SimpleEntry<>(new Type("", "K"), new Type(Integer.class)));
    }

}