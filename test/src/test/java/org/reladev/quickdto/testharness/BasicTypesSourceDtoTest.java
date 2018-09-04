package org.reladev.quickdto.testharness;

import org.junit.Test;
import org.reladev.quickdto.testharness.impl.BasicTypesImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class BasicTypesSourceDtoTest {

    @Test
    public void testPrimitiveEmpty() {
        BasicTypesSourceDto dto = new BasicTypesSourceDto();

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

        BasicTypesImpl impl = new BasicTypesImpl();
        dto.copyTo(impl);

        assertEquals(0, impl.getMyInt());
        assertEquals(null, impl.getObjInteger());
        assertEquals(0, impl.getMyLong());
        assertEquals(null, impl.getObjLong());
        assertEquals(0, impl.getMyShort());
        assertEquals(null, impl.getObjShort());
        assertEquals(0, impl.getMyByte());
        assertEquals(null, impl.getObjByte());
        assertEquals(0, impl.getMyDouble(), 0);
        assertEquals(null, impl.getObjDouble());
        assertEquals(0, impl.getMyFloat(), 0);
        assertEquals(null, impl.getObjFloat());
        assertEquals(false, impl.isMyBoolean());
        assertEquals(null, impl.getObjBoolean());
        assertEquals(null, impl.getMyString());
    }

    @Test
    public void testTestCopyDefaults() {
        BasicTypesImpl impl = new BasicTypesImpl();
        BasicTypesImpl implDefaults = new BasicTypesImpl();

        impl.setMyInt(2);
        impl.setObjInteger(2);
        impl.setMyLong(2);
        impl.setObjLong((long) 2);
        impl.setMyShort((short) 2);
        impl.setObjShort((short) 2);
        impl.setMyByte((byte) 2);
        impl.setObjByte((byte) 2);
        impl.setMyDouble(2);
        impl.setObjDouble((double) 2);
        impl.setMyFloat(2);
        impl.setObjFloat((float) 2);
        impl.setMyBoolean(false);
        impl.setObjBoolean(true);
        impl.setMyString("2");


        BasicTypesDirtySourceDto dto = new BasicTypesDirtySourceDto();
        dto.copyFrom(impl);

        assertEquals(2, dto.getMyInt());
        assertEquals(new Integer(2), dto.getObjInteger());
        assertEquals(2, dto.getMyLong());
        assertEquals(new Long(2), dto.getObjLong());
        assertEquals(2, dto.getMyShort());
        assertEquals(new Short((short) 2), dto.getObjShort());
        assertEquals(2, dto.getMyByte());
        assertEquals(new Byte((byte) 2), dto.getObjByte());
        assertEquals(2, dto.getMyDouble(), 0);
        assertEquals(new Double((double) 2), dto.getObjDouble());
        assertEquals(2, dto.getMyFloat(), 0);
        assertEquals(new Float((float) 2), dto.getObjFloat());
        assertFalse(dto.isMyBoolean());
        assertEquals(Boolean.TRUE, dto.getObjBoolean());

        impl = new BasicTypesImpl();
        dto.copyTo(impl);

        assertEquals(2, impl.getMyInt());
        assertEquals(new Integer(2), impl.getObjInteger());
        assertEquals(2, impl.getMyLong());
        assertEquals(new Long(2), impl.getObjLong());
        assertEquals(2, impl.getMyShort());
        assertEquals(new Short((short) 2), impl.getObjShort());
        assertEquals(2, impl.getMyByte());
        assertEquals(new Byte((byte) 2), impl.getObjByte());
        assertEquals(2, impl.getMyDouble(), 0);
        assertEquals(new Double((double) 2), impl.getObjDouble());
        assertEquals(2, impl.getMyFloat(), 0);
        assertEquals(new Float((float) 2), impl.getObjFloat());
        assertFalse(impl.isMyBoolean());
        assertEquals(Boolean.TRUE, impl.getObjBoolean());

        dto.copyFrom(implDefaults);
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


}
