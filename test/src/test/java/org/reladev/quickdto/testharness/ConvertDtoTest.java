package org.reladev.quickdto.testharness;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.reladev.quickdto.testharness.impl.BasicImpl;
import org.reladev.quickdto.testharness.impl.ConvertImpl;
import org.reladev.quickdto.testharness.impl.ExistingParamImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class ConvertDtoTest {
    @Test
    public void testCopy() {
        ConvertImpl convert = new ConvertImpl();

        BasicImpl basic = new BasicImpl();
        basic.setText("basic");
        convert.setBasic(basic);
        convert.setBasicList(Arrays.asList(basic));

        ConvertDto convertDto = new ConvertDto();
        convertDto.copyFrom(convert);

        BasicDto basicDto = convertDto.getBasic();
        assertEquals("basic", basicDto.getText());

        List<BasicDto> basicList = convertDto.getBasicList();
        assertEquals(1, basicList.size());
        assertEquals("basic", basicList.get(0).getText());

        ConvertImpl newConvert = new ConvertImpl();
        convertDto.copyTo(newConvert);

        BasicImpl newBasic = newConvert.getBasic();
        assertNotNull(newBasic);
        assertEquals("basic", newBasic.getText());
        assertEquals(1, newConvert.getBasicList().size());
        assertEquals("basic", newConvert.getBasicList().get(0).getText());
    }

    @Test
    public void testNullCopy() {
        BasicImpl basic = new BasicImpl();
        basic.setText("foo");

        ConvertImpl convert = new ConvertImpl();
        convert.setBasic(basic);

        ConvertDto convertDto = new ConvertDto();
        convertDto.copyFrom(convert);

        BasicDto basicDto = convertDto.getBasic();
        assertEquals("foo", basicDto.getText());

        ConvertImpl newConvert = new ConvertImpl();
        convertDto.copyTo(newConvert);

        BasicImpl newBasic = newConvert.getBasic();
        assertNotNull(newBasic);
        assertEquals("foo", newBasic.getText());
    }

    @Test
    public void testExisting() {
        ConvertImpl convert = new ConvertImpl();

        ExistingParamImpl existingParam = new ExistingParamImpl();
        existingParam.setText("existingParam");
        convert.setExisting(existingParam);

        ConvertDto convertDto = new ConvertDto();
        convertDto.copyFrom(convert);

        ExistingParamDto existingDto = convertDto.getExisting();
        assertEquals("existingParam", existingDto.getText());

        existingParam.setText("changed");
        convertDto.copyFrom(convert);

        assertEquals("changed", existingDto.getText());

        existingDto.setText("changed2");
        convertDto.copyTo(convert);

        assertEquals("changed2", existingParam.getText());
    }

}
