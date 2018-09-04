package org.reladev.quickdto.testharness;

import org.junit.Test;
import org.reladev.quickdto.testharness.impl.AnnotationImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class AnnotationDtoTest {
    @Test
    public void testEquals() {
        AnnotationDto dto1 = new AnnotationDto();
        dto1.setEqualsHash(5);
        dto1.setResult("Not part of equals");

        AnnotationDto dto2 = new AnnotationDto();
        dto2.setEqualsHash(5);
        dto2.setResult("Different String not part of equals");

        assertTrue(dto1.equals(dto2));
        assertTrue(dto1.hashCode() == dto2.hashCode());

        dto1.setEqualsHash(3);
        assertFalse(dto1.equals(dto2));
    }

    @Test
    public void testReadOnly() {
        AnnotationImpl impl = new AnnotationImpl();
        impl.setReadOnly(4);

        AnnotationDto dto = new AnnotationDto();
        dto.copyFrom(impl);

        assertThat(dto.getReadOnly()).isEqualTo(4);

        try {
            dto.getClass().getMethod("setReadOnly", int.class);
            fail("setReadOnly() shouldn't exist.");
        } catch (NoSuchMethodException ignore) {
        }

        AnnotationImpl impl2 = new AnnotationImpl();
        impl2.setReadOnly(2);
        dto.copyTo(impl2);

        assertThat(impl2.getReadOnly()).isEqualTo(2);
    }

    @Test
    public void testReadOnlyWithSetter() {
        AnnotationImpl impl = new AnnotationImpl();
        impl.setReadOnlyWithSetter(4);

        AnnotationDto dto = new AnnotationDto();
        dto.copyFrom(impl);

        assertThat(dto.getReadOnlyWithSetter()).isEqualTo(4);

        try {
            dto.getClass().getMethod("setReadOnlyWithSetter", int.class);
        } catch (NoSuchMethodException ignore) {
            fail("setReadOnlyWithSetter() should exist.");
        }

        AnnotationImpl impl2 = new AnnotationImpl();
        impl2.setReadOnlyWithSetter(2);
        dto.copyTo(impl2);

        assertThat(impl2.getReadOnlyWithSetter()).isEqualTo(2);
    }

    @Test
    public void testWriteOnly() {
        AnnotationImpl impl = new AnnotationImpl();
        impl.setWriteOnly(4);

        AnnotationDto dto = new AnnotationDto();
        dto.copyFrom(impl);

        assertThat(dto.getWriteOnly()).isEqualTo(0);

        dto.setWriteOnly(5);
        dto.copyTo(impl);

        assertThat(impl.getWriteOnly()).isEqualTo(5);
    }
}
