package org.reladev.quickdto.processor;

import java.util.HashMap;

public class Source {
	String type;
	HashMap<String, Method> getters = new HashMap<>();
	HashMap<String, Method> setters = new HashMap<>();
}
