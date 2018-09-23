package org.reladev.quickdto.test_classes;


public class StringIntConverter {

    public static Integer convert(String string) {
        if (string == null) {
            return null;
        }
        return Integer.parseInt(string);
    }

    public static String convert(Integer integer) {
        return integer.toString();
    }
}
