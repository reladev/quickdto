package com.github.quickdto;

public class JavaTypeUtil {
    private static final String TYPE_PARSE = "avaType\"";

    public static void main(String[] args) {

    }

    private static String extractJavaType(String json) {
        int nesting = 0;
        if (json.charAt(0) != '{') {
            throw new IllegalStateException("Expected a Json Object");
        }
        for (int i = 1; i < json.length(); i++) {
            char chr = json.charAt(i);
            if (chr == '{') {
                nesting++;
            } else if (chr == '>') {
                nesting--;
            } else if (chr == 'j' && nesting == 0) {
                String type = parseProperty(json, i);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

    private static String parseProperty(String json, int i) {
        for (int j = 0; j < TYPE_PARSE.length(); j++, i++) {
            if (json.charAt(i) != TYPE_PARSE.charAt(j)) {
                return null;
            }
        }
        i = consumeWhitespace(json, i);
        if (json.charAt(i) != ':') {
            return null;
        } else {
            i++;
        }
        i = consumeWhitespace(json, i);
        if (json.charAt(i) != '"') {
            return null;
        } else {
            i++;
        }
        int start = i;
        while (json.charAt(i) != '"') {
            i++;
        }

        return json.substring(start, i);
    }

    private static int consumeWhitespace(String json, int i) {
        while (json.charAt(i) == ' ') {
            i++;
        }
        return i;
    }

}
