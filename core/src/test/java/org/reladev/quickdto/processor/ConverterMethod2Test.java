package org.reladev.quickdto.processor;


import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;

import org.junit.Test;
import org.reladev.quickdto.QuickDtoTest;
import org.reladev.quickdto.test_classes.BasicConvertDtoDef;
import org.reladev.quickdto.test_classes.BasicTypesDtoDef;
import org.reladev.quickdto.test_classes.BasicTypesImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class ConverterMethod2Test extends QuickDtoTest {

    @Test
    public void verifyConvertMethods() {
        List<ConverterMethod2> converters = new ArrayList<>();
        for (Element subElement: elementBasicConvertDtoDef.getEnclosedElements()) {
            if (subElement.getKind() == ElementKind.METHOD) {
                ExecutableElement execElement = (ExecutableElement) subElement;
                ConverterMethod2 converter = ConverterMethod2.build(execElement, new Type(BasicConvertDtoDef.class));
                if (converter != null) {
                    converters.add(converter);
                }
            }
        }


        assertThat(converters).hasSize(2)
                              .contains(new ConverterMethod2(BasicTypesDtoDef.class, BasicTypesImpl.class))
                              .contains(
                                    new ConverterMethod2(new Type(List.class, BasicTypesDtoDef.class), new Type(List.class, BasicTypesImpl.class)));
    }
}