package org.reladev.quickdto.testharness;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;
import org.reladev.quickdto.testharness.impl.CollectionsImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;


public class CollectionsSourceDtoTest {

    @Test
    public void testCopyNulls() {
        CollectionsImpl impl = new CollectionsImpl();

        CollectionsSourceDto dto = new CollectionsSourceDto();
        dto.copyFrom(impl);

        assertNull(dto.getIntArray());
        assertNull(dto.getFloatArray());
        assertNull(dto.getBooleanArray());
        assertNull(dto.getStringArray());
        assertNull(dto.getIntegerList());
        assertNull(dto.getFloatList());
        assertNull(dto.getBooleanList());
        assertNull(dto.getStringList());
        assertNull(dto.getIntegerSet());
        assertNull(dto.getFloatSet());
        assertNull(dto.getBooleanSet());
        assertNull(dto.getStringSet());
        assertNull(dto.getIntegerMap());
        assertNull(dto.getFloatMap());
        assertNull(dto.getBooleanMap());
        assertNull(dto.getStringMap());

        dto.copyTo(impl);

        assertNull(impl.getIntArray());
        assertNull(impl.getFloatArray());
        assertNull(impl.getBooleanArray());
        assertNull(impl.getStringArray());
        assertNull(impl.getIntegerList());
        assertNull(impl.getFloatList());
        assertNull(impl.getBooleanList());
        assertNull(impl.getStringList());
        assertNull(impl.getIntegerSet());
        assertNull(impl.getFloatSet());
        assertNull(impl.getBooleanSet());
        assertNull(impl.getStringSet());
        assertNull(impl.getIntegerMap());
        assertNull(impl.getFloatMap());
        assertNull(impl.getBooleanMap());
        assertNull(impl.getStringMap());

    }

    @Test
    public void testTestCopy() {
        CollectionsImpl impl = new CollectionsImpl();

        impl.setIntArray(new int[]{1, 2, 3});
        impl.setFloatArray(new float[]{1.2f, 2.3f});
        impl.setBooleanArray(new boolean[]{true, false});
        impl.setStringArray(new String[]{"foo", "bar"});
        impl.setIntegerList(Arrays.asList(1, 2, 3));
        impl.setFloatList(Arrays.asList(1.2f, 2.3f));
        impl.setBooleanList(Arrays.asList(Boolean.TRUE, Boolean.FALSE));
        impl.setStringList(Arrays.asList("foo", "bar"));
        impl.setIntegerSet(new HashSet<>(Arrays.asList(1, 2, 3)));
        impl.setFloatSet(new HashSet<>(Arrays.asList(1.2f, 2.3f)));
        impl.setBooleanSet(new HashSet<>(Arrays.asList(Boolean.TRUE, Boolean.FALSE)));
        impl.setStringSet(new HashSet<>(Arrays.asList("foo", "bar")));
        impl.setIntegerMap(new HashMap<Integer, Integer>() {{
            put(1, 2);
        }});
        impl.setFloatMap(new HashMap<Float, Float>() {{
            put(1.2f, 2.3f);
        }});
        impl.setBooleanMap(new HashMap<Boolean, Boolean>() {{
            put(Boolean.TRUE, Boolean.FALSE);
        }});
        impl.setStringMap(new HashMap<String, String>() {{
            put("foo", "bar");
        }});

        CollectionsSourceDto dto = new CollectionsSourceDto();
        dto.copyFrom(impl);

        assertThat(dto.getIntArray()).containsExactly(1, 2, 3);
        assertThat(dto.getFloatArray()).containsExactly(1.2f, 2.3f);
        assertThat(dto.getBooleanArray()).containsExactly(true, false);
        assertThat(dto.getStringArray()).containsExactly("foo", "bar");
        assertThat(dto.getIntegerList()).containsExactly(1, 2, 3);
        assertThat(dto.getFloatList()).containsExactly(1.2f, 2.3f);
        assertThat(dto.getBooleanList()).containsExactly(Boolean.TRUE, Boolean.FALSE);
        assertThat(dto.getStringList()).containsExactly("foo", "bar");
        assertThat(dto.getIntegerSet()).containsExactlyInAnyOrder(1, 2, 3);
        assertThat(dto.getFloatSet()).containsExactlyInAnyOrder(1.2f, 2.3f);
        assertThat(dto.getBooleanSet()).containsExactlyInAnyOrder(Boolean.TRUE, Boolean.FALSE);
        assertThat(dto.getStringSet()).containsExactlyInAnyOrder("foo", "bar");
        assertThat(dto.getIntegerMap()).hasSize(1)
                                       .contains(new SimpleImmutableEntry<>(1, 2));
        assertThat(dto.getFloatMap()).hasSize(1)
                                     .contains(new SimpleImmutableEntry<>(1.2f, 2.3f));
        assertThat(dto.getBooleanMap()).hasSize(1)
                                       .contains(new SimpleImmutableEntry<>(Boolean.TRUE, Boolean.FALSE));
        assertThat(dto.getStringMap()).hasSize(1)
                                      .contains(new SimpleImmutableEntry<>("foo", "bar"));

        impl = new CollectionsImpl();
        dto.copyTo(impl);

        assertThat(impl.getIntArray()).containsExactly(1, 2, 3);
        assertThat(impl.getFloatArray()).containsExactly(1.2f, 2.3f);
        assertThat(impl.getBooleanArray()).containsExactly(true, false);
        assertThat(impl.getStringArray()).containsExactly("foo", "bar");
        assertThat(impl.getIntegerList()).containsExactly(1, 2, 3);
        assertThat(impl.getFloatList()).containsExactly(1.2f, 2.3f);
        assertThat(impl.getBooleanList()).containsExactly(Boolean.TRUE, Boolean.FALSE);
        assertThat(impl.getStringList()).containsExactly("foo", "bar");
        assertThat(impl.getIntegerSet()).containsExactlyInAnyOrder(1, 2, 3);
        assertThat(impl.getFloatSet()).containsExactlyInAnyOrder(1.2f, 2.3f);
        assertThat(impl.getBooleanSet()).containsExactlyInAnyOrder(Boolean.TRUE, Boolean.FALSE);
        assertThat(impl.getStringSet()).containsExactlyInAnyOrder("foo", "bar");
        assertThat(impl.getIntegerMap()).hasSize(1)
                                        .contains(new SimpleImmutableEntry<>(1, 2));
        assertThat(impl.getFloatMap()).hasSize(1)
                                      .contains(new SimpleImmutableEntry<>(1.2f, 2.3f));
        assertThat(impl.getBooleanMap()).hasSize(1)
                                        .contains(new SimpleImmutableEntry<>(Boolean.TRUE, Boolean.FALSE));
        assertThat(impl.getStringMap()).hasSize(1)
                                       .contains(new SimpleImmutableEntry<>("foo", "bar"));


    }


}
