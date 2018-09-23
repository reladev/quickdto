package org.reladev.quickdto.testharness;

import org.junit.Test;
import org.reladev.quickdto.testharness.impl.InheritanceImpl;

import static org.junit.Assert.assertEquals;


public class InheritanceSourceDtoTest {
    @Test
    public void testCopy() {
        InheritanceImpl inheritance = new InheritanceImpl();
        InheritanceSourceDto dto = new InheritanceSourceDto();
        dto.copyFrom(inheritance);

        assertEquals(inheritance.getText(), dto.getText());
        assertEquals(inheritance.getBaseText(), dto.getBaseText());

        InheritanceBaseDto baseDto = dto;
        assertEquals(inheritance.getBaseText(), dto.getBaseText());

        dto.setText("bar");
        dto.setBaseText("bar");
        dto.copyTo(inheritance);
        assertEquals("bar", inheritance.getBaseText());
        assertEquals("bar", inheritance.getText());
    }
}
