package org.reladev.quickdto.testharness;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.reladev.quickdto.testharness.impl.ConvertChildImpl;
import org.reladev.quickdto.testharness.impl.ConvertExistingImpl;
import org.reladev.quickdto.testharness.impl.ConvertImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class ConvertSourceDtoTest {
    @Test
    public void testCopy() {
        ConvertImpl convert = new ConvertImpl();

        ConvertChildImpl basic = new ConvertChildImpl();
        basic.setText("basic");
        convert.setChild(basic);
        convert.setChildList(Arrays.asList(basic));

        ConvertSourceDto convertDto = new ConvertSourceDto();
        convertDto.copyFrom(convert);

        ConvertChildSourceDto convertChildDto = convertDto.getChild();
        assertEquals("basic", convertChildDto.getText());

        List<ConvertChildSourceDto> basicList = convertDto.getChildList();
        assertEquals(1, basicList.size());
        assertEquals("basic", basicList.get(0).getText());

        ConvertImpl newConvert = new ConvertImpl();
        convertDto.copyTo(newConvert);

        ConvertChildImpl newBasic = newConvert.getChild();
        assertNotNull(newBasic);
        assertEquals("basic", newBasic.getText());
        assertEquals(1, newConvert.getChildList().size());
        assertEquals("basic", newConvert.getChildList().get(0).getText());
    }

    @Test
    public void testNullCopy() {
        ConvertChildImpl basic = new ConvertChildImpl();
        basic.setText("foo");

        ConvertImpl convert = new ConvertImpl();
        convert.setChild(basic);

        ConvertSourceDto convertDto = new ConvertSourceDto();
        convertDto.copyFrom(convert);

        ConvertChildSourceDto convertChildDto = convertDto.getChild();
        assertEquals("foo", convertChildDto.getText());

        ConvertImpl newConvert = new ConvertImpl();
        convertDto.copyTo(newConvert);

        ConvertChildImpl newBasic = newConvert.getChild();
        assertNotNull(newBasic);
        assertEquals("foo", newBasic.getText());
    }

    @Test
    public void testExisting() {
        ConvertImpl convert = new ConvertImpl();

        ConvertExistingImpl existingParam = new ConvertExistingImpl();
        existingParam.setText("existingParam");
        convert.setExisting(existingParam);

        ConvertSourceDto convertDto = new ConvertSourceDto();
        convertDto.copyFrom(convert);

        ConvertExistingSourceDto existingDto = convertDto.getExisting();
        assertEquals("existingParam", existingDto.getText());

        existingParam.setText("changed");
        convertDto.copyFrom(convert);
        existingDto = convertDto.getExisting();
        assertEquals("changed", existingDto.getText());

        existingDto.setText("changed2");
        convertDto.copyTo(convert);
        existingDto = convertDto.getExisting();
        assertEquals("changed2", existingParam.getText());
    }

}
