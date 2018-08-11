package org.reladev.quickdto.processor;

import java.util.HashMap;

public class Source {
    public String type;
    public HashMap<String, Method> getters = new HashMap<>();
    public HashMap<String, Method> setters = new HashMap<>();
}
