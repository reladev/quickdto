package org.reladev.quickdto.testharness;

import org.junit.Test;
import org.reladev.quickdto.testharness.SimpleDto.Fields;
import org.reladev.quickdto.testharness.impl.Simple;

import static org.junit.Assert.*;


public class SimpleDtoTest {

    @Test
    public void testMethodCreation() throws Exception {

        // Normal
        SimpleDto.class.getDeclaredMethod("getNormal");
        SimpleDto.class.getDeclaredMethod("setNormal", int.class);

        // ReadOnly
        SimpleDto.class.getDeclaredMethod("getReadOnly");
        try {
            SimpleDto.class.getDeclaredMethod("setReadOnly", int.class);
            fail();
        } catch (NoSuchMethodException ignored) {
        }

        // ReadOnlyWithSetter
        SimpleDto.class.getDeclaredMethod("getReadOnlyWithSetter");
        SimpleDto.class.getDeclaredMethod("setReadOnlyWithSetter", int.class);

        // WriteOnly
        SimpleDto.class.getDeclaredMethod("getWriteOnly");
        SimpleDto.class.getDeclaredMethod("setWriteOnly", int.class);

    }

    @Test
    public void testCopy() {
        Simple from = new Simple();
        from.setNormal(1);
        from.setReadOnly(1);
        from.setReadOnlyWithSetter(1);
        from.setWriteOnly(1);

        SimpleDto dto = new SimpleDto();

        dto.copyFrom(from);
        assertFalse(dto.checkDirty());
        assertEquals(1, dto.getReadOnly());
        assertEquals(1, dto.getReadOnly());
        assertEquals(1, dto.getReadOnly());
        assertEquals(0, dto.getWriteOnly());

        Simple toClean = new Simple();
        assertEquals(0, toClean.getNormal());
        assertEquals(0, toClean.getReadOnly());
        assertEquals(0, toClean.getReadOnlyWithSetter());
        assertEquals(0, toClean.checkWriteOnly());

        dto.setNormal(2);
        dto.setReadOnlyWithSetter(2);
        dto.setWriteOnly(2);
        dto.markDirty(true, Fields.READ_ONLY);

        Simple toDirty = new Simple();
        dto.copyTo(toDirty);
        assertEquals(2, toDirty.getNormal());
        assertEquals(0, toDirty.getReadOnly());
        assertEquals(0, toDirty.getReadOnlyWithSetter());
        assertEquals(2, toDirty.checkWriteOnly());

    }


}
