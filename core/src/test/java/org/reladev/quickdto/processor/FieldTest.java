package org.reladev.quickdto.processor;

import org.junit.Test;
import org.reladev.quickdto.QuickDtoTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class FieldTest extends QuickDtoTest {

    @Test
    public void verifyBasicTypeImplFields() {
        ClassDef basicTypeImpl = new ClassDef(elementBasicTypesImpl);

        assertEquals(17, basicTypeImpl.getNameFieldMap().size());

        for (Field field: basicTypeImpl.getNameFieldMap().values()) {
            assertTrue(field.hasGetter());
            assertTrue(field.hasSetter());
        }
    }

    @Test
    public void verifyBasicTypeDtoFields() {
        ClassDef basicTypeDto = new ClassDef(elementBasicTypesDtoDef);

        assertEquals(17, basicTypeDto.getNameFieldMap().size());

        for (Field field: basicTypeDto.getNameFieldMap().values()) {
            assertTrue(field.hasGetter());
            assertTrue(field.hasSetter());
            assertTrue(Character.isLowerCase(field.getName().charAt(0)));
            assertTrue(Character.isUpperCase(field.getAccessorName().charAt(0)));
        }
    }

    @Test
    public void verifyExclude() {
        ClassDef annotationDto = new ClassDef(elementAnnotationDtoDef);
        ClassDef annotationImpl = new ClassDef(elementAnnotationImpl);

        Field excludeField = annotationDto.getNameFieldMap().get("exclude");
        assertThat(excludeField).isNotNull();
        assertFalse(excludeField.isCopyGettable(annotationDto));
        assertFalse(excludeField.isCopyGettable(annotationDto));

        Field excludeFromImpl = annotationDto.getNameFieldMap().get("excludeFromImpl");
        assertThat(excludeFromImpl).isNotNull();
        assertTrue(excludeFromImpl.isCopyGettable(annotationDto));
        assertTrue(excludeFromImpl.isCopyGettable(annotationDto));
        assertFalse(excludeFromImpl.isCopyGettable(annotationImpl));
        assertFalse(excludeFromImpl.isCopyGettable(annotationImpl));
    }

}