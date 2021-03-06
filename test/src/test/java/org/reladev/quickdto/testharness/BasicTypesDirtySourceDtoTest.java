package org.reladev.quickdto.testharness;

import org.junit.Test;
import org.reladev.quickdto.testharness.BasicTypesDirtySourceDto.Fields;

import static org.junit.Assert.*;


public class BasicTypesDirtySourceDtoTest {

    @Test
    public void testPrimitiveEmpty() {
        BasicTypesDirtySourceDto dto = new BasicTypesDirtySourceDto();

        assertEquals(0, dto.getMyInt());
        assertEquals(null, dto.getObjInteger());
        assertEquals(0, dto.getMyLong());
        assertEquals(null, dto.getObjLong());
        assertEquals(0, dto.getMyShort());
        assertEquals(null, dto.getObjShort());
        assertEquals(0, dto.getMyByte());
        assertEquals(null, dto.getObjByte());
        assertEquals(0, dto.getMyDouble(), 0);
        assertEquals(null, dto.getObjDouble());
        assertEquals(0, dto.getMyFloat(), 0);
        assertEquals(null, dto.getObjFloat());
        assertEquals(false, dto.isMyBoolean());
        assertEquals(null, dto.getObjBoolean());
        assertEquals(null, dto.getMyString());
    }

    @Test
    public void testPrimitiveDirty() {
        BasicTypesDirtySourceDto dto = new BasicTypesDirtySourceDto();

        assertFalse(dto.checkDirty(Fields.MY_INT));
        dto.setMyInt(0);
        assertTrue(dto.checkDirty(Fields.MY_INT));
        assertFalse(dto.checkDirty(Fields.OBJ_INTEGER));
        dto.setObjInteger(null);
        assertTrue(dto.checkDirty(Fields.OBJ_INTEGER));

        assertFalse(dto.checkDirty(Fields.MY_LONG));
        dto.setMyLong(0);
        assertTrue(dto.checkDirty(Fields.MY_LONG));
        assertFalse(dto.checkDirty(Fields.OBJ_LONG));
        dto.setObjLong(null);
        assertTrue(dto.checkDirty(Fields.OBJ_LONG));

        assertFalse(dto.checkDirty(Fields.MY_SHORT));
        dto.setMyShort((short) 0);
        assertTrue(dto.checkDirty(Fields.MY_SHORT));
        assertFalse(dto.checkDirty(Fields.OBJ_SHORT));
        dto.setObjShort(null);
        assertTrue(dto.checkDirty(Fields.OBJ_SHORT));

        assertFalse(dto.checkDirty(Fields.MY_BYTE));
        dto.setMyByte((byte) 0);
        assertTrue(dto.checkDirty(Fields.MY_BYTE));
        assertFalse(dto.checkDirty(Fields.OBJ_BYTE));
        dto.setObjByte(null);
        assertTrue(dto.checkDirty(Fields.OBJ_BYTE));

        assertFalse(dto.checkDirty(Fields.MY_DOUBLE));
        dto.setMyDouble(0);
        assertTrue(dto.checkDirty(Fields.MY_DOUBLE));
        assertFalse(dto.checkDirty(Fields.OBJ_DOUBLE));
        dto.setObjDouble(null);
        assertTrue(dto.checkDirty(Fields.OBJ_DOUBLE));

        assertFalse(dto.checkDirty(Fields.MY_FLOAT));
        dto.setMyFloat(0);
        assertTrue(dto.checkDirty(Fields.MY_FLOAT));
        assertFalse(dto.checkDirty(Fields.OBJ_FLOAT));
        dto.setObjFloat(null);
        assertTrue(dto.checkDirty(Fields.OBJ_FLOAT));

        assertFalse(dto.checkDirty(Fields.MY_BOOLEAN));
        dto.setMyBoolean(false);
        assertTrue(dto.checkDirty(Fields.MY_BOOLEAN));
        assertFalse(dto.checkDirty(Fields.OBJ_BOOLEAN));
        dto.setObjBoolean(null);
        assertTrue(dto.checkDirty(Fields.OBJ_BOOLEAN));

        assertFalse(dto.checkDirty(Fields.MY_STRING));
        dto.setMyString(null);
        assertTrue(dto.checkDirty(Fields.MY_STRING));
    }

