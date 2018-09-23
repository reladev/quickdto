//package org.reladev.quickdto.testharness.impl;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.Test;
//import org.reladev.quickdto.testharness.ConvertChildDto;
//import org.reladev.quickdto.testharness.ConvertDto;
//import org.reladev.quickdto.testharness.ExistingParamDto;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//
//public class ConvertImplCopyUtilTest {
//    @Test
//    public void testCopy() {
//        ConvertImpl convert = new ConvertImpl();
//
//        BasicImpl basic = new BasicImpl();
//        basic.setText("basic");
//        convert.setBasic(basic);
//        convert.setBasicList(Arrays.asList(basic));
//
//        ConvertDto convertDto = new ConvertDto();
//        ConvertImplCopyUtil.copy(convert, convertDto);
//
//        ConvertChildDto convertChildDto = convertDto.getBasic();
//        assertEquals("basic", convertChildDto.getText());
//
//        List<ConvertChildDto> basicList = convertDto.getBasicList();
//        assertEquals(1, basicList.size());
//        assertEquals("basic", basicList.get(0).getText());
//
//        ConvertImpl newConvert = new ConvertImpl();
//        ConvertImplCopyUtil.copy(convertDto, newConvert);
//
//        BasicImpl newBasic = newConvert.getBasic();
//        assertNotNull(newBasic);
//        assertEquals("basic", newBasic.getText());
//        assertEquals(1, newConvert.getBasicList().size());
//        assertEquals("basic", newConvert.getBasicList().get(0).getText());
//    }
//
//    @Test
//    public void testNullCopy() {
//        BasicImpl basic = new BasicImpl();
//        basic.setText("foo");
//
//        ConvertImpl convert = new ConvertImpl();
//        convert.setBasic(basic);
//
//        ConvertDto convertDto = new ConvertDto();
//        ConvertImplCopyUtil.copy(convert, convertDto);
//
//        ConvertChildDto convertChildDto = convertDto.getBasic();
//        assertEquals("foo", convertChildDto.getText());
//
//        ConvertImpl newConvert = new ConvertImpl();
//        ConvertImplCopyUtil.copy(convertDto, newConvert);
//
//        BasicImpl newBasic = newConvert.getBasic();
//        assertNotNull(newBasic);
//        assertEquals("foo", newBasic.getText());
//    }
//
//    @Test
//    public void testExisting() {
//        ConvertImpl convert = new ConvertImpl();
//
//        ExistingParamImpl existingParam = new ExistingParamImpl();
//        existingParam.setText("existingParam");
//        convert.setExisting(existingParam);
//
//        ConvertDto convertDto = new ConvertDto();
//        ConvertImplCopyUtil.copy(convert, convertDto);
//
//        ExistingParamDto existingDto = convertDto.getExisting();
//        assertEquals("existingParam", existingDto.getText());
//
//        existingParam.setText("changed");
//        ConvertImplCopyUtil.copy(convert, convertDto);
//
//        assertEquals("changed", existingDto.getText());
//
//        existingDto.setText("changed2");
//        ConvertImplCopyUtil.copy(convertDto, convert);
//
//        assertEquals("changed2", existingParam.getText());
//    }
//
//}
