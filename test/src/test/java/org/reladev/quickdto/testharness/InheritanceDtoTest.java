package org.reladev.quickdto.testharness;

import org.junit.Test;
import org.reladev.quickdto.testharness.impl.InheritanceImpl;

import static org.junit.Assert.assertEquals;


public class InheritanceDtoTest {
    @Test
    public void testCopy() {
        InheritanceImpl inheritance = new InheritanceImpl();
        InheritanceDto dto = new InheritanceDto();
        dto.copyFrom(inheritance);

        assertEquals(inheritance.getText(), dto.getText());
        assertEquals(inheritance.getBaseText(), dto.getBaseText());

        BaseInheritanceDto baseDto = dto;
        assertEquals(inheritance.getBaseText(), dto.getBaseText());

        dto.setText("bar");
        dto.setBaseText("bar");
        dto.copyTo(inheritance);
        assertEquals("bar", inheritance.getBaseText());
        assertEquals("bar", inheritance.getText());
    }
}