    @Test
    public void testDirtyResetAssignSame() {
        BasicTypesDirtySourceDto dto = new BasicTypesDirtySourceDto();

        dto.setMyInt(2);
        dto.setObjInteger(2);
        dto.setMyLong(2);
        dto.setObjLong((long) 2);
        dto.setMyShort((short) 2);
        dto.setObjShort((short) 2);
        dto.setMyByte((byte) 2);
        dto.setObjByte((byte) 2);
        dto.setMyDouble(2);
        dto.setObjDouble((double) 2);
        dto.setMyFloat(2);
        dto.setObjFloat((float) 2);
        dto.setMyBoolean(false);
        dto.setObjBoolean(true);
        dto.setMyString("2");

        assertTrue(dto.checkDirty());
        dto.resetDirty();
        assertFalse(dto.checkDirty());

        assertFalse(dto.checkDirty(Fields.MY_INT));
        dto.setMyInt(2);
        assertFalse(dto.checkDirty(Fields.MY_INT));
        assertFalse(dto.checkDirty(Fields.OBJ_INTEGER));
        dto.setObjInteger(2);
        assertFalse(dto.checkDirty(Fields.OBJ_INTEGER));

        assertFalse(dto.checkDirty(Fields.MY_LONG));
        dto.setMyLong(2);
        assertFalse(dto.checkDirty(Fields.MY_LONG));
        assertFalse(dto.checkDirty(Fields.OBJ_LONG));
        dto.setObjLong((long) 2);
        assertFalse(dto.checkDirty(Fields.OBJ_LONG));

        assertFalse(dto.checkDirty(Fields.MY_SHORT));
        dto.setMyShort((short) 2);
        assertFalse(dto.checkDirty(Fields.MY_SHORT));
        assertFalse(dto.checkDirty(Fields.OBJ_SHORT));
        dto.setObjShort((short) 2);
        assertFalse(dto.checkDirty(Fields.OBJ_SHORT));

        assertFalse(dto.checkDirty(Fields.MY_BYTE));
        dto.setMyByte((byte) 2);
        assertFalse(dto.checkDirty(Fields.MY_BYTE));
        assertFalse(dto.checkDirty(Fields.OBJ_BYTE));
        dto.setObjByte((byte) 2);
        assertFalse(dto.checkDirty(Fields.OBJ_BYTE));

        assertFalse(dto.checkDirty(Fields.MY_DOUBLE));
        dto.setMyDouble(2);
        assertFalse(dto.checkDirty(Fields.MY_DOUBLE));
        assertFalse(dto.checkDirty(Fields.OBJ_DOUBLE));
        dto.setObjDouble((double) 2);
        assertFalse(dto.checkDirty(Fields.OBJ_DOUBLE));

        assertFalse(dto.checkDirty(Fields.MY_FLOAT));
        dto.setMyFloat(2);
        assertFalse(dto.checkDirty(Fields.MY_FLOAT));
        assertFalse(dto.checkDirty(Fields.OBJ_FLOAT));
        dto.setObjFloat((float) 2);
        assertFalse(dto.checkDirty(Fields.OBJ_FLOAT));

        assertFalse(dto.checkDirty(Fields.MY_BOOLEAN));
        dto.setMyBoolean(false);
        assertFalse(dto.checkDirty(Fields.MY_BOOLEAN));
        assertFalse(dto.checkDirty(Fields.OBJ_BOOLEAN));
        dto.setObjBoolean(true);
        assertFalse(dto.checkDirty(Fields.OBJ_BOOLEAN));

        assertFalse(dto.checkDirty(Fields.MY_STRING));
        dto.setMyString("2");
        assertFalse(dto.checkDirty(Fields.MY_STRING));
    }


}
