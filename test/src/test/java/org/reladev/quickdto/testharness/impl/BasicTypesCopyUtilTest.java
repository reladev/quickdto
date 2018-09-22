package org.reladev.quickdto.testharness.impl;

import org.junit.Test;
import org.reladev.quickdto.testharness.BasicTypesDto;

import static org.junit.Assert.assertEquals;


public class BasicTypesCopyUtilTest {

    @Test
    public void testCopy() {
        BasicTypesImpl impl = new BasicTypesImpl();
        impl.myByte = 2;
        impl.objByte = 2;
        impl.myShort = 2;
        impl.objShort = 2;
        impl.myChar = 2;
        impl.objCharacter = 2;
        impl.myInt = 2;
        impl.objInteger = 2;
        impl.myLong = 2;
        impl.objLong = 2L;
        impl.myFloat = 2;
        impl.objFloat = 2f;
        impl.myDouble = 2;
        impl.objDouble = 2d;
        impl.myBoolean = true;
        impl.objBoolean = true;
        impl.myString = "2";

        BasicTypesDto dto = new BasicTypesDto();
        BasicTypesImplCopyUtil.copy(impl, dto);

        assertEquals(2, dto.getMyByte());
        assertEquals(new Byte((byte) 2), dto.getObjByte());
        assertEquals(2, dto.getMyShort());
        assertEquals(new Short((short) 2), dto.getObjShort());
        assertEquals(2, dto.getMyChar());
        assertEquals(new Character((char) 2), dto.getObjCharacter());
        assertEquals(2, dto.getMyInt());
        assertEquals(new Integer(2), dto.getObjInteger());
        assertEquals(2, dto.getMyLong());
        assertEquals(new Long(2), dto.getObjLong());
        assertEquals(2, dto.getMyFloat(), 0f);
        assertEquals(new Float(2), dto.getObjFloat());
        assertEquals(2, dto.getMyDouble(), 0d);
        assertEquals(new Double(2), dto.getObjDouble());
        assertEquals(true, dto.isMyBoolean());
        assertEquals(Boolean.TRUE, dto.getObjBoolean());
        assertEquals("2", dto.getMyString());


        impl.myByte = 3;
        impl.objByte = 3;
        impl.myShort = 3;
        impl.objShort = 3;
        impl.myChar = 3;
        impl.objCharacter = 3;
        impl.myInt = 3;
        impl.objInteger = 3;
        impl.myLong = 3;
        impl.objLong = 3L;
        impl.myFloat = 3;
        impl.objFloat = 3f;
        impl.myDouble = 3;
        impl.objDouble = 3d;
        impl.myBoolean = false;
        impl.objBoolean = false;
        impl.myString = "3";

        BasicTypesImplCopyUtil.copy(dto, impl);

        assertEquals(2, impl.getMyByte());
        assertEquals(new Byte((byte) 2), impl.getObjByte());
        assertEquals(2, impl.getMyShort());
        assertEquals(new Short((short) 2), impl.getObjShort());
        assertEquals(2, impl.getMyChar());
        assertEquals(new Character((char) 2), impl.getObjCharacter());
        assertEquals(2, impl.getMyInt());
        assertEquals(new Integer(2), impl.getObjInteger());
        assertEquals(2, impl.getMyLong());
        assertEquals(new Long(2), impl.getObjLong());
        assertEquals(2, impl.getMyFloat(), 0f);
        assertEquals(new Float(2), impl.getObjFloat());
        assertEquals(2, impl.getMyDouble(), 0d);
        assertEquals(new Double(2), impl.getObjDouble());
        assertEquals(true, impl.getObjBoolean());
        assertEquals(Boolean.TRUE, impl.getObjBoolean());
        assertEquals("2", impl.getMyString());

    }


}
