package org.reladev.quickdto.processor;


import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;

import org.junit.Test;
import org.reladev.quickdto.QuickDtoTest;
import org.reladev.quickdto.test_classes.BasicTypesImpl;
import org.reladev.quickdto.test_dto.BasicConvertDtoDef;
import org.reladev.quickdto.test_dto.BasicTypesDtoDef;

import static org.assertj.core.api.Assertions.assertThat;

public class ConverterMethodTest extends QuickDtoTest {

    @Test
    public void verifyConvertMethods() {
        List<ConverterMethod> converters = new ArrayList<>();
        for (Element subElement: elementBasicConvertDtoDef.getEnclosedElements()) {
            if (subElement.getKind() == ElementKind.METHOD) {
                ExecutableElement execElement = (ExecutableElement) subElement;
                ConverterMethod converter = ConverterMethod.build(execElement, new Type(BasicConvertDtoDef.class));
                if (converter != null) {
                    converters.add(converter);
                }
            }
        }


        assertThat(converters).hasSize(1).contains(new ConverterMethod(BasicTypesDtoDef.class, BasicTypesImpl.class));
    }
}