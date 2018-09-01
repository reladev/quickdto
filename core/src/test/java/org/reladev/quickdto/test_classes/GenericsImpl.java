package org.reladev.quickdto.test_classes;

public class GenericsImpl extends GenericsBaseImpl<String, Integer> {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
