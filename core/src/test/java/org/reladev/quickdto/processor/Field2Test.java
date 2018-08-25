package org.reladev.quickdto.processor;

import org.junit.Test;
import org.reladev.quickdto.QuickDtoTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Field2Test extends QuickDtoTest {

    @Test
    public void verifyBasicTypeImplFields() {
        ClassDef2 basicTypeImpl = new ClassDef2(elementBasicTypesImpl);

        assertEquals(17, basicTypeImpl.getNameFieldMap().size());

        for (Field2 field : basicTypeImpl.getNameFieldMap().values()) {
            assertTrue(field.isGettable());
            assertTrue(field.isSettable());
        }
    }

    @Test
    public void verifyBasicTypeDtoFields() {
        ClassDef2 basicTypeDto = new ClassDef2(elementBasicTypesDtoDef);

        assertEquals(17, basicTypeDto.getNameFieldMap().size());

        for (Field2 field : basicTypeDto.getNameFieldMap().values()) {
            assertTrue(field.isGettable());
            assertTrue(field.isSettable());
            assertTrue(Character.isLowerCase(field.getName().charAt(0)));
            assertTrue(Character.isUpperCase(field.getAccessorName().charAt(0)));
        }
    }

}