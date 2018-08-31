package org.reladev.quickdto.processor;

import java.util.List;

import org.junit.Test;
import org.reladev.quickdto.QuickDtoTest;
import org.reladev.quickdto.test_classes.BasicTypesImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassDefTest extends QuickDtoTest {

    @Test
    public void verifyConvertMap() {
        ClassDef classDef = new ClassDef(elementBasicConvertDtoDef);

        assertThat(classDef.getConverterMap()
                           .getMap()).hasSize(2)
                                     .containsKeys(new Type(BasicTypesImpl.class), new Type(List.class, BasicTypesImpl.class));


        System.out.println();
    }

}