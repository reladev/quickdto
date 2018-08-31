package org.reladev.quickdto.processor;

import org.junit.Test;
import org.reladev.quickdto.QuickDtoTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FieldTest extends QuickDtoTest {

    @Test
    public void verifyBasicTypeImplFields() {
        ClassDef basicTypeImpl = new ClassDef(elementBasicTypesImpl);

        assertEquals(17, basicTypeImpl.getNameFieldMap().size());

        for (Field field: basicTypeImpl.getNameFieldMap().values()) {
            assertTrue(field.isGettable());
            assertTrue(field.isSettable());
        }
    }

    @Test
    public void verifyBasicTypeDtoFields() {
        ClassDef basicTypeDto = new ClassDef(elementBasicTypesDtoDef);

        assertEquals(17, basicTypeDto.getNameFieldMap().size());

        for (Field field: basicTypeDto.getNameFieldMap().values()) {
            assertTrue(field.isGettable());
            assertTrue(field.isSettable());
            assertTrue(Character.isLowerCase(field.getName().charAt(0)));
            assertTrue(Character.isUpperCase(field.getAccessorName().charAt(0)));
        }
    }

}