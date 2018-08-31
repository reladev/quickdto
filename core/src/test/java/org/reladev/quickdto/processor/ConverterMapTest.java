package org.reladev.quickdto.processor;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConverterMapTest {
    @Test
    public void verifyAdd() {
        ConverterMap map = new ConverterMap();

        map.add(new ConverterMethod2(new Type(Integer.class), new Type(String.class)));
        assertThat(map.getMap()).hasSize(1)
                                .containsOnlyKeys(new Type(String.class));
        assertThat(map.getMap()
                      .get(new Type(String.class))).hasSize(1)
                                                   .contains(new ConverterMethod2(new Type(Integer.class), new Type(String.class)));


        map.add(new ConverterMethod2(new Type(Integer.class), new Type(String.class)));
        assertThat(map.getMap()).hasSize(1)
                                .containsOnlyKeys(new Type(String.class));
        assertThat(map.getMap()
                      .get(new Type(String.class))).hasSize(1)
                                                   .contains(new ConverterMethod2(new Type(Integer.class), new Type(String.class)));

        map.add(new ConverterMethod2(new Type(Float.class), new Type(String.class)));
        assertThat(map.getMap()).hasSize(1)
                                .containsOnlyKeys(new Type(String.class));
        assertThat(map.getMap()
                      .get(new Type(String.class))).hasSize(2)
                                                   .contains(new ConverterMethod2(new Type(Integer.class), new Type(String.class)))
                                                   .contains(new ConverterMethod2(new Type(Float.class), new Type(String.class)));

    }

}