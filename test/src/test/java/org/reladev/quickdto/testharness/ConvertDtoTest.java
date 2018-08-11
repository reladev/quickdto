package org.reladev.quickdto.testharness;

import org.junit.Test;
import org.reladev.quickdto.testharness.impl.Basic;
import org.reladev.quickdto.testharness.impl.Convert;

import static org.junit.Assert.*;


public class ConvertDtoTest {
    @Test
    public void testCopy() {
        Basic basic = new Basic();
        basic.setText("foo");

        Convert convert = new Convert();
        convert.setBasic(basic);

        ConvertDto convertDto = new ConvertDto();
        convertDto.copyFrom(convert);

        BasicDto basicDto = convertDto.getBasic();

        assertFalse(convertDto.checkDirty());
        assertFalse(basicDto.checkDirty());
        assertEquals("foo", basicDto.getText());

        convertDto.markDirty(true, ConvertDto.Fields.values());
        basicDto.markDirty(true, BasicDto.Fields.values());

        Convert newConvert = new Convert();
        convertDto.copyTo(newConvert);

        Basic newBasic = newConvert.getBasic();
        assertNotNull(newBasic);
        assertEquals("foo", newBasic.getText());
    }
}
