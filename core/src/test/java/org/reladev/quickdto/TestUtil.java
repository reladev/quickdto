package org.reladev.quickdto;

import java.util.function.Consumer;

import org.assertj.core.api.SoftAssertions;

public class TestUtil {
    public static void soft(Consumer<SoftAssertions> test) {
        SoftAssertions softAssertions = new SoftAssertions();
        test.accept(softAssertions);
        softAssertions.assertAll();
    }
}
